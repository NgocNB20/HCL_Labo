/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.memberinfo;

import jp.co.itechh.quad.core.dto.memberinfo.MemberCsvDto;
import jp.co.itechh.quad.core.dto.memberinfo.MemberInfoSearchForDaoConditionDto;

import java.util.stream.Stream;

/**
 * 会員情報
 * CSVダウンロードロジック：全件出力
 * 作成日：2021/04/13
 *
 * @author Phan Tien VU (VTI Japan Co., Ltd.)
 */
public interface MemberInfoAllCsvDownloadLogic {

    Stream<MemberCsvDto> execute(MemberInfoSearchForDaoConditionDto conditionDto);

}
