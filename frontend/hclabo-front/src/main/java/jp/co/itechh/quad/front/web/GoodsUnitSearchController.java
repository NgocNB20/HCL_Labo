/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.web;

import jp.co.itechh.quad.front.base.util.seasar.StringUtil;
import jp.co.itechh.quad.front.base.utility.ConversionUtility;
import jp.co.itechh.quad.front.base.utility.DateUtility;
import jp.co.itechh.quad.product.presentation.api.ProductApi;
import jp.co.itechh.quad.product.presentation.api.param.PageInfoRequest;
import jp.co.itechh.quad.product.presentation.api.param.StandardListRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 規格２プルダウン取得コントローラー<br/>
 * 規格１プルダウンの選択値をもとに、紐づく規格２を取得し、プルダウンに設定する
 *
 * @author kn23834
 * @version $Revision: 1.0 $
 */
@RestController
@RequestMapping("/")
public class GoodsUnitSearchController extends AbstractController {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(GoodsUnitSearchController.class);

    /** マップに格納するプルダウンラベルのkey名 */
    public static final String KEY_OF_LABEL = "label";

    /** マップに格納するプルダウン値のkey名 */
    public static final String KEY_OF_VALUE = "value";

    /** 商品API */
    private final ProductApi productApi;

    /** 日付関連Utilityクラス */
    private final DateUtility dateUtility;

    /** 変換Helper */
    private final ConversionUtility conversionUtility;

    @Autowired
    public GoodsUnitSearchController(ProductApi productApi,
                                     DateUtility dateUtility,
                                     ConversionUtility conversionUtility) {
        this.productApi = productApi;
        this.dateUtility = dateUtility;
        this.conversionUtility = conversionUtility;
    }

    /**
     * 規格値2を取得する
     *
     * @param unitGgcd 商品グループコード
     * @param unit1Gcd 選択した規格値1の商品コード
     * @param preTime  プレビュー日時（yyyyMMddHHmmss）
     * @return 規格値2(JSON)
     */
    @GetMapping("/searchGoodsUnit2")
    @ResponseBody
    public Map<String, String> searchGoodsUnit2(@RequestParam(name = "unitGgcd", required = false) String unitGgcd,
                                                @RequestParam(name = "unit1Gcd", required = false) String unit1Gcd,
                                                @RequestParam(name = "preTime", required = false) String preTime) {

        Map<String, String> unit2Map = new LinkedHashMap<>();
        PageInfoRequest pageInfoRequest = new PageInfoRequest();
        StandardListRequest request = new StandardListRequest();
        request.setFrontDisplayReferenceDate(this.conversionUtility.toDate(
                        this.dateUtility.toTimestampValue(preTime, this.dateUtility.YMD_HMS)));

        if (StringUtil.isNotEmpty(unit1Gcd)) {
            try {
                unit2Map = productApi.getItemsStandards2(unitGgcd, unit1Gcd, request, pageInfoRequest)
                                     .getStandardValue2Map();
            } catch (HttpClientErrorException | HttpServerErrorException e) {
                LOGGER.error("例外処理が発生しました", e);
                unit2Map = null;
            }
        }
        return unit2Map;
    }

}