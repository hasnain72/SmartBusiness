package com.example.zafar.sbusiness.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.zafar.sbusiness.Database.DatabaseAttributes;
import com.example.zafar.sbusiness.Models.Category;
import com.example.zafar.sbusiness.Models.PAFB;
import com.example.zafar.sbusiness.Models.Product;
import com.example.zafar.sbusiness.Models.ProductAttributes;
import com.example.zafar.sbusiness.Models.PwithC;
import com.example.zafar.sbusiness.Models.User;
import com.example.zafar.sbusiness.Models.store;
import com.example.zafar.sbusiness.R;
import com.example.zafar.sbusiness.SharedPreference.SessionManager;
import com.example.zafar.sbusiness.other.AttributeListAdapter;
import com.example.zafar.sbusiness.other.MySingleton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.kosalgeek.android.photoutil.ImageBase64;
import com.kosalgeek.android.photoutil.PhotoLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddProduct extends AppCompatActivity {

    ArrayList<Category> category = new ArrayList<Category>();
    ArrayList<Category> sub_category = new ArrayList<Category>();

    ArrayList<String> category_list = new ArrayList<String>();
    ArrayList<String> sub_category_list = new ArrayList<String>();
    Spinner mySpinner , subCatSpinner;
    EditText tittle , description , price , inventoryTxt;
    Switch inventory_switch , allow_osp_switch;
    Category selected_category;
    ArrayAdapter<String> adapter , adapter2;
    String invSwitch  , invAlloOsp;

    // Image Variables
    ImageView ImgGallery;
    GalleryPhoto galleryPhoto;
    LinearLayout linearMain;

    final int GALLERY_REQUEST = 1200;
    final int GET_MY_PERMISSION = 1;
    int cat_check=0;
    ArrayList<String> imageList = new ArrayList<>();

    // Work for Add Variants
    ListView attributeList;
    int[] images={R.drawable.options};
    ArrayList<Integer> id = new ArrayList<>();
    ArrayList<String> options = new ArrayList<>();
    ArrayList<String> values = new ArrayList<>();
    List<ProductAttributes> attributes = new ArrayList<>();

    SharedPreferences pref ;
    SharedPreferences.Editor editor ;

    SessionManager session;
    String category_id;

    Uri filePath;

    //creating reference to firebase storage
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://sbusiness-a868c.appspot.com/");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        // Asking for Persmission
        checkPermission();

        invSwitch =  invAlloOsp = "0";
        // Setting Preference
        session = new SessionManager(getApplicationContext());
        pref = getSharedPreferences("variantPref", 0);
        editor = pref.edit();

        // Image Work
        linearMain = (LinearLayout) findViewById(R.id.linearMain);
        galleryPhoto = new GalleryPhoto(getApplicationContext());

        // Product Work
        mySpinner = (Spinner) findViewById(R.id.spinner_add_product);
        subCatSpinner = (Spinner) findViewById(R.id.spinner_sub_category);


        tittle = (EditText) findViewById(R.id.titles);
        description = (EditText) findViewById(R.id.description);
        price = (EditText) findViewById(R.id.price);
        inventoryTxt = (EditText) findViewById(R.id.qty);

        inventory_switch = (Switch) findViewById(R.id.inventory_switch);
        allow_osp_switch = (Switch) findViewById(R.id.allow_osp);

        inventory_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    invSwitch = "1";
                    inventoryTxt.setEnabled(true);
                    allow_osp_switch.setEnabled(true);
                    // do something when check is selected
                } else {
                    //do something when unchecked
                    invSwitch = "0";
                    inventoryTxt.setEnabled(false);
                    allow_osp_switch.setEnabled(false);
                }
            }
        });

        allow_osp_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    invAlloOsp = "true";
                }else{
                    invAlloOsp = "false";
                }
            }
        });

        tittle.setText(pref.getString("title" , null));
        description.setText(pref.getString("description" , null));
        price.setText(pref.getString("price" , null));

        // Add Variant Buton
        Button add_variant_btn = (Button) findViewById(R.id.add_variant);

        getSupportActionBar().setTitle("Add product");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);


        ImgGallery = (ImageView) findViewById(R.id.ImgGallery);

        ImgGallery.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = galleryPhoto.openGalleryIntent();
                startActivityForResult(intent, GALLERY_REQUEST);

            }
        });

        add_variant_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editor.putString("title" , tittle.getText().toString());
                editor.putString("description" , description.getText().toString());
                editor.putString("price" , price.getText().toString());
                editor.commit();

                Intent i = new Intent(AddProduct.this , AddVariants.class);
                startActivity(i);
            }
        });

        final ProgressDialog Dialog = new ProgressDialog(AddProduct.this);
        Dialog.setMessage("Loading Data");
        Dialog.setCanceledOnTouchOutside(false);
        Dialog.show();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ProductTableDataReference = databaseReference.child("store");
        Query query = ProductTableDataReference.orderByChild("store_id").equalTo(session.getStoreId());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                    store Store = postSnapshot.getValue(store.class);
                    category_id = Store.getCat_id();
                }

                DatabaseReference ProductTableDataReference = databaseReference.child("Categories");
                Query query = ProductTableDataReference.orderByChild("parent_id").equalTo(category_id);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Category cat = postSnapshot.getValue(Category.class);
                            category_list.add(cat.getCat_name());
                            category.add(cat);
                        }

                        adapter = new ArrayAdapter<String>(getApplication(), R.layout.text_spinner, category_list);
                        adapter.notifyDataSetChanged();
                        mySpinner.setAdapter(adapter);

                        Dialog.dismiss();

                        // Spinner on item click listener
                        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(AdapterView<?> parent,
                                                       View arg1, int position, long arg3) {
                                selected_category = category.get(position);
                                //                  Toast.makeText(getApplicationContext(), selected_category.getCat_id(), Toast.LENGTH_SHORT).show();
                                sub_category_list.clear();
                                sub_category.clear();

                                DatabaseReference CategoryTableDataReference = databaseReference.child("Categories");
                                Query query = CategoryTableDataReference.orderByChild("parent_id").equalTo(selected_category.getCat_id());
                                query.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.getValue() != null) {
                                            subCatSpinner.setVisibility(View.VISIBLE);
                                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                                Category cat = postSnapshot.getValue(Category.class);
                                                sub_category.add(cat);
                                                sub_category_list.add(cat.getCat_name());
                                            }
                                        } else {
                                            subCatSpinner.setVisibility(View.GONE);
                                        }
                                        adapter2 = new ArrayAdapter<String>(getApplication(), R.layout.text_spinner, sub_category_list);
                                        adapter2.notifyDataSetChanged();
                                        subCatSpinner.setAdapter(adapter2);

                                        // Spinner on item click listener
                                        subCatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> parent,
                                                                       View arg1, int position, long arg3) {
                                                selected_category = sub_category.get(position);
                                                //                                     Toast.makeText(getApplicationContext(), selected_category.getCat_name(), Toast.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> arg0) {
                                                // TODO Auto-generated method stub
                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });
