package in.co.powerusers.vegfesta;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Powerusers on 28-11-2017.
 */

public class Connect {
    Connect()
    {

    }
    private static final String TAG = "Connect";
    public boolean checkUser(String emailid)
    {
        String stat = "";
        try {
            String[] params = {"rtype|CHECKU", "emailid|" + emailid};
            JsonReader jreader = CallService(params);
            jreader.beginObject();
            //System.out.println(jreader.toString());
            while (jreader.hasNext()) {
                String name = jreader.nextName();
                if (name.equals("result")) {
                    stat = jreader.nextString();
                }
            }
        }catch(IOException ie){ie.printStackTrace();}
        if(stat.equals("Valid"))
            return true;
        else
            return false;
    }

    public boolean validateLogin(String emailid,String pass)
    {
        String stat = "";
        try {
            String[] params = {"rtype|LOGIN","emailid|"+emailid,"pass|"+pass};
            //stat = new JsonTask().execute(params).get();
            JsonReader jreader = CallService(params);
            jreader.beginObject();
            while (jreader.hasNext()) {
                String name = jreader.nextName();
                if (name.equals("result")) {
                    stat = jreader.nextString();
                }
            }
        }catch(IOException ie){ie.printStackTrace();}
        if(stat.equals("Valid"))
            return true;
        else
            return false;

    }

    public String createUser(String _name,String _emailid,String _mobile,String _pass)
    {
        String stat = "";
        try {
            String[] params = {"rtype|REGISTER", "uname|" + _name, "emailid|" + _emailid, "mobile|" + _mobile, "pass|" + _pass};
            JsonReader jreader = CallService(params);
            jreader.beginObject();
            while(jreader.hasNext()){
                String name = jreader.nextName();
                if(name.equals("result")){
                    stat = jreader.nextString();
                }
            }
        }catch(IOException ie){ie.printStackTrace();}
        return stat;
    }

    public List<Vegetable> getInventory()
    {
        List<Vegetable> vegs = new ArrayList<>();
        try{
            String[] params = {"rtype|GETVEGS"};
            JsonReader jreader = new JsonTask().execute(params).get();
            jreader.beginArray();
            while(jreader.hasNext()){
                Vegetable veg;
                String _vname = "";
                Double _vprice = 0.0;
                String _vimage = "";
                String _vid = "";
                jreader.beginObject();
                while(jreader.hasNext()){
                    String name = jreader.nextName();
                    if(name.equals("vegName"))
                        _vname = jreader.nextString();
                    if(name.equals("vegPrice"))
                        _vprice = jreader.nextDouble();
                    if(name.equals("vegImage"))
                        _vimage = jreader.nextString();
                    if(name.equals("vid"))
                        _vid = jreader.nextString();
                }
                jreader.endObject();
                veg = new Vegetable(_vname,_vname,0.0,0.0,_vprice,_vimage,_vid);
                vegs.add(veg);
            }
            jreader.endArray();
        }catch (IOException ie){ie.printStackTrace();}
        catch(InterruptedException ite){ite.printStackTrace();}
        catch(ExecutionException ee){ee.printStackTrace();}
        return vegs;
    }

