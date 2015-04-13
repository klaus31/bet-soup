package beso.laboratory.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import beso.base.Beso;

public class UrlReader {

  private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

  public static JSONArray getJsonArrayFromUrl(final String url) {
    return getJsonArrayFromUrl(url, DEFAULT_CHARSET);
  }

  public static JSONArray getJsonArrayFromUrl(final String url, final Charset charset) {
    try {
      JSONArray json = new JSONArray(getStringFromUrl(url, charset));
      return json;
    } catch (JSONException e) {
      Beso.exit(url + " is silly");
      return null;
    }
  }

  public static JSONObject getJsonObjectFromUrl(final String url) {
    return getJsonObjectFromUrl(url, DEFAULT_CHARSET);
  }

  public static JSONObject getJsonObjectFromUrl(final String url, final Charset charset) {
    try {
      JSONObject json = new JSONObject(getStringFromUrl(url, charset));
      return json;
    } catch (JSONException e) {
      Beso.exit(url + " is silly");
      return null;
    }
  }

  public static String getStringFromUrl(final String url) {
    return getStringFromUrl(url, DEFAULT_CHARSET);
  }

  public static String getStringFromUrl(final String url, final Charset charset) {
    System.out.println("requesting " + url);
    BufferedReader rd = null;
    try {
      rd = new BufferedReader(new InputStreamReader(new URL(url).openStream(), charset));
      String jsonText = readAll(rd);
      return jsonText;
    } catch (IOException e) {
      Beso.exit(url + " is silly");
      return null;
    } finally {
      try {
        rd.close();
      } catch (IOException e) {
      }
    }
  }

  private static String readAll(final Reader rd) throws IOException {
    StringBuilder sb = new StringBuilder();
    int cp;
    while ((cp = rd.read()) != -1) {
      sb.append((char) cp);
    }
    return sb.toString();
  }
}