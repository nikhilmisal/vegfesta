package in.co.powerusers.vegfesta;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Powerusers on 27-12-2017.
 */

public class OrderFragment extends Fragment {
    private RecyclerView orderRV;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<String> orders;
    private static final String TAG = "OrderActivity";
    private DbConn db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.activity_order,container,false);

        orderRV = (RecyclerView)view.findViewById(R.id.orderRV);
        orderRV.setHasFixedSize(true);
        orderRV.setNestedScrollingEnabled(false);
        layoutManager = new LinearLayoutManager(view.getContext());
        orderRV.setLayoutManager(layoutManager);

        db = new DbConn(view.getContext());
        String emailid = db.getLoggedInId();

        Connect conn = new Connect();
        orders = conn.getOrders(emailid);

        populateOrders();
        return view;
    }

    private void populateOrders()
    {
        layoutManager = new LinearLayoutManager(getContext());
        orderRV.setLayoutManager(layoutManager);
        adapter = new OrderAdapter(orders);
        orderRV.setAdapter(adapter);
    }
}
