package com.example.zafar.sbusiness.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.example.zafar.sbusiness.Models.Category;
import com.example.zafar.sbusiness.Models.Product;
import com.example.zafar.sbusiness.R;
import com.example.zafar.sbusiness.activity.AddProduct;
import com.example.zafar.sbusiness.other.CustomListProducts;
import com.example.zafar.sbusiness.other.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProductsWithProductFragment extends Fragment {
    ArrayList<Product> products = new ArrayList<Product>();
    ArrayList<Product> new_products = new ArrayList<Product>();
    Product product;

    ListView list;
    String strtext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        strtext = getArguments().getString("cat_id");
        if(strtext != null){
            Log.d("STRTXT" , strtext);
            products = getArguments().getParcelableArrayList("products");
            Log.d("STRTXT SIZE" ,""+ products.size());

            new_products.clear();
            for(int i=0; i<products.size(); i++){
                if(products.get(i).getCat_id().equals(strtext) && products.get(i).getStatus().equals("1")){
                    new_products.add(products.get(i));
                }
            }

        }

        return inflater.inflate(R.layout.fragment_products_with_product, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


     //   Log.d("Products" , "" + products.size());
        list = (ListView) getView().findViewById(R.id.list_product);;

        CustomListProducts adapter = new
                CustomListProducts (getActivity(), new_products );

        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                    Toast.makeText(getActivity(), "You Clicked at " +new_products.get(position).getProduct_id(), Toast.LENGTH_SHORT).show();

            }
        });

    }


}