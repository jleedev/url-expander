package com.jleedev;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class TestUrlsActivity extends Activity implements View.OnClickListener {
  static final String TEST_URL = "https://t.co/VYujTFaK";

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    LinearLayout view = new LinearLayout(this);
    setContentView(view);
    Button button1 = new Button(this);
    button1.setText(TEST_URL);
    button1.setOnClickListener(this);
    view.addView(button1);
  }

  @Override
  public void onClick(View view) {
    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(TEST_URL));
    intent.setClass(this, ExpandUrlActivity.class);
    startActivity(intent);
  }
}
