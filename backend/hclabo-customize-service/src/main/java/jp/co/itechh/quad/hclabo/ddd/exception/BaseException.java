/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.hclabo.ddd.exception;

import lombok.Getter;
import org.apache.commons.collections.MapUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * BaseException<br/>
 * 全例外の共通親クラス<br/>
 *
 * @author yt23807
 */
@Getter
public abstract class BaseException extends RuntimeException {

    /** シリアルバージョンID */
    private static final long serialVersionUID = 1L;

    /** グローバルエラーメッセージ用フィールド名 */
    public static final String GLOBAL_MESSAGE_FIELD_NAME = "common";

    /**
     * メッセージマップ<br/>
     * <br/>
     * 以下、具体的なマップイメージ例<br/>
     * <pre>
     * +----------+-------------------------------------------------------------+
     * | MAP_KEY  | MAP_VALUE                                                   |
     * +==========+=============================================================+
     * | lastName |  [List]                                                     |
     * |          |  +---+-------------------------------------------------+    |
     * |          |  |IDX| LIST_VALUE                                      |    |
     * |          |  +===+=================================================+    |
     * |          |  | 0 | [ExecptionContent Object]                       |    |
     * |          |  |   |     code: ERROR_CODE_001                        |    |
     * |          |  |   |     message: 氏名（姓）は20桁で入力してください |    |
     * |          |  +---+-------------------------------------------------+    |
     * |          |  | 1 | [ExecptionContent Object]                       |    |
     * |          |  |   |     code: ERROR_CODE_002                        |    |
     * |          |  |   |     message: 氏名（姓）は全角で入力してください |    |
     * |          |  +---+-------------------------------------------------+    |
     * +----------+-------------------------------------------------------------+
     * | common   |  [List]                                                     |
     * |          |  +---+-------------------------------------------------+    |
     * |          |  |IDX| LIST_VALUE                                      |    |
     * |          |  +===+=================================================+    |
     * |          |  | 0 | [ExecptionContent Object]                       |    |
     * |          |  |   |     code: ERROR_CODE_100                        |    |
     * |          |  |   |     message: メールアドレスが重複しています     |    |
     * |          |  +---+-------------------------------------------------+    |
     * +----------+-------------------------------------------------------------+
     * </pre>
     */
    private Map<String, List<ExceptionContent>> messageMap = new HashMap<>();

    /**
     * コンストラクタ
     */
    public BaseException() {
        super();
    }

    /**
     * Add message.
     *
     * @param fieldName the field name
     * @param code      the code
     */
    protected void addMessage(String fieldName, String code) {
        addMessage(fieldName, code, null);
    }

    /**
     * Add message.
     *
     * @param fieldName the field name
     * @param code      the code
     * @param args      the message arguments
     */
    protected void addMessage(String fieldName, String code, String[] args) {
        List<ExceptionContent> errorResponseList;

        if (messageMap.containsKey(fieldName)) {
            errorResponseList = messageMap.get(fieldName);
        } else {
            errorResponseList = new ArrayList<>();
        }
        errorResponseList.add(new ExceptionContent(code, args));

        messageMap.put(fieldName, errorResponseList);
    }

    /**
     * Has message boolean.
     *
     * @return the boolean
     */
    public boolean hasMessage() {
        return !MapUtils.isEmpty(messageMap);
    }
}