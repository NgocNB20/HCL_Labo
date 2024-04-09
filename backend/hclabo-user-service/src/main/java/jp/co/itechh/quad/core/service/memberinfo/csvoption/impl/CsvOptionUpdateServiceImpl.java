package jp.co.itechh.quad.core.service.memberinfo.csvoption.impl;

import jp.co.itechh.quad.core.entity.memberinfo.csvoption.CsvOptionEntity;
import jp.co.itechh.quad.core.logic.memberinfo.csvoption.CsvOptionUpdateLogic;
import jp.co.itechh.quad.core.service.memberinfo.csvoption.CsvOptionUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The type Csv option update service.
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Service
public class CsvOptionUpdateServiceImpl implements CsvOptionUpdateService {

    /** csvOptionUpdateLogic */
    private final CsvOptionUpdateLogic csvOptionUpdateLogic;

    /**
     * コンストラクタ.
     *
     * @param csvOptionUpdateLogic
     */
    @Autowired
    public CsvOptionUpdateServiceImpl(CsvOptionUpdateLogic csvOptionUpdateLogic) {
        this.csvOptionUpdateLogic = csvOptionUpdateLogic;
    }

    /**
     * Execute.
     *
     * @param csvOptionEntity
     * @return the int
     */
    @Override
    public int execute(CsvOptionEntity csvOptionEntity) {
        return csvOptionUpdateLogic.execute(csvOptionEntity);
    }
}
