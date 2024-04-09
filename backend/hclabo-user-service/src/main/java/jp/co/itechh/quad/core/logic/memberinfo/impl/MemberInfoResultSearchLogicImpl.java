/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.memberinfo.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.dao.memberinfo.MemberInfoDao;
import jp.co.itechh.quad.core.dto.memberinfo.MemberInfoForBackDto;
import jp.co.itechh.quad.core.dto.memberinfo.MemberInfoSearchForDaoConditionDto;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.memberinfo.MemberInfoResultSearchLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 会員検索結果一覧取得ロジック<br/>
 *
 * @author natume
 * @version $Revision: 1.5 $
 *
 */
@Component
public class MemberInfoResultSearchLogicImpl extends AbstractShopLogic implements MemberInfoResultSearchLogic {

    /**
     * MemberInfoDao<br/>
     */
    private final MemberInfoDao memberInfoDao;

    @Autowired
    public MemberInfoResultSearchLogicImpl(MemberInfoDao memberInfoDao) {
        this.memberInfoDao = memberInfoDao;
    }

    /**
     * 会員情報一覧取得処理<br/>
     *
     * @param memberInfoConditionDto 会員検索条件Dto
     * @return 会員情報管理サイト用リスト
     */
    @Override
    public List<MemberInfoForBackDto> execute(MemberInfoSearchForDaoConditionDto memberInfoConditionDto) {

        // パラメータチェック
        ArgumentCheckUtil.assertNotNull("memberInfoConditionDto", memberInfoConditionDto);

        // 一覧取得
        return memberInfoDao.getMemberInfoConditionDtoList(
                        memberInfoConditionDto, memberInfoConditionDto.getPageInfo().getSelectOptions());
    }
}