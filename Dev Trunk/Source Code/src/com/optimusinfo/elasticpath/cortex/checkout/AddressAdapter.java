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
package com.optimusinfo.elasticpath.cortex.checkout;

import java.util.ArrayList;
import java.util.List;

import com.optimusinfo.elasticpath.cortexAPI.R;
import com.optimusinfo.elasticpath.cortex.checkout.CheckoutModel.AddressChoice;
import com.optimusinfo.elasticpath.cortex.checkout.CheckoutModel.AddressSelector;
import com.optimusinfo.elasticpath.cortex.common.EPFragment;
import com.optimusinfo.elasticpath.cortex.common.EPFragmentActivity;
import com.optimusinfo.elasticpath.cortex.profile.address.AddressActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.TextView;

public class AddressAdapter extends BaseAdapter {

	private EPFragment mCurrent;

	protected List<AddressChoice> mBillingElements;
	protected int mIndexBillingChosen;

	protected List<AddressChoice> mShippingElements;
	protected int mIndexShippingChosen;

	public AddressAdapter(EPFragment current, AddressSelector bElements,
			AddressSelector sElements) {
		this.mCurrent = current;
		mIndexBillingChosen = -1;
		mIndexShippingChosen = -1;

		this.mBillingElements = new ArrayList<CheckoutModel.AddressChoice>();
		if (bElements != null && bElements.mChoice != null) {
			for (AddressChoice choice : bElements.mChoice) {
				this.mBillingElements.add(choice);
			}
		}
		if (bElements != null && bElements.mChosen != null) {
			for (AddressChoice choice : bElements.mChosen) {
				this.mIndexBillingChosen = this.mBillingElements.size();
				this.mBillingElements.add(choice);
			}
		}

		this.mShippingElements = new ArrayList<CheckoutModel.AddressChoice>();
		if (sElements != null) {
			if (sElements.mChoice != null) {
				for (AddressChoice choice : sElements.mChoice) {
					this.mShippingElements.add(choice);
				}
			}
			if (sElements.mChosen != null) {
				for (AddressChoice choice : sElements.mChosen) {
					this.mIndexShippingChosen = this.mShippingElements.size();
					this.mShippingElements.add(choice);
				}
			}
		}
	}

	@Override
	public int getCount() {
		return mBillingElements.size() + 1;
	}

	@Override
	public Object getItem(int position) {
		return mBillingElements.get(position);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) mCurrent.getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View gridView;
		gridView = new View(mCurrent.getActivity());
		gridView = inflater.inflate(R.layout.item_list_checkout_address_desc,
				null);
		if (position >= mBillingElements.size()) {
			View noAddressLayout = (View) gridView
					.findViewById(R.id.llNoAddress);
			noAddressLayout.setVisibility(View.VISIBLE);
			gridView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(mCurrent.getActivity(),
							AddressActivity.class);
					mCurrent.getActivity().startActivityForResult(intent,
							EPFragmentActivity.REQUEST_CODE_ADDRESS);
				}
			});
		} else {
			View addressLayout = (View) gridView
					.findViewById(R.id.llAddressDesc);
			addressLayout.setVisibility(View.VISIBLE);
			String shipAddress = "";
			if (mIndexShippingChosen != -1) {
				shipAddress = CheckoutModel.getAddressLine(mShippingElements
						.get(mIndexShippingChosen).mDescription[0]);
			}

			String billAddress = CheckoutModel.getAddressLine(mBillingElements
					.get(position).mDescription[0]);

			// set value into textview
			TextView textView = (TextView) gridView
					.findViewById(R.id.tvAddressLine);
			textView.setText(billAddress);

			RadioButton rbBilling = (RadioButton) gridView
					.findViewById(R.id.rbUseForBilling);
			if (mIndexBillingChosen == position) {
				rbBilling.setChecked(true);
			} else {
				rbBilling.setChecked(false);
				rbBilling
						.setOnCheckedChangeListener(new OnCheckedChangeListener() {
							@Override
							public void onCheckedChanged(
									CompoundButton buttonView, boolean isChecked) {
								// TODO Auto-generated method stub
								((CheckoutFragment) mCurrent.getMainFragment()).updateSelectOption(CheckoutModel
										.getSelectActionLink(mBillingElements
												.get(position).mLinks));
							}
						});
			}

			RadioButton rbShipping = (RadioButton) gridView
					.findViewById(R.id.rbUseForShipping);
			if (mShippingElements.size() == 0) {
				rbShipping.setEnabled(false);
			}

			if (shipAddress.equalsIgnoreCase(billAddress)) {
				rbShipping.setChecked(true);
			} else {
				rbShipping.setChecked(false);
				final String link = getShippingLink(billAddress);
				rbShipping
						.setOnCheckedChangeListener(new OnCheckedChangeListener() {
							@Override
							public void onCheckedChanged(
									CompoundButton buttonView, boolean isChecked) {
								// TODO Auto-generated method stub
								((CheckoutFragment) mCurrent.getMainFragment())
										.updateSelectOption(link);
							}
						});
			}
		}

		return gridView;
	}

	public String getShippingLink(String mAddress) {
		for (AddressChoice choice : mShippingElements) {
			String currAdd = CheckoutModel
					.getAddressLine(choice.mDescription[0]);
			if (currAdd.equalsIgnoreCase(mAddress) && choice.mLinks != null) {
				return CheckoutModel.getSelectActionLink(choice.mLinks);
			}
		}
		return "";
	}

}
