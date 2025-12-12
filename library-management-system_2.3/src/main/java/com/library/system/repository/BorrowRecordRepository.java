package com.library.system.repository;

import com.library.system.model.BorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {

    // Find all active (unreturned) records for a specific user ID
    List<BorrowRecord> findByUserIdAndReturnDateIsNull(Long userId);

    // NEW METHOD: Find all active (unreturned) records globally
    List<BorrowRecord> findByReturnDateIsNull();

    // Previous methods are often still helpful:
    List<BorrowRecord> findByUserId(Long userId);
    List<BorrowRecord> findByBookId(Long bookId);
}