package jp.co.itechh.quad.front.pc.web.front.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * カード情報
 *
 * @author Pham Quang Dieu
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardInfo {

    /** カード登録連番 */
    private Integer cardSeq;
    /** 標準フラグ */
    private String defaultFlag;
    /** カード名 */
    private String cardName;
    /** カード番号 */
    private String cardNo;
    /** 期限 */
    private String expire;
    /** 名義人 */
    private String holderName;
    /** 削除フラグ */
    private String deleteFlag;
    /** ブランド */
    private String brand;
    /** 国内フラグ */
    private String domesticFlag;
    /** 発行者コード */
    private String issuerCode;
    /** デビットプリペイド発行者名 */
    private String debitPrepaidIssuerName;
    /** デビットプリペイドフラグ */
    private String debitPrepaidFlag;
    /** フォワードファイナル */
    private String forwardFinal;
}