package jp.co.itechh.quad.admin.pc.web.admin.goods.registupdate;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * 商品管理：商品登録更新（関連商品設定－検索結果）ページ情報<br/>
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Data
@Component
@Scope("prototype")
public class GoodsRegistUpdateRelationSearchItem implements Serializable {

    /**
     * シリアルバージョンID<br/>
     */
    private static final long serialVersionUID = 1L;

    /************************************
     ** 商品規格項目
     ************************************/
    /**
     * No<br/>
     */
    private Integer resultDspNo;

    /**
     * チェックボックス<br/>
     */
    private boolean resultCheck;

    /**
     * 商品グループSEQ<br/>
     */
    private Integer resultGoodsGroupSeq;

    /**
     * 商品グループコード<br/>
     */
    private String resultGoodsGroupCode;

    /**
     * 商品グループ名<br/>
     */
    private String resultGoodsGroupName;

    /**
     * 公開状態PC<br/>
     */
    private String resultGoodsOpenStatusPC;

}
