package com.app.guide.library;

import android.support.v4.app.Fragment;

/**
 * Created by hubert
 * <p>
 * Created on 2017/9/13.
 */

public class V4ListenerFragment extends Fragment {

    FragmentLifecycle mFragmentLifecycle;

    public void setFragmentLifecycle(FragmentLifecycle lifecycle) {
        mFragmentLifecycle = lifecycle;
    }

    @Override
    public void onStart() {
        super.onStart();
        mFragmentLifecycle.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mFragmentLifecycle.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mFragmentLifecycle.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mFragmentLifecycle.onDestroy();
    }
}
