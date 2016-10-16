/**
 * 
 */
package com.wy.stock.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

/**
 * 工具类
 * 
 * @author leslie
 * 
 */
public class HttpUtils {
	// log4j
	private static Logger LOGGER = Logger.getLogger(HttpUtils.class.getName());

	// 连接网络的次数.
	private static int numOfConn = 0;

	/**
	 * 使用httpclient 获取 html.
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static String getStringByHttpClient(String url, String encoding)
			throws IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost method = null;
		StringBuilder sb = new StringBuilder("");
		try {
			method = new HttpPost(url);

			RequestConfig requestConfig = RequestConfig.custom()
					.setConnectionRequestTimeout(5000).setConnectTimeout(5000)
					.setSocketTimeout(5 * 1000).build();
			method.setConfig(requestConfig);

			CloseableHttpResponse response = (CloseableHttpResponse) httpclient
					.execute(method);
			HttpEntity entity = response.getEntity();

			// If the response does not enclose an entity, there is no need
			// to bother about connection release
			if (entity != null) {
				InputStream instream = entity.getContent();
				instream.read();
				// 使用GB2312, 因为返回的 html 的 CONTENT 中设置了.
				BufferedReader in = new BufferedReader(new InputStreamReader(
						instream, encoding));
				String line = "";
				while ((line = in.readLine()) != null) {
					sb.append(line);
				}
				instream.close();
			}
		} catch (ClientProtocolException e) {
			LOGGER.error(e);
			throw e;
		} catch (IOException e) {
			throw e;
		} finally {
			if (null != method) {
				method.releaseConnection();
			}
			httpclient.close();
		}
		return sb.toString();
	}

	public static String getAjaxDocByHttpClient(String url, String encoding,
			Map<String, String> params) throws IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost method = null;
		StringBuilder sb = new StringBuilder("");
		try {
			method = new HttpPost(url);

			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			if (params != null && !params.isEmpty()) {
				for (Entry<String, String> entry : params.entrySet()) {
					nvps.add(new BasicNameValuePair(entry.getKey(), entry
							.getValue()));
				}
			}
			method.setEntity(new UrlEncodedFormEntity(nvps, encoding));

			RequestConfig requestConfig = RequestConfig.custom()
					.setConnectionRequestTimeout(10000)
					.setConnectTimeout(10000).setSocketTimeout(10 * 1000)
					// .setProxy(new HttpHost("127.0.0.1", 8087))
					.build();
			method.setConfig(requestConfig);

			CloseableHttpResponse response = (CloseableHttpResponse) httpclient
					.execute(method);
			HttpEntity entity = response.getEntity();

			// If the response does not enclose an entity, there is no need
			// to bother about connection release
			if (entity != null) {
				InputStream instream = entity.getContent();
				instream.read();
				// 使用GB2312, 因为返回的 html 的 CONTENT 中设置了.
				BufferedReader in = new BufferedReader(new InputStreamReader(
						instream, encoding));
				String line = "";
				while ((line = in.readLine()) != null) {
					sb.append(line);
				}
				instream.close();
			}
		} catch (ClientProtocolException e) {
			LOGGER.error(e);
			throw e;
		} catch (IOException e) {
			throw e;
		} finally {
			if (null != method) {
				method.releaseConnection();
			}
			httpclient.close();
		}
		return sb.toString();
	}

	/**
	 * HTTP 下载文件.
	 * 
	 * @param httpUrl
	 * @param saveFile
	 * @return
	 * @throws IOException 
	 */
	public static void httpDownload(String httpUrl, FileOutputStream fs) throws IOException {
		// 下载网络文件
		int byteread = 0;

		URL url = null;
		try {
			url = new URL(httpUrl);
		} catch (MalformedURLException e1) {
			throw e1;
		}
		
		URLConnection conn = null;
		InputStream inStream = null;
		try {
			conn = url.openConnection();
			conn.setConnectTimeout(1 * 1000);
			conn.setReadTimeout(10 * 1000);
			inStream = conn.getInputStream();

			byte[] buffer = new byte[1024];
			while ((byteread = inStream.read(buffer)) != -1) {
				fs.write(buffer, 0, byteread);
			}
		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		}finally{
			if(fs != null){
				fs.close();
			}
			
			if(inStream != null){
				inStream.close();
			}
			
			if(conn != null){
				conn = null;
			}
		}
	}
	
	/**
	 * HTTP 下载文件.
	 * 
	 * @param httpUrl
	 * @param saveFile
	 * @return
	 * @throws IOException 
	 */
	public static void httpDownload(String httpUrl, String encoding, int timeout, File fs) throws IOException {
		URL url = null;
		try {
			url = new URL(httpUrl);
		} catch (MalformedURLException e1) {
			throw e1;
		}
		
		StringBuilder sb = new StringBuilder("");
		URLConnection conn = null;
		InputStream inStream = null;
		BufferedWriter bw = new BufferedWriter(new FileWriter(fs));
		try {
			conn = url.openConnection();
			conn.setConnectTimeout(1 * 1000);
			conn.setReadTimeout(10 * 1000);
			inStream = conn.getInputStream();

//			byte[] buffer = new byte[1024];
//			while ((byteread = inStream.read(buffer)) != -1) {
//				fs.write(buffer, 0, byteread);
//			}
			
			inStream.read();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					inStream, encoding));
			String line = "";
			while ((line = in.readLine()) != null) {
				sb.append(line).append("\n");
			}
			inStream.close();
			
			bw.write(sb.toString());
			bw.close();
		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		}finally{
			
			if(inStream != null){
				inStream.close();
			}
			
			if(conn != null){
				conn = null;
			}
			if(bw != null){
				bw.close();
			}
		}
	}
	
	public static String getAjaxDocByHttpClient(String url, String encoding, int timeout) throws IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost method = null;
		StringBuilder sb = new StringBuilder("");
		try {
			method = new HttpPost(url);

//			method.addHeader("connection", "keep-alive");

//			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
//			nvps.add(new BasicNameValuePair("IDToken1", "username"));
//			nvps.add(new BasicNameValuePair("IDToken2", "password"));
//			method.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
			
			RequestConfig requestConfig = RequestConfig.custom()
					.setConnectionRequestTimeout(timeout)
					.setConnectTimeout(timeout).setSocketTimeout(timeout)
					.build();
			method.setConfig(requestConfig);
			
			
			CloseableHttpResponse response = (CloseableHttpResponse) httpclient
					.execute(method);
			HttpEntity entity = response.getEntity();

			// If the response does not enclose an entity, there is no need
			// to bother about connection release
			if (entity != null) {
				InputStream instream = entity.getContent();
				instream.read();
				// 使用GB2312, 因为返回的 html 的 CONTENT 中设置了.
				BufferedReader in = new BufferedReader(new InputStreamReader(
						instream, encoding));
				String line = "";
				while ((line = in.readLine()) != null) {
					sb.append(line);
				}
				instream.close();
			}
		} catch (ClientProtocolException e) {
			LOGGER.error(e);
			throw e;
		} catch (IOException e) {
			throw e;
		} finally {
			if (null != method) {
				method.releaseConnection();
			}
			httpclient.close();
		}
		return sb.toString();
	}

	public static int getNumOfConn() {
		return numOfConn;
	}
	
}
