/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.shop.news;

import jp.co.itechh.quad.core.entity.shop.news.NewsEntity;

/**
 * ニュース更新
 *
 * @author shibuya
 * @version $Revision: 1.1 $
 *
 */
public interface NewsUpdateService {

    /* メッセージ SSN0005 */

    /**
     * ニュース更新失敗エラー<br/>
     * <code>MSGCD_NEWS_UPDATE_FAIL</code>
     */
    String MSGCD_NEWS_UPDATE_FAIL = "SSN000501";

    /**
     * ニュース更新
     *
     * @param newsEntity ニュースエンティティ
     * @return 処理件数
     */
    int execute(NewsEntity newsEntity);

}
