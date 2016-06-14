package com.example.jpa.service.apps.secondapp;

import com.example.jpa.locks.InterProcessLock;
import com.example.jpa.locks.InterProcessTryLock;
import com.example.jpa.service.apps.AppProfile;
import com.example.jpa.service.apps.ITestController;
import com.example.jpa.service.apps.TestControllerBase;
import com.example.jpa.service.testdomain.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.logging.Logger;

import static com.example.jpa.locks.LockConstants.LOCK_1;
import static com.example.jpa.locks.LockConstants.LOCK_2;

/**
 * A simple controller , transaction support
 */
@Transactional(isolation = Isolation.READ_UNCOMMITTED)
@Component
@Profile(AppProfile.SECOND)
public class TestController extends TestControllerBase implements ITestController {

  private Logger log = Logger.getLogger(TestController.class.getName());


  @Autowired
  public TestController(TestRepository repo) {
    super(repo);
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
  public void updateRecords(long from, long to, String value) {
    InterProcessLock lock = new InterProcessLock(LOCK_2);
    lock.aquireLock();
    sleep();
    lock.releaseLock();

    repo.updateBetween(from, to, value);
    log.info("Writing done , but no commit ");
    log.info(String.format("Actual Values %s", findBetween(from, to)));

    InterProcessTryLock tryLock = new InterProcessTryLock(LOCK_1);
    tryLock.waitForTheOther();
  }

}
