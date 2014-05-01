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
package com.optimusinfo.elasticpath.cortex.profile.address;

import org.json.JSONException;
import org.json.JSONObject;

import com.optimusinfo.elasticpath.cortexAPI.R;
import com.optimusinfo.elasticpath.cortex.common.Constants;
import com.optimusinfo.elasticpath.cortex.common.EPFragmentActivity;
import com.optimusinfo.elasticpath.cortex.common.NotificationUtils;
import com.optimusinfo.elasticpath.cortex.profile.ProfileModel.AddressElement;
import com.optimusinfo.elasticpath.cortex.profile.address.AddressModel.CreateAddressFormModel;
import com.optimusinfo.elasticpath.cortex.profile.address.GeographiesModel.Geographies;
import com.optimusinfo.elasticpath.cortex.profile.address.GeographiesModel.GeographyElement;
import com.optimusinfo.elasticpath.cortex.profile.address.GeographiesModel.RegionElement;
import com.optimusinfo.elasticpath.cortex.profile.address.GeographiesModel.Regions;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

public class AddressActivity extends EPFragmentActivity {

	public static final String KEY_REGIONS = "REGIONS";
	public static final String KEY_GEOGRAPHIES = "GEOGRAPHIES";

	public static final String KEY_POSITION_COUNTRIES = "POSTION_COUNTRIES";
	public static final String KEY_POSITION_REGIONS = "POSTION_REGIONS";

	public static final String KEY_ADDRESS_POST = "ADDRESS_POST_URL";
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

	protected ListenerGetGeographies mListenerGetGeographies;
	protected ListenerGetGeographies mListenerGetRegions;

	protected EditText mETFirstName, mETLastName, mETStreetAddress,
			mETExtendedAddress, mETCity, mETPostalCode;
	protected Spinner mSPCountry, mSPProvince;

	protected boolean mIsOrderConfirmed = false;

	protected Geographies mGeographies;
	protected Regions mRegions;

	public int posCountry = 0;
	public int posRegion = 0;

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

		if (savedInstanceState != null) {
			mGeographies = (Geographies) savedInstanceState
					.getSerializable(KEY_GEOGRAPHIES);
			mRegions = (Regions) savedInstanceState
					.getSerializable(KEY_REGIONS);			
			posCountry = savedInstanceState.getInt(KEY_POSITION_COUNTRIES);
			posRegion = savedInstanceState.getInt(KEY_POSITION_REGIONS);
			mAddressPostUrl = savedInstanceState.getString(KEY_ADDRESS_POST);

		}

		if (mGeographies == null) {
			// Populate the geographies views
			getGeographies();
		} else {
			setUpCountries(mGeographies);
			setUpRegions(mRegions);
		}

		if (mObjAddress != null) {
			bindContent();
		}
		if (mAddressPostUrl == null) {
			getAddressForm();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
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
		mSPCountry = (Spinner) findViewById(R.id.spCountry);
		mSPProvince = (Spinner) findViewById(R.id.spProvince);
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
		mETPostalCode.setText(mObjAddress.mAddressDesc.mPostalCode);
	}

