/*
 * Copyright © 2014 Elastic Path Software Inc. All rights reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * 
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.optimusinfo.elasticpath.cortex.common;

import android.app.ActionBar;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.optimusinfo.elasticpath.cortex.authentication.AuthenticationActivity;
import com.optimusinfo.elasticpath.cortex.cart.CartFragment;
import com.optimusinfo.elasticpath.cortex.checkout.CheckoutFragment;
import com.optimusinfo.elasticpath.cortex.configuration.EPConfiguration;
import com.optimusinfo.elasticpath.cortex.configuration.EPCortex;
import com.optimusinfo.elasticpath.cortex.profile.ProfileFragment;
import com.optimusinfo.elasticpath.cortex.profile.logout.LogoutFragment;
import com.optimusinfo.elasticpath.cortex.profile.logout.LogoutFragment.LogoutDialogListener;
import com.optimusinfo.elasticpath.cortex.purchase.PurchaseFragment;
import com.optimusinfo.elasticpath.cortexAPI.R;

public abstract class EPFragmentActivity extends FragmentActivity implements
		LogoutDialogListener {

	protected SharedPreferences mObjPreferences;
	protected EPCortex mObjCortexParams;
	protected ActionBar mObjActionBar;

	protected ProgressDialog mProgressDialog;

	public static int REQUEST_CODE_AUTHENTICATION = 27;
	public static int RESULT_CODE_AUTHENTICATION_SUCESSFUL = 28;

	public static int REQUEST_CODE_PROFILE = 32;
	public static int RESULT_CODE_PROFILE_RESULT = 33;

	public static int REQUEST_CODE_ADDRESS = 42;
	public static int RESULT_CODE_ADDRESS_RESULT = 43;

	public static int REQUEST_CODE_CHECKOUT = 52;
	public static int RESULT_CODE_CHECKOUT_COMPLETE = 53;

	public static int REQUEST_CODE_PURCHASE = 62;
	public static int RESULT_CODE_PURCHASE_COMPLETE = 63;

	public static String DIALOG_STATE_PROGRESS = "progress";

	protected int mStackLevel = 0;
	protected boolean mProgressState = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Enable the flag for showing loader at the top right corner
		if (mProgressDialog == null) {
			mProgressDialog = new ProgressDialog(this);
			mProgressDialog.setMessage(getString(R.string.messageLoading));
			mProgressDialog.setCancelable(false);
			mProgressDialog.setCanceledOnTouchOutside(false);
		}

		if (savedInstanceState != null) {
			mStackLevel = savedInstanceState.getInt("level");
			mProgressState = savedInstanceState
					.getBoolean(DIALOG_STATE_PROGRESS);
		}

		showProgressDialog(mProgressState);

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
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		if (isUserLoggedIn()) {
			inflater.inflate(R.menu.activity_main_actions_logged_in, menu);
		} else {
			inflater.inflate(R.menu.activity_main_actions_logged_out, menu);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// Incase user presses back button from top navigation
			if (getFragmentManager().getBackStackEntryCount() > 1) {
				onBackPressed();
			}
			break;
		case R.id.action_cart:
			if (getCurrentFragment() instanceof CartFragment) {

			} else if (getCurrentFragment() instanceof CheckoutFragment) {
				onBackPressed();
			} else {
				getCurrentFragment().detachChildFragments();
				// Start the product details activity
				CartFragment mObjFragment = new CartFragment(null, null);
				addFragmentToBreadCrumb("Cart", R.id.fragment_container,
						mObjFragment);
			}
			break;
		case R.id.action_login:
			getCurrentFragment().detachChildFragments();
			Intent mAuthIntent = new Intent(this, AuthenticationActivity.class);
			startActivityForResult(mAuthIntent, REQUEST_CODE_AUTHENTICATION);
			break;
		case R.id.action_logout:
			// Create an instance of the dialog fragment and show it
			DialogFragment dialog = new LogoutFragment();
			dialog.show(getFragmentManager(), "Logout");
			break;
		case R.id.action_refresh:
			onRefreshData();
			break;
		case R.id.action_profile:
			if (getCurrentFragment() instanceof ProfileFragment) {

			} else {
				getCurrentFragment().detachChildFragments();
				ProfileFragment objFragment = new ProfileFragment();
				addFragmentToBreadCrumb("Profile", R.id.fragment_container,
						objFragment);
			}
			break;
		}
		return true;
	}

	public EPFragment getCurrentFragment() {
		EPFragment mCurrentFrgament = (EPFragment) getFragmentManager()
				.findFragmentById(R.id.fragment_container);
		return mCurrentFrgament;
	}

	/**
	 * This is a skeleton method which refreshes user data.
	 */
	public void onRefreshData() {

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
	 * This method returns the current user name
	 * 
	 * @return
	 */
	public final String getUserName() {
		String userName = mObjPreferences.getString(
				Constants.Preferences.KEY_USERNAME, "");
		return userName;
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
	 * Return the string
	 * 
	 * @param sResId
	 *            - String Resource ID
	 * @return
	 */
	public final String getStringFromResource(int sResId) {
		return getString(sResId);
	}

	/**
	 * This method adds a fragment to the current activity
	 * 
	 * @param title
	 * @param isNew
	 * @param tag
	 * @param fargmentContainerId
	 * @param objFragment
	 */
	public void addFragmentToBreadCrumb(String title, int fargmentContainerId,
			EPFragment objFragment) {
		String breadTitle = title;
		if (title.length() > 16) {
			breadTitle = title.substring(0, 14).concat(
					getString(R.string.ellipsis));
		}
		mStackLevel++;
		// Add the fragment to the activity, pushing this transaction
		// on to the back stack.
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.setBreadCrumbTitle(breadTitle);
		ft.replace(fargmentContainerId, objFragment);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		ft.addToBackStack(null);
		ft.commit();

	}

	protected void addFragment(String title, int fargmentContainerId,
			EPFragment objFragment) {
		mStackLevel++;
		// Add the fragment to the activity, pushing this transaction
		// on to the back stack.
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.setBreadCrumbTitle(title);
		ft.add(fargmentContainerId, objFragment);
		ft.commit();
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
		super.onSaveInstanceState(outState);
		outState.putInt("level", mStackLevel);
		outState.putBoolean(DIALOG_STATE_PROGRESS, mProgressDialog.isShowing());
		showProgressDialog(false);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		if (requestCode == REQUEST_CODE_AUTHENTICATION
				&& resultCode == RESULT_CODE_AUTHENTICATION_SUCESSFUL) {
			this.invalidateOptionsMenu();
			getCurrentFragment().onAuthenticationSucessful();
		} else if (requestCode == REQUEST_CODE_ADDRESS) {
			getCurrentFragment().onRefreshData();
		}
	}

	/** Logout Listener */

	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
		mObjPreferences.edit()
				.remove(Constants.Preferences.KEY_ACCESS_TOKEN_REGISTERED)
				.commit();
		Intent resIntent = getBaseContext().getPackageManager()
				.getLaunchIntentForPackage(getBaseContext().getPackageName());
		resIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(resIntent);
	}

	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		// TODO - For Future Requirements
	}

	public void clearTop() {
		Intent resIntent = getBaseContext().getPackageManager()
				.getLaunchIntentForPackage(getBaseContext().getPackageName());
		resIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(resIntent);
	}

	public SharedPreferences getObjPreferences() {
		return mObjPreferences;
	}

	public EPCortex getObjCortexParams() {
		return mObjCortexParams;
	}

	@Override
	public void onBackPressed() {
		int count = getFragmentManager().getBackStackEntryCount();
		if (getCurrentFragment() instanceof PurchaseFragment) {
			getCurrentFragment().onBackPressed();
		} else {
			if (count > 1) {
				getCurrentFragment().detachChildFragments();
				getFragmentManager().popBackStack();
			} else {
				finish();
			}
		}
	}

	public void showProgressDialog(boolean isVisible) {
		if (isVisible) {
			mProgressDialog.show();
		} else {
			if (mProgressDialog.isShowing()) {
				mProgressDialog.dismiss();
			}
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

}
