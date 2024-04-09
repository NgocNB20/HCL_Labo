/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.shop.news;

import jp.co.itechh.quad.core.entity.shop.news.NewsEntity;

/**
 * ニュース登録
 *
 * @author shibuya
 * @version $Revision: 1.1 $
 *
 */
public interface NewsRegistService {

    /* メッセージ SSN0004 */

    /**
     * ニュース登録失敗エラー<br/>
     * <code>MSGCD_NEWS_REGIST_FAIL</code>
     */
    String MSGCD_NEWS_REGIST_FAIL = "SSN000401";

    /**
     * ニュース登録
     *
     * @param newsEntity ニュースエンティティ
     * @return 処理件数
     */
    int execute(NewsEntity newsEntity);

}
