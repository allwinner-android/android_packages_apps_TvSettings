/*
 * Copyright (C) 2014 The Android Open Source Project
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

package com.android.tv.settings;

import android.app.Fragment;
import android.view.KeyEvent;

/**
 * Main settings which loads up the top level headers.
 */
public class MainSettings extends TvSettingsActivity {

    @Override
    protected Fragment createSettingsFragment() {
        return SettingsFragment.newInstance();
    }

    public static class SettingsFragment extends BaseSettingsFragment {

        public static SettingsFragment newInstance() {
            return new SettingsFragment();
        }

        @Override
        public void onPreferenceStartInitialScreen() {
            final MainFragment fragment = MainFragment.newInstance();
            startPreferenceFragment(fragment);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_SETTINGS) {
            super.finish();
        }
        return super.onKeyDown(keyCode,event);
    }

}
