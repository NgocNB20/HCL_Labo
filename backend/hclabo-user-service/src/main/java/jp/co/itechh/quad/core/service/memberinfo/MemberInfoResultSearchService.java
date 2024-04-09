/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.memberinfo;

import jp.co.itechh.quad.core.dto.memberinfo.MemberInfoForBackDto;
import jp.co.itechh.quad.core.dto.memberinfo.MemberInfoSearchForDaoConditionDto;

import java.util.List;

/**
 * バック会員検索サービス<br/>
 *
 * @author natume
 * @version $Revision: 1.4 $
 *
 */
public interface MemberInfoResultSearchService {

    /**
     * 会員情報一覧取得処理<br/>
     *
     * @param memberInfoConditionDto 会員検索条件Dto
     * @return 会員情報管理サイト用リスト
     */
    List<MemberInfoForBackDto> execute(MemberInfoSearchForDaoConditionDto memberInfoConditionDto);
}
