/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.validator;

import jp.co.itechh.quad.admin.annotation.validator.HVWindows31J;
import jp.co.itechh.quad.admin.annotation.validator.util.DynamicValidatorAnnotationUtil;
import lombok.Data;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.nio.charset.Charset;

/**
 * <span class="defaultValidator">★デフォルトバリデータ</span><br />
 * <span class="logicName">【Windows-31J文字列】</span>Windows-31Jのコード外の文字列チェックバリデータ。<br />
 * <br />
 * デフォルトの挙動：文字列にWindows-31Jに変換できない文字が含まれている場合エラー<br />
 * バリデータを指定すれば、Windows-31Jの文字コード範囲外の文字が入力可能となる<br />
 * <br />
 * <pre>
 * Windows-31Jの文字コード範囲外の文字を入力可能としたい項目に対して付与する。
 * デフォルトでは、Windows-31Jの文字コード範囲外の使用を認めていない。
 * ＜理由＞
 *  CSVファイルのダウンロードやメール送信時に文字化けが発生することを防ぐ目的の為
 * ＜使用箇所：抜粋＞
 *  郵便番号CSV　町域名(カナ)、町域名(カナ)
 * </pre>
 *
 * @author hk57400
 */
@Data
public class HWindows31JValidator implements ConstraintValidator<HVWindows31J, Object> {

