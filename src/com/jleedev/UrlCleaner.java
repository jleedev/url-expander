package com.jleedev;

import android.net.Uri;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class UrlCleaner {
  private static final Pattern URCHIN_PARAMS = Pattern.compile(
          "utm_(medium|source|campaign|content|feed)");

  public Uri cleanUp(Uri uri) {
    Map<String, List<String>> params = new HashMap<String, List<String>>();
    Uri.Builder builder = uri.buildUpon();
    builder.clearQuery();
    for (String key : uri.getQueryParameterNames()) {
      if (URCHIN_PARAMS.matcher(key).matches()) {
        continue;
      }
      for (String value : uri.getQueryParameters(key)) {
        builder.appendQueryParameter(key, value);
      }
    }
    return builder.build();
  }
}
