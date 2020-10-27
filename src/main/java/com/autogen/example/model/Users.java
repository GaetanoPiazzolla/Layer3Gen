package com.autogen.example.model;

import javax.persistence.*;
import lombok.Data;

@Data
@Entity(name = "com.gae.piaz.autogen.entities.Users")
@Table(name = "users")
public class Users {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "\"user_id\"", nullable = false)
  private Integer userId;
  @Column(name = "\"first_name\"", nullable = false)
  private String firstName;
  @Column(name = "\"last_name\"", nullable = false)
  private String lastName;
}