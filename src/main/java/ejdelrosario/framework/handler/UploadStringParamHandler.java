/**
 * Created by EJ Del Rosario
 * Copyright (c) 2015
 * Personal Intellectual Property
 * All Rights Reserved
 */
package ejdelrosario.framework.handler;

public class UploadStringParamHandler {
	
	private String key, value;

	/**
	 *
	 * @param key POST variable name
	 * @param value value
	 */
	public UploadStringParamHandler(String key, String value){
		this.key = key;
		this.value = value;
	}

	/**
	 *
	 * @return the POST key / variable name
	 */
	public String getKey() {
		return key;
	}

	/**
	 *
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

}
