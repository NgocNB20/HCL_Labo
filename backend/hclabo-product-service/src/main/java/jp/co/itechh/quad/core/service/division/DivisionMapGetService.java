/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */
package jp.co.itechh.quad.core.service.division;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 分類リスト取得サービス
 *
 * @author wh4200
 * @version $Revision: 1.3 $
 */
public interface DivisionMapGetService {

    public Map<BigDecimal, String> getTaxRateMapList();

}
