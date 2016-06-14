package com.example.jpa;

/**
 * Same host system wide locking tool.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;

public class InterProcessTryLock {


  private static final int MAX_ITERATIONS = 100;
  private final FileOutputStream outputStream;

  public InterProcessTryLock(File aFile) {
    try {
      outputStream = new FileOutputStream(aFile);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }

  }

  public void waitForTheOther() {
    System.out.println("Waiting for the other process...");
    FileLock fileLock;
    int iterations = 0;
    try {
      while (iterations++ < MAX_ITERATIONS)
      synchronized (this) {
        fileLock = outputStream.getChannel().tryLock();
        if (fileLock == null) {
          System.out.println("Detected the other process.");
          return;
        }
        fileLock.release();
        Thread.sleep(200);

      }
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    } catch (OverlappingFileLockException e) {
      System.out.println("Detected the other process.");
    }
  }

  @Override
  protected void finalize() throws Throwable {
    outputStream.close();
    super.finalize();
  }
}
