package tallerii.stories.fragments.profile;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;

import tallerii.stories.R;
import tallerii.stories.helpers.DateUtils;

public class DatePickerFragment extends DialogFragment
                            implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        String date = getArguments().getString("date");
        initializeCalendar(c, date);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT, this, year, month, day);
    }

    private void initializeCalendar(Calendar c, String date) {
        if (date != null && date.length() >= 9) {
            try {
                c.setTime(DateUtils.getDateWithDayPrecision(date));
            } catch (ParseException e) {
                e.printStackTrace();
                c.add(Calendar.YEAR, -18);
            }
        } else {
            c.add(Calendar.YEAR, -18);
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getActivity().getApplicationContext());
        TextView birthdayTextView = getActivity().findViewById(R.id.birthday);
        birthdayTextView.setText(dateFormat.format(calendar.getTime()));
    }
}