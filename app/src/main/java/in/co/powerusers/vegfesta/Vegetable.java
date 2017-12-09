package in.co.powerusers.vegfesta;

/**
 * Created by Powerusers on 26-11-2017.
 */

public class Vegetable {
    private String vegName;
    private String vegDesc;
    private String weight;
    private double qty;
    private double vegPrice;
    private String imageURL;
    private String vid;
    private String inStock;

    public Vegetable(String _vegName,String _vegDesc,String _weight,double _qty,double _vegPrice,String _imageURL,String _vid,String _inStock)
    {
        vegName = _vegName;
        vegDesc = _vegDesc;
        weight = _weight;
        qty = _qty;
        vegPrice = _vegPrice;
        imageURL = _imageURL;
        vid = _vid;
        inStock = _inStock;
    }

    public void setVegName(String _vegName){vegName = _vegName;}

    public String getVegName(){return  vegName;}

    public void setVegDesc(String _vegDesc){vegDesc = _vegDesc;}

    public  String getVegDesc(){return vegDesc;}

    public void setWeight(String _weight){weight = _weight;}

    public String getWeight(){return weight;}

    public void setQty(double _qty){qty = _qty;}

    public double getQty(){return qty;}

    public void setVegPrice(double _vegPrice){vegPrice = _vegPrice;}

    public  double getVegPrice(){return vegPrice;}

    public void setImageURL(String _imageURL){imageURL = _imageURL;}

    public String getImageURL(){return imageURL;}

    public void setVid(String _vid){vid = _vid;}

    public String getVid(){return vid;}

    public void setInStock(String _inStock){inStock = _inStock;}

    public String getInStock(){return inStock;}
}
