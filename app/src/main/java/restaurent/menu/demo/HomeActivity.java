package restaurent.menu.demo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;


public class HomeActivity extends AppCompatActivity //implements NavigationDrawerCallbacks
{


    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    //private NavigationDrawerFragment mNavigationDrawerFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);

        //mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.fragment_drawer);

        // Set up the drawer.
        //mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);

        displayView(0);
    }


    /*@Override
    public void onNavigationDrawerItemSelected(int position)
    {

        displayView(position);
    }*/


    @Override
    public void onBackPressed()
    {

        /*if (mNavigationDrawerFragment.isDrawerOpen())
        {
            mNavigationDrawerFragment.closeDrawer();
        }*/

        Fragment f = getSupportFragmentManager().findFragmentById(R.id.container_body);

        if (f instanceof HomeFragment)
        {
            super.onBackPressed();
        }

        if( f instanceof ItemDetailsFragment)
        {
            displayView(1);
        }

        else if (f instanceof ItemFragment)
        {
            displayView(0);
        }

        else if (f instanceof ItemListFragment)
        {
            displayView(1);
        }

        /*else if (f instanceof ItemDetailsFragment)
        {
            displayView(2);
        }*/
    }


    private void displayView(int position)
    {

        Fragment fragment = null;
        String title = getString(R.string.app_name);

        switch (position)
        {

            case 0:

                fragment = new HomeFragment();
                break;

            case 1:

                fragment = new ItemFragment();
                break;

            case 2:

                fragment = new ItemListFragment();
                break;

            default:

                break;
        }


        if (fragment != null)
        {

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.enter_anim, R.anim.exit_anim);
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            setTitle(title);
        }
    }
}