    public boolean PlaceOrder(Context context)
    {
        DbConn db = new DbConn(context);
        List<Vegetable> vegs = db.getCartItems();
        //Cursor cr = db.getCartItems();
        boolean flg = false;
        double totalAmt = 0.0;
        StringBuilder sb = new StringBuilder();
        try {
            //JSONArray jsA = new JSONArray();
            for(Vegetable veg: vegs)
            {
                totalAmt = totalAmt + veg.getQty();
                Log.d(TAG,veg.getVid()+"----"+veg.getQty()+"-----"+veg.getVegPrice());
                sb.append(veg.getVid()+":"+veg.getQty()+":"+veg.getVegPrice()+"?");
            }
            sb.setLength(sb.length()-1);
            //JSONObject jsOFinal = new JSONObject();
            //jsOFinal.put("items",jsA);
            //jsOFinal.put("totalamt",totalAmt);
            //jsOFinal.put("emailid",db.getLoggedInId());
            String[] params = {"rtype|CRORDER","items|"+sb.toString(),"totalamt|"+totalAmt,"emailid|"+db.getLoggedInId()};
            JsonReader jreader = new JsonTask().execute(params).get();
            jreader.beginObject();
            while(jreader.hasNext()){
                String name = jreader.nextName();
                if(name.equals("result")){
                    flg = jreader.nextString().equals("Success");
                }
            }
            if(flg)
                db.clearCart();
        }/*catch(JSONException je){
            je.printStackTrace();
            flg = false;
        }*/catch (InterruptedException ie){
            ie.printStackTrace();
            flg = false;
        }catch(ExecutionException ee){
            ee.printStackTrace();
            flg = false;
        }catch(IOException ie){
            ie.printStackTrace();
            flg = false;
        }
        return flg;
    }

    public List<String> getOrders(String emailid)
    {
        List<String> orders = new ArrayList<>();
        String sb = "";
        try{
            String[] params = {"rtype|GETORDERS","emailid|"+emailid};
            JsonReader jreader = new JsonTask().execute(params).get();
            jreader.beginArray();
            while(jreader.hasNext()){
                Vegetable veg;
                String _ono = "";
                Double _oqty = 0.0;
                Double _oamount = 0.0;
                jreader.beginObject();
                while(jreader.hasNext()){
                    String name = jreader.nextName();
                    if(name.equals("ono"))
                        _ono = jreader.nextString();
                    if(name.equals("oqty"))
                        _oqty = jreader.nextDouble();
                    if(name.equals("oamount"))
                        _oamount = jreader.nextDouble();
                }
                jreader.endObject();
                sb = _ono+"|"+_oqty+"|"+_oamount;
                orders.add(sb);
            }
            jreader.endArray();
        }catch (IOException ie){ie.printStackTrace();}
        catch(InterruptedException ite){ite.printStackTrace();}
        catch(ExecutionException ee){ee.printStackTrace();}
        return orders;
    }

    private JsonReader CallService(String... params)
    {
        String stat = "";
        JsonReader jreader = null;
        try {
            HttpURLConnection connection = null;
            StringBuilder query = new StringBuilder();
            for(int i=0;i<params.length;i++)
            {
                System.out.println("===> "+params[i]);
                query.append(params[i].split("\\|")[0]+"="+params[i].split("\\|")[1]+"&");
            }
            query.setLength(query.length()-1);
            //System.out.println("http://imnikhil.net/vegfesta/connect.php?"+query.toString());
            URL url = new URL("http://imnikhil.net/vegfesta/connect.php?"+query.toString());
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();

            InputStream is = connection.getInputStream();
            jreader = new JsonReader(new InputStreamReader(is, "UTF-8"));

        }catch (IOException ie){ie.printStackTrace();}
        return jreader;
    }

    class JsonTask extends AsyncTask<String, Void, JsonReader>
    {
        JsonReader jreader = null;
        protected JsonReader doInBackground(String... params)
        {
            try {
                HttpURLConnection connection = null;
                StringBuilder query = new StringBuilder();
                for (int i = 0; i < params.length; i++) {
                    System.out.println("===> " + params[i]);
                    query.append(params[i].split("\\|")[0] + "=" + params[i].split("\\|")[1] + "&");
                }
                query.setLength(query.length() - 1);
                //System.out.println("http://imnikhil.net/vegfesta/connect.php?" + query.toString());
                URL url = new URL("http://imnikhil.net/vegfesta/connect.php?" + query.toString());
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.connect();

                InputStream is = connection.getInputStream();
                jreader = new JsonReader(new InputStreamReader(is, "UTF-8"));
                Log.d(TAG,"JsonTask: "+jreader.toString());
            }catch(IOException ie){ie.printStackTrace();}
            return jreader;
        }
    }
}

