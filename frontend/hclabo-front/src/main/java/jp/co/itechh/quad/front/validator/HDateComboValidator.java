/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.front.validator;

import jp.co.itechh.quad.front.annotation.validator.HVRDateCombo;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.core.convert.TypeDescriptor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * <span class="logicName">【日時比較（対象項目 ≦
 * 比較項目）】</span>年月日項目と時分秒項目が分かれた日付同士の比較バリデータ。<br />
 * <br />
 * 対象日時 ≦ 比較日時でない場合エラー（年月日項目と時分秒項目が分かれた日付を比較）<br />
 * 定数との比較や日付だけの比較、時間だけの比較も可能。<br />
 *
 * @author kimura
 *
 */
@Data
public class HDateComboValidator implements ConstraintValidator<HVRDateCombo, Object> {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(HDateComboValidator.class);

    /** フォーマット定数：年月日 */
    protected static final String DATE_FORMAT = "yyyy/MM/dd";

    /** フォーマット定数：時分秒 */
    protected static final String TIME_FORMAT = "HH:mm:ss";

    /** 時間定数：日の初め */
    protected static final String BEGIN_OF_THE_DAY = "00:00:00";

    /** 時間定数：日の終わり */
    protected static final String END_OF_THE_DAY = "23:59:59";

    /** NGメッセージID:解析エラー */
    public static final String UNPARSABLE_MESSAGE_ID = "{HDateComboValidator.UNPARSABLE_detail}";

    /** NGメッセージID:相関必須 */
    public static final String REQUIRED_MESSAGE_ID = "{javax.validation.constraints.NotEmpty.message}";

    /** NGメッセージID:等しくない */
    public static final String EQUAL_MESSAGE_ID = "{HDateComboValidator.EQUAL_detail}";

    /** NGメッセージID:小さくない */
    public static final String LESS_MESSAGE_ID = "{HDateComboValidator.LESS_detail}";

    /** NGメッセージID:大きくない */
    public static final String GREATER_MESSAGE_ID = "{HDateComboValidator.GREATER_detail}";

    /** NGメッセージID:等しくないし小さくもない */
    public static final String LESS_EQUAL_MESSAGE_ID = "{HDateComboValidator.LESS_EQUAL_detail}";

    /** NGメッセージID:等しくないし大きくもない */
    public static final String GREATER_EQUAL_MESSAGE_ID = "{HDateComboValidator.GREATER_EQUAL_detail}";

    /** NGメッセージID:異なっていない */
    public static final String NOT_EQUAL_MESSAGE_ID = "{HDateComboValidator.NOT_EQUAL_detail}";

    /** 比較用左辺年月日値 */
    private String dateLeft;

    /** 比較用左辺年月日フィールド名 */
    private String dateLeftTarget;

    /** 比較用左辺年月日フォーマット */
    private String dateLeftFormat = DATE_FORMAT;

    /** 比較用左辺時分秒値 */
    private String timeLeft;

    /** 比較用左辺時分秒フィールド名 */
    private String timeLeftTarget;

    /** 比較用左辺時分秒フォーマット */
    private String timeLeftFormat = TIME_FORMAT;

    /** 比較用右辺年月日値 */
    private String dateRight;

    /** 比較用右辺年月日フィールド名 */
    private String dateRightTarget;

    /** 比較用右辺年月日フォーマット */
    private String dateRightFormat = DATE_FORMAT;

    /** 比較用右辺時分秒値 */
    private String timeRight;

    /** 比較用右辺時分秒フィールド名 */
    private String timeRightTarget;

    /** 比較用右辺時分秒フォーマット */
    private String timeRightFormat = TIME_FORMAT;

    /**
     * 演算子<br />
     * デフォルトは From - To 要
     */
    private String operator = "<=";

    /** 依存する日付情報がない時刻情報の入力を許可するかどうか */
    private boolean acceptOrphanedTime = false;

    /** 相関チェック対象 */
    private String[] fields;

    /** メッセージID */
    private String messageId;

