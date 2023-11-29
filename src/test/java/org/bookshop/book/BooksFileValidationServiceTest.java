package org.bookshop.book;


import org.assertj.core.api.Assertions;
import org.bookshop.book.BadFileException;
import org.bookshop.book.Book;
import org.bookshop.book.BooksFileValidationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class BooksFileValidationServiceTest {

    @Test
    void shouldThrowExceptionWhenFileIsNotCSV() throws Exception {
        String csvData = """
                0195153448;Classical Mythology;Classical Mythology;Mark P. O. Morford;2002;http://images.amazon.com/images/P/0195153448.01.MZZZZZZZ.jpg;http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg;256.22242641102025;0;0.0
                0002005018;Clara Callan;Clara Callan;Richard Bruce Wright;2001;http://images.amazon.com/images/P/0002005018.01.MZZZZZZZ.jpg;http://images.amazon.com/images/P/0002005018.01.LZZZZZZZ.jpg;135.3421165008236;9;4.928571428571429
                0060973129;Decision in Normandy;Decision in Normandy;Carlo D'Este;1991;http://images.amazon.com/images/P/0060973129.01.MZZZZZZZ.jpg;http://images.amazon.com/images/P/0060973129.01.LZZZZZZZ.jpg;433.5962349629304;0;5.0
                0374157065;Flu: The Story of the Great Influenza Pandemic of 1918 and the Search for the Virus That Caused It;Flu: The Story of the Great Influenza Pandemic of 1918 and the Search for the Virus That Caused It;Gina Bari Kolata;1999;http://images.amazon.com/images/P/0374157065.01.MZZZZZZZ.jpg;http://images.amazon.com/images/P/0374157065.01.LZZZZZZZ.jpg;411.03564672400944;5;4.2727272727272725
                0393045218;The Mummies of Urumchi;The Mummies of Urumchi;E. J. W. Barber;1999;http://images.amazon.com/images/P/0393045218.01.THUMBZZZ.jpg;http://images.amazon.com/images/P/0393045218.01.MZZZZZZZ.jpg;329.95144587558644;0;0.0
                0399135782;The Kitchen God's Wife;The Kitchen God's Wife;Amy Tan;1991;http://images.amazon.com/images/P/0399135782.01.MZZZZZZZ.jpg;http://images.amazon.com/images/P/0399135782.01.LZZZZZZZ.jpg;488.4089826549853;2;4.212121212121212
                0425176428;What If?: The World's Foremost Military Historians Imagine What Might Have Been;What If?: The World's Foremost Military Historians Imagine What Might Have Been;Robert Cowley;2000;http://images.amazon.com/images/P/0425176428.01.MZZZZZZZ.jpg;http://images.amazon.com/images/P/0425176428.01.LZZZZZZZ.jpg;161.62414691225874;9;1.6
                0671870432;PLEADING GUILTY;PLEADING GUILTY;Scott Turow;1993;http://images.amazon.com/images/P/0671870432.01.MZZZZZZZ.jpg;http://images.amazon.com/images/P/0671870432.01.LZZZZZZZ.jpg;203.0408146454649;8;2.6666666666666665
                """;
        MockMultipartFile csvFile = new MockMultipartFile("file", "books.json", "text/json", csvData.getBytes());

        BooksFileValidationService booksFileValidationService = new BooksFileValidationService();

        Assertions.assertThatThrownBy(() -> booksFileValidationService.getUniqueBooksFromCSVFiles(csvFile)).isInstanceOf(BadFileException.class);
    }

    @Test
    void shouldThrowExceptionWhenDataIsInvalid() throws Exception {
        String csvData = """
                0195153448;Classical Mythology;Classical Mythology;Mark P. O. Morford;2002;http://images.amazon.com/images/P/0195153448.01.MZZZZZZZ.jpg;http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg;256.22242641102025;0;0.0
                0002005018;Clara Callan;Clara Callan;Richard Bruce Wright;2001;http://images.amazon.com/images/P/0002005018.01.MZZZZZZZ.jpg;http://images.amazon.com/images/P/0002005018.01.LZZZZZZZ.jpg;135.3421165008236;9;4.928571428571429
                0060973129;Decision in Normandy;Decision in Normandy;Carlo D'Este;1991;http://images.amazon.com/images/P/0060973129.01.MZZZZZZZ.jpg;http://images.amazon.com/images/P/0060973129.01.LZZZZZZZ.jpg;433.5962349629304;0;5.0
                0374157065;Flu: The Story of the Great Influenza Pandemic of 1918 and the Search for the Virus That Caused It;Flu: The Story of the Great Influenza Pandemic of 1918 and the Search for the Virus That Caused It;Gina Bari Kolata;1999;http://images.amazon.com/images/P/0374157065.01.MZZZZZZZ.jpg;http://images.amazon.com/images/P/0374157065.01.LZZZZZZZ.jpg;411.03564672400944;5;4.2727272727272725
                0393045218;The Mummies of Urumchi;The Mummies of Urumchi;E. J. W. Barber;1999;http://images.amazon.com/images/P/0393045218.01.THUMBZZZ.jpg;http://images.amazon.com/images/P/0393045218.01.MZZZZZZZ.jpg;329.95144587558644;0;0.0
                0399135782The Kitchen God's Wife;The Kitchen God's Wife;Amy Tan;1991;http://images.amazon.com/images/P/0399135782.01.MZZZZZZZ.jpg;http://images.amazon.com/images/P/0399135782.01.LZZZZZZZ.jpg;488.4089826549853;2;4.212121212121212
                0425176428;What If?: The World's Foremost Military Historians Imagine What Might Have Been;What If?: The World's Foremost Military Historians Imagine What Might Have Been;Robert Cowley;2000;http://images.amazon.com/images/P/0425176428.01.MZZZZZZZ.jpg;http://images.amazon.com/images/P/0425176428.01.LZZZZZZZ.jpg;161.62414691225874;9;1.6
                0671870432;PLEADING GUILTY;PLEADING GUILTY;Scott Turow;1993;http://images.amazon.com/images/P/0671870432.01.MZZZZZZZ.jpg;http://images.amazon.com/images/P/0671870432.01.LZZZZZZZ.jpg;203.0408146454649;8;2.6666666666666665
                """;
        MockMultipartFile csvFile = new MockMultipartFile("file", "books.csv", "text/csv", csvData.getBytes());

        BooksFileValidationService booksFileValidationService = new BooksFileValidationService();

        Assertions.assertThatThrownBy(() -> booksFileValidationService.getUniqueBooksFromCSVFiles(csvFile)).isInstanceOf(BadFileException.class);
    }

    @Test
    void shouldReturnBooksFromCSV() throws Exception {
        String csvData = """
                0195153448;Classical Mythology;Classical Mythology;Mark P. O. Morford;2002;http://images.amazon.com/images/P/0195153448.01.MZZZZZZZ.jpg;http://images.amazon.com/images/P/0195153448.01.LZZZZZZZ.jpg;256.22242641102025;0;0.0
                0002005018;Clara Callan;Clara Callan;Richard Bruce Wright;2001;http://images.amazon.com/images/P/0002005018.01.MZZZZZZZ.jpg;http://images.amazon.com/images/P/0002005018.01.LZZZZZZZ.jpg;135.3421165008236;9;4.928571428571429
                0060973129;Decision in Normandy;Decision in Normandy;Carlo D'Este;1991;http://images.amazon.com/images/P/0060973129.01.MZZZZZZZ.jpg;http://images.amazon.com/images/P/0060973129.01.LZZZZZZZ.jpg;433.5962349629304;0;5.0
                0374157065;Flu: The Story of the Great Influenza Pandemic of 1918 and the Search for the Virus That Caused It;Flu: The Story of the Great Influenza Pandemic of 1918 and the Search for the Virus That Caused It;Gina Bari Kolata;1999;http://images.amazon.com/images/P/0374157065.01.MZZZZZZZ.jpg;http://images.amazon.com/images/P/0374157065.01.LZZZZZZZ.jpg;411.03564672400944;5;4.2727272727272725
                0393045218;The Mummies of Urumchi;The Mummies of Urumchi;E. J. W. Barber;1999;http://images.amazon.com/images/P/0393045218.01.THUMBZZZ.jpg;http://images.amazon.com/images/P/0393045218.01.MZZZZZZZ.jpg;329.95144587558644;0;0.0
                0399135782;The Kitchen God's Wife;The Kitchen God's Wife;Amy Tan;1991;http://images.amazon.com/images/P/0399135782.01.MZZZZZZZ.jpg;http://images.amazon.com/images/P/0399135782.01.LZZZZZZZ.jpg;488.4089826549853;2;4.212121212121212
                0425176428;What If?: The World's Foremost Military Historians Imagine What Might Have Been;What If?: The World's Foremost Military Historians Imagine What Might Have Been;Robert Cowley;2000;http://images.amazon.com/images/P/0425176428.01.MZZZZZZZ.jpg;http://images.amazon.com/images/P/0425176428.01.LZZZZZZZ.jpg;161.62414691225874;9;1.6
                0671870432;PLEADING GUILTY;PLEADING GUILTY;Scott Turow;1993;http://images.amazon.com/images/P/0671870432.01.MZZZZZZZ.jpg;http://images.amazon.com/images/P/0671870432.01.LZZZZZZZ.jpg;203.0408146454649;8;2.6666666666666665
                """;
        MockMultipartFile csvFile = new MockMultipartFile("file", "books.csv", "text/csv", csvData.getBytes());

        BooksFileValidationService booksFileValidationService = new BooksFileValidationService();
        List<Book> books = booksFileValidationService.getUniqueBooksFromCSVFiles(csvFile);

        Assertions.assertThat(books.size()).isEqualTo(8);

    }


}
