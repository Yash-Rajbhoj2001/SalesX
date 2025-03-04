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

public class SignUpFragment extends Fragment {

    private EditText nameEditText, emailEditText, passwordEditText, repasswordEditText;
    private ImageView showHidePassword1, showHidePassword2;
    private boolean isPasswordVisible1 = false;
    private boolean isPasswordVisible2 = false;

//    public SignUpFragment() {
//        // Required empty public constructor
//    }

    public static SignUpFragment newInstance() {
        return new SignUpFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        // Initialize views
        nameEditText = view.findViewById(R.id.edittext_name);
        emailEditText = view.findViewById(R.id.edittext_signup_email);
        passwordEditText = view.findViewById(R.id.edittext_signup_password);
        repasswordEditText = view.findViewById(R.id.edittext_signup_repassword);
        showHidePassword1 = view.findViewById(R.id.show_hide_password_1);
        showHidePassword2 = view.findViewById(R.id.show_hide_password_2);
        Button signUpButton = view.findViewById(R.id.sign_up_submit);
        Button signInButton = view.findViewById(R.id.signin);

        // Redirect to SignInActivity when the button is clicked
        signInButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SignInFragment.class);
            startActivity(intent);
        });

        // Toggle Password Visibility
        showHidePassword1.setOnClickListener(v -> togglePasswordVisibility1());
        showHidePassword2.setOnClickListener(v -> togglePasswordVisibility2());

        // Handle Sign Up Button Click
        signUpButton.setOnClickListener(v -> validateInput());

        // Setup Spinner
        String[] items = {"Sign up as Manager", "Sign up as Employee"};
        Spinner spinner = view.findViewById(R.id.sign_up_spinner);
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

        return view;
    }

    private void togglePasswordVisibility1() {
        if (isPasswordVisible1) {
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            showHidePassword1.setImageResource(R.drawable.visibility_off);
        } else {
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            showHidePassword1.setImageResource(R.drawable.visibility);
        }
        isPasswordVisible1 = !isPasswordVisible1;
        // Move the cursor to the end of the text
        passwordEditText.setSelection(passwordEditText.length());
    }

    private void togglePasswordVisibility2() {
        if (isPasswordVisible2) {
            repasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            showHidePassword2.setImageResource(R.drawable.visibility_off);
        } else {
            repasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            showHidePassword2.setImageResource(R.drawable.visibility);
        }
        isPasswordVisible2 = !isPasswordVisible2;
        // Move the cursor to the end of the text
        repasswordEditText.setSelection(repasswordEditText.length());
    }

    private void validateInput() {
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String repassword = repasswordEditText.getText().toString().trim();

        // Check if name, email, password, or repassword is empty
        if (TextUtils.isEmpty(name)) {
            showToast("Name field cannot be empty.", true);
            return;
        }
        if (TextUtils.isEmpty(email)) {
            showToast("Email field cannot be empty.", true);
            return;
        }
        if (TextUtils.isEmpty(password)) {
            showToast("Password field cannot be empty.", true);
            return;
        }
        if (TextUtils.isEmpty(repassword)) {
            showToast("Please confirm your password.", true);
            return;
        }

        // Validate email
        if (!isValidEmail(email)) {
            showToast("Invalid email address. Must be a @gmail.com address.", true);
            return;
        }

        // Validate password
        String passwordValidationMessage = getPasswordValidationMessage(password);
        if (passwordValidationMessage != null) {
            showToast(passwordValidationMessage, true);
            return;
        }

        // Check if password and repassword match
        if (!password.equals(repassword)) {
            showToast("Passwords do not match.", true);
            return;
        }

        // Proceed with sign-up or other logic if validation passes
        showToast("Sign up successful!", false);
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