package com.example.jpa.service.apps;

import com.example.jpa.service.testdomain.model.TestEntity;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 */
public interface ITestController {
  List<TestEntity> findAll();

  @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
  void updateRecords(long from, long to, String value);
}
