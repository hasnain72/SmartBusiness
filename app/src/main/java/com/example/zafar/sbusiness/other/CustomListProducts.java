package com.example.zafar.sbusiness.other;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.zafar.sbusiness.Models.Order;
import com.example.zafar.sbusiness.Models.PAFB;
import com.example.zafar.sbusiness.Models.Product;
import com.example.zafar.sbusiness.Models.ProductAttributes;
import com.example.zafar.sbusiness.R;
import com.example.zafar.sbusiness.fragments.ProductsWithCategory;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class CustomListProducts extends ArrayAdapter<Product>{

    private final Activity context;

    ArrayList<Product> products = new ArrayList<Product>();
    public CustomListProducts(Activity context , ArrayList<Product> products ) {
        super(context, R.layout.list_single_product ,products);
        this.context = context;
        this.products = products;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_single_product, null, true);

//        if(products.size()>0){
            final Product single_product = products.get(position);

            TextView fresho = (TextView) rowView.findViewById(R.id.p_fresho);
            TextView product_title = (TextView) rowView.findViewById(R.id.p_title);
            TextView product_price= (TextView) rowView.findViewById(R.id.p_price);
            Button delete_btn = (Button) rowView.findViewById(R.id.dlt_button);

            ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
            fresho.setText(single_product.getProduct_name());
            product_title.setText(single_product.getDescription());
            product_price.setText(single_product.getPrice());

        // Decoding Image
        String image[] = single_product.getImage().split("!@");
        Glide.with(context).load(image[0]).dontAnimate().into(imageView);
        Log.i("imoge" , image[0]);
 //       Toast.makeText(context, single_product.getImage(), Toast.LENGTH_SHORT).show();
            //            byte[] imageAsBytes = Base64.decode(encodedDataString.getBytes(), 0);
//            imageView.setImageBitmap(BitmapFactory.decodeByteArray(
//                    imageAsBytes, 0, imageAsBytes.length));
//          //  imageView.setImageResource( R.drawable.apple_product);

            delete_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference ProductTableDataReference = databaseReference.child("product");
                    ProductTableDataReference.child(single_product.getProduct_id()).removeValue();
                    Toast.makeText(getContext(), single_product.getProduct_id() , Toast.LENGTH_SHORT).show();
                    ProductTableDataReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Toast.makeText(getContext(), "Succesfully Deleted", Toast.LENGTH_SHORT).show();

                            DatabaseReference ProductTableDataReference = databaseReference.child("product_attribute");
                            ProductTableDataReference.orderByChild("productId");
                            ProductTableDataReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()){
                                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                            PAFB pa = postSnapshot.getValue(PAFB.class);
                                            if(pa.getProductId().equals(single_product.getProduct_id())){
                                                Log.i("fuck" , pa.getProductId());
                                            DatabaseReference ProductTableDataReference = databaseReference.child("product_attribute");
                                            ProductTableDataReference.child(pa.getID()).removeValue();

                                            }
                                        }
                                    }
                                 }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {}
                            });

                            ProductsWithCategory pdc = new ProductsWithCategory();
                            ((FragmentActivity)context).getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.frame, pdc)
                                    .commit();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            });

//        }

        return rowView;
    }
}