package com.optimusinfo.elasticpath.cortex.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

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
				return "";
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
	 * 
	 * @param accessToken
	 * @param postURL
	 * @param ent
	 * @param contentType
	 * @return
	 */
	public static String postData(String accessToken, String postURL,
			JSONObject objInput, String contentType) {

		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(postURL);
			post.setHeader("Content-Type", contentType);
			post.setHeader("Authorization", "Bearer " + accessToken);
			post.setEntity(new StringEntity(objInput.toString(), "UTF-8"));
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

}
