package com.optimusinfo.elasticpath.cortex.common;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

/**
 * This Fragment manages a single background task and retains itself across
 * configuration changes.
 */
public abstract class EPFragment extends Fragment {
	/**
	 * Hold a reference to the parent Activity so we can report the task's
	 * current progress and results. The Android framework will pass us a
	 * reference to the newly created Activity after each configuration change.
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	/**
	 * This method will only be called once when the retained Fragment is first
	 * created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Retain this fragment across configuration changes.
		setRetainInstance(true);
	}

	/**
	 * Set the callback to null so we don't accidentally leak the Activity
	 * instance.
	 */
	@Override
	public void onDetach() {
		super.onDetach();
	}
}
