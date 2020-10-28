package com.example.demo.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
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