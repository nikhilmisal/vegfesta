package in.co.powerusers.vegfesta;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class RegisterActivity extends AppCompatActivity {

    private EditText rname,remail,rmobile,rpass;
    private Button rsubmit;
    private View mProgressView;
    private View mRegisterFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        rname = (EditText)findViewById(R.id.rname);
        remail = (EditText)findViewById(R.id.remail);
        rmobile = (EditText)findViewById(R.id.rmobile);
        rpass = (EditText)findViewById(R.id.rpass);
        rsubmit = (Button)findViewById(R.id.rsubmit);
        mRegisterFormView = findViewById(R.id.register_form);
        mProgressView = findViewById(R.id.register_progress);

        rsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!rname.equals("") && !remail.equals("") && !rmobile.equals("") && !rpass.equals(""))
                {
                    try {
                        showProgress(true);
                        String[] params = {rname.getText().toString().replaceAll(" ","+"), remail.getText().toString(), rmobile.getText().toString(), rpass.getText().toString()};
                        String stat = new JsonTask().execute(params).get();
                        //Snackbar sb = Snackbar.make(view,stat,Snackbar.LENGTH_SHORT);
                        //sb.show();
                    }catch(InterruptedException ie)
                    {ie.printStackTrace();}
                    catch(ExecutionException ee)
                    {ee.printStackTrace();}
                }
            }
        });
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mRegisterFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    class JsonTask extends AsyncTask<String, Void, String>
    {
        protected String doInBackground(String... params)
        {
            String stat = "";
            Connect conn = new Connect();
            stat = conn.createUser(params[0],params[1],params[2],params[3]);
            return stat;
        }

        @Override
        protected void onPostExecute(final String success)
        {
            showProgress(false);

            if (success.equals("Success")) {
                //finish();
                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
            } else {
                //mPasswordView.setError(getString(R.string.error_incorrect_password));
                //mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            showProgress(false);
        }
    }
}
