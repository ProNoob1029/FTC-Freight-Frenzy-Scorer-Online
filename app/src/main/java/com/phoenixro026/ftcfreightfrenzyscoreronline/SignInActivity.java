package com.phoenixro026.ftcfreightfrenzyscoreronline;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.phoenixro026.ftcfreightfrenzyscoreronline.database.User;
import com.phoenixro026.ftcfreightfrenzyscoreronline.databinding.ActivitySigninBinding;

import java.util.Objects;

public class SignInActivity extends AppCompatActivity {

    protected ActivitySigninBinding binding;

    private static final String TAG = "SignInFragment";

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    public void showProgressBar() {
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        binding.progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySigninBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        // Click listeners
        binding.buttonSignIn.setOnClickListener(v -> signIn());
        binding.buttonSignUp.setOnClickListener(v -> signUp());
    }


    @Override
    public void onStart() {
        super.onStart();

        // Check auth on Activity start
        if (mAuth.getCurrentUser() != null) {
            onAuthSuccess(mAuth.getCurrentUser());
        }
    }

    private void signIn() {
        Log.d(TAG, "signIn");
        if (validateForm()) {
            return;
        }

        showProgressBar();
        String email = binding.fieldEmail.getText().toString();
        String password = binding.fieldPassword.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignInActivity.this, task -> {
                    Log.d(TAG, "signIn:onComplete:" + task.isSuccessful());
                    hideProgressBar();

                    if (task.isSuccessful()) {
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Sign In Failed",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void signUp() {
        Log.d(TAG, "signUp");
        if (validateForm()) {
            return;
        }

        showProgressBar();
        String email = binding.fieldEmail.getText().toString();
        String password = binding.fieldPassword.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignInActivity.this, task -> {
                    Log.d(TAG, "createUser:onComplete:" + task.isSuccessful());
                    hideProgressBar();

                    if (task.isSuccessful()) {
                        onAuthSuccess(Objects.requireNonNull(task.getResult().getUser()));
                    } else {
                        Toast.makeText(getApplicationContext(), "Sign Up Failed",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void onAuthSuccess(FirebaseUser user) {
        String username = usernameFromEmail(Objects.requireNonNull(user.getEmail()));

        // Write new user
        writeNewUser(user.getUid(), username, user.getEmail());

        // Go to MainFragment
        finish();
    }

    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(binding.fieldEmail.getText().toString())) {
            binding.fieldEmail.setError("Required");
            result = false;
        } else {
            binding.fieldEmail.setError(null);
        }

        if (TextUtils.isEmpty(binding.fieldPassword.getText().toString())) {
            binding.fieldPassword.setError("Required");
            result = false;
        } else {
            binding.fieldPassword.setError(null);
        }

        return !result;
    }

    private void writeNewUser(String userId, String name, String email) {
        User user = new User(name, email);

        mDatabase.child("users").child(userId).setValue(user);
    }

    @Override
    public void onBackPressed() {
    }
}