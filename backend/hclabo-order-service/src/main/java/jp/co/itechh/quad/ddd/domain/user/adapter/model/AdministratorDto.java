package jp.co.itechh.quad.ddd.domain.user.adapter.model;

import lombok.Data;

import java.util.Date;

/**
 * 運営者レスポンス
 */
@Data
public class AdministratorDto {

    private Integer administratorSeq;

    private String administratorStatus;

    private String administratorId;

    private String administratorPassword;

    private String mail;

    private String administratorLastName;

    private String administratorFirstName;

    private String administratorLastKana;

    private String administratorFirstKana;

    private Date useStartDate;

    private Date useEndDate;

    private Integer adminAuthGroupSeq;

}
