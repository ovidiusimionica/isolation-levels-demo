package com.example.jpa.service.testdomain.repository;

import com.example.jpa.service.testdomain.model.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * A JPA repo
 */
public interface TestRepository extends JpaRepository<TestEntity, Long> {

  TestEntity findByValue(String value);

  @Query(value = "from TestEntity t where t.id > :minim and t.id < :maxim")
  Iterable<TestEntity> findBetween(@Param("minim")long from, @Param("maxim")long to);

  @Modifying
  @Query(value = "UPDATE TestEntity t set value=:value where t.id > :minim and t.id < :maxim")
  void updateBetween(@Param("minim")long from, @Param("maxim")long to, @Param("value")String value);
}
