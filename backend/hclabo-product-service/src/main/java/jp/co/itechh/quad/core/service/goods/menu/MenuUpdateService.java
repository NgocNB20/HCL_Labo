package jp.co.itechh.quad.core.service.goods.menu;

import jp.co.itechh.quad.core.entity.goods.menu.MenuEntity;

import javax.mail.MessagingException;

/**
 * メニュー更新サービス実装クラス
 *
 * @author Pham Quang Dieu
 */
public interface MenuUpdateService {

    /**
     * アップデート
     *
     * @param menuEntity メニューエンティティ
     * @return 処理件数
     */
    void execute(MenuEntity menuEntity) throws MessagingException;
}