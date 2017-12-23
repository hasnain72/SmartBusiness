package com.example.zafar.sbusiness.other;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zafar.sbusiness.R;

public class CustomListCustomer extends ArrayAdapter<String> {

    private final Activity context;

    private final String[] customer;
    private final String[] order;
    private final Integer[] images;
    public CustomListCustomer(Activity context,
                           String[] customers,String[] orders, Integer[] images ) {
        super(context, R.layout.list_single_customer, customers);
        this.context = context;
        this.customer = customers;
        this.order = orders;
        this.images = images;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_single_customer, null, true);

        TextView customers = (TextView) rowView.findViewById(R.id.customers);
        TextView orders = (TextView) rowView.findViewById(R.id.orders);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.customer_avatar);

        customers.setText(customer[position]);
        orders.setText(order[position]);
        imageView.setImageResource(images[position]);

        return rowView;
    }
}