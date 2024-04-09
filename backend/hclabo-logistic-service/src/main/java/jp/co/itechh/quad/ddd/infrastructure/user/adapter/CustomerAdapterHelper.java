package jp.co.itechh.quad.ddd.infrastructure.user.adapter;

import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.constant.type.HTypeMemberInfoStatus;
import jp.co.itechh.quad.core.constant.type.HTypeSexUnnecessaryAnswer;
import jp.co.itechh.quad.core.dto.memberinfo.MemberInfoDetailsDto;
import jp.co.itechh.quad.core.entity.memberinfo.MemberInfoEntity;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 * 顧客アダプタHelperクラス
 */
@Component
public class CustomerAdapterHelper {

    /**
     * 変換ユーティリティクラス
     */
    private final ConversionUtility conversionUtility;

    /**
     * コンストラクタ
     *
     * @param conversionUtility 変換ユーティリティクラス
     */
    @Autowired
    public CustomerAdapterHelper(ConversionUtility conversionUtility) {
        this.conversionUtility = conversionUtility;
    }

    /**
     * 会員詳細Dtoクラスに変換
     *
     * @param response 会員レスポンス
     * @return 会員詳細Dtoクラス
     */
    public MemberInfoDetailsDto toMemberInfoDetailsDto(CustomerResponse response) {

        if (ObjectUtils.isEmpty(response)) {
            return null;
        }

        MemberInfoDetailsDto memberInfoDetailsDto = new MemberInfoDetailsDto();

        MemberInfoEntity memberInfoEntity = new MemberInfoEntity();

        memberInfoEntity.setMemberInfoSeq(response.getMemberInfoSeq());
        memberInfoEntity.setMemberInfoStatus(
                        EnumTypeUtil.getEnumFromValue(HTypeMemberInfoStatus.class, response.getMemberInfoStatus()));
        memberInfoEntity.setMemberInfoUniqueId(response.getMemberInfoUniqueId());
        memberInfoEntity.setMemberInfoId(response.getMemberInfoId());
        memberInfoEntity.setMemberInfoPassword(response.getMemberInfoPassword());
        memberInfoEntity.setMemberInfoLastName(response.getMemberInfoLastName());
        memberInfoEntity.setMemberInfoFirstName(response.getMemberInfoFirstName());
        memberInfoEntity.setMemberInfoLastKana(response.getMemberInfoLastKana());
        memberInfoEntity.setMemberInfoFirstKana(response.getMemberInfoFirstKana());
        memberInfoEntity.setMemberInfoSex(
                        EnumTypeUtil.getEnumFromValue(HTypeSexUnnecessaryAnswer.class, response.getMemberInfoSex()));
        memberInfoEntity.setMemberInfoBirthday(conversionUtility.toTimestamp(response.getMemberInfoBirthday()));
        memberInfoEntity.setMemberInfoTel(response.getMemberInfoTel());
        memberInfoEntity.setMemberInfoMail(response.getMemberInfoMail());
        memberInfoEntity.setMemberInfoAddressId(response.getMemberInfoAddressId());
        memberInfoEntity.setShopSeq(1001);
        memberInfoEntity.setAccessUid(response.getAccessUid());
        memberInfoEntity.setVersionNo(response.getVersionNo());
        memberInfoEntity.setAdmissionYmd(response.getAdmissionYmd());
        memberInfoEntity.setSecessionYmd(response.getSecessionYmd());
        memberInfoEntity.setMemo(response.getMemo());
        memberInfoEntity.setLastLoginTime(conversionUtility.toTimestamp(response.getLastLoginTime()));
        memberInfoEntity.setLastLoginUserAgent(response.getLastLoginUserAgent());
        memberInfoEntity.setRegistTime(conversionUtility.toTimestamp(response.getRegistTime()));
        memberInfoEntity.setUpdateTime(conversionUtility.toTimestamp(response.getUpdateTime()));

        memberInfoDetailsDto.setMemberInfoEntity(memberInfoEntity);

        return memberInfoDetailsDto;
    }
}