package com.example.zafar.sbusiness.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.zafar.sbusiness.Database.DatabaseAttributes;
import com.example.zafar.sbusiness.Models.ProductAttributes;
import com.example.zafar.sbusiness.R;
import com.example.zafar.sbusiness.other.CustomAdapterProductAttributes;
import com.example.zafar.sbusiness.other.MySingleton;
import com.kosalgeek.android.photoutil.ImageBase64;
import com.kosalgeek.android.photoutil.PhotoLoader;


import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddValue extends AppCompatActivity {
    ListView valueList;
    EditText attributeValue;
    ImageButton addSign;
    CustomAdapterProductAttributes adapter;
    int[] images={R.drawable.deleteicon};
    ArrayList<String> options = new ArrayList<>();
    ArrayList<String> values = new ArrayList<>();
    String option; int update = 0;
    Integer finalI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_value);

        getSupportActionBar().setTitle("Enter " + getIntent().getStringExtra("variantName") + " Value");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);

        valueList = (ListView) findViewById(R.id.valueList);
        attributeValue = (EditText) findViewById(R.id.attribueValue);
        addSign = (ImageButton) findViewById(R.id.addSign);
        attributeValue.requestFocus();
        //Preventing keyboard from pushing listView up
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        options.add(option);
        // To Check if the Intent is from UPDATE VALUE
        Intent intent = getIntent();
        Integer i = 0;
        String val;
        i = intent.getIntExtra("id",0);
        val = intent.getStringExtra("value");
        if(i!=0 && i!=null) { // For Intent from Update Value
            finalI = i;
            getSupportActionBar().setTitle("Enter Value to Update");
            //Toast.makeText(getApplicationContext(), "Recevied "+i+" "+opt+" "+val, Toast.LENGTH_SHORT).show();
            attributeValue.setText(val);
            attributeValue.setSelection(attributeValue.getText().length());
            addSign.setVisibility(View.INVISIBLE);
            valueList.setVisibility(View.INVISIBLE);
            update = 1;
        }
        else {//For Normal Activity Start
            Intent intent2 = getIntent();
            option = intent2.getStringExtra("variantName");
            Toast.makeText(getApplicationContext(),"Recived : "+option,Toast.LENGTH_SHORT).show();
            addSign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (attributeValue.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(), "Value Can't be Empty ", Toast.LENGTH_SHORT).show();
                    } else {
                        addValue();
                        attributeValue.setText("");
                    }
                }
            });
        }
    }
    public void addValue() {
        values.add(attributeValue.getText().toString());
        adapter = new CustomAdapterProductAttributes(this,images,values);
        valueList.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_product, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
            Intent i = new Intent(AddValue.this , AddProduct.class);
            startActivity(i);
        }

        if (item.getItemId() == R.id.done) {
            if(update == 1){
                if (attributeValue.getText().toString().equals("") && values.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Enter Value ", Toast.LENGTH_SHORT).show();
                }
                else {
                    values.add(attributeValue.getText().toString());
                    Intent intent = new Intent(getApplicationContext(), AddProduct.class);
                    attributeValue.setText("");
                    addValue();
                    new DatabaseAttributes(getBaseContext()).updateValuePA(new ProductAttributes(
                            finalI,
                            values.get(0)
                    ));
                    Toast.makeText(getApplicationContext(), "Product Attributes Updated", Toast.LENGTH_SHORT).show();
                    adapter.clear();
                    startActivity(intent);
                }
            }else{
                if (attributeValue.getText().toString().equals("") && values.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Enter Value ", Toast.LENGTH_SHORT).show();
                }
                else {
                    String check;
                    check = attributeValue.getText().toString();
                    if(check.equals("")) {}
                    else {
                        addValue();
                        attributeValue.setText("");
                    }
                    Intent intent = new Intent(getApplicationContext(), AddProduct.class);
                    int productId = 1;
                    for (int j=0;j<values.size();j++) {
                        new DatabaseAttributes(getBaseContext()).addToPA(new ProductAttributes(
                                String.valueOf(productId),
                                option,
                                values.get(j)
                        ));
                    }
                    Toast.makeText(getApplicationContext(), "Product Attributes Added", Toast.LENGTH_SHORT).show();
                    adapter.clear();
                    startActivity(intent);
                }
            }

        }

        return super.onOptionsItemSelected(item);
    }
}