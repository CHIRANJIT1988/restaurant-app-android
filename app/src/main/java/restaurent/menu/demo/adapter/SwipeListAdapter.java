package restaurent.menu.demo.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import restaurent.menu.demo.R;
import restaurent.menu.demo.app.MyApplication;
import restaurent.menu.demo.configuration.ConnectionString;
import restaurent.menu.demo.helper.MenuItem;
import restaurent.menu.demo.helper.Helper;


/**
 * Created by Ravi on 13/05/15.
 */

public class SwipeListAdapter extends BaseAdapter
{

    private Activity activity;
    private LayoutInflater inflater;
    private List<MenuItem> categoryList;

    ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();


    public SwipeListAdapter(Activity activity, List<MenuItem> categoryList)
    {
        this.activity = activity;
        this.categoryList = categoryList;
    }


    @Override
    public int getCount()
    {
        return categoryList.size();
    }


    @Override
    public Object getItem(int location) {
        return categoryList.get(location);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        if (inflater == null)
        {
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.list_row, null);
        }


        if (imageLoader == null)
        {
            imageLoader = MyApplication.getInstance().getImageLoader();
        }


        //NetworkImageView thumbNail = (NetworkImageView) convertView.findViewById(R.id.thumbnail);
        ImageView thumbNail = (ImageView) convertView.findViewById(R.id.thumbnail);
        TextView type = (TextView) convertView.findViewById(R.id.name);
        //TextView description = (TextView) convertView.findViewById(R.id.description);


        MenuItem category = categoryList.get(position);


        String URL = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/"
                    + Helper.IMAGE_DIRECTORY_NAME + "/" + category.item_type + ".jpg";


        // Bitmap bm = BitmapFactory.decodeResource(convertView.getResources(), R.drawable.category);
        // thumbNail.setImageBitmap(getCircleBitmap(bm));

        // thumbnail image
        //thumbNail.setImageUrl(ConnectionString.IMAGE_URL + category.name + ".jpg", imageLoader);

        File imgFile = new File(URL);
        {

            if(imgFile.exists())
            {

                try
                {
                    Bitmap bmp = BitmapFactory.decodeFile(URL);
                    thumbNail.setImageBitmap(bmp);//getCircleBitmap(bmp));
                }

                catch(Exception e)
                {

                }
            }
        }


        type.setText(Helper.toCamelCase(category.item_type));
        //description.setText(Helper.toCamelCase(category.description));


        // create bitmap from resource
        // Bitmap bm = BitmapFactory.decodeResource(convertView.getResources(), R.drawable.drain);
        // ivCategory.setImageBitmap(getCircleBitmap(bm));

        // Bitmap bm = getBitmapFromURL(category.getThumbnailUrl());

        return convertView;
    }



    private Bitmap getCircleBitmap(Bitmap bitmap)
    {

        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        final Canvas canvas = new Canvas(output);

        final int color = Color.RED;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        bitmap.recycle();

        return output;
    }


    /*public static Bitmap getBitmapFromURL(String src)
    {

        try
        {

            URL url = new URL(src);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();

            InputStream input = connection.getInputStream();

            Bitmap myBitmap = BitmapFactory.decodeStream(input);

            return myBitmap;
        }

        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }*/
}