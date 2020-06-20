package restaurent.menu.demo.mysqldb.send;

import restaurent.menu.demo.app.MyApplication;
import restaurent.menu.demo.helper.OnTaskCompleted;
import restaurent.menu.demo.helper.Employee;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import static restaurent.menu.demo.configuration.ConnectionString.API_URL;


public class SendUserInfo
{

	private OnTaskCompleted listener;
	
	private String URL = "";

	private Context context;
	
	Employee emp;
	
	
	public SendUserInfo(Context _context , OnTaskCompleted listener)
	{
		this.listener = listener;
		this.context = _context;

		this.URL = API_URL + "Register.php";
	}
	
	
	public void saveUserInfo(Employee emp)
	{
		
		this.emp = emp;
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

					boolean error = jsonObj.getBoolean("error");
					String message = jsonObj.getString("message");


					if (!error) // checking for error node in json
					{
						listener.onTaskCompleted(true, message); // Successfull
					}

					else
					{
						listener.onTaskCompleted(false, message); // Unsuccessfull
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

				params.put("name", emp.getEmployeeName());
				params.put("phone_no", emp.getMobileNo());

				return params;
			}
		};

		// Adding request to request queue
		MyApplication.getInstance().addToRequestQueue(postRequest);
	}
}