package com.caticu.workingoutsmarter.View.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.caticu.workingoutsmarter.API.WorkoutFromAPI;
import com.caticu.workingoutsmarter.R;

import java.util.List;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder>
{

    private List<WorkoutFromAPI> workoutList;
    private OnItemClickListener listener;

    public interface OnItemClickListener
    {
        void onItemClick(WorkoutFromAPI workout);
        void onAddButtonClick(WorkoutFromAPI workout);
    }

    public WorkoutAdapter(List<WorkoutFromAPI> workoutList, OnItemClickListener listener)
    {
        this.workoutList = workoutList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_workout, parent, false);
        return new WorkoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutViewHolder holder, int position)
    {
        WorkoutFromAPI workout = workoutList.get(position);
        holder.bind(workout, listener);
    }

    @Override
    public int getItemCount()
    {
        return workoutList != null ? workoutList.size() : 0;
    }

    public void updateWorkoutList(List<WorkoutFromAPI> newList)
    {
        workoutList = newList;
        notifyDataSetChanged();
    }

    public static class WorkoutViewHolder extends RecyclerView.ViewHolder
    {
        private final TextView workoutName;
        private final Button addButton;

        public WorkoutViewHolder(View itemView)
        {
            super(itemView);
            workoutName = itemView.findViewById(R.id.NameTextView);
            addButton = itemView.findViewById(R.id.addButtonToActiveWorkouts);
        }

        public void bind(WorkoutFromAPI workout, OnItemClickListener listener)
        {
            workoutName.setText(workout.getName());
            itemView.setOnClickListener(v ->
            {
                if (listener != null)
                {
                    listener.onItemClick(workout);
                }
            });
            addButton.setOnClickListener(v ->
            {
                if (listener != null)
                {
                    listener.onAddButtonClick(workout);
                }
            });
        }
    }
}
