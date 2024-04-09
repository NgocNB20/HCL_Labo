package jp.co.itechh.quad.core.service.card.impl;

import jp.co.itechh.quad.card.presentation.api.CardApi;
import jp.co.itechh.quad.core.service.card.CardAdapter;
import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * カードアダプター実装クラス
 *
 * @author Pham Quang Dieu
 */
@Service
public class CardAdapterImpl implements CardAdapter {

    /** クレジットカードAPI */
    private final CardApi cardApi;

    @Autowired
    public CardAdapterImpl(CardApi cardApi, HeaderParamsUtility headerParamsUtil) {
        this.cardApi = cardApi;

        // サービス間のAPI呼び出す時、ヘッダーにパラメーターを設定する
        headerParamsUtil.setHeader(this.cardApi.getApiClient());
    }

    /**
     * 登録済みクレジットカード情報削除
     */
    @Override
    public void deleteCardAdapter() {
        // 登録済みクレジットカード情報削除
        this.cardApi.delete();
    }
}