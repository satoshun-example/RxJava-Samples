package com.github.satoshun.example.rxjava.sample;

import android.support.v7.widget.RecyclerView;

import com.trello.rxlifecycle2.android.ActivityEvent;

public abstract class LifeCycleAdapter<VH extends LifeCycleViewHolder> extends RecyclerView.Adapter<VH> {

  @Override public void onViewAttachedToWindow(VH holder) {
    super.onViewAttachedToWindow(holder);
    holder.nextEvent(ActivityEvent.START);
  }

  @Override public void onViewDetachedFromWindow(VH holder) {
    super.onViewDetachedFromWindow(holder);
    holder.nextEvent(ActivityEvent.STOP);
  }
}
