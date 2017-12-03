package in.co.powerusers.vegfesta;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> {
    private List<Vegetable> vegs;
    private Callbacks mCallbacks;
    private int cnt = 0;
    private String _imageURL;
    private Context context;
    private static final String TAG = "RVAdapter";
    private DbConn db;
    public static class ViewHolder extends RecyclerView.ViewHolder{
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

    public RVAdapter(List<Vegetable> _vegs, Callbacks _cbk, Context _context){vegs = _vegs; mCallbacks = _cbk;context = _context;db = new DbConn(context);}

    @Override
    public RVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item,parent,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final RVAdapter.ViewHolder holder, final int position) {
        holder._vegName.setText(vegs.get(position).getVegName());
        holder._vegDesc.setText(vegs.get(position).getVegDesc());
        holder._vegPrice.setText("₹ "+vegs.get(position).getVegPrice());
        //DbConn db = new DbConn(context);
        holder._qty.setText(db.getVidQty(vegs.get(position).getVid())+"");
        _imageURL = vegs.get(position).getImageURL();

        try {
            //URL url = new URL("http://imnikhil.net/vegfesta/images/" + _imageURL);
            Bitmap bmp = new LoadImageTask().execute(_imageURL).get();
            holder._vegImage.setImageBitmap(bmp);
        }catch(InterruptedException ie)
        {ie.printStackTrace();}
        catch(ExecutionException ee)
        {ee.printStackTrace();}


        holder._addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = Integer.parseInt(holder._qty.getText().toString())+1;
                holder._qty.setText(i+"");
                cnt++;
                if(mCallbacks!=null)
                    mCallbacks.onButtonClick(new Vegetable(vegs.get(position).getVegName(),vegs.get(position).getVegDesc(),vegs.get(position).getWeight(),vegs.get(position).getQty(),vegs.get(position).getVegPrice(),null,vegs.get(position).getVid()),true);
            }
        });
        holder._removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = 0;
                if(Integer.parseInt(holder._qty.getText().toString())>=1) {
                    i = Integer.parseInt(holder._qty.getText().toString()) - 1;
                    cnt--;
                }
                holder._qty.setText(i+"");
                if(mCallbacks!=null)
                    mCallbacks.onButtonClick(new Vegetable(vegs.get(position).getVegName(),vegs.get(position).getVegDesc(),vegs.get(position).getWeight(),vegs.get(position).getQty(),vegs.get(position).getVegPrice(),null,vegs.get(position).getVid()),false);
            }
        });
    }

    @Override
    public int getItemCount() {
        return vegs.size();
    }

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
}
