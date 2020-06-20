package restaurent.menu.demo;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import restaurent.menu.demo.helper.OnTaskCompleted;
import restaurent.menu.demo.helper.Employee;
import restaurent.menu.demo.mysqldb.send.UserLogin;
import restaurent.menu.demo.network.InternetConnectionDetector;
import restaurent.menu.demo.session.SessionManager;


public class LoginFragment extends Fragment implements OnClickListener, OnTaskCompleted
{

    private Button btnLogin, btnNewUser;
    private EditText editPhone;
    private ImageView ivLogo;

    private ProgressBar pBar;

    SessionManager session; // Session Manager Class

    private ScrollView scrollableContents;

    private RelativeLayout relativeLayout;

    private Context context = null;


	public LoginFragment()
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
 
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        findViewById(rootView);

        btnLogin.setOnClickListener(this);
        btnNewUser.setOnClickListener(this);


        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            ivLogo.setVisibility(View.GONE);
        }

        else
        {
            ivLogo.setVisibility(View.VISIBLE);
        }


        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String number = tm.getLine1Number();

        if(number != null)
        {
            editPhone.setText(number);

        }
        return rootView;
    }


    /** Called when the activity is about to become visible. */
    @Override
    public void onStart()
    {

        super.onStart();
        Log.d("Inside : ", "onStart() event");

        session = new SessionManager(getActivity()); // Session Manager
    }



    /** Called when the activity has become visible. */
    @Override
    public void onResume()
    {
        super.onResume();
        Log.d("Inside : ", "onResume() event");
    }


    /** Called when another activity is taking focus. */
    @Override
    public void onPause()
    {
        super.onPause();
        Log.d("Inside : ", "onPause() event");
    }


    /** Called when the activity is no longer visible. */
    @Override
    public void onStop()
    {
        super.onStop();
        Log.d("Inside : ", "onStop() event");
    }


    //** Called just before the activity is destroyed. */
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Log.d("Inside : ", "onDestroy() event");
    }


    private void findViewById(View rootView)
    {

        btnLogin = (Button) rootView.findViewById(R.id.btnLogin);
        btnNewUser = (Button) rootView.findViewById(R.id.btnNewUser);
        editPhone = (EditText) rootView.findViewById(R.id.editPhoneNumber);

        pBar = (ProgressBar) rootView.findViewById(R.id.pbLoading);


        ivLogo = (ImageView) rootView.findViewById(R.id.imgLogo);
        scrollableContents = (ScrollView) rootView.findViewById(R.id.scrollableContents);

        relativeLayout = (RelativeLayout) rootView.findViewById(R.id.layout_main);
    }


    public void onClick(View v)
    {

        switch (v.getId())
        {

            case R.id.btnLogin:

                if(validateForm())
                {

                    if (!new InternetConnectionDetector(getActivity()).isConnected())
                    {
                        makeSnackbar("Internet Connection Fail");
                        break;
                    }

                    pBar.setVisibility(View.VISIBLE);

                    scrollContentDown();

                    new UserLogin(getActivity().getApplicationContext(), this).checkUserDetails(initUserObject().getMobileNo());
                }

                break;

            case R.id.btnNewUser:

                Fragment fragment = new RegisterFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.setCustomAnimations(R.anim.enter_anim, R.anim.exit_anim);
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.commit();
                break;
        }
    }


    private boolean validateForm()
    {

        if(editPhone.getText().toString().trim().length() != 10)
        {

            makeSnackbar("Invalid Phone Number");
            return false;
        }

        return  true;
    }


    private void makeSnackbar(String msg)
    {

        Snackbar snackbar = Snackbar.make(relativeLayout, msg, Snackbar.LENGTH_SHORT);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(getResources().getColor(R.color.myPrimaryColor));
        snackbar.show();
    }


    private Employee initUserObject()
    {

        Employee user = new Employee();

        user.setMobileNo(editPhone.getText().toString());

        return user;
    }


    @Override
    public void onTaskCompleted(boolean flag, String message)
    {

        pBar.setVisibility(View.GONE);

        if(flag)
        {

            Employee user = initUserObject();

            session.createLoginSession(user.getMobileNo(), "12345");

            startActivity(new Intent(getActivity(), HomeActivity.class));
            getActivity().finish();
        }

        else
        {
            makeSnackbar(message);
            scrollContentUp();
        }
    }


    private void scrollContentDown()
    {

        scrollableContents.post(new Runnable() {

            @Override
            public void run()
            {
                scrollableContents.fullScroll(View.FOCUS_DOWN);
            }
        });
    }


    private void scrollContentUp()
    {

        scrollableContents.post(new Runnable() {

            @Override
            public void run()
            {
                scrollableContents.fullScroll(View.FOCUS_UP);
            }
        });
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {

        super.onConfigurationChanged(newConfig);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            ivLogo.setVisibility(View.GONE);
        }

        else
        {
            ivLogo.setVisibility(View.VISIBLE);
        }
    }
}