/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.ddd.presentation.handler;

import jp.co.itechh.quad.core.base.exception.AppLevelException;
import jp.co.itechh.quad.core.base.exception.AppLevelListException;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.ApplicationLogUtility;
import jp.co.itechh.quad.ddd.exception.ApplicationException;
import jp.co.itechh.quad.ddd.exception.AssertException;
import jp.co.itechh.quad.ddd.exception.BaseException;
import jp.co.itechh.quad.ddd.exception.DomainException;
import jp.co.itechh.quad.ddd.exception.ExceptionContent;
import jp.co.itechh.quad.ddd.exception.ValidateException;
import jp.co.itechh.quad.product.presentation.api.param.ErrorContent;
import jp.co.itechh.quad.product.presentation.api.param.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * コントローラーの例外ハンドラー
 */
@ControllerAdvice
public class ControllerExceptionHandler {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    /**
     * API例外ハンドリング
     * 例外タイプ：AssertException
     *
     * @param e エラー
     * @return レスポンスコード：500 / エラーリスト（マップ形式）
     */
    @ExceptionHandler(AssertException.class)
    public ResponseEntity<ErrorResponse> handleAssertException(AssertException e) {

        // ログを出力
        for (String log : getLog(e.getMessageMap())) {
            LOGGER.error(log);
        }

        // -------------------
        // アプリケーションログを出力
        // -------------------
        ApplicationLogUtility applicationLogUtility = ApplicationContextUtility.getBean(ApplicationLogUtility.class);
        applicationLogUtility.writeDddErrorLog(e);

        // キャッチした例外をもとに、エラーレスポンスを生成
        ErrorResponse errorResponse = createErrorResponse(e);
        // エラーレスポンスを返却
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * API例外ハンドリング
     * 例外タイプ：ApplicationException
     *
     * @param e エラー
     * @return レスポンスコード：400 / エラーリスト（マップ形式）
     */
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> handleApplicationException(ApplicationException e) {

        // ログを出力
        for (String log : getLog(e.getMessageMap())) {
            LOGGER.error(log);
        }

        // -------------------
        // アプリケーションログを出力
        // -------------------
        ApplicationLogUtility applicationLogUtility = ApplicationContextUtility.getBean(ApplicationLogUtility.class);
        applicationLogUtility.writeExceptionLog(e);

        // キャッチした例外をもとに、エラーレスポンスを生成
        ErrorResponse errorResponse = createErrorResponse(e);
        // エラーレスポンスを返却
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * API例外ハンドリング
     * 例外タイプ：ValidateException
     *
     * @param e エラー
     * @return レスポンスコード：400 / エラーリスト（マップ形式）
     */
    @ExceptionHandler(ValidateException.class)
    public ResponseEntity<ErrorResponse> handleValidateException(ValidateException e) {

        // ログを出力
        for (String log : getLog(e.getMessageMap())) {
            LOGGER.error(log);
        }

        // -------------------
        // アプリケーションログを出力
        // -------------------
        ApplicationLogUtility applicationLogUtility = ApplicationContextUtility.getBean(ApplicationLogUtility.class);
        applicationLogUtility.writeExceptionLog(e);

        // キャッチした例外をもとに、エラーレスポンスを生成
        ErrorResponse errorResponse = createErrorResponse(e);
        // エラーレスポンスを返却
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * API例外ハンドリング
     * 例外タイプ：DomainException
     *
     * @param e エラー
     * @return レスポンスコード：400 / エラーリスト（マップ形式）
     */
    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handleDomainException(DomainException e) {

        // ログを出力
        for (String log : getLog(e.getMessageMap())) {
            LOGGER.error(log);
        }

        // -------------------
        // アプリケーションログを出力
        // -------------------
        ApplicationLogUtility applicationLogUtility = ApplicationContextUtility.getBean(ApplicationLogUtility.class);
        applicationLogUtility.writeExceptionLog(e);

        // キャッチした例外をもとに、エラーレスポンスを生成
        ErrorResponse errorResponse = createErrorResponse(e);
        // エラーレスポンスを返却
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * ボトムアップ実装のAPI例外ハンドリング
     * 例外タイプ：AppLevelListException
     *
     * @param e エラー
     * @return レスポンスコード：400 / エラーリスト（マップ形式）
     */
    @ExceptionHandler(AppLevelListException.class)
    public ResponseEntity<ErrorResponse> handleAppLevelListException(AppLevelListException e) {

        // ログを出力
        for (AppLevelException appLevelException : e.getErrorList()) {
            if (appLevelException.getAppLevelFacesMessage() != null) {
                LOGGER.error(appLevelException.getAppLevelFacesMessage().getMessage());
            } else if (appLevelException.getCause() != null) {
                LOGGER.error(appLevelException.getCause().getMessage());
            }
        }

        // -------------------
        // アプリケーションログを出力
        // -------------------
        ApplicationLogUtility applicationLogUtility = ApplicationContextUtility.getBean(ApplicationLogUtility.class);
        applicationLogUtility.writeLogicErrorLog(e);

        // キャッチした例外をもとに、エラーレスポンスを生成
        ErrorResponse errorResponse = createErrorResponse(e);
        // エラーレスポンスを返却
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * 他サービス呼出先でエラー（クライアントエラー）
     *
     * @param e HttpClientErrorException
     * @return レスポンスコード：400
     */
    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<String> handleHttpClientErrorException(HttpClientErrorException e) {

        // ログを出力
        LOGGER.error(e.getMessage());

        // -------------------
        // アプリケーションログを出力
        // -------------------
        ApplicationLogUtility applicationLogUtility = ApplicationContextUtility.getBean(ApplicationLogUtility.class);
        applicationLogUtility.writeExceptionLog(e);

        // エラーレスポンスを返却
        return new ResponseEntity<>(e.getResponseBodyAsString(), HttpStatus.BAD_REQUEST);
    }

    /**
     * 他サービス呼出先でエラー（サーバーエラー）
     *
     * @param e HttpServerErrorException
     * @return レスポンスコード：500
     */
    @ExceptionHandler(HttpServerErrorException.class)
    public ResponseEntity<String> handleHttpServerErrorException(HttpServerErrorException e) {

        // ログを出力
        LOGGER.error(e.getMessage());

        // -------------------
        // アプリケーションログを出力
        // -------------------
        ApplicationLogUtility applicationLogUtility = ApplicationContextUtility.getBean(ApplicationLogUtility.class);
        applicationLogUtility.writeExceptionLog(e);

        // エラーレスポンスを返却
        return new ResponseEntity<>(e.getResponseBodyAsString(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 予期せぬエラー
     *
     * @param e Throwable
     * @return レスポンスコード：500
     */
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<String> handleThrowable(Throwable e) {

        // ログを出力
        LOGGER.error(e.getMessage());

        // -------------------
        // アプリケーションログを出力
        // -------------------
        ApplicationLogUtility applicationLogUtility = ApplicationContextUtility.getBean(ApplicationLogUtility.class);
        applicationLogUtility.writeExceptionLog(e);

        // エラーレスポンスを返却
        return new ResponseEntity<>("UNEXPECTED_EXCEPTION", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * エラーレスポンス生成
     *
     * @param e AppLevelListException
     * @return エラーレスポンス
     */
    private ErrorResponse createErrorResponse(AppLevelListException e) {
        // エラーレスポンスを設定
        ErrorResponse errorResponse = new ErrorResponse();
        Map<String, List<ErrorContent>> messageMap = new HashMap<>();

        // ----------------------------------------------------------------------
        // Exceptionの持つList内のAppLevelExceptionをErrorContentに型変換
        // （参考）
        //   [INPUT] e.getErrorList() ･･･ List<AppLevelException>>型
        //   [OUTPUT]errorResponseList･･･ List<ErrorContent>>型
        // ----------------------------------------------------------------------
        List<ErrorContent> errorResponseList = e.getErrorList().stream().map(exception -> {
            ErrorContent errorContent = new ErrorContent();
            errorContent.setMessage(exception.getMessage());
            errorContent.setCode(exception.getMessageCode());
            return errorContent;
        }).collect(Collectors.toList());
        // エラーマップにエラーリストを設定
        // ※フィールド名はグローバルメッセージ名で固定
        messageMap.put(BaseException.GLOBAL_MESSAGE_FIELD_NAME, errorResponseList);

        // エラーレスポンスにマップを登録
        errorResponse.setMessages(messageMap);
        // エラーレスポンス返却
        return errorResponse;
    }

    /**
     * エラーレスポンス生成
     *
     * @param e BaseException
     * @return エラーレスポンス
     */
    private ErrorResponse createErrorResponse(BaseException e) {
        // エラーレスポンスを設定
        ErrorResponse errorResponse = new ErrorResponse();
        Map<String, List<ErrorContent>> messageMap = new HashMap<>();

        // ----------------------------------------------------------------------
        // Exceptionの持つMap内のExceptionContentをErrorContentに型変換
        // （参考）
        //   [INPUT] e.getMessageMap()･･･ Map<String, List<ExceptionContent>>型
        //   [OUTPUT]messageMap       ･･･ Map<String, List<ErrorContent>>型
        // ----------------------------------------------------------------------
        e.getMessageMap().forEach((fieldName, exceptionContentList) -> {
            // ExceptionContent->ErrorContentへ要素型変換したリスト生成
            List<ErrorContent> errorContentList = exceptionContentList.stream().map(exceptionContent -> {
                ErrorContent errorContent = new ErrorContent();
                errorContent.setCode(exceptionContent.getCode());
                errorContent.setMessage(exceptionContent.getMessage());
                return errorContent;
            }).collect(Collectors.toList());
            // マップにListを登録（fieldNameは元のまま）
            messageMap.put(fieldName, errorContentList);
        });
        // エラーレスポンスにマップを登録
        errorResponse.setMessages(messageMap);
        // エラーレスポンスを返却
        return errorResponse;
    }

    /**
     * ログを出力
     *
     * @param map MessageMap
     * @return エラー保持リスト
     */
    private List<String> getLog(Map<String, List<ExceptionContent>> map) {

        List<String> errorList = new ArrayList<>();
        for (Map.Entry<String, List<ExceptionContent>> entry : map.entrySet()) {
            String key = entry.getKey();
            List<ExceptionContent> value = entry.getValue();
            StringBuilder log = null;
            for (ExceptionContent content : value) {
                if (log == null) {
                    log = new StringBuilder(content.getMessage());
                } else {
                    log.append(", ").append(content.getMessage());
                }
            }
            errorList.add(key + ": " + log);
        }

        return errorList;
    }
}