package com.example.termproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RegUsrLandingActivity extends AppCompatActivity {

    DatabaseReference databaseAccounts;
    DatabaseReference databaseClasses;
    public static Account userAccount;

    TextView textViewWelcome;
    TextView textViewAccountType;
    TextView textSearchBar;

    Button buttonMyClasses;

    ListView listViewGymClasses;
    List<GymClass> gymClasses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_usr_landing);
        textViewWelcome = (TextView) findViewById(R.id.textViewWelcome);
        textViewAccountType = (TextView) findViewById(R.id.textViewAccountType);

        buttonMyClasses = (Button) findViewById(R.id.buttonMyClasses);

        textSearchBar = (TextView) findViewById(R.id.textSearchBar);

        listViewGymClasses = (ListView) findViewById(R.id.listViewGymClasses);

        databaseAccounts = FirebaseDatabase.getInstance().getReference("accounts");

        gymClasses = new ArrayList<>();
        databaseClasses = FirebaseDatabase.getInstance().getReference("gymClasses");

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

        buttonMyClasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToMyClassesPage();
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        databaseClasses.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gymClasses.clear();
                for(DataSnapshot postSnapshot : snapshot.getChildren()) {
                    GymClass gymClass = postSnapshot.getValue(GymClass.class);
                    gymClasses.add(gymClass);
                }

                GymClassListUser gymClassAdapter = new GymClassListUser(RegUsrLandingActivity.this, gymClasses);
                listViewGymClasses.setAdapter(gymClassAdapter);
                listViewGymClasses.setTextFilterEnabled(true);

                textSearchBar.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        gymClassAdapter.getFilter().filter(charSequence);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {}
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                /* bro imagine actually throwing an exception
                 * instead of just using print statements */
                System.out.println("oopsie that didn't work");
            }
        });
    }

    private void switchToMyClassesPage() {
        Intent switchActivityIntent = new Intent(RegUsrLandingActivity.this, MyClassesActivity.class).putExtra("accountID",userAccount.get_id());
        startActivity(switchActivityIntent);
    }
}