//                        End Here of OnItemSelection
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> arg0) {
                                // TODO Auto-generated method stub
                            }
                        });
                    }else{
                            subCatSpinner.setVisibility(View.GONE);
                            mySpinner.setVisibility(View.GONE);
                            selected_category = new Category(category_id , "" , "" , "" , "" );
                            Dialog.dismiss();
                        }
                }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

//        Set Scrolling Issues
        final ScrollView mScrollView = (ScrollView) findViewById(R.id.scrolly_view);
        attributeList = (ListView) findViewById(R.id.attributeList);
        // Add Variants Work
        attributeList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mScrollView.requestDisallowInterceptTouchEvent(true);
                int action = event.getActionMasked();
                switch (action) {
                    case MotionEvent.ACTION_UP:
                        mScrollView.requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });
        updateAttributeList();
    }

    public void updateAttributeList() {
        attributeList.setAdapter(null);
        id.clear();
        values.clear();
        options.clear();
        attributes = new DatabaseAttributes(this).getAttributes();
        for(ProductAttributes pa:attributes) {
            id.add(pa.getAttributeId());
            options.add(pa.getAttributeName());
            values.add(pa.getAttributeValue());
            Log.d("YOOOOO",String.valueOf(pa.getAttributeId())+" "+pa.getAttributeName());
        }
        AttributeListAdapter adapter = new AttributeListAdapter(AddProduct.this, images ,id , options, values);
        attributeList.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
            overridePendingTransition(0, 0);
        }

        if (item.getItemId() == R.id.done) {

            if(imageList.size() > 0 && selected_category != null && !tittle.getText().toString().isEmpty() && !description.getText().toString().isEmpty() && !price.getText().toString().isEmpty()) {
                // Image Uploading Request

                final ProgressDialog pd = new ProgressDialog(AddProduct.this);
                pd.setMessage("Wait");
                pd.show();
                if (filePath != null) {
                    final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                    final DatabaseReference CategoryTableDataReference = databaseReference.child("product");
                    final String idya = CategoryTableDataReference.push().getKey();

                    StorageReference childRef = storageRef.child("Store"+session.getStoreId()+"/Product"+idya+"/"+tittle.getText().toString()+".png");

                    //uploading the image
                    UploadTask uploadTask = childRef.putFile(filePath);

                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            pd.dismiss();

                            Product product = new Product(idya, session.getStoreId(), selected_category.getCat_id(), tittle.getText().toString(), description.getText().toString(), price.getText().toString(), taskSnapshot.getDownloadUrl().toString() , invSwitch, invSwitch, inventoryTxt.getText().toString() , "1");
                            CategoryTableDataReference.child(idya).setValue(product);

                            attributes = new DatabaseAttributes(getApplication()).getAttributes();
                            if (attributes.size() > 0) {
                                for (final ProductAttributes cart : attributes) {
                                    DatabaseReference ProductAttributesTableDataReference = databaseReference.child("product_attribute");
                                    String newKey = ProductAttributesTableDataReference.push().getKey();
                                    PAFB productAttributes = new PAFB(newKey, idya, cart.getAttributeName(), cart.getAttributeValue());
                                    ProductAttributesTableDataReference.child(newKey).setValue(productAttributes);
                                }
                                new DatabaseAttributes(AddProduct.this).cleanCart();
                                Intent i = new Intent(AddProduct.this, MainActivity.class);
                                startActivity(i);

                            }

                            linearMain.removeAllViews();
                            imageList.clear();

                            editor.clear();
                            editor.commit();

                            final ProgressDialog Dg = new ProgressDialog(AddProduct.this);
                            Dg.setMessage("Please wait ... !!!");
                            Dg.setCanceledOnTouchOutside(false);
                            Dg.show();
                            DatabaseReference NewTableDataReference = databaseReference.child("store_with_cat");
                            Query query = NewTableDataReference.orderByChild("store_id").equalTo(session.getStoreId());
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                            PwithC pc = postSnapshot.getValue(PwithC.class);
                                            if (pc.getCat_id().equals(selected_category.getCat_id())) {
                                                cat_check = 1;
                                            }
                                        }
                                        if (cat_check == 0) {
                                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                                            DatabaseReference CategoryTableDataReference = databaseReference.child("store_with_cat");
                                            String idya = CategoryTableDataReference.push().getKey();
                                            PwithC pwc = new PwithC(idya, session.getStoreId(), selected_category.getCat_id());
                                            CategoryTableDataReference.child(idya).setValue(pwc);
                                        }

                                    } else {
                                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                                        DatabaseReference CategoryTableDataReference = databaseReference.child("store_with_cat");
                                        String idya = CategoryTableDataReference.push().getKey();
                                        PwithC pwc = new PwithC(idya, session.getStoreId(), selected_category.getCat_id());
                                        CategoryTableDataReference.child(idya).setValue(pwc);
                                    }
                                    Dg.dismiss();
                                    Intent i = new Intent(AddProduct.this, MainActivity.class);
                                    startActivity(i);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });

                            Toast.makeText(AddProduct.this, "Product Succesfully Added", Toast.LENGTH_SHORT).show();

