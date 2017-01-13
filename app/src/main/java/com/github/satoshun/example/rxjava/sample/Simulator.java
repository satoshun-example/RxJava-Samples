package com.github.satoshun.example.rxjava.sample;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public final class Simulator {

  public static Single<FakeUser> singleSlowNetwork() {
    return Single.fromCallable(() -> new FakeUser("slow network"))
        .delay(5, TimeUnit.SECONDS)
        .subscribeOn(Schedulers.io());
  }

  public static Observable<FakeUser> observableSlowNetwork() {
    return singleSlowNetwork().toObservable();
  }

  public static Single<FakeUser> singleSlowNetwork(Object original) {
    return Single.fromCallable(() -> new FakeUser(String.valueOf(original)))
        .delay(5, TimeUnit.SECONDS);
  }

  public static Observable<FakeUser> observableSlowNetwork(Object original) {
    return singleSlowNetwork(original).toObservable();
  }
}
