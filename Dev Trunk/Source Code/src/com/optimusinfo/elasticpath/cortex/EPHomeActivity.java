package com.optimusinfo.elasticpath.cortex;

import com.optimusinfo.elasticpath.cortex.authentication.Authentication;
import com.optimusinfo.elasticpath.cortex.authentication.ListenerAsyncTaskAuthentication;
import com.optimusinfo.elasticpath.cortex.category.CategoryFragment;
import com.optimusinfo.elasticpath.cortex.checkout.CheckoutModel;
import com.optimusinfo.elasticpath.cortex.common.Constants;
import com.optimusinfo.elasticpath.cortex.common.EPFragmentActivity;
import com.optimusinfo.elasticpath.cortex.common.NotificationUtils;
import com.optimusinfo.elasticpath.cortex.configuration.EPTestLocal;
import com.optimusinfo.elasticpath.cortexAPI.R;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;

/**
 * This is main application activity in which all the fragments are added. Also
 * authentication occurs from this activity.
 * 
 * @author Optimus
 * 
 */
public class EPHomeActivity extends EPFragmentActivity {

	protected ListenerAsyncTaskAuthentication mAuthListener;
	protected CategoryFragment mObjFragmentCategory;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set the activity layout
		setContentView(R.layout.activity_main);
		// Initialize the param objects
		super.initializeParams();
		// Disable the title
		mObjActionBar.setDisplayShowTitleEnabled(false);
		mObjActionBar.setDisplayHomeAsUpEnabled(false);

		if (savedInstanceState == null) {
			if (TextUtils.isEmpty(getUserAuthenticationToken())) {
				// Get authentication token and save
				getNewAuthenticationTokenFromAPI();
			} else {
				mObjFragmentCategory = new CategoryFragment();
				addFragment(R.id.fragment_container, mObjFragmentCategory);
			}
		} else {

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
						// Add the category fragment
						mObjFragmentCategory = new CategoryFragment();
						addFragment(R.id.fragment_container,
								mObjFragmentCategory);
					}
				});
			}

			@Override
			public void onTaskFailed(final int errorCode) {
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
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);

		return super.onCreateOptionsMenu(menu);
	}

}
