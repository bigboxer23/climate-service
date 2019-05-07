package com.bigboxer23.util.http;

import com.google.common.base.Charsets;
import com.google.common.io.ByteStreams;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 *
 */
public class HttpClientUtils
{
	private static final Logger myLogger = LoggerFactory.getLogger(HttpClientUtils.class);

	private static CloseableHttpClient myHttpClient;

	public static String execute(HttpRequestBase theRequestBase)
	{
		myLogger.debug("executing " + theRequestBase.getURI());
		try (CloseableHttpResponse aResponse = HttpClientUtils.getInstance().execute(theRequestBase))
		{
			String aResponseString = new String(ByteStreams.toByteArray(aResponse.getEntity().getContent()), Charsets.UTF_8);
			myLogger.debug("executed " + theRequestBase.getURI());
			return aResponseString;
		}
		catch (IOException e)
		{
			HttpClientUtils.reset();
			myLogger.error("HttpClientUtils:execute", e);
		}
		return null;
	}
	/**
	 * Remove the cached client
	 */
	private static void reset()
	{
		try
		{
			myHttpClient.close();
		}
		catch (IOException theE)
		{
			myLogger.error("HttpClientUtils:reset", theE);
		}
		myHttpClient = null;
	}

	/**
	 * Return a cached http client which has good default timeouts, and ignores self signed SSL certs for
	 * internal HTTPS
	 *
	 * @return
	 */
	private static CloseableHttpClient getInstance()
	{
		if (myHttpClient != null)
		{
			return myHttpClient;
		}
		try
		{
			myLogger.info("Creating new HTTP client");
			myHttpClient = HttpClients
					.custom()
					.setDefaultRequestConfig(RequestConfig.custom()
							.setConnectTimeout(5000)
							.setConnectionRequestTimeout(5000)
							.setSocketTimeout(5000).build())
					.evictExpiredConnections()
					.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
					.setSSLContext(new SSLContextBuilder().loadTrustMaterial(null, (TrustStrategy) (arg0, arg1) -> true).build())
					.build();
		}
		catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException theE)
		{
			myLogger.error("HttpClientUtils:getInstance", theE);
		}
		myLogger.info("Created new HTTP client");
		return myHttpClient;
	}
}
