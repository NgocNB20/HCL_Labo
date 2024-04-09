/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.admin.validator;

import jp.co.itechh.quad.admin.annotation.validator.HVBothSideSpace;
import jp.co.itechh.quad.admin.constant.type.SpaceValidateMode;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * <span class="defaultValidator">★デフォルトバリデータ</span><br />
 * <span class="logicName">【データ両端の空白】</span>データの先頭／末尾のスペースをチェックするバリデータ。<br />
 * <br />
 * デフォルトの挙動：先頭／末尾にスペースが存在する場合エラー<br />
 * バリデータを指定すれば、末尾スペースが入力可能となる<br />
 * <br />
 *
 * <pre>
 * 入力値の先頭／末尾にスペースを許可したい項目に対して付与する。
 * デフォルトでは、入力値の先頭/末尾の空白入力を認めていない。
 * ＜理由＞
 *  基本的なユーザー操作として、入力値の先頭/末尾にスペースを入れるという動作が、入力ミス以外
 *  想定しにくい為、デフォルトはＮＧとして、必要な個所のみ解除するという方針の為
 * ＜使用箇所：抜粋＞
 *  会員情報の管理用メモ、メルマガ本文など
 * </pre>
 *
 * @author kimura
 *
 */
@Data
public class HBothSideSpaceValidator implements ConstraintValidator<HVBothSideSpace, Object> {

    /** スペースを許可しない場合 */
    public static final String SPACE_DENY_MESSAGE_ID = "{HBothSideSpaceValidator.INVALID_detail}";

    /** 先頭のスペースを許可しない場合 */
    public static final String LEFT_SIDE_SPACE_DENY_MESSAGE_ID =
                    "{HBothSideSpaceValidator.DENY_LEFT_SIDE_SPACE.INVALID_detail}";

    /** 末尾のスペースを許可しない場合 */
    public static final String RIGHT_SIDE_SPACE_DENY_MESSAGE_ID =
                    "{HBothSideSpaceValidator.DENY_RIGHT_SIDE_SPACE.INVALID_detail}";

    /** データ先頭の空白処理モード */
    private SpaceValidateMode startWith;

    /** データ末尾の空白処理モード */
    private SpaceValidateMode endWith;

    /** アノテーションの情報設定 */
    @Override
    public void initialize(HVBothSideSpace annotation) {
        startWith = annotation.startWith();
        endWith = annotation.endWith();
    }

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

        // startWith, endWithともにALLOW_SPACEの場合、処理不要
        if (startWith == SpaceValidateMode.ALLOW_SPACE && endWith == SpaceValidateMode.ALLOW_SPACE) {
            return true;
        }

        // startWith, endWithから正規表現パターンを構築する
        String startWithPattern;
        String endWithPattern;

        // startWith
        if (startWith == SpaceValidateMode.ALLOW_SPACE) {
            startWithPattern = "^.*";
        } else if (startWith == SpaceValidateMode.DENY_FULLWIDTH_SPACE) {
            startWithPattern = "^[^　].*";
        } else if (startWith == SpaceValidateMode.DENY_HALFWIDTH_SPACE) {
            startWithPattern = "^[^ ].*";
        } else {
            startWithPattern = "^[^ 　].*";
        }

        // endWith
        if (endWith == SpaceValidateMode.ALLOW_SPACE) {
            endWithPattern = ".*$";
        } else if (endWith == SpaceValidateMode.DENY_FULLWIDTH_SPACE) {
            endWithPattern = ".*[^　]$";
        } else if (endWith == SpaceValidateMode.DENY_HALFWIDTH_SPACE) {
            endWithPattern = ".*[^ ]$";
        } else {
            endWithPattern = ".*[^ 　]$";
        }

        // テキストエリアの場合の対応
        // テキストエリアの場合、各行に対してチェックを行う
        // 想定される改行 LF, CR + LF, CRで分割
        String[] strValues = value.toString().split("\\x0D\\x0A|\\x0D|\\x0A");
        for (String strValue : strValues) {
            if (StringUtils.isEmpty(strValue)) {
                // 空文字の場合は次へ
                continue;
            }

            // 両側チェック
            if (!Pattern.matches(startWithPattern, strValue) && !Pattern.matches(endWithPattern, strValue)) {
                return false;
            }

            // 先頭チェック
            else if (!Pattern.matches(startWithPattern, strValue)) {
                makeContext(context, LEFT_SIDE_SPACE_DENY_MESSAGE_ID);
                return false;
            }

            // 末尾チェック
            else if (!Pattern.matches(endWithPattern, strValue)) {
                makeContext(context, RIGHT_SIDE_SPACE_DENY_MESSAGE_ID);
                return false;
            }
        }
        return true;
    }

    /**
     * エラーメッセージを構成<br/>
     */
    protected void makeContext(ConstraintValidatorContext context, String messageId) {
        // 動的バリデーションによる手動バリデータの発火はこちらの設定は不要
        if (context == null) {
            return;
        }
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(messageId).addConstraintViolation();
    }
}