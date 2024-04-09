package jp.co.itechh.quad.core.service.memberinfo.csvoption;

import jp.co.itechh.quad.core.entity.memberinfo.csvoption.CsvOptionEntity;

import java.util.List;

/**
 * The interface Csv option get service.
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
public interface CsvOptionGetService {

    /**
     * Execute.
     *
     * @param optionId
     * @return CSVオプションEntity
     */
    CsvOptionEntity execute(Integer optionId);

    /**
     * Execute.
     *
     * @return CSVオプションEntityクラス
     */
    List<CsvOptionEntity> execute();
}
