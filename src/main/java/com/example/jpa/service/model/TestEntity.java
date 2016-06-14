package com.example.jpa.service.model;

import javax.persistence.*;

/**
 */
@Entity
@Table(name = "TEST")
public class TestEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID", columnDefinition = "serial")
  private Long id;

  @Column(name = "VALUE")
  private String value;


  public TestEntity() {
  }

  public TestEntity(String value) {
    this.value = value;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return "TestEntity{" +
      "id=" + id +
      ", value='" + value + '\'' +
      '}';
  }
}
