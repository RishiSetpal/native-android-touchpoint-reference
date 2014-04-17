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
package com.optimusinfo.elasticpath.cortex.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

public class Utils {
	/**
	 * Utility method to check network availability
	 * 
	 * @return boolean value indicating the presence of network availability
	 */
	public static boolean isNetworkAvailable(Context argActivity) {

		if (argActivity == null) {
			return false;
		}
		ConnectivityManager connectivityManager;
		NetworkInfo activeNetworkInfo = null;
		try {

			connectivityManager = (ConnectivityManager) argActivity
					.getSystemService(Context.CONNECTIVITY_SERVICE);

			activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		return activeNetworkInfo != null;
	}

	/**
	 * Get data from the given Cortex URL
	 * 
	 * @param url
	 *            URL of the API
	 * @param accessToken
	 *            the access token to authenticate the request
	 * @param contentType
	 *            the content type of the request
	 * @param contentTypeString
	 * @param authorizationString
	 * @param accessTokenInitializer
	 *            the string to be used before the access token as per the
	 *            guidelines
	 * @return the JSON data returned by this navigation task
	 */
	public static String getJSONFromCortexUrl(String url, String accessToken,
			String contentType, String contentTypeString,
			String authorizationString, String accessTokenInitializer) {
		try {
			// Input stream
			InputStream objInputStream;
			// JSON String
			String responseJSON;
			// Making HTTP request
			DefaultHttpClient httpClient = new DefaultHttpClient();

			Log.i("GET REQUEST", url);

			HttpGet httpGet = new HttpGet(url);

			httpGet.setHeader(contentTypeString, contentType);

			httpGet.setHeader(authorizationString, accessTokenInitializer + " "
					+ accessToken);
			HttpResponse httpResponse = httpClient.execute(httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();
			switch (httpResponse.getStatusLine().getStatusCode()) {
			case Constants.ApiResponseCode.REQUEST_SUCCESSFUL_CREATED:
			case Constants.ApiResponseCode.REQUEST_SUCCESSFUL_UPDATED:
				objInputStream = httpEntity.getContent();
				break;
			case Constants.ApiResponseCode.UNAUTHORIZED_ACCESS:
				return Integer
						.toString((Constants.ApiResponseCode.UNAUTHORIZED_ACCESS));
			default:
				return Integer.toString(httpResponse.getStatusLine()
						.getStatusCode());
			}
			;

			// Parse the response to String
			BufferedReader objReader = new BufferedReader(
					new InputStreamReader(objInputStream, "iso-8859-1"), 8);
			StringBuilder objSb = new StringBuilder();
			String line = null;

			while ((line = objReader.readLine()) != null) {
				objSb.append(line + "\n");
			}
			objInputStream.close();

			// Instantiate the String before setting it to the result
			// string
			responseJSON = new String();
			responseJSON = objSb.toString();

			return responseJSON;
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * Returns the string corresponding to the data in the input stream
	 * 
	 * @param inputStream
	 * @return the corresponding string for the corresponding inputstream
	 */
	public static String getStringFromInputStream(InputStream inputStream) {
		if (inputStream != null) {
			String configJson = null;
			try {
				int size = inputStream.available();
				byte[] buffer = new byte[size];
				inputStream.read(buffer);
				configJson = new String(buffer);
				inputStream.close();
				return configJson;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 
	 * @param accessToken
	 * @param postURL
	 * @param ent
	 * @param contentType
	 * @return
	 */
	public static int postData(String postUrl, JSONObject requestBody,
			String accessToken, String contentType,
			String accessTokenInitializer) {
		HttpClient client = new DefaultHttpClient();
		try {

			HttpPost postRequest = new HttpPost(postUrl);
			postRequest.setHeader(Constants.RequestHeaders.CONTENT_TYPE_STRING,
					contentType);
			postRequest.setHeader(
					Constants.RequestHeaders.AUTHORIZATION_STRING,
					accessTokenInitializer + " " + accessToken);

			if (requestBody != null) {
				StringEntity requestEntity = new StringEntity(
						requestBody.toString());
				requestEntity.setContentEncoding("UTF-8");
				requestEntity.setContentType(contentType);
				postRequest.setEntity(requestEntity);
			}

			HttpResponse responsePOST = client.execute(postRequest);
			Log.i("POST RESPONSE",
					EntityUtils.toString(responsePOST.getEntity()));

			return responsePOST.getStatusLine().getStatusCode();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} finally {
			client.getConnectionManager().shutdown();
		}
		return 0;
	}

	/**
	 * Creates a HTTP post request and posts it
	 * 
	 * @param postURL
	 * @param ent
	 *            the request parameters to be submitted with HTTP post
	 * @param contentType
	 *            describes the content type of this HTTP post request
	 * @return the response returned by this request
	 */
	public static String postData(String postURL, UrlEncodedFormEntity ent,
			String contentType) {
		try {

			HttpClient client = new DefaultHttpClient();

			HttpPost post = new HttpPost(postURL);
			post.setHeader("Content-Type", contentType);
			post.setEntity(ent);

			HttpResponse responsePOST = client.execute(post);
			HttpEntity resEntity = responsePOST.getEntity();

			String response = null;

			if (resEntity != null) {
				response = EntityUtils.toString(resEntity);
			}

			return response;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Creates a HTTP post request and posts it
	 * 
	 * @param postURL
	 *            the request parameters to be submitted with HTTP post
	 * @param contentType
	 *            describes the content type of this HTTP post request
	 * @return the response returned by this request
	 */
	public static String postData(String postURL, String accessToken,
			String contentType, String accessTokenInitializer) {
		HttpClient client = new DefaultHttpClient();
		try {

			HttpPost postRequest = new HttpPost(postURL);
			postRequest.setHeader(Constants.RequestHeaders.CONTENT_TYPE_STRING,
					contentType);
			postRequest.setHeader(
					Constants.RequestHeaders.AUTHORIZATION_STRING,
					accessTokenInitializer + " " + accessToken);

			HttpResponse httpResponse = client.execute(postRequest);
			Header[] headers = httpResponse.getAllHeaders();

			switch (httpResponse.getStatusLine().getStatusCode()) {

			case Constants.ApiResponseCode.REQUEST_SUCCESSFUL_CREATED:

			case Constants.ApiResponseCode.REQUEST_SUCCESSFUL_UPDATED:
				for (Header header : headers) {
					String name = header.getName();
					String value = header.getValue();
					if (name.equalsIgnoreCase("Location")) {
						return value;
					}
				}
				break;
			case Constants.ApiResponseCode.UNAUTHORIZED_ACCESS:
				return Integer
						.toString((Constants.ApiResponseCode.UNAUTHORIZED_ACCESS));
			default:
				return Integer.toString(httpResponse.getStatusLine()
						.getStatusCode());
			}
			;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * This function performs the delete request
	 * 
	 * @param deleteUrl
	 * @param accessToken
	 * @param contentType
	 * @param contentTypeString
	 * @param authorizationString
	 * @param accessTokenInitializer
	 * @return
	 */
	public static int deleteRequest(String deleteUrl, String accessToken,
			String contentType, String contentTypeString,
			String authorizationString, String accessTokenInitializer) {
		// Making HTTP request
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			Log.i("DELETE REQUEST", deleteUrl);
			HttpDelete httpDelete = new HttpDelete(deleteUrl);
			httpDelete.setHeader(contentTypeString, contentType);
			httpDelete.setHeader(authorizationString, accessTokenInitializer
					+ " " + accessToken);
			HttpResponse httpResponse = httpClient.execute(httpDelete);
			return httpResponse.getStatusLine().getStatusCode();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	public static int putRequest(String deleteUrl, JSONObject requestBody,
			String accessToken, String contentType, String contentTypeString,
			String authorizationString, String accessTokenInitializer) {
		// Making HTTP request
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			Log.i("PUT REQUEST", deleteUrl);
			HttpPut httpPut = new HttpPut(deleteUrl);
			httpPut.setHeader(contentTypeString, contentType);
			httpPut.setHeader(authorizationString, accessTokenInitializer + " "
					+ accessToken);

			if (requestBody != null) {
				StringEntity requestEntity = new StringEntity(
						requestBody.toString());
				requestEntity.setContentEncoding("UTF-8");
				requestEntity.setContentType(contentType);
				httpPut.setEntity(requestEntity);
			}

			HttpResponse httpResponse = httpClient.execute(httpPut);
			return httpResponse.getStatusLine().getStatusCode();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@SuppressWarnings("rawtypes")
	public static int getTotalHeightofListView(AdapterView argAdapterView) {
		Adapter mAdapter = argAdapterView.getAdapter();
		if (mAdapter == null) {
			// pre-condition
			return 0;
		}
		int totalHeight = 0;
		for (int i = 0; i < mAdapter.getCount(); i++) {
			View mView = mAdapter.getView(i, null, argAdapterView);
			mView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));
			mView.measure(
					MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
					MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
			totalHeight += mView.getMeasuredHeight();
		}
		return totalHeight;
	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
		listView.requestLayout();
	}

	/**
	 * This methods sets the gridview height
	 * 
	 * @param gridView
	 */
	public static void setGridViewHeightBasedOnChildren(GridView gridView,
			int columncount) {
		ViewGroup.LayoutParams params = gridView.getLayoutParams();
		int size = gridView.getAdapter().getCount();
		int dynamicHeight = Utils.getTotalHeightofListView(gridView);
		if (size % columncount == 0) {
			params.height = (dynamicHeight / columncount) + 25;
		} else {
			params.height = (dynamicHeight / columncount)
					+ (dynamicHeight / size) - 25;
		}
		gridView.setLayoutParams(params);
		gridView.requestLayout();
	}

	/**
	 * This methods sets the gridview height
	 * 
	 * @param gridView
	 */
	public static void setSingleCoulmnGridViewHeightBasedOnChildren(
			GridView gridView) {
		ViewGroup.LayoutParams params = gridView.getLayoutParams();
		int size = gridView.getAdapter().getCount();
		int dynamicHeight = Utils.getTotalHeightofListView(gridView);
		params.height = (dynamicHeight) + (dynamicHeight / size);
		gridView.setLayoutParams(params);
		gridView.requestLayout();
	}

}
