package com.gae.piaz.autogen.model;

import javax.persistence.*;
import lombok.Data;

@Data
@Entity(name = "com.gae.piaz.autogen.entities.Orders")
@Table(name = "orders")
public class Orders {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "\"order_id\"", nullable = false)
  private Integer orderId;
  @Column(name = "\"user_id\"", nullable = false)
  private Integer userId;
  @Column(name = "\"book_id\"", nullable = false)
  private Integer bookId;
  @Column(name = "\"quantity\"", nullable = false)
  private Integer quantity;
}