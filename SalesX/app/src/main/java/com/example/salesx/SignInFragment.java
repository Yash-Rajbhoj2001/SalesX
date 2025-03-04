package com.example.salesx;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class SignInFragment extends Fragment {

    private EditText emailEditText, passwordEditText;
    private ImageView showHidePassword;
    private boolean isPasswordVisible = false;

//    public SignInFragment() {
//        // Required empty public constructor
//    }

    public static SignInFragment newInstance() {
        return new SignInFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        // Initialize views
        emailEditText = view.findViewById(R.id.edittext_email);
        passwordEditText = view.findViewById(R.id.edittext_password);
        showHidePassword = view.findViewById(R.id.show_hide_password);
//        Button signInSubmit = view.findViewById(R.id.sign_in_submit);
        Button register = view.findViewById(R.id.register);
//        Button forgot = view.findViewById(R.id.forgot_pass);

//         Redirect to Sign_Up activity
        register.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SignUpFragment.class);
            startActivity(intent);
        });

        // Redirect to Forgot_Password activity
//        forgot.setOnClickListener(v -> {
//            Intent intent = new Intent(getActivity(), Forgot_Password.class);
//            startActivity(intent);
//        });

        // Handle Sign In Button Click
//        signInSubmit.setOnClickListener(v -> {
//            // Validate input before proceeding
//            if (validateInput()) {
//                // If validation passes, navigate to Employee_Dashboard
//                Intent intent = new Intent(getActivity(), Employee_Dashboard.class);
//                startActivity(intent);
//                showToast("Sign in successful!", false);
//            }
//        });

        // Setup Spinner
        String[] items = {"Sign in as Manager", "Sign in as Employee"};
        Spinner spinner = view.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Spinner Item Selected Listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                showToast(selectedItem, false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Toggle Password Visibility
        showHidePassword.setOnClickListener(v -> togglePasswordVisibility());

        return view;
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            showHidePassword.setImageResource(R.drawable.visibility_off);
        } else {
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            showHidePassword.setImageResource(R.drawable.visibility);
        }
        isPasswordVisible = !isPasswordVisible;
        // Move the cursor to the end of the text
        passwordEditText.setSelection(passwordEditText.length());
    }

    private boolean validateInput() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Check if email or password is empty
        if (TextUtils.isEmpty(email)) {
            showToast("Email field cannot be empty.", true);
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            showToast("Password field cannot be empty.", true);
            return false;
        }

        // Validate email
        if (!isValidEmail(email)) {
            showToast("Invalid email address. Must be a @gmail.com address.", true);
            return false;
        }

        // Validate password
        String passwordValidationMessage = getPasswordValidationMessage(password);
        if (passwordValidationMessage != null) {
            showToast(passwordValidationMessage, true);
            return false;
        }

        // Validation successful
        return true;
    }

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches() && email.endsWith("@gmail.com");
    }

    private String getPasswordValidationMessage(String password) {
        if (password.length() < 8) {
            return "Password must be at least 8 characters long.";
        }

        boolean hasUpperCase = !password.equals(password.toLowerCase());
        boolean hasNumber = password.matches(".*\\d.*");
        boolean hasSpecialChar = password.matches(".*[^a-zA-Z0-9].*");

        StringBuilder message = new StringBuilder();

        if (!hasUpperCase) {
            message.append("Password must contain at least one uppercase letter. ");
        }
        if (!hasNumber) {
            message.append("Password must contain at least one number. ");
        }
        if (!hasSpecialChar) {
            message.append("Password must contain at least one special character. ");
        }

        return message.length() > 0 ? message.toString().trim() : null;
    }

    private void showToast(String message, boolean isError) {
        Toast toast = Toast.makeText(requireContext(), message, Toast.LENGTH_LONG);
        View view = toast.getView();

        // Change text color to red if it's an error
        if (isError) {
            if (view != null) {
                view.setBackgroundResource(android.R.color.holo_red_dark);
            }
        }
        toast.show();

        // Cancel the toast after 1 second
        new Handler().postDelayed(toast::cancel, 1000);
    }
}