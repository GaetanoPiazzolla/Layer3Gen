package com.gae.piaz.autogen.model;

import javax.persistence.*;
import lombok.Data;

@Data
@Entity(name = "com.gae.piaz.autogen.entities.Books")
@Table(name = "books")
public class Books {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "\"book_id\"", nullable = false)
  private Integer bookId;
  @Column(name = "\"title\"", nullable = false)
  private String title;
  @Column(name = "\"author\"", nullable = false)
  private String author;
  @Column(name = "\"isbn\"", nullable = false)
  private String isbn;
  @Column(name = "\"year\"", nullable = false)
  private Integer year;

}