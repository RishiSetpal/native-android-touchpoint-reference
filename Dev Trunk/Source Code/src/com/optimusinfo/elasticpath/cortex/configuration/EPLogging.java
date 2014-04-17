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
