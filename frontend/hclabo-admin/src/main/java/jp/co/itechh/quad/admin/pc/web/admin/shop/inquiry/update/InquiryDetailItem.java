package jp.co.itechh.quad.admin.pc.web.admin.shop.inquiry.update;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * InquiryDetailItem Class
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Data
@Component
@Scope("prototype")
public class InquiryDetailItem implements Serializable {

    /**
     * シリアルバージョンID<br/>
     */
    private static final long serialVersionUID = 1L;
    /**
     * 連番
     */
    private Integer inquiryVersionNo;
    /**
     * 発信者種別
     */
    private String requestType;
    /**
     * 問い合わせ日時
     */
    private Timestamp inquiryTime;
    /**
     * 問い合わせ内容
     */
    private String inquiryBody;
    /**
     * 部署名
     */
    private String divisionName;
    /**
     * 担当者
     */
    private String representative;
    /**
     * 連絡先TEL
     */
    private String contactTel;
    /**
     * 処理担当者
     */
    private String operator;
    /**
     * 表示用（問い合わせ者）
     */
    private String inquiryMan;
    /**
     * 問い合わせ内容の背景色(問い合わせ者)
     */
    private String bgColorInquiryDetailManClass;
    /**
     * 問い合わせ内容の背景色(問い合わせ日時)
     */
    private String bgColorInquiryDetailTimeClass;
    /**
     * 問い合わせ内容の背景色(問い合わせ内容)
     */
    private String bgColorInquiryDetailBodyClass;

}
