/**
 * Created by EJ Del Rosario
 * Copyright (c) 2015
 * Personal Intellectual Property
 * All Rights Reserved
 */
package ejdelrosario.framework.handler;

public class UploadFileParamHandler {
	
	private String key, filePath, fileName;

	/**
	 *
	 * @param key POST variable name
	 * @param filePath full file path of the upload file
	 * @param fileName file name for debugging purposes
	 */
	public UploadFileParamHandler(String key, String filePath, String fileName){
		this.key = key;
		this.filePath = filePath;
		this.fileName = fileName;
	}

	/**
	 *
	 * @param key POST variable name
	 * @param filePath full file path of the upload file
	 */
	public UploadFileParamHandler(String key, String filePath){
		this.key = key;
		this.filePath = filePath;
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
	 * @return file path
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 *
	 * @return file name
	 */
	public String getFileName(){
		return fileName;
	}

}
