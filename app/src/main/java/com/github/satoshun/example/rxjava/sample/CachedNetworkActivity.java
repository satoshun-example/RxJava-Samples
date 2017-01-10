package com.github.satoshun.example.rxjava.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CachedNetworkActivity extends AppCompatActivity {

  static Intent callingIntent(Context context) {
    Intent intent = new Intent(context, CachedNetworkActivity.class);
    return intent;
  }

  private SimpleAdapter adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.cached_network_act);

    RecyclerView view = (RecyclerView) findViewById(R.id.log);
    view.setLayoutManager(new LinearLayoutManager(this));
    adapter = new SimpleAdapter();
    view.setAdapter(adapter);

    findViewById(R.id.cached_item_network)
        .setOnClickListener(v -> Observable.concat(fastCachedItem(), slowNetwork())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map(User::toString)
            .subscribe(adapter::add));
  }

  private static Observable<User> fastCachedItem() {
    return Observable.fromCallable(() -> {
      Thread.sleep(500);
      return new User("fast cached item");
    });
  }

  private static Observable<User> slowNetwork() {
    return Observable.fromCallable(() -> {
      Thread.sleep(5000);
      return new User("slow network");
    });
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
