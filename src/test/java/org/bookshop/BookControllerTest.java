package org.bookshop;

import com.fasterxml.jackson.databind.ObjectMapper;
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
                        "Think And Grow Rich",
                        "Nepolean Hill",
                        120.00, 50, "imageUrl")));

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
    void getBooksShouldReturnBooks() throws Exception {
        List<String> bookList = new ArrayList<>();
        bookList.add("b0ca96a9-d5d6-4f8b-8353-8d81e2222747");
        bookList.add("30faf58b-fd4e-46aa-aa18-964e95a4f56b");
        Mockito.when(bookService.getBooks(bookList))
                .thenReturn(List.of(new Book("abcd",
                "Think And Grow Rich",
                "Nepolean Hill",
                120.00, 50, "imageUrl")));

        ListBookRequest request = new ListBookRequest(bookList);

        mockMvc.perform(post("/books-by-ids")
                        .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.books").isArray());
    }

    @Test
    void getBooksShouldReturnBooksOnId() throws Exception {
        List<String> bookList = new ArrayList<>();
        bookList.add("b0ca96a9-d5d6-4f8b-8353-8d81e2222747");
        bookList.add("30faf58b-fd4e-46aa-aa18-964e95a4f56b");
        Mockito.when(bookService.getBooks(bookList))
                .thenReturn(List.of(new Book("b0ca96a9-d5d6-4f8b-8353-8d81e2222747",
                        "Think And Grow Rich",
                        "Nepolean Hill",
                        120.00, 50, "imageUrl"),
                        new Book("30faf58b-fd4e-46aa-aa18-964e95a4f56b",
                                "Alchamist",
                                "Paulo Coelho",
                                520.00, 5, "imageUrl2")));

        ListBookRequest request = new ListBookRequest(bookList);

        mockMvc.perform(post("/books-by-ids")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.books[0].id").value("b0ca96a9-d5d6-4f8b-8353-8d81e2222747"))
                .andExpect(jsonPath("$.books[0].title").value("Think And Grow Rich"))
                .andExpect(jsonPath("$.books[0].author").value("Nepolean Hill"))
                .andExpect(jsonPath("$.books[0].price").value(120.0))
                .andExpect(jsonPath("$.books[0].quantity").value(50))
                .andExpect(jsonPath("$.books[1].id").value("30faf58b-fd4e-46aa-aa18-964e95a4f56b"))
                .andExpect(jsonPath("$.books[1].title").value("Alchamist"))
                .andExpect(jsonPath("$.books[1].author").value("Paulo Coelho"))
                .andExpect(jsonPath("$.books[1].price").value(520.00))
                .andExpect(jsonPath("$.books[1].quantity").value(5));
    }



}
