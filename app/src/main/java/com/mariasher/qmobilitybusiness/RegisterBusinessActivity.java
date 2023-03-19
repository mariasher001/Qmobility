package com.mariasher.qmobilitybusiness;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.FirebaseDatabase;
import com.mariasher.qmobilitybusiness.database.BusinessInfo;
import com.mariasher.qmobilitybusiness.databinding.ActivityRegisterBusinessBinding;

import java.util.UUID;

public class RegisterBusinessActivity extends AppCompatActivity implements LocationListener {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private ActivityRegisterBusinessBinding binding;
    private FirebaseDatabase mReal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBusinessBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        permissions();
        mReal = FirebaseDatabase.getInstance();
    }

    public void registerBusinessButtonClicked(View view) {
        setBusinessData();
    }

    private void setBusinessData() {
        String businessName = binding.businessNameEditText.getText().toString();
        String businessPhoneNumber = binding.businessPhoneNumberEditText.getText().toString();
        String businessEmail = binding.businessEmailEditText.getText().toString();
        String businessAddress = binding.businessAddressEditText.getText().toString();
        String businessLatitude = binding.businessLatitudeEditText.getText().toString();
        String businessLongitude = binding.businessLongitudeEditText.getText().toString();
        String businessType = binding.businessTypeEditText.getText().toString();
        String locationManagersName = binding.locationManagerNameEditText.getText().toString();
        String businessID = businessName + "-" + UUID.randomUUID().toString();

        if (checkBusinessInformation(businessName, businessPhoneNumber, businessEmail, businessAddress,
                businessLatitude, businessLongitude, businessType, locationManagersName)) {
            BusinessInfo businessInfo = new BusinessInfo(businessName, businessPhoneNumber, businessEmail, businessAddress,
                    businessLatitude, businessLongitude, businessType, locationManagersName, businessID);

            createRealtimeDataBase(businessInfo);
        }
    }

    private boolean checkBusinessInformation(String businessName, String businessPhoneNumber, String businessEmail, String businessAddress, String businessLatitude, String businessLongitude, String businessType, String locationManagersName) {
        if (businessName.isEmpty())
            return setError(binding.businessNameEditText, " Business Name is Required!");
        if (businessPhoneNumber.isEmpty())
            return setError(binding.businessPhoneNumberEditText, "Business PhoneNumber is Required!");
        if (businessEmail.isEmpty())
            return setError(binding.businessEmailEditText, "BusinessEmail is Required!");
        if (!Patterns.EMAIL_ADDRESS.matcher(businessEmail).matches())
            return setError(binding.businessEmailEditText, "Please enter Correct Email Address");
        if (businessAddress.isEmpty())
            return setError(binding.businessAddressEditText, "BusinessAddress is Required!");
        if (businessLatitude.isEmpty())
            return setError(binding.businessLatitudeEditText, "Business Latitude Coordinates are required!");
        if (businessLongitude.isEmpty())
            return setError(binding.businessLongitudeEditText, "BusinessLongitude Coordinates are Required!");
        if (businessType.isEmpty())
            return setError(binding.businessTypeEditText, "BusinessType is Required!");
        if (locationManagersName.isEmpty())
            return setError(binding.locationManagerNameEditText, "Location Manager Name is required!");

        return true;
    }

    private boolean setError(EditText editText, String message) {
        editText.setError(message);
        editText.requestFocus();
        return false;
    }


    private void createRealtimeDataBase(BusinessInfo businessInfo) {
        mReal.getReference("QMobility")
                .child("Businesses")
                .child(businessInfo.getBusinessID())
                .child("BusinessDetails")
                .setValue(businessInfo).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Registration Successful!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, "Registration Failed!", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void getLocationUsingGPSClicked(View view) {
        if (binding.getGpsBusinessAddressCheckBox.isChecked()) {
            getGPSLocation();
            binding.businessLatitudeEditText.setEnabled(false);
            binding.businessLongitudeEditText.setEnabled(false);

        } else {
            binding.businessLatitudeEditText.setText("");
            binding.businessLongitudeEditText.setText("");
            binding.businessLatitudeEditText.setEnabled(true);
            binding.businessLongitudeEditText.setEnabled(true);
        }
    }

    @SuppressLint("MissingPermission")
    private void getGPSLocation() {
        // Get the location manager
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Create a criteria with your requirements
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
        criteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);

        // Request location updates with the criteria and the location listener
        locationManager.requestSingleUpdate(criteria, this, null);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onLocationChanged(@NonNull Location location) {
        Log.i("Location Coordinates", location.toString());
        binding.businessLatitudeEditText.setText("" + location.getLatitude());
        binding.businessLongitudeEditText.setText("" + location.getLongitude());
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        Log.i("Provider Disabled", provider);
        Toast.makeText(getApplicationContext(), "Please enable your location", Toast.LENGTH_LONG).show();
    }

    private void permissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length != 0) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Oops!! You didn't allow permissions!", Toast.LENGTH_SHORT).show();
                    binding.getGpsBusinessAddressCheckBox.setEnabled(false);
                }
            }
        }
    }
}


