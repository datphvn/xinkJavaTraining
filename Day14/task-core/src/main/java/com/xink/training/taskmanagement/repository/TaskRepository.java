package com.xink.training.taskmanagement.repository;

import com.xink.training.taskmanagement.domain.Task;
import com.xink.training.taskmanagement.domain.TaskPriority;
import com.xink.training.taskmanagement.domain.TaskStatus;
import com.xink.training.taskmanagement.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {
    
    List<Task> findByAssignee(User assignee);
    
    List<Task> findByCreator(User creator);
    
    List<Task> findByPriority(TaskPriority priority);
    
    List<Task> findByDueDateBefore(LocalDateTime date);
    
    @Query("SELECT t FROM Task t WHERE t.status = :status")
    List<Task> findByStatus(@Param("status") TaskStatus status);
    
    @Query("SELECT t FROM Task t WHERE t.dueDate < :now AND t.status NOT IN :completedStatuses")
    List<Task> findOverdueTasks(@Param("now") LocalDateTime now, 
                                @Param("completedStatuses") List<Class<? extends TaskStatus>> completedStatuses);
    
    @Query("SELECT t FROM Task t WHERE :label MEMBER OF t.labels")
    List<Task> findByLabel(@Param("label") String label);
    
    @Query("SELECT t FROM Task t WHERE t.title LIKE %:searchText% OR t.description LIKE %:searchText%")
    List<Task> searchByText(@Param("searchText") String searchText);
}

