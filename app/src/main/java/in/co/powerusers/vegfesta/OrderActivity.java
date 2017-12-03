package in.co.powerusers.vegfesta;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

public class OrderActivity extends AppCompatActivity {
    private RecyclerView orderRV;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<String> orders;
    private static final String TAG = "OrderActivity";
    private DbConn db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        orderRV = (RecyclerView)findViewById(R.id.orderRV);
        orderRV.setHasFixedSize(true);
        orderRV.setNestedScrollingEnabled(false);
        layoutManager = new LinearLayoutManager(this);
        orderRV.setLayoutManager(layoutManager);

        db = new DbConn(this);
        String emailid = db.getLoggedInId();

        Connect conn = new Connect();
        orders = conn.getOrders(emailid);

        populateOrders();
    }

    private void populateOrders()
    {
        layoutManager = new LinearLayoutManager(this);
        orderRV.setLayoutManager(layoutManager);
        adapter = new OrderAdapter(orders);
        orderRV.setAdapter(adapter);
    }
}
