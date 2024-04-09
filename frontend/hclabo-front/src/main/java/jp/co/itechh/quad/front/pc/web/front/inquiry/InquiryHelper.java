/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.front.pc.web.front.inquiry;

import jp.co.itechh.quad.customer.presentation.api.param.CustomerResponse;
import jp.co.itechh.quad.front.constant.type.HTypeInquiryRequestType;
import jp.co.itechh.quad.front.constant.type.HTypeInquiryStatus;
import jp.co.itechh.quad.front.constant.type.HTypeInquiryType;
import jp.co.itechh.quad.front.utility.CommonInfoUtility;
import jp.co.itechh.quad.inquiry.presentation.api.param.InquiryCustomerDetailRequest;
import jp.co.itechh.quad.inquiry.presentation.api.param.InquiryDetailRequest;
import jp.co.itechh.quad.inquiry.presentation.api.param.InquiryRegistRequest;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 問い合わせ Helperクラス<br/>
 *
 * @author ando-no
 */
@Component
public class InquiryHelper {
    /**
     * CommonInfoUtility
     */
    private CommonInfoUtility commonInfoUtility;

    /**
     * コンストラクタ
     *
     * @param commonInfoUtility
     */
    @Autowired
    public InquiryHelper(CommonInfoUtility commonInfoUtility) {
        this.commonInfoUtility = commonInfoUtility;
    }

    /**
     * Modelへの変換処理
     *
     * @param customerResponse 会員情報Response
     * @param inquiryModel     問い合わせModel
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public void toPageForLoad(CustomerResponse customerResponse, InquiryModel inquiryModel)
                    throws IllegalAccessException, InvocationTargetException {

        // フィールドコピー
        BeanUtils.copyProperties(inquiryModel, customerResponse);

        // 取得した会員情報をページに変換
        inquiryModel.setInquiryLastName(customerResponse.getMemberInfoLastName());
        inquiryModel.setInquiryFirstName(customerResponse.getMemberInfoFirstName());
        inquiryModel.setInquiryLastKana(customerResponse.getMemberInfoLastKana());
        inquiryModel.setInquiryFirstKana(customerResponse.getMemberInfoFirstKana());
        inquiryModel.setInquiryMail(customerResponse.getMemberInfoMail());
        inquiryModel.setInquiryTel(customerResponse.getMemberInfoTel());
    }

    /**
     * Modelへの変換処理
     *
     * @param inquiryModel 問い合わせ入力画面
     */
    public void toPageForConfirm(InquiryModel inquiryModel) {
        // 問い合わせ分類SEQから名称を取得
        Map<String, String> inquiryGroupItems = inquiryModel.getInquiryGroupItems();

        inquiryModel.setInquiryGroupName(inquiryGroupItems.get(inquiryModel.getInquiryGroup()));
    }

    /**
     * 問い合わせリクエストへ変換
     *
     * @param inquiryModel 問い合わせModel
     * @return 問い合わせエンティティDTO
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public InquiryRegistRequest toRegistRequestForPage(InquiryModel inquiryModel)
                    throws IllegalAccessException, InvocationTargetException {

        InquiryRegistRequest inquiryRegistRequest = new InquiryRegistRequest();
        BeanUtils.copyProperties(inquiryRegistRequest, inquiryModel);
        if (inquiryModel.getInquiryGroup() != null) {
            inquiryRegistRequest.setInquiryGroupSeq(Integer.parseInt(inquiryModel.getInquiryGroup()));
        }

        // 画面入力項目以外を設定
        Date date = new Date();
        inquiryRegistRequest.setLastUserInquiryTime(date);
        inquiryRegistRequest.setFirstInquiryTime(date);
        inquiryRegistRequest.setInquiryStatus(HTypeInquiryStatus.RECEIVING.getValue());
        inquiryRegistRequest.setInquiryType(HTypeInquiryType.GENERAL.getValue());

        InquiryCustomerDetailRequest inquiryCustomerDetailRequest = new InquiryCustomerDetailRequest();
        // TODO フロントエンド実装 commonInfo
        // if (commonInfoUtility.isLogin(inquiryModel.getCommonInfo())) {
        //            // ログイン中の場合は会員SEQを取得
        //            inquiryCustomerDetailRequest.setMemberInforSeq(inquiryModel.getCommonInfo().getCommonInfoUser().getMemberInfoSeq());
        //        }

        // 画面入力項目を設定
        InquiryDetailRequest inquiryDetailRequest = new InquiryDetailRequest();
        inquiryDetailRequest.setInquiryBody(inquiryModel.getInquiryBody());
        // 画面入力項目以外を設定
        inquiryDetailRequest.setInquiryVersionNo(1);
        inquiryDetailRequest.setRequestType(HTypeInquiryRequestType.CONSUMER.getValue());
        inquiryDetailRequest.setInquiryTime(date);

        // お問い合わせリクエストに設定
        inquiryRegistRequest.setInquiryCustomerDetail(inquiryCustomerDetailRequest);
        List<InquiryDetailRequest> inquiryDetailRequestList = new ArrayList<>();
        inquiryDetailRequestList.add(inquiryDetailRequest);
        inquiryRegistRequest.setInquiryDetailList(inquiryDetailRequestList);

        return inquiryRegistRequest;
    }

}