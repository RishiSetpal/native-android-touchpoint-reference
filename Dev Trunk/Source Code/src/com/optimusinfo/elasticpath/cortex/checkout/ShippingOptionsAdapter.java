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
import java.util.Collections;

import com.optimusinfo.elasticpath.cortexAPI.R;
import com.optimusinfo.elasticpath.cortex.checkout.CheckoutModel.Deliveries.ShippingOptionChoice;
import com.optimusinfo.elasticpath.cortex.checkout.CheckoutModel.Deliveries.ShippingOptionSelector;
import com.optimusinfo.elasticpath.cortex.common.EPFragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class ShippingOptionsAdapter extends BaseAdapter {

	private EPFragment mCurrent;
	private ArrayList<ShippingOptionChoice> mSelectorElements;

	public ShippingOptionsAdapter(EPFragment current,
			ShippingOptionSelector elements) {
		this.mCurrent = current;
		this.mSelectorElements = new ArrayList<ShippingOptionChoice>();
		if (elements.mChoice != null) {
			for (ShippingOptionChoice choice : elements.mChoice) {
				this.mSelectorElements.add(choice);
			}
		}
		if (elements.mChosen != null) {
			for (ShippingOptionChoice choice : elements.mChosen) {
				this.mSelectorElements.add(choice);
			}
		}

		Collections.sort(this.mSelectorElements);
	}

	@Override
	public int getCount() {
		return mSelectorElements.size();
	}

	@Override
	public Object getItem(int position) {
		return mSelectorElements.get(position);
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
		if (convertView == null) {

			gridView = new View(mCurrent.getActivity());

			// get layout from mobile.xml
			gridView = inflater.inflate(
					R.layout.item_list_checkout_shipping_option, null);

			// set value into textview
			TextView tvDesc = (TextView) gridView
					.findViewById(R.id.tvShippingOptionDesc);

			tvDesc.setText(mSelectorElements.get(position).mDescriptionElement[0].mDisplayName);

			// set value into textview
			TextView tvCosts = (TextView) gridView
					.findViewById(R.id.tvShippingOptionCost);

			tvCosts.setText(mSelectorElements.get(position).mDescriptionElement[0].mCosts[0].mTotalCost);

			RadioButton rbShippingOption = (RadioButton) gridView
					.findViewById(R.id.rbShippingOption);
			if (mSelectorElements.get(position).mLinks == null) {
				rbShippingOption.setChecked(true);
				((CheckoutFragment) mCurrent.getMainFragment())
						.setShippingAmount(mSelectorElements.get(position).mDescriptionElement[0].mCosts[0].mTotalCost);
			} else {
				rbShippingOption.setChecked(false);
				rbShippingOption
						.setOnCheckedChangeListener(new OnCheckedChangeListener() {

							@Override
							public void onCheckedChanged(CompoundButton arg0,
									boolean arg1) {
								// TODO Auto-generated method stub
								((CheckoutFragment) mCurrent.getMainFragment()).updateSelectOption(CheckoutModel
										.getSelectActionLink(mSelectorElements
												.get(position).mLinks));
							}
						});
			}

		} else {
			gridView = (View) convertView;
		}
		return gridView;
	}

}
