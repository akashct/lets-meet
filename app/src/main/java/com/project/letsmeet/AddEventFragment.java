package com.project.letsmeet;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddEventFragment extends Fragment {

    private SimpleDateFormat mSimpleDateFormat;
    private Calendar mCalendar;
    Button btnStartDate, btnEndDate, addPart;
    private static final String TAG = "CreateEvent";
    Button txtLocation;
    private static final int ERROR_DIALOG_REQUEST = 9001;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_event, container, false);

        btnStartDate = (Button) view.findViewById(R.id.btnStartDate);
        btnEndDate = (Button) view.findViewById(R.id.btnEndDate);
        txtLocation = (Button) view.findViewById(R.id.txtLocation);
        addPart = (Button)view.findViewById(R.id.addParticipant);
        mSimpleDateFormat = new SimpleDateFormat("MM/dd/yyyy h:mm a", Locale.getDefault());

        btnStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendar = Calendar.getInstance();
                new DatePickerDialog(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_DARK, mDateDataSet, mCalendar.get(Calendar.YEAR),
                        mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        btnEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendar = Calendar.getInstance();
                new DatePickerDialog(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_DARK, mDateDataSet2, mCalendar.get(Calendar.YEAR),
                        mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        txtLocation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!isPlayServicesOK()){
                    return;
                }
                else {
                    Intent intent = new Intent(getActivity(),MapsActivity.class);
                    startActivityForResult(intent, 1);
                }
            }
        });

        addPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FindUserActivity.class);
                startActivityForResult(intent, 2);
            }
        });



        return view;
    }

    private final DatePickerDialog.OnDateSetListener mDateDataSet = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mCalendar.set(Calendar.YEAR, year);
            mCalendar.set(Calendar.MONTH, monthOfYear);
            mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            new TimePickerDialog(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_DARK,mTimeDataSet, mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE), false).show();
        }
    };
    private final DatePickerDialog.OnDateSetListener mDateDataSet2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mCalendar.set(Calendar.YEAR, year);
            mCalendar.set(Calendar.MONTH, monthOfYear);
            mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            new TimePickerDialog(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_DARK,mTimeDataSet2, mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE), false).show();
        }
    };

    /* After user decided on a time, save them into our calendar instance, and now parse what our calendar has into the TextView */
    private final TimePickerDialog.OnTimeSetListener mTimeDataSet = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            mCalendar.set(Calendar.MINUTE, minute);
            btnStartDate.setText(mSimpleDateFormat.format(mCalendar.getTime()));
        }
    };
    private final TimePickerDialog.OnTimeSetListener mTimeDataSet2 = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            mCalendar.set(Calendar.MINUTE, minute);
            btnEndDate.setText(mSimpleDateFormat.format(mCalendar.getTime()));
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 1) {
            if (data.hasExtra("myData1")) {
                Log.d("MAP_RETURN", "The returning data  for map is: "+data.getExtras().getString("myData1"));
                Toast.makeText(getActivity(), "The returning data  for map is: "+data.getExtras().getString("myData1"),
                        Toast.LENGTH_LONG).show();
            }
        }
        else if(requestCode == RESULT_OK && requestCode == 2) {
            if (data.hasExtra("myData1")) {
                Log.d("PART_RETURN", "The returning data  for participants is: "+data.getExtras().getString("myData1"));
                Toast.makeText(getActivity(), data.getExtras().getString("myData1"),
                        Toast.LENGTH_LONG).show();
            }
        }
    }


    public String validate(int time) {
        if (time < 10)
            return "0" + String.valueOf(time);
        else
            return String.valueOf(time);
    }

    public boolean isPlayServicesOK(){
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getActivity()); // check whether play service is available or not
        if (available == ConnectionResult.SUCCESS){
            Log.d(TAG, "Google play services working");
            return true;
        }
        else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)){ // check whether updating play services will fix this issue
            Log.d(TAG, "Google play error occurred but can resolve");

            // Google will show an error dialog and solution for it
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(getActivity(),available,ERROR_DIALOG_REQUEST);
            dialog.show();
        }else {
            Toast.makeText(getActivity(), "Play services unavailable", Toast.LENGTH_SHORT).show();
        }
        return false;
    }







}
