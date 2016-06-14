package com.example.jpa.service.apps.secondapp;

import com.example.jpa.service.apps.AppProfile;
import com.example.jpa.service.apps.ITestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.logging.Logger;

/**
 * A simple Service class
 */
@Service
@Profile(AppProfile.SECOND)
public class TestService {

  private Logger log = Logger.getLogger(TestService.class.getName());


  private static final String VALUE = "value2";
  private ITestController controller;

  @Autowired
  public TestService(ITestController controller) {
    this.controller = controller;
  }

  @PostConstruct
  public void doTests() {
    // TODO: contributors please
    testReadUncommitted();
    testReadCommited();
    testRepeatableReads();
    // TODO: ~ contributors please

    // what is implemented already
    testSerializable();


  }
  private void testRepeatableReads() {
    //TODO: implement me
  }

  private void testReadCommited() {
    //TODO: implement me
  }

  private void testReadUncommitted() {
    //TODO: implement me
  }

  private void testSerializable() {
    log.info("START of ISOLATION SERIALIZABLE test");
    log.info(String.format("Before starting changes we have these records in DB: %s", controller.findAll()));
    log.info("Step1: changing records ranging Values from-to  [10-20]");
    try {
      controller.updateRecords(10, 15, VALUE);
      log.severe("Serialization test failed, no serialization anomaly was generated when it was actually expected");
    } catch (CannotAcquireLockException e) {
      log.info(String.format("Caught expected serialize isolation anomaly (%s)", e.getClass().getCanonicalName()));
    }
    log.info("END of ISOLATION SERIALIZABLE test");
  }

}
