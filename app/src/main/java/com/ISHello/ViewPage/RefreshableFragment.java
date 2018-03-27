package com.ISHello.ViewPage;

import android.app.Fragment;

/**
 * An abstract class that defines a {@link Fragment} like refreshable
 */
public abstract class RefreshableFragment extends Fragment {

    /**
     * Method invoked when the fragment need to be refreshed
     */
    public abstract void refresh();
}
