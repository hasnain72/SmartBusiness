package com.example.zafar.sbusiness.activity;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.zafar.sbusiness.R;
import com.example.zafar.sbusiness.SharedPreference.SessionManager;
import com.example.zafar.sbusiness.fragments.HomeFragment;
import com.example.zafar.sbusiness.fragments.Orders;
import com.example.zafar.sbusiness.fragments.Products;
import com.example.zafar.sbusiness.fragments.ProductsWithCategory;
import com.example.zafar.sbusiness.fragments.ProductsWithProductFragment;
import com.example.zafar.sbusiness.fragments.Store;
import com.example.zafar.sbusiness.fragments.WalletFragment;
import com.example.zafar.sbusiness.other.GPSTracker;
import com.example.zafar.sbusiness.other.MySingleton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
        ,HomeFragment.OnFragmentInteractionListener
        ,Products.OnFragmentInteractionListener
        ,Orders.OnFragmentInteractionListener
        ,Store.OnFragmentInteractionListener{

    NavigationView navigationView;
    DrawerLayout drawer;
    Fragment fragmentproduct;

    GPSTracker gps;

    SessionManager session;
    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    private static final String TAG_HOME = "home";
    private static final String TAG_HISTORY = "history";
    private static final String TAG_WALLET = "wallet";
    private static final String TAG_SETTINGS = "settings";
    public static String CURRENT_TAG = TAG_HOME;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;

    @Override
    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        gps = new GPSTracker(getApplicationContext() , this);
        session = new SessionManager(getApplicationContext());
        if(!session.isLoggedIn()){
            session.checkLogin();
            finish();
        }

//        String url = "http://192.168.155.105/android/update_firebase_token.php";
//        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Log.d("REsponseeeeeeeeeeeee", response.toString());
//                Toast.makeText(MainActivity.this, "Response" + response , Toast.LENGTH_SHORT).show();
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e("Error 2: " , error.getMessage().toString());
//                Toast.makeText(getApplicationContext(),
//                        error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        }){
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("token", token);
//                params.put("user_id", "7");
//                return params;
//            }
//        };
//        // Adding request to request queue
//        MySingleton.getInstance(this).addToRequestQueue(strReq);

        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        // Handle the camera action
        Fragment fragment = new HomeFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commitAllowingStateLoss();

        navigationView.getMenu().getItem(0).setChecked(true);
        getSupportActionBar().setTitle("Home");

        loadHomeFragment();

    }


    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadHomeFragment() {

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            // show or hide the fab button

            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }




    @Override
    public void onBackPressed() {
        session.checkLogin();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

            // Handle the camera action
            Fragment fragment = new HomeFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                    android.R.anim.fade_out);
            fragmentTransaction.replace(R.id.frame, fragment);
            fragmentTransaction.commitAllowingStateLoss();

            navigationView.getMenu().getItem(0).setChecked(true);
            getSupportActionBar().setTitle("Home");

        } else if (id == R.id.nav_store) {
            Fragment fragment = new Store();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                    android.R.anim.fade_out);
            fragmentTransaction.replace(R.id.frame, fragment);
            fragmentTransaction.commitAllowingStateLoss();

            navigationView.getMenu().getItem(3).setChecked(true);
            getSupportActionBar().setTitle("Store");

        } else if (id == R.id.nav_orders) {
            Fragment fragment = new Orders();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                    android.R.anim.fade_out);
            fragmentTransaction.replace(R.id.frame, fragment);
            fragmentTransaction.commitAllowingStateLoss();

            navigationView.getMenu().getItem(1).setChecked(true);
            getSupportActionBar().setTitle("Orders");
        } else if (id == R.id.nav_products) {
            if(session.getStoreId()!=null){
                fragmentproduct  = new ProductsWithCategory();
            }
            else{
                fragmentproduct  = new Products();
            }
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                    android.R.anim.fade_out);
            fragmentTransaction.replace(R.id.frame, fragmentproduct );
            fragmentTransaction.commitAllowingStateLoss();

            navigationView.getMenu().getItem(2).setChecked(true);
            getSupportActionBar().setTitle("Products");

        } else if (id == R.id.nav_contact) {

        }
        else if (id == R.id.nav_wallet) {

            Fragment fragment = new WalletFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                    android.R.anim.fade_out);
            fragmentTransaction.replace(R.id.frame, fragment);
            fragmentTransaction.commitAllowingStateLoss();

            navigationView.getMenu().getItem(1).setChecked(true);
            getSupportActionBar().setTitle("Wallet");

        }
        else if (id == R.id.nav_help) {

        }

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
