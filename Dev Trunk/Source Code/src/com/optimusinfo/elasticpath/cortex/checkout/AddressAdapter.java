package com.optimusinfo.elasticpath.cortex.checkout;

import java.util.ArrayList;
import com.optimusinfo.elasticpath.cortexAPI.R;
import com.optimusinfo.elasticpath.cortex.checkout.CheckoutModel.AddressChoice;
import com.optimusinfo.elasticpath.cortex.checkout.CheckoutModel.AddressSelector;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AddressAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<AddressChoice> mSelectorElements;

	public AddressAdapter(Context context, AddressSelector elements) {
		this.mContext = context;
		this.mSelectorElements = new ArrayList<CheckoutModel.AddressChoice>();
		if (elements.mChoice != null) {
			for (AddressChoice choice : elements.mChoice) {
				this.mSelectorElements.add(choice);
			}
		}
		if (elements.mChosen != null) {
			for (AddressChoice choice : elements.mChosen) {
				this.mSelectorElements.add(choice);
			}
		}
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
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View gridView;
		if (convertView == null) {

			gridView = new View(mContext);

			// get layout from mobile.xml
			gridView = inflater.inflate(
					R.layout.item_list_checkout_address_desc, null);

			// set value into textview
			TextView textView = (TextView) gridView
					.findViewById(R.id.tvAddressLine);
			textView.setText(CheckoutModel.getAddressLine(mSelectorElements
					.get(position).mDescription[0]));
		} else {
			gridView = (View) convertView;
		}
		return gridView;
	}

}
