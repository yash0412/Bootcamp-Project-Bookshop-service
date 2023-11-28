package org.bookshop;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    BookRepository bookRepository;

    @Test
    void shouldGetAllBooksFromRepository() {
        Mockito.when(bookRepository.findAll())
                .thenReturn(List.of(new BookEntity("someid",
                        "Think And Grow Rich",
                        "Nepolean Hill",
                        120.00, 50, "imageUrl")));

        BookService bookService = new BookService(bookRepository);

        List<Book> expectedBooks = List.of(new Book("someid",
                "Think And Grow Rich",
                "Nepolean Hill",
                120.00, 50, "imageUrl"));
        List<Book> allBooks = bookService.getAllBooks();

        Assertions.assertThat(allBooks).containsAll(expectedBooks);
    }

    @Test
    void shouldGetBookByBookIdFromRepository() {
        List<String> bookList = new ArrayList<>();
        bookList.add("b0ca96a9-d5d6-4f8b-8353-8d81e2222747");
        Mockito.when(bookRepository.findBookEntitiesByidIn(bookList))
                .thenReturn(List.of(new BookEntity("someid",
                        "Think And Grow Rich",
                        "Nepolean Hill",
                        120.00, 50, "imageUrl")));

        BookService bookService = new BookService(bookRepository);

        List<Book> expectedBooks = List.of(new Book("someid",
                "Think And Grow Rich",
                "Nepolean Hill",
                120.00, 50, "imageUrl"));
        List<Book> allBooks = bookService.getBooks(bookList);

        Assertions.assertThat(allBooks).containsAll(expectedBooks);
    }




}
