/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.goods;

import jp.co.itechh.quad.core.dto.goods.goods.GoodsCsvDto;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsSearchForBackDaoConditionDto;

import java.util.stream.Stream;

/**
 * 商品検索CSV一括ダウンロードロジックインターフェース
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 *
 */
public interface GoodsAllCsvDownloadLogic {
    Stream<GoodsCsvDto> execute(GoodsSearchForBackDaoConditionDto conditionDto);
}
