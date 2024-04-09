package jp.co.itechh.quad.ddd.infrastructure.user.adapter;

import jp.co.itechh.quad.administrator.presentation.api.param.AdministratorResponse;
import jp.co.itechh.quad.ddd.domain.user.adapter.model.AdministratorDto;
import org.springframework.stereotype.Component;

/**
 * 運営者アダプターHelperクラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 *
 */
@Component
public class AdministratorAdapterHelper {

    /**
     * 運営者レスポンスに変換.
     *
     * @param administratorResponse 運営者レスポンス
     * @return 運営者レスポンス
     */
    public AdministratorDto toAdministratorDto(AdministratorResponse administratorResponse) {

        if (administratorResponse == null) {
            return null;
        }

        AdministratorDto administratorDto = new AdministratorDto();

        administratorDto.setAdministratorSeq(administratorResponse.getAdministratorSeq());
        administratorDto.setAdministratorStatus(administratorResponse.getAdministratorStatus());
        administratorDto.setAdministratorId(administratorResponse.getAdministratorId());
        administratorDto.setAdministratorPassword(administratorResponse.getAdministratorPassword());
        administratorDto.setMail(administratorResponse.getMail());
        administratorDto.setAdministratorLastName(administratorResponse.getAdministratorLastName());
        administratorDto.setAdministratorFirstName(administratorResponse.getAdministratorFirstName());
        administratorDto.setAdministratorLastKana(administratorResponse.getAdministratorLastKana());
        administratorDto.setAdministratorFirstKana(administratorResponse.getAdministratorFirstKana());
        administratorDto.setUseStartDate(administratorResponse.getUseStartDate());
        administratorDto.setUseEndDate(administratorResponse.getUseEndDate());
        administratorDto.setAdminAuthGroupSeq(administratorResponse.getAdminAuthGroupSeq());

        return administratorDto;
    }
}
