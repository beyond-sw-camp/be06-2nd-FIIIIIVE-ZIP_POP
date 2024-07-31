package com.fiiiiive.zippop.popup_store;

import com.fiiiiive.zippop.popup_store.model.PopupStoreLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PopupStoreLikeRepository extends JpaRepository<PopupStoreLike, Long> {
    @Query("SELECT psl FROM PopupStoreLike psl " +
            "JOIN FETCH psl.customer " +
            "JOIN FETCH psl.popupStore " +
            "WHERE psl.customer.customerIdx = :customerIdx AND psl.popupStore.storeIdx = :storeIdx")
    Optional<PopupStoreLike> findByCustomerIdxAndStoreIdx(Long customerIdx, Long storeIdx);

    @Modifying
    @Query("DELETE FROM PopupStoreLike psl " +
            "WHERE psl.customer.customerIdx = :customerIdx AND psl.popupStore.storeIdx = :storeIdx")
    void deleteByCustomerIdxAndStoreIdx(Long customerIdx, Long storeIdx);
}
