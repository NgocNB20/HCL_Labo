package jp.co.itechh.quad.admin.entity.shop.novelty;

import jp.co.itechh.quad.admin.constant.type.HTypeEnclosureUnitType;
import jp.co.itechh.quad.admin.constant.type.HTypeGoodsPriceTotalFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeMagazineSendFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeNoveltyPresentState;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * ノベルティプレゼント条件クラス<br/>
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Data
@Component
@Scope("prototype")
public class NoveltyPresentConditionEntity implements Serializable {
    /**
     * serialVersionUID
     */
    public static final long serialVersionUID = 1L;

    /** ノベルティプレゼント条件SEQ */
    public Integer noveltyPresentConditionSeq;

    /** ノベルティプレゼント条件名 */
    public String noveltyPresentName;

    /** 同梱単位区分 */
    public HTypeEnclosureUnitType enclosureUnitType;

    /** ノベルティプレゼント条件状態 */
    public HTypeNoveltyPresentState noveltyPresentState;

    /** ノベルティプレゼント条件開始日時 */
    public Timestamp noveltyPresentStartTime;

    /** ノベルティプレゼント条件終了日時 */
    public Timestamp noveltyPresentEndTime;

    /** 除外条件SEQ */
    public String exclusionNoveltyPresentSeq;

    /** メールマガジン配信条件フラグ */
    public HTypeMagazineSendFlag magazineSendFlag;

    /** 入会開始日時 */
    public Timestamp admissionStartTime;

    /** 入会終了日時 */
    public Timestamp admissionEndTime;

    /** 商品管理番号 */
    public String goodsGroupCode;

    /** 商品番号 */
    public String goodsCode;

    /** カテゴリーID */
    public String categoryId;

    /** アイコンID */
    public String iconId;

    /** 商品名 */
    public String goodsName;

    /** 検索キーワード */
    public String searchKeyword;

    /** 商品金額合計 */
    public BigDecimal goodsPriceTotal;

    /** 商品金額条件フラグ */
    public HTypeGoodsPriceTotalFlag goodsPriceTotalFlag;

    /** プレゼント数制限 */
    public Integer prizeGoodsLimit;

    /** 管理メモ */
    public String memo;

    /** 登録日時 */
    public Timestamp registTime;

    /** 更新日時 */
    public Timestamp updateTime;
}