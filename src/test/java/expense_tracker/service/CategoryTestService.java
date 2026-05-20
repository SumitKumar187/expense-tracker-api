package expense_tracker.service;

import expense_tracker.dto.request.CategoryRequest;
import expense_tracker.dto.response.CategoryResponse;
import expense_tracker.entity.Category;
import expense_tracker.entity.User;
import expense_tracker.repository.CategoryRepository;
import expense_tracker.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private User user;
    private Category category;
    private CategoryRequest categoryRequest;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@test.com")
                .build();

        category = Category.builder()
                .id(1L)
                .name("Food")
                .description("Food expenses")
                .user(user)
                .build();

        categoryRequest = new CategoryRequest();
        categoryRequest.setName("Food");
        categoryRequest.setDescription("Food expenses");
    }

    @Test
    void createCategory_Success() {
        // Given
        when(categoryRepository.existsByNameAndUser(anyString(), any(User.class))).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        // When
        CategoryResponse response = categoryService.createCategory(categoryRequest, user);

        // Then
        assertNotNull(response);
        assertEquals("Food", response.getName());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void createCategory_AlreadyExists_ThrowsException() {
        // Given
        when(categoryRepository.existsByNameAndUser(anyString(), any(User.class))).thenReturn(true);

        // When & Then
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> categoryService.createCategory(categoryRequest, user)
        );
        assertEquals("Category already exists", exception.getMessage());
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    void getAllCategories_Success() {
        // Given
        Category category2 = Category.builder()
                .id(2L)
                .name("Transport")
                .description("Transport expenses")
                .user(user)
                .build();
        when(categoryRepository.findByUser(any(User.class)))
                .thenReturn(Arrays.asList(category, category2));

        // When
        List<CategoryResponse> categories = categoryService.getAllCategories(user);

        // Then
        assertEquals(2, categories.size());
        assertEquals("Food", categories.get(0).getName());
        assertEquals("Transport", categories.get(1).getName());
    }

    @Test
    void updateCategory_Success() {
        // Given
        CategoryRequest updateRequest = new CategoryRequest();
        updateRequest.setName("Groceries");
        updateRequest.setDescription("Grocery shopping");

        when(categoryRepository.findByIdAndUser(anyLong(), any(User.class)))
                .thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        // When
        CategoryResponse response = categoryService.updateCategory(1L, updateRequest, user);

        // Then
        assertNotNull(response);
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void deleteCategory_Success() {
        // Given
        when(categoryRepository.findByIdAndUser(anyLong(), any(User.class)))
                .thenReturn(Optional.of(category));
        doNothing().when(categoryRepository).delete(any(Category.class));

        // When
        categoryService.deleteCategory(1L, user);

        // Then
        verify(categoryRepository, times(1)).delete(any(Category.class));
    }

    @Test
    void deleteCategory_NotFound_ThrowsException() {
        // Given
        when(categoryRepository.findByIdAndUser(anyLong(), any(User.class)))
                .thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> categoryService.deleteCategory(1L, user));
        verify(categoryRepository, never()).delete(any(Category.class));
    }
}