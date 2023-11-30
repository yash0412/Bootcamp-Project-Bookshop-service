package org.bookshop.book;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

@Service
public class BooksFileValidationService implements BooksFileValidation {

    @Override
    public List<Book> getUniqueBooksFromCSVFiles(MultipartFile csvFile) throws BadFileException, IOException {
        if (!Objects.equals(csvFile.getContentType(), "text/csv")) {
            throw new BadFileException("Only CSV Files are allowed");
        }
        CSVParser csvParser = new CSVParserBuilder().withSeparator(';').build(); // custom separator
        Reader reader = new InputStreamReader(csvFile.getInputStream());
        List<Book> books = new ArrayList<>();
        try (CSVReader csvReader = new CSVReaderBuilder(reader)
                .withCSVParser(csvParser)
                .build()) {
            List<String[]> allData = csvReader.readAll();
            System.out.println("Read file, read line: " + allData.size());
            int index = 0;
            for (String[] rowData : allData) {
                if (rowData.length != 10) {
                    throw new BadFileException("File Data not valid at row " + (index + 1));
                }
                try {
                    Book book = new Book(
                            java.util.UUID.randomUUID().toString(),
                            rowData[0],
                            rowData[1],
                            rowData[2],
                            rowData[3],
                            rowData[4],
                            Math.round(Double.parseDouble(rowData[7]) * 100) / 100D,
                            Integer.parseInt(rowData[8]),
                            rowData[6],
                            rowData[5],
                            Math.round(Double.parseDouble(rowData[9]) * 100) / 100D
                    );
                    books.add(book);
                    index++;
                } catch (Exception e) {
                    System.out.println(e);
                    throw new BadFileException("File Data not valid at row " + (index + 1));
                }
            }
        } catch (IOException | CsvException e) {
            throw new RuntimeException(e);
        } catch (BadFileException e) {
            throw e;
        }
        System.out.println("Read all Data");
        return getUniqueBooks(books);
    }


    private List<Book> getUniqueBooks(List<Book> books) {
        List<Book> uniqueBooks = new ArrayList<>();
        Map<String, Boolean> booksMapByISBN = new HashMap<>();
        books.forEach(book -> {
            if (!booksMapByISBN.containsKey(book.isbn())) {
                uniqueBooks.add(book);
                booksMapByISBN.put(book.isbn(), true);
            }
        });
        return uniqueBooks;
    }
}