    /** ロガー クラスは誤りではなく意図的 ローカルデバッグ用に専用ログへ集めている */
    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicValidatorAnnotationUtil.class);

    /** 強制変換し、OKとする文字 ※isValid()内の前提事項に記載した内容を参照 */
    private static final String REGEXP_FORCE_CONV_OK = "[―～∥－￠￡￢]";

    /** 強制変換し、NGとする文字 ※isValid()内の前提事項に記載した内容を参照 */
    private static final String REGEXP_FORCE_CONV_NG = "[—〜‖−¢£¬]";

    /** メッセージコード：標準 */
    public static final String MESSAGE_ID = "{HWindows31JValidator.INVALID_detail}";

    /**
     * デフォルトのWindows-31Jチェックではなく、JIS X 0208チェックとするフラグ
     * <pre>
     *   GMO連携項目など、NEC特殊文字・IBM拡張文字を弾きたい場合に利用する。
     *   ※注意※
     *     Shift_JIS <> Windows-31J 間でUnicode変換マッピングに差異があり、文字化けする文字は
     *     チェックを捻じ曲げて個別対応している。⇒ —〜‖−¢£¬
     *     内容・理由は、isValid()メソッドのコメントを参照。
     * </pre>
     */
    private boolean checkJISX0208;

    @Override
    public void initialize(HVWindows31J constraintAnnotation) {
        this.checkJISX0208 = constraintAnnotation.checkJISX0208();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        // 【前提事項①】
        // ＜JIS X 0208＞
        //   1-2区に全角記号、3区全角数字・ローマ字、4区平仮名、5区片仮名、6区ギリシャ文字、7区キリル文字、8区罫線、16～47区第一水準漢字、48～84区第二水準漢字
        //     http://www.asahi-net.or.jp/~ax2s-kmtn/ref/jisx0208.html
        // ＜Windows-31J(MS932)＞
        //   JIS X 0208-1990 をベースとし、13区に83字の「NEC特殊文字」を、89～92区に374字の「NEC選定IBM拡張文字」を、115～119区に 388字の「IBM拡張文字」を追加
        // ● システム標準のWindows-31Jチェックとは、下でチェックすることを指す
        // ● GMOでは、上しか使えないので独自チェックする（NEC特殊文字・IBM拡張文字が使えない）
        //     https://mp-faq.gmo-pg.com/s/article/F00148
        // ● 但し、実際にテストすると①や髙も普通にコンビニ注文が通る・・・が、FAQ記載の通りのチェックとしている

        // 【前提事項②】※JIS X 0208チェック限定事項
        // Ver.3でチェックを外している以下文字について
        //   ～∥－￠￡￢
        // ● これらは、Shift_JISとWindows-31Jで、Unicodeマッピングが異なる文字である
        // ● 以下文字がShift_JIS側のため注意が必要であるが、現状のJava・Tomcat構成上で処理した場合自動的に↑に変換されるため通常では試験できない -> デバッグで確認要
        //     〜‖−¢£¬
        // ● JavaのJIS X 0208ではShift_JIS側が正らしく、GMOのチェックとずれるため、以下のように捻じ曲げて実装している
        //     Shift_JIS側（〜‖−¢£¬）: 強制エラー
        //     Windows-31J側（～∥－￠￡￢）: 強制OK
        // ● 但し、実際にテストすると¢£¬については普通にコンビニ注文が通る・・・が、FAQ記載の通りのチェックとしている

        // 【前提事項③】※JIS X 0208チェック限定事項
        // Ver.3でチェックを外している以下文字について
        //   ―
        // ● 前提事項②同様、Shift_JISとWindows-31Jで、Unicodeマッピングが異なる文字である
        // ● 以下文字がShift_JIS側のため注意が必要である
        //     —
        // ● ―と—に関しては、Unicode:2014側（—）が正規マッピングと途中でリファレンス修正されたため、現在はUnicode:2015側（―）には変換されない
        // ● JavaのJIS X 0208ではShift_JIS側が正らしく、GMOのチェックとずれるため、以下のように捻じ曲げて実装している
        //     Shift_JIS側（—）: 強制エラー
        //     Windows-31J側（―）: 強制OK

        // 【前提事項④】※JIS X 0208チェック限定事項
        // Ver.3で強制エラーとしている以下文字について
        //   ≒≡∫√⊥∠∵∩∪
        // ● これらは、通常2区と、13区NEC特殊文字に重複登録されている文字である
        // ● Ver.3対応が誤りで、2区の文字として扱い登録できるのが正しい仕様である（GMOも利用可能）

        // 実装時検討・調査資料
        //   https://docs.google.com/spreadsheets/d/1dSF-18-KxfPNtJ1iV66dlsCo1iP70b8syuEy2Yb3ouw
        //   「文字コード確認」シート

        if (checkJISX0208) {
            return checkJISX0208(value, context);
        } else {
            return checkWindows31J(value, context);
        }
    }

    /**
     * JIS X 0208チェック
     *
     * @param value バリデーション対象Object
     * @param context コンテキスト
     * @return true:OK, false:バリデーションエラー
     */
    private boolean checkJISX0208(Object value, ConstraintValidatorContext context) {
        return check(value, context, Charset.forName("x-JIS0208"), true);
    }

    /**
     * Windows-31Jチェック
     *
     * @param value バリデーション対象Object
     * @param context コンテキスト
     * @return true:OK, false:バリデーションエラー
     */
    private boolean checkWindows31J(Object value, ConstraintValidatorContext context) {
        return check(value, context, Charset.forName("windows-31j"), false);
    }

    /**
     * 文字コードチェック
     *
     * @param value バリデーション対象Object
     * @param context コンテキスト
     * @param charset 文字セット
     * @param forceConv 強制的に文字変換してチェックするフラグ
     * @return true:OK, false:バリデーションエラー
     */
    private boolean check(Object value, ConstraintValidatorContext context, Charset charset, boolean forceConv) {

        // ※注意※ DynamicValidatorAnnotationUtilにも記載したとおり、String型ではないObject型のフィールドも容赦なくここへ到達する
        if (ObjectUtils.isEmpty(value)) {
            return true;
        }
        String target = value.toString();
        if (StringUtils.isEmpty(target)) {
            return true;
        }

        // isValid()内の前提事項に記載した内容を参照
        if (forceConv) {
            target = convTarget(target);
        }

        // 1度指定した文字セットに変換してから戻し、元の文字列と一致するか確認
        String conv = new String(target.getBytes(charset), charset);
        boolean ret = target.equals(conv);

        // デバッグログ出力
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format(
                            "[HWindows31JValidator] チェック結果: %-5b チェック対象: %s", ret, getTargetInfo(target, context)));
        }

        return ret;
    }

    /**
     * チェック対象文字列の強制変換
     * <pre>
     *   isValid()内の前提事項に記載した内容を参照
     * </pre>
     *
     * @param target 対象文字列
     * @return 変換後文字列
     */
    private String convTarget(String target) {
        return target.replaceAll(REGEXP_FORCE_CONV_OK, "").replaceAll(REGEXP_FORCE_CONV_NG, "①");
    }

    /**
     * ログ出力情報生成
     *
     * @param target 対象文字列
     * @param context コンテキスト
     * @return 情報
     */
    private String getTargetInfo(String target, ConstraintValidatorContext context) {

        StringBuilder sb = new StringBuilder();

        // 対象フィールド
        try {
            Field field = context.getClass().getDeclaredField("basePath");
            field.setAccessible(true);
            sb.append(field.get(context).toString());
        } catch (Exception e) {
            LOGGER.error("[HWindows31JValidator] ログ出力中にエラー", e);
        }

        // 入力値
        sb.append(" target: ");
        sb.append(target);

        // HEX
        sb.append(" hex: ");
        for (char c : target.toCharArray()) {
            sb.append('x');
            sb.append(StringUtils.leftPad(Integer.toHexString(c).toUpperCase(), 4, '0'));
        }

        return sb.toString();
    }

}
