package jp.co.itechh.quad.core.logic.memberinfo.csvoption.impl;

import jp.co.itechh.quad.core.dao.memberinfo.csvoption.CsvOptionDao;
import jp.co.itechh.quad.core.entity.memberinfo.csvoption.CsvOptionEntity;
import jp.co.itechh.quad.core.logic.memberinfo.csvoption.CsvOptionGetLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * The type Csv option get logic.
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class CsvOptionGetLogicImpl implements CsvOptionGetLogic {

    /** CSVオプションDao */
    private final CsvOptionDao csvOptionDao;

    /**
     * コンストラクタ.
     *
     * @param csvOptionDao     CSVオプションDao
     */
    @Autowired
    public CsvOptionGetLogicImpl(CsvOptionDao csvOptionDao) {
        this.csvOptionDao = csvOptionDao;
    }

    /**
     * execute<br/>
     *
     * @param optionId
     * @return CSVオプションDto
     */
    @Override
    public CsvOptionEntity execute(Integer optionId) {
        return csvOptionDao.getByOptionId(optionId);
    }

    /**
     * execute<br/>
     *
     * @return CSVオプションDtoクラス
     */
    @Override
    public List<CsvOptionEntity> execute() {
        return csvOptionDao.getAll();
    }
}
