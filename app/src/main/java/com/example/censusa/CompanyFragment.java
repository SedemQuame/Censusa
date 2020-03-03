package com.example.censusa;

import android.app.DatePickerDialog;
import android.app.DownloadManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

public class CompanyFragment extends Fragment {

    private static final String TAG = "CompanyFragment";
    View v;
    private Button submitBtn;
    private EditText companyName, companyCategory, companyFounders, dateFounded, numOfCompanyEmployees, minWage, maxWage, companyEmail, companyPhone, companyPostalAddress;
    private ProgressBar progress;
    private String BASE_URL = "https://radiant-headland-52474.herokuapp.com/createCompanyEntry/";
    private int mYear, mMonth, mDay;

    public CompanyFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.company_fragment, container, false);
        companyName = v.findViewById(R.id.companyNameEdt);
        companyCategory = v.findViewById(R.id.companyCategoryEdt);
        companyFounders = v.findViewById(R.id.founderEdt);
        dateFounded = v.findViewById(R.id.dateFoundedEdt);
        numOfCompanyEmployees = v.findViewById(R.id.numberOfEmployedCitizens);
        minWage = v.findViewById(R.id.minWageEdt);
        maxWage = v.findViewById(R.id.maxWageEdt);
        companyEmail = v.findViewById(R.id.companyEmail);
        companyPhone = v.findViewById(R.id.compnayPhone);
        companyPostalAddress = v.findViewById(R.id.companyPostalAddressEdt);
        progress = v.findViewById(R.id.progressBarCompany);

        submitBtn = v.findViewById(R.id.companySubmitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.setVisibility(View.VISIBLE);
                String FORMATTED_URL = formatCompanyRequestUrl(companyName.getText().toString(),
                        companyCategory.getText().toString(),
                        companyFounders.getText().toString(),
                        dateFounded.getText().toString(),
                        numOfCompanyEmployees.getText().toString(),
                        minWage.getText().toString(),
                        maxWage.getText().toString(),
                        companyEmail.getText().toString(),
                        companyPhone.getText().toString(),
                        companyPostalAddress.getText().toString());

                Log.d(TAG, "FORMATTED URL: " + FORMATTED_URL);
                sendingRequest(FORMATTED_URL);
                progress.setVisibility(View.GONE);
            }
        });


        dateFounded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Date of Birth", Toast.LENGTH_SHORT).show();
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                dateFounded.setText(dayOfMonth + "." + (monthOfYear + 1) + "." + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        return v;
    }


    private String formatCompanyRequestUrl(String name, String category, String founders, String dateFounded, String numOfEmployees, String minWage, String maxWage,
                                           String email, String phone, String postal) {
        StringBuilder builder = new StringBuilder(BASE_URL);
        builder.append(name.replace("-", "%2D")).append("-")
                .append(category.replace("-", "%2D")).append("-")
                .append(founders.replace("-", "%2D")).append("-")
                .append(dateFounded.replace("-", "%2D")).append("-")
                .append(numOfEmployees.replace("-", "%2D")).append("-")
                .append(minWage.replace("-", "%2D")).append("-")
                .append(maxWage.replace("-", "%2D")).append("-")
                .append(email.replace("-", "%2D")).append("-")
                .append(phone.replace("-", "%2D")).append("-")
                .append(postal.replace("-", "%2D"));

        return builder.toString().replace(" ", "%20");
    }

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
                        companyName.setText("");
                        companyCategory.setText("");
                        companyFounders.setText("");
                        dateFounded.setText("");
                        numOfCompanyEmployees.setText("");
                        minWage.setText("");
                        maxWage.setText("");
                        companyEmail.setText("");
                        companyPhone.setText("");
                        companyPostalAddress.setText("");
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
