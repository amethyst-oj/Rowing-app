package com.example.row.implementation;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

public class DatePickerDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Create a new instance of DatePickerDialog and return it
        return new android.app.DatePickerDialog(getActivity(), (view, year, month, dayOfMonth) -> {
            // Handle the date selected by the user
            // You can pass the selected date back to the activity or fragment
        }, 2025, 0, 1); // Default date is set to January 1, 2023
    }

}
