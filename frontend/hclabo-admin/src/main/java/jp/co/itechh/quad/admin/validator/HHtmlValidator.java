/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.admin.validator;

import jp.co.itechh.quad.admin.annotation.validator.HVHtml;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

/**
 * <span class="logicName">【HTMLバリデータ】</span>HTML構文バリデータ。<br />
 * <br />
 * 対象項目のHTMLタグが不整合の場合エラー<br />
 * <br />
 *
 * <pre>
 * チェック詳細は「13_HM3_共通部仕様書_HTMLバリデータを含むテキストエリアについて.xls」参照。
 * HTMLタグの入力を許可する項目に対して付与する。
 * 本チェックを付与することで、データ入力時にHTMLタグの不整合が検出可能となる。
 * ＜使用箇所：抜粋＞
 * 商品概要、商品詳細、ニュース本文など
 * </pre>
 *
 * @author kimura
 *
 */
@Data
public class HHtmlValidator implements ConstraintValidator<HVHtml, Object> {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(HHtmlValidator.class);

    /** XHTML として検証を行うかどうか */
    protected boolean xhtml;

    /** 入れ子 HTML として検証を行うかどうか */
    protected boolean innerHtml;

    /** ユニークタグの検証を行うかどうか */
    protected boolean uniqueTagCheck;

    /** 排他タグの検証を行うかどうか */
    protected boolean exclusiveTagCheck;

    /** タグ依存の検証を行うかどうか */
    protected boolean dependencyCheck;

    /** 未定義タグの使用を許可するかどうか */
    protected boolean rejectUndefinedTag;

    /** FORM タグの使用を許可するかどうか */
    protected boolean permitFormTag;

    /** SCRIPT タグの */
    protected boolean permitScriptTag;

    /** パースエラーメッセージ */
    public static final String PARSE_MESSAGE_ID = ".PARSE";
    /** パースエラーメッセージ(CSV用) */
    public static final String PARSE_MESSAGE_ID_CSV = PARSE_MESSAGE_ID + "_csv";

    /** 木構造エラーメッセージ - 開始タグが見つからない */
    public static final String STRUCTURE1_MESSAGE_ID = ".STRUCTURE1";
    /** 木構造エラーメッセージ - 開始タグが見つからない(CSV用) */
    public static final String STRUCTURE1_MESSAGE_ID_CSV = STRUCTURE1_MESSAGE_ID + "_csv";

    /** 木構造エラーメッセージ - 終了タグか開始タグかどちらかが見つからない */
    public static final String STRUCTURE2_MESSAGE_ID = ".STRUCTURE2";
    /** 木構造エラーメッセージ - 終了タグか開始タグかどちらかが見つからない(CSV用) */
    public static final String STRUCTURE2_MESSAGE_ID_CSV = STRUCTURE2_MESSAGE_ID + "_csv";

    /** 木構造エラーメッセージ - 終了していないタグ */
    public static final String STRUCTURE3_MESSAGE_ID = ".STRUCTURE3";
    /** 木構造エラーメッセージ - 終了していないタグ(CSV用) */
    public static final String STRUCTURE3_MESSAGE_ID_CSV = STRUCTURE3_MESSAGE_ID + "_csv";

    /** 記号不正使用エラーメッセージ - 行が異なる */
    public static final String SIGN1_MESSAGE_ID = ".SIGN1";
    /** 記号不正使用エラーメッセージ - 行が異なる(CSV用) */
    public static final String SIGN1_MESSAGE_ID_CSV = SIGN1_MESSAGE_ID + "_csv";

    /** 記号不正使用エラーメッセージ - 同一行 */
    public static final String SIGN2_MESSAGE_ID = ".SIGN2";
    /** 記号不正使用エラーメッセージ - 同一行(CSV用) */
    public static final String SIGN2_MESSAGE_ID_CSV = SIGN2_MESSAGE_ID + "_csv";

    /** 未定義タグエラーメッセージ */
    public static final String UNDEFINED_MESSAGE_ID = ".UNDEFINED";
    /** 未定義タグエラーメッセージ(CSV用) */
    public static final String UNDEFINED_MESSAGE_ID_CSV = UNDEFINED_MESSAGE_ID + "_csv";

    /** 使用不可タグエラーメッセージ */
    public static final String UNIQUE_MESSAGE_ID = ".UNIQUE";
    /** 使用不可タグエラーメッセージ(CSV用) */
    public static final String UNIQUE_MESSAGE_ID_CSV = UNIQUE_MESSAGE_ID + "_csv";

    /** 排他タグエラーメッセージ */
    public static final String EXCLUSIVE_MESSAGE_ID = ".EXCLUSIVE";
    /** 排他タグエラーメッセージ(CSV用) */
    public static final String EXCLUSIVE_MESSAGE_ID_CSV = EXCLUSIVE_MESSAGE_ID + "_csv";

    /** 依存タグエラーメッセージ */
    public static final String DEPENDENCY_MESSAGE_ID = ".DEPENDENCY";
    /** 依存タグエラーメッセージ(CSV用) */
    public static final String DEPENDENCY_MESSAGE_ID_CSV = DEPENDENCY_MESSAGE_ID + "_csv";

    /**
     * バリデーション実行
     *
     * @return チェックエラーの場合 false
     */
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        // null or 空の場合未実施
        if (Objects.equals(value, null) || Objects.equals(value, "")) {
            return true;
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("--- LISTING @HVHtml annotation parameters ---");
            LOGGER.debug("             xhtml:" + this.xhtml);
            LOGGER.debug("         innerHtml:" + this.innerHtml);
            LOGGER.debug("    uniqueTagCheck:" + this.uniqueTagCheck);
            LOGGER.debug(" exclusiveTagCheck:" + this.exclusiveTagCheck);
            LOGGER.debug("   dependencyCheck:" + this.dependencyCheck);
            LOGGER.debug("rejectUndefinedTag:" + this.rejectUndefinedTag);
            LOGGER.debug("     permitFormTag:" + this.permitFormTag);
            LOGGER.debug("   permitScriptTag:" + this.permitScriptTag);
        }
        return true;
    }

    /**
     * @return the xhtml
     */
    public boolean isXhtml() {
        return xhtml;
    }

    /**
     * @return the innerHtml
     */
    public boolean isInnerHtml() {
        return innerHtml;
    }

    /**
     * @return the uniqueTagCheck
     */
    public boolean isUniqueTagCheck() {
        return uniqueTagCheck;
    }

    /**
     * @return the exclusiveTagCheck
     */
    public boolean isExclusiveTagCheck() {
        return exclusiveTagCheck;
    }

    /**
     * @return the dependencyCheck
     */
    public boolean isDependencyCheck() {
        return dependencyCheck;
    }

    /**
     * @return the rejectUndefinedTag
     */
    public boolean isRejectUndefinedTag() {
        return rejectUndefinedTag;
    }

    /**
     * @return the permitFormTag
     */
    public boolean isPermitFormTag() {
        return permitFormTag;
    }

    /**
     * @return the permitScriptTag
     */
    public boolean isPermitScriptTag() {
        return permitScriptTag;
    }
}