package restaurent.menu.demo.mysqldb.send;

import restaurent.menu.demo.app.MyApplication;
import restaurent.menu.demo.configuration.ConnectionString;
import restaurent.menu.demo.helper.OnTaskCompleted;
import restaurent.menu.demo.helper.Order;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;


public class SendOrderInfo
{

	private OnTaskCompleted listener;
	
	private String URL = "";

	private Context context;
	
	Order order;

	private static final int MAX_ATTEMPTS = 5;
	private int ATTEMPTS_COUNT;


	public SendOrderInfo(Context _context , OnTaskCompleted listener)
	{
		this.listener = listener;
		this.context = _context;
		this.URL = new ConnectionString().getURL() + "SyncOrderDetails.php";
	}
	
	
	public void sendOrderInfo(Order order)
	{
		
		this.order = order;
		execute();
	}


	public void execute()
	{

		StringRequest postRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

			@Override
			public void onResponse(String response)
			{

				try
				{

					JSONObject jsonObj = new JSONObject(response);

					boolean success = jsonObj.getBoolean("success");
					String message = jsonObj.getString("message");

					Log.v("Response: ", response);

					if(success)
					{
						listener.onTaskCompleted(true, message); // Successful
					}

					else
					{

						if(ATTEMPTS_COUNT != MAX_ATTEMPTS)
						{

							execute();

							ATTEMPTS_COUNT ++;

							Log.v("#Attempt No: ", "" + ATTEMPTS_COUNT);
							return;
						}

						listener.onTaskCompleted(false, message); // Unsuccessful
					}
				}

				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error)
			{
				listener.onTaskCompleted(false, "Internet connection fail. Try again");
			}
		})

		{

			@Override
			protected Map<String, String> getParams()
			{

				Map<String, String> params = new HashMap<>();

				params.put("order_id", order.getOrderId());
				params.put("item_code", String.valueOf(order.getItemCode()));
				params.put("quantity", String.valueOf(order.getQuantity()));
				params.put("table_no", String.valueOf(order.getTableNo()));
				params.put("datetime", order.getDatetime());
				params.put("waiter_id", order.getWaiterId());

				Log.v("Waiter ", order.getWaiterId());

				return params;
			}
		};

		// Adding request to request queue
		MyApplication.getInstance().addToRequestQueue(postRequest);
	}
}