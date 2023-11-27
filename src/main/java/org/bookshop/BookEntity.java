package org.bookshop;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "books")
public class BookEntity {


    int quantity;
    double price;
    @Id
    @Column(name = "id")
    String Id;

    String title;

    String author;

    public BookEntity(String id,
                      String title,
                      String author,
                      double price,
                      int quantity)  {
        this.Id = id;
        this.title = title;
        this.author = author;
        this.price = price;
        this.quantity = quantity;
    }


    public BookEntity() {
    }
}
