/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.goods;

import jp.co.itechh.quad.core.dto.goods.goods.GoodsCsvDto;

import java.util.List;
import java.util.stream.Stream;

/**
 * 商品検索CSV一括ダウンロードロジックインターフェース
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 *
 */
public interface GoodsCsvDownloadLogic {
    Stream<GoodsCsvDto> execute(List<Integer> goodsSeqList);
}
