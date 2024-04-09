/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.admin.pc.web.admin.shop.novelty;

import jp.co.itechh.quad.admin.constant.type.HTypeNoveltyPresentState;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * ノベルティプレゼント検索結果画面情報<br/>
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 *
 */
@Data
@Component
@Scope("prototype")
public class NoveltyModelItem implements Serializable {
    /**
     * シリアルバージョンID<br/>
     */
    private static final long serialVersionUID = 1L;

    /**
     * No
     */
    private Integer resultNo;
    /**
     * ノベルティプレゼント条件開始日
     */
    private Timestamp resultNoveltyPresentStartTime;
    /**
     * ノベルティプレゼント条件終了日
     */
    private Timestamp resultNoveltyPresentEndTime;
    /**
     * ノベルティプレゼント条件名
     */
    private String resultNoveltyPresentName;
    /**
     * ノベルティプレゼント条件状態
     */
    private String resultNoveltyPresentState;
    /**
     * ノベルティ商品
     */
    private String resultNoveltyPresentGoods;
    /**
     * ノベルティプレゼント条件SEQ
     */
    private Integer noveltyPresentConditionSeq;
    /**
     * ノベルティプレゼント条件状態
     */
    private HTypeNoveltyPresentState resultNoveltyPresentStateType;

    /**
     * ノベルティプレゼント条件状態が有効かどうか
     * @return ノベルティプレゼント条件状態が有効の場合にTRUEを返却
     */
    public boolean isNoveltyPresentStateValid() {
        if (HTypeNoveltyPresentState.VALID.equals(resultNoveltyPresentStateType)) {
            return true;
        }
        return false;
    }

    /**
     * ノベルティプレゼント条件状態が無効かどうか
     * @return ノベルティプレゼント条件状態が無効の場合にTRUEを返却
     */
    public boolean isNoveltyPresentStateInvalid() {
        if (HTypeNoveltyPresentState.INVALID.equals(resultNoveltyPresentStateType)) {
            return true;
        }
        return false;
    }
}
