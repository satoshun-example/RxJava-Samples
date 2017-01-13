package com.github.satoshun.example.rxjava.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.TextView;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.ActivityEvent;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class RecyclerViewActivity extends AppCompatActivity {

  static Intent callingIntent(Context context) {
    Intent intent = new Intent(context, RecyclerViewActivity.class);
    return intent;
  }

  private RecyclerView recyclerView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.recycler_view_act);

    recyclerView = (RecyclerView) findViewById(R.id.infinite);
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setAdapter(new Adapter());
  }

  static class Adapter extends RecyclerView.Adapter<ThisViewHolder> {

    @Override public ThisViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      ThisViewHolder holder = new ThisViewHolder(new TextView(parent.getContext()));
      holder.lifecycle(ActivityEvent.CREATE);
      return holder;
    }

    @Override public void onBindViewHolder(ThisViewHolder holder, int position) {
      holder.bind(String.valueOf(position));
    }

    @Override public int getItemCount() {
      return Integer.MAX_VALUE;
    }

    @Override public void onViewAttachedToWindow(ThisViewHolder holder) {
      super.onViewAttachedToWindow(holder);
      holder.lifecycle(ActivityEvent.START);
    }

    @Override public void onViewDetachedFromWindow(ThisViewHolder holder) {
      super.onViewDetachedFromWindow(holder);
      holder.lifecycle(ActivityEvent.STOP);
    }
  }

  static class ThisViewHolder extends RecyclerView.ViewHolder {

    private final PublishSubject<ActivityEvent> lifecycle = PublishSubject.create();
    private final TextView root;

    ThisViewHolder(TextView itemView) {
      super(itemView);
      root = itemView;
    }

    void lifecycle(ActivityEvent event) {
      lifecycle.onNext(event);
    }

    void bind(String data) {
      Simulator.observableSlowNetwork(data)
          .compose(bindUntilEvent(ActivityEvent.STOP))
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .doOnSubscribe(d -> root.setText(""))
          .map(FakeUser::toString)
          .subscribe(root::setText);
    }

    protected <T> LifecycleTransformer<T> bindUntilEvent(ActivityEvent event) {
      return RxLifecycle.bindUntilEvent(lifecycle, event);
    }
  }
}
