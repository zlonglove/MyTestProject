/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ISHello.preference;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.ishelloword.R;

/**
 * Demonstration of PreferenceFragment, showing a single fragment in an
 * activity.
 */
public class FragmentPreferences extends PreferenceActivity {
    private final String TAG = "FragmentPreferences";
    private CheckBoxPreference checkBoxPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content. 不是继承PreferenceActivity的做法
        // getFragmentManager().beginTransaction().replace(android.R.id.content,
        // new PrefsFragment()).commit();
        addPreferencesFromResource(R.xml.preferences);
        checkBoxPreference = ((CheckBoxPreference) findPreference("checkbox_preference1"));
        checkBoxPreference.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Log.i(TAG, "--->onPreferenceChange--" + preference.getKey());
                return true;
            }
        });

        checkBoxPreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Log.i(TAG, "--->onPreferenceClick--" + preference.getKey());
                Log.i(TAG,
                        "--->value--" + PreferenceManager.getDefaultSharedPreferences(FragmentPreferences.this).getBoolean(preference.getKey(), true));
                return true;
            }
        });
    }

    public static class PrefsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.preferences);
        }
    }

}