    /** アノテーションの情報設定 */
    @Override
    public void initialize(HVRDateCombo annotation) {
        this.messageId = annotation.message();
        this.dateLeftFormat = annotation.dateLeftFormat();
        this.timeLeftFormat = annotation.timeLeftFormat();
        this.dateRightFormat = annotation.dateRightFormat();
        this.timeRightFormat = annotation.timeRightFormat();
        this.operator = annotation.operator();
        this.acceptOrphanedTime = annotation.acceptOrphanedTime();
        this.dateLeftTarget = annotation.dateLeftTarget();
        this.timeLeftTarget = annotation.timeLeftTarget();
        this.dateRightTarget = annotation.dateRightTarget();
        this.timeRightTarget = annotation.timeRightTarget();
    }

    /** スレッドセーフなワークオブジェクト */
    private class ThreadSafeWorkObject implements Serializable {

        /** シリアル */
        private static final long serialVersionUID = 6439010942329833316L;

        /** コンストラクタ */
        private ThreadSafeWorkObject(ConstraintValidatorContext context) {
            this.emptyLeft = true;
            this.emptyRight = true;
            this.dateLeftValue = HDateComboValidator.this.dateLeft;
            this.dateRightValue = HDateComboValidator.this.dateRight;
            this.timeLeftValue = HDateComboValidator.this.timeLeft;
            this.timeRightValue = HDateComboValidator.this.timeRight;
            this.dateLeftFormat = HDateComboValidator.this.dateLeftFormat;
            this.timeLeftFormat = HDateComboValidator.this.timeLeftFormat;
            this.dateRightFormat = HDateComboValidator.this.dateRightFormat;
            this.timeRightFormat = HDateComboValidator.this.timeRightFormat;
        }

        /** 左辺の日付時刻共に入力されていなかったかどうか */
        private boolean emptyLeft;

        /** 左辺の日付時刻共に入力されていなかったかどうか */
        private boolean emptyRight;

        /** 左辺日付項目入力値 */
        private String dateLeftValue;

        /** 左辺時刻項目入力値 */
        private String dateRightValue;

        /** 右辺日付項目入力値 */
        private String timeLeftValue;

        /** 右辺時刻項目入力値 */
        private String timeRightValue;

        /** 左辺の日付と時刻を合わせた情報 */
        private Date left;

        /** 右辺の日付と時刻を合わせた情報 */
        private Date right;

        /** 比較用左辺年月日フォーマット */
        private String dateLeftFormat = DATE_FORMAT;

        /** 比較用左辺時分秒フォーマット */
        private String timeLeftFormat = TIME_FORMAT;

        /** 比較用右辺年月日フォーマット */
        private String dateRightFormat = DATE_FORMAT;

        /** 比較用右辺時分秒フォーマット */
        private String timeRightFormat = TIME_FORMAT;
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

        ThreadSafeWorkObject work = new ThreadSafeWorkObject(context);

        // 比較用左辺・右辺の情報を取得する
        // 年月日＋時分秒の相関チェックが必要な場合は行う。
        // @see HDateComboValidator#acceptOrphanedTime
        boolean parseException = this.prepareTargetValues(context, value, work);

        // 日付/時間としてのパースができない場合は、
        // 別のバリデータがチェックしているはずなのでエラーメッセージは出さない。
        if (parseException) {
            LOGGER.debug("日時のパースに失敗しています。バリデーションは行いません。");
            return false;
        }

        // 全項目が空の場合はバリデーションを行わない
        if (work.emptyLeft || work.emptyRight) {
            LOGGER.debug("左辺または右辺が入力されていないので、バリデーションは行いません。");
            return true;
        }

        // 比較バリデーション
        if (!this.compareDates(context, work)) {
            return false;
        }

        return true;
    }