//                            Toast.makeText(AddProduct.this, "Upload successful", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(AddProduct.this, "Upload Failed -> " + e, Toast.LENGTH_SHORT).show();
                        }
                    });



                    return true;
                } else {
                    Toast.makeText(this, "Please Fill All Fields And Also Insert An Image", Toast.LENGTH_SHORT).show();
                }
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_product, menu);
        return true;
    }


//    Gallery GALLERY_REQUEST

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            if(requestCode == GALLERY_REQUEST) {
                filePath = data.getData();
                galleryPhoto.setPhotoUri(data.getData());
                String photoPath = galleryPhoto.getPath();
                imageList.add(photoPath);
//                Log.d("ImagePath",galleryPhoto.getPath());

                try {
                    Bitmap bitmap = PhotoLoader.init().from(photoPath).requestSize(512,512).getBitmap();
                    ImageView imageView = new ImageView(getApplicationContext());
   //                 Toast.makeText(getApplicationContext(), "set Image", Toast.LENGTH_SHORT).show();
                    LinearLayout.LayoutParams layoutParams =
                            new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    imageView.setLayoutParams(layoutParams);
                    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    imageView.setPadding(10,0,10,0);
                    imageView.setAdjustViewBounds(true);
                    imageView.setImageBitmap(bitmap);
                    linearMain.addView(imageView);
                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(),"Error while uploading Image",Toast.LENGTH_LONG).show();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void checkPermission(){
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, GET_MY_PERMISSION);
        } else {
            //callMethod();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case GET_MY_PERMISSION:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                }
                break;
            default:
                break;
        }
    }

}
