/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.zipcode.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.constant.type.HTypeZipCodeType;
import jp.co.itechh.quad.core.dao.zipcode.ZipCodeDao;
import jp.co.itechh.quad.core.dto.shop.ZipCodeAddressDto;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.shop.zipcode.ZipCodeAddressGetLogic;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 郵便番号住所情報取得Logic実装<br/>
 *
 * @author negishi
 * @version $Revision: 1.3 $
 *
 */
@Component
public class ZipCodeAddressGetLogicImpl extends AbstractShopLogic implements ZipCodeAddressGetLogic {

    /** 郵便番号Dao */
    private final ZipCodeDao zipCodeDao;

    @Autowired
    public ZipCodeAddressGetLogicImpl(ZipCodeDao zipCodeDao) {
        this.zipCodeDao = zipCodeDao;
    }

    /**
     * 郵便番号住所情報取得処理<br/>
     * <ul>
     *  <li>入力チェック</li>
     *  <li>郵便番号整合性チェック</li>
     *  <li>郵便番号情報リスト取得</li>
     *  <li>住所情報作成</li>
     * </ul>
     *
     * @param zipCode 郵便番号
     * @return 郵便番号住所情報Dto
     */
    @Override
    public ZipCodeAddressDto execute(String zipCode) {

        // 入力チェック
        inputCheck(zipCode);

        // 郵便番号の整合性チェック
        zipCodeCheck(zipCode);

        // 郵便番号情報リスト取得
        List<ZipCodeAddressDto> addressList = getZipCodeEntityList(zipCode);

        // 住所情報補正
        // 事前に取得件数のチェックが行われているので、検索結果が 0件の場合、
        // この処理が実行されることはない。
        return correctAddress(zipCode, addressList);
    }

    /**
     * 郵便番号住所情報取得処理<br/>
     *
     * @param zipCode 郵便番号
     * @return 郵便番号に一致する住所を格納したList
     */
    @Override
    public List<ZipCodeAddressDto> getAddressList(String zipCode) {

        // 入力チェック
        inputCheck(zipCode);

        // 郵便番号の整合性チェック
        zipCodeCheck(zipCode);

        // 郵便番号情報リスト取得
        return getZipCodeEntityList(zipCode);
    }

    /**
     * 入力チェック<br/>
     *
     * @param zipCode 郵便番号
     */
    protected void inputCheck(String zipCode) {
        ArgumentCheckUtil.assertNotEmpty("zipCode", zipCode);
    }

    /**
     * 郵便番号の整合性チェック
     *
     * @param zipCode 郵便番号
     */
    protected void zipCodeCheck(String zipCode) {

        // 郵便番号不正
        if (!zipCode.matches(ZIPCODE_MATCHREGIX)) {
            throwMessage(MSGCD_ZIPCODE_FAIL);
        }
    }

    /**
     * 郵便番号情報リスト取得
     * 取得件数0件の場合 エラー
     *
     * @param zipCode 郵便番号
     * @return 郵便番号情報リスト
     */
    protected List<ZipCodeAddressDto> getZipCodeEntityList(String zipCode) {

        // 郵便番号検索
        List<ZipCodeAddressDto> addressList = zipCodeDao.getAddressList(zipCode);
        if (addressList.isEmpty()) {
            throwMessage(MSGCD_ZIPCODELIST_ZERO);
        }
        return addressList;
    }

    /**
     * 住所データの補正を行う<br/>
     * 住所データに対して以下の補正を行う。<br/>
     * １．複数の住所から共通部分を抽出する。<br/>
     * 例：以下の場合、大阪府大阪市福島区を住所として返却する。<br/>
     *  大阪府大阪市福島区吉野<br/>
     *  大阪府大阪市福島区海老江<br/>
     * <br/>
     * ２．以下に掲載にない場合という文言を消去する。<br/>
     * 住所の郵便番号の末尾が 00 の場合、町域名に設定されている「以下に掲載のない場合」を空文字に変換する<br/>
     *
     * @param zipCode 郵便番号
     * @param addressList 郵便番号情報リスト。null, emptyでないこと。
     * @return 郵便番号住所情報Dto
     */
    protected ZipCodeAddressDto correctAddress(String zipCode, List<ZipCodeAddressDto> addressList) {
        // 複数取得できた住所情報の1件目と2件目以降を比較し、住所の差異を探す。
        // 差異が見つかった場合、その項目も含め、下位の項目を空白でクリアする。
        ZipCodeAddressDto resultDto = addressList.get(0);

        // 住所の郵便番号の末尾が 00 の場合、町域名をクリアする。
        // 町域名には町域ではなく、「以下に掲載のない場合」という文言が入っているため。
        if (HTypeZipCodeType.NORMAL.getValue().equals(resultDto.getZipCodeType()) && zipCode.endsWith(
                        ZIPCODE_LAST_DIGIT)) {
            resultDto.setTownName("");
            resultDto.setTownNameKana("");
            // 住所の郵便番号には丁目はないのでクリア不要
        }

        for (int i = 1; i < addressList.size(); i++) {
            ZipCodeAddressDto addressDto = addressList.get(i);

            // 都道府県名が一致しない場合、すべての情報をクリア
            if (StringUtils.isNotEmpty(resultDto.getPrefectureName()) && !resultDto.getPrefectureName()
                                                                                   .equals(addressDto.getPrefectureName())) {
                resultDto.setPrefectureName("");
                resultDto.setPrefectureNameKana("");
                resultDto.setCityName("");
                resultDto.setCityNameKana("");
                resultDto.setTownName("");
                resultDto.setTownNameKana("");
                resultDto.setNumbers("");
                break;
            }

            // 市区町村が一致しない場合、市区町村、町域、丁目をクリア
            if (StringUtils.isNotEmpty(resultDto.getCityName()) && !resultDto.getCityName()
                                                                             .equals(addressDto.getCityName())) {
                resultDto.setCityName("");
                resultDto.setCityNameKana("");
                resultDto.setTownName("");
                resultDto.setTownNameKana("");
                resultDto.setNumbers("");
                break;
            }

            // 町域が一致しない場合、町域、丁目をクリア
            if (StringUtils.isNotEmpty(resultDto.getTownName()) && !resultDto.getTownName()
                                                                             .equals(addressDto.getTownName())) {
                resultDto.setTownName("");
                resultDto.setTownNameKana("");
                resultDto.setNumbers("");
                break;
            }

            // 丁目が一致しない場合、丁目をクリア
            if (StringUtils.isNotEmpty(resultDto.getNumbers()) && !resultDto.getNumbers()
                                                                            .equals(addressDto.getNumbers())) {
                resultDto.setNumbers("");
                break;
            }
        }

        return resultDto;

    }
}