/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.memberinfo.impl;

import jp.co.itechh.quad.core.dto.memberinfo.MemberInfoForBackDto;
import jp.co.itechh.quad.core.dto.memberinfo.MemberInfoSearchForDaoConditionDto;
import jp.co.itechh.quad.core.logic.memberinfo.MemberInfoResultSearchLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.memberinfo.MemberInfoResultSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * バック会員検索サービス実装<br/>
 *
 * @author natume
 * @version $Revision: 1.5 $
 *
 */
@Service
public class MemberInfoResultSearchServiceImpl extends AbstractShopService implements MemberInfoResultSearchService {

    /**
     * 会員検索ロジック<br/>
     */
    private final MemberInfoResultSearchLogic memberInfoResultSearchLogic;

    @Autowired
    public MemberInfoResultSearchServiceImpl(MemberInfoResultSearchLogic memberInfoResultSearchLogic) {
        this.memberInfoResultSearchLogic = memberInfoResultSearchLogic;
    }

    /**
     * 検索処理<br/>
     *
     * @param memberInfoConditionDto 会員検索条件DTO
     * @return 会員情報管理サイト用リスト
     */
    @Override
    public List<MemberInfoForBackDto> execute(MemberInfoSearchForDaoConditionDto memberInfoConditionDto) {

        Integer shopSeq = 1001;
        // 検索条件作成
        memberInfoConditionDto.setShopSeq(shopSeq);

        // リスト取得
        List<MemberInfoForBackDto> resultList = memberInfoResultSearchLogic.execute(memberInfoConditionDto);

        // 結果セット
        return resultList;
    }
}
