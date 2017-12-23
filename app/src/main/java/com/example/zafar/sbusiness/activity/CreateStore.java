package com.example.zafar.sbusiness.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.zafar.sbusiness.Models.Category;
import com.example.zafar.sbusiness.Models.store;
import com.example.zafar.sbusiness.R;
import com.example.zafar.sbusiness.SharedPreference.SessionManager;
import com.example.zafar.sbusiness.other.MySingleton;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CreateStore extends AppCompatActivity implements LocationListener {
    static ArrayList<String> category_list = new ArrayList();
    ArrayList<Category> category = new ArrayList<Category>();
    Category selected_category;
    EditText store_title;
    Spinner mySpinner;
    Button createStore , mapButton;
    TextView LatLng_textView;
    int PLACE_PICKER_REQUEST = 1;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_store);
        // Locate the spinner in activity_main.xml

        getSupportActionBar().setTitle("Create Store");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        session = new SessionManager(getApplicationContext());

        mySpinner = (Spinner) findViewById(R.id.spinner_add_store);
        mySpinner.setPrompt("Select Category");
        createStore = (Button) findViewById(R.id.create_store_btn);
        store_title = (EditText) findViewById(R.id.add_store_title);
        mapButton = (Button) findViewById(R.id.mapBtn);
        LatLng_textView = (TextView) findViewById(R.id.latlng_txtView);

        final ProgressDialog Dialog = new ProgressDialog(CreateStore.this);
        Dialog.setCanceledOnTouchOutside(false);
        Dialog.setMessage("Loading Data");
        Dialog.show();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ProductTableDataReference = databaseReference.child("Categories");
        Query query = ProductTableDataReference.orderByChild("parent_id").equalTo("0");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                category.clear();
                category_list.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Category cat = postSnapshot.getValue(Category.class);
                    category_list.add(cat.getCat_name());
                    category.add(cat);
                }

                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplication(), R.layout.text_spinner, category_list);
                adapter.notifyDataSetChanged();
                mySpinner.setAdapter(adapter);

                Dialog.dismiss();
                // Spinner on item click listener
                mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                                               View arg1, int position, long arg3) {
                        selected_category = category.get(position);
                        //               Toast.makeText(getApplicationContext(), selected_category.getCat_name(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub
                    }
                });

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
             }
        });

        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext() , "clicked", Toast.LENGTH_SHORT).show();
                String loc[] = session.getUserlocation().split(",");
                LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
                        new LatLng(Double.valueOf(loc[0]), Double.valueOf(loc[1])), new LatLng(Double.valueOf(loc[0]), Double.valueOf(loc[1])));
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                builder.setLatLngBounds(BOUNDS_MOUNTAIN_VIEW);
                Intent intent;
                try {
                    intent = builder.build(CreateStore.this);
                    startActivityForResult(intent,PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });


        createStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                String url = "http://192.168.155.105/android/insert_data_store.php";
//                StringRequest stringRequest1 = new StringRequest(Request.Method.POST , url, new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String s) {
//                        Log.d("Shitty" , s);
//                        if(s.equals("Inserted")){
//                            Toast.makeText(getApplicationContext() , "New Store Created" , Toast.LENGTH_SHORT).show();
//                            Intent i = new Intent(getApplicationContext() , AddProduct.class);
//                            startActivity(i);
//                        }
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        Toast.makeText(getApplicationContext(), "Erroro :" + volleyError.toString(), Toast.LENGTH_SHORT).show();
//                        Log.d("Erroro" , volleyError.toString());
//                    }
//                }){
//                    @Override
//                    protected Map<String, String> getParams() throws AuthFailureError {
//                        Map<String , String> params  = new HashMap<>();
//                        params.put("business_id" , "5");
//                        params.put("cat_id" , selected_category.getCat_id());
//                        params.put("business_title" , store_title.getText().toString());
//                        params.put("business_location" , LatLng_textView.getText().toString());
//                        return params;
//                    }
//                };
//
//                MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest1);

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                DatabaseReference CategoryTableDataReference = databaseReference.child("store");
                String idya =  CategoryTableDataReference.push().getKey();
                store Store = new store(idya , session.getUserDetails().get("user_id") , selected_category.getCat_id() , store_title.getText().toString() , LatLng_textView.getText().toString() , Calendar.getInstance().getTime().toString() );
                CategoryTableDataReference.child(idya).setValue(Store);
                session.setStoreId(idya);
                session.setStoreLocation(LatLng_textView.getText().toString());
                Toast.makeText(CreateStore.this, session.getStoreId(), Toast.LENGTH_SHORT).show();

                Intent i = new Intent(getApplicationContext() , AddProduct.class);
                startActivity(i);

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

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
            category_list.clear();
    }

    protected void onActivityResult(int requestCode,int resultCode, Intent data) {
        if(requestCode==PLACE_PICKER_REQUEST) {
            if(resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data,this);
                String latitude = String.valueOf(place.getLatLng().latitude);
                String longitude = String.valueOf(place.getLatLng().longitude);
                LatLng_textView.setText(latitude + "," + longitude);
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
