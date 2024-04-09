package jp.co.itechh.quad.core.service.goods.menu.impl;

import jp.co.itechh.quad.core.entity.goods.menu.MenuEntity;
import jp.co.itechh.quad.core.logic.goods.menu.MenuUpdateLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.goods.menu.MenuUpdateService;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

/**
 * メニュー更新サービス実装クラス
 * @author Pham Quang Dieu
 */
@Service
public class MenuUpdateServiceImpl extends AbstractShopService implements MenuUpdateService {

    /** メニュー更新 */
    private final MenuUpdateLogic menuUpdateLogic;

    /**
     * メニュー更新サービス実装クラス
     *
     * @param menuUpdateLogic メニュー更新
     */
    public MenuUpdateServiceImpl(MenuUpdateLogic menuUpdateLogic) {
        this.menuUpdateLogic = menuUpdateLogic;
    }

    /**
     * アップデート
     *
     * @param menuEntity メニューエンティティ
     * @return 処理件数
     */
    @Override
    public void execute(MenuEntity menuEntity) throws MessagingException {
        int count = menuUpdateLogic.execute(menuEntity);
        if (count != 1) {
            throw new MessagingException();
        }
    }
}
