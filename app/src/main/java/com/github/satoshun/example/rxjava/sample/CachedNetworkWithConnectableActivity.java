package com.github.satoshun.example.rxjava.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.schedulers.Schedulers;

public class CachedNetworkWithConnectableActivity extends AppCompatActivity {

  static Intent callingIntent(Context context) {
    Intent intent = new Intent(context, CachedNetworkWithConnectableActivity.class);
    return intent;
  }

  private ConnectableObservable<String> cached;
  private SimpleAdapter adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.cached_network_with_connectable_act);

    RecyclerView view = (RecyclerView) findViewById(R.id.log);
    view.setLayoutManager(new LinearLayoutManager(this));
    adapter = new SimpleAdapter();
    view.setAdapter(adapter);

    cached = Simulator.observableSlowNetwork()
        .map(FakeUser::hashCode)
        .map(String::valueOf)
        .subscribeOn(Schedulers.io())
        .replay();
    cached.connect();

    findViewById(R.id.add).setOnClickListener(v -> add());
  }

  private void add() {
    cached
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(adapter::add);
  }
}
