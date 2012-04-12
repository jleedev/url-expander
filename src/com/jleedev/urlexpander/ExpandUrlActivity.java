package com.jleedev.urlexpander;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import java.net.HttpURLConnection;
import java.net.URL;

public class ExpandUrlActivity extends Activity {

  static final String URL_ORIGINAL = "com.jleedev.urlexpander.EXTRA_URL_ORIGINAL";

  ExpandUrlTask mExpandUrlTask;
  UrlCache cache;
  UrlCleaner urlCleaner;
  String urlOriginal;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    cache = new UrlCache(this);
    urlCleaner = new UrlCleaner();
    Uri uri = getIntent().getData();
    urlOriginal = getIntent().getStringExtra(URL_ORIGINAL);
    if (urlOriginal == null) {
      urlOriginal = uri.toString();
    } else if (urlOriginal.equals(uri.toString())) {
      giveUp(uri);
      return;
    }
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
    if (mExpandUrlTask != null) {
      mExpandUrlTask.cancel(true);
    }
  }

  void giveUp(Uri uri) {
    startActivity(Intent.createChooser(getIntent(), null));
    finish();
  }

  Uri resolve(Uri uri) {
    Uri result = cache.findUri(uri);
    if (result != null) {
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
      try {
        if (false) {
          // Convince java that this exception might be thrown.
          throw new InterruptedException();
        }
        Uri result = resolve(uri);
        if (result == null) {
          return uri;
        }
        result = urlCleaner.cleanUp(result);
        return result;
      } catch (InterruptedException e) {
        return uri;
      }
    }

    @Override
    protected void onPostExecute(Uri result) {
      Intent intent = new Intent(Intent.ACTION_VIEW, result);
      intent.putExtra(URL_ORIGINAL, urlOriginal);
      startActivity(intent);
      finish();
    }
  }
}
