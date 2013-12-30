package com.optimusinfo.elasticpath.cortex.category;

import java.util.ArrayList;

import com.optimusinfo.elasticpath.cortex.R;
import com.optimusinfo.elasticpath.cortex.category.Category.CategoryElement;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CategoriesAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<CategoryElement> mListElements;

	public CategoriesAdapter(Context context,
			ArrayList<CategoryElement> listElements) {
		this.mContext = context;
		mListElements = listElements;
	}

	@Override
	public int getCount() {
		return mListElements.size();
	}

	@Override
	public Object getItem(int position) {
		return mListElements.get(position);
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
			gridView = inflater.inflate(R.layout.item_list_category, null);

			// set value into textview
			TextView textView = (TextView) gridView
					.findViewById(R.id.tvCategoryName);
			textView.setText(mListElements.get(position).getDisplayName());
			Log.i("NAME", mListElements.get(position).getDisplayName());

		} else {
			gridView = (View) convertView;
		}
		return gridView;
	}

}
