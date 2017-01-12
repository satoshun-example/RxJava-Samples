package com.github.satoshun.example.rxjava.sample;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public final class Simulator {

  public static Single<FakeUser> singleSlowNetwork() {
    return Single.fromCallable(() -> {
      Thread.sleep(5000);
      return new FakeUser("slow network");
    }).subscribeOn(Schedulers.io());
  }

  public static Observable<FakeUser> observableSlowNetwork() {
    return singleSlowNetwork().toObservable();
  }
}
