package jp.co.itechh.quad.core.dao.memberinfo.csvoption;

import jp.co.itechh.quad.core.entity.memberinfo.csvoption.CsvOptionEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.List;

/**
 * CSVオプションDaoクラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Dao
@ConfigAutowireable
public interface CsvOptionDao {

    /**
     * 更新
     *
     * @param csvOptionEntity CSVオプションEntityクラス
     * @return 更新件数 int
     */
    @Update
    int update(CsvOptionEntity csvOptionEntity);

    /**
     * CSVオプションEntity取得.
     *
     * @param optionId
     * @return CSVオプションEntity
     */
    @Select
    CsvOptionEntity getByOptionId(Integer optionId);

    /**
     * CSVオプションEntityリスト取得.
     *
     * @return CSVオプションEntityリスト
     */
    @Select
    List<CsvOptionEntity> getAll();
}