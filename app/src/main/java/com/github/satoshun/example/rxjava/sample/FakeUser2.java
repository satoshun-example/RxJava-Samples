package com.github.satoshun.example.rxjava.sample;

public class FakeUser2 {

  private final String name;

  public FakeUser2(String name) {
    this.name = name;
  }

  @Override public String toString() {
    return "{user2=" + name + "}";
  }
}
