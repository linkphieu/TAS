package hanelsoft.vn.timeattendance.common.servicenetwork.ultis;

import hanelsoft.vn.timeattendance.common.servicenetwork.model.ServerError;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;

public class HttpConnectionUtils {
	// public static final String BASE_URL =
	// "http://boique.petlifevs.com:81/app_dev.php/webview/server/getQue";
	public static final String BASE_URL = "http://maps.googleapis.com/maps/api/directions/json";

	public static final int REGISTRATION_TIMEOUT = 30 * 1000; // ms
	public static final int UPLOAD_CHUNK_SIZE = 1 * 1024 * 1024;// 1 Mb
	static JSONObject retObj = null;
	static String json = "";
	private static HttpClient httpClient = null;

	/**
	 * Configures the httpClient to connect to the URL provided.
	 */
	public static void maybeCreateHttpClient() {
		if (httpClient == null) {
			httpClient = new DefaultHttpClient();
			final HttpParams params = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params,
					REGISTRATION_TIMEOUT);
			HttpConnectionParams.setSoTimeout(params, REGISTRATION_TIMEOUT);
			ConnManagerParams.setTimeout(params, REGISTRATION_TIMEOUT);
		}
	}

	private static Object sendHttpRequest(String url,
			List<NameValuePair> params, RequestMethod method, Handler handler,
			Type retType) {
		if (StringUtils.isBlank(url)) {
			throw new IllegalArgumentException();
		}
		// Luong set timeout 30s
		HttpClient httpClient = new DefaultHttpClient();
		HttpUriRequest reqMethod = null;
		HttpParams paramsHTTP = httpClient.getParams();
		HttpConnectionParams.setConnectionTimeout(paramsHTTP,
				REGISTRATION_TIMEOUT);
		HttpConnectionParams.setSoTimeout(paramsHTTP, REGISTRATION_TIMEOUT);
		//
		//
		switch (method) {
		case GET:
			reqMethod = new HttpGet(buildURLWithQueryString(url, params));
			break;
		case POST:
			UrlEncodedFormEntity postEnt = createUrlEncodedFormEntity(params);
			reqMethod = new HttpPost(url);
			((HttpPost) reqMethod).setEntity(postEnt);
			break;
		case PUT:
			UrlEncodedFormEntity putEnt = createUrlEncodedFormEntity(params);
			reqMethod = new HttpPut(url);
			((HttpPut) reqMethod).setEntity(putEnt);
			break;
		case DELETE:
			reqMethod = new HttpDelete(buildURLWithQueryString(url, params));
			break;
		}

		try {
			// Log.v("*************HttpResponse***************************","HttpResponse");
			HttpResponse resp = httpClient.execute(reqMethod); // vai giay
			// Log.v("*************HttpResponse***************************","HttpResponse");
			if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// BufferedReader reader = new BufferedReader(new
				// InputStreamReader(
				// is, "iso-8859-1"), 8);
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(resp.getEntity().getContent()));
				StringBuffer buffer = new StringBuffer();
				String line = null;
				while ((line = reader.readLine()) != null) {
					buffer.append(line + "\n");
				}

				// Object retObj = GsonUtils.convertToObject(buffer.toString(),
				// retType);
				json = buffer.toString();
				try {
					retObj = new JSONObject(json);
				} catch (JSONException e) {
				}
				// Call back
				if (handler != null) {
					Message msg = new Message();
					msg.arg1 = 0;
					msg.obj = retObj;
					handler.sendMessage(msg);
				}
				// return retObj;
			} else {
				sendErrorMessage(handler,
						"Error authenticating" + resp.getStatusLine());
			}

		} catch (IOException io) {
			sendErrorMessage(handler, io.getMessage());

		} catch (Exception ex) {
			if (handler != null) {
				Message msg = new Message();
				msg.arg1 = -1;
				msg.obj = ex.getMessage();
				handler.sendMessage(msg);
			}
		}
		// Log.v("httpresponse            :"+retObj, "sadsadsadadsad");
		return retObj;
	}

	// @SuppressWarnings("deprecation")
	// public static String uploadDocument(UploadDoc doc, Handler handler) {
	//
	// String urlServer = BASE_URL + "/upload";
	//
	// StringBuffer params = new StringBuffer("?");
	// params.append("t=").append(URLEncoder.encode(doc.getTitle()))
	// .append("&");
	// params.append("f=").append(doc.getTargetFormat()).append("&");
	// params.append("mid=").append(doc.getMemberTel()).append("&");
	// params.append("l=").append(doc.getLanguage());
	//
	// return doUpload(urlServer, params, doc.getFile(), null,
	// handler, false);
	// }

	public static void download(String durl, String fileSaveTo, Handler handler) {

		try {
			URL url = new URL(durl);
			HttpURLConnection c = (HttpURLConnection) url.openConnection();
			c.setRequestMethod("GET");
			c.setDoOutput(true);
			c.connect();

			File outputFile = new File(fileSaveTo);
			FileOutputStream fos = new FileOutputStream(outputFile);

			InputStream is = c.getInputStream();
			int size = c.getContentLength();
			String type = c.getContentType();
			if (type.equals("application/json")) { // read error
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is));
				StringBuffer buffer = new StringBuffer();
				String line = null;
				while ((line = reader.readLine()) != null) {
					buffer.append(line + "\n");
				}
				Object retObj = GsonUtils.convertToObject(buffer.toString(),
						ServerError.class);
				Message m = new Message();
				if (retObj instanceof ServerError) {
					m.what = -1;
					m.obj = ((ServerError) retObj).getMessage();
				}
				handler.sendMessage(m);
			} else {

				byte[] buffer = new byte[1024];
				int len1 = 0;
				int writeSize = 0;
				c.getContentLength();
				while ((len1 = is.read(buffer)) != -1) {
					fos.write(buffer, 0, len1);
					Message m = new Message();
					m.arg1 = size;// OK
					m.arg2 = writeSize;
					m.what = 99;
					handler.sendMessage(m);
					writeSize += len1;
				}

				fos.close();
				is.close();
				Message m = new Message();
				m.what = 1;// OK
				handler.sendMessage(m);
			}

		} catch (IOException e) {
			Message m = new Message();
			m.what = -1;
			m.obj = e.getMessage();
			handler.sendMessage(m);
		}

	}

	private static void sendErrorMessage(Handler handler, String msg) {
		if (handler != null) {
			// Log.e("Chinh", "Error 2: " + msg);
			Message errorMsg = new Message();
			errorMsg.arg1 = -1;
			errorMsg.obj = msg;
			handler.sendMessage(errorMsg);
		}
	}

	public static Object getFromURL(String url, List<NameValuePair> params,
			Handler handler, Type retType) {
		return sendHttpRequest(url, params, RequestMethod.GET, handler, retType);
	}

	public static Object postToURL(String url, List<NameValuePair> params,
			Handler handler, Type retType) {
		return sendHttpRequest(url, params, RequestMethod.POST, handler,
				retType);
	}

	public static Object putToURL(String url, List<NameValuePair> params,
			Handler handler, Type retType) {
		return sendHttpRequest(url, params, RequestMethod.PUT, handler, retType);
	}

	public static Object deleteFromURL(String url, List<NameValuePair> params,
			Handler handler, Type retType) {
		return sendHttpRequest(url, params, RequestMethod.DELETE, handler,
				retType);
	}

	/**
	 * Executes the network requests on a separate thread.
	 * 
	 * @param runnable
	 *            The runnable instance containing network mOperations to be
	 *            executed.
	 */
	public static Thread performOnBackgroundThread(final Runnable runnable) {
		final Thread t = new Thread() {
			@Override
			public void run() {
				try {
					runnable.run();
				} finally {

				}
			}
		};
		t.start();
		return t;
	}

	public static String buildUrlGetApi(String url, String param) {
		String result = url + "/" + param;
		return result;
	}

	public static String buildURLWithQueryString(String url,
			List<NameValuePair> params) {
		String paramString = URLEncodedUtils.format(params, "utf-8");
		// Log.v("paramString:"+paramString, "buildURLWithQueryString");
		return url + "?" + paramString;

	}

	public static UrlEncodedFormEntity createUrlEncodedFormEntity(
			List<NameValuePair> params) {
		UrlEncodedFormEntity ent;
		try {
			ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
		} catch (UnsupportedEncodingException e) {
			throw new AssertionError();
		}
		return ent;
	}

	/**
	 * Do main upload process
	 * 
	 * @param url
	 * @param params
	 * @param inputFile
	 * @param handler
	 * @param useChunk
	 * @return error code
	 */
	public static String doUpload(String url, List<NameValuePair> params,
			File inputFile, byte[] thumnailContent, Handler handler,
			boolean useChunk) {

		HttpURLConnection connection = null;
		int errorCode = 0;
		try {
			System.getProperties().put("HTTPClient.dontChunkRequests", "true");

			if (useChunk) {
				// TODO: when use chunk upload, server must define c (chunk)
				// parameter c=[curentchunk]-[total] and uid(upload id)
				long size = inputFile.length();
				int numChunks = getNumChunks(size, UPLOAD_CHUNK_SIZE);
				long uid = System.currentTimeMillis();

				for (int i = 0; i < numChunks; i++) {
					int offset = i * UPLOAD_CHUNK_SIZE;
					String paramChunk = "&c=" + (i + 1) + "-" + numChunks
							+ "&uid=" + uid + "&thumb=true";
					URL urlObj = new URL(url + params + paramChunk);
					connection = (HttpURLConnection) urlObj.openConnection();
					byte[] dataToUpload = FileUtil.getChunk(inputFile, offset,
							UPLOAD_CHUNK_SIZE);
					errorCode = processUploadBytes(connection,
							inputFile.getName(), dataToUpload);
					// Call back for progress bar
					if (handler != null) {
						Message msg = new Message();
						msg.what = 99;
						msg.arg1 = Math.round(offset * 100 / size);
						handler.sendMessage(msg);
					}
				}
				// upload thumbnail
				if (thumnailContent != null) {
					String paramChunk = "&c=0-" + numChunks + "&uid=" + uid
							+ "&thumb=true";
					URL urlObj = new URL(url + params + paramChunk);
					connection = (HttpURLConnection) urlObj.openConnection();
					errorCode = processUploadBytes(connection,
							inputFile.getName(), thumnailContent);
					// Call back for progress bar
				}
			} else {
				URL urlObj = new URL(url + params);
				connection = (HttpURLConnection) urlObj.openConnection();
				byte[] dataToUpload = FileUtil.getChunk(inputFile, 0,
						(int) inputFile.length());
				errorCode = processUploadBytes(connection, inputFile.getName(),
						dataToUpload);

			}
			// Call back
			if (handler != null) {
				Message msg = new Message();
				msg.arg1 = errorCode;
				handler.sendMessage(msg);
			}

		} catch (Exception ex) {
			sendErrorMessage(handler, ex.getMessage());
		} finally {
			if (connection != null) {
				connection.disconnect();
				connection = null;
			}
		}
		return "" + errorCode;
	}

	/**
	 * upload bytes data
	 * 
	 * @param connection
	 * @param filename
	 * @param data
	 * @param errorHandler
	 * @return
	 * @throws IOException
	 */
	private static int processUploadBytes(HttpURLConnection connection,
			String filename, byte[] data) throws IOException {
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		int serverResponseCode = 0;

		DataOutputStream outputStream = null;

		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setUseCaches(false);
		// connection.setChunkedStreamingMode(1024*1024);

		// Enable POST method
		connection.setRequestMethod("POST");

		connection.setRequestProperty("Connection", "Keep-Alive");
		connection.setRequestProperty("Content-Type",
				"multipart/form-data;boundary=" + boundary);

		outputStream = new DataOutputStream(connection.getOutputStream());
		outputStream.writeBytes(twoHyphens + boundary + lineEnd);
		outputStream
				.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\""
						+ filename + "\"" + lineEnd);
		outputStream.writeBytes(lineEnd);
		// write data
		outputStream.write(data);

		outputStream.writeBytes(lineEnd);
		outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

		// Responses from the server (code and message)
		serverResponseCode = connection.getResponseCode();

		outputStream.flush();

		return serverResponseCode;
	}

	private static int getNumChunks(long fileSize, int chunkSize) {
		int ret = (int) (fileSize / chunkSize);
		if (fileSize % chunkSize > 0) {
			ret++;
		}
		return ret;
	}

	// public static String uploadFile(UploadFileDTO file, Handler handler) {
	//
	// String urlServer = BASE_URL + "/uploadFiles";
	//
	// StringBuffer params = new StringBuffer("?");
	// params.append("mid=").append(file.getMemberTel()).append("&"); //
	//
	// return doUpload(urlServer, params, file.getFile(), null,
	// handler, true);
	// }
}
