package com.example.zafar.sbusiness.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.zafar.sbusiness.Models.User;
import com.example.zafar.sbusiness.R;
import com.example.zafar.sbusiness.SharedPreference.SessionManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Created by AJ
 * Created on 09-Jun-17.
 */

public class PhoneAuthActivity extends AppCompatActivity implements
        View.OnClickListener {

    EditText mPhoneNumberField, mVerificationField , userName , userEmail , userPassword;
    Button mStartButton, mVerifyButton , signupToLogin;
    SessionManager pref;

    private FirebaseAuth mAuth;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    String mVerificationId;

    private static final String TAG = "PhoneAuthActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        pref = new SessionManager(getApplicationContext());
        userName = (EditText) findViewById(R.id.user_name);
        userEmail = (EditText) findViewById(R.id.user_email);
        userPassword = (EditText) findViewById(R.id.user_password);
        mPhoneNumberField = (EditText) findViewById(R.id.field_phone_number);
        mVerificationField = (EditText) findViewById(R.id.field_verification_code);


        mStartButton = (Button) findViewById(R.id.button_start_verification);
        mVerifyButton = (Button) findViewById(R.id.button_verify_phone);
        signupToLogin = (Button) findViewById(R.id.signupToLogin);

//        Hide Verify Text Field And Button
        mVerificationField.setVisibility(View.GONE);
        mVerifyButton.setVisibility(View.GONE);

        mStartButton.setOnClickListener(this);
        mVerifyButton.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.d(TAG, "onVerificationCompleted:" + credential);
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                DatabaseReference UserTableDataReference = databaseReference.child("user");
                String idya =  UserTableDataReference.push().getKey();
                String user_token = FirebaseInstanceId.getInstance().getToken();

                User user1 = new User(idya , "Business" , "null" , userEmail.getText().toString() ,userName.getText().toString() , mPhoneNumberField.getText().toString() , "0" , "0" , "0"  , "0" , String.valueOf(Calendar.getInstance().getTime()) , userPassword.getText().toString() , user_token , "0");
                UserTableDataReference.child(idya).setValue(user1);

                pref.createLoginSession(userName.getText().toString(), userEmail.getText().toString() , mPhoneNumberField.getText().toString() , idya);
                pref.setWallet("0");

                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.w(TAG, "onVerificationFailed", e);
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    mPhoneNumberField.setError("Invalid phone number.");
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
                            Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {

                mVerificationField.setVisibility(View.VISIBLE);
                mVerifyButton.setVisibility(View.VISIBLE);

                mStartButton.setVisibility(View.GONE);
                userName.setVisibility(View.GONE);
                userEmail.setVisibility(View.GONE);
                userPassword.setVisibility(View.GONE);
                mPhoneNumberField.setVisibility(View.GONE);

                Log.d(TAG, "onCodeSent:" + verificationId);
                mVerificationId = verificationId;
                mResendToken = token;

            }
        };

        signupToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PhoneAuthActivity.this , LoginActivity.class);
                startActivity(i);
            }
        });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();

                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                            DatabaseReference UserTableDataReference = databaseReference.child("user");
                            UserTableDataReference.child(pref.getUserDetails().get("user_id")).child("auth").setValue("1");

                            startActivity(new Intent(PhoneAuthActivity.this, MainActivity.class));
                            finish();
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                mVerificationField.setError("Invalid code.");
                            }
                        }
                    }
                });
    }


    private void startPhoneNumberVerification(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }

    private boolean validatePhoneNumber() {
        String phoneNumber = mPhoneNumberField.getText().toString();
        if (TextUtils.isEmpty(phoneNumber)) {
            mPhoneNumberField.setError("Invalid phone number.");
            return false;
        }
        return true;
    }
    @Override
    public void onStart() {
        super.onStart();
        if(pref.getUserDetails().get("user_id") == null){
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                startActivity(new Intent(PhoneAuthActivity.this, MainActivity.class));
                finish();
            }
        }else{
            startActivity(new Intent(PhoneAuthActivity.this, MainActivity.class));
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_start_verification:
                if (!validatePhoneNumber()) {
                    return;
                }
                if(userName.getText().toString().isEmpty()){userName.setError("Must not be Empty");}
                else if(userEmail.getText().toString().isEmpty()){userEmail.setError("Must not be Empty");}
                else if(userPassword.getText().toString().isEmpty()){userPassword.setError("Must not be Empty");}
                else{

                    final ProgressDialog dialog = new ProgressDialog(PhoneAuthActivity.this);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setMessage("Pleaes Wait Here");
                    dialog.show();

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference UserTableDataReference = databaseReference.child("user");
                    Query query = UserTableDataReference.orderByChild("mobile_no").equalTo(mPhoneNumberField.getText().toString());
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(!dataSnapshot.exists()){
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                                DatabaseReference UserTableDataReference = databaseReference.child("user");
                                Query query = UserTableDataReference.orderByChild("email").equalTo(userEmail.getText().toString());
                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(!dataSnapshot.exists()){
                                            startPhoneNumberVerification(mPhoneNumberField.getText().toString());
                                        }else{
                                            userEmail.setError("Already Registered. Please Login");
                                        }
                                        dialog.dismiss();
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {}
                                });

                            }else{
                                mPhoneNumberField.setError("Already Registered. Please Login");
                            }
                            dialog.dismiss();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                    });
                }
                break;
            case R.id.button_verify_phone:
                String code = mVerificationField.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    mVerificationField.setError("Cannot be empty.");
                    return;
                }

                verifyPhoneNumberWithCode(mVerificationId, code);
                break;

        }

    }

}
