package com.caticu.workingoutsmarter.View.Adapter;

import android.text.Editable;
import android.text.TextWatcher;
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

public class SetsAdapter extends RecyclerView.Adapter<SetsAdapter.ViewHolder> {

    private List<Set> sets;

    public SetsAdapter() {
        this.sets = new ArrayList<>();
    }

    public void setSets(List<Set> sets) {
        this.sets = sets;
        notifyDataSetChanged();
    }

    public void addSet(Set set) {
        sets.add(set);
        notifyItemInserted(sets.size() - 1);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_set_fields_for_active_workouts, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Set set = sets.get(position);
        holder.setNumber.setText(String.valueOf(set.getNumber()));
        holder.repetitionsEditText.setText(String.valueOf(set.getRepetitions()));
        holder.weightEditText.setText(String.valueOf(set.getWeight()));

        holder.repetitionsEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    int repetitions = Integer.parseInt(s.toString());
                    set.setRepetitions(repetitions);
                } catch (NumberFormatException e) {
                    set.setRepetitions(0);
                }
            }
        });

        holder.weightEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    float weight = Float.parseFloat(s.toString());
                    set.setWeight(weight);
                } catch (NumberFormatException e) {
                    set.setWeight(0);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return sets.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView setNumber;
        EditText repetitionsEditText;
        EditText weightEditText;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            setNumber = itemView.findViewById(R.id.setLabel);
            repetitionsEditText = itemView.findViewById(R.id.repetitionsEditText);
            weightEditText = itemView.findViewById(R.id.weightEditText);
        }
    }
}