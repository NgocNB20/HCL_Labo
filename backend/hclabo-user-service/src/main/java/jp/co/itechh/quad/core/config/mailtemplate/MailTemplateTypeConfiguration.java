package jp.co.itechh.quad.core.config.mailtemplate;

import jp.co.itechh.quad.core.application.mailtemplate.MailTemplateTypeEntry;
import jp.co.itechh.quad.core.constant.type.HTypeMailTemplateType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class MailTemplateTypeConfiguration {

    @Bean("mailTemplateTypeEntryList")
    public List<MailTemplateTypeEntry> mailTemplateTypeEntryList() {
        List<MailTemplateTypeEntry> mailTemplateTypeEntries = new ArrayList<>();

        MailTemplateTypeEntry orderConfirm = new MailTemplateTypeEntry();
        orderConfirm.setTemplateType(HTypeMailTemplateType.ORDER_CONFIRMATION);
        orderConfirm.setOrder(70);
        mailTemplateTypeEntries.add(orderConfirm);

        MailTemplateTypeEntry shipmentNotification = new MailTemplateTypeEntry();
        shipmentNotification.setTemplateType(HTypeMailTemplateType.SHIPMENT_NOTIFICATION);
        shipmentNotification.setOrder(80);
        mailTemplateTypeEntries.add(shipmentNotification);

        MailTemplateTypeEntry settlementReminder = new MailTemplateTypeEntry();
        settlementReminder.setTemplateType(HTypeMailTemplateType.SETTLEMENT_REMINDER);
        settlementReminder.setOrder(100);
        mailTemplateTypeEntries.add(settlementReminder);

        MailTemplateTypeEntry settlementExpirationNotification = new MailTemplateTypeEntry();
        settlementExpirationNotification.setTemplateType(HTypeMailTemplateType.SETTLEMENT_EXPIRATION_NOTIFICATION);
        settlementExpirationNotification.setOrder(110);
        mailTemplateTypeEntries.add(settlementExpirationNotification);

        MailTemplateTypeEntry examResultsNotification = new MailTemplateTypeEntry();
        examResultsNotification.setTemplateType(HTypeMailTemplateType.EXAM_RESULTS_NOTIFICATION);
        examResultsNotification.setOrder(670);
        mailTemplateTypeEntries.add(examResultsNotification);

        return mailTemplateTypeEntries;
    }
}