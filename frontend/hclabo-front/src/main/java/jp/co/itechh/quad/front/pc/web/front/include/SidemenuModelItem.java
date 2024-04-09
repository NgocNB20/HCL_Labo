package jp.co.itechh.quad.front.pc.web.front.include;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * 共通サイドメニューAjax Modelアイテム
 *
 * @author kaneda
 */
@Data
@Component
@Scope("prototype")
public class SidemenuModelItem implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    // カテゴリ
    /** カテゴリID */
    private String cid;
    /** 階層付き通番 例）1-1-1 */
    private String hsn;
    /** カテゴリID */
    private String cidParent;
    /** カテゴリ表示名PC */
    private String displayName;
    /** 階層付き通番 */
    private String hierarchicalSerialNumber;
    /** カテゴリレベル */
    private Integer categoryLevel;
    /** カテゴリファイル名 */
    private String categoryFileName;
    /** 選択ルートカテゴリ */
    private boolean selectedRootCategory;
}