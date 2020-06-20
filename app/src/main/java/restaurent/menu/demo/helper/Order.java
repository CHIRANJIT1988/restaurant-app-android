package restaurent.menu.demo.helper;

import java.io.Serializable;

/**
 * Created by dell on 12-06-2015.
 */
public class Order implements Serializable
{

    public String order_id, datetime, status, waiter_id;

    public int item_code, quantity, table_no;


    public Order()
    {

    }


    public Order(String order_id, int item_code, int quantity, int table_no, String datetime, String status)
    {

        this.order_id = order_id;
        this.item_code = item_code;
        this.datetime = datetime;
        this.quantity = quantity;
        this.table_no = table_no;
        this.status = status;
    }


    public void setOrderId(String order_id)
    {
        this.order_id = order_id;
    }

    public String getOrderId()
    {
        return this.order_id;
    }


    public void setItemCode(int item_code)
    {
        this.item_code = item_code;
    }

    public int getItemCode()
    {
        return this.item_code;
    }


    public void setDatetime(String datetime)
    {
        this.datetime = datetime;
    }

    public String getDatetime()
    {
        return this.datetime;
    }


    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return this.status;
    }


    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }

    public int getQuantity()
    {
        return this.quantity;
    }


    public void setTableNo(int table_no)
    {
        this.table_no = table_no;
    }

    public int getTableNo()
    {
        return this.table_no;
    }

    public void setWaiterId(String waiter_id)
    {
        this.waiter_id = waiter_id;
    }

    public String getWaiterId()
    {
        return this.waiter_id;
    }
}
