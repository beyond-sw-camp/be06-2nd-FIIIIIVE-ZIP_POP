package com.fiiiiive.zippop.favorite.model.response;

import com.fiiiiive.zippop.popup_store.model.PopupStore;
import com.fiiiiive.zippop.popup_store.model.response.GetPopupStoreRes;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetFavoriteRes {
    private GetPopupStoreRes getPopupStoreRes;
}
