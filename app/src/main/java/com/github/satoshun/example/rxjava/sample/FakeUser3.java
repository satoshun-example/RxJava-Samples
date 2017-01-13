package com.github.satoshun.example.rxjava.sample;

public class FakeUser3 {

  private final String name;

  public FakeUser3(String name) {
    this.name = name;
  }

  @Override public String toString() {
    return "{user3=" + name + "}";
  }
}
