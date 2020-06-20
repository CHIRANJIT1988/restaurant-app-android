package restaurent.menu.demo.mysqldb.send;

import restaurent.menu.demo.app.MyApplication;
import restaurent.menu.demo.helper.Employee;
import restaurent.menu.demo.helper.OnTaskCompleted;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import static restaurent.menu.demo.configuration.ConnectionString.API_URL;


public class UserLogin
{

	private OnTaskCompleted listener;
	
	private String URL = "";

	private Context context;
	
	private String mobile_no;

	SharedPreferences prefs = null;
	


	public UserLogin(Context _context , OnTaskCompleted listener)
	{

		this.listener = listener;
		this.context = _context;

		this.URL = API_URL + "UserLogin.php";

		prefs = context.getSharedPreferences("restaurent.demo.config", Context.MODE_PRIVATE);
	}
	


	public void checkUserDetails(String mobile_no)
	{

		this.mobile_no = mobile_no;
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

					boolean login = jsonObj.getBoolean("login");
					String message = jsonObj.getString("message");

					if (login) // checking for error node in json
					{
						listener.onTaskCompleted(true, message); // Valid Employee
						prefs.edit().putString("user_name", message).apply();
					}

					else
					{
						listener.onTaskCompleted(false, message); // Invalid Employee
					}
				}

				catch (JSONException e)
				{
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error)
			{
				listener.onTaskCompleted(false, error.getMessage());
			}
		})

		{

			@Override
			protected Map<String, String> getParams()
			{

				Map<String, String> params = new HashMap<>();

				params.put("phone_no", mobile_no);

				return params;
			}
		};

		// Adding request to request queue
		MyApplication.getInstance().addToRequestQueue(postRequest);
	}
}