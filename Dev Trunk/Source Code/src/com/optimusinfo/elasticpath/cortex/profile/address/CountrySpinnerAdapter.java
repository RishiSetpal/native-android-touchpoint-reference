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

import com.optimusinfo.elasticpath.cortex.profile.address.GeographiesModel.GeographyElement;
import com.optimusinfo.elasticpath.cortexAPI.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CountrySpinnerAdapter extends ArrayAdapter<GeographyElement> {
	private Context mCurrent;
	private GeographyElement[] mGeographies;

	public CountrySpinnerAdapter(Context context, int textViewResourceId,
			GeographyElement[] values) {
		super(context, textViewResourceId, values);
		this.mCurrent = context;
		this.mGeographies = values;
	}

	public int getCount() {
		return mGeographies.length;
	}

	public GeographyElement getItem(int position) {
		return mGeographies[position];
	}

	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView label = new TextView(mCurrent);
		label.setTextAppearance(mCurrent, R.style.TextViewSpinnerCountry);
		label.setText(mGeographies[position].mDisplayName);
		return label;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		TextView label = new TextView(mCurrent);
		label.setTextAppearance(mCurrent, R.style.TextViewSpinnerCountry);
		label.setText(mGeographies[position].mDisplayName);
		return label;
	}
}
