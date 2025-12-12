package com.library.system.repository;

import com.library.system.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    // Existing method for category filtering
    List<Book> findByCategoryIgnoreCase(String category);

    /**
     * NEW: Searches for books where the title, author, or ISBN contains the keyword (case-insensitive).
     * The original findByTitleContainingIgnoreCase is now superseded by this method.
     * @param keyword The search term.
     * @return A list of matching books.
     */
    @Query("SELECT b FROM Book b WHERE " +
            "LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(b.author) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(b.isbn) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Book> searchBooks(@Param("keyword") String keyword);
}