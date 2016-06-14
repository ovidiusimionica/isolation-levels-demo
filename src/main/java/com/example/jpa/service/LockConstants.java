package com.example.jpa.service;

import java.io.File;

/**
 */
public interface LockConstants {
  File LOCK_2 = new File("trylock.test");
  File LOCK_1 = new File("lock.test");
}
