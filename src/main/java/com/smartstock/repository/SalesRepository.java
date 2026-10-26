package com.smartstock.repository;

import com.smartstock.model.Sales;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SalesRepository extends JpaRepository<Sales, Long> {

    List<Sales> findAllByOrderByProductIdAscDateAsc();
}