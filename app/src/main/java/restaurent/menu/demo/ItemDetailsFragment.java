package restaurent.menu.demo;


import static restaurent.menu.demo.HomeFragment.table_no;

import android.content.Context;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import restaurent.menu.demo.alert.CustomAlertDialog;
import restaurent.menu.demo.helper.GenerateUniqueId;
import restaurent.menu.demo.helper.OnTaskCompleted;
import restaurent.menu.demo.helper.Order;
import restaurent.menu.demo.mysqldb.send.SendOrderInfo;
import restaurent.menu.demo.session.SessionManager;


public class ItemDetailsFragment extends Fragment implements View.OnClickListener, OnTaskCompleted
{

    private Button btnPlaceOrder;
    private RadioButton radio1, radio2, radio3, radio4, radio5;

    SessionManager session;

    private Context context = null;


	public ItemDetailsFragment()
    {

    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        session = new SessionManager(getActivity()); // Session Manager
        context = this.getActivity();
    }


	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
 
        View rootView = inflater.inflate(R.layout.fragment_item_datails, container, false);

        btnPlaceOrder = (Button) rootView.findViewById(R.id.btnPlaceOrder);

        radio1 = (RadioButton) rootView.findViewById(R.id.radio1);
        radio2 = (RadioButton) rootView.findViewById(R.id.radio2);
        radio3 = (RadioButton) rootView.findViewById(R.id.radio3);
        radio4 = (RadioButton) rootView.findViewById(R.id.radio4);
        radio5 = (RadioButton) rootView.findViewById(R.id.radio5);

        btnPlaceOrder.setOnClickListener(this);
        radio1.setOnClickListener(this);
        radio2.setOnClickListener(this);
        radio3.setOnClickListener(this);
        radio4.setOnClickListener(this);
        radio5.setOnClickListener(this);

        return rootView;
    }


    public void onClick(View v)
    {


        if(v.getId() == R.id.btnPlaceOrder && validate())
        {

            makeToast("Placing order. Please wait..");
            new SendOrderInfo(getActivity().getApplicationContext(), this).sendOrderInfo(initOrderObject());
        }
    }


    private Order initOrderObject()
    {

        Order order = new Order();

        order.setOrderId(GenerateUniqueId.generateOrderId(table_no));
        order.setItemCode(this.getArguments().getInt("ITEM"));

        if(radio1.isChecked())
        {
            order.setQuantity(1);
        }

        if(radio2.isChecked())
        {
            order.setQuantity(2);
        }

        if(radio3.isChecked())
        {
            order.setQuantity(3);
        }

        if(radio4.isChecked())
        {
            order.setQuantity(4);
        }

        if(radio5.isChecked())
        {
            order.setQuantity(5);
        }

        order.setTableNo(table_no);
        order.setDatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        order.setWaiterId(getMobileNumber());

        return order;
    }


    private String getMobileNumber()
    {

        // Check for logged in
        session.checkLogin();

        // Get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // Ruturn user
        return user.get(SessionManager.KEY_PHONE);
    }

    private boolean validate()
    {

        if(!radio1.isChecked() && !radio2.isChecked() && !radio3.isChecked() && !radio4.isChecked()&& !radio5.isChecked())
        {
            return false;
        }

        return  true;
    }


    private void makeToast(String msg)
    {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onTaskCompleted(boolean flag, String message)
    {

        if(flag)
        {

            new CustomAlertDialog(context).showAlertDialog("Success", message, flag);
            vibrate();

            Fragment fragment = new ItemFragment();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.enter_anim, R.anim.exit_anim);
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();
        }

        else
        {
            new CustomAlertDialog(context).showAlertDialog("Fail", message, flag);
        }
    }


    private void vibrate()
    {

        Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);

        // If you want to vibrate  in a pattern
        long pattern[] = { 0, 200, 100, 200 };

        // 2nd argument is for repetition pass -1 if you do not want to repeat the Vibrate
        v.vibrate(pattern,-1);
    }
}