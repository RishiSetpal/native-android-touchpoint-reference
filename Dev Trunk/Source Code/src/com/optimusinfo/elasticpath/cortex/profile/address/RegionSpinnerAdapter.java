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

import com.optimusinfo.elasticpath.cortex.profile.address.GeographiesModel.RegionElement;
import com.optimusinfo.elasticpath.cortexAPI.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class RegionSpinnerAdapter extends ArrayAdapter<RegionElement> {
	private Context mCurrent;
	private RegionElement[] mRegions;

	public RegionSpinnerAdapter(Context context, int textViewResourceId,
			RegionElement[] values) {
		super(context, textViewResourceId, values);
		this.mCurrent = context;
		this.mRegions = values;
	}

	public int getCount() {
		return mRegions.length;
	}

	public RegionElement getItem(int position) {
		return mRegions[position];
	}

	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView label = new TextView(mCurrent);
		label.setTextAppearance(mCurrent, R.style.TextViewSpinnerCountry);
		label.setText(mRegions[position].mDisplayName);
		return label;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		TextView label = new TextView(mCurrent);
		label.setTextAppearance(mCurrent, R.style.TextViewSpinnerCountry);
		label.setText(mRegions[position].mDisplayName);
		return label;
	}
}
