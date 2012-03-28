package com.jleedev;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import java.net.HttpURLConnection;
import java.net.URL;

public class ExpandUrlActivity extends Activity {

  private static final String TAG = "ExpandUrlActivity";
  private static final String USER_AGENT = "Android URL Expander";

  ExpandUrlTask mExpandUrlTask;
  UrlCache cache;
  UrlCleaner urlCleaner;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    cache = new UrlCache(this);
    urlCleaner = new UrlCleaner();
    Uri uri = getIntent().getData();
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    TextView uriLabel = (TextView) findViewById(R.id.uri);
    uriLabel.setText(uri.toString());
    mExpandUrlTask = new ExpandUrlTask();
    mExpandUrlTask.execute(uri);
  }

  @Override
  public void onPause() {
    super.onPause();
    mExpandUrlTask.cancel(true);
  }

  Uri resolve(Uri uri) {
    Uri result;
    if ((result = cache.findUri(uri)) != null) {
      return result;
    }
    try {
      URL url = new URL(uri.toString());
      HttpURLConnection client = (HttpURLConnection) url.openConnection();
      client.setInstanceFollowRedirects(false);
      String location = client.getHeaderField("Location");
      client.disconnect();
      if (location == null) {
        return null;
      } else {
        result = Uri.parse(location);
        cache.putUri(uri, result);
        return result;
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  class ExpandUrlTask extends AsyncTask<Uri, Void, Uri> {
    @Override
    protected Uri doInBackground(Uri... uris) {
      Uri uri = uris[0];
      Uri result = resolve(uri);
      if (result == null) {
        cancel(true);
      }
      result = urlCleaner.cleanUp(result);
      return result;
    }

    @Override
    protected void onPostExecute(Uri result) {
      Intent intent = new Intent(Intent.ACTION_VIEW, result);
      startActivity(intent);
      finish();
    }
  }
}
