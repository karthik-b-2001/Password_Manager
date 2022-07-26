package com.example.password_manager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.example.password_manager.sqlite.DBconnect;
import com.example.password_manager.validation.InputValidation;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;
import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity{
    private final AppCompatActivity activity = MainActivity.this;
    private ScrollView ScrollView;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;

    private TextInputEditText Email_Input;
    private TextInputEditText Password_Input;

    private MaterialButton appCompatButtonLogin;
    private MaterialButton textViewLinkRegister;
    private InputValidation inputValidation;
    private DBconnect databaseHelper;

    BiometricPrompt biometricPrompt;
    BiometricPrompt.PromptInfo promptInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

        initObjects();
        appCompatButtonLogin.setOnClickListener(v -> verifyFromSQLite());

        textViewLinkRegister.setOnClickListener(v -> {
            Log.d("onclick","in signup");
            Intent intentRegister = new Intent(MainActivity.this, SignUp.class);
            startActivity(intentRegister);

        });
        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate()) {
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE: {
                Toast.makeText(getApplicationContext(), "Device has no Fingerprint", Toast.LENGTH_SHORT).show();
            }

            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE: {
                Toast.makeText(getApplicationContext(), "Not Working", Toast.LENGTH_SHORT).show();
            }

            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED: {
                Toast.makeText(getApplicationContext(), "No FingerPrint Assigned", Toast.LENGTH_SHORT).show();
            }

            case BiometricManager.BIOMETRIC_SUCCESS:{
                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
            }
        }
        Executor executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(MainActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();
                ScrollView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });
        promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("Password Manager").setDescription("Use Fingerprint or Pin to login").setDeviceCredentialAllowed(true).build();
        biometricPrompt.authenticate(promptInfo);



    }
    private void initViews() {
        ScrollView = findViewById(R.id.ScrollView);
        textInputLayoutEmail = findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = findViewById(R.id.textInputLayoutPassword);
        Email_Input = findViewById(R.id.Email_Input);
        Password_Input = findViewById(R.id.Password_Input);
        appCompatButtonLogin = findViewById(R.id.appCompatButtonLogin);
        textViewLinkRegister = findViewById(R.id.textViewLinkRegister);
    }



    private void initObjects() {
        databaseHelper = new DBconnect(activity);
        inputValidation = new InputValidation(activity);
    }

    private void verifyFromSQLite() {
        if (!inputValidation.isInputEditTextFilled(Email_Input, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextEmail(Email_Input, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(Password_Input, textInputLayoutPassword, getString(R.string.error_message_email))) {
            return;
        }
        if (databaseHelper.checkUser(Objects.requireNonNull(Email_Input.getText()).toString().trim()
                , Objects.requireNonNull(Password_Input.getText()).toString().trim()) && databaseHelper.checkPrimeUser(Email_Input.getText().toString().trim()
                , Password_Input.getText().toString().trim())){
            Intent accountsIntent = new Intent(activity, Dashboard.class);
            accountsIntent.putExtra("EMAIL", Email_Input.getText().toString().trim());
            emptyInputEditText();
            startActivity(accountsIntent);
        } else {
            // Snack Bar to show success message that record is wrong
            Snackbar.make(ScrollView, getString(R.string.error_valid_email_password), Snackbar.LENGTH_LONG).show();
        }
    }

//    private void doLogin(){
//        String encryptedData = null;
//        if (!inputValidation.isInputEditTextFilled(Email_Input, textInputLayoutEmail, getString(R.string.error_message_email))) {
//            return;
//        }
//        if (!inputValidation.isInputEditTextEmail(Email_Input, textInputLayoutEmail, getString(R.string.error_message_email))) {
//            return;
//        }
//        if (!inputValidation.isInputEditTextFilled(Password_Input, textInputLayoutPassword, getString(R.string.error_message_email))) {
//            return;
//        }
//        String Email = Objects.requireNonNull(Email_Input.getText()).toString().trim();
//        String Password = Objects.requireNonNull(Password_Input.getText()).toString().trim();
//        String storedPassword = "password";
//
//
//        if(Email.equals("admin@gmail.com")&&Password.equals(storedPassword)){
//            Intent accountsIntent = new Intent(activity, Dashboard.class);
//            accountsIntent.putExtra("EMAIL", Email_Input.getText().toString().trim());
//            emptyInputEditText();
//            startActivity(accountsIntent);
//        }
//        else {
//            // Snack Bar to show success message that record is wrong
//            Snackbar.make(ScrollView, getString(R.string.error_valid_email_password), Snackbar.LENGTH_LONG).show();
//        }
//    }
    private void emptyInputEditText() {
        Email_Input.setText(null);
        Password_Input.setText(null);
    }
}