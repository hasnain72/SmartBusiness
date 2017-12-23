package com.example.zafar.sbusiness.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.zafar.sbusiness.R;
import com.example.zafar.sbusiness.SharedPreference.SessionManager;
import com.example.zafar.sbusiness.activity.Customer;
import com.example.zafar.sbusiness.activity.Settings;
import com.example.zafar.sbusiness.activity.Stores;
import com.example.zafar.sbusiness.other.CustomListStore;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;


public class Store extends Fragment {

    SessionManager session;
    ListView list;
    String[] titles = {
            "Customers",
            "Reports",
            "Settings",
            "Stores",
            "Support"
    } ;
    Integer[] images = {
            R.drawable.user_store,
            R.drawable.reports_store,
            R.drawable.settings_store,
            R.drawable.store_store,
            R.drawable.support_store

    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionManager(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_store, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CustomListStore adapter = new
                CustomListStore (getActivity(), titles, images);
        list=(ListView) getView().findViewById(R.id.list_store);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
               switch(position){
                   case 0:{
                       // Toast.makeText(getActivity(), "You Clicked at Customer" , Toast.LENGTH_SHORT).show();
                       Intent i = new Intent(getActivity() , Customer.class);
                       startActivity(i);
                        break;
                   }

                   case 2:{
                       Intent i = new Intent(getActivity() , Settings.class);
                       startActivity(i);
                       break;
                   }

                   case 3:{
                       Intent i = new Intent(getActivity() , Stores.class);
                       startActivity(i);
                       break;
                   }
               }

            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_store, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
// Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.create_new_store:
             Toast.makeText(getActivity() , "Create New Store" , Toast.LENGTH_LONG).show();
                return true;

            case R.id.log_out:
                Toast.makeText(getActivity() , "Log out" , Toast.LENGTH_LONG).show();
                session.logoutUser();
                FirebaseAuth.getInstance().signOut();
                return  true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


        public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
