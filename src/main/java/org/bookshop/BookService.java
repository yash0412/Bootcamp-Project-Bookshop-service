package org.bookshop;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepo) {

        this.bookRepository = bookRepo;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(bookEntity -> new Book(bookEntity.id,
                        bookEntity.isbn,
                        bookEntity.title,
                        bookEntity.description,
                        bookEntity.author,
                        bookEntity.publicationYear,
                        bookEntity.price,
                        bookEntity.quantity,
                        bookEntity.imageUrl,
                        bookEntity.shortImageUrl,
                        bookEntity.averageRating

                ))
                .toList();
    }

    public List<Book> getBooks(List<String> bookIdList) {
        return bookRepository.findBookEntitiesByidIn(bookIdList)
                .stream()
                .map(bookEntity -> new Book(bookEntity.id,
                        bookEntity.isbn,
                        bookEntity.title,
                        bookEntity.description,
                        bookEntity.author,
                        bookEntity.publicationYear,
                        bookEntity.price,
                        bookEntity.quantity,
                        bookEntity.imageUrl,
                        bookEntity.shortImageUrl,
                        bookEntity.averageRating))
                .toList();
    }

    public int loadBooks(List<Book> books) {
        int saveSuccessCount = 0;
        for (Book book : books) {
            try {
                bookRepository.save(new BookEntity(
                        book.id(),
                        book.isbn(),
                        book.title(),
                        book.description(),
                        book.author(),
                        book.publicationYear(),
                        book.price(),
                        book.quantity(),
                        book.imageUrl(),
                        book.shortImageUrl(),
                        book.averageRating()
                ));
                saveSuccessCount++;
            } catch (DataIntegrityViolationException e) {
                System.out.println(e);
            } catch (Exception e) {
                throw e;
            }
        }
        return saveSuccessCount;
    }
}
