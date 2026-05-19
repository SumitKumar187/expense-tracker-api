package expense_tracker.repository;

import expense_tracker.entity.Category;
import expense_tracker.entity.Expense;
import expense_tracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByUser(User user);
    Optional<Expense> findByIdAndUser(Long id, User user);
    List<Expense> findByUserAndCategory(User user, Category category);
    List<Expense> findByUserAndExpenseDateBetween(User user, LocalDate start, LocalDate end);
}