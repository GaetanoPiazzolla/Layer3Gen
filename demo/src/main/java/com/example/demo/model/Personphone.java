package com.example.demo.model;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity(name = "com.gae.piaz.autogen.entities.Personphone")
@Table(name = "personphone")
public class Personphone {

  @Column(name = "\"businessentityid\"", nullable = false)
  private Integer businessentityid;
  @Id
  @Column(name = "\"phonenumber\"", nullable = false)
  private Integer phonenumber;
  @Column(name = "\"phonenumbertypeid\"", nullable = false)
  private Integer phonenumbertypeid;
  @Column(name = "\"modifieddate\"", nullable = false)
  private Timestamp modifieddate;
}