package jp.co.itechh.quad.temp.presentation.api;

import jp.co.itechh.quad.core.dto.memberinfo.TempMemberInfoDto;
import jp.co.itechh.quad.core.entity.memberinfo.MemberInfoEntity;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.temp.presentation.api.param.MemberInforResponse;
import jp.co.itechh.quad.temp.presentation.api.param.TempCustomerResponse;
import org.springframework.stereotype.Component;

/**
 * 仮会員 Helper
 *
 * @author PHAM QUANG DIEU (VJP)
 *
 */
@Component
public class TempHelper {

    /**
     * レスポンスに変換
     *
     * @param tempMemberInfoDto 仮会員登録Dto
     * @return 仮会員レスポンス
     */
    public TempCustomerResponse toTempCustomerResponse(TempMemberInfoDto tempMemberInfoDto) {

        TempCustomerResponse tempCustomerResponse = new TempCustomerResponse();

        tempCustomerResponse.setOrderSeq(tempMemberInfoDto.getOrderSeq());
        tempCustomerResponse.setMemberInfo(toMemberInforResponse(tempMemberInfoDto.getMemberInfoEntity()));

        return tempCustomerResponse;
    }

    /**
     * レスポンスに変換
     *
     * @param memberInfoEntity 会員情報
     * @return 会員情報レスポンス
     */
    private MemberInforResponse toMemberInforResponse(MemberInfoEntity memberInfoEntity) {

        MemberInforResponse memberInforResponse = new MemberInforResponse();

        memberInforResponse.setMemberInfoSeq(memberInfoEntity.getMemberInfoSeq());
        memberInforResponse.setMemberInfoStatus(EnumTypeUtil.getValue(memberInfoEntity.getMemberInfoStatus()));
        memberInforResponse.setMemberInfoUniqueId(memberInfoEntity.getMemberInfoUniqueId());
        memberInforResponse.setMemberInfoId(memberInfoEntity.getMemberInfoId());
        memberInforResponse.setMemberInfoPassword(memberInfoEntity.getMemberInfoPassword());
        memberInforResponse.setMemberInfoLastName(memberInfoEntity.getMemberInfoLastName());
        memberInforResponse.setMemberInfoFirstName(memberInfoEntity.getMemberInfoFirstName());
        memberInforResponse.setMemberInfoLastKana(memberInfoEntity.getMemberInfoLastKana());
        memberInforResponse.setMemberInfoFirstKana(memberInfoEntity.getMemberInfoFirstKana());
        memberInforResponse.setMemberInfoSex(EnumTypeUtil.getValue(memberInfoEntity.getMemberInfoSex()));
        memberInforResponse.setMemberInfoBirthday(memberInfoEntity.getMemberInfoBirthday());
        memberInforResponse.setMemberInfoTel(memberInfoEntity.getMemberInfoTel());
        memberInforResponse.setMemberInfoAddressId(memberInfoEntity.getMemberInfoAddressId());
        memberInforResponse.setMemberInfoMail(memberInfoEntity.getMemberInfoMail());
        memberInforResponse.setAccessUid(memberInfoEntity.getAccessUid());
        memberInforResponse.setVersionNo(memberInfoEntity.getVersionNo());
        memberInforResponse.setAdmissionYmd(memberInfoEntity.getAdmissionYmd());
        memberInforResponse.setSecessionYmd(memberInfoEntity.getSecessionYmd());
        memberInforResponse.setMemo(memberInfoEntity.getMemo());
        memberInforResponse.setLastLoginTime(memberInfoEntity.getLastLoginTime());
        memberInforResponse.setLastLoginUserAgent(memberInfoEntity.getLastLoginUserAgent());
        memberInforResponse.setRegistTime(memberInfoEntity.getRegistTime());
        memberInforResponse.setUpdateTime(memberInfoEntity.getUpdateTime());

        return memberInforResponse;
    }
}
