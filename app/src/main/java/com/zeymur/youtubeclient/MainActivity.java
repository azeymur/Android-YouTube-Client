package com.zeymur.youtubeclient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private final String KEY_FRAGMENT =  "key_fragment";
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            currentFragment = getSupportFragmentManager().getFragment(savedInstanceState, KEY_FRAGMENT);
        } else {
            currentFragment = YouTubeFragment.newInstance("UCbDuARPGsQJBG8dELL-37zw",
                    YOUTUBE_IDTYPE.CHANNEL,
                    "AIzaXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.frameLayout, currentFragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        getSupportFragmentManager().putFragment(outState, KEY_FRAGMENT, currentFragment);
    }

}