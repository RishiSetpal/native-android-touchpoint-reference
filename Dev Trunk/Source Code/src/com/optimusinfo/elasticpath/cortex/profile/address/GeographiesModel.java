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

import android.content.Context;

import com.google.gson.annotations.SerializedName;

public class GeographiesModel {

	/**
	 * This method returns the geographies
	 * 
	 * @param current
	 * @param url
	 * @param accessToken
	 * @param mListner
	 */
	public static void getGeographies(Context current, String url,
			String accessToken, ListenerGetGeographies mListner) {
		try {
			new AsyncTaskGetGeographies(current, url, accessToken,
					Geographies.class, mListner).execute();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method returns the Regions
	 * 
	 * @param current
	 * @param url
	 * @param accessToken
	 * @param mListner
	 */
	public static void getRegions(Context current, String url,
			String accessToken, ListenerGetGeographies mListner) {
		try {
			new AsyncTaskGetGeographies(current, url, accessToken,
					Regions.class, mListner).execute();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	public class Geographies {
		@SerializedName("_element")
		public GeographyElement[] mElement;
	}

	public class GeographyElement {
		@SerializedName("links")
		public GeographyLinks[] mLinks;

		@SerializedName("display-name")
		public String mDisplayName;

		@SerializedName("name")
		public String mValue;
	}

	public class GeographyLinks {
		@SerializedName("rel")
		public String mRelation;
		@SerializedName("href")
		public String mHref;
	}

	/**
	 * This method returns the country position
	 * 
	 * @param countryName
	 * @param mElements
	 * @return
	 */
	public static int getCountriesPosition(String countryName,
			GeographyElement[] mElements) {
		for (int i = 0; i < mElements.length; i++) {
			if (countryName.equalsIgnoreCase(mElements[i].mValue)) {
				return i;
			}
		}
		return 0;
	}

	public class Regions {
		@SerializedName("_element")
		public RegionElement[] mElement;
	}

	public class RegionElement {
		@SerializedName("display-name")
		public String mDisplayName;

		@SerializedName("name")
		public String mValue;
	}
	
	
	/**
	 * This method returns the regions position
	 * 
	 * @param countryName
	 * @param mElements
	 * @return
	 */
	public static int getRegionsPosition(String countryName,
			RegionElement[] mElements) {
		for (int i = 0; i < mElements.length; i++) {
			if (countryName.equalsIgnoreCase(mElements[i].mValue)) {
				return i;
			}
		}
		return 0;
	}
	
	/**
	 * Returns the region url
	 * @param mCurrent
	 * @return
	 */
	public static String getRegionsUrl(GeographyElement mCurrent){
		for(GeographyLinks currLink : mCurrent.mLinks){
			if(currLink.mRelation.equalsIgnoreCase("regions")){
				return currLink.mHref;
			}
		}
		return null;
	}

}
