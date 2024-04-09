/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.inquiry.impl;

import jp.co.itechh.quad.core.entity.inquiry.InquiryGroupEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.shop.inquiry.InquiryGroupRegistUpdateCheckLogic;
import jp.co.itechh.quad.core.service.shop.inquiry.InquiryGroupListGetForBackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * お問い合わせ分類登録更新チェックロジック実装クラス<br/>
 *
 * @author Nishigaki Mio (itec)
 */
@Component
public class InquiryGroupRegistUpdateCheckLogicImpl extends AbstractShopLogic
                implements InquiryGroupRegistUpdateCheckLogic {

    /** バック用問い合わせ分類一覧取得サービス */
    private final InquiryGroupListGetForBackService inquiryGroupListGetService;

    @Autowired
    public InquiryGroupRegistUpdateCheckLogicImpl(InquiryGroupListGetForBackService inquiryGroupListGetService) {
        this.inquiryGroupListGetService = inquiryGroupListGetService;
    }

    /**
     * お問い合わせ分類登録更新チェック<br/>
     * ・問い合わせ分類名重複チェック
     *
     * @param inquiryGroupEntity 問い合わせ分類エンティティ
     */
    @Override
    public void execute(InquiryGroupEntity inquiryGroupEntity) {

        // 問い合わせ分類SEQ
        Integer groupSeq = inquiryGroupEntity.getInquiryGroupSeq();

        // 問い合わせ分類名重複チェック
        String newName = inquiryGroupEntity.getInquiryGroupName();
        if (!checkGroupName(newName, groupSeq)) {
            addErrorMessage(MSGCD_INQUIRYGROUP_NAME_OVERLAP, new Object[] {newName});
        }
        // エラーがあった場合
        if (hasErrorList()) {
            throwMessage();
        }
    }

    /**
     * 問い合わせ分類名重複チェック<br/>
     * 既に登録している名称はエラー<br/>
     *
     * @param newName 新しく登録しようとする名称
     * @param groupSeq 更新中の問い合わせ分類SEQ(新規登録時はnull)
     * @return OK…true / NG…false
     */
    protected boolean checkGroupName(String newName, Integer groupSeq) {
        // お問い合わせ分類の一覧を取得
        List<InquiryGroupEntity> list = inquiryGroupListGetService.execute();

        for (InquiryGroupEntity registeredGroupEntity : list) {
            String registeredName = registeredGroupEntity.getInquiryGroupName();
            // 登録済みの名称と一致し、かつ登録時であるか更新時で別の分類と重複している
            if (registeredName != null && registeredName.equals(newName)) {
                // 登録済みの名称と一致する
                if (groupSeq == null) {
                    // 新規登録時である
                    return false;
                }
                if (!registeredGroupEntity.getInquiryGroupSeq().equals(groupSeq)) {
                    // 更新時、別の分類と名称が重複する
                    return false;
                }
            }
        }
        return true;
    }
}