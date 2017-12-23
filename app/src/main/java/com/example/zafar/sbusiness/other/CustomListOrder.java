package com.example.zafar.sbusiness.other;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zafar.sbusiness.Models.Order;
import com.example.zafar.sbusiness.R;

import java.util.ArrayList;

public class CustomListOrder extends ArrayAdapter<Order> {

    private final Activity context;
    ArrayList<Order> orders = new ArrayList<Order>();

    public CustomListOrder(Activity context,ArrayList<Order> orders) {
        super(context, R.layout.list_single_order, orders);
        this.context = context;
        this.orders = orders;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_single_order, null, true);

        TextView OrderNo = (TextView) rowView.findViewById(R.id.order_no);
        TextView CustomerName = (TextView) rowView.findViewById(R.id.customer_name);
   //     TextView Status = (TextView) rowView.findViewById(R.id.order_status);
        TextView Amount = (TextView) rowView.findViewById(R.id.order_amount);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.status);

        OrderNo.setText("#" + orders.get(position).getOrder_id());
        CustomerName.setText(orders.get(position).getCustomer_name());

        Amount.setText("Rs." + orders.get(position).getTotal_price() + "/");
    //    Status.setText(status[position]);

        if(orders.get(position).getOrder_status().equals("0")){
            imageView.setImageResource(R.drawable.unfulfilled);
        }else{
            imageView.setImageResource(R.drawable.fulfilled);
        }

        return rowView;
    }
}