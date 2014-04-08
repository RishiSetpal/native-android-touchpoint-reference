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

package com.optimusinfo.elasticpath.cortex;

import com.optimusinfo.elasticpath.cortex.authentication.Authentication;
import com.optimusinfo.elasticpath.cortex.authentication.ListenerAsyncTaskAuthentication;
import com.optimusinfo.elasticpath.cortex.category.CategoryFragment;
import com.optimusinfo.elasticpath.cortex.common.Constants;
import com.optimusinfo.elasticpath.cortex.common.EPFragment;
import com.optimusinfo.elasticpath.cortex.common.EPFragmentActivity;
import com.optimusinfo.elasticpath.cortex.common.NotificationUtils;
import com.optimusinfo.elasticpath.cortexAPI.R;

import android.app.FragmentBreadCrumbs;
import android.os.Bundle;
import android.text.TextUtils;

/**
 * This is main application activity in which all the fragments are added. Also
 * authentication occurs from this activity.
 * 
 * @author Optimus
 * 
 */
public class EPHomeActivity extends EPFragmentActivity {

	protected ListenerAsyncTaskAuthentication mAuthListener;
	protected FragmentBreadCrumbs mObjBreadCrumbs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set the activity layout
		setContentView(R.layout.activity_main);
		// Initialize the param objects
		super.initializeParams();
		// Disable the title
		mObjActionBar.setDisplayHomeAsUpEnabled(false);

		mObjBreadCrumbs = (FragmentBreadCrumbs) findViewById(R.id.breadcrumbs);
		mObjBreadCrumbs.setActivity(this);
		mObjBreadCrumbs.setTitle(
				getStringFromResource(R.string.breadcrumb_title_home), null);

		if (savedInstanceState == null) {
			if (TextUtils.isEmpty(getUserAuthenticationToken())) {
				// Get authentication token and save
				getNewAuthenticationTokenFromAPI();
			} else {
				CategoryFragment mObjFragment = new CategoryFragment();
				addFragment("Category", R.id.fragment_container, mObjFragment);
			}
		}
	}

	/**
	 * This function creates an asynchronous task to get the public
	 * authentication token
	 */
	private void getNewAuthenticationTokenFromAPI() {

		mAuthListener = new ListenerAsyncTaskAuthentication() {
			@Override
			public void onTaskComplete(
					final Authentication authenticationResponse) {
				// Save the authentication token to application preferences
				mObjPreferences
						.edit()
						.putString(
								Constants.Preferences.KEY_ACCESS_TOKEN_PUBLIC,
								authenticationResponse.getAcessToken())
						.commit();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						setProgressBarIndeterminateVisibility(false);
						CategoryFragment mObjFragment = new CategoryFragment();
						addFragment("Category", R.id.fragment_container,
								mObjFragment);

					}
				});
			}

			@Override
			public void onTaskFailed(final int errorCode, final String response) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						setProgressBarIndeterminateVisibility(false);
						NotificationUtils.showErrorToast(
								getApplicationContext(), errorCode);
					}
				});
			}
		};

		setProgressBarIndeterminateVisibility(true);
		// get the user authentication and save it to shared preferences
		Authentication.getTokenFromServer(getApplicationContext(),
				mObjCortexParams.getEndpoint(), mAuthListener, null, null,
				mObjCortexParams.getScope(), mObjCortexParams.getRole());

	}

	@Override
	public void onRefreshData() {
		super.onRefreshData();
		EPFragment mObjMainFragment = getCurrentFragment();
		if (mObjMainFragment != null) {
			mObjMainFragment.onRefreshData();
		} else {
			getNewAuthenticationTokenFromAPI();
		}
	}
}
