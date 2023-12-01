package org.bookshop.book;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bookshop.book.Book;
import org.bookshop.book.BookController;
import org.bookshop.book.BookService;
import org.bookshop.book.ListBookRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
@AutoConfigureMockMvc
public class BookControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void getAllBooks() throws Exception {
        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.books").isArray());
    }

    @Test
    void shouldReturnBook() throws Exception {
        Mockito.when(bookService.getAllBooks())
                .thenReturn(List.of(new Book("abcd",
                        "ISBN",
                        "Think And Grow Rich",
                        "Description",
                        "Nepolean Hill",
                        "1980",
                        120.00, 50, "imageUrl", "shortImage", 4.5)));

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.books[0].title").value("Think And Grow Rich"))
                .andExpect(jsonPath("$.books[0].id").value("abcd"))
                .andExpect(jsonPath("$.books[0].author").value("Nepolean Hill"))
                .andExpect(jsonPath("$.books[0].quantity").value(50))
                .andExpect(jsonPath("$.books[0].imageUrl").value("imageUrl"));

    }

    @Test
    void shouldReturnEmptyBook() throws Exception {
        Mockito.when(bookService.getAllBooks()).thenReturn(List.of());

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.books").isEmpty());
    }
    @Test
    void shouldReturnSearchedBook() throws Exception {
        Mockito.when(bookService.searchBook("Think"))
                .thenReturn(List.of(new Book("abcd",
                        "ISBN",
                        "Think And Grow Rich",
                        "Description",
                        "Nepolean Hill",
                        "1980",
                        120.00, 50, "imageUrl", "shortImage", 4.5)));

        mockMvc.perform(get("/books?query=Think"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.books[0].title").value("Think And Grow Rich"))
                .andExpect(jsonPath("$.books[0].id").value("abcd"))
                .andExpect(jsonPath("$.books[0].author").value("Nepolean Hill"))
                .andExpect(jsonPath("$.books[0].quantity").value(50))
                .andExpect(jsonPath("$.books[0].imageUrl").value("imageUrl"));

    }

    @Test
    void shouldReturnError() throws Exception {
        Mockito.when(bookService.searchBook("Th"))
                .thenReturn(List.of(new Book("abcd",
                        "ISBN",
                        "Think And Grow Rich",
                        "Description",
                        "Nepolean Hill",
                        "1980",
                        120.00, 50, "imageUrl", "shortImage", 4.5)));

        mockMvc.perform(get("/books?query=Th"))
                .andExpect(status().isBadRequest());

    }


}
