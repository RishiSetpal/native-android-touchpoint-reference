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
package com.optimusinfo.elasticpath.cortex.category;

import java.util.ArrayList;

import com.optimusinfo.elasticpath.cortexAPI.R;
import com.optimusinfo.elasticpath.cortex.category.CategoryModel.CategoryElement;
import com.optimusinfo.elasticpath.cortex.common.EPImageView;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * This is the Adapter for showing the categories
 * 
 * @author Optimus
 * 
 */
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
			EPImageView catImage = (EPImageView) gridView
					.findViewById(R.id.ivCategoryPhoto);
			String imageUrl = CategoryModel.getCategoryImage(mListElements
					.get(position));
			if (!TextUtils.isEmpty(imageUrl)) {
				catImage.setImageUrl(imageUrl);
			}
		} else {
			gridView = (View) convertView;
		}
		return gridView;
	}

}
