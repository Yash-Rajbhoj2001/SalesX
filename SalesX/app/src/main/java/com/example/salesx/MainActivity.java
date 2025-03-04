package com.example.salesx;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class MainActivity extends AppCompatActivity
{
    Button signIn, signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        signIn = findViewById(R.id.sign_in);
        signUp = findViewById(R.id.sign_up);

        signIn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                replaceFragment(new SignInFragment());
            }
        });

        signUp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                replaceFragment(new SignUpFragment());
            }
        });
    }
    private void replaceFragment(SignInFragment f1) {
        FragmentManager fragment = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragment.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentlayout, f1);
        fragmentTransaction.commit();
    }
    private void replaceFragment(SignUpFragment f2) {
        FragmentManager fragment = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragment.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentlayout, f2);
        fragmentTransaction.commit();
    }
}