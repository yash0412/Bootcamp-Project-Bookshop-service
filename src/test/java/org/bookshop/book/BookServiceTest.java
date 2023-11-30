package org.bookshop.book;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.List;


class mockISBnOnly implements IsbnOnly {

    private final String isbn;

    mockISBnOnly(String isbn) {
        this.isbn = isbn;
    }

    @Override
    public String getIsbn() {
        return isbn;
    }
}

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    BookRepository bookRepository;

    @Test
    void shouldGetAllBooksFromRepository() {
        Mockito.when(bookRepository.findFirst50By())
                .thenReturn(List.of(new BookEntity("someid",
                        "ISBN",
                        "Think And Grow Rich",
                        "Description",
                        "Nepolean Hill",
                        "1980",
                        120.00, 50, "imageUrl", "shortURL", 4.5)));

        BookService bookService = new BookService(bookRepository);

        List<Book> expectedBooks = List.of(new Book("someid",
                "ISBN",
                "Think And Grow Rich",
                "Description",
                "Nepolean Hill",
                "1980",
                120.00, 50, "imageUrl", "shortURL", 4.5));
        List<Book> allBooks = bookService.getAllBooks();

        Assertions.assertThat(allBooks).containsAll(expectedBooks);
    }

    @Test
    void shouldGetBookByBookIdFromRepository() {
        List<String> bookList = new ArrayList<>();
        bookList.add("b0ca96a9-d5d6-4f8b-8353-8d81e2222747");
        Mockito.when(bookRepository.findBookEntitiesByidIn(bookList))
                .thenReturn(List.of(new BookEntity("someid",
                        "ISBN",
                        "Think And Grow Rich",
                        "Description",
                        "Nepolean Hill",
                        "1980",
                        120.00, 50, "imageUrl", "shortURL", 4.5)));


        BookService bookService = new BookService(bookRepository);

        List<Book> expectedBooks = List.of(new Book("someid",
                "ISBN",
                "Think And Grow Rich",
                "Description",
                "Nepolean Hill",
                "1980",
                120.00, 50, "imageUrl", "shortURL", 4.5));
        List<Book> allBooks = bookService.getBooks(bookList);

        Assertions.assertThat(allBooks).containsAll(expectedBooks);
    }

    @Test
    void shouldLoadAllBooksSuccessfullyAndReturnCount() {
        List<Book> books = List.of(new Book("someid",
                "ISBN",
                "Think And Grow Rich",
                "Description",
                "Nepolean Hill",
                "1980",
                120.00, 50, "imageUrl", "shortURL", 4.5), new Book("someid",
                "ISBN",
                "Think And Grow Rich",
                "Description",
                "Nepolean Hill",
                "1980",
                120.00, 50, "imageUrl", "shortURL", 4.5), new Book("someid",
                "ISBN",
                "Think And Grow Rich",
                "Description",
                "Nepolean Hill",
                "1980",
                120.00, 50, "imageUrl", "shortURL", 4.5));
        Mockito.when(bookRepository.findBookEntitiesByIsbnIn(Mockito.any()))
                .thenReturn(List.of());
        Mockito.when(bookRepository.saveAll(Mockito.any()))
                .thenReturn(List.of());
        BookService bookService = new BookService(bookRepository);
        int saveCount = bookService.loadBooks(books);
        Assertions.assertThat(saveCount).isEqualTo(3);

    }

    @Test
    void shouldLoadOtherBooksExceptOnesWithDuplicateISBN() {
        List<Book> books = List.of(new Book("someid",
                        "ISBN1",
                        "Think And Grow Rich",
                        "Description",
                        "Nepolean Hill",
                        "1980",
                        120.00, 50, "imageUrl", "shortURL", 4.5),
                new Book("someid1",
                        "ISBN2",
                        "Think And Grow Rich",
                        "Description",
                        "Nepolean Hill",
                        "1980",
                        120.00, 50, "imageUrl", "shortURL", 4.5),
                new Book("someid2",
                        "ISBN3",
                        "Think And Grow Rich",
                        "Description",
                        "Nepolean Hill",
                        "1980",
                        120.00, 50, "imageUrl", "shortURL", 4.5));

        Mockito.when(bookRepository.findBookEntitiesByIsbnIn(Mockito.any()))
                .thenReturn(List.of(new mockISBnOnly("ISBN2")));
        Mockito.when(bookRepository.saveAll(Mockito.any()))
                .thenReturn(List.of());
        BookService bookService = new BookService(bookRepository);
        int saveCount = bookService.loadBooks(books);
        Assertions.assertThat(saveCount).isEqualTo(2);
    }

    @Test
    void shouldNotLoadAnyBooksWhenNoNewBooksAreProvided() {
        List<Book> books = List.of(new Book("someid",
                        "ISBN1",
                        "Think And Grow Rich",
                        "Description",
                        "Nepolean Hill",
                        "1980",
                        120.00, 50, "imageUrl", "shortURL", 4.5),
                new Book("someid1",
                        "ISBN2",
                        "Think And Grow Rich",
                        "Description",
                        "Nepolean Hill",
                        "1980",
                        120.00, 50, "imageUrl", "shortURL", 4.5),
                new Book("someid2",
                        "ISBN3",
                        "Think And Grow Rich",
                        "Description",
                        "Nepolean Hill",
                        "1980",
                        120.00, 50, "imageUrl", "shortURL", 4.5));

        Mockito.when(bookRepository.findBookEntitiesByIsbnIn(Mockito.any()))
                .thenReturn(List.of(new mockISBnOnly("ISBN1"), new mockISBnOnly("ISBN2"), new mockISBnOnly("ISBN3")));
        BookService bookService = new BookService(bookRepository);
        int saveCount = bookService.loadBooks(books);
        Assertions.assertThat(saveCount).isEqualTo(0);
    }

    @Test
    void shouldThrowExceptionWhenExceptionOccursWhileSavingBooks() {
        List<Book> books = List.of(new Book("someid",
                        "ISBN1",
                        "Think And Grow Rich",
                        "Description",
                        "Nepolean Hill",
                        "1980",
                        120.00, 50, "imageUrl", "shortURL", 4.5),
                new Book("someid1",
                        "ISBN2",
                        "Think And Grow Rich",
                        "Description",
                        "Nepolean Hill",
                        "1980",
                        120.00, 50, "imageUrl", "shortURL", 4.5),
                new Book("someid2",
                        "ISBN3",
                        "Think And Grow Rich",
                        "Description",
                        "Nepolean Hill",
                        "1980",
                        120.00, 50, "imageUrl", "shortURL", 4.5));

        Mockito.when(bookRepository.findBookEntitiesByIsbnIn(Mockito.any()))
                .thenReturn(List.of());
        Mockito.when(bookRepository.saveAll(Mockito.any()))
                .thenThrow(new DataIntegrityViolationException("error"));
        BookService bookService = new BookService(bookRepository);
        Assertions.assertThatThrownBy(() -> bookService.loadBooks(books)).isInstanceOf(DataIntegrityViolationException.class);
    }

}
