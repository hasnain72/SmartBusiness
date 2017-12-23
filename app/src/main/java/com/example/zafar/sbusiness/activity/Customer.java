package com.example.zafar.sbusiness.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.zafar.sbusiness.R;
import com.example.zafar.sbusiness.other.CustomListCustomer;
import com.example.zafar.sbusiness.other.CustomListStore;

public class Customer extends AppCompatActivity {

    ListView list;
    String[] customers = {
            "Pauline Brown",
            "Jeremy Buering",
            "Timothy Bryan",
            "Richard Canchola"
    } ;
    String[] orders = {
            "6 orders",
            "5 orders",
            "3 orders",
            "0 orders"
    } ;
    Integer[] images = {
            R.drawable.customer1,
            R.drawable.customer2,
            R.drawable.customer3,
            R.drawable.customer4

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        CustomListCustomer adapter = new
                CustomListCustomer(this, customers , orders, images);

        list=(ListView) findViewById(R.id.list_customer);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

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
}
