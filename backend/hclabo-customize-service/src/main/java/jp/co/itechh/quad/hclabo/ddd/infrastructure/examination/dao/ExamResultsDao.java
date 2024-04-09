package jp.co.itechh.quad.hclabo.ddd.infrastructure.examination.dao;

import jp.co.itechh.quad.hclabo.ddd.infrastructure.examination.dbentity.ExamResultsDbEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.List;

/**
 * 検査結果Daoクラス
 */
@Dao
@ConfigAutowireable
public interface ExamResultsDao {

    /**
     * インサート
     *
     * @param examResultsDbEntity 検査結果Dbエンティティ
     */
    @Insert(excludeNull = true)
    int insert(ExamResultsDbEntity examResultsDbEntity);

    /**
     * アップデート
     *
     * @param examResultsDbEntity 検査結果Dbエンティティ
     */
    @Update
    int update(ExamResultsDbEntity examResultsDbEntity);

    /**
     * 検査キット番号による検査結果削除
     *
     * @param examKitCode 検査キット番号
     * @return 削除の番号
     */
    @Delete(sqlFile = true)
    int deleteByExamKitCode(String examKitCode);

    /**
     * 検査キット番号で検査結果取得<br/>
     *
     * @param examKitCode 検査キット番号
     * @return 検査結果リスト
     */
    @Select
    List<ExamResultsDbEntity> getByExamKitCode(String examKitCode);

    /**
     * 検査キット番号リストで検査結果取得<br/>
     *
     * @param examKitCodeList 検査キット番号リスト
     * @return 検査結果リスト
     */
    @Select
    List<ExamResultsDbEntity> getExamResultsList(List<String> examKitCodeList);

}