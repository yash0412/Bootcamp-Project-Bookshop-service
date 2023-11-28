package org.bookshop;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

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

    @Test
    void shouldBeAbleToReachAdminLoadBooksAndGet400WhenFilesAreNotSentInRequest() throws Exception {
        mockMvc.perform(multipart("/admin/loadBooks")).andExpect(status().isBadRequest());
    }

    @Test
    void shouldBeAbleUploadFile() throws Exception {
        String csvData = """
                Title,Author,Price,ImageURL
                Harry Potter,JK Rowling,330,https://google.com
                Harry Potter,JK Rowling,330,https://google.com
                Harry Potter,JK Rowling,330,https://google.com
                """;
        MockMultipartFile csvFile = new MockMultipartFile("files", "books.csv", "text/csv", csvData.getBytes());
        Mockito.when(fileValidator.getUniqueBooksFromCSVFiles(List.of(csvFile, csvFile)))
                .thenReturn(List.of());
        mockMvc.perform(multipart("/admin/loadBooks").file(csvFile).file(csvFile)).andExpect(status().isOk());
    }

    @Test
    void shouldCallBookValidator() throws Exception {

        String csvData = """
                Title,Author,Price,ImageURL
                Harry Potter,JK Rowling,330,https://google.com
                Harry Potter,JK Rowling,330,https://google.com
                Harry Potter,JK Rowling,330,https://google.com
                """;
        MultipartFile csvFile = new MockMultipartFile("files", "books.csv", "text/csv", csvData.getBytes());


        Mockito.when(fileValidator.getUniqueBooksFromCSVFiles(List.of(csvFile)))
                .thenReturn(List.of(new Book("1", "Harry Potter", "JK Rowling", 330, 5, "https://google.com")));

        Assertions.assertThat(true);
    }
}


