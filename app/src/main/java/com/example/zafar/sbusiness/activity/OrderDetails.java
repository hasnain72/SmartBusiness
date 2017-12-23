package com.example.zafar.sbusiness.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zafar.sbusiness.Models.Order;
import com.example.zafar.sbusiness.Models.OrderDetail;
import com.example.zafar.sbusiness.Models.Product;
import com.example.zafar.sbusiness.Models.User;
import com.example.zafar.sbusiness.Models.store;
import com.example.zafar.sbusiness.R;
import com.example.zafar.sbusiness.SharedPreference.SessionManager;
import com.example.zafar.sbusiness.other.CustomListOrderDetails;
import com.example.zafar.sbusiness.other.MySingleton;
import com.example.zafar.sbusiness.other.OnGetDataListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OrderDetails extends AppCompatActivity {

    TextView order_detail_date;
    TextView order_detail_price;
    TextView order_detail_soldBy;
    ArrayList<OrderDetail> orderdetails = new ArrayList<OrderDetail>();
    ImageView customer;
    TextView order_detail_customer_city;
    TextView order_detail_customers , status_message;
    ListView list;
    OkHttpClient mClient = new OkHttpClient();
    JSONArray jsonArray = new JSONArray();
    JSONArray jsonArrayRP = new JSONArray();
    Button deliver_button;
    Activity activity;
    String order_id , order_status , customerId ;
    ArrayList<User> resource_persons = new ArrayList<User>();
    ProgressDialog Dialog , Dialogi;
    SessionManager session;
    Location store_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        activity = this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        session = new SessionManager(getApplicationContext());
        String[] locationParts = session.getStorelocation().split(",");
        Double lat = Double.valueOf(locationParts[0]);
        Double lng = Double.valueOf(locationParts[1]);
        store_location = new Location("myProvider");
        store_location.setLatitude(lat);
        store_location.setLongitude(lng);


        order_id = getIntent().getStringExtra("order_id");
        order_status = getIntent().getStringExtra("order_status");
        String total_price = getIntent().getStringExtra("total");
        String customerName = getIntent().getStringExtra("customerName");
        String time = getIntent().getStringExtra("time");
        customerId = getIntent().getStringExtra("customerId");

        getSupportActionBar().setTitle("#"+order_id);

        status_message = (TextView) findViewById(R.id.status_message);
        order_detail_date = (TextView) findViewById(R.id.order_detail_date);
        order_detail_price = (TextView) findViewById(R.id.order_detail_price);
        order_detail_soldBy = (TextView) findViewById(R.id.order_detail_sold_by);
        order_detail_customers = (TextView) findViewById(R.id.order_detail_customers);
        order_detail_customer_city = (TextView) findViewById(R.id.order_detail_customer_city);
        customer = (ImageView) findViewById(R.id.customer_avatar_order_detail);
        deliver_button = (Button) findViewById(R.id.deliver_button);

        if(order_status.equals("1")){
            deliver_button.setVisibility(View.GONE);
            status_message.setText("Activated");
        }

//        Main Section
        order_detail_date.setText("Today "+time);
        order_detail_price.setText("Rs." + total_price + "/-");
        order_detail_soldBy.setText("Sold by " + "Charles Gay");

//        Customer Section
        customer.setImageResource(R.drawable.customer2);
        order_detail_customers.setText(customerName);
        order_detail_customer_city.setText("Torronto , Ontario");



        ReadDatabase(new OnGetDataListener() {
            @Override
            public void onStart() {
                Dialog = new ProgressDialog(OrderDetails.this);
                Dialog.setMessage("Loading Order Details");
                Dialog.setCanceledOnTouchOutside(false);
                Dialog.show();

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                DatabaseReference StoreTableDataReference = databaseReference.child("user");
                Query query = StoreTableDataReference.orderByChild("user_id").equalTo(customerId);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            User user = postSnapshot.getValue(User.class);
                            String recieverId = user.getToken();
                            jsonArray.put(recieverId);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onSuccess(DataSnapshot data) {
                //        Products details
                CustomListOrderDetails adapter = new CustomListOrderDetails(activity , orderdetails);

                list=(ListView) findViewById(R.id.list_order_details);
                list.setAdapter(adapter);

                if (Dialog  != null && Dialog .isShowing() && !OrderDetails.this.isFinishing())
                {
                    Dialog .dismiss();
                }
            }

            @Override
            public void onFailed(DatabaseError databaseError) {

            }
        });

        ResourcePersonSelection(new OnGetDataListener() {
            @Override
            public void onStart() {
                Dialogi = new ProgressDialog(OrderDetails.this);
                Dialogi.setMessage("Loading RP Details");
                Dialogi.setCanceledOnTouchOutside(false);
                Dialogi.show();
            }

            @Override
            public void onSuccess(DataSnapshot data) {
                Dialogi.dismiss();
            }

            @Override
            public void onFailed(DatabaseError databaseError) {

            }
        });
        // Deliver Button Clicked
        deliver_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                DatabaseReference UserTableDataReference = databaseReference.child("order");
                UserTableDataReference.child(order_id).child("order_status").setValue("1");
                Toast.makeText(OrderDetails.this, "Activated", Toast.LENGTH_SHORT).show();


                sendMessage(jsonArray , "Congratulation" ,"You Order Accepted ! Deliver b/w 09:00 A.M to 12:00 A.M" , "Http:\\google.com" , order_id);

               sendMessage(jsonArrayRP , "Alert" ,"Tap for New Order Delivery" , "Http:\\google.com" , order_id);


                Intent i = new Intent(OrderDetails.this , MainActivity.class);
                startActivity(i);
            }
        });

    }



    //    FCM Push Notification to Business Person
    public void sendMessage(final JSONArray recipients, final String title, final String body, final String icon , final String order_id) {

        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                String result = "";
                try {
                    JSONObject root = new JSONObject();
                    JSONObject notification = new JSONObject();
                    notification.put("body", body);
                    notification.put("title", title);
                    notification.put("icon", icon);
                    notification.put("ItemId", 2222);
                    notification.put("sound" , "Default");
                    notification.put("color" , "#203E78");
                    notification.put("order" , order_id);
                    notification.put("click_action" , "INTENT_SOME_ACTION_ORDER");

                    JSONObject data = new JSONObject();
                    data.put("order", order_id);
                    root.put("notification", notification);
                    root.put("data", data);
                    root.put("registration_ids", recipients);

                    result = postToFCM(root.toString());
                    Log.d("Main Activity", "Result: " + result);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                try {
                    JSONObject resultJson = new JSONObject(result);
                    int success, failure;
                    success = resultJson.getInt("success");
                    failure = resultJson.getInt("failure");
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(OrderDetails.this, result + "Message Failed, Unknown error occurred.", Toast.LENGTH_LONG).show();
                }
            }
        }.execute();
    }

    String postToFCM(String bodyString) throws IOException {
        String FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send";
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(JSON, bodyString);
        Request request = new Request.Builder()
                .url(FCM_MESSAGE_URL)
                .post(body)
                .addHeader("Authorization", "key=" + "AAAAaJ3REbE:APA91bH_Af3v9j5G1UUW5mf0UEVPehTfXnLieaqPnBq46qteGpIMdBjdOEM4B2uE6rJ-toV9D7yssfWFTT-d2eGv1XrcBhzzr1jDyxS8fkpqJJwjaKHDvsVXfEi4e5AFObs9wcOsoN1m")
                .build();
        Response response = mClient.newCall(request).execute();
        return response.body().string();
    }





    public void ReadDatabase(final OnGetDataListener listener){
        listener.onStart();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ProductTableDataReference = databaseReference.child("order_detail");
        Query query = ProductTableDataReference.orderByChild("order_id").equalTo(order_id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                    final OrderDetail od = postSnapshot.getValue(OrderDetail.class);

                    DatabaseReference ProductTableDataReference = databaseReference.child("product");
                    Query query = ProductTableDataReference.orderByChild("product_id").equalTo(od.getProduct_id());
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot postSnapshot:dataSnapshot.getChildren()) {
                                Product pd = postSnapshot.getValue(Product.class);
                                od.setProduct_name(pd.getProduct_name());
                                od.setPrice(pd.getPrice());
                            }
                            orderdetails.add(od);
                            listener.onSuccess(dataSnapshot);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onFailed(databaseError);
            }
        });
       }

    public void ResourcePersonSelection(final OnGetDataListener listener){
        listener.onStart();
        //                Notification to Resource Persons
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference UserTableDataReference = databaseReference.child("user");
        Query queryRP = UserTableDataReference.orderByChild("user_type").equalTo("Resource");
        queryRP.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                    User RP = postSnapshot.getValue(User.class);

                    String[] locationParts = RP.getLocation().split(",");
                    Double lat = Double.valueOf(locationParts[0]);
                    Double lng = Double.valueOf(locationParts[1]);
                    Location rp_location = new Location("myProvider");
                    rp_location.setLatitude(lat);
                    rp_location.setLongitude(lng);

                    double distanceInKiloMeters = (store_location.distanceTo(rp_location)) / 1000; // as distance is in meter
                    if(distanceInKiloMeters <= 1) {
                        // It is in range of 1 km
                        jsonArrayRP.put(RP.getToken());
                        Toast.makeText(activity, "Yesh in Range" + RP.getToken(), Toast.LENGTH_SHORT).show();
                    }
                }
                listener.onSuccess(dataSnapshot);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {listener.onFailed(databaseError);}
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
}
