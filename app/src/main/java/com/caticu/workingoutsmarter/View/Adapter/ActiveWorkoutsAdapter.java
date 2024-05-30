package com.caticu.workingoutsmarter.View.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.caticu.workingoutsmarter.Model.ActiveWorkout.ActiveWorkoutModel;
import com.caticu.workingoutsmarter.R;

import java.util.ArrayList;
import java.util.List;

public class ActiveWorkoutsAdapter extends RecyclerView.Adapter<ActiveWorkoutsAdapter.ViewHolder> {

    private List<ActiveWorkoutModel> activeWorkouts;
    private List<ActiveWorkoutModel> selectedItems = new ArrayList<>();
    private OnItemLongClickListener longClickListener;
    private OnItemClickListener clickListener;

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, ActiveWorkoutModel workout);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, ActiveWorkoutModel workout);
    }

    public ActiveWorkoutsAdapter(List<ActiveWorkoutModel> activeWorkouts, OnItemLongClickListener longClickListener, OnItemClickListener clickListener) {
        this.activeWorkouts = activeWorkouts;
        this.longClickListener = longClickListener;
        this.clickListener = clickListener;
    }

    public void setActiveWorkouts(List<ActiveWorkoutModel> activeWorkouts) {
        this.activeWorkouts = activeWorkouts;
        notifyDataSetChanged();
    }

    public void setSelectedItems(List<ActiveWorkoutModel> selectedItems) {
        this.selectedItems = selectedItems;
        notifyDataSetChanged();
    }

    public void removeSelectedItems(List<ActiveWorkoutModel> itemsToRemove) {
        activeWorkouts.removeAll(itemsToRemove);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_active_workout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ActiveWorkoutModel workout = activeWorkouts.get(position);
        holder.bind(workout, selectedItems.contains(workout), longClickListener, clickListener);
    }

    @Override
    public int getItemCount() {
        return activeWorkouts.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView workoutName;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            workoutName = itemView.findViewById(R.id.workoutName);
        }

        void bind(ActiveWorkoutModel workout, boolean isSelected, OnItemLongClickListener longClickListener, OnItemClickListener clickListener) {
            workoutName.setText(workout.getName());
            itemView.setBackgroundColor(isSelected ? itemView.getContext().getResources().getColor(R.color.selected_item) : itemView.getContext().getResources().getColor(R.color.unselected_item));
            itemView.setOnLongClickListener(v -> {
                longClickListener.onItemLongClick(itemView, workout);
                return true;
            });
            itemView.setOnClickListener(v -> {
                clickListener.onItemClick(itemView, workout);
            });
        }
    }

}