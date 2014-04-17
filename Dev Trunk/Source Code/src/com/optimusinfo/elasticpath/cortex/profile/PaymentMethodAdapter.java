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
package com.optimusinfo.elasticpath.cortex.profile;

import com.optimusinfo.elasticpath.cortexAPI.R;
import com.optimusinfo.elasticpath.cortex.profile.ProfileModel.PaymentElement;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PaymentMethodAdapter extends BaseAdapter {

	private Activity mCurrent;

	protected PaymentElement[] mElements;

	public PaymentMethodAdapter(Activity current, PaymentElement[] mElements) {
		this.mCurrent = current;
		this.mElements = mElements;
	}

	@Override
	public int getCount() {
		return mElements.length;
	}

	@Override
	public Object getItem(int position) {
		return mElements[position];
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) mCurrent
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View gridView;
		if (convertView == null) {
			gridView = new View(mCurrent);
			// get layout from mobile.xml
			gridView = inflater.inflate(
					R.layout.item_list_profile_payment_desc, null);

			// set value into textview
			TextView textView = (TextView) gridView
					.findViewById(R.id.tvPaymentMethod);
			textView.setText(mElements[position].mDisplayValue);
		} else {
			gridView = (View) convertView;
		}
		return gridView;
	}
}
