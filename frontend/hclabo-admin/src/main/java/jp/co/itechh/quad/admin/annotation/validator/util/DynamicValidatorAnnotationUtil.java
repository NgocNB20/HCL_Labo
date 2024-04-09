/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.annotation.validator.util;

import jp.co.itechh.quad.admin.annotation.csv.CsvColumn;
import jp.co.itechh.quad.admin.annotation.validator.HVBothSideSpace;
import jp.co.itechh.quad.admin.annotation.validator.HVDate;
import jp.co.itechh.quad.admin.annotation.validator.HVHtml;
import jp.co.itechh.quad.admin.annotation.validator.HVItems;
import jp.co.itechh.quad.admin.annotation.validator.HVMailAddressExtended;
import jp.co.itechh.quad.admin.annotation.validator.HVMailAddressExtendedArray;
import jp.co.itechh.quad.admin.annotation.validator.HVNumber;
import jp.co.itechh.quad.admin.annotation.validator.HVSpecialCharacter;
import jp.co.itechh.quad.admin.annotation.validator.HVWindows31J;
import jp.co.itechh.quad.admin.constant.type.SpaceValidateMode;
import jp.co.itechh.quad.admin.validator.HBothSideSpaceValidator;
import jp.co.itechh.quad.admin.validator.HSpecialCharacterValidator;
import jp.co.itechh.quad.admin.validator.HWindows31JValidator;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.CreditCardNumber;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;
import org.hibernate.validator.internal.metadata.location.ConstraintLocation.ConstraintLocationKind;
import org.hibernate.validator.internal.properties.Constrainable;
import org.hibernate.validator.internal.properties.javabean.JavaBeanAnnotatedElement;
import org.hibernate.validator.internal.properties.javabean.JavaBeanField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Payload;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Negative;
import javax.validation.constraints.NegativeOrZero;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * デフォルトValidatorアノテーション設定Util
 *
 * @author hk57400
 */
