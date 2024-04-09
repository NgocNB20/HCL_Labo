package jp.co.itechh.quad.ddd.domain.user.adapter.model;

import lombok.Data;

import java.util.Date;

/**
 * 会員レスポンス
 */
@Data
public class Customer {

    private Integer memberInfoSeq;

    private String memberInfoStatus;

    private String memberInfoUniqueId;

    private String memberInfoId;

    private String memberInfoPassword;

    private String memberInfoLastName;

    private String memberInfoFirstName;

    private String memberInfoLastKana;

    private String memberInfoFirstKana;

    private String memberInfoSex;

    private Date memberInfoBirthday;

    private String memberInfoTel;

    private String memberInfoAddressId;

    private String memberInfoMail;

    private String accessUid;

    private Integer versionNo;

    private String admissionYmd;

    private String secessionYmd;

    private String memo;

    private Date lastLoginTime;

    private String lastLoginUserAgent;

    private Date registTime;

    private Date updateTime;
}
