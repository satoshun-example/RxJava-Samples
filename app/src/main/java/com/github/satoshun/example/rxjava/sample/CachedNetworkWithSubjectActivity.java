package com.github.satoshun.example.rxjava.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.Arrays;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

public class CachedNetworkWithSubjectActivity extends AppCompatActivity {

  static Intent callingIntent(Context context) {
    Intent intent = new Intent(context, CachedNetworkWithSubjectActivity.class);
    return intent;
  }

  private final BehaviorSubject<User> cachedItem = BehaviorSubject.create();

  private SimpleAdapter adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.cached_network_act);

    RecyclerView view = (RecyclerView) findViewById(R.id.log);
    view.setLayoutManager(new LinearLayoutManager(this));
    adapter = new SimpleAdapter();
    view.setAdapter(adapter);

    challengeCachedItem();
  }

  private void challengeCachedItem() {
    findViewById(R.id.cached_item_network).setOnClickListener(v ->
        Single.amb(Arrays.asList(cachedItem.firstOrError(), slowNetwork()))
            .map(User::toString)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess(user -> cachedItem.onNext(new User("cached:" + user)))
            .subscribe(adapter::add));
  }

  private static Single<User> slowNetwork() {
    return Single.fromCallable(() -> {
      try {
        Thread.sleep(5000);
      } catch (Exception e) {
        // ignore
      }
      return new User("slow network");
    }).subscribeOn(Schedulers.io());
  }

  private static class User {

    private final String name;

    private User(String name) {
      this.name = name;
    }

    @Override public String toString() {
      return "{user=" + name + "}";
    }
  }
}
