package com.jleedev;

import android.R;
import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TestUrlsActivity extends ListActivity {
  static final Uri[] TEST_URLS = new Uri[]{
          Uri.parse("http://t.co/N2Ivhj0O"),
          Uri.parse("https://t.co/4LzOmUQ9"),
          Uri.parse("http://t.co/Yflsf54M"),
          Uri.parse("http://feedproxy.google.com/~r/GooglePublicPolicyBlog/~3/wDlEpK19UW8/explore-mandelas-archives-online.html"),
  };

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setListAdapter(new ArrayAdapter<Uri>(this, R.layout.simple_list_item_1, TEST_URLS));
  }

  @Override
  public void onListItemClick(ListView l, View v, int position, long id) {
    Intent intent = new Intent(Intent.ACTION_VIEW, TEST_URLS[position]);
    intent.setClass(this, ExpandUrlActivity.class);
    startActivity(intent);
  }
}
