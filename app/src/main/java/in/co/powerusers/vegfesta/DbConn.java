package in.co.powerusers.vegfesta;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Powerusers on 02-12-2017.
 */

public class DbConn extends SQLiteAssetHelper {
    //DB_NAME
    private static final String DB_NAME = "vfdb.db";

    //DB VERSION
    private static int DB_VERSION = 2;

    //TABLES
    private static final String CART_TABLE = "cart";
    private static final String ADDRESS_TABLE = "address";
    private static final String LOGIN_TABLE = "login";

    private static final String TAG = "DbConn";

    public DbConn(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion!=oldVersion){
            String sql = "alter table "+CART_TABLE+" add weight numeric";
            db.execSQL(sql);
        }
        onCreate(db);
    }

    public boolean addToCart(String vegName,double qty,double price,String vid)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String insertQuery = "insert into "+CART_TABLE+" (vegname,price,qty,vid) values ('"+vegName+"',"+price+","+qty+",'"+vid+"')";
        try
        {
            db.execSQL(insertQuery);
        }catch(SQLiteConstraintException se)
        {
            se.printStackTrace();
            return false;
        }catch(SQLiteException le){
            le.printStackTrace();
            return false;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public List<Vegetable> getCartItems()
    {
        List<Vegetable> vegs = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "select vegname, count(1) qty, sum(price) price, vid from "+CART_TABLE+" group by vegname,vid";
        Cursor cr =db.rawQuery(selectQuery,null);
        if(cr.moveToFirst()){
            do{
                vegs.add(new Vegetable(cr.getString(0),null,null,cr.getDouble(1),cr.getDouble(2),null,cr.getString(3),null));
                Log.d(TAG,cr.getString(0)+"------"+cr.getDouble(1)+"------"+cr.getDouble(2)+"------"+cr.getDouble(3));
            }while (cr.moveToNext());
        }
        return vegs;
    }

    public int getCartCount()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "select count(1) from "+CART_TABLE;
        Cursor cr =db.rawQuery(selectQuery,null);
        int cnt = 0;
        if(cr.moveToFirst())
        {
            cnt = cr.getInt(0);
        }
        return cnt;
    }

    public void clearCart()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String deleteQuery = "delete from "+CART_TABLE;
        try {
            db.execSQL(deleteQuery);
        }catch(SQLiteException se){
            se.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public boolean deleteCartItem(String vegName)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String deleteQuery = "delete from "+CART_TABLE+" where vegname = '"+vegName+"'";
        try {
            db.execSQL(deleteQuery);
        }catch(SQLiteException se){
            se.printStackTrace();
            return false;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean deleteCartItemByVid(String vid)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "select ifnull(max(cnt),0) from "+CART_TABLE+" where vid = '"+vid+"'";
        Cursor cr =db.rawQuery(selectQuery,null);
        int cnt = 0;
        if(cr.moveToFirst())
        {
            cnt = cr.getInt(0);
        }
        String deleteQuery = "";
        if(cnt>1)
            deleteQuery = "delete from "+CART_TABLE+" where vid = '"+vid+"' and cnt = "+cnt;
        else
            deleteQuery = "delete from "+CART_TABLE+" where vid = '"+vid+"'";
        Log.d(TAG,deleteQuery);
        try {
            db.execSQL(deleteQuery);
        }catch(SQLiteException se){
            se.printStackTrace();
            return false;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public int getVidQty(String vid)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "select count(1) from "+CART_TABLE+" where vid = '"+vid+"'";
        Cursor cr =db.rawQuery(selectQuery,null);
        int cnt = 0;
        if(cr.moveToFirst())
        {
            cnt = cr.getInt(0);
        }
        return cnt;
    }

    public Cursor getAddress()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "select address_type,address_line1,address_line2,landmark,city,state,pincode from "+ADDRESS_TABLE +" order by cnt desc";
        Cursor cr = db.rawQuery(selectQuery,null);
        return cr;
    }

    public boolean addAddress(String address_type,String address_line1,String address_line2,String landmark,String city,String state,int pincode)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String insertQuery = "insert into "+ADDRESS_TABLE+" (address_type,address_line1,address_line2,landmark,city,state,pincode) values " +
                "('"+address_type+"','"+address_line1+"','"+address_line2+"','"+landmark+"','"+city+"','"+state+"',"+pincode+")";
        try
        {
            db.execSQL(insertQuery);
        }catch(SQLiteConstraintException se){
            se.printStackTrace();
            return false;
        }catch(SQLiteException qe){
            qe.printStackTrace();
            return false;
        }catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean isLoggedIn()
    {
        boolean loggedInFlg = false;
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "select count(1) from "+LOGIN_TABLE;
        Cursor cr = db.rawQuery(selectQuery,null);
        if(cr.moveToFirst())
        {
            if(Integer.valueOf((cr.getString(0))) > 0)
                loggedInFlg = true;
            else
                loggedInFlg = false;
        }
        return loggedInFlg;
    }

    public boolean logIn(String emailid)
    {
        boolean loggedInFlg = false;
        SQLiteDatabase db = this.getReadableDatabase();
        String deleteQuery = "delete from "+LOGIN_TABLE;
        db.execSQL(deleteQuery);
        String insertQuery = "insert into "+LOGIN_TABLE+" (login_flg) values ('"+emailid+"')";
        Log.d(TAG, insertQuery);
        try
        {
            db.execSQL(insertQuery);
        }catch(SQLiteConstraintException se){
            se.printStackTrace();
            loggedInFlg = false;
        }catch(SQLiteException qe){
            qe.printStackTrace();
            loggedInFlg = false;
        }catch(Exception e){
            e.printStackTrace();
            loggedInFlg = false;
        }
        return loggedInFlg;
    }

    public String getLoggedInId()
    {
        String emailid = "";
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "select login_flg from "+LOGIN_TABLE;
        Cursor cr = db.rawQuery(selectQuery,null);
        if(cr.moveToFirst()){
            emailid = cr.getString(0);
        }
        //Log.d(TAG,"emailid ===> "+emailid);
        return emailid;
    }

    public void logOut()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String deleteQuery = "delete from "+LOGIN_TABLE;
        db.execSQL(deleteQuery);
    }
}
