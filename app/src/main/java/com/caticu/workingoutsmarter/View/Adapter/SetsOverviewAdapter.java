package com.caticu.workingoutsmarter.View.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.caticu.workingoutsmarter.Model.ActiveWorkout.Set;
import com.caticu.workingoutsmarter.R;

import java.util.ArrayList;
import java.util.List;

public class SetsOverviewAdapter extends RecyclerView.Adapter<SetsOverviewAdapter.SetViewHolder>
{

    private List<Set> sets = new ArrayList<>();

    public void setSets(List<Set> sets)
    {
        this.sets = sets;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_set_fields_for_active_workouts, parent, false);
        return new SetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SetViewHolder holder, int position)
    {
        Set set = sets.get(position);
        holder.bind(set);
    }

    @Override
    public int getItemCount()
    {
        return sets.size();
    }

    static class SetViewHolder extends RecyclerView.ViewHolder
    {
        private final TextView setLabel;
        private final EditText repetitionsEditText;
        private final EditText weightEditText;

        public SetViewHolder(@NonNull View itemView)
        {
            super(itemView);
            setLabel = itemView.findViewById(R.id.setLabel);
            repetitionsEditText = itemView.findViewById(R.id.repetitionsEditText);
            weightEditText = itemView.findViewById(R.id.weightEditText);
        }

        public void bind(Set set)
        {
            setLabel.setText(String.valueOf(set.getNumber()));
            repetitionsEditText.setText(String.valueOf(set.getRepetitions()));
            weightEditText.setText(String.valueOf(set.getWeight()));
        }
    }
}
