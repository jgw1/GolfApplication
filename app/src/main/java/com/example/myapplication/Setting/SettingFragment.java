package com.example.myapplication.Setting;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.example.myapplication.R;

public class SettingFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings_preference);
    }
}