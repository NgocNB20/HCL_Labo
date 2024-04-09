package jp.co.itechh.quad.front.logic.common.impl;

import jp.co.itechh.quad.front.dto.shop.ZipCodeAddressDto;
import jp.co.itechh.quad.zipcode.presentation.api.param.ZipCodeAddressResponse;
import org.springframework.stereotype.Component;

@Component
public class ZipCodeAddressImpl {

    /**
     * 郵便番号住所情報Dtoに変換
     *
     * @param zipCodeAddressResponse 郵便番号住所情報レスポンス
     * @return 郵便番号住所情報Dto
     */
    public ZipCodeAddressDto toZipCodeAddressDto(ZipCodeAddressResponse zipCodeAddressResponse) {
        ZipCodeAddressDto zipCodeAddressDto = new ZipCodeAddressDto();

        zipCodeAddressResponse.getListZipCodeAddress().forEach(zipCodeResponse -> {
            zipCodeAddressDto.setPrefectureName(zipCodeResponse.getPrefectureName());
            zipCodeAddressDto.setPrefectureNameKana(zipCodeResponse.getPrefectureNameKana());
            zipCodeAddressDto.setCityName(zipCodeResponse.getCityName());
            zipCodeAddressDto.setCityNameKana(zipCodeResponse.getCityNameKana());
            zipCodeAddressDto.setTownName(zipCodeResponse.getTownName());
            zipCodeAddressDto.setTownNameKana(zipCodeResponse.getTownNameKana());
            zipCodeAddressDto.setNumbers(zipCodeResponse.getNumbers());
            zipCodeAddressDto.setZipCodeType(zipCodeResponse.getZipCodeType());
        });

        return zipCodeAddressDto;
    }
}