    /**
     * 検証準備を行う
     *
     * @param context ConstraintValidatorContext
     * @param value   Object
     * @param work    ワーク変数オブジェクト
     * @return true - 処理時に不正な値を発見
     */
    protected boolean prepareTargetValues(ConstraintValidatorContext context, Object value, ThreadSafeWorkObject work) {

        //
        // 左辺の年月日
        //

        // 左辺の年月日の入力値を取得
        String tmpDateLeft = getFieldValue(value, this.dateLeftTarget);
        if (!StringUtils.isEmpty(tmpDateLeft)) {
            work.dateLeftValue = tmpDateLeft;
            work.emptyLeft = false;
        }

        // 左辺の年月日に値がない場合は規定値を設定する
        if (StringUtils.isEmpty(work.dateLeftValue)) {
            work.dateLeftValue = new SimpleDateFormat(DATE_FORMAT).format(new Date());
            work.dateLeftFormat = DATE_FORMAT;
        } else {
            // 左辺に値が無かったぞマークを解除
            work.emptyLeft = false;
        }

        //
        // 左辺の時分秒
        //

        boolean timeLeftIsEmpty = false;

        // 左辺の時分秒の入力値を取得
        String tmpTimeLeft = getFieldValue(value, this.timeLeftTarget);
        if (!StringUtils.isEmpty(tmpTimeLeft)) {
            work.timeLeftValue = tmpTimeLeft;
        } else {
            timeLeftIsEmpty = true;
        }

        // 時刻のみを受付けない設定で、時刻があるのに日付がない場合は ValidationError とする
        if (!timeLeftIsEmpty && work.emptyLeft) {
            if (!this.acceptOrphanedTime) {
                makeContext(context, messageId, dateLeftTarget);
                return true;
            }
        }

        // 左辺の時分秒に値がない場合は規定値を設定する
        if (timeLeftIsEmpty) {

            work.timeLeftValue = BEGIN_OF_THE_DAY;
            work.timeLeftFormat = TIME_FORMAT;

            if (this.operator.contains(">") && this.operator.contains("=")) {
                work.timeLeftValue = END_OF_THE_DAY;
            }

        } else {
            // 左辺に値が無かったぞマークを解除
            work.emptyLeft = false;
        }

        //
        // 左辺の年月日と時分秒を連結し Date にパースする
        //

        try {
            work.left = new SimpleDateFormat(work.dateLeftFormat + " " + work.timeLeftFormat).parse(
                            work.dateLeftValue + " " + work.timeLeftValue);
        } catch (ParseException e) {
            // パース出来なくてもバリデーションエラーにしない。※別のバリデータが既にバリデーションエラーにしているはず
            return true;
        }

        //
        // 右辺の年月日
        //

        // 右辺の年月日の入力値を取得
        String tmpDateRight = getFieldValue(value, this.dateRightTarget);
        if (!StringUtils.isEmpty(tmpDateRight)) {
            work.dateRightValue = tmpDateRight;
            work.emptyRight = false;
        }

        // 右辺の年月日に値がない場合は規定値を設定する
        if (StringUtils.isEmpty(work.dateRightValue)) {
            work.dateRightValue = new SimpleDateFormat(DATE_FORMAT).format(new Date());
            work.dateRightFormat = DATE_FORMAT;
        } else {
            // 右辺に値が無かったぞマークを解除
            work.emptyRight = false;
        }

        //
        // 右辺の時分秒
        //

        boolean timeRightIsEmpty = false;

        // 右辺の時分秒の入力値を取得
        String tmpTimeRight = getFieldValue(value, this.timeRightTarget);
        if (!StringUtils.isEmpty(tmpTimeRight)) {
            work.timeRightValue = tmpTimeRight;
        } else {
            timeRightIsEmpty = true;
        }

        // 時刻のみを受付けない設定で、時刻があるのに日付がない場合は ValidationError とする
        if (!timeRightIsEmpty && work.emptyRight) {
            if (!this.acceptOrphanedTime) {
                makeContext(context, messageId, dateRightTarget);
                return true;
            }
        }

        // 右辺の時分秒に値がない場合は規定値を設定する
        if (timeRightIsEmpty) {

            work.timeRightValue = BEGIN_OF_THE_DAY;
            work.timeRightFormat = TIME_FORMAT;

            if (this.operator.contains("<") && this.operator.contains("=")) {
                work.timeRightValue = END_OF_THE_DAY;
            }

        } else {
            // 右辺に値が無かったぞマークを解除
            work.emptyRight = false;
        }

        //
        // 右辺の年月日と時分秒を連結し Date にパースする
        //

        try {
            work.right = new SimpleDateFormat(work.dateRightFormat + " " + work.timeRightFormat).parse(
                            work.dateRightValue + " " + work.timeRightValue);
        } catch (ParseException e) {
            // パース出来なくてもバリデーションエラーにしない。※別のバリデータが既にバリデーションエラーにしているはず
            return true;
        }

        return false;
    }

