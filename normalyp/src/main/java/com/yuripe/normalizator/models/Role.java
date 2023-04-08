package com.yuripe.normalizator.models;

import javax.persistence.*;

@Entity
@Table(name = "roles", schema = "Config")
public class Role {
  @Id
  @Column(name = "roleId")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer roleId;

  @Enumerated(EnumType.STRING)
  @Column(length = 20)
  private ERole name;

  public Role() {

  }

  public Role(ERole name) {
    this.name = name;
  }

  public Integer getId() {
    return roleId;
  }

  public void setId(Integer id) {
    this.roleId = id;
  }

  public ERole getName() {
    return name;
  }

  public void setName(ERole name) {
    this.name = name;
  }
}