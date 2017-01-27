/**
 * Created by EJ Del Rosario
 * Copyright (c) 2015
 * Personal Intellectual Property
 * All Rights Reserved
 */
package ejdelrosario.framework.webservice;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import ejdelrosario.framework.handler.UploadFileParamHandler;
import ejdelrosario.framework.handler.UploadStringParamHandler;
import ejdelrosario.framework.interfaces.WebServiceInterface.onExceptionListener;
import ejdelrosario.framework.interfaces.WebServiceInterface.onNetworkExceptionListener;
import ejdelrosario.framework.interfaces.WebServiceInterface.onProgressUpdateListener;
import ejdelrosario.framework.interfaces.WebServiceInterface.onResponseListener;
import ejdelrosario.framework.webservice.MultiPartEntity.ProgressListener;

public class WebServiceUpload extends AsyncTask<Void, Integer, Void>{
	
	private String URL = null;
	
	private ArrayList<UploadStringParamHandler> stringParam = new ArrayList<UploadStringParamHandler>();
	private ArrayList<UploadFileParamHandler> fileParam = new ArrayList<UploadFileParamHandler>();
	
	private Context context;
	
	private ProgressDialog dlg;
	
	private HttpClient httpClient;
	
	private String responseString;
	
	private onResponseListener mResponseListener;
	
	private onNetworkExceptionListener mNetworkListener;
	
	private onExceptionListener mExceptionListener;

	private onProgressUpdateListener mProgressListener;
	
	private Exception catcher;
	private String exceptionMessage = "";
	
	private long totalSize = 0;
	
	private String dialogMessage = "";
	
	public WebServiceUpload(Context context){
		this.context = context;
	}
	
	@Override
	protected void onPreExecute() {
		if(getProgressDialog() != null){
			getProgressDialog().show();
		}
		super.onPreExecute();
		
	}
	
	
	@Override
	protected Void doInBackground(Void... params) {
		if(WebServiceHelper.isNetworkAvailable(context)){
			
			httpClient = new DefaultHttpClient();
			
			HttpPost post = new HttpPost(getURL());
			MultiPartEntity mEntity = new MultiPartEntity(new ProgressListener() {
				
				@Override
				public void transferred(long num) {
					publishProgress((int) ((num / (float) totalSize) * 100));
				}
			});
			
			for(UploadFileParamHandler files : fileParam){
				mEntity.addPart(files.getKey(), new FileBody(new File(files.getFilePath())));
			}
			
			for(UploadStringParamHandler strings : stringParam){
				try {
					mEntity.addPart(strings.getKey(), new StringBody(strings.getValue()));
				} catch (UnsupportedEncodingException e) {
					exceptionMessage = "Invalid string parameter encoding";
					catcher = e;
					e.printStackTrace();
				}
			}
			
			totalSize = mEntity.getContentLength();
			
			post.setEntity(mEntity);
			
			try {
				
				HttpResponse resp = httpClient.execute(post);
				HttpEntity entity = resp.getEntity();
				responseString = EntityUtils.toString(entity);
				
			} catch (UnsupportedEncodingException e) {
				catcher = e;
				exceptionMessage = "Connection Error";
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				exceptionMessage = "Connection Error";
				catcher = e;
				e.printStackTrace();
			} catch (UnknownHostException e){
				exceptionMessage = "Connection Error";
				catcher = e;
				e.printStackTrace();
			} catch(ConnectTimeoutException e){
				exceptionMessage = "Connection Timeout";
				catcher = e;
				e.printStackTrace();
			} catch (IOException e) {
				exceptionMessage = "Connection Error";
				catcher = e;
				e.printStackTrace();
			}
			
		}
		return null;
	}
	
	@Override
	protected void onProgressUpdate(final Integer... values) {
		if(getProgressDialog() != null){
			getProgressDialog().setProgress(values[0]);
//			new Handler().post(new Runnable() {
//
//				@Override
//				public void run() {
//					getProgressDialog().setMessage(dialogMessage + "" + values[0] + "%");
//				}
//			});
		}

		if(mProgressListener != null){
			mProgressListener.onProgress(values[0]);
		}
	}
	
	
	@Override
	protected void onPostExecute(Void result) {
		if(getProgressDialog() != null){
			if(getProgressDialog().isShowing()){
				getProgressDialog().dismiss();
			}
		}
		if(responseString != null){
			mResponseListener.onResponse(responseString);
		}
		else{
			if(!WebServiceHelper.isNetworkAvailable(context)){
				mNetworkListener.onNetworkException();
			}
			else if(catcher != null){
				mExceptionListener.onException(catcher, exceptionMessage);
			}
		}
		super.onPostExecute(result);
	}


	/**
	 * adds a POST string parameter for the webservice
	 * @param handler
	 */
	public void addStringParam(UploadStringParamHandler handler){
		stringParam.add(handler);
	}

	/**
	 *
	 * @return List of string parameters set from {@link #addStringParam(UploadStringParamHandler)}
	 */
	public ArrayList<UploadStringParamHandler> getStringParam(){
		return stringParam;
	}

	/**
	 * adds a file parameter for the webservice
	 * @param handler
	 */
	public void addFileParam(UploadFileParamHandler handler){
		fileParam.add(handler);
	}

	/**
	 *
	 * @return List of file parameters set from {@link #addFileParam(UploadFileParamHandler)}
	 */
	public ArrayList<UploadFileParamHandler> getFileParam(){
		return fileParam;
	}

	/**
	 * creates a non-cancellable progressDialog show the given message
	 * @param msg message while task is executing in background
	 */
	public void setProgressDialog(String msg){
		dlg = new ProgressDialog(context);
		dlg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		dlg.setMessage(msg);
		dlg.setCancelable(false);
		dialogMessage = msg;
	}
	
	private ProgressDialog getProgressDialog(){
		return dlg;
	}

	/**
	 * sets the url of the webservice
	 * @param url
	 */
	public void setURL(String url){
		URL = url;
	}

	/**
	 *
	 * @return URL set from {@link #setURL(String)}
	 */
	public String getURL(){
		return URL;
	}

	/**
	 * sets the listener for the webservice response
	 * @param listener
	 */
	public void setOnResponseListener(onResponseListener listener){
		mResponseListener = listener;
	}

	/**
	 * sets the listener for the network checker
	 * @param listener
	 */
	public void setOnNetworkExceptionListener(onNetworkExceptionListener listener){
		mNetworkListener = listener;
	}

	/**
	 * sets the listener for various exceptions while the webservice is executing
	 * @param listener
	 */
	public void setOnExceptionListener(onExceptionListener listener){
		mExceptionListener = listener;
	}

	/**
	 * sets the listener for upload progress
	 * @param listener
     */
	public void setOnProgressUpdateListener(onProgressUpdateListener listener){
		mProgressListener = listener;
	}

}
