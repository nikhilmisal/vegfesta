package in.co.powerusers.vegfesta;

/**
 * Created by Powerusers on 26-11-2017.
 */

public class Vegetable {
    private String vegName;
    private String vegDesc;
    private double qty;
    private double vegPrice;

    public Vegetable(String _vegName,String _vegDesc,double _qty,double _vegPrice)
    {
        vegName = _vegName;
        vegDesc = _vegDesc;
        qty = _qty;
        vegPrice = _vegPrice;
    }

    public void setVegName(String _vegName){vegName = _vegName;}

    public String getVegName(){return  vegName;}

    public void setVegDesc(String _vegDesc){vegDesc = _vegDesc;}

    public  String getVegDesc(){return vegDesc;}

    public void setQty(double _qty){qty = _qty;}

    public double getQty(){return qty;}

    public void setVegPrice(double _vegPrice){vegPrice = _vegPrice;}

    public  double getVegPrice(){return vegPrice;}
}
