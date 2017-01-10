package com.github.satoshun.example.rxjava.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main_act);

    // todo use RxBinding
    findViewById(R.id.cached_network).setOnClickListener(v -> startActivity(CachedNetworkActivity.callingIntent(this)));
  }
}
