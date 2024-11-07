package com.example.webtutors.controllers;

import com.example.webtutors.models.Image;
import com.example.webtutors.models.Product;
import com.example.webtutors.models.User;
import com.example.webtutors.repositories.UserRepository;
import com.example.webtutors.services.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/")
    public String products(@RequestParam(name = "searchWord", required = false) String searchWord,
                           Principal principal, Model model) {
        List<Product> products = productService.listProducts(searchWord);

        model.addAttribute("products", products);

        model.addAttribute("user", principal != null ? productService.getUserByPrincipal(principal) : null);

        model.addAttribute("searchWord", searchWord);

        return "products";
    }


    @GetMapping("/products")
    public String productsByCity(@RequestParam(name = "searchCity", required = false) String searchCity,
                                 @RequestParam(name = "searchWord", required = false) String searchWord,
                                 Principal principal, Model model) {

        List<Product> products;


        if ((searchCity == null || searchCity.isEmpty()) && (searchWord == null || searchWord.isEmpty())) {
            products = productService.listAllProducts();
        } else if (searchCity != null && !searchCity.isEmpty()) {
            products = productService.listProductsByCity(searchCity);
        } else {
            products = productService.listProducts(searchWord);
        }

        model.addAttribute("products", products);
        model.addAttribute("searchCity", searchCity);
        model.addAttribute("searchWord", searchWord);


        if (principal != null) {
            model.addAttribute("user", productService.getUserByPrincipal(principal));
        } else {
            model.addAttribute("user", null);
        }

        return "products-sorted-by-city";
    }





    @GetMapping("/product/{id}")
    public String productInfo(@PathVariable Long id, Model model, Principal principal) {
        Product product = productService.getProductById(id);
        model.addAttribute("user", productService.getUserByPrincipal(principal));
        model.addAttribute("product", product);
        model.addAttribute("images", product.getImages());
        model.addAttribute("authorProduct", product.getUser());
        return "product-info";
    }




    @PostMapping("/product/create")
    public String createProduct(@RequestParam("file1") MultipartFile file1, @RequestParam("file2") MultipartFile file2,
                                @RequestParam("file3") MultipartFile file3, Product product, Principal principal) throws IOException {
        productService.saveProduct(principal, product, file1, file2, file3);
        return "redirect:/my/products";
    }

    @PostMapping("/product/delete/{id}")
    public String deleteProduct(Principal principal, @PathVariable Long id) {
        log.info("Deleting product with id = {} by user {}", id, principal.getName());
        productService.deleteProduct(productService.getUserByPrincipal(principal), id);
        return "redirect:/my/products";
    }

    @GetMapping("/product/edit/{id}")
    public String editProductForm(@PathVariable Long id, Model model, Principal principal) {
        Product product = productService.getProductById(id);
        model.addAttribute("user", productService.getUserByPrincipal(principal));
        model.addAttribute("product", product);
        model.addAttribute("images", product.getImages());
        return "edit-product-form"; // Шаблон для отображения формы редактирования товара
    }



    @PostMapping("/product/update/{id}")
    public String editProduct(@PathVariable Long id, Product updatedProduct,
                              @RequestParam("file1") MultipartFile file1,
                              @RequestParam("file2") MultipartFile file2,
                              @RequestParam("file3") MultipartFile file3,
                              Principal principal) throws IOException {
        Product existingProduct = productService.getProductById(id);

        // Проверка, что пользователь является владельцем товара
        if (!existingProduct.getUser().getEmail().equals(principal.getName())) {
            return "redirect:/"; // Редирект на главную страницу или другую страницу, если пользователь не авторизован
        }

        // Установка обновленных данных товара
        existingProduct.setTitle(updatedProduct.getTitle());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setDescription(updatedProduct.getDescription());

        // Обновление изображений, если они были выбраны
        if (file1 != null && !file1.isEmpty()) {
            Image image1 = productService.toImageEntity(file1);
            image1.setPreviewImage(true);
            existingProduct.addImageToProduct(image1);
        }
        // Аналогично для file2 и file3

        // Сохранение обновленного товара в базе данных
        productService.saveProduct(principal, existingProduct, file1, file2, file3);

        return "redirect:/product/" + id; // Редирект на страницу с информацией о товаре
    }




    @GetMapping("/my/products")
    public String userProducts(Principal principal, Model model) {
        User user = productService.getUserByPrincipal(principal);
        model.addAttribute("user", user);
        model.addAttribute("products", user.getProducts());
        return "my-products";
    }
}
