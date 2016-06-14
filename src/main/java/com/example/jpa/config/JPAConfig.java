package com.example.jpa.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

/**
 *
 */
@EnableJpaRepositories(entityManagerFactoryRef = "defaultEntityManagerFactory",
  transactionManagerRef = "defaultTransactionManager",
  basePackages = {"com.example.jpa.service.repository"})
@EnableTransactionManagement
@Configuration
public class JPAConfig {

  @Primary
  @Bean
  @ConfigurationProperties(prefix = "spring.datasource")
  public DataSource defaultDataSource() {
    return DataSourceBuilder.create().build();
  }

  @Primary
  @Bean(name="defaultEntityManagerFactory")
  public LocalContainerEntityManagerFactoryBean defaultEntityManagerFactory(EntityManagerFactoryBuilder builder,
                                                                            @Qualifier("defaultDataSource") DataSource dataSource) {
    LocalContainerEntityManagerFactoryBean vhp = builder
      .dataSource(dataSource)
      .packages("com.example.jpa.service.model")
      .persistenceUnit("postgres")
      .build();
    vhp.setJpaDialect(new MyDialect());
    return vhp;
  }

  @Primary
  @Bean(name="defaultTransactionManager")
  public PlatformTransactionManager defaultTransactionManager(@Qualifier("defaultEntityManagerFactory") EntityManagerFactory defaultEntityManager) {
    return new JpaTransactionManager(defaultEntityManager);
  }




}
