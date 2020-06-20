package restaurent.menu.demo;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import restaurent.menu.demo.helper.Employee;
import restaurent.menu.demo.helper.OnTaskCompleted;
import restaurent.menu.demo.mysqldb.send.SendUserInfo;
import restaurent.menu.demo.network.InternetConnectionDetector;
import restaurent.menu.demo.session.SessionManager;


public class RegisterFragment extends Fragment implements View.OnClickListener, OnTaskCompleted
{

    private Button btnRegister;
    private EditText editEmployeeName, editMobileNo;

    private ProgressBar pBar;

    SessionManager session; // Session Manager Class

    private ImageView ivLogo;

    private ScrollView scrollableContents;

    private RelativeLayout relativeLayout;

    private Context context = null;



	public RegisterFragment()
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

        View rootView = inflater.inflate(R.layout.fragment_register, container, false);

        session = new SessionManager(getActivity()); // Session Manager

        findViewById(rootView);

        btnRegister.setOnClickListener(this);


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
            editMobileNo.setText(number);

        }


        hideKeyboard(rootView);

        return rootView;
    }



    private void findViewById(View rootView)
    {

        btnRegister = (Button) rootView.findViewById(R.id.btnRegister);
        editEmployeeName = (EditText) rootView.findViewById(R.id.editEmployeeName);
        editMobileNo = (EditText) rootView.findViewById(R.id.editPhoneNumber);

        pBar = (ProgressBar) rootView.findViewById(R.id.pbLoading);

        ivLogo = (ImageView) rootView.findViewById(R.id.imgLogo);
        scrollableContents = (ScrollView) rootView.findViewById(R.id.scrollableContents);

        relativeLayout = (RelativeLayout) rootView.findViewById(R.id.layout_main);
    }


    public void onClick(View v)
    {

        if(validateForm())
        {

            if (!new InternetConnectionDetector(getActivity()).isConnected())
            {
                makeSnackbar("Internet Connection Fail");
                return;
            }

            pBar.setVisibility(View.VISIBLE);

            scrollContentDown();

            new SendUserInfo(getActivity().getApplicationContext(), this).saveUserInfo(initEmployeeObject());
        }
    }


    private boolean validateForm()
    {

        if(editEmployeeName.getText().toString().trim().length() < 5)
        {
            makeSnackbar("Name should be atleast 5 character long");
            return false;
        }

        if(editMobileNo.getText().toString().trim().length() < 10)
        {
            makeSnackbar("Invalid Mobile Number");
            return false;
        }

        return  true;
    }


    private Employee initEmployeeObject()
    {

        Employee employee = new Employee();

        employee.setEmployeeName(editEmployeeName.getText().toString());
        employee.setMobileNo(editMobileNo.getText().toString());

        return employee;
    }


    /*private void makeToast(String msg)
    {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }*/


    private void makeSnackbar(String msg)
    {

        Snackbar snackbar = Snackbar.make(relativeLayout, msg, Snackbar.LENGTH_SHORT);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(getResources().getColor(R.color.myPrimaryColor));
        snackbar.show();
    }


    @Override
    public void onTaskCompleted(boolean flag, String message)
    {

        pBar.setVisibility(View.GONE);

        if(flag)
        {
            startActivity(new Intent(getActivity(), HomeActivity.class));
            session.createLoginSession(initEmployeeObject().getMobileNo(), "12345");
            getActivity().finish();
        }

        else
        {
            scrollContentUp();
            makeSnackbar(message);
        }
    }


    private void scrollContentDown()
    {

        scrollableContents.post(new Runnable() {

            @Override
            public void run() {

                scrollableContents.fullScroll(View.FOCUS_DOWN);
            }
        });
    }


    private void scrollContentUp()
    {

        scrollableContents.post(new Runnable() {

            @Override
            public void run() {
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


    private void hideKeyboard(final View rootView)
    {

        editMobileNo.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (editMobileNo.getText().toString().trim().length() == 10) {
                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(rootView.getWindowToken(), 0);
                }
            }
        });
    }
}