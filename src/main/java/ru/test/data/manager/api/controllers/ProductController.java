package ru.test.data.manager.api.controllers;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;
import ru.test.data.manager.api.models.product.Product;
import ru.test.data.manager.api.models.product.ProductList;
import ru.test.data.manager.api.services.product.ProductService;

import java.util.List;

@RestController
public class ProductController {

    @Autowired
    ProductService productService;

//    @ApiOperation(value = "Получить все продукты в системе", notes = "Получить все продукты в системе")
//    @GetMapping("/getAllProducts")
//    public List<Product> getAllProduct() {
//        return productService.getProductList();
//    }
//
//    @ApiOperation(value = "Получить продукты клиента", notes = "Получить продукты клиента по его clientId")
//    @ApiImplicitParam(name = "clientId")
//    @GetMapping("/getProductByClientId")
//    public ProductList getProductByClientId(@RequestParam long clientId) {
//        return productService.getProductListByClient(clientId);
//    }

    @ApiOperation(value = "Добавить продукт клиенту", notes = "Добавить продукт клиенту по clientId")
    @ApiImplicitParam(name = "clientId", required = true)
    @PutMapping("/addProductByClientId")
    public Product addProductByClientId(@RequestBody(required = true) Product product, @RequestParam long clientId) {
        return productService.addProductByClientId(product, clientId);
    }
}
