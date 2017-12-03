package in.co.powerusers.vegfesta;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Powerusers on 03-12-2017.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder>{
    private List<String> orders;
    private static final String TAG = "OrderAdapter";

    public static class ViewHolder extends RecyclerView.ViewHolder{
        CardView _orderCv;
        TextView _orderNo;
        TextView _orderQty;
        TextView _orderAmount;
        LinearLayout _orderLLayout;

        public ViewHolder(View v){
            super(v);
            _orderCv = (CardView)v.findViewById(R.id.orderCv);
            _orderNo = (TextView)v.findViewById(R.id.orderNo);
            _orderQty = (TextView)v.findViewById(R.id.orderQty);
            _orderAmount = (TextView)v.findViewById(R.id.orderAmount);
            _orderLLayout = (LinearLayout)v.findViewById(R.id.orderLLayout);
        }
    }

    public OrderAdapter(List<String> _orders){orders = _orders;}

    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_item,parent,false);
        OrderAdapter.ViewHolder vh = new OrderAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final OrderAdapter.ViewHolder holder, final int position) {
        holder._orderNo.setText(orders.get(position).split("\\|")[0]);
        holder._orderQty.setText(orders.get(position).split("\\|")[1]);
        holder._orderAmount.setText("₹ "+orders.get(position).split("\\|")[2]);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }
}
