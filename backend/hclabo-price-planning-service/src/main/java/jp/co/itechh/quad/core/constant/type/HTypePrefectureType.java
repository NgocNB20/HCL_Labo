/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.constant.type;

import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.seasar.doma.Domain;

import java.util.HashSet;
import java.util.Set;

/**
 * 都道府県種別：列挙型
 *
 * @author kaneda
 *
 */
@Domain(valueType = String.class, factoryMethod = "of")
@Getter
@AllArgsConstructor
public enum HTypePrefectureType implements EnumType {

    /**北海道*/
    HOKKAIDO("北海道", "01"),

    /**青森県*/
    AOMORI("青森県", "02"),

    /**岩手県*/
    IWATE("岩手県", "03"),

    /**宮城県*/
    MIYAGI("宮城県", "04"),

    /**秋田県*/
    AKITA("秋田県", "05"),

    /**山形県*/
    YAMAGATA("山形県", "06"),

    /**福島県*/
    FUKUSHIMA("福島県", "07"),

    /**茨城県*/
    IBARAKI("茨城県", "08"),

    /**栃木県*/
    TOCHIGI("栃木県", "09"),

    /**群馬県*/
    GUNNMA("群馬県", "10"),

    /**埼玉県*/
    SAITAMA("埼玉県", "11"),

    /**千葉県*/
    CHIBA("千葉県", "12"),

    /**東京都*/
    TOKYO("東京都", "13"),

    /**神奈川県*/
    KANAGAWA("神奈川県", "14"),

    /**新潟県*/
    NIIGATA("新潟県", "15"),

    /**富山県*/
    TOYAMA("富山県", "16"),

    /**石川県*/
    ISHIKAWA("石川県", "17"),

    /**福井県*/
    FUKUI("福井県", "18"),

    /**山梨県*/
    YAMANASHI("山梨県", "19"),

    /**長野県*/
    NAGANO("長野県", "20"),

    /**岐阜県*/
    GIFU("岐阜県", "21"),

    /**静岡県*/
    SIZUOKA("静岡県", "22"),

    /**愛知県*/
    AICHI("愛知県", "23"),

    /**三重県*/
    MIE("三重県", "24"),

    /**滋賀県*/
    SHIGA("滋賀県", "25"),

    /**京都府*/
    KYOTO("京都府", "26"),

    /**大阪府*/
    OOSAKA("大阪府", "27"),

    /**兵庫県*/
    HYOGO("兵庫県", "28"),

    /**奈良県*/
    NARA("奈良県", "29"),

    /**和歌山県*/
    WAKAYAMA("和歌山県", "30"),

    /**鳥取県*/
    TOTTORI("鳥取県", "31"),

    /**島根県*/
    SHIMANE("島根県", "32"),

    /**岡山県*/
    OKAYAMA("岡山県", "33"),

    /**広島県*/
    HIROSHIMA("広島県", "34"),

    /**山口県*/
    YAMAGUCHI("山口県", "35"),

    /**徳島県*/
    TOKUSHIMA("徳島県", "36"),

    /**香川県*/
    KAGAWA("香川県", "37"),

    /**愛媛県*/
    EHIME("愛媛県", "38"),

    /**高知県*/
    KOCHI("高知県", "39"),

    /**福岡県*/
    FUKUOKA("福岡県", "40"),

    /**佐賀県*/
    SAGA("佐賀県", "41"),

    /**長崎県*/
    NAGASAKI("長崎県", "42"),

    /**熊本県*/
    KUMAMOTO("熊本県", "43"),

    /**大分県*/
    OOITA("大分県", "44"),

    /**宮崎県*/
    MIYAZAKI("宮崎県", "45"),

    /**鹿児島県*/
    KAGOSHIMA("鹿児島県", "46"),

    /**沖縄県*/
    OKINAWA("沖縄県", "47"),

    /**国外*/
    FOREIGN("国外", "98"),

    /**定義なし*/
    NONE("定義なし", "99");

