package restaurent.menu.demo.helper;

import java.io.Serializable;

/**
 * Created by dell on 12-06-2015.
 */

public class MenuItem implements Serializable
{

    public int id;
    public String item_type, name;
    public  float price;


    public  MenuItem()
    {

    }


    public MenuItem(int id, String item_type, String name, float price)
    {

        this.id = id;
        this.item_type = item_type;
        this.name = name;
        this.price = price;
    }


    public void setId(int id)
    {
        this.id = id;
    }

    public int getId()
    {
        return this.id;
    }


    public void setItemType(String item_type)
    {
        this.item_type = item_type;
    }

    public String getItemType()
    {
        return this.item_type;
    }


    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }


    public void setPrice(float price)
    {
        this.price = price;
    }

    public float getPrice()
    {
        return this.price;
    }
}