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

package com.android.tv.settings.connectivity;

import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.internal.logging.nano.MetricsProto;
import com.android.tv.settings.R;
import com.android.tv.settings.connectivity.setup.HotspotNameState;
import com.android.tv.settings.connectivity.setup.HotspotNameInvalidState;
import com.android.tv.settings.connectivity.util.State;
import com.android.tv.settings.connectivity.util.StateMachine;
import com.android.tv.settings.core.instrumentation.InstrumentedActivity;

/**
 * Manual-style modify wifi hotspot name
 */
public class HotspotNameActivity extends InstrumentedActivity
            implements State.FragmentChangeListener {
    private static final String TAG = "HotspotNameActivity";
    private State mHotspotNameState;
    private State mSaveSuccessState;
    private State mHotspotNameInvalidState;
    private StateMachine mStateMachine;
    private final StateMachine.Callback mStateMachineCallback = new StateMachine.Callback() {
        @Override
        public void onFinish(int result) {
            setResult(result);
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi_container);
        mStateMachine = ViewModelProviders.of(this).get(StateMachine.class);
        mStateMachine.setCallback(mStateMachineCallback);

        mHotspotNameState = new HotspotNameState(this);
        mSaveSuccessState = new SaveSuccessState(this);
        mHotspotNameInvalidState = new HotspotNameInvalidState(this);

        mStateMachine.addState(
                mHotspotNameState,
                StateMachine.CONTINUE,
                mSaveSuccessState);
        mStateMachine.addState(
                mHotspotNameState,
                StateMachine.SOFTAP_NAME_INVALID,
                mHotspotNameInvalidState
        );

        /* Hotspot Name Setup Invalid */
        mStateMachine.addState(
                mHotspotNameInvalidState,
                StateMachine.CONTINUE,
                mHotspotNameState
        );

        mStateMachine.setStartState(mHotspotNameState);
        mStateMachine.start(true);
    }

    @Deprecated
    public int getMetricsCategory() {
        // do not log visibility.
        return MetricsProto.MetricsEvent.WIFI_TETHER_SETTINGS;
    }

    @Override
    public void onBackPressed() {
        mStateMachine.back();
    }

    private void updateView(Fragment fragment, boolean movingForward) {
        if (fragment != null) {
            FragmentTransaction updateTransaction = getSupportFragmentManager().beginTransaction();
            if (movingForward) {
                updateTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            } else {
                updateTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
            }
            updateTransaction.replace(R.id.wifi_container, fragment, TAG);
            updateTransaction.commit();
        }
    }

    @Override
    public void onFragmentChange(Fragment newFragment, boolean movingForward) {
        updateView(newFragment, movingForward);
    }
}
