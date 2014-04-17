package com.optimusinfo.elasticpath.cortex.profile.address;

import org.json.JSONException;
import org.json.JSONObject;

import com.optimusinfo.elasticpath.cortexAPI.R;
import com.optimusinfo.elasticpath.cortex.common.Constants;
import com.optimusinfo.elasticpath.cortex.common.EPFragmentActivity;
import com.optimusinfo.elasticpath.cortex.profile.ProfileModel.AddressElement;
import com.optimusinfo.elasticpath.cortex.profile.address.AddressModel.CreateAddressFormModel;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class AddressActivity extends EPFragmentActivity {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments representing each object in a collection. We use a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter} derivative,
	 * which will destroy and re-create fragments as needed, saving and
	 * restoring their state in the process. This is important to conserve
	 * memory and is a best practice when allowing navigation between objects in
	 * a potentially large collection.
	 */
	protected LinearLayout mLayout;
	protected AddressElement mObjAddress;

	protected String mAddressPostUrl;

	protected ListenerUpdateAdddress mListenerUpdate;
	protected ListenerGetAddressForm mListenerGetForm;
	protected ListenerAddAddress mListenerAddAddress;

	protected EditText mETFirstName, mETLastName, mETStreetAddress,
			mETExtendedAddress, mETCity, mETCountry, mETProvince,
			mETPostalCode;

	protected boolean mIsOrderConfirmed = false;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set the content view
		setContentView(R.layout.activity_address);
		// Initialize the params objects
		super.initializeParams();
		// Disable the title
		mObjActionBar.setDisplayShowTitleEnabled(false);

		mObjAddress = (AddressElement) getIntent().getSerializableExtra(
				Constants.PageUrl.INTENT_ADRESS);
		// Initialize views
		initializeViews();
		if (mObjAddress != null) {
			bindContent();
		} else {
			getAddressForm();
		}
		setProgressBarIndeterminateVisibility(false);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * This method initializes the page view elements
	 */
	private void initializeViews() {
		mLayout = (LinearLayout) findViewById(R.id.lHeader);
		mLayout.setVisibility(View.VISIBLE);

		mETFirstName = (EditText) findViewById(R.id.etFirstName);
		mETLastName = (EditText) findViewById(R.id.etLastName);
		mETStreetAddress = (EditText) findViewById(R.id.etStreetAddress);
		mETExtendedAddress = (EditText) findViewById(R.id.etExtendedAddress);
		mETCity = (EditText) findViewById(R.id.etCity);
		mETCountry = (EditText) findViewById(R.id.etCountry);
		mETProvince = (EditText) findViewById(R.id.etProvince);
		mETPostalCode = (EditText) findViewById(R.id.etPostalCode);

		Button mBtCancel = (Button) findViewById(R.id.btCancel);
		mBtCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onCancel();
			}
		});

		Button mBtSave = (Button) findViewById(R.id.btSave);
		mBtSave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mObjAddress != null) {
					onEditSave();
				} else {
					onNewSave();
				}
			}
		});
	}

	public void bindContent() {
		mETFirstName.setText(mObjAddress.mAddressName.mGivenName);
		mETLastName.setText(mObjAddress.mAddressName.mFamilyName);
		mETStreetAddress.setText(mObjAddress.mAddressDesc.mStreetAddress);

		if (mObjAddress.mAddressDesc.mExtendedAddress != null) {
			mETExtendedAddress
					.setText(mObjAddress.mAddressDesc.mExtendedAddress);
		}
		mETCity.setText(mObjAddress.mAddressDesc.mLocality);

		mETCountry.setText(mObjAddress.mAddressDesc.mCountryName);

		mETProvince.setText(mObjAddress.mAddressDesc.mRegion);

		mETPostalCode.setText(mObjAddress.mAddressDesc.mPostalCode);
	}

	public void onEditSave() {
		mListenerUpdate = new ListenerUpdateAdddress() {

			@Override
			public void onTaskSuccessful(final int response) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						setProgressBarIndeterminateVisibility(false);
						if (response != 400) {
							setResult(
									EPFragmentActivity.RESULT_CODE_ADDRESS_RESULT,
									getIntent());
							finish();
						}
					}
				});
			}

			@Override
			public void onTaskFailed(int errorCode) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						setProgressBarIndeterminateVisibility(false);
						// TODO For Future Req
					}
				});

			}

			@Override
			public void onAuthenticationFailed() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						setProgressBarIndeterminateVisibility(false);
						// TODO For Future Req
					}
				});

			}
		};

		setProgressBarIndeterminateVisibility(true);
		AddressModel.updateAddress(getApplicationContext(),
				mObjAddress.mSelfLinks.mHREF, getUserAuthenticationToken(),
				mListenerUpdate, getAddressModelJSON());
	}

	public void onNewSave() {
		mListenerAddAddress = new ListenerAddAddress() {

			@Override
			public void onTaskSuccessful(final int response) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						setProgressBarIndeterminateVisibility(false);
						if (response != 400) {
							setResult(
									EPFragmentActivity.RESULT_CODE_ADDRESS_RESULT,
									getIntent());
							finish();
						}
					}
				});
			}

			@Override
			public void onTaskFailed(int errorCode) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						setProgressBarIndeterminateVisibility(false);
						// TODO For Future Req
					}
				});

			}

			@Override
			public void onAuthenticationFailed() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						setProgressBarIndeterminateVisibility(false);
						// TODO For Future Req
					}
				});

			}
		};
		setProgressBarIndeterminateVisibility(true);
		AddressModel.addAddress(mAddressPostUrl,
				Constants.Config.CONTENT_TYPE_ADD_TO_CART,
				getUserAuthenticationToken(), mListenerAddAddress,
				getAddressModelJSON());
	}

	public void getAddressForm() {
		mListenerGetForm = new ListenerGetAddressForm() {

			@Override
			public void onTaskSuccessful(CreateAddressFormModel response) {
				mAddressPostUrl = AddressModel
						.getCreateAddressLink(response.mAddresses[0].mElements[0].mLinks);
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						setProgressBarIndeterminateVisibility(false);
						// TODO For Future Req
					}
				});

			}

			@Override
			public void onTaskFailed(int errorCode) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						setProgressBarIndeterminateVisibility(false);
						// TODO For Future Req
					}
				});

			}

			@Override
			public void onAuthenticationFailed() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						setProgressBarIndeterminateVisibility(false);
						// TODO For Future Req
					}
				});

			}
		};
		setProgressBarIndeterminateVisibility(true);
		AddressModel.getAddressForm(getApplicationContext(), getAddressUrl(),
				getUserAuthenticationToken(), mListenerGetForm);

	}

	public void onCancel() {
		onBackPressed();
	}

	@Override
	public void onBackPressed() {
		// Incase user presses back button from top navigation
		setResult(0, getIntent());
		finish();// finishing activity
	}

	@Override
	public void onRefreshData() {
		// TODO Auto-generated method stub
		super.onRefreshData();
	}

	public JSONObject getAddressModelJSON() {
		JSONObject addressObject = new JSONObject();
		JSONObject descObject = new JSONObject();
		JSONObject nameObject = new JSONObject();
		try {
			descObject.put("street-address", mETStreetAddress.getText()
					.toString());
			descObject.put("extended-address", mETExtendedAddress.getText()
					.toString());
			descObject.put("locality", mETCity.getText().toString());
			descObject.put("region", mETProvince.getText().toString());
			descObject.put("country-name", mETCountry.getText().toString());
			descObject.put("postal-code", mETPostalCode.getText().toString());
			addressObject.put("address", descObject);

			nameObject.put("given-name", mETFirstName.getText().toString());
			nameObject.put("family-name", mETLastName.getText().toString());
			addressObject.put("name", nameObject);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return addressObject;
	}

	public String getAddressUrl() {
		String scope = mObjCortexParams.getScope();
		String url = mObjCortexParams.getEndpoint().concat("profiles/")
				.concat(scope).concat(Constants.ZoomUrl.URL_ZOOM_ADDRESS_FORM);

		return url;
	}

}