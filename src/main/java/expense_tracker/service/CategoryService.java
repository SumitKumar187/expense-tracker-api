package expense_tracker.service;

import expense_tracker.dto.request.CategoryRequest;
import expense_tracker.dto.response.CategoryResponse;
import expense_tracker.entity.User;

import java.util.List;

public interface CategoryService {

    CategoryResponse createCategory(
            CategoryRequest request,
            User user
    );

    List<CategoryResponse> getAllCategories(User user);

    CategoryResponse updateCategory(
            Long id,
            CategoryRequest request,
            User user
    );

    void deleteCategory(Long id, User user);
}