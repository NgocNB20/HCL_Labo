/*
 * Project Name : HIT-MALL4
 */

package jp.co.itechh.quad.admin.pc.web.admin.goods.category;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * カテゴリ管理：カテゴリ入力
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Data
@Component
@Scope("prototype")
public class CategoryTreeItem implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** カテゴリID */
    private String categoryId;

    /** カテゴリSEQ */
    private Integer value;

    /** カテゴリSeqPath */
    private String categorySeqPath;

    /** カテゴリ確認用連結文字列 */
    private String categoryPathName;

    /** PCテンプ画像をアップロードしてるかどうか */
    private boolean tmpImagePC;

    /** PCアップロードファイル名 */
    private String fileNamePC;

    /** 一覧画面遷移フラグ */
    private boolean listScreen;

    /************************************
     ** カテゴリ項目
     ************************************/
    /**
     * /** ターゲットカテゴリ <br/>
     */
    private Integer targetValue;

    /**
     * カテゴリSEQ<br/>
     */
    private Integer categorySeq;

    /**
     * カテゴリ名<br/>
     */
    private String tCategoryName;

    /**
     * カテゴリ階層<br/>
     */
    private Integer categoryLevel;

    /************************************
     ** カテゴリ字下げ用
     ************************************/
    /**
     * カテゴリ字下げ文字列取得
     *
     * @return カテゴリ字下げ文字列
     */
    public String getCategoryIndent() {
        StringBuilder ret = new StringBuilder();
        for (int i = 1; categoryLevel != null && i < categoryLevel; i++) {
            ret.append("　　　");
        }
        return ret.toString();
    }

}
