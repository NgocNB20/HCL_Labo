/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.memberinfo;

import jp.co.itechh.quad.core.dto.memberinfo.MemberCsvDto;

import java.util.List;
import java.util.stream.Stream;

/**
 * 会員情報
 * CSVダウンロードロジック：選択した会員のみ出力
 * 作成日：2021/04/13
 *
 * @author Phan Tien VU (VTI Japan Co., Ltd.)
 */
public interface MemberInfoCsvDownloadLogic {

    Stream<MemberCsvDto> execute(List<Integer> memberInfoSeqList);

}
