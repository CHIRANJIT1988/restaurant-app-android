package restaurent.menu.demo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import restaurent.menu.demo.session.SessionManager;


public class MainActivity extends AppCompatActivity //implements NavigationDrawerCallbacks
{

    private Toolbar mToolbar;

    SessionManager session; // Session Manager Class
    SharedPreferences prefs = null;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);


        session = new SessionManager(getApplicationContext()); // Session Manager
        prefs = getSharedPreferences("restaurent.demo.config", MODE_PRIVATE);


        if (prefs.getBoolean("firstrun", true))
        {

            prefs.edit().putBoolean("firstrun", false).commit();
        }


        if(session.checkLogin())
        {

            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }

        else
        {

            Fragment fragment = new RegisterFragment();

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.setCustomAnimations(R.anim.enter_anim, R.anim.exit_anim);
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();
        }
    }


    /*@Override
    public void onNavigationDrawerItemSelected(int position)
    {

        // update the main content by replacing fragments
        // displayView(position);
    }*/


    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }
}