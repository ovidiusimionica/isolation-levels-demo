package com.example.jpa.service.apps;

import com.example.jpa.service.testdomain.model.TestEntity;
import com.example.jpa.service.testdomain.repository.TestRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for Test Controller.
 */
public abstract class TestControllerBase implements ITestController {

  protected TestRepository repo;

  public TestControllerBase(TestRepository repo) {
    this.repo = repo;
  }

  public List<TestEntity> findAll() {
    return findAllInternal();
  }

  protected List<TestEntity> findAllInternal() {
    List<TestEntity> returnList = new ArrayList<>();
    repo.findAll().forEach(returnList::add);
    return returnList;
  }

  protected List<TestEntity> findBetween(long min, long max) {
    List<TestEntity> returnList = new ArrayList<>();
    repo.findBetween(min, max).forEach(returnList::add);
    return returnList;
  }

  protected void sleep() {
    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
