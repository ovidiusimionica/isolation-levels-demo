package com.example.jpa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * A simple Service class
 */
@Service
@Profile("second")
public class TestServiceProcess2 {

  private static final String VALUE = "value2";
  private TestController controller;

  @Autowired
  public TestServiceProcess2(TestController controller) {
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
    System.out.println("START of ISOLATION SERIALIZABLE test");
    System.out.println(String.format("Before starting changes we have these records in DB: %s", controller.findAll()));
    System.out.println("Step1: changing records ranging Values from-to  [10-20]");
    controller.updateRecordsTransaction2(10, 15, VALUE);
    System.out.println("END of ISOLATION SERIALIZABLE test");
  }

}
