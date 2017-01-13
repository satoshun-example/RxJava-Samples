package com.github.satoshun.example.rxjava.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class RxBusActivity extends AppCompatActivity {

  static RxBus rxBus = new RxBus(); // use di and singleton

  static Intent callingIntent(Context context) {
    Intent intent = new Intent(context, RxBusActivity.class);
    return intent;
  }

  private final CompositeDisposable disposables = new CompositeDisposable();

  private SimpleAdapter adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.rx_bus_act);

    RecyclerView view = (RecyclerView) findViewById(R.id.log);
    view.setLayoutManager(new LinearLayoutManager(this));
    adapter = new SimpleAdapter();
    view.setAdapter(adapter);

    rxBus.receive()
        .ofType(FakeUser.class)
        .map(FakeUser::toString)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(adapter::add);
    rxBus.receive()
        .ofType(FakeUser2.class)
        .map(FakeUser2::toString)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(adapter::add);

    Observable<Long> interval = Observable.interval(1, TimeUnit.SECONDS)
        .subscribeOn(Schedulers.io())
        .share();
    disposables.add(interval
        .filter(v -> v % 3 == 0)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(v -> rxBus.send(new FakeUser2(String.valueOf(v)))));
    disposables.add(interval
        .filter(v -> v % 5 == 0)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(v -> rxBus.send(new FakeUser(String.valueOf(v)))));
    disposables.add(interval
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(v -> rxBus.send(v)));
  }

  @Override protected void onDestroy() {
    disposables.clear();
    super.onDestroy();
  }
}
