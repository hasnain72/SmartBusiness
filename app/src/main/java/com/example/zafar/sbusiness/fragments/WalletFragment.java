package com.example.zafar.sbusiness.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zafar.sbusiness.Models.User;
import com.example.zafar.sbusiness.R;
import com.example.zafar.sbusiness.SharedPreference.SessionManager;
import com.example.zafar.sbusiness.activity.OrderDetails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class WalletFragment extends Fragment {
    SessionManager sessionManager;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = new SessionManager(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wallet, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final TextView price =(TextView) getView().findViewById(R.id.WF_AMOUNT_IN_WALLET);

        final ProgressDialog Dialog = new ProgressDialog(getActivity());
        Dialog.setMessage("Loading Wallet Info");
        Dialog.setCanceledOnTouchOutside(false);
        Dialog.show();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference UserTableDataReference = databaseReference.child("user");
        Query queryRP = UserTableDataReference.orderByChild("user_id").equalTo(sessionManager.getUserDetails().get("user_id"));
        queryRP.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                    User Don = postSnapshot.getValue(User.class);
                    price.setText("Rs."+Don.getWallet() +"/-");
                    sessionManager.setWallet(Don.getWallet());
                }
            Dialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }
}
