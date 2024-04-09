/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.previewaccess.presentation.api;

import io.swagger.annotations.ApiParam;
import jp.co.itechh.quad.authorization.presentation.api.UsersApi;
import jp.co.itechh.quad.authorization.presentation.api.param.PreviewAccessCheckRequest;
import jp.co.itechh.quad.authorization.presentation.api.param.PreviewAccessKeyResponse;
import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.entity.previewaccess.PreviewAccessControlEntity;
import jp.co.itechh.quad.core.logic.previewaccess.PreviewAccessLogic;
import jp.co.itechh.quad.core.web.AbstractController;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * プレビューアクセスエンドポイント Controller
 *
 * @author kimura
 */
@RestController
public class PreviewAccessController extends AbstractController implements UsersApi {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(PreviewAccessController.class);

    /** プレビューアクセス制御ロジック */
    private final PreviewAccessLogic previewAccessLogic;

    /** 変換ユーティリティ */
    private final ConversionUtility conversionUtility;

    /** コンストラクタ */
    @Autowired
    public PreviewAccessController(PreviewAccessLogic previewAccessLogic, ConversionUtility conversionUtility) {
        this.previewAccessLogic = previewAccessLogic;
        this.conversionUtility = conversionUtility;
    }

    /**
     * POST /users/authorizations/preview-access-key/issue : プレビューアクセスキー発行
     *
     * @return プレビューアクセスキーレスポンス (status code 200) or その他エラー (status code 400)
     */
    @Override
    public ResponseEntity<PreviewAccessKeyResponse> issuePreviewAccessKey() {

        String previewAccessKey = null;
        try {
            // ヘッダーから管理者SEQを取得
            String administratorSeq = getAdministratorSeq();
            // プレビューアクセスキーを生成
            previewAccessKey = this.previewAccessLogic.insertPreviewAccess(StringUtils.isNotEmpty(administratorSeq) ?
                                                                                           this.conversionUtility.toInteger(
                                                                                                           administratorSeq) :
                                                                                           null);
        } catch (Exception e) {
            LOGGER.error("プレビューアクセス制御時にエラーが発生しました。", e);
            throwMessage();
        }

        PreviewAccessKeyResponse response = new PreviewAccessKeyResponse();
        response.setPreviewAccessKey(previewAccessKey);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * POST /users/authorizations/preview-access-check : プレビューアクセスチェック
     *
     * @return プレビューアクセスキーレスポンス (status code 200) or その他エラー (status code 400)
     */
    @Override
    public ResponseEntity<Void> chackPreviewAccess(
                    @ApiParam(value = "プレビューアクセスチェックリクエスト", required = true) @Valid @RequestBody
                                    PreviewAccessCheckRequest previewAccessCheckRequest) {

        PreviewAccessControlEntity previewAccessControlEntity =
                        this.previewAccessLogic.checkPreviewAccess(previewAccessCheckRequest.getPreviewAccessKey());

        // プレビューアクセス制御アサートチェック
        ArgumentCheckUtil.assertNotNull("previewAccessControlEntity", previewAccessControlEntity);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 管理者SEQ取得
     *
     * @return 管理者SEQ
     */
    public String getAdministratorSeq() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return null;
        } else {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            if (request.getHeader("X-LOGGED-IN-ADMIN-SEQ") == null) {
                return null;
            } else {
                return request.getHeader("X-LOGGED-IN-ADMIN-SEQ");
            }
        }
    }

}