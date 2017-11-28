package in.co.powerusers.vegfesta;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Powerusers on 19-11-2017.
 */

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> {
    private List<Vegetable> vegs;
    private Callbacks mCallbacks;
    private int cnt = 0;
    public static class ViewHolder extends RecyclerView.ViewHolder{
        CardView _cv;
        TextView _vegName;
        TextView _vegDesc;
        TextView _vegPrice;
        ImageButton _addBtn;
        ImageButton _removeBtn;
        TextView _qty;
        RelativeLayout _cvLayout;


        public ViewHolder(View v){
            super(v);
            _cv = (CardView)v.findViewById(R.id.cv);
            _vegName = (TextView)v.findViewById(R.id.vegName);
            _vegDesc = (TextView)v.findViewById(R.id.vegDesc);
            _vegPrice = (TextView)v.findViewById(R.id.vegPrice);
            _qty = (TextView)v.findViewById(R.id.qty);
            _addBtn = (ImageButton)v.findViewById(R.id.addBtn);
            _removeBtn = (ImageButton)v.findViewById(R.id.removeBtn);
            _cvLayout = (RelativeLayout)v.findViewById(R.id.cvLayout);

        }
    }

    public RVAdapter(List<Vegetable> _vegs,Callbacks cbk){vegs = _vegs; mCallbacks = cbk;}

    @Override
    public RVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item,parent,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final RVAdapter.ViewHolder holder, int position) {
        holder._vegName.setText(vegs.get(position).getVegName());
        holder._vegDesc.setText(vegs.get(position).getVegDesc());
        holder._vegPrice.setText("₹ "+vegs.get(position).getVegPrice());
        holder._addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = Integer.parseInt(holder._qty.getText().toString())+1;
                holder._qty.setText(i+"");
                cnt++;
                if(mCallbacks!=null)
                    mCallbacks.onButtonClick(cnt);
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
                    mCallbacks.onButtonClick(cnt);
            }
        });
    }

    @Override
    public int getItemCount() {
        return vegs.size();
    }

    public interface Callbacks
    {
        void onButtonClick(int count);
    }
}
