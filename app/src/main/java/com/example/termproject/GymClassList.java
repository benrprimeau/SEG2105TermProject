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

public class GymClassList extends ArrayAdapter<GymClass> {
	private Activity context;
	List<GymClass> gymClasses;
	DatabaseReference databaseClasses;

	private GymClassFilter filter;

	public GymClassList(Activity context, List<GymClass> gymClasses) {
		super(context, R.layout.layout_gymclass_list, gymClasses);
		this.context = context;
		this.gymClasses = gymClasses;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View listViewItem = inflater.inflate(R.layout.layout_gymclass_list, null, true);

		TextView textViewClassType = (TextView) listViewItem.findViewById(R.id.textViewClassType);
		TextView textViewInstructor = (TextView) listViewItem.findViewById(R.id.textViewInstructor);
		TextView textViewDifficulty = (TextView) listViewItem.findViewById(R.id.textViewDifficulty);
		TextView textViewDay = (TextView) listViewItem.findViewById(R.id.textViewDay);
		TextView textViewTime = (TextView) listViewItem.findViewById(R.id.textViewTime);
		Button cancelButton = (Button) listViewItem.findViewById(R.id.cancelButton);

		GymClass gymClass = gymClasses.get(position);
		textViewClassType.setText(gymClass.getClassType().getName());
		textViewInstructor.setText(gymClass.getInstructorName());
		textViewDifficulty.setText(gymClass.getDifficultyLevel());
		textViewDay.setText(gymClass.getDay());
		textViewTime.setText(gymClass.getTime());

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
			constraint = constraint.toString().toLowerCase();
			FilterResults result = new FilterResults();

			if(constraint == null || constraint.toString().length() == 0) {
				ArrayList<GymClass> gymClassList = new ArrayList<GymClass>(gymClasses);
				result.values = gymClassList;
				result.count = gymClassList.size();
			}

			else {
				final ArrayList<GymClass> list = new ArrayList<GymClass>(gymClasses);
				final ArrayList<GymClass> nlist = new ArrayList<GymClass>();

				for(final GymClass g : gymClasses) {
					System.out.println("test");
					if(g.getClassType().getName().contains(constraint) || g.getInstructorName().contains(constraint)) {
						nlist.add(g);
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

			clear();
			for(GymClass g : fitems) {
				add(g);
			}
		}
	}
}
