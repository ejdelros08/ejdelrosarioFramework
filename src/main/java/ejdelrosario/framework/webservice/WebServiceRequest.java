/**
 * Created by EJ Del Rosario
 * Copyright (c) 2015
 * Personal Intellectual Property
 * All Rights Reserved
 */

package ejdelrosario.framework.webservice;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import ejdelrosario.framework.interfaces.WebServiceInterface.onExceptionListener;
import ejdelrosario.framework.interfaces.WebServiceInterface.onNetworkExceptionListener;
import ejdelrosario.framework.interfaces.WebServiceInterface.onResponseListener;

public class WebServiceRequest extends AsyncTask<Void, Void, Void>{
	
	private String URL = null;
	
	private ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
	
	private Context context;
	
	private Method method;
	
	private ProgressDialog dlg;
	
	private HttpClient httpClient;
	
	private String responseString;
	
	private onResponseListener mResponseListener;
	
	private onNetworkExceptionListener mNetworkListener;
	
	private onExceptionListener mExceptionListener;
	
	private Exception catcher;
	private String exceptionMessage = "";
	
	public WebServiceRequest(Context context, Method method){
		this.context = context;
		this.method = method;
	}
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		if(getProgressDialog() != null){
			getProgressDialog().show();
		}
		super.onPreExecute();
		
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		if(WebServiceHelper.isNetworkAvailable(context)){
			
			HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, WebServiceHelper.TIMEOUT);
			HttpConnectionParams.setSoTimeout(httpParams, WebServiceHelper.TIMEOUT);
			httpClient = new DefaultHttpClient(httpParams);
			
			switch(method){
			
				case GET:{
					
					String url = getURL();
					if(parameters.size() > 0){
						url = url + "?" + URLEncodedUtils.format(parameters, "UTF-8");
					}
					HttpGet httpGet = new HttpGet(url);
					
					try {
						HttpResponse resp = httpClient.execute(httpGet);
						HttpEntity entity = resp.getEntity();
						responseString = EntityUtils.toString(entity);
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						catcher = e;
						exceptionMessage = "Connection Error";
						e.printStackTrace();
					} catch (ClientProtocolException e) {
						// TODO Auto-generated catch block
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
					} catch (SocketTimeoutException e){
						exceptionMessage = "Connection Timeout";
						catcher = e;
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						exceptionMessage = "Connection Error";
						catcher = e;
						e.printStackTrace();
					}


					
					break;
				}
				case POST:{
					
					HttpPost httpPost = new HttpPost(getURL());
					
					try {
						httpPost.setEntity(new UrlEncodedFormEntity(getParams(), "UTF-8"));
						HttpResponse resp = httpClient.execute(httpPost);
						HttpEntity entity = resp.getEntity();
						responseString = EntityUtils.toString(entity);
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						catcher = e;
						exceptionMessage = "Connection Error";
						e.printStackTrace();
					} catch (ClientProtocolException e) {
						// TODO Auto-generated catch block
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
					} catch (SocketTimeoutException e){
						exceptionMessage = "Connection Timeout";
						catcher = e;
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						exceptionMessage = "Connection Error";
						catcher = e;
						e.printStackTrace();
					}
					break;
				}
			}
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
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
	 * creates a non-cancellable progressDialog show the given message
	 * @param msg message while task is executing in background
	 */
	public void setProgressDialog(String msg){
		dlg = new ProgressDialog(context);
		dlg.setMessage(msg);
		dlg.setCancelable(false);
	}
	
	private ProgressDialog getProgressDialog(){
		return dlg;
	}

	/**
	 * adds an argument for the webservice
	 * @param key GET or POST variable name
	 * @param value
	 */
	public void addParams(String key, String value){
		parameters.add(new BasicNameValuePair(key, value));
	}

	private ArrayList<NameValuePair> getParams(){
		return parameters;
	}

	/**
	 *
	 * @return parameters set as a list from {@link #addParams(String, String)}
	 */
	public ArrayList<NameValuePair> getParam(){
		return parameters;
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

}
