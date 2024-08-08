package com.fiiiiive.zippop.popup_store;

import com.fiiiiive.zippop.popup_store.model.PopupStore;
import com.fiiiiive.zippop.post.model.Post;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.*;

public interface PopupStoreRepository extends JpaRepository<PopupStore, Long> {
    // 팝업스토어 인덱스를 기반으로 팝업 스토어 조회
    Optional<PopupStore> findByStoreIdx(Long storeIdx);

    // 기업 인덱스를 기반으로 팝업 스토어 조회
    Optional<Page<PopupStore>> findByCompanyEmail(String companyEmail,Pageable pageable);
    @Query("SELECT ps FROM PopupStore ps JOIN FETCH ps.popupGoodsList WHERE ps.company.companyIdx = :companyIdx")
    Page<PopupStore> findByCompanyIdxFetchJoin(Long companyIdx,Pageable pageable);

    // 검색어 기반으로 전체 조회
    @Query("SELECT ps FROM PopupStore ps " +
            "WHERE ps.storeAddress LIKE %:keyword% " +
            "OR ps.storeName LIKE %:keyword% " +
            "OR ps.category LIKE %:keyword% " +
            "OR ps.companyEmail LIKE %:keyword%")
    Optional<Page<PopupStore>> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

    // 날짜 범위 기반으로 전체 조회
    @Query("SELECT ps FROM PopupStore ps " +
            "WHERE (:startDate IS NULL OR ps.storeStartDate >= :storeStartDate) " +
            "AND (:endDate IS NULL OR ps.storeEndDate <= :storeEndDate)")
    Optional<Page<PopupStore>> findByStoreDateRange (LocalDateTime storeStartDate, LocalDateTime storeEndDate, Pageable pageable);

    // 팝업스토어 이름을 기반으로 팝업 스토어 조회
    Optional<PopupStore> findByStoreName(String storeName);

    @Query("SELECT ps FROM PopupStore ps JOIN FETCH ps.popupGoodsList WHERE ps.storeName = :storeName")
    Optional<PopupStore> findByStoreNameFetchJoin(String storeName);

    Page<PopupStore> findByStoreAddress(String storeAddress,Pageable pageable);

    @Query("SELECT ps FROM PopupStore ps JOIN FETCH ps.popupGoodsList WHERE ps.storeAddress = :storeAddress")
    Page<PopupStore> findByStoreAddressFetchJoin(String storeAddress,Pageable pageable);

    @Query("SELECT ps FROM PopupStore ps JOIN FETCH ps.popupGoodsList WHERE ps.storeEndDate = :storeDate")
    Page<PopupStore> findByStoreEndDateFetchJoin(String storeDate,Pageable pageable);

    Optional<Page<PopupStore>> findByCategory(String category, Pageable pageable);

    @Query("SELECT ps FROM PopupStore ps JOIN FETCH ps.popupGoodsList WHERE ps.category = :category")
    Page<PopupStore> findByCategoryFetchJoin(String category, Pageable pageable);
}