package com.example.zafar.sbusiness.other;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zafar.sbusiness.Models.store;
import com.example.zafar.sbusiness.R;
import com.example.zafar.sbusiness.SharedPreference.SessionManager;
import com.example.zafar.sbusiness.activity.MainActivity;

import java.util.ArrayList;

public class CustomListStores extends ArrayAdapter<store> {

    private final Activity context;
    SessionManager session;
    ArrayList<store> stores = new ArrayList<store>();
    public CustomListStores(Activity context, ArrayList<store> stores , SessionManager session) {
        super(context, R.layout.list_single_customer, stores);
        this.context = context;
        this.stores = stores;
        this.session = session;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_single_stores, null, true);

        TextView stores_txtView = (TextView) rowView.findViewById(R.id.stores);
        TextView stores_category = (TextView) rowView.findViewById(R.id.category_of_stores);
        Button btn = (Button) rowView.findViewById(R.id.stores_activate_button);

        stores_txtView.setText(stores.get(position).getTitle());
        stores_category.setText("Category : " + stores.get(position).getCat_id());

        if(stores.get(position).getStore_id().equals(session.getStoreId())){
            btn.setText("Activated");
            btn.setEnabled(false);
            btn.setBackgroundColor(Color.parseColor("#ed832f"));
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                session.setStoreId(stores.get(position).getStore_id());
                session.setStoreLocation(stores.get(position).getLocation());
                Toast.makeText(getContext(), "Activated" , Toast.LENGTH_SHORT).show();

                Intent i = new Intent(context , MainActivity.class);
                context.startActivity(i);
            }
        });
        return rowView;
    }
}