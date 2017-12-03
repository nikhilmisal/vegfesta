package in.co.powerusers.vegfesta;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Powerusers on 01-12-2017.
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder>{
    private List<Vegetable> vegs;
    private DelCallBack delCallBack;
    private static final String TAG = "CartAdapter";
    public static class ViewHolder extends RecyclerView.ViewHolder{
        CardView _cCv;
        TextView _cVegName;
        TextView _cPrice;
        TextView _cQty;
        ImageButton _cDeleteBtn;
        RelativeLayout _cRelLayout;

        public ViewHolder(View v){
            super(v);
            _cCv = (CardView)v.findViewById(R.id.cCv);
            _cVegName = (TextView)v.findViewById(R.id.cVegName);
            _cPrice = (TextView)v.findViewById(R.id.cPrice);
            _cQty = (TextView)v.findViewById(R.id.cQty);
            _cDeleteBtn = (ImageButton)v.findViewById(R.id.cDeleteBtn);
            _cRelLayout = (RelativeLayout)v.findViewById(R.id.cRelLayout);
        }
    }

    public CartAdapter(List<Vegetable> _vegs,DelCallBack _delCallBack){vegs = _vegs; delCallBack = _delCallBack;}

    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_item,parent,false);
        CartAdapter.ViewHolder vh = new CartAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final CartAdapter.ViewHolder holder, final int position) {
        Log.d(TAG,vegs.get(position).getVegName()+"-----"+vegs.get(position).getQty()+"----------"+vegs.get(position).getVegPrice());
        holder._cVegName.setText(vegs.get(position).getVegName());
        holder._cQty.setText(vegs.get(position).getQty()+"");
        holder._cPrice.setText("₹ " + vegs.get(position).getVegPrice());
        holder._cDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(delCallBack !=null)
                    delCallBack.onButtonClick(vegs.get(position).getVegName());
            }
        });
        //_imageURL = vegs.get(position).getImageURL();
    }

    @Override
    public int getItemCount() {
        return vegs.size();
    }

    public interface DelCallBack
    {
        void onButtonClick(String vid);
    }
}
