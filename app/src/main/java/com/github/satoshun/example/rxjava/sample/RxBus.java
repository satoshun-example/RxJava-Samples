package com.github.satoshun.example.rxjava.sample;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class RxBus {

  // should use RxRelay?
  private final PublishSubject<Object> bus = PublishSubject.create();

  public void send(Object o) {
    bus.onNext(o);
  }

  public Observable<Object> receive() {
    return bus.hide();
  }
}
