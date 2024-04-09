/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.convenience.presentation.api;

import jp.co.itechh.quad.convenience.presentation.api.param.ConvenienceListResponse;
import jp.co.itechh.quad.core.entity.conveni.ConvenienceEntity;
import jp.co.itechh.quad.core.logic.order.ConvenienceLogic;
import jp.co.itechh.quad.core.web.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * コンビニ　Controller
 *
 * @author PHAM QUANG DIEU (VJP)
 */
@RestController
public class ConvenienceController extends AbstractController implements PaymentsApi {

    /** コンビニ名称リスト取得ロジック */
    private final ConvenienceLogic convenienceLogic;

    /** コンビニ　Helper */
    private final ConvenienceHelper convenienceHelper;

    @Autowired
    public ConvenienceController(ConvenienceLogic convenienceLogic, ConvenienceHelper convenienceHelper) {
        this.convenienceLogic = convenienceLogic;
        this.convenienceHelper = convenienceHelper;
    }

    /**
     * GET /payments/conveniences : コンビニ名称リスト取得
     * コンビニ名称リスト取得
     *
     * @return コンビニ一覧レスポンス (status code 200)
     *         or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<ConvenienceListResponse> get() {

        List<ConvenienceEntity> convenienceEntityList = convenienceLogic.getConvenienceList();

        ConvenienceListResponse convenienceListResponse =
                        convenienceHelper.toConvenienceListResponse(convenienceEntityList);

        return new ResponseEntity<>(convenienceListResponse, HttpStatus.OK);
    }
}