package ip.labwork.shop.controller;

import ip.labwork.shop.service.ComponentService;
import ip.labwork.shop.service.ProductService;
import ip.labwork.user.model.UserRole;
import jakarta.validation.Valid;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@Controller
@RequestMapping("/product")
public class ProductMvcController {
    private final ProductService productService;
    private final ComponentService componentService;
    public ProductMvcController(ProductService productService, ComponentService componentService) {
        this.productService = productService;
        this.componentService = componentService;
    }
    @GetMapping
    public String getProducts(Model model) {
        model.addAttribute("products", productService.findAllProduct());
        return "product";
    }
    @GetMapping(value = {"/edit", "/edit/{id}"})
    @Secured({UserRole.AsString.ADMIN})
    public String editProduct(@PathVariable(required = false) Long id,
                              Model model) {
        if (id == null || id <= 0) {
            model.addAttribute("productDto", new ProductDTO());
        } else {
            model.addAttribute("productId", id);
            model.addAttribute("componentDto", new ComponentDTO());
            model.addAttribute("productDto", new ProductDTO(productService.findProduct(id)));
        }
        model.addAttribute("components", componentService.findAllComponent());
        return "product-edit";
    }
    @PostMapping(value = {"/", "/{id}"})
    public String saveProduct(@PathVariable(required = false) Long id,
                              @RequestParam("file") MultipartFile file,
                              @ModelAttribute @Valid ProductDTO productDTO,
                              BindingResult bindingResult,
                              Model model) throws IOException {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "product-edit";
        }
        productDTO.setImage("data:image/jpeg;base64," + Base64.getEncoder().encodeToString(file.getBytes()));
        if (id == null || id <= 0) {
            return "redirect:/product/edit/" + productService.create(productDTO).getId();
        } else {
            productService.updateFields(id, productDTO);
            return "redirect:/product/edit/" + id;
        }
    }
    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/product";
    }
    @PostMapping("/delete/{id}/{componentid}")
    public String deleteComponent(@PathVariable Long id,
                                  @PathVariable Long componentid) {
        productService.deleteComponent(id,componentid);
        return "redirect:/product/edit/" + id;
    }
    @PostMapping("/addproduct/{id}")
    public String addComponent(@PathVariable Long id,
                             @ModelAttribute @Valid ComponentDTO componentDTO,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "product-edit";
        }
        productService.addComponent(id, componentDTO);
        return "redirect:/product/edit/" + id;
    }
}