	public void getGeographies() {
		mListenerGetGeographies = new ListenerGetGeographies() {
			@Override
			public void onTaskSuccessful(final Object response) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						showProgressDialog(false);
						setUpCountries((Geographies) response);
					}
				});
			}

			@Override
			public void onTaskFailed(int errorCode) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						showProgressDialog(false);
					}
				});
			}
		};
		showProgressDialog(true);
		GeographiesModel.getGeographies(getApplicationContext(),
				getGeographiesUrl(), getUserAuthenticationToken(),
				mListenerGetGeographies);

	}

	public void getRegions(String url) {
		mListenerGetRegions = new ListenerGetGeographies() {
			@Override
			public void onTaskSuccessful(final Object response) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						showProgressDialog(false);
						setUpRegions((Regions) response);
					}
				});
			}

			@Override
			public void onTaskFailed(int errorCode) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						showProgressDialog(false);
					}
				});
			}
		};
		showProgressDialog(true);
		GeographiesModel.getRegions(getApplicationContext(), url,
				getUserAuthenticationToken(), mListenerGetRegions);

	}

	/**
	 * Set up countries
	 * 
	 * @param listObj
	 */
	public void setUpCountries(Geographies listObj) {
		mGeographies = listObj;
		CountrySpinnerAdapter mAdapter = new CountrySpinnerAdapter(
				getApplicationContext(), android.R.layout.simple_spinner_item,
				mGeographies.mElement);
		mSPCountry.setAdapter(mAdapter);
		if (mObjAddress != null && posCountry == 0) {
			String countryName = mObjAddress.mAddressDesc.mCountryName;
			posCountry = GeographiesModel.getCountriesPosition(countryName,
					mGeographies.mElement);
		}

		if (posCountry != 0) {
			mSPCountry.setSelection(posCountry);
		}

		mSPCountry.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parentView,
					View selectedItemView, int position, long id) {
				String regionsUrl = GeographiesModel.getRegionsUrl(
						((GeographyElement) mSPCountry
								.getItemAtPosition(position))).concat(
						Constants.ZoomUrl.URL_ZOOM_ELEMENT);
				if (regionsUrl != null) {
					getRegions(regionsUrl);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO
			}
		});
	}

	/**
	 * Set up regions
	 * 
	 * @param listObj
	 */
	public void setUpRegions(Regions listObj) {
		mRegions = listObj;
		RegionSpinnerAdapter mAdapter = new RegionSpinnerAdapter(
				getApplicationContext(), android.R.layout.simple_spinner_item,
				mRegions.mElement);
		mSPProvince.setAdapter(mAdapter);
		if (mRegions.mElement.length > 0) {
			if (mObjAddress != null && posRegion == 0) {
				String regionName = mObjAddress.mAddressDesc.mRegion;
				if (regionName != null) {
					posRegion = GeographiesModel
							.getRegionsPosition(
									mObjAddress.mAddressDesc.mRegion,
									mRegions.mElement);
				}
			}
		} else {
			mSPProvince.setEnabled(false);
		}

		if (true == mSPProvince.isEnabled() && posRegion != 0) {
			mSPProvince.setSelection(posRegion);
		}
	}

	public void onEditSave() {
		mListenerUpdate = new ListenerUpdateAdddress() {

			@Override
			public void onTaskSuccessful(final int response) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						showProgressDialog(false);
						if (response != 400) {
							setResult(
									EPFragmentActivity.RESULT_CODE_ADDRESS_RESULT,
									getIntent());
							finish();
						} else {
							NotificationUtils.showNotificationToast(
									getApplicationContext(), "Error");
						}
					}
				});
			}

			@Override
			public void onTaskFailed(int errorCode) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						showProgressDialog(false);
						// TODO For Future Req
						NotificationUtils.showNotificationToast(
								getApplicationContext(), "Error");
					}
				});

			}

			@Override
			public void onAuthenticationFailed() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						showProgressDialog(false);
						// TODO For Future Req
						NotificationUtils.showNotificationToast(
								getApplicationContext(), "Error");
					}
				});

			}
		};

		showProgressDialog(true);
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
						showProgressDialog(false);
						if (response != 400) {
							setResult(
									EPFragmentActivity.RESULT_CODE_ADDRESS_RESULT,
									getIntent());
							finish();
						} else {
							NotificationUtils.showNotificationToast(
									getApplicationContext(), "Error");
						}
					}
				});
			}

			@Override
			public void onTaskFailed(int errorCode) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						showProgressDialog(false);
						// TODO For Future Req
						NotificationUtils.showNotificationToast(
								getApplicationContext(), "Error");
					}
				});

			}

			@Override
			public void onAuthenticationFailed() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						showProgressDialog(false);
						// TODO For Future Req
						NotificationUtils.showNotificationToast(
								getApplicationContext(), "Error");
					}
				});

			}
		};
		showProgressDialog(true);
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
						showProgressDialog(false);
					}
				});

			}

			@Override
			public void onTaskFailed(int errorCode) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						showProgressDialog(false);
					}
				});

			}

			@Override
			public void onAuthenticationFailed() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						showProgressDialog(false);
						// TODO For Future Req
					}
				});

			}
		};
		showProgressDialog(true);
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
		super.onRefreshData();
	}

	public String getAddressUrl() {
		String scope = mObjCortexParams.getScope();
		String url = mObjCortexParams.getEndpoint().concat("profiles/")
				.concat(scope).concat(Constants.ZoomUrl.URL_ZOOM_ADDRESS_FORM);
		return url;
	}

	/**
	 * This method returns the address url
	 * 
	 * @return
	 */
	public String getGeographiesUrl() {
		String scope = mObjCortexParams.getScope();
		String url = mObjCortexParams.getEndpoint().concat("geographies/")
				.concat(scope)
				.concat(Constants.ZoomUrl.URL_ZOOM_GEOGRAPHY_ELEMENT);
		return url;
	}

	/**
	 * This method returns the address model json
	 * 
	 * @return
	 */
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
			if (mSPProvince.getCount() > 0) {
				String regionName = ((RegionElement) mSPProvince
						.getSelectedItem()).mValue;
				descObject.put("region", regionName);
			} else {
				descObject.put("region", " ");
			}

			String countryName = ((GeographyElement) mSPCountry
					.getSelectedItem()).mValue;
			descObject.put("country-name", countryName);
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

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putSerializable(KEY_GEOGRAPHIES, mGeographies);
		outState.putSerializable(KEY_REGIONS, mRegions);
		if (mObjAddress != null) {
			outState.putSerializable(Constants.PageUrl.INTENT_ADRESS,
					mObjAddress);

		}
		outState.putInt(KEY_POSITION_COUNTRIES,
				mSPCountry.getSelectedItemPosition());
		outState.putInt(KEY_POSITION_REGIONS,
				mSPProvince.getSelectedItemPosition());
		outState.putString(KEY_ADDRESS_POST, mAddressPostUrl);

	}

}
