package gift.controller;

import gift.dto.ProductDto;
import gift.model.Product;
import gift.service.CategoryService;
import gift.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/web/products")
public class ProductWebController {
    private final ProductService productService;
    private final CategoryService categoryService;

    public ProductWebController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String getAllProducts(Model model,
                                 @PageableDefault(size = 10, sort = {"ProductId"},
                                         direction = Sort.Direction.ASC) Pageable pageable) {
        Page<Product> productPage = productService.getProducts(pageable);
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", productPage.getNumber());
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("totalItems", productPage.getTotalElements());
        model.addAttribute("pageSize", pageable.getPageSize());
        model.addAttribute("sortField", pageable.getSort().iterator().next().getProperty());
        model.addAttribute("sortDirection", pageable.getSort().iterator().next().getDirection().toString());
        return "index";
    }

    @GetMapping("/add")
    public String addProductForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "addProduct";
    }

    @PostMapping("/add")
    public String addProduct(@ModelAttribute @Valid ProductDto productDto) {
        productService.addProduct(productDto);
        return "redirect:/web/products";
    }

    @GetMapping("/edit/{ProductId}")
    public String editProductForm(@PathVariable Long ProductId, Model model) {
        Product product = productService.getProductById(ProductId);
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "editProduct";
    }

    @PostMapping("/edit/{ProductId}")
    public String updateProduct(@PathVariable Long ProductId,
                                @ModelAttribute @Valid ProductDto productDto) {
        productService.updateProduct(ProductId, productDto);
        return "redirect:/web/products";
    }

    @GetMapping("/delete/{ProductId}")
    public String deleteProduct(@PathVariable Long ProductId) {
        productService.deleteProduct(ProductId);
        return "redirect:/web/products";
    }
}
