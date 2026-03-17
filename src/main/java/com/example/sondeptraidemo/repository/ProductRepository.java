package com.example.sondeptraidemo.repository;

import com.example.sondeptraidemo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}