package com.example.zafar.sbusiness.FireBase;

import android.provider.ContactsContract;
import android.util.Log;

import com.example.zafar.sbusiness.SharedPreference.SessionManager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {
    SessionManager session;

    @Override
    public void onTokenRefresh() {
        session = new SessionManager(getApplicationContext());
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d("Firebase", "token "+ token);

        if(session.getUserDetails().get("user_id") != null){
            String my_id = session.getUserDetails().get("user_id");
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            DatabaseReference UserTableDataReference = databaseReference.child("user");
            UserTableDataReference.child(my_id).child("token").setValue(String.valueOf(token));
        }
    }
}
