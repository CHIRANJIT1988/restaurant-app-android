package restaurent.menu.demo;


import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
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
import java.util.ArrayList;
import java.util.List;

import restaurent.menu.demo.app.MyApplication;
import restaurent.menu.demo.configuration.ConnectionString;
import restaurent.menu.demo.helper.MenuItem;
import restaurent.menu.demo.adapter.SwipeListAdapter;
import restaurent.menu.demo.helper.Helper;
import restaurent.menu.demo.sqlitedb.SQLiteDatabaseHelper;


/**
 * Created by dell on 19-06-2015.
 */

public class ItemFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, AbsListView.OnScrollListener {


    private String URL_TOP_250 = new ConnectionString().getURL() + "ItemList.php?offset=";

    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;
    private SwipeListAdapter adapter;
    private List<MenuItem> categoryList;


    // initially offset will be 0, later will be updated while parsing the json
    private int offSet = 0;


    private int lastTopValue = 0;
    private ImageView backgroundImage;

    private Context context = null;


    public ItemFragment()
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


        View rootView = inflater.inflate(R.layout.fragment_item, container, false);

        listView = (ListView) rootView.findViewById(R.id.listView);

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);


        categoryList = new ArrayList<>();
        adapter = new SwipeListAdapter(getActivity(), categoryList);
        listView.setAdapter(adapter);


        // inflate custom header and attach it to the list
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.custom_header, listView, false);
        listView.addHeaderView(header, null, false);

        // we take the background image and button reference from the header
        backgroundImage = (ImageView) header.findViewById(R.id.listHeaderImage);
        listView.setOnScrollListener(this);


        swipeRefreshLayout.setOnRefreshListener(this);


        if(new SQLiteDatabaseHelper(getActivity()).dbRowCount(SQLiteDatabaseHelper.TABLE_ITEM) == 0) {

            swipeRefreshLayout.post(new Runnable() {

                                        @Override
                                        public void run() {
                                            swipeRefreshLayout.setRefreshing(true);
                                            fetchCategoryItems();
                                        }
                                    }
            );
        }


        else
        {
            fetchSQLIteCategory();
        }


        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
        {

            listView.smoothScrollToPositionFromTop(1, -1, 500);
        }

        else
        {
            listView.smoothScrollToPositionFromTop(0, -1, 500);
        }

        return rootView;
    }


    public void onClick(View v)
    {

    }


    /**
     * This method is called when swipe refresh is pulled down
     */

    @Override
    public void onRefresh()
    {
        fetchCategoryItems();
    }


    /**
     * Fetching movies json by making http call
     */
    public void fetchCategoryItems()
    {

        // showing refresh animation before making http call
        swipeRefreshLayout.setRefreshing(true);

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

                            categoryList.clear();

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

                                    categoryList.add(0, category);


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

                            adapter.notifyDataSetChanged();

                            //new SQLiteDatabaseHelper(getActivity()).deleteRecord(SQLiteDatabaseHelper.TABLE_CATEGORY);

                            //new SQLiteDatabaseHelper(getActivity()).insertCategory(categoryList);
                        }

                        // stopping swipe refresh
                        swipeRefreshLayout.setRefreshing(false);

                        //TransparentActivity.myActivity.finish();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error)
            {

                makeToast("Failed to Fetch Menu");

                //TransparentActivity.myActivity.finish();

                // stopping swipe refresh
                swipeRefreshLayout.setRefreshing(false);
            }
        });


        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(req);
    }


    private  void makeToast(String msg)
    {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }


    private void redirectComplainFragment(MenuItem category)
    {

        Bundle args = new Bundle();
        args.putString("ITEM_TYPE", category.getItemType());
        //args.putInt("TABLE_NO", this.getArguments().getInt("TABLE_NO"));

        Fragment fragment = new ItemListFragment();
        fragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_anim, R.anim.exit_anim);
        fragmentTransaction.replace(R.id.container_body, fragment);
        fragmentTransaction.commit();
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
    {

        Rect rect = new Rect();
        backgroundImage.getLocalVisibleRect(rect);

        if (lastTopValue != rect.top)
        {
            lastTopValue = rect.top;
            backgroundImage.setY((float) (rect.top / 2.0));
        }
    }



    private void fetchSQLIteCategory()
    {

        categoryList = new SQLiteDatabaseHelper(getActivity()).getAllItemType();

        adapter = new SwipeListAdapter(getActivity(), categoryList);

        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                redirectComplainFragment(categoryList.get(position - 1));
            }
        });
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


    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {

        super.onConfigurationChanged(newConfig);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            listView.smoothScrollToPositionFromTop(1, -1, 500);
        }

        else
        {
            listView.smoothScrollToPositionFromTop(0, -1, 500);
        }
    }
}