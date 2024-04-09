/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.memberinfo.csvoption;

import jp.co.itechh.quad.core.entity.memberinfo.csvoption.CsvOptionEntity;

import java.util.List;

/**
 * The interface Csv option get logic.
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
public interface CsvOptionGetLogic {

    CsvOptionEntity execute(Integer optionId);

    List<CsvOptionEntity> execute();
}
