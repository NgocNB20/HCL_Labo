/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dao.common;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.boot.ConfigAutowireable;

/**
 * 端末識別番号SEQDao<br/>
 *
 * @author thang
 *
 */
@Dao
@ConfigAutowireable
public interface AccessUidDao {
    /**
     * 端末識別番号SEQ採番<br/>
     *
     * @return 端末識別番号SEQ
     */
    @Select
    String getNextVal();
}
