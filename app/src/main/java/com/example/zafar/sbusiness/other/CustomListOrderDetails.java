package com.example.zafar.sbusiness.other;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zafar.sbusiness.Models.OrderDetail;
import com.example.zafar.sbusiness.R;

import java.util.ArrayList;

public class CustomListOrderDetails extends ArrayAdapter<OrderDetail> {

    private final Activity context;
    ArrayList<OrderDetail> orderDetails = new ArrayList<OrderDetail>();

    public CustomListOrderDetails(Activity context, ArrayList<OrderDetail> orderDetails) {
        super(context, R.layout.list_single_order_details, orderDetails);
        this.context = context;
        this.orderDetails = orderDetails;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_single_order_details, null, true);

        TextView titles = (TextView) rowView.findViewById(R.id.product_title);
        TextView prices = (TextView) rowView.findViewById(R.id.product_price);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.product_avatar);

        titles.setText(orderDetails.get(position).getProduct_name());
        prices.setText("Rs."+orderDetails.get(position).getPrice()+"/-");
        imageView.setImageResource(R.drawable.apple_product);

        return rowView;
    }
}