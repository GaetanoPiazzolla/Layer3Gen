package com.example.demo.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;

/**
 * The persistent class for the books database table.
 * 
 */
@Entity
@Table(name="books")
@NamedQuery(name="Book.findAll", query="SELECT b FROM Book b")
public class Book implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="BOOKS_BOOKID_GENERATOR", sequenceName="BOOKS_BOOKS_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="BOOKS_BOOKID_GENERATOR")
	@Column(name="book_id")
	private Integer bookId;

	private String author;

	private String isbn;

	private String title;

	private Integer year;

	//bi-directional many-to-one association to Order
	@OneToMany(mappedBy="book")
	private List<Order> orders;

	public Book() {
	}

	public Integer getBookId() {
		return this.bookId;
	}

	public void setBookId(Integer bookId) {
		this.bookId = bookId;
	}

	public String getAuthor() {
		return this.author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getIsbn() {
		return this.isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getYear() {
		return this.year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public List<Order> getOrders() {
		return this.orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	public Order addOrder(Order order) {
		getOrders().add(order);
		order.setBook(this);

		return order;
	}

	public Order removeOrder(Order order) {
		getOrders().remove(order);
		order.setBook(null);

		return order;
	}

}