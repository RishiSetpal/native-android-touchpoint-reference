package com.optimusinfo.elasticpath.cortex;

import com.optimusinfo.elasticpath.cortex.authentication.Authentication;
import com.optimusinfo.elasticpath.cortex.authentication.ListenerAsyncTaskAuthentication;
import com.optimusinfo.elasticpath.cortex.category.CategoryFragment;
import com.optimusinfo.elasticpath.cortex.common.Constants;
import com.optimusinfo.elasticpath.cortex.common.NotificationUtils;
import com.optimusinfo.elasticpath.cortex.configuration.EPConfiguration;
import com.optimusinfo.elasticpath.cortex.configuration.EPCortex;
import com.optimusinfo.elasticpath.cortexAPI.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.Window;

/**
 * This is main application activity in which all the fragments are added. Also
 * authentication occurs from this activity.
 * 
 * @author Optimus
 * 
 */
public class EPHomeActivity extends FragmentActivity {

	protected SharedPreferences mObjPreferences;
	protected ListenerAsyncTaskAuthentication mAuthListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_main);

		// Disable the title
		getActionBar().setDisplayShowTitleEnabled(false);
		mObjPreferences = getSharedPreferences(
				Constants.Preferences.PREFERENCES_FILE_NAME,
				Context.MODE_PRIVATE);
		mObjPreferences.edit().remove(Constants.Preferences.LIST_CART).commit();
		mObjPreferences.edit()
				.putString(Constants.Preferences.KEY_ACCESS_TOKEN_USER, "")
				.commit();

		// Get authentication token and save
		getAuthenticationToken();

	}

	/**
	 * This function creates an asynchronous task to get the public
	 * authentication token
	 */
	public void getAuthenticationToken() {

		EPCortex objCortexParams = EPConfiguration.getConfigurationParameters(
				getApplicationContext(), Constants.Config.FILE_NAME_CONFIG);

		mAuthListener = new ListenerAsyncTaskAuthentication() {

			@Override
			public void onTaskComplete(
					final Authentication authenticationResponse) {
				// Save the authentication token to application preferences
				mObjPreferences
						.edit()
						.putString(Constants.Preferences.KEY_ACCESS_TOKEN,
								authenticationResponse.getAcessToken())
						.commit();

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						setProgressBarIndeterminateVisibility(false);
						// Add the category fragment
						CategoryFragment fragmentNavigation = new CategoryFragment();
						getFragmentManager()
								.beginTransaction()
								.add(R.id.fragment_container,
										fragmentNavigation).commit();
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
				objCortexParams.getEndpoint(), mAuthListener, false, null,
				null, objCortexParams.getScope(), objCortexParams.getRole());

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return false;
	}

	public SharedPreferences getObjPreferences() {
		return mObjPreferences;
	}

	public void setObjPreferences(SharedPreferences mObjPreferences) {
		this.mObjPreferences = mObjPreferences;
	}

}
