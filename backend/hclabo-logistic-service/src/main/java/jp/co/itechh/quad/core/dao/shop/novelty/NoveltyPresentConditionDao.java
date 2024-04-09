 /*
  * Project Name : HIT-MALL4
  *
  * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
  *
  */
 package jp.co.itechh.quad.core.dao.shop.novelty;

 import jp.co.itechh.quad.core.dto.shop.novelty.NoveltyPresentSearchForDaoConditionDto;
 import jp.co.itechh.quad.core.entity.shop.novelty.NoveltyPresentConditionEntity;
 import org.seasar.doma.Dao;
 import org.seasar.doma.Delete;
 import org.seasar.doma.Insert;
 import org.seasar.doma.Select;
 import org.seasar.doma.Update;
 import org.seasar.doma.boot.ConfigAutowireable;
 import org.seasar.doma.jdbc.SelectOptions;

 import java.util.List;

 /**
  * ノベルティプレゼント条件Daoクラス<br/>
  *
  * @author PHAM QUANG DIEU (VJP)
  */
 @Dao
 @ConfigAutowireable
 public interface NoveltyPresentConditionDao {
     /**
      * インサート
      *
      * @param noveltyPresentCondition ノベルティプレゼント条件エンティティ
      * @return 処理件数
      */
     @Insert(excludeNull = true)
     int insert(NoveltyPresentConditionEntity noveltyPresentCondition);

     /**
      * アップデート
      *
      * @param noveltyPresentCondition ノベルティプレゼント条件エンティティ
      * @return 処理件数
      */
     @Update
     int update(NoveltyPresentConditionEntity noveltyPresentCondition);

     /**
      * デリート
      *
      * @param noveltyPresentCondition ノベルティプレゼント条件エンティティ
      * @return 処理件数
      */
     @Delete
     int delete(NoveltyPresentConditionEntity noveltyPresentCondition);

     /**
      * エンティティ取得
      *
      * @param noveltyPresentConditionSeq ノベルティプレゼント条件SEQ
      * @return カテゴリエンティティDTO
      */
     @Select
     NoveltyPresentConditionEntity getEntity(Integer noveltyPresentConditionSeq);

     /**
      * 検索条件に一致するレコードを取得する
      *
      * @param conditionDto 検索条件
      * @return 検索結果
      */
     @Select
     List<NoveltyPresentConditionEntity> getListByCondition(NoveltyPresentSearchForDaoConditionDto conditionDto,
                                                            SelectOptions selectOptions);

     /**
      * 除外条件用のレコードを取得する
      *
      * @param noveltyPresentConditionSeq 除外するノベルティプレゼント条件SEQ
      * @return 検索結果
      */
     @Select
     List<NoveltyPresentConditionEntity> getExclusionListBySeq(Integer noveltyPresentConditionSeq);

     /**
      * ノベルティプレゼント条件SEQのリストからエンティティのリストを取得
      *
      * @param noveltyPresentConditionSeqList ノベルティプレゼント条件SEQのリスト
      * @return エンティティのリスト
      */
     @Select
     List<NoveltyPresentConditionEntity> getListBySeqList(List<Integer> noveltyPresentConditionSeqList);

     /**
      * 検索条件に一致するレコードを取得する
      *
      * @param conditionDto 検索条件
      * @return 検索結果
      */
     @Select
     List<NoveltyPresentConditionEntity> getJudgmentListByCondition(NoveltyPresentSearchForDaoConditionDto conditionDto);

     /**
      * 検索条件に一致するレコードを取得する
      *
      * @return 検索結果 novelty present condition seq
      */
     @Select
     int getNoveltyPresentConditionSeq();
 }