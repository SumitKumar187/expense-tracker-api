package expense_tracker.service;

import expense_tracker.dto.request.ExpenseRequest;
import expense_tracker.dto.response.ExpenseResponse;
import expense_tracker.entity.User;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseService {

    ExpenseResponse createExpense(ExpenseRequest request, User user);

    List<ExpenseResponse> getAllExpenses(User user);

    ExpenseResponse getExpenseById(Long id, User user);

    ExpenseResponse updateExpense(
            Long id,
            ExpenseRequest request,
            User user
    );

    void deleteExpense(Long id, User user);

    List<ExpenseResponse> getExpensesByDateRange(
            User user,
            LocalDate start,
            LocalDate end
    );
}