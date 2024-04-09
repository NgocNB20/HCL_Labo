package jp.co.itechh.quad.mailcertification.presentation.api;

import jp.co.itechh.quad.core.entity.memberinfo.MemberInfoEntity;
import jp.co.itechh.quad.core.entity.memberinfo.confirmmail.ConfirmMailEntity;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.mail_certification.presentation.api.param.ConfirmMailResponse;
import jp.co.itechh.quad.mail_certification.presentation.api.param.CustomerResponse;
import org.springframework.stereotype.Component;

@Component
public class MailCertificationHelper {

    /**
     * レスポンスに変換
     *
     * @param memberInfoEntity メンバー情報エンティティ
     * @return 会員レスポンス
     */
    public CustomerResponse toCustomerResponse(MemberInfoEntity memberInfoEntity) {

        CustomerResponse customerResponse = new CustomerResponse();
        customerResponse.setMemberInfoSeq(memberInfoEntity.getMemberInfoSeq());
        customerResponse.setMemberInfoStatus(EnumTypeUtil.getValue(memberInfoEntity.getMemberInfoStatus()));
        customerResponse.setMemberInfoUniqueId(memberInfoEntity.getMemberInfoUniqueId());
        customerResponse.setMemberInfoId(memberInfoEntity.getMemberInfoId());
        customerResponse.setMemberInfoPassword(memberInfoEntity.getMemberInfoPassword());
        customerResponse.setMemberInfoLastName(memberInfoEntity.getMemberInfoLastName());
        customerResponse.setMemberInfoFirstName(memberInfoEntity.getMemberInfoFirstName());
        customerResponse.setMemberInfoLastKana(memberInfoEntity.getMemberInfoLastKana());
        customerResponse.setMemberInfoFirstKana(memberInfoEntity.getMemberInfoFirstKana());
        customerResponse.setMemberInfoSex(EnumTypeUtil.getValue(memberInfoEntity.getMemberInfoSex()));
        customerResponse.setMemberInfoBirthday(memberInfoEntity.getMemberInfoBirthday());
        customerResponse.setMemberInfoTel(memberInfoEntity.getMemberInfoTel());
        customerResponse.setMemberInfoAddressId(memberInfoEntity.getMemberInfoAddressId());
        customerResponse.setMemberInfoMail(memberInfoEntity.getMemberInfoMail());
        customerResponse.setAccessUid(memberInfoEntity.getAccessUid());
        customerResponse.setVersionNo(memberInfoEntity.getVersionNo());
        customerResponse.setAdmissionYmd(memberInfoEntity.getAdmissionYmd());
        customerResponse.setSecessionYmd(memberInfoEntity.getSecessionYmd());
        customerResponse.setMemo(memberInfoEntity.getMemo());
        customerResponse.setLastLoginTime(memberInfoEntity.getLastLoginTime());
        customerResponse.setLastLoginUserAgent(memberInfoEntity.getLastLoginUserAgent());
        customerResponse.setRegistTime(memberInfoEntity.getRegistTime());
        customerResponse.setUpdateTime(memberInfoEntity.getUpdateTime());

        return customerResponse;
    }

    /**
     * レスポンスに変換
     *
     * @param confirmMailEntity メールエンティティを確認する
     * @return 確認メールレスポンス
     */
    public ConfirmMailResponse toConfirmMailResponse(ConfirmMailEntity confirmMailEntity) {

        if (confirmMailEntity == null) {
            return null;
        }
        ConfirmMailResponse confirmMailResponse = new ConfirmMailResponse();

        confirmMailResponse.setConfirmMailSeq(confirmMailEntity.getConfirmMailSeq());
        confirmMailResponse.setMemberInfoSeq(confirmMailEntity.getMemberInfoSeq());
        confirmMailResponse.setOrderSeq(confirmMailEntity.getOrderSeq());
        confirmMailResponse.setConfirmMail(confirmMailEntity.getConfirmMail());
        confirmMailResponse.setConfirmMailType(EnumTypeUtil.getValue(confirmMailEntity.getConfirmMailType()));
        confirmMailResponse.setConfirmMailPassword(confirmMailEntity.getConfirmMailPassword());
        confirmMailResponse.setEffectiveTime(confirmMailEntity.getEffectiveTime());
        confirmMailResponse.setRegistTime(confirmMailEntity.getRegistTime());
        confirmMailResponse.setUpdateTime(confirmMailEntity.getUpdateTime());

        return confirmMailResponse;
    }

}