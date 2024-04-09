package jp.co.itechh.quad.admin.pc.web.admin.goods.registupdate;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@Component
@Scope("prototype")
public class GoodsRegistUpdateIconItem implements Serializable {

    /**
     * serialVersionUID<br/>
     */
    private static final long serialVersionUID = 1L;

    // 商品インフォメーションアイコン情報
    /**
     * アイコンSEQ
     */
    private Integer iconSeq;

    /**
     * アイコン名
     */
    private String iconName;

    /**
     * カラーコード
     */
    private String colorCode;

    /**
     * チェックボックス
     */
    private boolean checkBoxPc;

    /************************************
     ** 条件判定
     ************************************/

    /**
     * チェックボックスのチェック判定<br/>
     *
     * @return true=チェックされている場合
     */
    public boolean isCheckedPcIcon() {
        return checkBoxPc;
    }

}
