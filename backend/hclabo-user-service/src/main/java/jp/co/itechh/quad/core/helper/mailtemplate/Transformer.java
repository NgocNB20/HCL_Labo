/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.helper.mailtemplate;

import java.util.Map;

/**
 * メールテンプレート用変換クラス<br />
 * DTO からメールテンプレート用の値マップを生成するヘルパクラスが実装するI/F
 *
 * @author tm27400
 * @author natsume
 */
public interface Transformer {

    /** 配列要素区切り文字 */
    String LOOP_STR = "_";

    /**
     * リソースファイル名の指定（ダミーMap）<br/>
     * xxxx.properties と紐づく。
     *
     * @return リソースファイル
     */
    String getResourceName();

    /**
     * メール送信に使用する値マップを作成する。<br />
     * 引数の数が合わなかったり、型が違うなど変換不能な場合は Collections.EMPTY_MAP を返す
     *
     * @param arguments 引数
     * @return 値マップ
     */
    Map<String, String> toValueMap(Object... arguments);

    /**
     * プレースホルダ名一覧を取得する
     *
     * @return プレースホルダ名一覧
     */
    Map<String, String> getDummyPlaceholderMap();

}