package in.co.powerusers.vegfesta;

import android.content.Intent;
import android.database.Cursor;
import android.opengl.Visibility;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class AddressActivity extends AppCompatActivity {

    private TextView naddressLine1,naddressLine2,nlandmark,ncitystate,npincode;
    private EditText addressLine1,addressLine2,landmark,city,state,pincode;
    private Spinner addrType;
    private Button addAddrBtn;
    private DbConn db;
    private CardView cv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        db = new DbConn(this);
        naddressLine1 = (TextView)findViewById(R.id.naddressLine1);
        naddressLine2 = (TextView)findViewById(R.id.naddressLine2);
        nlandmark = (TextView)findViewById(R.id.nlandmark);
        ncitystate = (TextView)findViewById(R.id.ncitystate);
        npincode = (TextView)findViewById(R.id.npincode);

        addressLine1 = (EditText)findViewById(R.id.addressLine1);
        addressLine2 = (EditText)findViewById(R.id.addressLine2);
        landmark = (EditText)findViewById(R.id.landmark);
        city = (EditText)findViewById(R.id.city);
        state = (EditText)findViewById(R.id.state);
        pincode = (EditText)findViewById(R.id.pincode);
        addrType = (Spinner)findViewById(R.id.addrType);

        addAddrBtn = (Button)findViewById(R.id.addAddrBtn);
        cv = (CardView)findViewById(R.id.cvAddress);

        Cursor cr = db.getAddress();
        if(cr.moveToFirst())
        {
            naddressLine1.setText(cr.getString(1));
            naddressLine2.setText(cr.getString(2));
            nlandmark.setText(cr.getString(3));
            ncitystate.setText(cr.getString(4)+", "+cr.getString(5));
            npincode.setText(cr.getString(6));
        }else{
            cv.setVisibility(View.GONE);
        }

        cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ConfirmActivity.class));
            }
        });

        addAddrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String aLine1 = addressLine1.getText().toString();
                String aLine2 = addressLine2.getText().toString();
                String aLandmark = landmark.getText().toString();
                String aCity = city.getText().toString();
                String aState = state.getText().toString();
                String aPincode = pincode.getText().toString();
                String aAddrType = addrType.getSelectedItem().toString();
                if(TextUtils.isEmpty(aLine1))
                {
                    addressLine1.setError("This field is required");
                    addressLine1.requestFocus();
                }
                if(TextUtils.isEmpty(aCity))
                {
                    city.setError("This field is required");
                    city.requestFocus();
                }
                if(TextUtils.isEmpty(aState))
                {
                    state.setError("This field is required");
                    state.requestFocus();
                }
                if(TextUtils.isEmpty(aPincode))
                {
                    pincode.setError("This field is required");
                    pincode.requestFocus();
                }
                if(!TextUtils.isEmpty(aLine1) && !TextUtils.isEmpty(aCity) && !TextUtils.isEmpty(aState) && !TextUtils.isEmpty(aPincode))
                {
                    boolean flg = db.addAddress(aAddrType,aLine1,aLine2,aLandmark,aCity,aState,Integer.valueOf(aPincode));
                    Connect conn = new Connect();
                    conn.addAddress(aAddrType,aLine1.replaceAll(" ","+"),aLine2 == null ? aLine2 : aLine2.replaceAll(" ","+"),aLandmark == null ? aLandmark : aLandmark.replaceAll(" ","+"),aCity.replaceAll(" ","+"),aState.replaceAll(" ","+"),aPincode,db.getLoggedInId());
                    if(flg){
                        startActivity(new Intent(getApplicationContext(),ConfirmActivity.class));
                    }else{
                        Snackbar.make(view,"Please try after some time",Snackbar.LENGTH_SHORT);
                    }
                }
            }
        });
    }
}
