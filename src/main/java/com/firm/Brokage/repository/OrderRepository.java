package com.firm.Brokage.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.firm.Brokage.entity.Customer;
import com.firm.Brokage.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
	List<Order> findByCustomerAndCreateDateBetween(Customer customer, LocalDate startDate, LocalDate endDate);

	List<Order> findByCustomer(Customer customer);

	List<Order> findByCustomerAndStatus(Customer customer, String status);
}
