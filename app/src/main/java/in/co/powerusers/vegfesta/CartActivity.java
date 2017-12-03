package in.co.powerusers.vegfesta;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity implements CartAdapter.DelCallBack {
    private DbConn db;
    private RecyclerView vegList;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Vegetable> vegs;
    private Button confirmBtn;
    private ImageButton cDeleteBtn;
    private static final String TAG = "CartActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        confirmBtn = (Button)findViewById(R.id.confirmBtn);
        vegList = (RecyclerView)findViewById(R.id.cartRecyclerVw);
        vegList.setHasFixedSize(true);
        vegList.setNestedScrollingEnabled(false);
        layoutManager = new LinearLayoutManager(this);
        cDeleteBtn = (ImageButton)findViewById(R.id.cDeleteBtn);
        vegList.setLayoutManager(layoutManager);

        db = new DbConn(this);
        vegs = new ArrayList<>();
        vegs = db.getCartItems();
        populateCart();

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),AddressActivity.class));
            }
        });
    }

    private void populateCart()
    {
        layoutManager = new LinearLayoutManager(this);
        vegList.setLayoutManager(layoutManager);
        adapter = new CartAdapter(vegs,this);
        vegList.setAdapter(adapter);
    }

    @Override
    public void onButtonClick(String vid) {
        db.deleteCartItem(vid);
        vegs = db.getCartItems();
        populateCart();
    }
}
