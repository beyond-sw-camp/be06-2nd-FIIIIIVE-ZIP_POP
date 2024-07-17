package com.fiiiiive.zippop.popup_goods;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PopupGoodsRepository extends JpaRepository<PopupGoods, Long> {
    public List<PopupGoods> findByProductPrice(Integer price);

    public List<PopupGoods> findByProductName(String product_name);

//    public Optional<List<PopupGoods>> findByStoreName(String store_name);

    @Query("SELECT pg FROM PopupGoods pg JOIN pg.popupStore ps WHERE ps.storeName = :storeName")
    public Optional<List<PopupGoods>> findByStoreName(@Param("storeName") String storeName);
}
