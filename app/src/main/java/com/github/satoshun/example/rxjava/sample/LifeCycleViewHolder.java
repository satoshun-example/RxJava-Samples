package com.github.satoshun.example.rxjava.sample;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.ActivityEvent;

import io.reactivex.subjects.PublishSubject;

public abstract class LifeCycleViewHolder<T> extends RecyclerView.ViewHolder {

  private final PublishSubject<ActivityEvent> lifecycle = PublishSubject.create();

  public LifeCycleViewHolder(View itemView) {
    super(itemView);
  }

  public abstract void bind(T data);

  public void nextEvent(ActivityEvent event) {
    lifecycle.onNext(event);
  }

  protected <T> LifecycleTransformer<T> bindUntilEvent(ActivityEvent event) {
    return RxLifecycle.bindUntilEvent(lifecycle, event);
  }
}
