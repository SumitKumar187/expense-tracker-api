package expense_tracker.service.impl;

import expense_tracker.dto.request.ExpenseRequest;
import expense_tracker.dto.response.ExpenseResponse;
import expense_tracker.entity.Category;
import expense_tracker.entity.Expense;
import expense_tracker.entity.User;
import expense_tracker.repository.CategoryRepository;
import expense_tracker.repository.ExpenseRepository;
import expense_tracker.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public ExpenseResponse createExpense(ExpenseRequest request, User user) {
        Category category = categoryRepository.findByIdAndUser(request.getCategoryId(), user)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        Expense expense = Expense.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .amount(request.getAmount())
                .expenseDate(request.getExpenseDate())
                .category(category)
                .user(user)
                .build();
        return mapToResponse(expenseRepository.save(expense));
    }

    @Override
    public List<ExpenseResponse> getAllExpenses(User user) {
        return expenseRepository.findByUser(user)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ExpenseResponse getExpenseById(Long id, User user) {
        return expenseRepository.findByIdAndUser(id, user)
                .map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Expense not found"));
    }

    @Override
    public ExpenseResponse updateExpense(Long id, ExpenseRequest request, User user) {
        Expense expense = expenseRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Expense not found"));
        Category category = categoryRepository.findByIdAndUser(request.getCategoryId(), user)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        expense.setTitle(request.getTitle());
        expense.setDescription(request.getDescription());
        expense.setAmount(request.getAmount());
        expense.setExpenseDate(request.getExpenseDate());
        expense.setCategory(category);
        return mapToResponse(expenseRepository.save(expense));
    }

    @Override
    public void deleteExpense(Long id, User user) {
        Expense expense = expenseRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Expense not found"));
        expenseRepository.delete(expense);
    }

    @Override
    public List<ExpenseResponse> getExpensesByDateRange(User user, LocalDate start, LocalDate end) {
        return expenseRepository.findByUserAndExpenseDateBetween(user, start, end)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private ExpenseResponse mapToResponse(Expense expense) {
        return new ExpenseResponse(
                expense.getId(),
                expense.getTitle(),
                expense.getDescription(),
                expense.getAmount(),
                expense.getExpenseDate(),
                expense.getCreatedAt(),
                expense.getCategory().getName()
        );
    }
}