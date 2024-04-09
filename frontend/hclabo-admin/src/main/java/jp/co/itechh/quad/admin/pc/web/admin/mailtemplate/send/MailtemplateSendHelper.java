package jp.co.itechh.quad.admin.pc.web.admin.mailtemplate.send;

import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.constant.TransmissionFormatForMagazine;
import jp.co.itechh.quad.admin.constant.type.HTypeAdministratorStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeMailTemplateDefaultFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeMailTemplateType;
import jp.co.itechh.quad.admin.constant.type.HTypePasswordNeedChangeFlag;
import jp.co.itechh.quad.admin.constant.type.HTypePasswordSHA256EncryptedFlag;
import jp.co.itechh.quad.admin.dto.shop.mail.MailTemplateIndexDto;
import jp.co.itechh.quad.admin.entity.administrator.AdminAuthGroupDetailEntity;
import jp.co.itechh.quad.admin.entity.administrator.AdminAuthGroupEntity;
import jp.co.itechh.quad.admin.entity.administrator.AdministratorEntity;
import jp.co.itechh.quad.admin.entity.shop.mail.MailTemplateEntity;
import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import jp.co.itechh.quad.administrator.presentation.api.param.AdminAuthGroupDetailResponse;
import jp.co.itechh.quad.administrator.presentation.api.param.AdminAuthGroupResponse;
import jp.co.itechh.quad.administrator.presentation.api.param.AdministratorResponse;
import jp.co.itechh.quad.notificationsub.presentation.api.param.MailListGetRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.MailListResponse;
import jp.co.itechh.quad.notificationsub.presentation.api.param.MailResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class MailtemplateSendHelper {

    /**
     * 変換ユーティリティクラス
     */
    private final ConversionUtility conversionUtility;

    /**
     * コントローラー
     *
     * @param conversionUtility 変換ユーティリティクラス
     */
    @Autowired
    public MailtemplateSendHelper(ConversionUtility conversionUtility) {
        this.conversionUtility = conversionUtility;
    }

    /**
     * 画面表示用 PageItem を作成する
     *
     * @param dtoList   元ねた
     * @param mailtemplateSendModel セットするページ
     */
    public void toPageSelect(List<MailTemplateIndexDto> dtoList, MailtemplateSendModel mailtemplateSendModel) {

        List<MailtemplateSelectItem> itemList = new ArrayList<>();
        mailtemplateSendModel.setIndexItems(itemList);

        Map<HTypeMailTemplateType, Integer> memberCountMap = new HashMap<>();
        for (MailTemplateIndexDto dto : dtoList) {
            Integer count = memberCountMap.get(dto.getMailTemplateType());
            if (count == null) {
                count = 0;
            }
            count = count + 1;
            memberCountMap.put(dto.getMailTemplateType(), count);
        }

        Set<HTypeMailTemplateType> foundSet = new HashSet<>();

        for (MailTemplateIndexDto dto : dtoList) {
            MailtemplateSelectItem item = ApplicationContextUtility.getBean(MailtemplateSelectItem.class);
            item.setMailTemplateDefaultFlag(dto.getMailTemplateDefaultFlag() == HTypeMailTemplateDefaultFlag.ON);
            item.setMailTemplateName(dto.getMailTemplateName());
            item.setMailTemplateSeq(dto.getMailTemplateSeq());
            item.setMailTemplateTypeName(dto.getMailTemplateType().getLabel());
            item.setMailTemplateType(dto.getMailTemplateType().getValue());
            item.setMailTemplateTypeRowRowspan(memberCountMap.get(dto.getMailTemplateType()));

            // テンプレート内先頭のレコードかどうか
            if (!foundSet.contains(dto.getMailTemplateType())) {
                item.setNewTemplateType(true);
                foundSet.add(dto.getMailTemplateType());
            } else {
                item.setNewTemplateType(false);
            }

            // テンプレートがまだ登録されていない種別かどうか
            if (dto.getMailTemplateSeq() == -1) {
                item.setEmptyTemplate(true);
            }

            itemList.add(item);

        }
    }

    /**
     * ロード時のＤＴＯ→ページ設定
     *
     * @param entity MailTemplateEntity
     * @param model MailtemplateEditModel
     */
    public void toPageForLoad(MailTemplateEntity entity, MailtemplateSendModel model) {

        model.setShowingPreview(false);

        if (entity != null) {
            // 既に登録済みのテンプレート
            model.setMailTemplateType(entity.getMailTemplateType().getValue());
            model.setMailTemplateTypeName(entity.getMailTemplateType().getLabel());

            // テンプレート名
            model.setMailTemplateName(entity.getMailTemplateName());

            // 送信者&BCC
            model.setFromAddress(entity.getMailTemplateFromAddress());
            model.setBccAddress(entity.getMailTemplateBccAddress());

            model.setMailTitle(entity.getMailTemplateSubject());
        }

        model.setEditingVersion(TransmissionFormatForMagazine.INT_PC_HTML);
        model.setLastEditingVersion(TransmissionFormatForMagazine.INT_PC_HTML);

    }

    /**
     * 運営者エンティティに反映
     *
     * @param administratorResponse 運営者レスポンス
     * @return 運営者エンティティ
     */
    public AdministratorEntity toAdministratorEntityFromAdministratorResponse(AdministratorResponse administratorResponse) {

        if (ObjectUtils.isEmpty(administratorResponse)) {
            return null;
        }

        AdministratorEntity administratorEntity = new AdministratorEntity();

        administratorEntity.setAdministratorSeq(administratorResponse.getAdministratorSeq());
        administratorEntity.setAdministratorStatus(EnumTypeUtil.getEnumFromValue(HTypeAdministratorStatus.class,
                                                                                 administratorResponse.getAdministratorStatus()
                                                                                ));
        administratorEntity.setAdministratorId(administratorResponse.getAdministratorId());
        administratorEntity.setAdministratorPassword(administratorResponse.getAdministratorPassword());
        administratorEntity.setMail(administratorResponse.getMail());
        administratorEntity.setAdministratorLastName(administratorResponse.getAdministratorLastName());
        administratorEntity.setAdministratorFirstName(administratorResponse.getAdministratorFirstName());
        administratorEntity.setAdministratorFirstKana(administratorResponse.getAdministratorFirstKana());
        administratorEntity.setAdministratorLastKana(administratorResponse.getAdministratorLastKana());
        administratorEntity.setUseStartDate(conversionUtility.toTimestamp(administratorResponse.getUseStartDate()));
        administratorEntity.setUseEndDate(conversionUtility.toTimestamp(administratorResponse.getUseEndDate()));
        administratorEntity.setAdminAuthGroupSeq(administratorResponse.getAdminAuthGroupSeq());
        administratorEntity.setRegistTime(conversionUtility.toTimestamp(administratorResponse.getRegistTime()));
        administratorEntity.setUpdateTime(conversionUtility.toTimestamp(administratorResponse.getUpdateTime()));
        administratorEntity.setPasswordChangeTime(
                        conversionUtility.toTimestamp(administratorResponse.getPasswordChangeTime()));
        administratorEntity.setPasswordExpiryDate(
                        conversionUtility.toTimestamp(administratorResponse.getPasswordExpiryDate()));
        administratorEntity.setLoginFailureCount(administratorResponse.getLoginFailureCount());
        administratorEntity.setAccountLockTime(
                        conversionUtility.toTimestamp(administratorResponse.getAccountLockTime()));
        administratorEntity.setPasswordNeedChangeFlag(EnumTypeUtil.getEnumFromValue(HTypePasswordNeedChangeFlag.class,
                                                                                    administratorResponse.getPasswordNeedChangeFlag()
                                                                                   ));
        administratorEntity.setPasswordSHA256EncryptedFlag(
                        EnumTypeUtil.getEnumFromValue(HTypePasswordSHA256EncryptedFlag.class,
                                                      administratorResponse.getPasswordSHA256EncryptedFlag()
                                                     ));
        administratorEntity.setAdminAuthGroup(
                        toAdminAuthGroupEntityFromAdminAuthGroupResponse(administratorResponse.getAdminAuthGroup()));

        return administratorEntity;
    }

    /**
     * 権限グループエンティティに反映
     *
     * @param adminAuthGroupResponse AdminAuthGroupResponse
     * @return 権限グループ
     */
    private AdminAuthGroupEntity toAdminAuthGroupEntityFromAdminAuthGroupResponse(AdminAuthGroupResponse adminAuthGroupResponse) {

        if (adminAuthGroupResponse == null) {
            return null;
        }

        AdminAuthGroupEntity adminAuthGroupEntity = new AdminAuthGroupEntity();

        adminAuthGroupEntity.setAdminAuthGroupSeq(adminAuthGroupResponse.getAdminAuthGroupSeq());
        adminAuthGroupEntity.setAuthGroupDisplayName(adminAuthGroupResponse.getAuthGroupDisplayName());
        adminAuthGroupEntity.setRegistTime(conversionUtility.toTimestamp(adminAuthGroupResponse.getRegistTime()));
        adminAuthGroupEntity.setUpdateTime(conversionUtility.toTimestamp(adminAuthGroupResponse.getUpdateTime()));
        adminAuthGroupEntity.setAdminAuthGroupDetailList(toAdminAuthGroupDetailListFromAdminAuthGroupDetailResponseList(
                        adminAuthGroupResponse.getAdminAuthGroupDetailList()));
        adminAuthGroupEntity.setUnmodifiableGroup(adminAuthGroupResponse.getUnmodifiableGroup());

        return adminAuthGroupEntity;
    }

    /**
     * 運営者権限グループ詳細エンティティリストに反映
     *
     * @param adminAuthGroupDetailList AdminAuthGroupDetailResponseList
     * @return 運営者権限グループ詳細エンティティリスト
     */
    private List<AdminAuthGroupDetailEntity> toAdminAuthGroupDetailListFromAdminAuthGroupDetailResponseList(List<AdminAuthGroupDetailResponse> adminAuthGroupDetailList) {

        if (adminAuthGroupDetailList == null) {
            return null;
        }

        List<AdminAuthGroupDetailEntity> adminAuthGroupDetailEntities = new ArrayList<>();

        for (AdminAuthGroupDetailResponse adminAuthGroupDetailResponse : adminAuthGroupDetailList) {

            AdminAuthGroupDetailEntity adminAuthGroupDetailEntity = new AdminAuthGroupDetailEntity();

            adminAuthGroupDetailEntity.setAdminAuthGroupSeq(adminAuthGroupDetailResponse.getAdminAuthGroupSeq());
            adminAuthGroupDetailEntity.setAuthTypeCode(adminAuthGroupDetailResponse.getAuthTypeCode());
            adminAuthGroupDetailEntity.setAuthLevel(adminAuthGroupDetailResponse.getAuthLevel());
            adminAuthGroupDetailEntity.setRegistTime(
                            conversionUtility.toTimestamp(adminAuthGroupDetailResponse.getRegistTime()));
            adminAuthGroupDetailEntity.setUpdateTime(
                            conversionUtility.toTimestamp(adminAuthGroupDetailResponse.getUpdateTime()));

            adminAuthGroupDetailEntities.add(adminAuthGroupDetailEntity);

        }

        return adminAuthGroupDetailEntities;
    }

    /**
     * レスポンスに変換
     *
     * @param mailResponse メール情報レスポンス
     * @return メールテンプレートクラス
     */
    public MailTemplateEntity toMailTemplateEntity(MailResponse mailResponse) {

        MailTemplateEntity mailTemplateEntity = new MailTemplateEntity();

        if (mailTemplateEntity != null) {
            mailTemplateEntity.setMailTemplateSeq(mailResponse.getMailTemplateSeq());
            mailTemplateEntity.setMailTemplateName(mailResponse.getMailTemplateName());
            mailTemplateEntity.setMailTemplateType(EnumTypeUtil.getEnumFromValue(HTypeMailTemplateType.class,
                                                                                 mailResponse.getMailTemplateType()
                                                                                ));
            mailTemplateEntity.setMailTemplateDefaultFlag(
                            EnumTypeUtil.getEnumFromValue(HTypeMailTemplateDefaultFlag.class,
                                                          mailResponse.getMailTemplateDefaultFlag()
                                                         ));
            mailTemplateEntity.setMailTemplateSubject(mailResponse.getMailTemplateSubject());
            mailTemplateEntity.setMailTemplateFromAddress(mailResponse.getMailTemplateFromAddress());
            mailTemplateEntity.setMailTemplateToAddress(mailResponse.getMailTemplateToAddress());
            mailTemplateEntity.setMailTemplateCcAddress(mailResponse.getMailTemplateCcAddress());
            mailTemplateEntity.setMailTemplateBccAddress(mailResponse.getMailTemplateBccAddress());
            mailTemplateEntity.setRegistTime(conversionUtility.toTimestamp(mailResponse.getRegistTime()));
            mailTemplateEntity.setUpdateTime(conversionUtility.toTimestamp(mailResponse.getUpdateTime()));
        }
        return mailTemplateEntity;
    }

    /**
     * レスポンスに変換
     *
     * @param typeArray メール情報一覧取得リクエスト
     * @return テンプレート種別一覧
     */
    public MailListGetRequest toMailListGetRequest(HTypeMailTemplateType[] typeArray) {
        MailListGetRequest mailListGetRequest = new MailListGetRequest();
        List<String> list = new ArrayList<>();
        for (int i = 0; i < typeArray.length; i++) {
            list.add(typeArray[i].getValue());
        }
        mailListGetRequest.setTypeArray(list);
        return mailListGetRequest;
    }

    /**
     * レスポンスに変換
     *
     * @param mailListResponse メール情報一覧レスポンス
     * @return メール情報一覧レスポンス
     */
    public List<MailTemplateIndexDto> toListMailTemplateIndexDto(MailListResponse mailListResponse) {
        List<MailTemplateIndexDto> list = new ArrayList<>();
        mailListResponse.getIndexListOrig().forEach(mailTemplateIndexResponse -> {
            MailTemplateIndexDto mailTemplateIndexDto = new MailTemplateIndexDto();
            HTypeMailTemplateDefaultFlag mailTemplateDefaultFlag =
                            EnumTypeUtil.getEnumFromValue(HTypeMailTemplateDefaultFlag.class,
                                                          mailTemplateIndexResponse.getMailTemplateDefaultFlag()
                                                         );
            mailTemplateIndexDto.setMailTemplateDefaultFlag(mailTemplateDefaultFlag);
            mailTemplateIndexDto.setMailTemplateName(mailTemplateIndexResponse.getMailTemplateName());
            HTypeMailTemplateType mailTemplateType = EnumTypeUtil.getEnumFromValue(HTypeMailTemplateType.class,
                                                                                   mailTemplateIndexResponse.getMailTemplateType()
                                                                                  );
            mailTemplateIndexDto.setMailTemplateType(mailTemplateType);
            mailTemplateIndexDto.setMailTemplateSeq(mailTemplateIndexResponse.getMailTemplateSeq());
            list.add(mailTemplateIndexDto);
        });

        return list;
    }
}