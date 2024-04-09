/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.dto.memberinfo;

import jp.co.itechh.quad.admin.entity.memberinfo.MemberInfoEntity;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * 会員詳細Dtoクラス
 *
 * @author DtoGenerator
 * @version $Revision: 1.3 $
 */
@Data
@Component
@Scope("prototype")
public class MemberInfoDetailsDto implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 会員エンティティ */
    private MemberInfoEntity memberInfoEntity;

    /** 会員住所Dto */
    private MemberInfoAddressDto memberInfoAddressDto;
}
