package jp.co.itechh.quad.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = {"classpath:config/hitmall/coreMessages.properties",
                "classpath:config/hitmall/mail/mail-template.properties",
                "classpath:config/hitmall/mailTemplateDummies/FollowMailCartTransformHelper.properties",
                "classpath:config/hitmall/mailTemplateDummies/GoodsArrivalTransformHelper.properties",
                "classpath:config/hitmall/mailTemplateDummies/InquiryTransformHelper.properties",
                "classpath:config/hitmall/mailTemplateDummies/MailMagazineMemberRemoveTransformHelper.properties",
                "classpath:config/hitmall/mailTemplateDummies/MailMagazineMemberTransformHelper.properties",
                "classpath:config/hitmall/mailTemplateDummies/MemberInfoTransformHelper.properties",
                "classpath:config/hitmall/mailTemplateDummies/MemberRankNotificationTransformHelper.properties",
                "classpath:config/hitmall/mailTemplateDummies/OrderTransformHelper.properties",
                "classpath:config/hitmall/mailTemplateDummies/PointAddBirthTransformHelper.properties",
                "classpath:config/hitmall/mailTemplateDummies/PointNotificationTransformHelper.properties",
                "classpath:config/hitmall/mailTemplateDummies/StringTransformHelper.properties",
                "classpath:config/hitmall/mailTemplateDummies/VoidTransformHelper.properties",
                "classpath:config/hitmall/IPAddress/IPAddress.properties",
                "classpath:config/hitmall/htmlparse/definedTag.properties",
                "classpath:config/hitmall/htmlparse/dependencyTag.properties",
                "classpath:config/hitmall/htmlparse/exclusiveTag.properties",
                "classpath:config/hitmall/htmlparse/singleTag.properties",
                "classpath:config/hitmall/htmlparse/uniqueTag.properties",
                "classpath:config/hitmall/carrier/CarrierAgent.properties",
                "classpath:config/hitmall/carrier/CarrierEmail.properties",
                "classpath:config/hitmall/alert/orderRegistAlert.properties"}, ignoreResourceNotFound = true,
                encoding = "UTF-8")
public class HmCorePropertiesConfiguration {
}