    /**
     * 日付項目の比較を行う。
     *
     * @param context コンテキスト
     * @param work    ワーク変数
     *
     * @return true..OK / false..NG
     */
    protected boolean compareDates(ConstraintValidatorContext context, ThreadSafeWorkObject work) {

        long left = work.left.getTime();
        long right = work.right.getTime();

        if (LOGGER.isDebugEnabled()) {
            SimpleDateFormat sdf = new SimpleDateFormat(work.dateRightFormat + " " + work.timeRightFormat);
            LOGGER.debug("比較します：" + sdf.format(work.left) + " " + this.operator + " " + sdf.format(work.right));
        }

        if ("=".equals(this.operator) || "==".equals(this.operator)) {

            // 左辺と右辺は等価か
            if (!(left == right)) {
                makeContext(context, EQUAL_MESSAGE_ID, timeRightTarget);
                return false;
            }

            return true;

        } else if ("<=".equals(this.operator) || "=<".equals(this.operator)) {

            // 左辺は右辺と同じかそれより小さいか
            if (!(left <= right)) {
                makeContext(context, LESS_EQUAL_MESSAGE_ID, timeRightTarget);
                return false;
            }

            return true;

        } else if (">=".equals(this.operator) || "=>".equals(this.operator)) {

            // 左辺は右辺と同じかそれより大きいか
            if (!(left >= right)) {
                makeContext(context, GREATER_EQUAL_MESSAGE_ID, timeRightTarget);
                return false;
            }

            return true;

        } else if ("<>".equals(this.operator) || "!=".equals(this.operator)) {

            // 左辺と右辺は等しくないか
            if (!(left != right)) {
                makeContext(context, NOT_EQUAL_MESSAGE_ID, timeRightTarget);
                return false;
            }

            return true;

        } else if ("<".equals(this.operator)) {

            // 左辺は右辺より小さいか
            if (!(left < right)) {
                makeContext(context, LESS_MESSAGE_ID, timeRightTarget);
                return false;
            }

        } else if (">".equals(this.operator)) {

            // 左辺は右辺より大きいか
            if (!(left > right)) {
                makeContext(context, GREATER_MESSAGE_ID, timeRightTarget);
                return false;
            }

            return true;

        }
        LOGGER.warn("バリデーションに使用する演算子が不正です。" + this.operator);
        return false;
    }

    /**
     * フィールドから値を取得
     *
     * @param value
     * @param field
     *
     * @return フィールドの値
     */
    protected String getFieldValue(Object value, String field) {

        BeanWrapper beanWrapper = new BeanWrapperImpl(value);

        TypeDescriptor targetValue = beanWrapper.getPropertyTypeDescriptor(field);
        if (targetValue != null) {

            String type = targetValue.getName();

            if ("java.lang.String".equals(type)) {

                Object result = beanWrapper.getPropertyValue(field);

                // nullの場合未変換
                if (result instanceof String) {
                    return result.toString();
                }
            }
        }
        return null;
    }

    /**
     * エラーメッセージを構成<br/>
     * メッセージセット＋エラーメッセージを表示する項目を指定
     */
    protected void makeContext(ConstraintValidatorContext context, String messageId, String errorFiled) {
        // 動的バリデーションによる手動バリデータの発火はこちらの設定は不要
        if (context == null) {
            return;
        }
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(messageId).addPropertyNode(errorFiled).addConstraintViolation();
    }
}