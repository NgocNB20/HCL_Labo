package jp.co.itechh.quad.core.logic.memberinfo.csvoption.impl;

import jp.co.itechh.quad.core.dao.memberinfo.csvoption.CsvOptionDao;
import jp.co.itechh.quad.core.entity.memberinfo.csvoption.CsvOptionEntity;
import jp.co.itechh.quad.core.logic.memberinfo.csvoption.CsvOptionUpdateLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The type Csv option update logic.
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class CsvOptionUpdateLogicImpl implements CsvOptionUpdateLogic {

    /** CSVオプションDao */
    private final CsvOptionDao csvOptionDao;

    /**
     * コンストラクタ.
     *
     * @param csvOptionDao
     */
    @Autowired
    public CsvOptionUpdateLogicImpl(CsvOptionDao csvOptionDao) {
        this.csvOptionDao = csvOptionDao;
    }

    /**
     * Execute.
     *
     * @param csvOptionEntity
     * @return the int
     */
    @Override
    public int execute(CsvOptionEntity csvOptionEntity) {
        return csvOptionDao.update(csvOptionEntity);
    }
}
