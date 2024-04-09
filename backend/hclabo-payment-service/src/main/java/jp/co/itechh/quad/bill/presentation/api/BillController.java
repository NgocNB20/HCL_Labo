/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.bill.presentation.api;

import jp.co.itechh.quad.bill.presentation.api.param.AuthorityCheckRequest;
import jp.co.itechh.quad.core.logic.multipayment.SettlememtMismatchCheckLogic;
import jp.co.itechh.quad.core.web.AbstractController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 請求情報詳細 Controller
 *
 * @author PHAM QUANG DIEU (VJP)
 */
@RestController
public class BillController extends AbstractController implements PaymentsApi {

    /** 請求情報の不整合チェックLogic */
    private final SettlememtMismatchCheckLogic settlememtMismatchCheckLogic;

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(BillController.class);

    /** コンストラクタ */
    public BillController(SettlememtMismatchCheckLogic settlememtMismatchCheckLogic) {
        this.settlememtMismatchCheckLogic = settlememtMismatchCheckLogic;
    }

    /**
     * POST /payments/bills/authority/check : 再オーソリ不整合チェック
     * 再オーソリ不整合チェック
     *
     * @param authorityCheckRequest オーソリチェックリクエスト (required)
     * @return 成功 (status code 200)
     * or システムエラー (status code 500)
     */
    @Override
    public ResponseEntity<Void> check(@Valid AuthorityCheckRequest authorityCheckRequest) {
        if (authorityCheckRequest.getOrderCode() != null) {
            // 請求情報の不整合チェック処理の初期処理
            settlememtMismatchCheckLogic.initReAuth(authorityCheckRequest.getOrderCode());
        } else if (authorityCheckRequest.getErrInfo() != null) {
            Throwable throwable = new Throwable(authorityCheckRequest.getErrInfo());
            // 請求情報の不整合チェック
            settlememtMismatchCheckLogic.execute(throwable);
        } else {
            // 請求情報の不整合チェック
            settlememtMismatchCheckLogic.execute();
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
