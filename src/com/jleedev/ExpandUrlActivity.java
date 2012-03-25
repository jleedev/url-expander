package com.jleedev;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import java.net.HttpURLConnection;
import java.net.URL;

public class ExpandUrlActivity extends Activity {

  private static final String TAG = "ExpandUrlActivity";
  private static final String USER_AGENT = "Android URL Expander";

  TextView outputLabel;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    Uri uri = getIntent().getData();
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    TextView uriLabel = (TextView) findViewById(R.id.uri);
    outputLabel = (TextView) findViewById(R.id.output_label);
    uriLabel.setText(uri.toString());
    new ExpandUrlTask().execute(uri);
  }

  class ExpandUrlTask extends AsyncTask<Uri, Void, String> {

    @Override
    protected String doInBackground(Uri... uris) {
      try {
        URL url = new URL(uris[0].toString());
        HttpURLConnection client = (HttpURLConnection) url.openConnection();
        client.setInstanceFollowRedirects(false);
        String location = client.getHeaderField("location");
        client.disconnect();
        return location;
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }

    @Override
    protected void onPostExecute(String result) {
      if (result != null) {
        outputLabel.setText(result);
      } else {
        outputLabel.setText("null");
      }
    }
  }
}
