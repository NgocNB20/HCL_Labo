/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.authorization.presentation.api;

import io.swagger.annotations.ApiParam;
import jp.co.itechh.quad.authorization.presentation.api.param.AuthorizationCheckDataGetRequest;
import jp.co.itechh.quad.authorization.presentation.api.param.AuthorizationListResponse;
import jp.co.itechh.quad.authorization.presentation.api.param.AuthorizationRegistRequest;
import jp.co.itechh.quad.authorization.presentation.api.param.AuthorizationResponse;
import jp.co.itechh.quad.authorization.presentation.api.param.AuthorizationSubResponse;
import jp.co.itechh.quad.authorization.presentation.api.param.AuthorizationUpdateRequest;
import jp.co.itechh.quad.authorization.presentation.api.param.PageInfoRequest;
import jp.co.itechh.quad.core.entity.administrator.AdminAuthGroupEntity;
import jp.co.itechh.quad.core.logic.administrator.AdminAuthGroupLogic;
import jp.co.itechh.quad.core.web.AbstractController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 権限エンドポイント Controller
 *
 * @author Nguyen Hong Son (VTI)
 */
@RestController
public class AuthorizationController extends AbstractController implements LoginApi {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationController.class);

    /** 権限グループ操作ロジック */
    private final AdminAuthGroupLogic adminAuthGroupLogic;

    /** 権限エンドポイント Helper */
    private final AuthorizationHelper authorizationHelper;

    /** メッセージコード定数：更新しようとした権限グループは存在しない */
    protected static final String GROUP_NOT_FOUND = "AYP001004";

    /** メッセージコード：権限グループ使用中 */
    protected static final String GROUP_IN_USE = "AYP001002";

    /** コンストラクタ */
    @Autowired
    public AuthorizationController(AuthorizationHelper authorizationHelper, AdminAuthGroupLogic adminAuthGroupLogic) {
        this.authorizationHelper = authorizationHelper;
        this.adminAuthGroupLogic = adminAuthGroupLogic;
    }

    /**
     * GET /login/administrators/authorizations/{adminAuthGroupSeq} : 権限グループ取得
     * 権限グループ取得
     *
     * @param adminAuthGroupSeq 権限グループSEQ (required)
     * @return 権限グループレスポンス (status code 200)
     * or その他エラー (status code 400)
     */
    @Override
    public ResponseEntity<AuthorizationResponse> getByAdminAuthGroupSeq(
                    @ApiParam(value = "権限グループSEQ", required = true) @PathVariable("adminAuthGroupSeq")
                                    Integer adminAuthGroupSeq) {

        AdminAuthGroupEntity entity = adminAuthGroupLogic.getAdminAuthGroup(adminAuthGroupSeq);
        AuthorizationResponse authorizationResponse = authorizationHelper.toAuthorizationResponse(entity);
        return new ResponseEntity<>(authorizationResponse, HttpStatus.OK);
    }

    /**
     * GET /login/administrators/authorizations : 権限グループ一覧取得
     * 権限グループ一覧取得
     *
     * @param pageInfoRequest ページ情報リクエスト（ページネーションのため） (optional)
     * @return 権限グループ一覧レスポンス (status code 200)
     * or その他エラー (status code 400)
     */
    @Override
    public ResponseEntity<AuthorizationListResponse> get(
                    @ApiParam(value = "ページ情報リクエスト（ページネーションのため）") @Valid PageInfoRequest pageInfoRequest) {

        Integer shopSeq = 1001;

        AuthorizationListResponse authorizationListResponse = new AuthorizationListResponse();

        List<AdminAuthGroupEntity> authList = adminAuthGroupLogic.getAdminAuthGroupList(shopSeq);

        List<AuthorizationSubResponse> authorizationResponseList =
                        authorizationHelper.toAuthorizationResponseList(authList);
        authorizationListResponse.setAuthorizationResponseList(authorizationResponseList);

        return new ResponseEntity<>(authorizationListResponse, HttpStatus.OK);
    }

    /**
     * POST /login/administrators/authorizations : 権限グループ登録
     * 権限グループ登録
     *
     * @param authorizationRegistRequest 権限グループ登録リクエスト (required)
     * @return 権限グループレスポンス (status code 200)
     * or その他エラー (status code 400)
     */
    @Override
    public ResponseEntity<AuthorizationResponse> regist(
                    @ApiParam(value = "権限グループ登録リクエスト", required = true) @Valid @RequestBody
                                    AuthorizationRegistRequest authorizationRegistRequest) {

        AdminAuthGroupEntity group = authorizationHelper.toAdminAuthGroupEntityForRegist(authorizationRegistRequest);

        Integer shopSeq = 1001;
        group.setShopSeq(shopSeq);

        adminAuthGroupLogic.register(group);

        AdminAuthGroupEntity entity = adminAuthGroupLogic.getAdminAuthGroup(group.getAdminAuthGroupSeq());
        AuthorizationResponse authorizationResponse = authorizationHelper.toAuthorizationResponse(entity);

        return new ResponseEntity<>(authorizationResponse, HttpStatus.OK);
    }

    /**
     * PUT /login/administrators/authorizations/{adminAuthGroupSeq} : 権限グループ更新
     * 権限グループ更新
     *
     * @param adminAuthGroupSeq          権限グループSEQ (required)
     * @param authorizationUpdateRequest 権限グループ更新リクエスト (required)
     * @return 権限グループレスポンス (status code 200)
     * or その他エラー (status code 400)
     */
    @Override
    public ResponseEntity<AuthorizationResponse> update(
                    @ApiParam(value = "権限グループSEQ", required = true) @PathVariable("adminAuthGroupSeq")
                                    Integer adminAuthGroupSeq,
                    @ApiParam(value = "権限グループ更新リクエスト", required = true) @Valid @RequestBody
                                    AuthorizationUpdateRequest authorizationUpdateRequest) {

        AdminAuthGroupEntity group = authorizationHelper.toAdminAuthGroupEntityForUpdate(authorizationUpdateRequest);
        adminAuthGroupLogic.update(group);

        AdminAuthGroupEntity entity = adminAuthGroupLogic.getAdminAuthGroup(group.getAdminAuthGroupSeq());
        AuthorizationResponse authorizationResponse = authorizationHelper.toAuthorizationResponse(entity);

        return new ResponseEntity<>(authorizationResponse, HttpStatus.OK);
    }

    /**
     * DELETE /login/administrators/authorizations/{adminAuthGroupSeq} : 権限グループ削除
     * 権限グループ削除
     *
     * @param adminAuthGroupSeq 権限グループSEQ (required)
     * @return 成功 (status code 200)
     * or その他エラー (status code 400)
     */
    @Override
    public ResponseEntity<Void> delete(
                    @ApiParam(value = "権限グループSEQ", required = true) @PathVariable("adminAuthGroupSeq")
                                    Integer adminAuthGroupSeq) {

        AdminAuthGroupEntity group = adminAuthGroupLogic.getAdminAuthGroup(adminAuthGroupSeq);
        if (group == null) {
            throwMessage(GROUP_NOT_FOUND);
        }
        try {
            adminAuthGroupLogic.delete(group);
        } catch (DataIntegrityViolationException e) {
            LOGGER.error("例外処理が発生しました", e);
            throwMessage(GROUP_IN_USE);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * GET /login/administrators/authorizations/check-data : チェック処理用権限グループ取得
     * チェック処理用権限グループ取得
     *
     * @param authorizationCheckDataGetRequest チェック処理用権限グループ取得リクエスト (required)
     * @return 権限グループレスポンス (status code 200)
     * or その他エラー (status code 200)
     */
    @Override
    public ResponseEntity<AuthorizationResponse> checkData(
                    @NotNull @Valid AuthorizationCheckDataGetRequest authorizationCheckDataGetRequest) {
        // ショップSEQ
        Integer shopSeq = 1001;

        AdminAuthGroupEntity entity = adminAuthGroupLogic.getAdminAuthGroup(shopSeq,
                                                                            authorizationCheckDataGetRequest.getAuthGroupDisplayName()
                                                                           );
        AuthorizationResponse authorizationResponse = authorizationHelper.toAuthorizationResponse(entity);
        return new ResponseEntity<>(authorizationResponse, HttpStatus.OK);
    }
}