/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.shop.inquiry.inquirygroup;

import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import jp.co.itechh.quad.inquirygroup.presentation.api.param.InquiryGroupListResponse;
import jp.co.itechh.quad.inquirygroup.presentation.api.param.InquiryGroupListUpdateRequest;
import jp.co.itechh.quad.inquirygroup.presentation.api.param.InquiryGroupRequest;
import jp.co.itechh.quad.inquirygroup.presentation.api.param.InquiryGroupResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 問い合わせ分類ヘルパー
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class InquiryGroupHelper {

    /**
     * 検索結果をページに反映<br/>
     *
     * @param inquiryGroupListResponse 問い合わせ分類一覧レスポンス
     * @param indexPage                問い合わせ分類一覧画面
     */
    public void toPageForLoad(InquiryGroupListResponse inquiryGroupListResponse, InquiryGroupModel indexPage) {

        List<InquiryGroupResponse> inquiryGroupList = inquiryGroupListResponse.getInquiryGroupList();

        List<InquiryGroupModelItem> pageItemList = new ArrayList<>();
        // 表示順はリストindex+1を表示順とする
        int orderDisplay = 1;
        Integer shopSeq = 1001;

        for (InquiryGroupResponse inquiryGroupResponse : inquiryGroupList) {
            InquiryGroupModelItem item = ApplicationContextUtility.getBean(InquiryGroupModelItem.class);

            item.setInquiryGroupSeq(inquiryGroupResponse.getInquiryGroupSeq());
            item.setInqueryGroupName(inquiryGroupResponse.getInquiryGroupName());
            item.setOpenStatus(EnumTypeUtil.getEnumFromValue(HTypeOpenDeleteStatus.class,
                                                             inquiryGroupResponse.getOpenStatus()
                                                            ));
            item.setShopSeq(shopSeq);
            item.setOrderDisplayRadio(orderDisplay++);

            pageItemList.add(item);
        }

        indexPage.setResultItems(pageItemList);
    }

    /**
     * 表示順更新用に、画面情報から更新用問い合わせ分類エンティティを作成
     *
     * @param indexPage 問い合わせ分類一覧表示
     * @return 更新用問い合わせ分類エンティティリスト
     */
    public InquiryGroupListUpdateRequest toInquiryGroupListUpdateRequestFromInquiryGroupModel(InquiryGroupModel indexPage) {

        List<InquiryGroupModelItem> resultList = indexPage.getResultItems();
        List<InquiryGroupRequest> inquiryGroupListUpdate = new ArrayList<>();
        InquiryGroupListUpdateRequest inquiryGroupListUpdateRequest = new InquiryGroupListUpdateRequest();

        for (InquiryGroupModelItem indexPageItem : resultList) {
            InquiryGroupRequest inquiryGroupRequest = new InquiryGroupRequest();

            inquiryGroupRequest.setInquiryGroupSeq(indexPageItem.getInquiryGroupSeq());
            inquiryGroupRequest.setInquiryGroupName(indexPageItem.getInqueryGroupName());
            inquiryGroupRequest.setOpenStatus(indexPageItem.getOpenStatus().getValue());
            inquiryGroupRequest.setOrderDisplay(indexPageItem.getOrderDisplayRadio());

            inquiryGroupListUpdate.add(inquiryGroupRequest);
        }

        inquiryGroupListUpdateRequest.setInquiryGroupListUpdate(inquiryGroupListUpdate);

        return inquiryGroupListUpdateRequest;
    }

    /**
     * 表示順を変更する(セッション情報)
     * ただし変更後の表示順が範囲外の場合は処理なし
     *
     * @param index     変更前の表示順(実際のindex)
     * @param addIndex  変更後の表示順
     * @param indexPage ページ
     */
    public void toPageForChangeOrderDisplay(int index, int addIndex, InquiryGroupModel indexPage) {
        List<InquiryGroupModelItem> inquiryGroupList = indexPage.getResultItems();

        // 変更可能な範囲であれば、表示順変更
        if (0 <= addIndex && addIndex <= inquiryGroupList.size() - 1) {
            InquiryGroupModelItem item = inquiryGroupList.remove(index);
            inquiryGroupList.add(addIndex, item);
            Integer selectedInquiryGroupSeq = item.getInquiryGroupSeq();

            // 表示順を再設定
            int orderDisplay = 1;
            for (InquiryGroupModelItem indexPageItem : inquiryGroupList) {
                indexPageItem.setOrderDisplayRadio(orderDisplay);

                if (selectedInquiryGroupSeq.equals(indexPageItem.getInquiryGroupSeq())) {
                    // 選択値保持
                    indexPage.setOrderDisplay(orderDisplay);
                }

                orderDisplay++;
            }
        }
    }
}