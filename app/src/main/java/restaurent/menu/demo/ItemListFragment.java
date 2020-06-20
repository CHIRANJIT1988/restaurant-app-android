package restaurent.menu.demo;

import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import restaurent.menu.demo.adapter.SwipeListMenuItemsAdapter;
import restaurent.menu.demo.helper.MenuItem;
import restaurent.menu.demo.sqlitedb.SQLiteDatabaseHelper;

/**
 * Created by dell on 23-06-2015.
 */
public class ItemListFragment extends Fragment implements AbsListView.OnScrollListener, View.OnClickListener{


    private int lastTopValue = 0;

    private List<MenuItem> modelList = new ArrayList<MenuItem>();

    private ListView listView;
    private ImageView backgroundImage;
    private SwipeListMenuItemsAdapter adapter;


    public ItemListFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_item_list, container, false);


        String item_type = this.getArguments().getString("ITEM_TYPE");

        modelList = new SQLiteDatabaseHelper(getActivity()).getAllItem(item_type);



        listView = (ListView) rootView.findViewById(R.id.list);

        adapter = new SwipeListMenuItemsAdapter(getActivity(), modelList);
        listView.setAdapter(adapter);

        // inflate custom header and attach it to the list
        // LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.custom_header, listView, false);
        listView.addHeaderView(header, null, false);

        // we take the background image and button reference from the header
        backgroundImage = (ImageView) header.findViewById(R.id.listHeaderImage);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                redirectComplainFragment(modelList.get(position-1));
            }
        });


        /*String PATH = Environment.getExternalStoragePublicDirectory(Helper.DIRECTORY_NAME) + "/"
                        + Helper.IMAGE_DIRECTORY_NAME + "/" + category.getThumbnailUrl();


        File imgFile = new File(PATH);
        {

            if(imgFile.exists())
            {

                try
                {
                    Bitmap bmp = BitmapFactory.decodeFile(PATH);
                    backgroundImage.setImageBitmap(bmp);
                }

                catch(Exception e)
                {

                }
            }
        }*/


        listView.setOnScrollListener(this);


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
        //redirectComplainFragment();
    }


    private void redirectComplainFragment(MenuItem item)
    {

        Bundle args = new Bundle();
        args.putInt("ITEM", item.getId());
        //args.putInt("TABLE_NO", this.getArguments().getInt("TABLE_NO"));


        Fragment fragment = new ItemDetailsFragment();
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


    @Override
    public void onConfigurationChanged(Configuration newConfig) {

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