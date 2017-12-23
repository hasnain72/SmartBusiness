package com.example.zafar.sbusiness.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import com.example.zafar.sbusiness.Database.DatabaseAttributes;
import com.example.zafar.sbusiness.Models.ProductAttributes;
import java.util.ArrayList;
import com.example.zafar.sbusiness.R;


public class AddVariants extends AppCompatActivity {
    EditText attributeOption;
    ListView variantsHints;
    Button addVariant;
    ImageButton addSign;
    ArrayList<String> variantsNames = new  ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_variants);

        getSupportActionBar().setTitle("Add Variants");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        attributeOption = (EditText)findViewById(R.id.attribueValue);
        variantsHints = (ListView) findViewById(R.id.variantsHints);
        addVariant = (Button) findViewById(R.id.addVariant);
        addVariant.setVisibility(View.INVISIBLE);
        addSign = (ImageButton) findViewById(R.id.addSign);
        // Check if Intent is from UPDATE OPTION
        Intent intent = getIntent();
        Integer i = 0; final String o;
        i = intent.getIntExtra("id",0);
        o = intent.getStringExtra("option");
        if(i!=0 && i!=null) { // Intent from Update Value
            Log.d("YOOOO","Entered"+" "+i+" "+o);
            variantsHints.setVisibility(View.INVISIBLE);
            attributeOption.setText(o);
            attributeOption.setSelection(attributeOption.getText().length());
            final Integer finalI = i;
            addSign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (attributeOption.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(), "Value Can't be Empty ", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        String val = attributeOption.getText().toString();
                        new DatabaseAttributes(getBaseContext()).updateOptionPA(new ProductAttributes(
                                finalI,
                                val
                        ));
                        Intent intent = new Intent(getApplicationContext(), AddProduct.class);
                        startActivity(intent);
                        attributeOption.setText("");
                    }
                }
            });
        }
        else { // Normal Activity Start
            variantsNames.add("Color");
            variantsNames.add("Finish");
            variantsNames.add("Material");
            variantsNames.add("Size");
            variantsNames.add("Style");
            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, variantsNames);
            variantsHints.setAdapter(arrayAdapter);

            attributeOption.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

            variantsHints.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getApplicationContext(), AddValue.class);
                    intent.putExtra("variantName", variantsNames.get(position));
                    startActivity(intent);
                }
            });
            addSign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (attributeOption.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(), "Value Can't be Empty ", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(getApplicationContext(), AddValue.class);
                        intent.putExtra("variantName", attributeOption.getText().toString());
                        startActivity(intent);
                        attributeOption.setText("");
                    }
                }
            });
        }
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
