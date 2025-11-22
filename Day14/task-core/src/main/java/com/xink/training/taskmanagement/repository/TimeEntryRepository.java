package com.xink.training.taskmanagement.repository;

import com.xink.training.taskmanagement.domain.TimeEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface TimeEntryRepository extends JpaRepository<TimeEntry, UUID> {
    
    List<TimeEntry> findByDateBetween(LocalDate startDate, LocalDate endDate);
    
    @Query("SELECT te FROM TimeEntry te WHERE te.date >= :fromDate AND te.date <= :toDate")
    List<TimeEntry> findTimeEntriesInDateRange(@Param("fromDate") LocalDate fromDate, 
                                                @Param("toDate") LocalDate toDate);
}

