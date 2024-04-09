package jp.co.itechh.quad.hclabo.ddd.infrastructure.examination.dao;

import jp.co.itechh.quad.hclabo.ddd.infrastructure.examination.dbentity.ExamKitDbEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.List;

/**
 * 検査キットDaoクラス
 */
@Dao
@ConfigAutowireable
public interface ExamKitDao {

    /**
     * インサート
     *
     * @param examKitDbEntity 検査キットDbエンティティ
     */
    @Insert(excludeNull = true)
    int insert(ExamKitDbEntity examKitDbEntity);

    /**
     * アップデート
     *
     * @param examKitDbEntity 検査キットDbエンティティ
     */
    @Update
    int update(ExamKitDbEntity examKitDbEntity);

    /**
     * 検査キット番号で取得<br/>
     *
     * @param examKitCode 検査キット番号
     * @return 検査キット
     */
    @Select
    ExamKitDbEntity getByExamKitCode(String examKitCode);

    /**
     * 注文商品IDで取得<br/>
     *
     * @param orderItemId 注文商品ID
     * @return 検査キット
     */
    @Select
    ExamKitDbEntity getByOrderItemId(String orderItemId);

    /**
     * 注文商品IDリストで取得<br/>
     *
     * @param orderItemIdList 注文商品IDリスト
     * @return 検査キットリスト
     */
    @Select
    List<ExamKitDbEntity> getExamKitEntityList(List<String> orderItemIdList);



    /**
     * 検査キット番号用連番取得
     *
     * @return 検査キット番号用連番
     */
    @Select
    int getExamKitCodeSeq();

}