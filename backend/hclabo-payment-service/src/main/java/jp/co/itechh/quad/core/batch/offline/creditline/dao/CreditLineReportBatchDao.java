/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.batch.offline.creditline.dao;

import jp.co.itechh.quad.core.batch.offline.creditline.entity.CreditLineReport;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.boot.ConfigAutowireable;

/**
 * 与信枠解放Dao
 * @author Kaneko(itec) チケット#2725対応　2011/12/26
 *
 */
@Dao
@ConfigAutowireable
public interface CreditLineReportBatchDao {

    /**
     * インサート<br/>
     *
     * @param creditLineReport 与信枠解放エンティティ
     * @return 処理件数
     */
    @Insert(excludeNull = true)
    int insert(CreditLineReport creditLineReport);

}
