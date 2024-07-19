package com.fiiiiive.zippop.popup_goods;

import com.fiiiiive.zippop.popup_goods.model.PopupGoods;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PopupGoodsRepository extends JpaRepository<PopupGoods, Long> {

    @Query("SELECT pg FROM PopupGoods pg")
    List<PopupGoods> findAll();

    @Query("SELECT pg FROM PopupGoods pg JOIN FETCH pg.popupStore")
    List<PopupGoods> findAllWithStore();

    @Query("SELECT pg FROM PopupGoods pg WHERE pg.productPrice = :productPrice")
    Optional<List<PopupGoods>> findByProductPrice(Integer productPrice);

    @Query("SELECT pg FROM PopupGoods pg JOIN FETCH pg.popupStore WHERE pg.productPrice = :productPrice")
    Optional<List<PopupGoods>> findByProductPriceWithStore(Integer productPrice);

    @Query("SELECT pg FROM PopupGoods pg WHERE pg.productName = :productName")
    Optional<List<PopupGoods>> findByProductName(String productName);

    @Query("SELECT pg FROM PopupGoods pg JOIN FETCH pg.popupStore WHERE pg.productName = :productName")
    Optional<List<PopupGoods>> findByProductNameWithStore(String productName);

    @Query("SELECT pg FROM PopupGoods pg WHERE pg.storeName = :storeName")
    public Optional<List<PopupGoods>> findByStoreName(String storeName);

    @Query("SELECT pg FROM PopupGoods pg JOIN FETCH pg.popupStore ps WHERE ps.storeName = :storeName")
    Optional<List<PopupGoods>> findByStoreNameWithStore(String storeName);

    // 비관적락 잠금 설정
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT pg FROM PopupGoods pg WHERE pg.productIdx = :productIdx")
    Optional<PopupGoods> findByIdx(@Param("productIdx") Long productIdx);
}
