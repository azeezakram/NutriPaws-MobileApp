package com.example.mobiledev;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

public class EditProfileActivity extends AppCompatActivity {

    private EditText nameEditText, passwordEditText, addressEditText;
    private Spinner paymentMethodSpinner;
    private ArrayAdapter<CharSequence> adapter;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_edit_profile);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.primaryDarkColor));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }


        nameEditText = findViewById(R.id.name);
        passwordEditText = findViewById(R.id.password);
        addressEditText = findViewById(R.id.address);
        paymentMethodSpinner = findViewById(R.id.payment_method_spinner);
        Button updateButton = findViewById(R.id.update_button);
        user = new User(this);

        adapter = ArrayAdapter.createFromResource(this,
                R.array.payment_methods, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        paymentMethodSpinner.setAdapter(adapter);

        loadUserDetails();

        updateButton.setOnClickListener(v -> updateUserDetails());
    }

    private void loadUserDetails() {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        int userId = sharedPreferences.getInt("user_id", -1);

        if (user.loadUser(userId)) {
            nameEditText.setText(user.getName());
            passwordEditText.setText(user.getPassword());
            addressEditText.setText(user.getAddress());

            String paymentMethod = user.getPaymentMethod();
            if (paymentMethod != null) {
                int spinnerPosition = adapter.getPosition(paymentMethod);
                paymentMethodSpinner.setSelection(spinnerPosition);
            }
        }
    }

    private void updateUserDetails() {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        int userId = sharedPreferences.getInt("user_id", -1);
        Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);

        if (userId != -1) {
            user.setId(userId);
            user.setName(nameEditText.getText().toString().trim());
            user.setPassword(passwordEditText.getText().toString().trim());
            user.setAddress(addressEditText.getText().toString().trim());
            user.setPaymentMethod(paymentMethodSpinner.getSelectedItem().toString());

            startActivity(intent);
            finish();
            Toast.makeText(this, "Profile successfully updated!", Toast.LENGTH_SHORT).show();
        }
    }

    public void goBack(View v) {
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);
    }
}
