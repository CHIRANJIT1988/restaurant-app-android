package restaurent.menu.demo.session;

import java.util.HashMap;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class SessionManager 
{
	
	SharedPreferences pref; // Shared Preferences
	
	Editor editor; // Editor for Shared preferences
	
	Context _context; // Context
	
	int PRIVATE_MODE = 0; // Shared pref mode
	
	
	private static final String PREF_NAME = "RestaurentPref"; // Sharedpref file name
	
	private static final String IS_LOGIN = "IsLoggedIn"; // All Shared Preferences Keys
	
	public static final String KEY_PASSWORD = "password"; // User id (make variable public to access from outside)
	
	public static final String KEY_PHONE = "phone"; // Password (make variable public to access from outside)
	
	
	@SuppressLint("CommitPrefEdits") 
	public SessionManager(Context context) // Constructor
	{
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}
	
	
	/**
	 * Create login session
	 * */
	
	public void createLoginSession(String phone, String password)
	{
		
		editor.putBoolean(IS_LOGIN, true); // Storing login value as TRUE
		
		editor.putString(KEY_PASSWORD, password); // Storing user id in pref
		
		editor.putString(KEY_PHONE, phone); // Storing password in pref
		
		editor.commit(); // commit changes
		
	}	
	
	
	/**
	 * Check login method wil check user login status
	 * If false it will redirect user to login page
	 * Else won't do anything
	 * */
	
	public boolean checkLogin()
	{
		
		if(!this.isLoggedIn()) // Check login status
		{
			
			/*Intent i = new Intent(_context, MainActivity.class); // user is not logged in redirect him to Login Activity
			
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Closing all the Activities
			
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Add new Flag to start new Activity
			
			_context.startActivity(i); // Staring Login Activity*/
			
			return false;
			
		}
		
		return true;
	}
	
	
	
	/**
	 * Get stored session data
	 * */
	
	public HashMap<String, String> getUserDetails()
	{
		
		HashMap<String, String> user = new HashMap<String, String>();
		
		user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null)); // user id
		user.put(KEY_PHONE, pref.getString(KEY_PHONE, null)); // user password
		
		return user; // return user
	}
	
	
	/**
	 * Clear session details
	 * */
	
	public void logoutUser()
	{
		
		// Clearing all data from Shared Preferences
		editor.clear();
		editor.commit();
		
		
		// After logout redirect user to Loing Activity
		/*Intent i = new Intent(_context, MainActivity.class);

		// Closing all the Activities
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		
		// Add new Flag to start new Activity
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		// Staring Login Activity
		_context.startActivity(i);*/
	
	}
	
	
	/**
	 * Quick check for login
	 * ***/
	
	public boolean isLoggedIn() // Get Login State
	{
		return pref.getBoolean(IS_LOGIN, false);
	}
}