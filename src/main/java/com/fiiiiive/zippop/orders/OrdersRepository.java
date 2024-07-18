package com.fiiiiive.zippop.orders;


import com.fiiiiive.zippop.member.model.Customer;
import com.fiiiiive.zippop.orders.model.Orders;
import com.fiiiiive.zippop.popup_goods.PopupGoods;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrdersRepository extends JpaRepository<Orders, Integer> {
//    Optional<Orders> findByCustomerAndPopupGoods(Customer customer, PopupGoods popupGoods);
}