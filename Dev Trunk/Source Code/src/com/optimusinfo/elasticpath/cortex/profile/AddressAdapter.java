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
		return mElements.length + 1;
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
		LayoutInflater inflater = (LayoutInflater) mCurrent.getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View gridView;

		if (position >= mElements.length) {

			gridView = new View(mCurrent.getActivity());
			// get layout from mobile.xml
			gridView = inflater.inflate(R.layout.item_add_new_address, null);
			gridView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					mCurrent.getMainFragment().detachChildFragments();
					Intent intent = new Intent(mCurrent.getActivity(), AddressActivity.class);
					mCurrent.startActivityForResult(intent,
							EPFragmentActivity.REQUEST_CODE_ADDRESS);
				}
			});

		} else {

			gridView = new View(mCurrent.getActivity());

			// get layout from mobile.xml
			gridView = inflater.inflate(
					R.layout.item_list_profile_address_desc, null);

			String billAddress = ProfileModel
					.getAddressLine(mElements[position]);

			// set value into textview
			TextView textView = (TextView) gridView
					.findViewById(R.id.tvAddressLine);
			textView.setText(billAddress);

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

			TextView btEditAddress = (TextView) gridView
					.findViewById(R.id.btEditAddress);
			btEditAddress.setCompoundDrawablePadding(5);
			btEditAddress.setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.icon_edit, 0, 0, 0);
			btEditAddress.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					mCurrent.getMainFragment().detachChildFragments();
					Intent intent = new Intent(mCurrent.getActivity(), AddressActivity.class);
					intent.putExtra(Constants.PageUrl.INTENT_ADRESS,
							mElements[position]);
					mCurrent.startActivityForResult(intent,
							EPFragmentActivity.REQUEST_CODE_ADDRESS);
				}
			});

		}

		return gridView;
	}
}
