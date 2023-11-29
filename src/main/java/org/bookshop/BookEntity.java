package org.bookshop;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "books")
public class BookEntity {


    String description;
    @Column(name = "averagerating")
    double averageRating;
    @Column(name = "shortimageurl")
    String shortImageUrl;
    @Column(name = "publicationyear")
    String publicationYear;
    @Column(name = "imageurl")
    String imageUrl;
    int quantity;
    double price;
    @Id
    @Column(name = "id")
    String id;

    String title;

    String author;
    String isbn;

    public BookEntity(String id, String isbn, String title, String description,
                      String author, String publicationYear, double price,
                      int quantity, String imageUrl, String shortImageUrl, double averageRating) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.description = description;
        this.author = author;
        this.publicationYear = publicationYear;
        this.price = price;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
        this.shortImageUrl = shortImageUrl;
        this.averageRating = averageRating;
    }


    public BookEntity() {
    }

}
