package com.optimusinfo.elasticpath.cortex.authentication;

import com.google.gson.annotations.SerializedName;
import com.optimusinfo.elasticpath.cortex.common.Constants;

import android.content.Context;

/**
 * This class serves as model for Authentication
 * 
 * @author Optimus
 * 
 */
public class Authentication {

	@SerializedName("access_token")
	protected String mAcessToken;

	@SerializedName("token_type")
	protected String mTokenType;

	@SerializedName("expires_in")
	protected int mExpiresIn;

	@SerializedName("scope")
	protected String mScope;

	@SerializedName("role")
	protected String mRole;

	public String getAcessToken() {
		return mAcessToken;
	}

	public void setAcessToken(String mAcessToken) {
		this.mAcessToken = mAcessToken;
	}

	public String getTokenType() {
		return mTokenType;
	}

	public void setTokenType(String mTokenType) {
		this.mTokenType = mTokenType;
	}

	public int getExpiresIn() {
		return mExpiresIn;
	}

	public void setExpiresIn(int mExpiresIn) {
		this.mExpiresIn = mExpiresIn;
	}

	public String getScope() {
		return mScope;
	}

	public void setScope(String mScope) {
		this.mScope = mScope;
	}

	public String getRole() {
		return mRole;
	}

	public void setRole(String mRole) {
		this.mRole = mRole;
	}

	/**
	 * This function executes the Asynchronous task to get the public
	 * authentication token from server
	 * 
	 * @param current
	 *            - current activity context
	 * @param urlEndpoint
	 *            - end point url
	 * @param objListener
	 *            - asynchronous task list5ener
	 * @param showDialogs
	 *            - is progress bar to be shown
	 * @param username
	 *            - user name
	 * @param password
	 *            - password
	 * @param scope
	 *            - user scope
	 * @param role
	 *            - user role
	 */
	public static void getTokenFromServer(Context current, String urlEndpoint,
			ListenerAsyncTaskAuthentication objListener, String username,
			String password, String scope, String role) {
		try {
			AsyncTaskAuthentication taskGetAcessToken = new AsyncTaskAuthentication(
					current, urlEndpoint, objListener, username, password,
					scope, role, Constants.Config.CONTENT_TYPE,
					Constants.Routes.AUTH_ROUTE);
			taskGetAcessToken.execute();
		} catch (NullPointerException e) {
			e.printStackTrace();
			return;
		}

	}
}
