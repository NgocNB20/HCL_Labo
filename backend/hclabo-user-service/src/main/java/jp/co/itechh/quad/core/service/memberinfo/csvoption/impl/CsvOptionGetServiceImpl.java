package jp.co.itechh.quad.core.service.memberinfo.csvoption.impl;

import jp.co.itechh.quad.core.entity.memberinfo.csvoption.CsvOptionEntity;
import jp.co.itechh.quad.core.logic.memberinfo.csvoption.CsvOptionGetLogic;
import jp.co.itechh.quad.core.service.memberinfo.csvoption.CsvOptionGetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * CsvOptionGetService.
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Service
public class CsvOptionGetServiceImpl implements CsvOptionGetService {

    /** csvOptionGetLogic */
    private final CsvOptionGetLogic csvOptionGetLogic;

    /**
     * コンストラクタ.
     *
     * @param csvOptionGetLogic
     */
    @Autowired
    public CsvOptionGetServiceImpl(CsvOptionGetLogic csvOptionGetLogic) {
        this.csvOptionGetLogic = csvOptionGetLogic;
    }

    /**
     * execute<br/>
     *
     * @param optionId
     * @return CSVオプションEntity
     */
    @Override
    public CsvOptionEntity execute(Integer optionId) {
        return csvOptionGetLogic.execute(optionId);
    }

    /**
     * execute<br/>
     *
     * @return CSVオプションEntityクラス
     */
    @Override
    public List<CsvOptionEntity> execute() {
        return csvOptionGetLogic.execute();
    }
}
