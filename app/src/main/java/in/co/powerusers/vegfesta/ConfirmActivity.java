package in.co.powerusers.vegfesta;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;

public class ConfirmActivity extends AppCompatActivity {

    private TextView fCartItems,fAddress,fTotal;
    private DbConn db;
    private double totalAmt;
    private Button placeOrdrBtn;
    private LinearLayout conLayout;
    private View progressVw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
        db = new DbConn(this);

        fCartItems = (TextView)findViewById(R.id.fCartItems);
        fAddress = (TextView)findViewById(R.id.fAddress);
        fTotal = (TextView)findViewById(R.id.fTotal);
        totalAmt = 0.0;
        placeOrdrBtn = (Button)findViewById(R.id.placeOrdrBtn);
        conLayout = (LinearLayout)findViewById(R.id.confirmLayout);
        progressVw = findViewById(R.id.confirmProcess);
        showProgress(false);
        StringBuilder sb = new StringBuilder();
        List<Vegetable> vegs = db.getCartItems();
        for(Vegetable veg : vegs){
            sb.append(veg.getVegName()+"       "+veg.getQty()+"      "+veg.getVegPrice()+"\n");
            totalAmt = totalAmt+veg.getVegPrice();
        }

        StringBuilder sb2 = new StringBuilder();
        Cursor cr2 = db.getAddress();
        if(cr2.moveToNext()){
            sb2.append(cr2.getString(1)+" "+cr2.getString(2)+"\n"+cr2.getString(3)+"\n"+cr2.getString(4)+", "+cr2.getString(5)+"\n Pin Code: "+cr2.getString(6));
        }

        fCartItems.setText(sb.toString());
        fAddress.setText(sb2.toString());
        fTotal.setText("₹ "+totalAmt);

        placeOrdrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                progressVw.setVisibility(View.VISIBLE);
                placeOrdrBtn.setVisibility(View.GONE);
                Connect conn = new Connect();
                boolean status = conn.PlaceOrder(getApplicationContext());
                if(status) {
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                }
                else {
                    progressVw.setVisibility(View.GONE);
                    placeOrdrBtn.setVisibility(View.VISIBLE);
                    Snackbar.make(view, "Error!, Please try again later", Snackbar.LENGTH_SHORT);
                }
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            conLayout.setVisibility(show ? View.GONE : View.VISIBLE);
            conLayout.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    conLayout.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            placeOrdrBtn.setVisibility(show ? View.GONE : View.VISIBLE);
            placeOrdrBtn.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    placeOrdrBtn.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressVw.setVisibility(show ? View.VISIBLE : View.GONE);
            progressVw.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressVw.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressVw.setVisibility(show ? View.VISIBLE : View.GONE);
            conLayout.setVisibility(show ? View.GONE : View.VISIBLE);
            placeOrdrBtn.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
