package com.github.satoshun.example.rxjava.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

public class CachedNetworkWithSubjectActivity extends AppCompatActivity {

  static Intent callingIntent(Context context) {
    Intent intent = new Intent(context, CachedNetworkWithSubjectActivity.class);
    return intent;
  }

  private final BehaviorSubject<FakeUser> cachedItem = BehaviorSubject.create();

  private SimpleAdapter adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.cached_network_act);

    RecyclerView view = (RecyclerView) findViewById(R.id.log);
    view.setLayoutManager(new LinearLayoutManager(this));
    adapter = new SimpleAdapter();
    view.setAdapter(adapter);

    cachedItem();
    cachedItem2();
  }

  private void cachedItem() {
    findViewById(R.id.cached_item_network).setOnClickListener(v ->
        Single.amb(Arrays.asList(cachedItem.firstOrError(), Simulator.singleSlowNetwork()))
            .map(FakeUser::toString)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess(user -> cachedItem.onNext(new FakeUser("cached:" + user)))
            .subscribe(adapter::add));
  }

  private void cachedItem2() {
    findViewById(R.id.cached_item_network2).setOnClickListener(v ->
        Observable.concat(cachedItem.timeout(1, TimeUnit.MILLISECONDS).onErrorResumeNext(Observable.empty()), Simulator.observableSlowNetwork())
            .firstElement()
            .map(FakeUser::toString)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess(user -> cachedItem.onNext(new FakeUser("cached2:" + user)))
            .subscribe(adapter::add));
  }
}
