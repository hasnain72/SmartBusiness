package com.example.zafar.sbusiness.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.zafar.sbusiness.Models.Order;
import com.example.zafar.sbusiness.Models.Product;
import com.example.zafar.sbusiness.Models.User;
import com.example.zafar.sbusiness.Models.store;
import com.example.zafar.sbusiness.R;
import com.example.zafar.sbusiness.SharedPreference.SessionManager;
import com.example.zafar.sbusiness.activity.Customer;
import com.example.zafar.sbusiness.activity.MainActivity;
import com.example.zafar.sbusiness.activity.OrderDetails;
import com.example.zafar.sbusiness.other.CustomListOrder;
import com.example.zafar.sbusiness.other.CustomListStore;
import com.example.zafar.sbusiness.other.MySingleton;
import com.example.zafar.sbusiness.other.OnGetDataListener;
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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Orders extends Fragment {

    ListView list;
    ProgressDialog Dialog;
    SessionManager session;
    ArrayList<Order> orders = new ArrayList<Order>();
    DatabaseReference databaseReference;

    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);
        session = new SessionManager(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_orders_blank, container, false);
        return inflater.inflate(R.layout.fragment_orders, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Dialog = new ProgressDialog(getActivity());
        Dialog.setCanceledOnTouchOutside(false);
        Dialog.setMessage("Loading Orders");
        Dialog.show();

        mReadDataOnce(new OnGetDataListener() {
            @Override
            public void onStart() {
//                Toast.makeText(getActivity(), "Start", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(DataSnapshot data) {
                Collections.reverse(orders);
                CustomListOrder adapter = new CustomListOrder(getActivity(), orders);
                list = (ListView) getView().findViewById(R.id.list_order);
                list.setAdapter(adapter);
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {

                        Intent i = new Intent(getActivity(), OrderDetails.class);
                        i.putExtra("time", orders.get(position).getOrder_time());
                        i.putExtra("customerName", orders.get(position).getCustomer_name());
                        i.putExtra("order_status" , orders.get(position).getOrder_status());
                        i.putExtra("total", orders.get(position).getTotal_price());
                        i.putExtra("order_id", orders.get(position).getOrder_id());
                //        Toast.makeText(getActivity(), orders.get(position).getCustomer_token(), Toast.LENGTH_SHORT).show();
                        i.putExtra("customerId", orders.get(position).getCustomer_id());
                        startActivity(i);

                    }
                });
                Dialog.dismiss();

            }

            @Override
            public void onFailed(DatabaseError databaseError) {

            }
        });

    }

    public void mReadDataOnce(final OnGetDataListener listener){
        listener.onStart();
        databaseReference = FirebaseDatabase.getInstance().getReference();
//        DatabaseReference UserTableDataReference = databaseReference.child("user");
//        User user = new User("5" , "customer", "pindi", "DD@gmail", "Yyu", "123", "123", "123", "123", "0", "123", "123", "123", "123", "132");
//        String idya =  UserTableDataReference.push().getKey();
//        UserTableDataReference.child(idya).setValue(user);

        DatabaseReference ProductTableDataReference = databaseReference.child("order");
        Query query = ProductTableDataReference.orderByChild("business_id").equalTo(session.getUserDetails().get("user_id"));
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        final Order order = postSnapshot.getValue(Order.class);
                        orders.add(order);

                        DatabaseReference UserTableDataReference = databaseReference.child("user");
                        Query query = UserTableDataReference.orderByChild("user_id").equalTo(order.getCustomer_id());
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                        User user = postSnapshot.getValue(User.class);
                                        order.setCustomer_token(user.getToken());
                                        order.setCustomer_name(user.getName());
                                    }
                                    listener.onSuccess(dataSnapshot);
                                }else{
           //                         Toast.makeText(getActivity(), "Not Exist", Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {}
                        });

                    }

                }else{
                    Dialog.dismiss();
                    Toast.makeText(getActivity(), "No Record Found", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
