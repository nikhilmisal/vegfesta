package in.co.powerusers.vegfesta;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Powerusers on 19-11-2017.
 */

public class RVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Vegetable> vegs;
    private Callbacks mCallbacks;
    private int cnt = 0;
    private String _imageURL;
    private Context context;
    private static final String TAG = "RVAdapter";
    private DbConn db;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private OnLoadMoreListner mOnLoadMoreListner;
    private boolean isLoading;
    private int lastVisibleItem,totalItemCount;
    private int visibleThreshold = 4;

    public class ViewHolder extends RecyclerView.ViewHolder{
        CardView _cv;
        TextView _vegName;
        TextView _vegDesc;
        TextView _vegPrice;
        TextView _qty;
        //TextView _vid;
        ImageView _vegImage;
        Button _addBtn,_removeBtn,_wtBtn;
        RelativeLayout _cvLayout;


        public ViewHolder(View v){
            super(v);
            _cv = (CardView)v.findViewById(R.id.cv);
            _vegName = (TextView)v.findViewById(R.id.vegName);
            _vegDesc = (TextView)v.findViewById(R.id.vegDesc);
            _vegPrice = (TextView)v.findViewById(R.id.vegPrice);
            _qty = (TextView)v.findViewById(R.id.qty);
            //_vid = (TextView)v.findViewById(R.id.vid);
            _vegImage = (ImageView)v.findViewById(R.id.vegImage);
            _addBtn = (Button)v.findViewById(R.id.addBtn);
            _removeBtn = (Button)v.findViewById(R.id.removeBtn);
            //_wtBtn = (Button)v.findViewById(R.id.wtBtn);
            _cvLayout = (RelativeLayout)v.findViewById(R.id.cvLayout);
        }
    }

    public RVAdapter(List<Vegetable> _vegs, Callbacks _cbk, Context _context,RecyclerView recyclerView)
    {
        vegs = _vegs;
        mCallbacks = _cbk;
        context = _context;
        db = new DbConn(context);
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView1,int dx,int dy){
                super.onScrolled(recyclerView1,dx,dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if(!isLoading && totalItemCount <= (lastVisibleItem+visibleThreshold)){
                    if(mOnLoadMoreListner!=null){
                        mOnLoadMoreListner.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position){
        return vegs.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }else if(viewType == VIEW_TYPE_LOADING){
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_loading,parent,false);
            return new LoadingViewHolder(v);
        }
        return null;

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof ViewHolder) {
            final ViewHolder holder1 = (ViewHolder) holder;
            holder1._vegName.setText(vegs.get(position).getVegName());
            holder1._vegDesc.setText(vegs.get(position).getWeight());
            _imageURL = vegs.get(position).getImageURL();
            try {
                //URL url = new URL("http://imnikhil.net/vegfesta/images/" + _imageURL);
                Bitmap bmp = new LoadImageTask().execute(_imageURL).get();
                holder1._vegImage.setImageBitmap(bmp);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            } catch (ExecutionException ee) {
                ee.printStackTrace();
            }

            if (vegs.get(position).getInStock().equals("Y")) {
                holder1._vegPrice.setText("₹ " + vegs.get(position).getVegPrice());
                //DbConn db = new DbConn(context);
                holder1._qty.setText(db.getVidQty(vegs.get(position).getVid()) + "");

                holder1._addBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int i = Integer.parseInt(holder1._qty.getText().toString()) + 1;
                        holder1._qty.setText(i + "");
                        cnt++;
                        if (mCallbacks != null)
                            mCallbacks.onButtonClick(vegs.get(position), true);
                    }
                });
                holder1._removeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int i = 0;
                        if (Integer.parseInt(holder1._qty.getText().toString()) >= 1) {
                            i = Integer.parseInt(holder1._qty.getText().toString()) - 1;
                            cnt--;
                        }
                        holder1._qty.setText(i + "");
                        if (mCallbacks != null)
                            mCallbacks.onButtonClick(vegs.get(position), false);
                    }
                });
            } else {
                holder1._vegPrice.setText("Coming Soon");
                holder1._qty.setVisibility(View.GONE);
                holder1._addBtn.setVisibility(View.GONE);
                holder1._removeBtn.setVisibility(View.GONE);
            }
        }else if(holder instanceof LoadingViewHolder){
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder)holder;
            loadingViewHolder.itemProgressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return vegs.size();
    }

    public void setLoaded(){ isLoading = false;}

    public interface Callbacks
    {
        void onButtonClick(Vegetable veg,boolean addflg);
    }

    class LoadImageTask extends AsyncTask<String, Void, Bitmap>
    {
        protected Bitmap doInBackground(String... params)
        {
            Bitmap bmp = null;
            try {
                URL url = new URL("http://imnikhil.net/vegfesta/images/" + params[0]);

                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            }catch (IOException ie){ie.printStackTrace();}
            return bmp;
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder{
        public ProgressBar itemProgressBar;

        public LoadingViewHolder(View view){
            super(view);
            itemProgressBar = (ProgressBar)view.findViewById(R.id.itemProgress);
        }
    }

    public void setmOnLoadMoreListner(OnLoadMoreListner onLoadMoreListner){
        this.mOnLoadMoreListner = onLoadMoreListner;
    }
}
