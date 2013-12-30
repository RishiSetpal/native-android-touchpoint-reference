package com.optimusinfo.elasticpath.cortex.authentication;

import com.optimusinfo.elasticpath.cortex.common.Constants;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * This is the fragment to be opened when authentication is to be done
 * 
 * @author Optimus
 * 
 */
public class AuthenticationFragment extends Fragment {

	SharedPreferences preferences;

	public AuthenticationFragment(Activity currentActivity) {
		preferences = currentActivity.getSharedPreferences(
				Constants.Preferences.PREFERENCES_FILE_NAME,
				Context.MODE_PRIVATE);
	}

	public AuthenticationFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}
}
