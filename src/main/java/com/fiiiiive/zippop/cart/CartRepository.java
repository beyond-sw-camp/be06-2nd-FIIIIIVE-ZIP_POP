package com.fiiiiive.zippop.cart;

import com.fiiiiive.zippop.cart.model.Cart;
import com.fiiiiive.zippop.member.model.Customer;
import com.fiiiiive.zippop.popup_goods.model.PopupGoods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {

    @Query("SELECT c FROM Cart c " +
            "JOIN FETCH c.customer " +
            "JOIN FETCH c.popupGoods "+
            "WHERE c.customer.customerIdx = :customerIdx")
    Optional<List<Cart>> findAllByCustomerIdx(Long customerIdx);

    @Query("SELECT c FROM Cart c " +
            "JOIN FETCH c.customer " +
            "JOIN FETCH c.popupGoods " +
            "WHERE c.customer.customerIdx = :customerIdx and c.popupGoods.productIdx = :productIdx")
    Optional<Cart> findByCustomerIdxAndProductIdx(@Param("customerIdx") Long customerIdx, @Param("productIdx") Long productIdx);

}
