package com.fiiiiive.zippop.popup_goods;

import com.fiiiiive.zippop.popup_goods.model.PopupGoods;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PopupGoodsRepository extends JpaRepository<PopupGoods, Long> {

    Page<PopupGoods> findAll(Pageable pageable);

    @Query("SELECT pg FROM PopupGoods pg JOIN FETCH pg.popupStore")
    Page<PopupGoods> findAllFetchJoin(Pageable pageable);

    Page<PopupGoods> findByProductPrice(Integer productPrice, Pageable pageable);

    @Query("SELECT pg FROM PopupGoods pg JOIN FETCH pg.popupStore WHERE pg.productPrice = :productPrice")
    Page<PopupGoods> findByProductPriceFetchJoin(Integer productPrice, Pageable pageable);

    Page<PopupGoods> findByProductName(String productName, Pageable pageable);

    @Query("SELECT pg FROM PopupGoods pg JOIN FETCH pg.popupStore WHERE pg.productName = :productName")
    Page<PopupGoods> findByProductNameFetchJoin(String productName, Pageable pageable);

    public Page<PopupGoods> findByStoreName(String storeName, Pageable pageable);

    @Query("SELECT pg FROM PopupGoods pg JOIN FETCH pg.popupStore ps WHERE ps.storeName = :storeName")
    Page<PopupGoods> findByStoreNameFetchJoin(String storeName, Pageable pageable);

}
