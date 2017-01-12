package com.github.satoshun.example.rxjava.sample;

public final class FakeUser {

  private final String name;

  public FakeUser(String name) {
    this.name = name;
  }

  @Override public String toString() {
    return "{user=" + name + "}";
  }
}
