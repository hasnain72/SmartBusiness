package com.example.zafar.sbusiness.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.zafar.sbusiness.Models.store;
import com.example.zafar.sbusiness.R;
import com.example.zafar.sbusiness.SharedPreference.SessionManager;
import com.example.zafar.sbusiness.other.CustomListCustomer;
import com.example.zafar.sbusiness.other.CustomListStore;
import com.example.zafar.sbusiness.other.CustomListStores;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Stores extends AppCompatActivity {

    ListView list;
    SessionManager session;
    ArrayList<store> stores = new ArrayList<store>();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_stores, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stores);
        session = new SessionManager(getApplicationContext());

        getSupportActionBar().setTitle("Select Store");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final ProgressDialog Dialog = new ProgressDialog(Stores.this);
        Dialog.setCanceledOnTouchOutside(false);
        Dialog.setMessage("Loading Stores");
        Dialog.show();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ProductTableDataReference = databaseReference.child("store");
        Query query = ProductTableDataReference.orderByChild("business_user_id").equalTo(session.getUserDetails().get("user_id"));
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                        store Store = postSnapshot.getValue(store.class);
                        stores.add(Store);
                    }

                    CustomListStores adapter = new CustomListStores(Stores.this , stores , session);
                    list=(ListView) findViewById(R.id.list_stores);
                    list.setAdapter(adapter);
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {

                        }
                    });
                }else{
                    Toast.makeText(Stores.this, "No Store Found Add First", Toast.LENGTH_SHORT).show();
                }
                Dialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
            overridePendingTransition(0, 0);
        }

        if (item.getItemId() == R.id.add) {
            Intent i = new Intent(Stores.this , CreateStore.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}
