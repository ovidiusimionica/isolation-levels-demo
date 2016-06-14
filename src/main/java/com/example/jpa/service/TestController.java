package com.example.jpa.service;

import com.example.InterProcessLock;
import com.example.jpa.InterProcessTryLock;
import com.example.jpa.service.model.TestEntity;
import com.example.jpa.service.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.example.jpa.service.LockConstants.LOCK_1;
import static com.example.jpa.service.LockConstants.LOCK_2;

/**
 * A simple controller , transaction support
 */
@Transactional(isolation = Isolation.READ_UNCOMMITTED)
@Component
public class TestController {

  private TestRepository repo;

  @Autowired
  public TestController(TestRepository repo) {
    this.repo = repo;
  }


  public List<TestEntity> findAll() {
    return findAllInternal();
  }

  private List<TestEntity> findAllInternal() {
    List<TestEntity> returnList = new ArrayList<>();
    repo.findAll().forEach(returnList::add);
    return returnList;
  }

  private List<TestEntity> findBetween(long min, long max) {
    List<TestEntity> returnList = new ArrayList<>();
    repo.findBetween(min, max).forEach(returnList::add);
    return returnList;
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_UNCOMMITTED)
  public void newRecord() {
    System.out.println("Writing a new Record , but no commit ");
    repo.saveAndFlush(new TestEntity("value"));
    System.out.println("Writing done , but no commit ");
    sleep();
    InterProcessLock lock = new InterProcessLock(LOCK_2);
    lock.aquireLock();
    sleep();
    lock.releaseLock();
    System.out.println("Writing done , commit ");
  }

  private void sleep() {
    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private void sleepAlittle() {
    try {
      Thread.sleep(500);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
  public void updateRecordsTransaction1(long from, long to, String value) {
    InterProcessTryLock tryLock = new InterProcessTryLock(LOCK_2);
    tryLock.waitForTheOther();
    repo.updateBetween(from, to, value);
    System.out.println("Writing done , but no commit ");
    System.out.println(String.format("Actual Values %s", findBetween(from, to)));
    sleep();
    // signal the other process to continue its transaction
    InterProcessLock lock = new InterProcessLock(LOCK_1);
    lock.aquireLock();
    sleep();
    lock.releaseLock();
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
  public void updateRecordsTransaction2(long from, long to, String value) {
    InterProcessLock lock = new InterProcessLock(LOCK_2);
    lock.aquireLock();
    sleep();
    lock.releaseLock();

    repo.updateBetween(from, to, value);
    System.out.println("Writing done , but no commit ");
    System.out.println(String.format("Actual Values %s", findBetween(from, to)));

    InterProcessTryLock tryLock = new InterProcessTryLock(LOCK_1);
    tryLock.waitForTheOther();
  }
}
