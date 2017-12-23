package com.example.zafar.sbusiness.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.zafar.sbusiness.Models.User;
import com.example.zafar.sbusiness.R;
import com.example.zafar.sbusiness.SharedPreference.SessionManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText Email = (EditText) findViewById(R.id.user_email_login);
        final EditText Password = (EditText) findViewById(R.id.user_password_login);

        session = new SessionManager(LoginActivity.this);

        Button login_button = (Button) findViewById(R.id.login_button_login);
        Button loginToSignup = (Button) findViewById(R.id.LoginToSignup);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Email.getText().toString().isEmpty()){
                    Email.setError("Must Not Empty");
                }else if(Password.getText().toString().isEmpty()){
                    Password.setError("Must Not Empty");
                }else{
                    final ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setMessage("Please Wait Here");
                    dialog.show();

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference UserTableDataReference = databaseReference.child("user");
                    Query query = UserTableDataReference.orderByChild("email").equalTo(Email.getText().toString());
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                    User user = postSnapshot.getValue(User.class);
                                    if(user.getAuth().equals("0")){
                                        Toast.makeText(LoginActivity.this, "Not Verified", Toast.LENGTH_SHORT).show();
                                    }else{
                                        session.createLoginSession(user.getName(), user.getEmail() , user.getMobile_no() , user.getUser_id());
                                        session.setWallet("0");

                                        Intent i = new Intent(LoginActivity.this , MainActivity.class);
                                        startActivity(i);
                                    }
                                }
                            }else{
                                Toast.makeText(LoginActivity.this, "User not found Please Register", Toast.LENGTH_SHORT).show();
                            }
                            dialog.dismiss();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                    });
                }

            }
        });

        loginToSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this , PhoneAuthActivity.class);
                startActivity(i);
            }
        });

    }
}