package com.example.emano.sendwithme.HomePackage;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.emano.sendwithme.R;

public class BlankActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blank);
        fragmentManager = getSupportFragmentManager(); //setou

        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.add(R.id.containerId, new HomeFragment(), "MapsFragment");

        transaction.commitAllowingStateLoss();

    }
}
