package jp.co.itechh.quad.notification.presentation.api.processor;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.constant.type.HTypeMailTemplateType;
import jp.co.itechh.quad.core.dao.shop.mail.MailTemplateDao;
import jp.co.itechh.quad.core.dto.mail.MailDto;
import jp.co.itechh.quad.core.dto.order.OrderReceivedDto;
import jp.co.itechh.quad.core.entity.shop.mail.MailTemplateEntity;
import jp.co.itechh.quad.core.logic.order.OrderReceivedGetLogic;
import jp.co.itechh.quad.core.service.mail.MailSendService;
import jp.co.itechh.quad.core.utility.MailUtility;
import jp.co.itechh.quad.notificationsub.presentation.api.param.PaymentDepositedRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 入金完了メール送信要求Processor
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
@Scope("prototype")
public class PaymentDepositedProcessor {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentDepositedProcessor.class);

    /** メールUtility取得 */
    private final MailUtility mailUtility;

    /**
     * @param mailUtility メールUtility
     */
    public PaymentDepositedProcessor(MailUtility mailUtility) {
        this.mailUtility = mailUtility;
    }

    /**
     * 入金完了メール送信要求
     *
     * @param request 入金完了メール送信要求リクエスト
     */
    public void processor(PaymentDepositedRequest request) throws Exception {

        LOGGER.info("【Subscribe】ルーティングキー： payment-deposited-routing");

        // 引数チェック
        checkParameter(request.getTargetOrderList());

        // 送信先取得
        MailTemplateDao mailTemplateDao = ApplicationContextUtility.getBean(MailTemplateDao.class);
        MailTemplateEntity entity = mailTemplateDao.getEntityByType(1001, HTypeMailTemplateType.PAYMENT_DEPOSITED);

        // ショップ名
        String shopName = PropertiesUtil.getSystemPropertiesValue("shop.name");

        // 送信先アドレスを設定
        String targetMailAddress = null;

        OrderReceivedGetLogic logic = ApplicationContextUtility.getBean(OrderReceivedGetLogic.class);
        for (String target : request.getTargetOrderList()) {
            OrderReceivedDto dto = logic.execute(target);

            // メール用値マップの作成
            Map<String, String> mailContentsMap = new LinkedHashMap<>();

            // メール用値マップの作成
            mailContentsMap.put("targetOrderList", target);
            mailContentsMap.put("O_ORDER_LAST_NAME",
                                dto.getMemberInfoDetailsDto().getMemberInfoEntity().getMemberInfoLastName()
                               );
            mailContentsMap.put("O_ORDER_FIRST_NAME",
                                dto.getMemberInfoDetailsDto().getMemberInfoEntity().getMemberInfoFirstName()
                               );
            mailContentsMap.put("O_ORDER_CODE", target);
            mailContentsMap.put("overFlag", request.getOverFlag().toString());
            mailContentsMap.put("shopName", shopName);

            targetMailAddress = dto.getMemberInfoDetailsDto().getMemberInfoEntity().getMemberInfoMail();

            MailDto mailDto = mailUtility.createMailDto(HTypeMailTemplateType.PAYMENT_DEPOSITED, entity,
                                                        Collections.singletonList(targetMailAddress), mailContentsMap
                                                       );

            // メール送信
            MailSendService mailSendService = ApplicationContextUtility.getBean(MailSendService.class);
            mailSendService.execute(mailDto);

            LOGGER.info("入金完了を送信しました。");
        }

    }

    /**
     * 引数チェック
     *
     * @param orderCodeList 受注番号リスト
     */
    protected void checkParameter(List<String> orderCodeList) {
        ArgumentCheckUtil.assertNotNull("OrderCodeList", orderCodeList);
    }
}