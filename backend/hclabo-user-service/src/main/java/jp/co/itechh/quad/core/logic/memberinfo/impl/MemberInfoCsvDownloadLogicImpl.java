/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.memberinfo.impl;

import jp.co.itechh.quad.core.dao.memberinfo.MemberInfoDao;
import jp.co.itechh.quad.core.dto.memberinfo.MemberCsvDto;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.memberinfo.MemberInfoCsvDownloadLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;

/**
 * 会員情報
 * CSVダウンロードロジックの実装クラス：選択した会員のみ出力
 * 作成日：2021/04/13
 *
 * @author Phan Tien VU (VTI Japan Co., Ltd.)
 */
@Component
public class MemberInfoCsvDownloadLogicImpl extends AbstractShopLogic implements MemberInfoCsvDownloadLogic {

    /** 会員情報DAO */
    private final MemberInfoDao memberInfoDao;

    @Autowired
    public MemberInfoCsvDownloadLogicImpl(MemberInfoDao memberInfoDao) {
        this.memberInfoDao = memberInfoDao;
    }

    @Override
    public Stream<MemberCsvDto> execute(List<Integer> memberInfoSeqList) {
        return this.memberInfoDao.exportCsvByMemberInfoSeqList(memberInfoSeqList);
    }

}