    /**
     * 全てのラベルを返す<br/>
     *
     * @return 全てのラベル
     */
    public static Set<String> getAllLabel() {

        Set<String> prefecture = new HashSet<>();

        prefecture.add(HTypePrefectureType.HOKKAIDO.getLabel());
        prefecture.add(HTypePrefectureType.AOMORI.getLabel());
        prefecture.add(HTypePrefectureType.IWATE.getLabel());
        prefecture.add(HTypePrefectureType.MIYAGI.getLabel());
        prefecture.add(HTypePrefectureType.AKITA.getLabel());
        prefecture.add(HTypePrefectureType.YAMAGATA.getLabel());
        prefecture.add(HTypePrefectureType.FUKUSHIMA.getLabel());
        prefecture.add(HTypePrefectureType.IBARAKI.getLabel());
        prefecture.add(HTypePrefectureType.TOCHIGI.getLabel());
        prefecture.add(HTypePrefectureType.GUNNMA.getLabel());
        prefecture.add(HTypePrefectureType.SAITAMA.getLabel());
        prefecture.add(HTypePrefectureType.CHIBA.getLabel());
        prefecture.add(HTypePrefectureType.TOKYO.getLabel());
        prefecture.add(HTypePrefectureType.KANAGAWA.getLabel());
        prefecture.add(HTypePrefectureType.NIIGATA.getLabel());
        prefecture.add(HTypePrefectureType.TOYAMA.getLabel());
        prefecture.add(HTypePrefectureType.ISHIKAWA.getLabel());
        prefecture.add(HTypePrefectureType.FUKUI.getLabel());
        prefecture.add(HTypePrefectureType.YAMANASHI.getLabel());
        prefecture.add(HTypePrefectureType.NAGANO.getLabel());
        prefecture.add(HTypePrefectureType.GIFU.getLabel());
        prefecture.add(HTypePrefectureType.SIZUOKA.getLabel());
        prefecture.add(HTypePrefectureType.AICHI.getLabel());
        prefecture.add(HTypePrefectureType.MIE.getLabel());
        prefecture.add(HTypePrefectureType.SHIGA.getLabel());
        prefecture.add(HTypePrefectureType.KYOTO.getLabel());
        prefecture.add(HTypePrefectureType.OOSAKA.getLabel());
        prefecture.add(HTypePrefectureType.HYOGO.getLabel());
        prefecture.add(HTypePrefectureType.NARA.getLabel());
        prefecture.add(HTypePrefectureType.WAKAYAMA.getLabel());
        prefecture.add(HTypePrefectureType.TOTTORI.getLabel());
        prefecture.add(HTypePrefectureType.SHIMANE.getLabel());
        prefecture.add(HTypePrefectureType.OKAYAMA.getLabel());
        prefecture.add(HTypePrefectureType.HIROSHIMA.getLabel());
        prefecture.add(HTypePrefectureType.YAMAGUCHI.getLabel());
        prefecture.add(HTypePrefectureType.TOKUSHIMA.getLabel());
        prefecture.add(HTypePrefectureType.KAGAWA.getLabel());
        prefecture.add(HTypePrefectureType.EHIME.getLabel());
        prefecture.add(HTypePrefectureType.KOCHI.getLabel());
        prefecture.add(HTypePrefectureType.FUKUOKA.getLabel());
        prefecture.add(HTypePrefectureType.SAGA.getLabel());
        prefecture.add(HTypePrefectureType.NAGASAKI.getLabel());
        prefecture.add(HTypePrefectureType.KUMAMOTO.getLabel());
        prefecture.add(HTypePrefectureType.OOITA.getLabel());
        prefecture.add(HTypePrefectureType.MIYAZAKI.getLabel());
        prefecture.add(HTypePrefectureType.KAGOSHIMA.getLabel());
        prefecture.add(HTypePrefectureType.OKINAWA.getLabel());
        prefecture.add(HTypePrefectureType.FOREIGN.getLabel());
        prefecture.add(HTypePrefectureType.NONE.getLabel());

        return prefecture;

    }

    /** doma用ファクトリメソッド */
    public static HTypePrefectureType of(String value) {

        HTypePrefectureType hType = EnumTypeUtil.getEnumFromValue(HTypePrefectureType.class, value);

        if (hType == null) {
            throw new IllegalArgumentException(value);
        } else {
            return hType;
        }
    }

    /**
     * ラベル<br/>
     */
    private String label;

    /**
     * 区分値<br/>
     */
    private String value;
}
