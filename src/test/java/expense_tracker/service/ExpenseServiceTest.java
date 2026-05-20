package expense_tracker.service;

import expense_tracker.dto.request.ExpenseRequest;
import expense_tracker.dto.response.ExpenseResponse;
import expense_tracker.entity.Category;
import expense_tracker.entity.Expense;
import expense_tracker.entity.User;
import expense_tracker.repository.CategoryRepository;
import expense_tracker.repository.ExpenseRepository;
import expense_tracker.service.impl.ExpenseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExpenseServiceTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ExpenseServiceImpl expenseService;

    private User user;
    private Category category;
    private Expense expense;
    private ExpenseRequest expenseRequest;

    @BeforeEach
    void setUp() {

        user = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@gmail.com")
                .password("password")
                .build();

        category = Category.builder()
                .id(1L)
                .name("Food")
                .description("Food expenses")
                .user(user)
                .build();

        expense = Expense.builder()
                .id(1L)
                .title("Burger")
                .description("Lunch")
                .amount(BigDecimal.valueOf(250))
                .expenseDate(LocalDate.now())
                .createdAt(LocalDateTime.now())
                .category(category)
                .user(user)
                .build();

        expenseRequest = new ExpenseRequest();
        expenseRequest.setTitle("Burger");
        expenseRequest.setDescription("Lunch");
        expenseRequest.setAmount(BigDecimal.valueOf(250));
        expenseRequest.setExpenseDate(LocalDate.now());
        expenseRequest.setCategoryId(1L);
    }

    @Test
    void createExpense_ShouldReturnExpenseResponse() {

        when(categoryRepository.findByIdAndUser(1L, user))
                .thenReturn(Optional.of(category));

        when(expenseRepository.save(any(Expense.class)))
                .thenReturn(expense);

        ExpenseResponse response =
                expenseService.createExpense(expenseRequest, user);

        assertNotNull(response);
        assertEquals("Burger", response.getTitle());
        assertEquals("Food", response.getCategoryName());

        verify(expenseRepository, times(1))
                .save(any(Expense.class));
    }

    @Test
    void getAllExpenses_ShouldReturnExpenseList() {

        when(expenseRepository.findByUser(user))
                .thenReturn(List.of(expense));

        List<ExpenseResponse> responses =
                expenseService.getAllExpenses(user);

        assertEquals(1, responses.size());
        assertEquals("Burger", responses.get(0).getTitle());

        verify(expenseRepository, times(1))
                .findByUser(user);
    }

    @Test
    void getExpenseById_ShouldReturnExpense() {

        when(expenseRepository.findByIdAndUser(1L, user))
                .thenReturn(Optional.of(expense));

        ExpenseResponse response =
                expenseService.getExpenseById(1L, user);

        assertNotNull(response);
        assertEquals("Burger", response.getTitle());

        verify(expenseRepository, times(1))
                .findByIdAndUser(1L, user);
    }

    @Test
    void deleteExpense_ShouldDeleteExpense() {

        when(expenseRepository.findByIdAndUser(1L, user))
                .thenReturn(Optional.of(expense));

        expenseService.deleteExpense(1L, user);

        verify(expenseRepository, times(1))
                .delete(expense);
    }
}