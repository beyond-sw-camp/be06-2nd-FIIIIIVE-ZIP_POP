package com.fiiiiive.zippop.favorite;

import com.fiiiiive.zippop.favorite.model.Favorite;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    @Query("SELECT f FROM Favorite f " +
            "WHERE f.customer.customerIdx = :customerIdx " +
            "AND f.popupStore.storeIdx = :storeIdx")
    Optional<Favorite> findByCustomerIdxAndStoreIdx(Long customerIdx, Long storeIdx);

    @Query("SELECT f FROM Favorite f " +
            "JOIN FETCH f.customer " +
            "JOIN FETCH f.popupStore " +
            "WHERE f.customer.customerIdx = :customerIdx")
    Optional<List<Favorite>> findAllByCustomerIdx(Long customerIdx);
}
