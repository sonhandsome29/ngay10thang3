package com.example.sondeptraidemo.controller;

import com.example.sondeptraidemo.model.Category;
import com.example.sondeptraidemo.model.Product;
import com.example.sondeptraidemo.service.CategoryService;
import com.example.sondeptraidemo.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping({"", "/"})
    public String listProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "product/list";
    }

    @GetMapping({"/add", "/create"})
    public String showAddForm(Model model) {
        Product product = new Product();
        product.setCategory(new Category());
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "product/add";
    }

    @PostMapping("/save")
    public String saveProduct(
            @Valid @ModelAttribute("product") Product product,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        Category selectedCategory = resolveCategory(product, result);

        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            return "product/add";
        }

        product.setCategory(selectedCategory);
        productService.saveProduct(product);
        redirectAttributes.addFlashAttribute("successMessage", "Thêm sản phẩm thành công.");
        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Product> productOptional = productService.getProductById(id);
        if (productOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy sản phẩm cần sửa.");
            return "redirect:/products";
        }

        Product product = productOptional.get();
        if (product.getCategory() == null) {
            product.setCategory(new Category());
        }

        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "product/edit";
    }

    @PostMapping("/update/{id}")
    public String updateProduct(
            @PathVariable Long id,
            @Valid @ModelAttribute("product") Product formProduct,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        Optional<Product> productOptional = productService.getProductById(id);
        if (productOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy sản phẩm cần cập nhật.");
            return "redirect:/products";
        }

        Category selectedCategory = resolveCategory(formProduct, result);
        if (result.hasErrors()) {
            formProduct.setId(id);
            model.addAttribute("categories", categoryService.getAllCategories());
            return "product/edit";
        }

        Product product = productOptional.get();
        product.setName(formProduct.getName());
        product.setPrice(formProduct.getPrice());
        product.setImage(formProduct.getImage());
        product.setCategory(selectedCategory);

        productService.saveProduct(product);
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật sản phẩm thành công.");
        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Product> productOptional = productService.getProductById(id);
        if (productOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Sản phẩm không tồn tại.");
            return "redirect:/products";
        }

        productService.deleteProduct(id);
        redirectAttributes.addFlashAttribute("successMessage", "Xóa sản phẩm thành công.");
        return "redirect:/products";
    }

    private Category resolveCategory(Product product, BindingResult result) {
        Long categoryId = product.getCategory() != null ? product.getCategory().getId() : null;
        if (categoryId == null) {
            result.rejectValue("category", "category.empty", "Vui lòng chọn danh mục");
            return null;
        }

        Optional<Category> categoryOptional = categoryService.getCategoryById(categoryId);
        if (categoryOptional.isEmpty()) {
            result.rejectValue("category", "category.invalid", "Danh mục không hợp lệ");
            return null;
        }

        return categoryOptional.get();
    }
}