public class DynamicValidatorAnnotationUtil {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicValidatorAnnotationUtil.class);

    /** デフォルトバリデータ自体の付与が不要なアノテーション ※付与されている場合、動的追加は行わない */
    private static final Set<Class<? extends Annotation>> ANNOTATION_SET_NO_NEED;

    /** 両端空白バリデータが不要なチェックアノテーション ※付与されている場合、動的追加は行わない */
    private static final Set<Class<? extends Annotation>> ANNOTATION_SET_NO_NEED_BOTH_SIDE_SPACE;

    /** 特殊文字バリデータが不要なチェックアノテーション ※付与されている場合、動的追加は行わない */
    private static final Set<Class<? extends Annotation>> ANNOTATION_SET_NO_NEED_SPECIAL_CHAR;

    /** Windows31Jバリデータが不要なチェックアノテーション ※付与されている場合、動的追加は行わない */
    private static final Set<Class<? extends Annotation>> ANNOTATION_SET_NO_NEED_WINDOWS31J;

    static {

        // 【前提事項】
        // Model/Itemの各フィールドの解析タイミングで処理を差し込んでいるため
        // ・フィールドに宣言されたアノテーション
        // しか判定処理できない。
        // ・Model/Itemクラス自体に付与されたアノテーション（相関チェック）
        // ・Controllerに付与された動的Validator
        // は判定不可のため、注意。

        ANNOTATION_SET_NO_NEED = Set.of(
                        // ●Ver.4で新規追加した概念、経緯はgetAnnotations()メソッド参照
                        Valid.class                         // 子Itemチェック対象マーキング
                                       );

        // String型のフィールドに対してデフォルトバリデータが不要なチェックアノテーション
        final Set<Class<? extends Annotation>> ANNOTATION_SET_NO_NEED_STRING_FIELD = Set.of(
                        // ●以下、Ver.3と仕様比較
                        Pattern.class,                      // 正規表現
                        CreditCardNumber.class,             // クレジットカード番号
                        // HVTelephoneNumber                   電話番号                     正規表現チェックになったため削除    -> Pattern
                        // HVZipCode                           郵便番号                     相関チェックになったため判定不可    -> HVRZipCode
                        Email.class,                        // メールアドレス
                        HVMailAddressExtended.class,        // 拡張メールアドレス
                        HVMailAddressExtendedArray.class,   // 拡張メールアドレス配列
                        // HVPassword                          パスワード                    正規表現チェックになったため削除    -> Pattern
                        URL.class,                          // URL
                        HVNumber.class,                     // 数値型
                        Digits.class,                       // 数値桁数
                        Min.class,                          // 数値範囲
                        Max.class,                          // 数値範囲
                        Positive.class,                     // 数値範囲
                        PositiveOrZero.class,               // 数値範囲
                        Negative.class,                     // 数値範囲
                        NegativeOrZero.class,               // 数値範囲
                        Range.class,                        // 数値範囲
                        // HVNumberEqual                       数値比較、対象項目 ＝ 比較項目   Ver.4利用箇所なしのため削除
                        // HVNumberGreater                     数値比較、対象項目 ＞ 比較項目   相関チェックになったため判定不可 -> HVRNumberGreater
                        // HVNumberGreaterEqual                数値比較、対象項目 ≧ 比較項目   相関チェックになったため判定不可 -> HVRNumberGreaterEqual
                        DecimalMin.class,                   // 数値比較、対象項目 ＞ 設定値
                        // HVNumberLess                        数値比較、対象項目 ＜ 比較項目   Ver.4利用箇所なしのため削除
                        // HVNumberLessEqual                   数値比較、対象項目 ≦ 比較項目   相関チェックになったため判定不可 -> HVRNumberLessEqual
                        DecimalMax.class,                   // 数値比較、対象項目 ＜ 設定値
                        HVDate.class,                       // 日付妥当性
                        // HVSeparateDate                      日付妥当性（年月日分割）        相関チェックになったため判定不可 -> HVRSeparateDate
                        // HVDateEqual                         日付比較、対象項目 ＝ 比較項目   Ver.4利用箇所なしのため削除
                        // HVDateGreater                       日付比較、対象項目 ＞ 比較項目   Ver.4利用箇所なしのため削除
                        // HVDateGreaterEqual                  日付比較、対象項目 ≧ 比較項目   相関チェックになったため判定不可 -> HVRDateGreaterEqual
                        // HVDateLess                          日付比較、対象項目 ＜ 比較項目   Ver.4利用箇所なしのため削除
                        // HVDateLessEqual                     日付比較、対象項目 ≦ 比較項目   Ver.4利用箇所なしのため削除
                        // HVDateRange                         日付範囲                     相関チェックになったため判定不可 -> HVRDateRange
                        // HVDateCombo                         日付組み合わせ比較             相関チェックになったため判定不可 -> HVRDateCombo
                        // HVSeparateDateTime                  日時妥当性（日付時刻分割）      相関チェックになったため判定不可 -> HVRSeparateDateTime
                        // ●以下、Ver.4で追加
                        HVItems.class,                      // Items
                        Null.class                          // NULL
                                                                                           );

        Set<Class<? extends Annotation>> noNeedBSSSet = new HashSet<>(ANNOTATION_SET_NO_NEED_STRING_FIELD);
        noNeedBSSSet.add(HVBothSideSpace.class);            // 自分自身
        ANNOTATION_SET_NO_NEED_BOTH_SIDE_SPACE = Collections.unmodifiableSet(noNeedBSSSet);

        Set<Class<? extends Annotation>> noNeedSCSet = new HashSet<>(ANNOTATION_SET_NO_NEED_STRING_FIELD);
        noNeedSCSet.add(HVSpecialCharacter.class);          // 自分自身
        // noNeedSCSet.add(HVZenkaku.class);                   全角                        正規表現チェックになったため削除    -> Pattern
        ANNOTATION_SET_NO_NEED_SPECIAL_CHAR = Collections.unmodifiableSet(noNeedSCSet);

        ANNOTATION_SET_NO_NEED_WINDOWS31J = Set.of(
                        // ●以下、Ver.3と仕様比較
                        CreditCardNumber.class,             // クレジットカード番号
                        // HVTelephoneNumber                   電話番号                     正規表現チェックになったため削除    -> Pattern
                        //   なお、Ver.3と同じく、正規表現Validator(Pattern)は不要対象に含めていない（\D,\L,\Sなどで定義されると利用不可文字が入ってしまうため）
                        // HVZipCode                           郵便番号                     相関チェックになったため判定不可    -> HVRZipCode
                        Email.class,                        // メールアドレス
                        HVMailAddressExtended.class,        // 拡張メールアドレス
                        HVMailAddressExtendedArray.class,   // 拡張メールアドレス配列
                        HVWindows31J.class,                 // 自分自身
                        // ●以下、Ver.4で追加
                        Null.class                          // NULL
                                                  );
    }

    /**
     * デフォルトValidatorを追加したアノテーション配列を取得
     *
     * @param constrainable 処理中のオブジェクトが定義されているオブジェクト ※フィールドの場合、基本的にannotatedElementと同じ
     * @param annotatedElement アノテーション抽出処理中のオブジェクト
     * @param kind オブジェクトの種類（メソッド・フィールド・コンストラクタ、など）
     * @return デフォルトValidator追加済のアノテーション配列
     */
    public static Annotation[] getAnnotations(Constrainable constrainable,
                                              JavaBeanAnnotatedElement annotatedElement,
                                              ConstraintLocationKind kind) {

        // カスタマイズ前処理
        Annotation[] annotations = annotatedElement.getDeclaredAnnotations();

        // 処理対象のObjectのtypeがフィールドの場合のみカスタマイズ
        if (kind != ConstraintLocationKind.FIELD) {
            return annotations;
        }

        // CSV-UL用 ★adminのみ
        // そのフィールドが所属するクラス名がCsvDtoの場合のみカスタマイズ
        String objectName = constrainable.getDeclaringClass().getSimpleName();
        if (StringUtils.endsWith(objectName, "CsvDto")) {
            try {
                return adjustAnnotationsForCsv(constrainable, (JavaBeanField) annotatedElement, annotations).toArray(
                                new Annotation[] {});
            } catch (Exception e) {
                // 万が一想定しているJavaBeanFieldじゃないのが来たら、無視してフレームワーク元処理に任せる
                LOGGER.error(String.format("[%-60s] ★★★★★★★★★★ CSVデフォルトValidator追加処理で想定外パターンを検知 ★★★★★★★★★★",
                                           logName(constrainable, annotatedElement)
                                          ), e);
                return annotations;
            }
        }

        // そのフィールドが所属するクラス名がModelの場合のみカスタマイズ
        // TODO 子要素はModelItemで絞りたいが、Itemしかつけてない命名規約違反クラスが多いのでItemとしておく
        if (!StringUtils.endsWith(objectName, "Model") && !StringUtils.endsWith(objectName, "Item")) {
            return annotations;
        }

        // AbstractModelの pageInfo, reloadFlag は処理対象から外す
        if ("AbstractModel".equals(objectName)) {
            return annotations;
        }

        // POST値以外もSpringBootがValidationを実行してしまうため、Modelに値を保持しているだけのフィールドは対象から外す
        //   ※特殊文字チェックなどが意図せず保持値に引っかかることによる、画面遷移停止を防ぐため
        // POST値には通常は必ず単項目チェックのアノテーションやコンバータが付与されているはずのため、
        // ModelItemをチェックするための@Valid以外のアノテーションがない項目は、画面入力値ではないと判断する
        // 【注意】
        // 稀に、相関チェック（動的Validator）でチェックするからという理由で、単項目チェックをしていない項目がある
        // このフィールドには、デフォルトValidatorは付与されない仕様
        // 本来は、長さ・数値・日時など、最低限の単項目チェックは行ってから、相関チェックすべきである
        if (ArrayUtils.isEmpty(annotations) || hasTargetAnnotations(annotations, ANNOTATION_SET_NO_NEED)) {
            LOGGER.info(String.format("[%-60s] デフォルトValidator追加処理対象外", logName(constrainable, annotatedElement)));
            return annotations;
        }

        try {
            return adjustAnnotations(constrainable, (JavaBeanField) annotatedElement, annotations).toArray(
                            new Annotation[] {});
        } catch (Exception e) {
            // 万が一想定しているJavaBeanFieldじゃないのが来たら、無視してフレームワーク元処理に任せる
            LOGGER.error(String.format("[%-60s] ★★★★★★★★★★ デフォルトValidator追加処理で想定外パターンを検知 ★★★★★★★★★★",
                                       logName(constrainable, annotatedElement)
                                      ), e);
        }

        return annotations;
    }

    /**
     * 定義されているアノテーションに、デフォルトValidatorのアノテーションを追加
     *
     * @param constrainable 処理中のオブジェクトが定義されているオブジェクト ※フィールドの場合、基本的にannotatedElementと同じ
     * @param javaBeanField アノテーション抽出処理中のオブジェクト
     * @param annotations アノテーション抽出処理中のオブジェクトに実際に定義されている全アノテーションの配列
     * @return デフォルトValidator追加済のアノテーション配列
     */
    private static List<Annotation> adjustAnnotations(Constrainable constrainable,
                                                      JavaBeanField javaBeanField,
                                                      Annotation[] annotations) {

        // 本処理には、画面Modelが保持しているフィールドのみが来ている前提
        // また、各Modelクラスにつき、MetaDataキャッシュ時の1回だけしか実行されない前提
        List<Annotation> adjustAnnotations = new ArrayList<>();
        String logName = logName(constrainable, javaBeanField);

        LOGGER.info(String.format("[%-60s] ==================== デフォルトValidator追加処理を開始 ====================", logName));
        LOGGER.info(String.format("[%-60s] 調整前のアノテーション: %s", logName, logAnnotation(Arrays.stream(annotations))));

        // バリデートする対象イベントのグループを取得
        List<Class<?>> groups = getGroups(annotations);

        // String型のフィールドの場合、両端空白バリデータ・特殊文字バリデータを追加
        if (javaBeanField.getType() == String.class) {
            addBothSideSpaceValidator(adjustAnnotations, annotations, groups, logName);
            addSpecialCharacterValidator(adjustAnnotations, annotations, groups, logName);
        }

        // String型以外も含め、全フィールドについてWindows31Jバリデータを追加
        // TODO Ver.3がString以外のフィールドにもすべて付けていたので踏襲している
        //   String型以外のフィールドにも念のため掛けたいのはわかるが、Itemsとかについてるのはどうかなと・・・（Validator側でチェック時にtoString）
        addWindows31JValidator(adjustAnnotations, annotations, groups, logName);

        // 実際に定義されているバリデータを追加
        adjustAnnotations.addAll(List.of(annotations));

        // TODO 未実装 ExcelLengthバリデータ
        //   Excel数式（=""で囲う）方式でのCSV出力を現状採用していないため、改行なし255文字制約が存在しない

        LOGGER.info(String.format("[%-60s] 調整後のアノテーション: %s", logName, logAnnotation(adjustAnnotations.stream())));
        LOGGER.info(String.format("[%-60s] ==================== デフォルトValidator追加処理を終了 ====================", logName));

        return adjustAnnotations;
    }

    /**
     * 定義されているアノテーションに、デフォルトValidatorのアノテーションを追加 ※CSV用
     *
     * @param constrainable 処理中のオブジェクトが定義されているオブジェクト ※フィールドの場合、基本的にannotatedElementと同じ
     * @param javaBeanField アノテーション抽出処理中のオブジェクト
     * @param annotations アノテーション抽出処理中のオブジェクトに実際に定義されている全アノテーションの配列
     * @return デフォルトValidator追加済のアノテーション配列
     */
    private static List<Annotation> adjustAnnotationsForCsv(Constrainable constrainable,
                                                            JavaBeanField javaBeanField,
                                                            Annotation[] annotations) {

        // 本処理には、CsvDtoが保持しているフィールドのみが来ている前提
        // また、各CsvDtoクラスにつき、MetaDataキャッシュ時の1回だけしか実行されない前提
        List<Annotation> adjustAnnotations = new ArrayList<>();
        String logName = logName(constrainable, javaBeanField);

        // @CsvColumn がついていないフィールドは、そのまま返す
        if (!hasTargetAnnotations(annotations, Set.of(CsvColumn.class))) {
            LOGGER.info(String.format("[%-60s] CSVデフォルトValidator追加処理対象外", logName));
            adjustAnnotations.addAll(List.of(annotations));
            return adjustAnnotations;
        }

        LOGGER.info(String.format(
                        "[%-60s] ==================== CSVデフォルトValidator追加処理を開始 ====================", logName));
        LOGGER.info(String.format("[%-60s] 調整前のアノテーション: %s", logName, logAnnotation(Arrays.stream(annotations))));

        // バリデートする対象イベントのグループを取得
        List<Class<?>> groups = getGroups(annotations);

        // String型以外も含め、全フィールドについて特殊文字バリデータを追加
        //   Ver.3がString以外のフィールドにもすべて付けていたので踏襲している
        //   CsvDtoは、画面とは異なり、Stringで定義されているとは限らないためこれは必要処理
        //   TODO ただし、先にDto変換処理が走るため、String型以外はチェックする前に暗黙処理されてしまう ⇒ 実質無意味…
        addSpecialCharacterValidator(adjustAnnotations, annotations, groups, logName);

        // TODO 未実装 両端空白バリデータ・Windows31Jバリデータ
        //   両端空白バリデータは、Ver.3も設定していないため ※画面コピペ時にスペース入っちゃう件に対する対策だからCSVは入れてないと思われる
        //   Windows31Jバリデータは、CSVファイル保存した時点で?に化けており、チェック入れても?は通してしまって意味がないため未実装
        //     ※将来、CSVの文字コードをUTF-8化する場合は、案件によっては対応が必要

        // 実際に定義されているバリデータを追加
        adjustAnnotations.addAll(List.of(annotations));

        // TODO 未実装 ExcelLengthバリデータ
        //   Excel数式（=""で囲う）方式でのCSV出力を現状採用していないため、改行なし255文字制約が存在しない

        LOGGER.info(String.format("[%-60s] 調整後のアノテーション: %s", logName, logAnnotation(adjustAnnotations.stream())));
        LOGGER.info(String.format(
                        "[%-60s] ==================== CSVデフォルトValidator追加処理を終了 ====================", logName));

        return adjustAnnotations;
    }

    /**
     * バリデートする対象イベントの設定グループを取得
     *
     * @param annotations アノテーション配列
     * @return 対象イベントグループ配列
     */
    private static List<Class<?>> getGroups(Annotation[] annotations) {

        Set<Class<?>> groups = new LinkedHashSet<>();

        for (Annotation annotation : annotations) {
            // TODO groups設定されてないアノテーションが1件でもいたら全グループ対象としたいが、コンバータのアノテーションにすべて持っていかれるので諦め
            groups.addAll(getGroups(annotation));
        }

        return new ArrayList<>(groups);
    }

    /**
     * バリデートする対象イベントの設定グループを取得
     *
     * @param annotation アノテーション
     * @return 対象イベントグループ配列
     */
    private static List<Class<?>> getGroups(Annotation annotation) {

        Class<?>[] groups;

        try {
            // groupsメソッドがあれば取得
            Method groupsMethod = annotation.getClass().getMethod("groups");
            // 設定値取得
            groups = (Class<?>[]) groupsMethod.invoke(annotation);
        } catch (Exception e) {
            return new ArrayList<>();
        }

        return List.of(groups);
    }

    /**
     * 両端空白バリデータを追加
     *
     * @param adjustAnnotations デフォルトValidatorを含む全アノテーションの配列
     * @param annotations アノテーション抽出処理中のオブジェクトに実際に定義されている全アノテーションの配列
     * @param groups 対象イベントグループ配列
     * @param logName ログ出力文言
     */
    private static void addBothSideSpaceValidator(List<Annotation> adjustAnnotations,
                                                  Annotation[] annotations,
                                                  List<Class<?>> groups,
                                                  String logName) {

        // 該当アノテーションが付与されているフィールドには追加しない
        if (hasTargetAnnotations(annotations, ANNOTATION_SET_NO_NEED_BOTH_SIDE_SPACE)) {
            LOGGER.info(String.format("[%-60s]   - 両端空白バリデータは不要", logName));
            return;
        }

        // TODO 本当は equals, hashCode なども正確にトレースすべきだが現状諦めている -> AnnotationInvocationHandler 参照
        //   Ver.3もこのインスタンス作成方法だったので様子見
        adjustAnnotations.add(new HVBothSideSpace() {

            private final Class<? extends Annotation> type = HVBothSideSpace.class;

            @Override
            public Class<? extends Annotation> annotationType() {
                return type;
            }

            @Override
            public String message() {
                return HBothSideSpaceValidator.SPACE_DENY_MESSAGE_ID; // アノテーションのdefaultが拾えないため、指定
            }

            @Override
            public Class<?>[] groups() {
                return groups.toArray(new Class[] {});
            }

            @Override
            @SuppressWarnings("unchecked")
            public Class<? extends Payload>[] payload() {
                return new Class[] {}; // HIT-MALLでは未使用だが、アノテーションのdefaultが拾えないため、指定
            }

            @Override
            public SpaceValidateMode startWith() {
                return SpaceValidateMode.DENY_SPACE; // デフォルトバリデータは許可しない
            }

            @Override
            public SpaceValidateMode endWith() {
                return SpaceValidateMode.DENY_SPACE; // デフォルトバリデータは許可しない
            }
        });

        LOGGER.info(String.format("[%-60s]   + 両端空白バリデータを追加", logName));
    }

    /**
     * 特殊文字バリデータを追加
     *
     * @param adjustAnnotations デフォルトValidatorを含む全アノテーションの配列
     * @param annotations アノテーション抽出処理中のオブジェクトに実際に定義されている全アノテーションの配列
     * @param groups 対象イベントグループ配列
     * @param logName ログ出力文言
     */
    private static void addSpecialCharacterValidator(List<Annotation> adjustAnnotations,
                                                     Annotation[] annotations,
                                                     List<Class<?>> groups,
                                                     String logName) {

        // 該当アノテーションが付与されているフィールドには追加しない
        if (hasTargetAnnotations(annotations, ANNOTATION_SET_NO_NEED_SPECIAL_CHAR)) {
            LOGGER.info(String.format("[%-60s]   - 特殊文字バリデータは不要", logName));
            return;
        }

        // HTMLバリデータのアノテーションが存在する場合、タブ・改行・半角記号は許可（制御文字だけ禁止）
        //   Ver.3はバグっていて、記号を許可できていないので修正（タブ・改行だけになってる）
        // それ以外の場合は、半角記号のみ許可
        boolean allowPunctuation = hasTargetAnnotations(annotations, Set.of(HVHtml.class));
        boolean allowSymbol = !allowPunctuation;

        // TODO 本当は equals, hashCode なども正確にトレースすべきだが現状諦めている -> AnnotationInvocationHandler 参照
        //   Ver.3もこのインスタンス作成方法だったので様子見
        adjustAnnotations.add(new HVSpecialCharacter() {

            private final Class<? extends Annotation> type = HVSpecialCharacter.class;

            @Override
            public Class<? extends Annotation> annotationType() {
                return type;
            }

            @Override
            public String message() {
                return HSpecialCharacterValidator.MESSAGE_ID; // アノテーションのdefaultが拾えないため、指定
            }

            @Override
            public Class<?>[] groups() {
                return groups.toArray(new Class[] {});
            }

            @Override
            @SuppressWarnings("unchecked")
            public Class<? extends Payload>[] payload() {
                return new Class[] {}; // HIT-MALLでは未使用だが、アノテーションのdefaultが拾えないため、指定
            }

            @Override
            public boolean allowPunctuation() {
                return allowPunctuation;
            }

            @Override
            public boolean allowSymbol() {
                return allowSymbol;
            }

            @Override
            public char[] allowCharacters() {
                return new char[] {}; // デフォルトバリデータは上記フラグいずれかで判定させるため不要
            }
        });

        LOGGER.info(String.format("[%-60s]   + 特殊文字バリデータを追加(allowPunctuation=%b,allowSymbol=%b)", logName,
                                  allowPunctuation, allowSymbol
                                 ));
    }

    /**
     * Windows31Jバリデータを追加
     *
     * @param adjustAnnotations デフォルトValidatorを含む全アノテーションの配列
     * @param annotations アノテーション抽出処理中のオブジェクトに実際に定義されている全アノテーションの配列
     * @param groups 対象イベントグループ配列
     * @param logName ログ出力文言
     */
    private static void addWindows31JValidator(List<Annotation> adjustAnnotations,
                                               Annotation[] annotations,
                                               List<Class<?>> groups,
                                               String logName) {

        // 該当アノテーションが付与されているフィールドには追加しない
        if (hasTargetAnnotations(annotations, ANNOTATION_SET_NO_NEED_WINDOWS31J)) {
            LOGGER.info(String.format("[%-60s]   - Windows31Jバリデータは不要", logName));
            return;
        }

        // TODO 本当は equals, hashCode なども正確にトレースすべきだが現状諦めている -> AnnotationInvocationHandler 参照
        //   Ver.3もこのインスタンス作成方法だったので様子見
        adjustAnnotations.add(new HVWindows31J() {

            private final Class<? extends Annotation> type = HVWindows31J.class;

            @Override
            public Class<? extends Annotation> annotationType() {
                return type;
            }

            @Override
            public String message() {
                return HWindows31JValidator.MESSAGE_ID; // アノテーションのdefaultが拾えないため、指定
            }

            @Override
            public Class<?>[] groups() {
                return groups.toArray(new Class[] {});
            }

            @Override
            @SuppressWarnings("unchecked")
            public Class<? extends Payload>[] payload() {
                return new Class[] {}; // HIT-MALLでは未使用だが、アノテーションのdefaultが拾えないため、指定
            }

            @Override
            public boolean checkJISX0208() {
                return false; // デフォルトバリデータはWindows-31Jチェック
            }
        });

        LOGGER.info(String.format("[%-60s]   + Windows31Jバリデータを追加", logName));
    }

    /**
     * デフォルトValidatorの設定が必要かどうか判断
     *
     * @param annotations アノテーション抽出処理中のオブジェクトに実際に定義されている全アノテーションの配列
     * @param targetAnnotationSet 同種として扱うアノテーションセット
     * @return true:すでに同種のアノテーションが付与されている, false:同種のアノテーションはない
     */
    private static boolean hasTargetAnnotations(Annotation[] annotations,
                                                Set<Class<? extends Annotation>> targetAnnotationSet) {

        // 【注意】
        // annotationTypeのアノテーションインタフェース同士のequals/contains比較だと、@HVWindows31J がなぜかfalseとなる
        // @Pattern などは正常に比較できるので謎だが、ClassLoaderが定義時と実行時で異なっている？デバッグ実行・ブレークポイントも影響しているかも
        // とりあえず、あまりやりたくないがclass名で比較している
        return Arrays.stream(annotations)
                     .anyMatch(annotation -> targetAnnotationSet.stream()
                                                                .anyMatch(target -> target.getName()
                                                                                          .equals(annotation.annotationType()
                                                                                                            .getName())));
    }

    /**
     * ログ出力文言生成 ※フィールド名
     *
     * @param constrainable 処理中のオブジェクトが定義されているオブジェクト ※フィールドの場合、基本的にannotatedElementと同じ
     * @param annotatedElement アノテーション抽出処理中のオブジェクト
     * @return ログ出力文言
     */
    private static String logName(Constrainable constrainable, JavaBeanAnnotatedElement annotatedElement) {

        String modelName = constrainable.getDeclaringClass().getSimpleName();
        String fieldName = (annotatedElement instanceof JavaBeanField) ?
                        ((JavaBeanField) annotatedElement).getName() :
                        constrainable.getName();

        return modelName + "#" + fieldName;
    }

    /**
     * ログ出力文言生成 ※アノテーション
     *
     * @param stream 全アノテーションの配列・リストのストリーム
     * @return ログ出力文言
     */
    private static String logAnnotation(Stream<Annotation> stream) {
        return stream.map(a -> "@" + a.annotationType().getSimpleName() + logGroups(a))
                     .collect(Collectors.joining(", "));
    }

    /**
     * ログ出力文言生成 ※バリデート対象グループ
     *
     * @param annotation アノテーション
     * @return ログ出力文言
     */
    private static String logGroups(Annotation annotation) {

        List<Class<?>> groups = getGroups(annotation);
        if (ObjectUtils.isEmpty(groups)) {
            return "";
        }

        return "(for:" + groups.stream().map(Class::getSimpleName).collect(Collectors.joining(",")) + ")";
    }

}
