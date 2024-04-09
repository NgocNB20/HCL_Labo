package jp.co.itechh.quad.admin.pc.web.admin.shop.inquiry;

import jp.co.itechh.quad.admin.constant.type.HTypeInquiryType;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * InquiryModelItem Class
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Data
@Component
@Scope("prototype")
public class InquiryModelItem implements Serializable {

    /**
     * シリアルバージョンID<br/>
     */
    private static final long serialVersionUID = 1L;

    /**
     * No
     */
    private Integer resultNo;
    /**
     * 問い合わせSEQ
     */
    private Integer inquirySeq;
    /**
     * 問い合わせ状態
     */
    private String inquiryStatus;
    /**
     * 問い合わせコード
     */
    private String inquiryCode;
    /**
     * 問い合わせ分類名
     */
    private String inquiryGroupName;
    /**
     * 問い合わせ者氏名(姓)
     */
    private String inquiryLastName;
    /**
     * 問い合わせ者氏名(名)
     */
    private String inquiryFirstName;
    /**
     * 問い合わせ日時
     */
    private Timestamp inquiryTime;
    /**
     * 回答日時
     */
    private Timestamp answerTime;

    /**
     * 注文番号
     * URLパラメータ
     */
    private String orderCode;
    /**
     * 問い合わせ者氏名
     */
    private String resultInquiryName;
    /**
     * 初回お客様問い合わせ日時
     */
    private Timestamp resultFirstInquiryTime;
    /**
     * 最終お客様問い合わせ日時
     */
    private Timestamp resultLastUserInquiryTime;
    /**
     * 担当者
     */
    private String resultLastRepresentative;
    /**
     * 問い合わせ種別
     */
    private String resultInquiryType;
    /**
     * 連携メモ
     */
    private String resultCooperationMemo;

    /**
     * 問い合わせリンク先判定(一般かどうか)
     *
     * @return 問い合わせリンク判定結果
     */
    public boolean isInquiryTypeGeneral() {
        return HTypeInquiryType.GENERAL.getValue().equals(resultInquiryType);
    }
}