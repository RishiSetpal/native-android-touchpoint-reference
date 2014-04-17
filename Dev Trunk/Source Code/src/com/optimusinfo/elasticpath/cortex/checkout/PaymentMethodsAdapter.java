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
import com.optimusinfo.elasticpath.cortex.checkout.CheckoutModel.PaymentMethodInfo.PaymentSelector;
import com.optimusinfo.elasticpath.cortex.checkout.CheckoutModel.PaymentMethodInfo.PaymentSelector.PaymentChoice;
import com.optimusinfo.elasticpath.cortex.common.EPFragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.TextView;

public class PaymentMethodsAdapter extends BaseAdapter {

	private EPFragment mCurrent;
	private ArrayList<PaymentChoice> mSelectorElements;	

	public PaymentMethodsAdapter(EPFragment current, PaymentSelector elements) {
		this.mCurrent = current;
		this.mSelectorElements = new ArrayList<PaymentChoice>();
		if (elements.mChoice != null) {
			for (PaymentChoice choice : elements.mChoice) {
				this.mSelectorElements.add(choice);
			}
		}
		if (elements.mChosen != null) {
			for (PaymentChoice choice : elements.mChosen) {
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
					R.layout.item_list_checkout_payment_desc, null);

			// set value into textview
			TextView textView = (TextView) gridView
					.findViewById(R.id.tvPaymentMethod);

			textView.setText(mSelectorElements.get(position).mDescription[0].mDisplayValue);

			RadioButton rbPaymentMethod = (RadioButton) gridView
					.findViewById(R.id.rbPaymentMethod);

			if (mSelectorElements.get(position).mLinks == null) {
				rbPaymentMethod.setChecked(true);
			} else {
				rbPaymentMethod.setChecked(false);
				rbPaymentMethod
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
