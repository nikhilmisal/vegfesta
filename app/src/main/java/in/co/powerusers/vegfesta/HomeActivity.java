package in.co.powerusers.vegfesta;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements RVAdapter.Callbacks {

    List<Vegetable> vegs = new ArrayList<>();
    private RecyclerView vegList;
    private RecyclerView.Adapter adapter;
    private  RecyclerView.LayoutManager layoutManager;
    private MenuItem itemCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        vegs.add(new Vegetable("Potato","Potato 1Kg",1.00,30.00));
        vegs.add(new Vegetable("Onion","Onion 1/2Kg",0.50,25.00));
        vegs.add(new Vegetable("Tomato","Tomato 1Kg",1.00,60.00));

        vegList = (RecyclerView) findViewById(R.id.recyclerVw);
        vegList.setHasFixedSize(true);
        vegList.setNestedScrollingEnabled(false);
        layoutManager = new LinearLayoutManager(this);
        vegList.setLayoutManager(layoutManager);

        populateVegs();
    }

    private void populateVegs()
    {
        layoutManager = new LinearLayoutManager(this);
        vegList.setLayoutManager(layoutManager);
        adapter = new RVAdapter(vegs,this);
        vegList.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        itemCart = menu.findItem(R.id.action_cart);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onButtonClick(int count) {
        LayerDrawable icon = (LayerDrawable)itemCart.getIcon();
        setBadgeCount(this,icon,count+"");
    }

    public static void setBadgeCount(Context context, LayerDrawable icon, String count) {

        BadgeDrawable badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
        Drawable reuse1 = icon.findDrawableByLayerId(R.id.ic_cart_icon);
        if (reuse != null && reuse instanceof BadgeDrawable) {
            badge = (BadgeDrawable) reuse;
        } else {
            badge = new BadgeDrawable(context);
        }

        badge.setmCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_badge, badge);
        icon.setDrawableByLayerId(R.id.ic_cart_icon,reuse1);
    }
}
