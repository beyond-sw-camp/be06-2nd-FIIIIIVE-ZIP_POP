package com.fiiiiive.zippop.orders;

import com.fiiiiive.zippop.orders.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Orders, Integer> {
//    Optional<Orders> findByCustomerAndPopupGoods(Customer customer, PopupGoods popupGoods);
}