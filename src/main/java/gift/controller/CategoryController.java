package gift.controller;

import gift.dto.CategoryDto;
import gift.model.Category;
import gift.service.CategoryService;
import gift.service.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@Tag(name = "Category", description = "카테고리 관련 api")
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final ProductService productService;

    public CategoryController(CategoryService categoryService, ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{CategoryId}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long CategoryId) {
        Category category = categoryService.getCategoryById(CategoryId);
        return ResponseEntity.ok(category);
    }

    @PostMapping
    public ResponseEntity<Category> addCategory(@RequestBody CategoryDto categoryDto) {
        Category category = categoryService.addCategory(categoryDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{CategoryId}")
                .buildAndExpand(category.getId())
                .toUri();
        return ResponseEntity.created(location).body(category);
    }

    @PutMapping("/{CategoryId}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long CategoryId, @RequestBody CategoryDto categoryDto) {
        Category category = categoryService.updateCategory(CategoryId, categoryDto);
        return ResponseEntity.ok(category);
    }

    @DeleteMapping("/{CategoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long CategoryId) {
        productService.updateProductCategoryToNone(CategoryId);
        categoryService.deleteCategory(CategoryId);
        return ResponseEntity.noContent().build();
    }
}
