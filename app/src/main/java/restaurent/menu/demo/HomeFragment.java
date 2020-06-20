package restaurent.menu.demo;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import restaurent.menu.demo.app.MyApplication;
import restaurent.menu.demo.configuration.ConnectionString;
import restaurent.menu.demo.helper.Helper;
import restaurent.menu.demo.helper.MenuItem;
import restaurent.menu.demo.sqlitedb.SQLiteDatabaseHelper;


public class HomeFragment extends Fragment implements View.OnClickListener
{

    private Button button1, button2, button3, button4, button5, button6;

    public static int table_no;

    private Context context = null;
    private ProgressDialog pDialog;


    private String URL_TOP_250 = new ConnectionString().getURL() + "ItemList.php?offset=";

    // initially offset will be 0, later will be updated while parsing the json
    private int offSet = 0;

    private static final int MAX_ATTEMPTS = 5;
    private int ATTEMPTS_COUNT;


    public HomeFragment()
    {

    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        context = this.getActivity();
    }


	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
 
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        findViewById(rootView);
        addOnClickListener();

        if(new SQLiteDatabaseHelper(getActivity()).dbRowCount(SQLiteDatabaseHelper.TABLE_ITEM) == 0)
        {
            initProgressDialog("Sync database.. Please wait ");
            fetchCategoryItems();
        }

        return rootView;
    }


    private void findViewById(View rootView)
    {

        button1 = (Button) rootView.findViewById(R.id.cp_button1);
        button2 = (Button) rootView.findViewById(R.id.cp_button2);
        button3 = (Button) rootView.findViewById(R.id.cp_button3);
        button4 = (Button) rootView.findViewById(R.id.cp_button4);
        button5 = (Button) rootView.findViewById(R.id.cp_button5);
        button6 = (Button) rootView.findViewById(R.id.cp_button6);
    }


    private void addOnClickListener()
    {

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
    }


    public void onClick(View view)
    {

        //Bundle args = new Bundle();

        switch (view.getId())
        {

            case R.id.cp_button1:

                table_no = 1;
                //args.putInt("TABLE_NO", 1);
                break;

            case R.id.cp_button2:

                table_no = 2;
                //args.putInt("TABLE_NO", 2);
                break;

            case R.id.cp_button3:

                table_no = 3;
                //args.putInt("TABLE_NO", 3);
                break;

            case R.id.cp_button4:

                table_no = 4;
                //args.putInt("TABLE_NO", 4);
                break;

            case R.id.cp_button5:

                table_no = 5;
                //args.putInt("TABLE_NO", 5);
                break;

            case R.id.cp_button6:

                table_no = 6;
                //args.putInt("TABLE_NO", 6);
                break;
        }


        Fragment fragment = new ItemFragment();
        //fragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_anim, R.anim.exit_anim);
        fragmentTransaction.replace(R.id.container_body, fragment);
        fragmentTransaction.commit();
    }


    /**
     * Fetching movies json by making http call
     */
    public void fetchCategoryItems()
    {

        // appending offset to url
        String url = URL_TOP_250 + offSet;

        // Volley's json array request object
        JsonArrayRequest req = new JsonArrayRequest(url,

                new Response.Listener<JSONArray>()
                {

                    @Override
                    public void onResponse(JSONArray response)
                    {

                        if (response.length() > 0)
                        {

                            // looping through json and adding to movies list
                            for (int i = 0; i < response.length(); i++)
                            {

                                try
                                {

                                    JSONObject categoryObj = response.getJSONObject(i);

                                    int id = categoryObj.getInt("id");
                                    int item_code = categoryObj.getInt("item_code");
                                    String type = categoryObj.getString("type");
                                    String name = categoryObj.getString("name");
                                    float price = (float)categoryObj.getDouble("price");


                                    MenuItem category = new MenuItem(item_code, type, name, price);


                                    new SQLiteDatabaseHelper(context).insertItem(category);

                                    // updating offset value to highest value
                                    if (id >= offSet)
                                    {
                                        offSet = id;
                                    }

                                    myAsyncTask myWebFetch = new myAsyncTask();
                                    myWebFetch.execute(ConnectionString.IMAGE_URL + category.item_type + ".jpg", category.item_type + ".jpg");
                                }

                                catch (JSONException e)
                                {

                                }
                            }
                        }

                        makeToast("Database sync successful");

                        if(pDialog.isShowing())
                        {
                            pDialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error)
            {

                try
                {

                    if(ATTEMPTS_COUNT != MAX_ATTEMPTS)
                    {

                        fetchCategoryItems();

                        ATTEMPTS_COUNT ++;

                        Log.v("#Attempt No: ", "" + ATTEMPTS_COUNT);
                        return;
                    }

                    makeToast("Failed to sync database. Swipe to sync again");

                    if(pDialog.isShowing())
                    {
                        pDialog.dismiss();
                    }
                }

                catch (Exception e)
                {

                }
            }
        });


        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(req);
    }


    private  void makeToast(String msg)
    {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }


    private void initProgressDialog(String message)
    {

        pDialog = new ProgressDialog(context);
        pDialog.setMessage(message);
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }


    class myAsyncTask extends AsyncTask<String, Void, Void>
    {

        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);
        }


        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }


        protected Void doInBackground(String... args)
        {

            try
            {

                //set the download URL, a url that points to a file on the internet
                //this is the file to be downloaded
                URL url = new URL(args[0]);

                //create the new connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //set up some things on the connection
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoOutput(true);

                //and connect!
                urlConnection.connect();

                //set the path where we want to save the file
                //in this case, going to save it on the root directory of the
                //sd card.
                //File SDCardRoot = Environment.getExternalStorageDirectory();

                // External sdcard location
                File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), Helper.IMAGE_DIRECTORY_NAME);

                // Create the storage directory if it does not exist
                if (!mediaStorageDir.exists())
                {
                    if (!mediaStorageDir.mkdirs())
                    {
                        Log.d(Helper.IMAGE_DIRECTORY_NAME, "Oops! Failed create " + Helper.IMAGE_DIRECTORY_NAME + " directory");
                        return null;
                    }
                }

                //create a new file, specifying the path, and the filename
                //which we want to save the file as.
                File file = new File(mediaStorageDir, args[1]);

                //this will be used to write the downloaded data into the file we created
                FileOutputStream fileOutput = new FileOutputStream(file);

                //this will be used in reading the data from the internet
                InputStream inputStream = urlConnection.getInputStream();

                //this is the total size of the file
                int totalSize = urlConnection.getContentLength();

                //variable to store total downloaded bytes
                int downloadedSize = 0;

                //create a buffer...
                byte[] buffer = new byte[1024];
                int bufferLength = 0; //used to store a temporary size of the buffer

                //now, read through the input buffer and write the contents to the file
                while ( (bufferLength = inputStream.read(buffer)) > 0 )
                {

                    //add the data in the buffer to the file in the file output stream (the file on the sd card
                    fileOutput.write(buffer, 0, bufferLength);

                    //add up the size so we know how much is downloaded
                    downloadedSize += bufferLength;

                    //this is where you would do something to report the prgress, like this maybe
                    //updateProgress(downloadedSize, totalSize);
                }

                //close the output stream when done
                fileOutput.close();

                //catch some possible errors...
            }

            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }

            catch (IOException e)
            {
                e.printStackTrace();
            }

            return null;
        }
    }
}