package org.bookshop.book;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepo) {

        this.bookRepository = bookRepo;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findFirst50By()
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
        int BATCH_SIZE = 1000;
        int numberOfBatches = (int) Math.ceil(((double) books.size() / BATCH_SIZE));
        for (int index = 0; index < numberOfBatches; index++) {
            int endIndex = index * BATCH_SIZE + BATCH_SIZE;
            if (endIndex > books.size()) {
                endIndex = books.size();
            }
            List<BookEntity> bookEntities = books.subList(index * BATCH_SIZE, endIndex).stream().map(
                    book -> new BookEntity(
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
                    )
            ).toList();
            List<BookEntity> booksToBeInserted = getNewBooks(bookEntities);
            if (booksToBeInserted.isEmpty()) {
                System.out.printf("No new records in batch %d, skipping it. \n", (index + 1));
                continue;
            }
            try {
                bookRepository.saveAll(booksToBeInserted);
                saveSuccessCount += booksToBeInserted.size();
                System.out.printf("Successfully inserted batch %d of %d ", (index + 1), numberOfBatches);
            } catch (Exception e) {
                System.out.printf("Exception while inserting batch %d, error: %s \n", (index + 1), e);
                throw e;
            }
        }
        return saveSuccessCount;
    }

    private List<BookEntity> getNewBooks(List<BookEntity> books) {
        List<IsbnOnly> existingBooksIsbnList = bookRepository.findBookEntitiesByIsbnIn(books.stream().map(bookEntity -> bookEntity.isbn).toList());
        Map<String, Boolean> existingBooksIsbnMap = new HashMap<>();
        System.out.printf("Found %d duplicate ISBNs in input data\n", existingBooksIsbnList.size());
        existingBooksIsbnList.forEach(isbnOnly -> existingBooksIsbnMap.put(isbnOnly.getIsbn(), true));
        List<BookEntity> newBooks = new ArrayList<>();
        books.forEach(bookEntity -> {
            if (!existingBooksIsbnMap.containsKey(bookEntity.isbn)) {
                newBooks.add(bookEntity);
            }
        });
        System.out.printf("Found %d new books\n", newBooks.size());
        return newBooks;
    }

    public List<Book> searchBook(String query) {
        return bookRepository.search(query)
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

    public Book getBookById(String bookId) {
        BookEntity bookEntity = bookRepository.findBookEntityById(bookId);
        return new Book(
                bookEntity.id,
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
        );
    }

    public boolean isBookExist(String bookId) {
        return bookRepository.existsById(bookId);
    }

    public void reduceBookQuantityBy(String bookid, Integer count) {
        BookEntity book = this.bookRepository.findBookEntityById(bookid);
        book.quantity -= count;
        if (book.quantity < 0) book.quantity = 0;
        bookRepository.saveAndFlush(
                book
        );
    }
}
