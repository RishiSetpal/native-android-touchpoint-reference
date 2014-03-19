package com.optimusinfo.elasticpath.cortex.common;

import com.optimusinfo.elasticpath.cortex.configuration.EPConfiguration;
import com.optimusinfo.elasticpath.cortex.configuration.EPCortex;

import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.Window;

public abstract class EPFragmentActivity extends FragmentActivity {

	protected SharedPreferences mObjPreferences;
	protected EPCortex mObjCortexParams;
	protected ActionBar mObjActionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// Enable the flag for showing loader at the top right corner
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

	}

	protected void initializeParams() {
		// Initialize the action bar drawable
		mObjActionBar = getActionBar();
		// Disable the title
		mObjActionBar.setDisplayShowTitleEnabled(false);
		// Specify that the Home button should show an "Up" caret, indicating
		// that touching the
		// button will take the user one step up in the application's hierarchy.
		mObjActionBar.setDisplayHomeAsUpEnabled(true);
		// Initialize the
		mObjPreferences = getSharedPreferences(
				Constants.Preferences.PREFERENCES_FILE_NAME,
				Context.MODE_PRIVATE);
		mObjCortexParams = EPConfiguration.getConfigurationParameters(
				getApplicationContext(), Constants.Config.FILE_NAME_CONFIG);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// Incase user presses back button from top navigation
			super.onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * This method returns the authentication token
	 * 
	 * @return
	 */
	public final String getUserAuthenticationToken() {
		String accessToken = mObjPreferences.getString(
				Constants.Preferences.KEY_ACCESS_TOKEN_REGISTERED, "");
		if (TextUtils.isEmpty(accessToken)) {
			accessToken = mObjPreferences.getString(
					Constants.Preferences.KEY_ACCESS_TOKEN_PUBLIC, "");
		}
		return accessToken;
	}

	/**
	 * This method tells whether user is logged in or not
	 * 
	 * @return
	 */
	public final boolean isUserLoggedIn() {
		String accessToken = mObjPreferences.getString(
				Constants.Preferences.KEY_ACCESS_TOKEN_REGISTERED, "");
		if (TextUtils.isEmpty(accessToken)) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * This method adds a fragment to the current activity
	 * 
	 * @param fargmentContainerId
	 * @param objFragment
	 */
	protected void addFragment(int fargmentContainerId, EPFragment objFragment) {
		getFragmentManager().beginTransaction()
				.add(fargmentContainerId, objFragment).commit();
	}

	/*
	 * This method is called when orientation is changed (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.FragmentActivity#onConfigurationChanged(android
	 * .content.res.Configuration)
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

	public SharedPreferences getObjPreferences() {
		return mObjPreferences;
	}

	public EPCortex getObjCortexParams() {
		return mObjCortexParams;
	}

}
