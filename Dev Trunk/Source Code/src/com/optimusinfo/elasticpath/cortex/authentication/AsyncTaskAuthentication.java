package com.optimusinfo.elasticpath.cortex.authentication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.os.AsyncTask;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.optimusinfo.elasticpath.cortex.common.Constants;
import com.optimusinfo.elasticpath.cortex.common.Utils;

/**
 * This is an Asynchronous Task used for authentication
 * 
 * @author Optimus
 * 
 */
public class AsyncTaskAuthentication extends AsyncTask<Void, Void, Boolean> {

	protected String mCortexUrl;
	protected Context mCurrent;
	protected Gson mObjGson;
	protected ListenerAsyncTaskAuthentication mListener;
	protected String mUsername;
	protected String mPassword;
	protected String mScope;
	protected String mRole;
	protected String mContentType;
	protected String mRoute;

	/**
	 * Initializes the variables
	 * 
	 * @param currentActivity
	 *            the activity calling the async task
	 * @param url
	 *            the base url for this async task
	 * @param listener
	 *            the listener receiving the responses
	 * @param userNameLoginForm
	 *            the user name for which the auth token has to be generated
	 * @param passwordLoginForm
	 *            the password using which the auth token has to be generated
	 * @param scope
	 *            the scope of the user making the call
	 * @param role
	 *            the role of the user requesting the call
	 * @param contentType
	 *            the content type of the Cortex Get Request
	 * @param route
	 *            the route URL to be used for calling the cortex auth API
	 */
	public AsyncTaskAuthentication(Context current, String url,
			ListenerAsyncTaskAuthentication listener, String userNameLoginForm,
			String passwordLoginForm, String scope, String role,
			String contentType, String route) {
		mCurrent = current;
		mObjGson = new Gson();
		mListener = listener;
		mUsername = userNameLoginForm;
		mPassword = passwordLoginForm;
		mCortexUrl = url;
		mScope = scope;
		mRole = role;
		mContentType = contentType;
		mRoute = route;
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		try {
			if (Utils.isNetworkAvailable(mCurrent)) {
				// Create the HTTP post Request
				List<NameValuePair> parameters = new ArrayList<NameValuePair>();
				// Add grant type
				parameters
						.add(new BasicNameValuePair(
								Constants.Authentication.HEADER_GRANT_TYPE,
								"password"));
				// Add user name only if provided
				if (mUsername != null && mUsername.length() != 0) {
					parameters
							.add(new BasicNameValuePair(
									Constants.Authentication.HEADER_USERNAME,
									mUsername));
				}
				// Add pass word only if provided
				if (mPassword != null && mPassword.length() != 0) {
					parameters
							.add(new BasicNameValuePair(
									Constants.Authentication.HEADER_PASSWORD,
									mPassword));
				}
				// Add scope header
				parameters.add(new BasicNameValuePair("scope", mScope));
				// Add role header
				parameters.add(new BasicNameValuePair("role", mRole));

				try {
					// Post the Request
					UrlEncodedFormEntity ent = new UrlEncodedFormEntity(
							parameters, HTTP.UTF_8);
					
					String entityResponse = Utils.postData(mCortexUrl + mRoute,
							ent, mContentType);
					mListener.onTaskComplete(mObjGson.fromJson(entityResponse,
							Authentication.class));
					return true;
				} catch (IOException e) {
					e.printStackTrace();					
				} catch (ParseException e) {
					e.printStackTrace();					
				} catch (JsonSyntaxException e) {
					e.printStackTrace();					
				}
			} else {
				mListener.onTaskFailed(Constants.ErrorCodes.ERROR_NETWORK);
			}
		} catch (NullPointerException e) {
			e.printStackTrace();			
		}
		return false;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		if (!result) {
			mListener.onTaskFailed(Constants.ErrorCodes.ERROR_SERVER);
		}
	}
}
