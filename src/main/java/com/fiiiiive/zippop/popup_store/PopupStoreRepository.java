package com.fiiiiive.zippop.popup_store;

import com.fiiiiive.zippop.popup_store.model.PopupStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface PopupStoreRepository extends JpaRepository<PopupStore, Long> {

    Optional<List<PopupStore>> findByCategory(String category);

    @Query("SELECT ps FROM PopupStore ps JOIN FETCH ps.popupGoodsList WHERE ps.category = :category")
    Optional<List<PopupStore>> findByCategoryWithGoods(String category);

    Optional<PopupStore> findByStoreName(String storeName);

    @Query("SELECT ps FROM PopupStore ps JOIN FETCH ps.popupGoodsList WHERE ps.storeName = :storeName")
    Optional<PopupStore> findByStoreNameWithGoods(String storeName);

    Optional<List<PopupStore>> findByStoreAddr(String storeAddr);

    @Query("SELECT ps FROM PopupStore ps JOIN FETCH ps.popupGoodsList WHERE ps.storeAddr = :storeAddr")
    Optional<List<PopupStore>> findByStoreAddrWithGoods(String storeAddr);

    @Query("SELECT ps FROM PopupStore ps JOIN FETCH ps.company WHERE ps.company.companyIdx = :companyIdx")
    Optional<List<PopupStore>> findByCompanyIdx(Long companyIdx);

    @Query("SELECT ps FROM PopupStore ps JOIN FETCH ps.popupGoodsList WHERE ps.company.companyIdx = :companyIdx")
    Optional<List<PopupStore>> findByCompanyIdxWithGoods(Long companyIdx);

    Optional<List<PopupStore>> findByStoreDate (String storeDate);

    @Query("SELECT ps FROM PopupStore ps JOIN FETCH ps.popupGoodsList WHERE ps.storeDate = :storeDate")
    Optional<List<PopupStore>> findByStoreDateWithGoods (String storeDate);
}