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
import com.optimusinfo.elasticpath.cortex.common.Constants;
import com.optimusinfo.elasticpath.cortex.common.EPFragment;
import com.optimusinfo.elasticpath.cortex.common.EPFragmentActivity;
import com.optimusinfo.elasticpath.cortex.profile.ProfileModel.AddressElement;
import com.optimusinfo.elasticpath.cortex.profile.address.AddressActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AddressAdapter extends BaseAdapter {

	private EPFragment mCurrent;

	protected AddressElement[] mElements;

	public AddressAdapter(EPFragment current, AddressElement[] mElements) {
		this.mCurrent = current;
		this.mElements = mElements;
	}

	@Override
	public int getCount() {
		if (mElements != null) {
			return mElements.length + 1;
		} else {
			return 1;
		}
	}

	@Override
	public Object getItem(int position) {
		if (mElements != null) {
			return mElements[position];
		}
		return null;
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
		gridView = inflater.inflate(R.layout.item_list_profile_address_desc,
				null);

		if (mElements == null || position >= mElements.length) {
			View noAddressLayout = (View) gridView
					.findViewById(R.id.llNoAddress);
			noAddressLayout.setVisibility(View.VISIBLE);

			gridView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					mCurrent.getMainFragment().detachChildFragments();
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
			String billAddress = ProfileModel
					.getAddressLine(mElements[position]);
			// set value into textview
			TextView textView = (TextView) gridView
					.findViewById(R.id.tvAddressLine);
			textView.setText(billAddress);
			// Set the remove button
			TextView btRemoveAddress = (TextView) gridView
					.findViewById(R.id.btRemoveAddress);
			btRemoveAddress.setCompoundDrawablePadding(5);
			btRemoveAddress.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.icon_remove, 0, 0, 0);
			btRemoveAddress.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					((ProfileFragment) mCurrent.getMainFragment())
							.deleteAddress(mElements[position].mSelfLinks.mHREF);
				}
			});

			// Set the edit button
			TextView btEditAddress = (TextView) gridView
					.findViewById(R.id.btEditAddress);
			btEditAddress.setCompoundDrawablePadding(5);
			btEditAddress.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.icon_edit, 0, 0, 0);
			btEditAddress.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					mCurrent.getMainFragment().detachChildFragments();
					Intent intent = new Intent(mCurrent.getActivity(),
							AddressActivity.class);
					intent.putExtra(Constants.PageUrl.INTENT_ADRESS,
							mElements[position]);
					mCurrent.getActivity().startActivityForResult(intent,
							EPFragmentActivity.REQUEST_CODE_ADDRESS);
				}
			});
		}
		return gridView;
	}
}
