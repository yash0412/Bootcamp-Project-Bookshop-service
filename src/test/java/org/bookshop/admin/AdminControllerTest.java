package org.bookshop.admin;

import org.bookshop.book.Book;
import org.bookshop.book.BookService;
import org.bookshop.book.BooksFileValidation;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminController.class)
@AutoConfigureMockMvc
public class AdminControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    private BooksFileValidation fileValidator;
    @MockBean
    private BookService bookService;

    @Test
    void shouldBeAbleToReachAdminLoadBooksAndGet400WhenFilesAreNotSentInRequest() throws Exception {
        mockMvc.perform(multipart("/admin/loadBooksRequest")).andExpect(status().isBadRequest());
    }

    @Test
    void shouldBeAbleUploadFile() throws Exception {
        String csvData = """
                Title,Author,Price,ImageURL
                Harry Potter,JK Rowling,330,https://google.com
                Harry Potter,JK Rowling,330,https://google.com
                Harry Potter,JK Rowling,330,https://google.com
                """;
        MockMultipartFile csvFile = new MockMultipartFile("file", "books.csv", "text/csv", csvData.getBytes());
        Mockito.when(fileValidator.getUniqueBooksFromCSVFiles(csvFile))
                .thenReturn(List.of());
        mockMvc.perform(multipart("/admin/loadBooksRequest").file(csvFile)).andExpect(status().isOk());
    }

    @Test
    void shouldCallBookValidator() throws Exception {

        String csvData = """
                Title,Author,Price,ImageURL
                Harry Potter,JK Rowling,330,https://google.com
                Harry Potter,JK Rowling,330,https://google.com
                Harry Potter,JK Rowling,330,https://google.com
                """;
        MockMultipartFile csvFile = new MockMultipartFile("file", "books.csv", "text/csv", csvData.getBytes());


        Mockito.when(fileValidator.getUniqueBooksFromCSVFiles(csvFile))
                .thenReturn(List.of(new Book("abcd",
                        "ISBN",
                        "Think And Grow Rich",
                        "Description",
                        "Napolean Hill",
                        "1980",
                        120.00, 50, "imageUrl", "shortImage", 4.5)));
        mockMvc.perform(multipart("/admin/loadBooksRequest").file(csvFile)).andExpect(status().isOk());
    }
}


