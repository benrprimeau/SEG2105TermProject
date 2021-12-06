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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MyClassesActivity extends AppCompatActivity {

    TextView textViewWelcome;

    TextView textSearchBar;

    ListView listViewGymClasses;
    ArrayList<GymClass> myGymClasses;
    ArrayList<String> myGymClassesRef;

    DatabaseReference databaseAccounts;
    DatabaseReference databaseClasses;

    Account userAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_classes);

        textViewWelcome = (TextView) findViewById(R.id.textViewWelcome);

        textSearchBar = (TextView) findViewById(R.id.textSearchBar);

        listViewGymClasses = (ListView) findViewById(R.id.listViewGymClasses);

        databaseAccounts = FirebaseDatabase.getInstance().getReference("accounts");
        databaseClasses = FirebaseDatabase.getInstance().getReference("classes");

        myGymClassesRef = new ArrayList<String>();
        myGymClasses = new ArrayList<GymClass>();

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            String accountID = extras.getString("accountID");

            /* because i seem incapable of passing an
             * String accountID = extras.getString("account");
             * account object between activites, i get
             * to query the database all over again!
             * yay me! */

            databaseAccounts.orderByKey().equalTo(accountID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        /* the account variable overwrites every time,
                         * but that shouldn't matter since only one result
                         * should ever by in getChildren(). This is more just to
                         * remove the list container that the datasnapshot is in. */
                        userAccount = childSnapshot.getValue(Account.class);
                        textViewWelcome.setText(String.valueOf("Welcome " + userAccount.getName()));

                        myGymClassesRef = userAccount.getClasses();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    /* bro imagine actually throwing an exception
                     * instead of just using print statements */
                    System.out.println("oopsie that didn't work");
                }
            });

            for (int i = 0; i < myGymClassesRef.size(); i++) {
                int finalI = i;
                databaseClasses.orderByKey().equalTo(myGymClassesRef.get(i)).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                            GymClass gymClass = childSnapshot.getValue(GymClass.class);
                            myGymClasses.set(finalI, gymClass);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseAccounts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myGymClasses.clear();
                for(DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Account account = postSnapshot.getValue(Account.class);

                    if(account.get_id().equals(userAccount.get_id())) {
                        myGymClassesRef = account.getClasses();
                        for (int i = 0; i < myGymClassesRef.size(); i++) {
                            int finalI = i;
                            databaseClasses.orderByKey().equalTo(myGymClassesRef.get(i)).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                        GymClass gymClass = childSnapshot.getValue(GymClass.class);
                                        myGymClasses.set(finalI, gymClass);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }
                }

                System.out.println("ref " + myGymClassesRef.size());
                System.out.println(myGymClasses.size());

                GymClassListInstructor gymClassAdapter = new GymClassListInstructor(MyClassesActivity.this, myGymClasses);
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
}