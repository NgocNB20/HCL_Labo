package jp.co.itechh.quad.notification.presentation.api;

import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.constant.type.HTypeMailTemplateType;
import jp.co.itechh.quad.core.dto.shop.mail.MailTemplateIndexDto;
import jp.co.itechh.quad.core.entity.shop.mail.MailTemplateEntity;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.notificationsub.presentation.api.param.MailListGetRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.MailListResponse;
import jp.co.itechh.quad.notificationsub.presentation.api.param.MailResponse;
import jp.co.itechh.quad.notificationsub.presentation.api.param.MailTemplateIndexResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * メールテンプレート詳細取得 Helper
 *
 * @author PHAM QUANG DIEU (VJP)
 */
@Component
public class NotificationHelper {

    /**
     * 日付関連Helper取得
     */
    private final DateUtility dateUtility;

    /**
     * コンストラクター
     *
     * @param dateUtility 日付関連Utilityクラス
     */
    public NotificationHelper(DateUtility dateUtility) {
        this.dateUtility = dateUtility;
    }

    /**
     * レスポンスに変換
     *
     * @param mailTemplateEntity メールテンプレートクラス
     * @return メール情報レスポンス
     */
    public MailResponse toMailResponse(MailTemplateEntity mailTemplateEntity) {

        MailResponse mailResponse = new MailResponse();
        if (mailTemplateEntity != null) {
            mailResponse.setMailTemplateSeq(mailTemplateEntity.getMailTemplateSeq());
            mailResponse.setMailTemplateName(mailTemplateEntity.getMailTemplateName());
            mailResponse.setMailTemplateType(EnumTypeUtil.getValue(mailTemplateEntity.getMailTemplateType()));
            mailResponse.setMailTemplateDefaultFlag(
                            EnumTypeUtil.getValue(mailTemplateEntity.getMailTemplateDefaultFlag()));
            mailResponse.setMailTemplateSubject(mailTemplateEntity.getMailTemplateSubject());
            mailResponse.setMailTemplateFromAddress(mailTemplateEntity.getMailTemplateFromAddress());
            mailResponse.setMailTemplateToAddress(mailTemplateEntity.getMailTemplateToAddress());
            mailResponse.setMailTemplateCcAddress(mailTemplateEntity.getMailTemplateCcAddress());
            mailResponse.setMailTemplateBccAddress(mailTemplateEntity.getMailTemplateBccAddress());
            mailResponse.setRegistTime(dateUtility.convertDateToTimestamp(mailTemplateEntity.getRegistTime()));
            mailResponse.setUpdateTime(dateUtility.convertDateToTimestamp(mailTemplateEntity.getUpdateTime()));
        }
        return mailResponse;
    }

    /**
     * レスポンスに変換
     *
     * @param mailListGetRequest メール情報一覧取得リクエスト
     * @return テンプレート種別一覧
     */
    public HTypeMailTemplateType[] toHTypeMailTemplateType(MailListGetRequest mailListGetRequest) {
        HTypeMailTemplateType[] typeArray = new HTypeMailTemplateType[mailListGetRequest.getTypeArray().size()];
        for (int i = 0; i < mailListGetRequest.getTypeArray().size(); i++) {
            typeArray[i] = EnumTypeUtil.getEnumFromValue(HTypeMailTemplateType.class,
                                                         mailListGetRequest.getTypeArray().get(i)
                                                        );
        }
        return typeArray;
    }

    /**
     * レスポンスに変換
     *
     * @param indexListOrig メールテンプレートクラス
     * @return メール情報一覧レスポンス
     */
    public MailListResponse toMailListResponse(List<MailTemplateIndexDto> indexListOrig) {
        MailListResponse mailListResponse = new MailListResponse();
        List<MailTemplateIndexResponse> mailTemplateIndexResponses = new ArrayList<>();
        indexListOrig.forEach(mailTemplateIndexDto -> {
            MailTemplateIndexResponse mailTemplateIndexResponse = new MailTemplateIndexResponse();
            mailTemplateIndexResponse.setMailTemplateDefaultFlag(
                            EnumTypeUtil.getValue(mailTemplateIndexDto.getMailTemplateDefaultFlag()));
            mailTemplateIndexResponse.setMailTemplateName(mailTemplateIndexDto.getMailTemplateName());
            mailTemplateIndexResponse.setMailTemplateSeq(mailTemplateIndexDto.getMailTemplateSeq());
            mailTemplateIndexResponse.setMailTemplateType(
                            EnumTypeUtil.getValue(mailTemplateIndexDto.getMailTemplateType()));
            mailTemplateIndexResponses.add(mailTemplateIndexResponse);
        });
        mailListResponse.setIndexListOrig(mailTemplateIndexResponses);
        return mailListResponse;
    }

}