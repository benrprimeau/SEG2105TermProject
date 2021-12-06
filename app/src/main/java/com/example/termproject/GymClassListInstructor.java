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

public class GymClassListInstructor extends ArrayAdapter<GymClass> implements Filterable{
	private Activity context;
	private final List<GymClass> gymClasses;
	List<GymClass> oGymClasses;
	DatabaseReference databaseClasses;

	private GymClassFilter filter;

	public GymClassListInstructor(Activity context, List<GymClass> gymClasses) {
		super(context, R.layout.layout_gymclass_list_instructor, gymClasses);
		this.context = context;
		this.gymClasses = gymClasses;
		this.oGymClasses = new ArrayList<GymClass>(gymClasses);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View listViewItem = inflater.inflate(R.layout.layout_gymclass_list_instructor, null, true);

		TextView textViewClassType = (TextView) listViewItem.findViewById(R.id.textViewClassType);
		TextView textViewInstructor = (TextView) listViewItem.findViewById(R.id.textViewInstructor);
		TextView textViewDifficulty = (TextView) listViewItem.findViewById(R.id.textViewDifficulty);
		TextView textViewDay = (TextView) listViewItem.findViewById(R.id.textViewDay);
		TextView textViewTime = (TextView) listViewItem.findViewById(R.id.textViewTime);
		TextView textViewCapacity = (TextView) listViewItem.findViewById(R.id.textViewCapacity);
		Button cancelButton = (Button) listViewItem.findViewById(R.id.cancelButton);

		GymClass gymClass = gymClasses.get(position);
		textViewClassType.setText(gymClass.getClassType().getName());
		textViewInstructor.setText(gymClass.getInstructorName());
		textViewDifficulty.setText(gymClass.getDifficultyLevel());
		textViewDay.setText(gymClass.getDay());
		textViewTime.setText(gymClass.getTime());
		textViewCapacity.setText("Capacity: " + String.valueOf(gymClass.getMaximumCapacity()));

		databaseClasses = FirebaseDatabase.getInstance().getReference("gymClasses");

		cancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Toast.makeText(context, gymClass.get_id(), Toast.LENGTH_LONG).show();
				databaseClasses.child(gymClass.get_id()).removeValue();
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
					String[] filterableStrings = {list.get(i).getClassType().getName(), list.get(i).getInstructorName()};
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
