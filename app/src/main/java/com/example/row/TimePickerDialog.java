package com.example.row;

import android.app.Dialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class TimePickerDialog extends DialogFragment implements android.app.TimePickerDialog.OnTimeSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Create a new instance of TimePickerDialog and return it
        final Calendar cal = Calendar.getInstance();
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        return new android.app.TimePickerDialog(getActivity(),this, minute,second,true);
    }
    public void onTimeSet(TimePicker view,  int minute, int second) {
                // Handle the time selected by the user
                // You can pass the selected time back to the activity or fragment
                // For example, you can use a callback interface or set a result

    }


}
