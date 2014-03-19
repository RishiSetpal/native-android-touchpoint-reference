package com.optimusinfo.elasticpath.cortex.authentication;

import com.optimusinfo.elasticpath.cortex.cart.CartActivity;
import com.optimusinfo.elasticpath.cortex.common.Constants;
import com.optimusinfo.elasticpath.cortex.common.NotificationUtils;
import com.optimusinfo.elasticpath.cortex.configuration.EPConfiguration;
import com.optimusinfo.elasticpath.cortex.configuration.EPCortex;
import com.optimusinfo.elasticpath.cortexAPI.R;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * This is the fragment to be opened when authentication is to be done
 * 
 * @author Optimus
 * 
 */
public class AuthenticationActivity extends FragmentActivity {

	protected ListenerAsyncTaskAuthentication mAuthListener;

	EditText tvUsername, tvPassword;

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments representing each object in a collection. We use a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter} derivative,
	 * which will destroy and re-create fragments as needed, saving and
	 * restoring their state in the process. This is important to conserve
	 * memory and is a best practice when allowing navigation between objects in
	 * a potentially large collection.
	 */
	protected String mAccessToken;
	protected SharedPreferences mPreferences;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		setContentView(R.layout.fragment_authentication);

		// Initialize Preferences
		mPreferences = getSharedPreferences(
				Constants.Preferences.PREFERENCES_FILE_NAME,
				Context.MODE_PRIVATE);

		// Set up action bar.
		final ActionBar actionBar = getActionBar();
		// Specify that the Home button should show an "Up" caret, indicating
		// that touching the
		// button will take the user one step up in the application's hierarchy.
		actionBar.setDisplayHomeAsUpEnabled(true);
		// Perform the add to cart method
		initializeViews();

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// Incase user presses back button from top navigation
			Intent intent = new Intent(AuthenticationActivity.this,
					CartActivity.class);
			setResult(0, intent);
			finish();// finishing activity
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * This function updates the views with data
	 */
	private void initializeViews() {

		tvUsername = (EditText) findViewById(R.id.etUsername);
		tvPassword = (EditText) findViewById(R.id.etPassword);

		Button btPurchase = (Button) findViewById(R.id.btLogin);
		btPurchase.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (tvUsername.getText().toString().length() != 0
						&& tvPassword.getText().toString().length() != 0) {
					getAuthenticationToken();
				} else {
					NotificationUtils.showNotificationToast(
							getApplicationContext(),
							"Please enter a username and password.");
				}
			}
		});
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
				mPreferences
						.edit()
						.putString(Constants.Preferences.KEY_ACCESS_TOKEN_PUBLIC,
								authenticationResponse.getAcessToken())
						.commit();
				mPreferences
						.edit()
						.putString(Constants.Preferences.KEY_ACCESS_TOKEN_REGISTERED,
								authenticationResponse.getAcessToken())
						.commit();

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						setProgressBarIndeterminateVisibility(false);
						Intent intent = new Intent(AuthenticationActivity.this,
								CartActivity.class);
						intent.putExtra(
								Constants.Preferences.KEY_ACCESS_TOKEN_REGISTERED,
								authenticationResponse.getAcessToken());
						setResult(
								CartActivity.RESULT_CODE_AUTHENTICATION_SUCESSFUL,
								intent);
						finish();// finishing activity
					}
				});
			}

			@Override
			public void onTaskFailed(final int errorCode) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						setProgressBarIndeterminateVisibility(false);
						NotificationUtils.showNotificationToast(
								getApplicationContext(), errorCode + " - "
										+ "Incorrect username or password");
					}
				});
			}
		};

		setProgressBarIndeterminateVisibility(true);
		// get the user authentication and save it to shared preferences
		Authentication.getTokenFromServer(getApplicationContext(),
				objCortexParams.getEndpoint(), mAuthListener, tvUsername
						.getText().toString(), tvPassword.getText().toString(),
				objCortexParams.getScope(), "REGISTERED");

	}

	@Override
	public void onBackPressed() {
		// Incase user presses back button from top navigation
		Intent intent = new Intent(AuthenticationActivity.this,
				CartActivity.class);
		setResult(0, intent);
		finish();// finishing activity
	}

}
