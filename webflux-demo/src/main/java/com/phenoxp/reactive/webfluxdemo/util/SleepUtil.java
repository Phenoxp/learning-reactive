package com.phenoxp.reactive.webfluxdemo.util;

public class SleepUtil {

  public static void sleepSeconds(int seconds) {
    try {
      Thread.sleep(seconds * 1000L);
    } catch (InterruptedException iep) {
      iep.printStackTrace();
    }
  }
}
