package com.optimusinfo.elasticpath.cortex.configuration;

import com.google.gson.annotations.SerializedName;

/**
 * This class is the model class for logging related configuration.
 * 
 * @author Optimus
 * 
 */
public class EPLogging {
	@SerializedName("logInfo")
	private boolean mIsLogInfoEnabled;
	
	@SerializedName("logWarnings")
	private boolean mIsLogWarningsEnabled;
	
	@SerializedName("logErrors")
	private boolean mIsLogErrorEnabled;

	public boolean isLogInfoEnabled() {
		return mIsLogInfoEnabled;
	}

	public void setLogInfoEnabled(boolean mIsLogInfoEnabled) {
		this.mIsLogInfoEnabled = mIsLogInfoEnabled;
	}

	public boolean isLogWarningsEnabled() {
		return mIsLogWarningsEnabled;
	}

	public void setLogWarningsEnabled(boolean mIsLogWarningsEnabled) {
		this.mIsLogWarningsEnabled = mIsLogWarningsEnabled;
	}

	public boolean isLogErrorEnabled() {
		return mIsLogErrorEnabled;
	}

	public void setLogErrorEnabled(boolean mIsLogErrorEnabled) {
		this.mIsLogErrorEnabled = mIsLogErrorEnabled;
	}

}
