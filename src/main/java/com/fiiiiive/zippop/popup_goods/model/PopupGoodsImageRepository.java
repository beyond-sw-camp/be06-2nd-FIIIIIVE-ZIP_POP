package com.fiiiiive.zippop.popup_goods.model;

import com.fiiiiive.zippop.popup_review.model.PopupReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PopupGoodsImageRepository extends JpaRepository<PopupGoodsImage, Long> { }
