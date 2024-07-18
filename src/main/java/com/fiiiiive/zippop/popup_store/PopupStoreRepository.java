package com.fiiiiive.zippop.popup_store;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PopupStoreRepository extends JpaRepository<PopupStore, Long> {
    public List<PopupStore> findByCategory(String category);

    public PopupStore findByStoreName(String name);

    public List<PopupStore> findByStoreAddr(String store_addr);

    public List<PopupStore> findByStoreDate(String store_date);
}
