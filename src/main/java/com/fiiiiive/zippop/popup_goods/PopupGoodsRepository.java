package com.fiiiiive.zippop.popup_goods;

import com.fiiiiive.zippop.popup_goods.model.PopupGoods;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PopupGoodsRepository extends JpaRepository<PopupGoods, Long> {

    Page<PopupGoods> findAll(Pageable pageable);

    @Query("SELECT pg FROM PopupGoods pg JOIN FETCH pg.popupStore ps WHERE ps.storeIdx = :storeIdx")
    Optional<Page<PopupGoods>> findByStoreIdx(Long storeIdx, Pageable pageable);

    @Query("SELECT pg FROM PopupGoods pg JOIN FETCH pg.popupStore")
    Page<PopupGoods> findAllFetchJoin(Pageable pageable);

    Page<PopupGoods> findByProductPrice(Integer productPrice, Pageable pageable);

    @Query("SELECT pg FROM PopupGoods pg JOIN FETCH pg.popupStore WHERE pg.productPrice = :productPrice")
    Page<PopupGoods> findByProductPriceFetchJoin(Integer productPrice, Pageable pageable);

    Optional<Page<PopupGoods>> findByProductName(String productName, Pageable pageable);

    Optional<PopupGoods> findByProductIdx(Long productIdx);
//    @Query("SELECT pg FROM PopupGoods pg JOIN FETCH pg.popupStore WHERE pg.productName = :productName")
//    Page<PopupGoods> findByProductNameFetchJoin(String productName, Pageable pageable);
//
//    public Page<PopupGoods> findByStoreName(String storeName, Pageable pageable);
//
//    @Query("SELECT pg FROM PopupGoods pg JOIN FETCH pg.popupStore ps WHERE ps.storeName = :storeName")
//    Page<PopupGoods> findByStoreNameFetchJoin(String storeName, Pageable pageable);

    // 비관적락 잠금 설정
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT pg FROM PopupGoods pg WHERE pg.productIdx = :productIdx")
    Optional<PopupGoods> findByIdx(@Param("productIdx") Long productIdx);
}
