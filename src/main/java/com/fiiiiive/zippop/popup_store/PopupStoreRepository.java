package com.fiiiiive.zippop.popup_store;

import com.fiiiiive.zippop.popup_store.model.PopupStore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.*;

public interface PopupStoreRepository extends JpaRepository<PopupStore, Long> {

    Page<PopupStore> findByCategory(String category, Pageable pageable);

    @Query("SELECT ps FROM PopupStore ps JOIN FETCH ps.popupGoodsList WHERE ps.category = :category")
    Page<PopupStore> findByCategoryFetchJoin(String category, Pageable pageable);

    Optional<PopupStore> findByStoreName(String storeName);

    @Query("SELECT ps FROM PopupStore ps JOIN FETCH ps.popupGoodsList WHERE ps.storeName = :storeName")
    Optional<PopupStore> findByStoreNameFetchJoin(String storeName);

    Page<PopupStore> findByStoreAddr(String storeAddr,Pageable pageable);

    @Query("SELECT ps FROM PopupStore ps JOIN FETCH ps.popupGoodsList WHERE ps.storeAddr = :storeAddr")
    Page<PopupStore> findByStoreAddrFetchJoin(String storeAddr,Pageable pageable);

    Page<PopupStore> findByCompanyIdx(Long companyIdx,Pageable pageable);

    @Query("SELECT ps FROM PopupStore ps JOIN FETCH ps.popupGoodsList WHERE ps.company.idx = :companyIdx")
    Page<PopupStore> findByCompanyIdxFetchJoin(Long companyIdx,Pageable pageable);


    Page<PopupStore> findByStoreDate (String storeDate,Pageable pageable);

    @Query("SELECT ps FROM PopupStore ps JOIN FETCH ps.popupGoodsList WHERE ps.storeDate = :storeDate")
    Page<PopupStore> findByStoreDateFetchJoin(String storeDate,Pageable pageable);
}