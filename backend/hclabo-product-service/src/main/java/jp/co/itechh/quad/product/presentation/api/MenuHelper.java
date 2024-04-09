package jp.co.itechh.quad.product.presentation.api;

import jp.co.itechh.quad.core.entity.goods.menu.MenuEntity;
import jp.co.itechh.quad.menu.presentation.api.param.MenuResponse;
import jp.co.itechh.quad.menu.presentation.api.param.MenuUpdateRequest;
import org.springframework.stereotype.Component;

/**
 * メニュー Helper
 */
@Component
public class MenuHelper {

    /**
     * メニューエンティティに変換
     *
     * @param menuUpdateRequest メニュー更新リクエスト
     * @return メニューエンティティ
     */
    public MenuEntity toMenuEntity(MenuUpdateRequest menuUpdateRequest) {
        MenuEntity menuEntity = new MenuEntity();

        menuEntity.setMenuId(1001);
        menuEntity.setCategoryTree(menuUpdateRequest.getCategoryTreeJson());

        return menuEntity;
    }

    /**
     * メニューレスポンスに変換
     *
     * @param menuEntity メニューエンティティ
     * @return メニューレスポンス
     */
    public MenuResponse toMenuResponse(MenuEntity menuEntity) {
        MenuResponse menuResponse = null;

        if (menuEntity != null) {
            menuResponse = new MenuResponse();
            menuResponse.setMenuSeq(menuEntity.getMenuId());
            menuResponse.setCategoryTreeJson(menuEntity.getCategoryTree());
            menuResponse.setRegistTime(menuEntity.getRegistTime());
            menuResponse.setUpdateTime(menuEntity.getUpdateTime());
        }

        return menuResponse;
    }
}