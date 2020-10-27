package com.autogen.example.model;

import javax.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity(name = "com.gae.piaz.autogen.entities.Carts")
@Table(name = "carts")
@IdClass(Carts.PrimaryKeys.class)
public class Carts {
  @Data
  public static class PrimaryKeys implements Serializable {
    private Integer userId;
    private Integer bookId;
  }

  @Id
  @Column(name = "\"user_id\"", nullable = false)
  private Integer userId;
  @Id
  @Column(name = "\"book_id\"", nullable = false)
  private Integer bookId;
  @Column(name = "\"quantity\"", nullable = false)
  private Integer quantity;
}