package com.example.jpa.config;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.transaction.TransactionDefinition;

import javax.persistence.EntityManager;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Not used in this demo but I was trying this also to see if any difference
 */
public class MyDialect extends HibernateJpaDialect {

  @Override
  public Object beginTransaction(final EntityManager entityManager, final TransactionDefinition definition)
    throws SQLException {

    Session session = (Session) entityManager.getDelegate();
    if (definition.getTimeout() != TransactionDefinition.TIMEOUT_DEFAULT) {
      getSession(entityManager).getTransaction().setTimeout(definition.getTimeout());
    }

    final TransactionData data = new TransactionData();

    session.doWork(new Work() {
      @Override
      public void execute(Connection connection) throws SQLException {
        System.out.println("ISOLATION YES!" + definition.getIsolationLevel());
        Integer previousIsolationLevel =
          DataSourceUtils.prepareConnectionForTransaction(connection, definition);
        data.setPreviousIsolationLevel(previousIsolationLevel);
        data.setConnection(connection);
      }
    });

    entityManager.getTransaction().begin();

    Object springTransactionData = prepareTransaction(entityManager, definition.isReadOnly(), definition.getName());

    data.setSpringTransactionData(springTransactionData);

    return data;
  }

  @Override
  public void cleanupTransaction(Object transactionData) {
    super.cleanupTransaction(((TransactionData) transactionData).getSpringTransactionData());
    ((TransactionData) transactionData).resetIsolationLevel();
  }

  private static class TransactionData {

    private Object springTransactionData;
    private Integer previousIsolationLevel;
    private Connection connection;

    public TransactionData() {
    }

    public void resetIsolationLevel() {
      if (this.previousIsolationLevel != null) {
        DataSourceUtils.resetConnectionAfterTransaction(connection, previousIsolationLevel);
      }
    }

    public Object getSpringTransactionData() {
      return this.springTransactionData;
    }

    public void setSpringTransactionData(Object springTransactionData) {
      this.springTransactionData = springTransactionData;
    }

    public void setPreviousIsolationLevel(Integer previousIsolationLevel) {
      this.previousIsolationLevel = previousIsolationLevel;
    }

    public void setConnection(Connection connection) {
      this.connection = connection;
    }

  }

}
