package com.example.censusa;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.util.Calendar;

public class IndividualFragment extends Fragment {
    private static final String TAG = "IndividualFragment";
    private EditText nameEdt, fathersNameEdt, mothersNameEdt, numOfFamilyMembersEdt,
            dOBEdt, ageEdt, nationalityEdt, occupationEdt, genderEdt, maritalStatusEdt, residentialStatusEdt;

    private Button submitBtn;
    private String BASE_URL =  "https://radiant-headland-52474.herokuapp.com/createIndividualEntry/";

    private int mYear, mMonth, mDay;
    View v;
    public IndividualFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.individual_fragment, container, false);


        nameEdt = v.findViewById(R.id.personFullName);
        fathersNameEdt = v.findViewById(R.id.personFatherName);
        mothersNameEdt = v.findViewById(R.id.personNameOfMother);
        numOfFamilyMembersEdt = v.findViewById(R.id.numberOfFamilyMembers);
        dOBEdt = v.findViewById(R.id.dateOfBirth);
        ageEdt = v.findViewById(R.id.personAge);
        nationalityEdt = v.findViewById(R.id.nationality);
        occupationEdt = v.findViewById(R.id.occupation);
        genderEdt = v.findViewById(R.id.gender);
        maritalStatusEdt = v.findViewById(R.id.maritalStatus);
        residentialStatusEdt = v.findViewById(R.id.residentialStatus);


        submitBtn = v.findViewById(R.id.submitIndividualDataBtn);
        submitBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String FORMATTED_URL = formatCompanyRequestUrl(nameEdt.getText().toString(),
                        fathersNameEdt.getText().toString(),
                        mothersNameEdt.getText().toString(),
                        numOfFamilyMembersEdt.getText().toString(),
                        dOBEdt.getText().toString(),
                        ageEdt.getText().toString(),
                        nationalityEdt.getText().toString(),
                        occupationEdt.getText().toString(),
                        genderEdt.getText().toString(),
                        maritalStatusEdt.getText().toString(),
                        residentialStatusEdt.getText().toString());

                Log.d(TAG, "FORMATTED URL: " + FORMATTED_URL);
                sendingRequest(FORMATTED_URL);
            }
        });

        dOBEdt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getContext(), "Date of Birth", Toast.LENGTH_SHORT).show();
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                dOBEdt.setText(dayOfMonth + "." + (monthOfYear + 1) + "." + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });


        return v;
    }


    private String formatCompanyRequestUrl(String name, String father, String mother, String familyNum, String dob, String age, String nationality,
                                           String occupation, String gender, String maritalStatus, String residential){
        StringBuilder builder = new StringBuilder(BASE_URL);
        builder.append(name.replace("-", "%2D")).append("-")
                .append(father.replace("-", "%2D")).append("-")
                .append(mother.replace("-", "%2D")).append("-")
                .append(familyNum.replace("-", "%2D")).append("-")
                .append(dob.replace("-", "%2D")).append("-")
                .append(age.replace("-", "%2D")).append("-")
                .append(nationality.replace("-", "%2D")).append("-")
                .append(occupation.replace("-", "%2D")).append("-")
                .append(gender.replace("-", "%2D")).append("-")
                .append(maritalStatus.replace("-", "%2D")).append("-")
                .append(residential.replace("-", "%2D"));

        return builder.toString().replace(" ", "%20");    }

    private void sendingRequest(String URL) {
        RequestQueue queue = Volley.newRequestQueue(getContext());

        // prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.POST, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());
                        Toast.makeText(getContext(), "Request Successful", Toast.LENGTH_SHORT).show();
//                        clearing data stored, in the input fields.
                        nameEdt.setText("");
                        fathersNameEdt.setText("");
                        mothersNameEdt.setText("");
                        numOfFamilyMembersEdt.setText("");
                        dOBEdt.setText("");
                        ageEdt.setText("");
                        nationalityEdt.setText("");
                        occupationEdt.setText("");
                        genderEdt.setText("");
                        maritalStatusEdt.setText("");
                        residentialStatusEdt.setText("");

                        Snackbar.make(v, "Data recorded successfully", Snackbar.LENGTH_SHORT).show();
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
//                        Toast.makeText(getContext(), "Request Failed", Toast.LENGTH_SHORT).show();
                        Snackbar.make(v, "Data failed to record", Snackbar.LENGTH_SHORT).show();

                    }
                }

        );

// add it to the RequestQueue
        queue.add(getRequest);

    }
}
