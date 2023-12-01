package org.bookshop.book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Arrays;
import java.util.List;

public interface BookRepository extends JpaRepository<BookEntity, String> {
    List<BookEntity> findBookEntitiesByidIn(List<String> id);

    List<BookEntity> findFirst50By();

    List<IsbnOnly> findBookEntitiesByIsbnIn(List<String> isbn);

    @Query(nativeQuery = true,value = "SELECT * FROM books  WHERE title LIKE CONCAT('%',:query, '%') Or author LIKE CONCAT('%', :query, '%') LIMIT 2")
    List<BookEntity> search(String query);

    BookEntity findBookEntityById(String id);

    boolean existsById(String id);
}
