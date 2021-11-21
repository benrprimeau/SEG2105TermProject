package com.example.termproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegUsrLandingActivity extends AppCompatActivity {

    DatabaseReference databaseAccounts;
    Account userAccount;

    TextView textViewWelcome;
    TextView textViewAccountType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_usr_landing);
        textViewWelcome = (TextView) findViewById(R.id.textViewWelcome);
        textViewAccountType = (TextView) findViewById(R.id.textViewAccountType);

        databaseAccounts = FirebaseDatabase.getInstance().getReference("accounts");

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            String accountID = extras.getString("account");
            
            /* because i seem incapable of passing an
             * account object between activites, i get
             * to query the database all over again!
             * yay me! */
            databaseAccounts.orderByKey().equalTo(accountID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                        /* the account variable overwrites every time,
                         * but that shouldn't matter since only one result
                         * should ever by in getChildren(). This is more just to
                         * remove the list container that the datasnapshot is in. */
                        userAccount = childSnapshot.getValue(Account.class);
                        textViewWelcome.setText(String.valueOf("Welcome " + userAccount.getName()));
                        textViewAccountType.setText(String.valueOf(("Your account is of type: " + userAccount.getAccountType())));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    /* bro imagine actually throwing an exception
                     * instead of just using print statements */
                    System.out.println("oopsie that didn't work");
                }
            });
        }
    }
}