package com.example.termproject;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ClassTypeList extends ArrayAdapter<ClassType> {
    private Activity context;
    List<ClassType> classTypes;
    DatabaseReference databaseClassTypes;

    public ClassTypeList(Activity context, List<ClassType> classTypes) {
        super(context, R.layout.layout_classtype_list, classTypes);
        this.context = context;
        this.classTypes = classTypes;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_classtype_list, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textViewDescription = (TextView) listViewItem.findViewById(R.id.textViewDescription);

        Button deleteButton = (Button) listViewItem.findViewById(R.id.deleteButton);

        ClassType classType = classTypes.get(position);
        textViewName.setText(classType.getName());
        textViewDescription.setText(classType.getDescription());

        databaseClassTypes = FirebaseDatabase.getInstance().getReference("classTypes");

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseClassTypes.child(classType.get_id()).removeValue();
            }
        });

        return listViewItem;
    }
}
