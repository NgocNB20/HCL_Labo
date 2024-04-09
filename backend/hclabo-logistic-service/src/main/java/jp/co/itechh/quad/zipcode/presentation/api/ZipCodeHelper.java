package jp.co.itechh.quad.zipcode.presentation.api;

import jp.co.itechh.quad.core.dto.shop.ZipCodeAddressDto;
import jp.co.itechh.quad.zipcode.presentation.api.param.ZipCodeAddressResponse;
import jp.co.itechh.quad.zipcode.presentation.api.param.ZipCodeResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 郵便番号自動更新　Helper
 *
 * @author PHAM QUANG DIEU (VJP)
 */
@Component
public class ZipCodeHelper {

    /**
     * 郵便番号住所情報レスポンスに変換
     *
     * @param list 郵便番号に一致する住所を格納したList
     * @return 郵便番号住所情報レスポンス
     */
    public ZipCodeAddressResponse toZipCodeAddressResponse(List<ZipCodeAddressDto> list) {
        ZipCodeAddressResponse zipCodeAddressResponse = new ZipCodeAddressResponse();
        List<ZipCodeResponse> zipCodeResponseList = new ArrayList<>();
        list.forEach(zipCodeAddressDto -> {
            ZipCodeResponse zipCodeResponse = new ZipCodeResponse();
            zipCodeResponse.setPrefectureName(zipCodeAddressDto.getPrefectureName());
            zipCodeResponse.setPrefectureNameKana(zipCodeAddressDto.getPrefectureNameKana());
            zipCodeResponse.setCityName(zipCodeAddressDto.getCityName());
            zipCodeResponse.setCityNameKana(zipCodeAddressDto.getCityNameKana());
            zipCodeResponse.setTownName(zipCodeAddressDto.getTownName());
            zipCodeResponse.setTownNameKana(zipCodeAddressDto.getTownNameKana());
            zipCodeResponse.setNumbers(zipCodeAddressDto.getNumbers());
            zipCodeResponse.setZipCodeType(zipCodeAddressDto.getZipCodeType());

            zipCodeResponseList.add(zipCodeResponse);
        });
        zipCodeAddressResponse.setListZipCodeAddress(zipCodeResponseList);

        return zipCodeAddressResponse;
    }
}
