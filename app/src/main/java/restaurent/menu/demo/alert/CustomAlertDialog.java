package restaurent.menu.demo.alert;


import restaurent.menu.demo.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Context;


public class CustomAlertDialog 
{

	private Context context;
	
	
	public CustomAlertDialog(Context _context)
	{
		this.context = _context;
	}
	

	

	public void showAlertDialog(String title, String message, Boolean status) 
	{
		
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();

		// Setting Dialog Title
		alertDialog.setTitle(title);

		// Setting Dialog Message
		alertDialog.setMessage(message);
			
		// Setting Cancelable
		alertDialog.setCancelable(true);
		
		// Setting alert dialog icon
		alertDialog.setIcon((status) ? R.drawable.ic_checkbox_marked_circle_grey600_24dp : R.drawable.ic_close_circle_grey600_24dp);

		// Setting OK Button
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which)
			{
				dialog.cancel();
			}

		});

		// Showing Alert Message
		alertDialog.show();	
	}
}