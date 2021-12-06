package com.example.termproject;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class GymClassListUser extends ArrayAdapter<GymClass> implements Filterable{
    private Activity context;
    private final List<GymClass> gymClasses;

    List<GymClass> oGymClasses;
    DatabaseReference databaseClasses;
    DatabaseReference databaseAccounts;
    public Account userAccount;

    private GymClassFilter filter;

    public GymClassListUser(Activity context, List<GymClass> gymClasses) {
        super(context, R.layout.layout_gymclass_list_user, gymClasses);
        this.context = context;
        this.gymClasses = gymClasses;
        this.userAccount = userAccount;
        this.oGymClasses = new ArrayList<GymClass>(gymClasses);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_gymclass_list_user, null, true);

        TextView textViewClassType = (TextView) listViewItem.findViewById(R.id.textViewClassType);
        TextView textViewInstructor = (TextView) listViewItem.findViewById(R.id.textViewInstructor);
        TextView textViewDifficulty = (TextView) listViewItem.findViewById(R.id.textViewDifficulty);
        TextView textViewDay = (TextView) listViewItem.findViewById(R.id.textViewDay);
        TextView textViewTime = (TextView) listViewItem.findViewById(R.id.textViewTime);
        TextView textViewCapacity = (TextView) listViewItem.findViewById(R.id.textViewCapacity);
        Button enrollButton = (Button) listViewItem.findViewById(R.id.enrollButton);
        Button leaveClassButton = (Button) listViewItem.findViewById(R.id.leaveClass);

        GymClass gymClass = gymClasses.get(position);
        textViewClassType.setText(gymClass.getClassType().getName());
        textViewInstructor.setText(gymClass.getInstructorName());
        textViewDifficulty.setText(gymClass.getDifficultyLevel());
        textViewDay.setText(gymClass.getDay());
        textViewTime.setText(gymClass.getTime());
        textViewCapacity.setText("Spots: " + String.valueOf(gymClass.getRemainingCapacity()));

        databaseClasses = FirebaseDatabase.getInstance().getReference("gymClasses");
        databaseAccounts = FirebaseDatabase.getInstance().getReference("accounts");

        enrollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(gymClass.userIsEnrolled(RegUsrLandingActivity.userAccount.get_id())) {
                    Toast.makeText(context, "You are already enrolled in this class", Toast.LENGTH_LONG).show();
                }

                else if(gymClass.getRemainingCapacity()<1) {
                    Toast.makeText(context, "This class is full.", Toast.LENGTH_LONG).show();
                }

                else {
                    gymClass.addUser(RegUsrLandingActivity.userAccount);
                    RegUsrLandingActivity.userAccount.addClass(gymClass.get_id());
                    databaseClasses.child(gymClass.get_id()).setValue(gymClass);
                    databaseAccounts.child(RegUsrLandingActivity.userAccount.get_id()).setValue(RegUsrLandingActivity.userAccount);
                }
            }
        });

        leaveClassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(gymClass.userIsEnrolled(RegUsrLandingActivity.userAccount.get_id())) {
                    gymClass.removeUser(RegUsrLandingActivity.userAccount.get_id());
                    RegUsrLandingActivity.userAccount.removeClass(gymClass.get_id());
                    databaseClasses.child(gymClass.get_id()).setValue(gymClass);
                    databaseAccounts.child(RegUsrLandingActivity.userAccount.get_id()).setValue(RegUsrLandingActivity.userAccount);
                }

                else {
                    Toast.makeText(context, "You are not enrolled in this class", Toast.LENGTH_LONG).show();
                }
            }
        });

        return listViewItem;
    }

    @Override
    public Filter getFilter() {
        filter = new GymClassFilter();
        return filter;
    }

    private class GymClassFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String constraintString = constraint.toString().toLowerCase();
            FilterResults result = new FilterResults();

            final List<GymClass> list = new ArrayList<GymClass>(gymClasses);
            int count = list.size();

            final ArrayList<GymClass> nlist = new ArrayList<>(count);

            if(constraintString.equals("") || constraintString.length() == 0) {
                System.out.println("empty query");
                System.out.println(oGymClasses.size());
                result.values = oGymClasses;
                result.count = oGymClasses.size();
            }

            else {
                for(int i=0; i < count; i++) {
                    String[] filterableStrings = {list.get(i).getClassType().getName(), list.get(i).getDay()};
                    if(filterableStrings[0].toLowerCase().contains(constraintString) || filterableStrings[1].toLowerCase().contains(constraintString)) {
                        nlist.add(list.get(i));
                    }
                }

                result.count = nlist.size();
                result.values = nlist;
            }
            return result;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<GymClass> fitems;
            fitems = (ArrayList<GymClass>)results.values;

            if(fitems.size()==0) {
                notifyDataSetInvalidated();
            }

            clear();
            for (GymClass g : fitems) {
                add(g);
            }
        }
    }
}
