package com.fiiiiive.zippop.popup_reserve;

import com.fiiiiive.zippop.popup_reserve.model.PopupReserve;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface PopupReserveRepository extends JpaRepository<PopupReserve, Long> { }