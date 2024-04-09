package jp.co.itechh.quad.core.transformer.mailtemplate;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.dto.inquiry.InquiryDetailsDto;
import jp.co.itechh.quad.core.entity.inquiry.InquiryEntity;
import jp.co.itechh.quad.core.helper.mailtemplate.Transformer;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.core.utility.MailTemplateDummyMapUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@Component("inquiryTransformHelper")
public class InquiryTransformer implements Transformer {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(InquiryTransformer.class);

    /**
     * ダミープレースホルダを返す
     *
     * @return ダミープレースホルダ
     */
    @Override
    public Map<String, String> getDummyPlaceholderMap() {
        // メールテンプレート用ダミー値マップ取得用Helper取得
        MailTemplateDummyMapUtility mailTemplateDummyMapUtility =
                        ApplicationContextUtility.getBean(MailTemplateDummyMapUtility.class);

        return mailTemplateDummyMapUtility.getDummyValueMap(getResourceName());
    }

    /**
     * テンプレートタイプ11/12の
     * メール送信に使用する値マップを作成する。
     *
     * @param arguments 引数
     * @return 値マップ
     */
    @Override
    public Map<String, String> toValueMap(Object... arguments) {

        // 引数チェック
        this.checkArguments(arguments);

        // メールテンプレート用ダミー値マップ取得用Helper取得
        MailTemplateDummyMapUtility mailTemplateDummyMapUtility =
                        ApplicationContextUtility.getBean(MailTemplateDummyMapUtility.class);

        if (arguments == null) {
            return mailTemplateDummyMapUtility.getDummyValueMap(getResourceName());
        } else if (arguments.length == 0) {
            return Collections.emptyMap();
        }

        // 問い合わせ詳細Dto
        InquiryDetailsDto inquiryDetailsDto = (InquiryDetailsDto) arguments[0];
        InquiryEntity inquiryEntity = inquiryDetailsDto.getInquiryEntity();
        Integer memberInfoSeq = inquiryDetailsDto.getMemberInfoEntity() == null ?
                        0 :
                        inquiryDetailsDto.getMemberInfoEntity().getMemberInfoSeq();

        Map<String, String> valueMap = new LinkedHashMap<>();

        ConversionUtility conversionUtility = ApplicationContextUtility.getBean(ConversionUtility.class);

        // 日付関連Helper取得
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

        // valueMap make
        valueMap.put("I_CODE", inquiryEntity.getInquiryCode());
        valueMap.put("I_LAST_NAME", inquiryEntity.getInquiryLastName());
        valueMap.put("I_FIRST_NAME", inquiryEntity.getInquiryFirstName());
        valueMap.put("I_MAIL", inquiryEntity.getInquiryMail());
        valueMap.put("I_INQUIRY_BODY", inquiryEntity.getInquiryBody());
        valueMap.put("I_INQUIRY_TIME", dateUtility.format(inquiryEntity.getInquiryTime(), "yyyy年M月d日H時m分"));
        valueMap.put("I_STATUS", EnumTypeUtil.getValue(inquiryEntity.getInquiryStatus()));
        valueMap.put("I_ANSWER_TIME", dateUtility.format(inquiryEntity.getAnswerTime(), "yyyy年M月d日H時m分"));
        valueMap.put("I_ANSWER_TITLE", inquiryEntity.getAnswerTitle());
        valueMap.put("I_ANSWER_BODY", inquiryEntity.getAnswerBody());
        valueMap.put("I_ANSWER_FROM", inquiryEntity.getAnswerFrom());
        valueMap.put("I_ANSWER_TO", inquiryEntity.getAnswerTo());
        valueMap.put("I_ANSWER_BCC", inquiryEntity.getAnswerBcc());
        valueMap.put("I_LAST_KANA", inquiryEntity.getInquiryLastKana());
        valueMap.put("I_FIRST_KANA", inquiryEntity.getInquiryFirstKana());
        valueMap.put("I_REGIST_TIME", dateUtility.format(inquiryEntity.getRegistTime(), "yyyy年M月d日H時m分"));
        valueMap.put("I_UPDATE_TIME", dateUtility.format(inquiryEntity.getUpdateTime(), "yyyy年M月d日H時m分"));

        // 問い合わせ分類名
        valueMap.put("I_CLASS_NAME", inquiryDetailsDto.getInquiryGroupName());

        valueMap.put("I_TYPE", EnumTypeUtil.getValue(inquiryEntity.getInquiryType()));
        valueMap.put("I_MEMBERINFO_SEQ", conversionUtility.toString(memberInfoSeq));
        valueMap.put("I_ORDER_CODE", inquiryEntity.getOrderCode());
        valueMap.put(
                        "I_FIRST_INQUIRY_TIME",
                        dateUtility.format(inquiryEntity.getFirstInquiryTime(), DateUtility.YMD_HMS_JP)
                    );
        valueMap.put(
                        "I_LAST_USER_INQUIRY_TIME",
                        dateUtility.format(inquiryEntity.getLastUserInquiryTime(), DateUtility.YMD_HMS_JP)
                    );

        return valueMap;
    }

    /**
     * 引数の有効性を確認する
     *
     * @param arguments 引数
     */
    protected void checkArguments(Object[] arguments) {

        // オブジェクトがない場合はテスト送信用とみなす
        if (arguments == null) {
            return;
        }

        if (arguments.length != 1) {
            RuntimeException e = new IllegalArgumentException("プレースホルダ用値マップに変換できません：引数の数が合いません。");
            LOGGER.warn("引数の数が合いません。", e);
            throw e;
        }

        if (!(arguments[0] instanceof InquiryDetailsDto)) {
            String v = "null";
            if (arguments[0] != null) {
                v = v.getClass().getSimpleName();
            }
            RuntimeException e = new IllegalArgumentException("プレースホルダ用値マップに変換できません：引数の型が合いません。" + v);
            LOGGER.warn("引数の型が合いません。", e);
            throw e;
        }
    }

    /**
     * リソースファイル名<br/>
     *
     * @return リソースファイル名
     */
    @Override
    public String getResourceName() {
        return "InquiryTransformHelper";
    }
}