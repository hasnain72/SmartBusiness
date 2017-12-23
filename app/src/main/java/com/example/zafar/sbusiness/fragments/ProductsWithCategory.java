package com.example.zafar.sbusiness.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.zafar.sbusiness.Models.Category;
import com.example.zafar.sbusiness.Models.Product;
import com.example.zafar.sbusiness.R;
import com.example.zafar.sbusiness.SharedPreference.SessionManager;
import com.example.zafar.sbusiness.activity.AddProduct;
import com.example.zafar.sbusiness.activity.MainActivity;
import com.example.zafar.sbusiness.other.MySingleton;
import com.example.zafar.sbusiness.other.OnGetDataListener;
import com.example.zafar.sbusiness.other.PagerAdapter;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.example.zafar.sbusiness.R.id.orders;


public class ProductsWithCategory extends Fragment {

    static int count=0;
    TabLayout tabLayout;
    ProgressDialog Dialog;
    SessionManager session;
    DatabaseReference databaseReference;
    ArrayList<Product> products = new ArrayList<Product>();
    ArrayList<Category> category_list2 = new ArrayList<Category>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session = new SessionManager(getActivity());
//        String url = "http://192.168.155.105/android/getting_products.php";
//        StringRequest stringRequest1 = new StringRequest(Request.Method.POST , url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String s) {
//                Log.d("Shitty" , s);
//         //       Toast.makeText(getActivity() , s , Toast.LENGTH_SHORT).show();
//
//                try {
//                    JSONArray jsonArray = new JSONArray(s);
//
//                    for (int i = 0; i < jsonArray.length(); i++) {
//
//                        JSONObject name = jsonArray.getJSONObject(i);
//                        String id = name.getString("product_id");
//                        String store_id = name.getString("store_id");
//                        String cat_id = name.getString("cat_id");
//                        String product_name = name.getString("product_name");
//                        String description = name.getString("description");
//                        String price = name.getString("price");
//                        String image = name.getString("image");
//
//                        Product single_product = new Product(id, store_id , cat_id , product_name , description , price , image);
//                        products.add(single_product);
//                        Log.d("Products" , "Added");
//
//                    }
//                }catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                Toast.makeText(getActivity(), "Error :" + volleyError.toString(), Toast.LENGTH_SHORT).show();
//                Log.d("Error" , volleyError.toString());
//            }
//        }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String , String> params  = new HashMap<>();
//                params.put("store_id" , "9");
//                return params;
//            }
//        };
//
//        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest1);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_products_with_category, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tabLayout = (TabLayout) getView().findViewById(R.id.tab_layout_product);

        Dialog = new ProgressDialog(getActivity());
        Dialog.setCanceledOnTouchOutside(false);
        Dialog.setMessage("Loading Products");
        Dialog.show();

        ReadDatabase(new OnGetDataListener() {

            @Override
            public void onStart() {
                Toast.makeText(getActivity(), "Start", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(DataSnapshot data) {
    //            Toast.makeText(getActivity(), "Sucess", Toast.LENGTH_SHORT).show();

                tabLayout.removeAllTabs();
                for(int i=0 ; i<category_list2.size() ; i++){
                    tabLayout.addTab(tabLayout.newTab().setText(category_list2.get(i).getCat_name()));
                }
                tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
                tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

                final ViewPager viewPager = (ViewPager) getView().findViewById(R.id.pager);
                final PagerAdapter adapter = new PagerAdapter
                        (getActivity().getSupportFragmentManager(), tabLayout.getTabCount() , category_list2 , tabLayout);


//                    Setting Content for each Tab
                for(int i=0;i<category_list2.size();i++){
                    final Category category = category_list2.get(i);
                    ProductsWithProductFragment fragment = new ProductsWithProductFragment();
                    Bundle args = new Bundle();
                    args.putString("cat_id", category.getCat_id());
                    args.putParcelableArrayList("products" , products);
                    Log.d("kuku" , " "+i + " Products" + products.size());
                    fragment.setArguments(args);
                    getFragmentManager().beginTransaction().commit();
                    adapter.addFrag(fragment);
                    adapter.notifyDataSetChanged();

                }

                viewPager.setAdapter(adapter);
                viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

                tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        viewPager.setCurrentItem(tab.getPosition());
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });
                Dialog.dismiss();
            }

            @Override
            public void onFailed(DatabaseError databaseError) {

            }

        });

//        tabLayout.addTab(tabLayout.newTab().setText("Active"));
//        tabLayout.addTab(tabLayout.newTab().setText("Pending"));
//        tabLayout.addTab(tabLayout.newTab().setText("History"));
    }

    public void ReadDatabase(final OnGetDataListener listener){
        listener.onStart();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ProductTableDataReference = databaseReference.child("product");
        Query query = ProductTableDataReference.orderByChild("store_id").equalTo(session.getStoreId());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) { // DataSnapshot is only for data read from Firebase DB
                if(dataSnapshot.exists()){
                    for (DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                        final Product product = postSnapshot.getValue(Product.class);
                        products.add(product);

                        DatabaseReference CategoryTableDataReference = databaseReference.child("Categories");
                        Query query = CategoryTableDataReference.orderByChild("cat_id").equalTo(product.getCat_id());
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                    Category category = postSnapshot.getValue(Category.class);
                                    if (count == 0) {
                                        category_list2.add(0, category);
                                        //        Toast.makeText(getActivity(), "Count == 0", Toast.LENGTH_LONG).show();
                                    } else {
                                        //          Toast.makeText(getActivity(), "Count != 0", Toast.LENGTH_LONG).show();
                                        int check=0;
                                        for (int i = 0; i < category_list2.size(); i++) {
                                            if (category_list2.get(i).getCat_id().equals(category.getCat_id())) {
                                                //    Toast.makeText(getActivity(), "Do Nothing", Toast.LENGTH_LONG).show();
                                                check=1;
                                            }
                                        }
                                        if(check==0){
                                            category_list2.add(category);
//                                    Toast.makeText(getActivity(), "Adding"+category.getCat_name(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                    count++;
                                }
                                listener.onSuccess(dataSnapshot);
                                //                      Toast.makeText(getActivity(), "BeforeSize !: " + category_list2.size(), Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {}
                        });

                    }

                }else{
                    Dialog.dismiss();
                    Toast.makeText(getActivity(), "Please Add Product", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onFailed(databaseError);
            }
        });
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_icons_product, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.add) {

//            Toast.makeText(getActivity() , "Clicked" , Toast.LENGTH_LONG).show();
            Intent i = new Intent(getActivity() , AddProduct.class);
            startActivity(i);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
