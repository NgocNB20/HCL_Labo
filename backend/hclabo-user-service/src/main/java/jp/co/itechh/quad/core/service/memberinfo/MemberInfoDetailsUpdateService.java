/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.memberinfo;

import jp.co.itechh.quad.core.dto.memberinfo.MemberInfoDetailsDto;

/**
 * 会員詳細情報更新サービス<br/>
 * 管理画面で使う事を想定としている。<br/>
 * <pre>
 * ・会員情報
 * ・メルマガ購読者情報
 * </pre>
 *
 * @author negishi
 * @version $Revision: 1.1 $
 *
 */
public interface MemberInfoDetailsUpdateService {

    /**
     * エラーコード<br/>
     * 携帯アドレスでHTMLメールを希望している場合
     */
    String MSGCD_MAILMAGAZINE_HTMLMAIL_FAIL = "SMM000901";

    /**
     * サービス実行
     *
     * @param memberInfoDetailsDto 会員詳細DTO
     * @return 更新件数
     */
    int execute(MemberInfoDetailsDto memberInfoDetailsDto);

}
