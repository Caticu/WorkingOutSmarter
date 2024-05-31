package com.caticu.workingoutsmarter.View.Fragments.ActiveWorkouts;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.caticu.workingoutsmarter.R;

import java.util.Calendar;

public class DatePickerFragment extends Fragment {

    private DatePicker datePicker;
    private Button confirmButton;
    private OnDateSelectedListener listener;

    public interface OnDateSelectedListener {
        void onDateSelected(String date);
    }

    public DatePickerFragment(OnDateSelectedListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_date_picker, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        datePicker = view.findViewById(R.id.datePicker);
        confirmButton = view.findViewById(R.id.confirmButton);

        confirmButton.setOnClickListener(v -> {
            int day = datePicker.getDayOfMonth();
            int month = datePicker.getMonth();
            int year = datePicker.getYear();


            String date = day + "/" + (month + 1) + "/" + year;
            if (listener != null) {
                listener.onDateSelected(date);
                getParentFragmentManager().popBackStack();
            }
        });
    }
}
