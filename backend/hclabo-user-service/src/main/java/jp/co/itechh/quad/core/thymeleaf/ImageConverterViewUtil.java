/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.core.thymeleaf;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.utility.GoodsUtility;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 画像用コンバータ<br/>
 * カスタムユーティリティオブジェク用Utility<br/>
 *
 * @author kimura
 */
public class ImageConverterViewUtil {

    /**
     * 画像表示用フォーマッタ
     *
     * @param goodsImageItems 画像リスト
     * @param num 画像番号
     * @param type 画像種別
     * @return 変換後
     */
    public String convert(final List<String> goodsImageItems, final int num, String type) {

        Environment environment = ApplicationContextUtility.getBean(Environment.class);
        String result = "";
        if (CollectionUtils.isEmpty(goodsImageItems)) {
            List<String> tmp = new ArrayList<String>();
            GoodsUtility goodsUtility = ApplicationContextUtility.getBean(GoodsUtility.class);
            tmp.add(goodsUtility.getGoodsImagePath(null));

            if (StringUtils.isEmpty(type)) {
                result = tmp.get(0);
            } else {
                result = environment.getProperty("images.path.goods.resize." + type) + tmp.get(0);
            }
        } else {

            if (StringUtils.isEmpty(type)) {
                result = goodsImageItems.get(num);
            } else {
                result = environment.getProperty("images.path.goods.resize." + type) + goodsImageItems.get(num);
            }
        }

        return result;
    }
}