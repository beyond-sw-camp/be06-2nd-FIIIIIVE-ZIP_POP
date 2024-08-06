package com.fiiiiive.zippop.cart;

import com.fiiiiive.zippop.cart.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {
    @Query("SELECT c FROM Cart c " +
            "JOIN FETCH c.customer " +
            "JOIN FETCH c.popupGoods "+
            "WHERE c.customer.email = :customerEmail")
    Optional<List<Cart>> findAllByCustomerEmail(String customerEmail);

    @Query("SELECT c FROM Cart c " +
            "JOIN FETCH c.customer " +
            "JOIN FETCH c.popupGoods " +
            "WHERE c.customer.email = :customerEmail and c.popupGoods.productIdx = :productIdx")
    Optional<Cart> findByCustomerEmailAndProductIdx(String customerEmail, Long productIdx);

    @Query("SELECT c FROM Cart c WHERE c.customer.email = :customerEmail and c.cartIdx = :cartIdx")
    Optional<Cart> findByIdAndCustomerEmail(Long cartIdx, String customerEmail);

    @Modifying
    @Query("DELETE FROM Cart c WHERE c.cartIdx = :cartIdx and c.customer.email = :customerEmail")
    void deleteByIdAndCustomerEmail(Long cartIdx, String customerEmail);

    @Modifying
    @Query("DELETE FROM Cart c WHERE c.customer.email = :customerEmail")
    void deleteAllByCustomerEmail(String customerEmail);
}
