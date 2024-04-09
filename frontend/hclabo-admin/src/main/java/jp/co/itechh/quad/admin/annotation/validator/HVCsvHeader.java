package jp.co.itechh.quad.admin.annotation.validator;

import jp.co.itechh.quad.admin.validator.HCsvHeaderValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <span class="logicName">【CSVヘッダ】</span>CSV ヘッダバリデータのアノテーション。<br/>
 * <p>
 * CSV アップロード時に使用する。<br />
 * ページクラス内の {@link org.springframework.web.multipart.MultipartFile} フィールドに対して付与する。
 * </p>
 *
 * @author Phan Tien VU (VTI Japan Co., Ltd.)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Constraint(validatedBy = HCsvHeaderValidator.class)
public @interface HVCsvHeader {

    /**
     * メッセージID
     *
     * @return メッセージID
     */
    String message() default HCsvHeaderValidator.CSV_HEADER_VALIDATOR_MESSAGE_ID;

    /**
     * バリデーショングループ
     *
     * @return バリデーショングループ
     */

    Class<?>[] groups() default {};

    /**
     * ペイロード
     *
     * @return ペイロード
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * ヘッダー行の定義<br/>
     * <p>
     * CSV の1行目が、このパラメータで指定された CsvDto クラス内の定義と一致するかを検証する。<br />
     *
     * <span style="color:red">
     * 通常は header パラメータではなく、このパラメータを使用する。<br />
     * header パラメータとは併用しないこと。
     * </span>
     * </p>
     *
     * @see jp.co.itechh.quad.admin.annotation.csv.CsvColumn
     * @return ヘッダー行
     **/
    Class<?> csvDtoClass() default Object.class;

    /**
     * ヘッダー行の定義<br/>
     * <p>
     * CSV の1行目がこのパラメータの文字列と一致するかを検証する。<br/>
     * <span style="color:red">
     * 対応する CsvDto クラスがない CSV を検証する場合に使用する。<br />
     * csvDtoClass パラメータとは併用しないこと。
     * </span>
     * </p>
     * @return ヘッダー行
     */
    String header() default "";

    /**
     * 部分アップロードの禁止設定<br />
     * <pre>
     * true  - 部分アップロードを禁止する。(デフォルト)
     * false - 部分アップロードを許可する。
     * </pre>
     *
     * @return 部分アップロードを禁止する:true 許可する:false
     */
    boolean restrict() default true;

    /**
     * &#x40;CsvFile(header = false)の ≪すなわち、対になるダウンロードファイルにヘッダ行がない≫ 場合、ヘッダの検証を行うかどうか<br />
     * <pre>
     * true  - &#x40;CsvFile(header = false) のアノテーションがある場合はバリデーションを行わない。（デフォルト）
     * false - アノテーションにかかわらずバリデーションを行う。
     * </pre>
     * @return バリデーションを行わない:true 行う:false
     */
    boolean followCsvFileAnnotation() default true;

    /**
     * CSV ファイルの文字セット<br/>
     * <p>
     * MS932 がデフォルト
     * </p>
     * @return CSV ファイルの文字セット
     */
    String csvCharset() default "MS932";

    /**
     * 定義されていないカラムが CSV に含まれる場合、それを無視するかエラーとするかの指定。<br />
     * 部分アップロードに対応させる場合は、 true 指定を推奨。
     * <pre>
     * true  - 定義されていないカラムを含む CSV がアップロードされた場合、該当カラムは無視する。
     * false - 定義されていないカラムを含む CSV がアップロードされた場合、エラーとする。（デフォルト）
     * </pre>
     *
     * @return 該当カラムは無視する:true エラーとする:false
     */
    boolean allowUndefinedColumn() default false;

}