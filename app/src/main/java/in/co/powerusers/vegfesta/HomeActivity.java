package in.co.powerusers.vegfesta;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements RVAdapter.Callbacks {

    List<Vegetable> vegs = new ArrayList<>();
    private RecyclerView vegList;
    private RecyclerView.Adapter adapter;
    private  RecyclerView.LayoutManager layoutManager;
    private MenuItem itemCart;
    private int cartCount = 0;
    private DbConn db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Connect conn = new Connect();
        vegs = conn.getInventory();
        db = new DbConn(this);

        vegList = (RecyclerView) findViewById(R.id.recyclerVw);
        vegList.setHasFixedSize(true);
        vegList.setNestedScrollingEnabled(false);
        layoutManager = new LinearLayoutManager(this);
        vegList.setLayoutManager(layoutManager);
        populateVegs();

        cartCount = db.getCartCount();

    }

    private void populateVegs()
    {
        layoutManager = new LinearLayoutManager(this);
        vegList.setLayoutManager(layoutManager);
        adapter = new RVAdapter(vegs,this,getApplicationContext());
        vegList.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        itemCart = menu.findItem(R.id.action_cart);

        if(db.isLoggedIn())
        {
            menu.findItem(R.id.action_signin).setVisible(false);

        }else{
            menu.findItem(R.id.action_orders).setVisible(false);
            menu.findItem(R.id.action_logout).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_cart) {
            if(cartCount>0)
            {
                if(db.isLoggedIn())
                    startActivity(new Intent(getApplicationContext(),CartActivity.class));
                else
                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
            return true;
        }
        if(id == R.id.action_signin){
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        }
        if(id == R.id.action_orders){
            startActivity(new Intent(getApplicationContext(),OrderActivity.class));
        }if(id == R.id.action_logout){
            db.logOut();
            invalidateOptionsMenu();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onButtonClick(Vegetable veg,boolean addflg) {
        LayerDrawable icon = (LayerDrawable)itemCart.getIcon();
        if(addflg)
            db.addToCart(veg.getVegName(),veg.getQty(),veg.getVegPrice(),veg.getVid());
        else
            db.deleteCartItemByVid(veg.getVid());
        cartCount = db.getCartCount();
        setBadgeCount(this,icon,cartCount+"");
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        LayerDrawable icon = (LayerDrawable)itemCart.getIcon();
        setBadgeCount(this,icon,cartCount+"");
        //setBadgeCount(this,icon, "0");
        return true;
    }

    public static void setBadgeCount(Context context, LayerDrawable icon, String count) {

        BadgeDrawable badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
        if (reuse != null && reuse instanceof BadgeDrawable) {
            badge = (BadgeDrawable) reuse;
        } else {
            badge = new BadgeDrawable(context);
        }

        badge.setmCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_badge, badge);
    }
}
