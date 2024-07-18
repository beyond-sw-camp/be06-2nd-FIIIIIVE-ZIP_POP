package com.fiiiiive.zippop.orders;


import com.fiiiiive.zippop.orders.model.OrdersDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersDetailRepository extends JpaRepository<OrdersDetail, Integer> {
}