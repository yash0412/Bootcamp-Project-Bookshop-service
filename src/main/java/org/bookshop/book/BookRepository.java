package org.bookshop.book;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<BookEntity, String> {
    List<BookEntity> findBookEntitiesByidIn(List<String> id);

    List<BookEntity> findFirst50By();

    List<IsbnOnly> findBookEntitiesByIsbnIn(List<String> isbn);
}
