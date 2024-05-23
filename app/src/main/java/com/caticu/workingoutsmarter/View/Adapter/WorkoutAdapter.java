package com.caticu.workingoutsmarter.View.Workout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.caticu.workingoutsmarter.API.WorkoutFromAPI;
import com.caticu.workingoutsmarter.R;

import java.util.List;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder> {

    private List<WorkoutFromAPI> workoutList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(WorkoutFromAPI workout);
    }

    public WorkoutAdapter(List<WorkoutFromAPI> workoutList, OnItemClickListener listener) {
        this.workoutList = workoutList;
        this.listener = listener;
    }

    @Override
    public WorkoutViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_workout, parent, false); // Create a simple item layout
        return new WorkoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WorkoutViewHolder holder, int position) {
        WorkoutFromAPI workout = workoutList.get(position);
        holder.bind(workout);
    }

    @Override
    public int getItemCount() {
        return workoutList != null ? workoutList.size() : 0;
    }

    public void updateWorkoutList(List<WorkoutFromAPI> newList) {
        workoutList = newList;
        notifyDataSetChanged();
    }

    public class WorkoutViewHolder extends RecyclerView.ViewHolder {
        private final TextView workoutName;

        public WorkoutViewHolder(View itemView) {
            super(itemView);
            workoutName = itemView.findViewById(R.id.NameTextView);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClick(workoutList.get(position));
                }
            });
        }

        public void bind(WorkoutFromAPI workout) {
            workoutName.setText(workout.getName());
        }
    }
}
