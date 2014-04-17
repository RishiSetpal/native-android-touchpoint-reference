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

import java.io.IOException;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.optimusinfo.elasticpath.cortex.common.Utils;

/**
 * This is the Configuration Model class.
 * 
 * @author Optimus
 * 
 */
public class EPConfiguration {

	@SerializedName("cortexApi")
	private EPCortex mCortexParams;

	@SerializedName("logging")
	private EPLogging mLoggingParams;

	public EPCortex getCortexParams() {
		return this.mCortexParams;
	}

	public void setCortexParams(EPCortex mCortexParams) {
		this.mCortexParams = mCortexParams;
	}

	public EPLogging getLoggingParams() {
		return this.mLoggingParams;
	}

	public void setLoggingParams(EPLogging mLoggingParams) {
		this.mLoggingParams = mLoggingParams;
	}

	/**
	 * This function returns the configuration parameter object
	 * 
	 * @param configFileName
	 * @return
	 */
	public static EPCortex getConfigurationParameters(Context current,
			String configFilename) {
		String configData;
		try {
			configData = Utils.getStringFromInputStream(current.getAssets()
					.open(configFilename));
			EPConfiguration objConfiguration = new Gson().fromJson(configData,
					EPConfiguration.class);
			return objConfiguration.getCortexParams();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

}
