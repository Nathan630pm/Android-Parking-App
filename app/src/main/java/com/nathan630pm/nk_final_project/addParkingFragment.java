package com.nathan630pm.nk_final_project;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class addParkingFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "addParkingFragment";
    private TextView TVBuildingCodeError, TVPlateNoError, TVSuitError, TVAddressError, TVDateError;
    private EditText ETBuildingCode, ETPlateNo, ETSuit, ETAddress;
    private CheckBox CBUseLocation;
    private DatePicker DPDatePicker;
    private Spinner SHours;
    private Button BAddParking;

    private int hoursSelect = 0;
    private ArrayList<String> hoursSelectArray = new ArrayList<String>();


    private Context context;

    private Boolean locationIsChecked = false;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_add_parking, container, false);


        this.context = v.getContext();

        this.hoursSelectArray.add("1 Hour or less");
        this.hoursSelectArray.add("4-hour");
        this.hoursSelectArray.add("12-hour");
        this.hoursSelectArray.add("24-hour");


        TVBuildingCodeError = v.findViewById(R.id.TVBuildingCodeError);
        TVPlateNoError = v.findViewById(R.id.TVPlateNoError);
        TVSuitError = v.findViewById(R.id.TVSuitError);
        TVAddressError = v.findViewById(R.id.TVAddressError);
        TVDateError = v.findViewById(R.id.TVDateError);

        ETBuildingCode = v.findViewById(R.id.ETBuildingCode);
        ETPlateNo = v.findViewById(R.id.ETPlateNo);
        ETSuit = v.findViewById(R.id.ETSuit);
        ETAddress = v.findViewById(R.id.ETAddress);

        CBUseLocation = v.findViewById(R.id.CBUseLocation);
        CBUseLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: checkbox clicked");
                if(locationIsChecked){
                    locationIsChecked = false;

                    ETAddress.setEnabled(true);

                }else {
                    locationIsChecked = true;

                    ETAddress.setEnabled(false);
                }

            }
        });

        DPDatePicker = v.findViewById(R.id.DPDatePicker);

        SHours = v.findViewById(R.id.SHours);
        SHours.setOnItemSelectedListener(this);

        ArrayAdapter<String> aa = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, hoursSelectArray);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SHours.setAdapter(aa);

        BAddParking = v.findViewById(R.id.BAddParking);







        return v;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(context, hoursSelectArray.get(i) + " SELECTION: " + i , Toast.LENGTH_LONG).show();
        hoursSelect = i;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}