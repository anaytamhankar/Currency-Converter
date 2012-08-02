package net.savitr.android.calc.currency.webservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

public class WebCommunicator {
	private static final String TAG = "WebCommunicator";

	// urls to access data
	private static final String LATEST_RATES_URL = "https://raw.github.com/currencybot/open-exchange-rates/master/latest.json";
	private static final String CURRENCIES_LIST_URL = "https://raw.github.com/currencybot/open-exchange-rates/master/currencies.json";
	
	public static String loadCurrencyList() {
		return loadData(CURRENCIES_LIST_URL);
	}
	
	public static String loadRates() {
		return loadData(LATEST_RATES_URL);
	}
	
	private static String loadData(String url) {
		Log.d(TAG, "Establishing Web Service Connection to url " + url);
		try {
			final HttpClient httpclient = new DefaultHttpClient();
			final HttpGet httpget = new HttpGet(url);
			final HttpResponse response = httpclient.execute(httpget);
			final HttpEntity entity = response.getEntity();
			final InputStream is = entity.getContent();
			return convertStreamToString(is);
		} catch (Exception e) {
			Log.e("TAG", "Error in http connection " + e.toString(), e);
			return null;
		}
	}

	private static String convertStreamToString(InputStream is) {
		/* To convert the InputStream to String */
		final BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		final StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			Log.e(TAG, "Error in converting web service data stream", e);
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
}
