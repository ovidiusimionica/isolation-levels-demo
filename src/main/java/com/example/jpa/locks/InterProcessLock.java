package com.example.jpa.locks;

/**
 * Same host system wide locking tool.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileLock;
import java.util.logging.Logger;

public class InterProcessLock {

  private Logger log = Logger.getLogger(InterProcessLock.class.getName());

  private final FileOutputStream outputStream;
  private FileLock fileLock;

  public InterProcessLock(File aFile) {
    try {
      outputStream = new FileOutputStream(aFile);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }

  }

  public void aquireLock() {
    assert (fileLock == null); //lock must not be acquired twice
    try {
      // time. Javadoc of FileLock states "File locks are held on behalf of the entire Java virtual machine. They are
      // not suitable for controlling access to a file by multiple threads within the same virtual machine." What this
      // actually means is that if two threads tries to get a lock on the same file, one fails with
      // OverlappingFileLockException instead of waiting for a release of the lock.
      synchronized (this) {
        // Synchronization doesn't have any effect on threads running on a different JVM, of
        // course. FileChannel.lock() acquires a lock on a file that prevents another process from getting a
        // lock on it. The method waits until a lock is released if the lock is held by a different process.
        fileLock = outputStream.getChannel().lock();
        log.info("Got system wide lock ");
      }
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  public void releaseLock() {
    assert (fileLock != null); //lock must not be released twice
    try {
      synchronized (this) {
        fileLock.release();
        fileLock = null;
        log.info("Released system wide lock ");
      }
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  @Override
  protected void finalize() throws Throwable {
    outputStream.close();
    super.finalize();
  }
}
