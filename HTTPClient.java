import java.io.*;
import java.net.*;
import java.util.*;
import HTTPResponse;

public class HTTPClient {
    public HTTPResponse sendRequest(String urlString, String method, Hashtable headers, String body) {
		try {
			URL url = new URL(urlString);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod(method);
			connection.setDoOutput(true);
			connection.setDoInput(true);

			// Set request headers
			if (headers != null) {
				Enumeration headerKeys = headers.keys();
				while (headerKeys.hasMoreElements()) {
				    String key = (String) headerKeys.nextElement();
				    String value = (String) headers.get(key);
				    connection.setRequestProperty(key, value);
				}
			}


			// Write request body
			if (body != null && body.length() > 0) {
			    OutputStream os = connection.getOutputStream();
			    os.write(body.getBytes());
			    os.flush();
			    os.close();
			}

			// Get response status code
			int statusCode = connection.getResponseCode();

			// Get response headers
			Hashtable responseHeaders = new Hashtable();
			int headerIndex = 0;
			while (true) {
			    String headerKey = connection.getHeaderFieldKey(headerIndex);
			    String headerValue = connection.getHeaderField(headerIndex);
			    if (headerKey == null && headerValue == null) {
			        break;
			    }
				if (headerValue == null || headerKey == null) {
					headerIndex++;
					continue;
				}
			    responseHeaders.put(headerKey, headerValue);
			    headerIndex++;
			}

			// Get response body
			InputStream is = null;
			if (statusCode >= 200 && statusCode < 300) {
			    is = connection.getInputStream();
			}
			if (is == null) {
				throw new RuntimeException("No way to get a body");
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			StringBuffer responseBody = new StringBuffer();
			String line;
			while ((line = reader.readLine()) != null) {
			    responseBody.append(line);
			}
			reader.close();

			return new HTTPResponse(statusCode, responseHeaders, responseBody.toString());
		} catch (IOException ex) {
			System.err.println(ex.getMessage());
			throw new RuntimeException(ex.getMessage());
		}

    }

    public static void main(String[] args) {
        HTTPClient client = new HTTPClient();
        Hashtable headers = new Hashtable();
        headers.put("x-requested-with", "HTTPClient Lual / 0.1");
		HTTPResponse response = client.sendRequest("http://192.168.50.204:8080/https://api.hackerwebapp.com/news", "GET", headers, null);
        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Headers: " + response.getHeaders());
        System.out.println("Body: " + response.getBody());
    }
}

