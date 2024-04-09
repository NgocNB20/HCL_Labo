/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.memberinfo.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.constant.type.HTypeMemberInfoStatus;
import jp.co.itechh.quad.core.entity.memberinfo.MemberInfoEntity;
import jp.co.itechh.quad.core.logic.memberinfo.MemberInfoDataCheckLogic;
import jp.co.itechh.quad.core.logic.memberinfo.MemberInfoUpdateLogic;
import jp.co.itechh.quad.core.logic.memberinfo.confirmmail.ConfirmMailDeleteLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.memberinfo.MemberInfoMailUpdateService;
import jp.co.itechh.quad.core.service.shop.mailmagazine.MailMagazineMemberChangeMailService;
import jp.co.itechh.quad.core.service.shop.mailmagazine.MailMagazineMemberRegistService;
import jp.co.itechh.quad.core.service.shop.mailmagazine.MailMagazineMemberRemoveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 会員メールアドレス変更サービス(本登録)実装<br/>
 *
 * @author natume
 * @author Kaneko (itec) 2012/08/21 UtilからHelperへ変更　Util使用箇所をgetComponentで取得するように変更
 *
 */
@Service
public class MemberInfoMailUpdateServiceImpl extends AbstractShopService implements MemberInfoMailUpdateService {

    /**
     * 会員データチェックロジック<br/>
     */
    private final MemberInfoDataCheckLogic memberInfoDataCheckLogic;

    /**
     * 会員情報更新ロジック<br/>
     */
    private final MemberInfoUpdateLogic memberInfoUpdateLogic;

    /**
     * メルマガ購読者情報登録サービス<br/>
     */
    private final MailMagazineMemberRegistService mailMagazineMemberRegistService;

    /** メルマガ解除サービス */
    private final MailMagazineMemberRemoveService mailMagazineMemberRemoveService;

    /** メルマガ会員メールアドレス更新サービス */
    private final MailMagazineMemberChangeMailService mailMagazineMemberChangeMailService;

    /** 確認メール削除ロジック */
    private final ConfirmMailDeleteLogic confirmMailDeleteLogic;

    @Autowired
    public MemberInfoMailUpdateServiceImpl(MemberInfoDataCheckLogic memberInfoDataCheckLogic,
                                           MemberInfoUpdateLogic memberInfoUpdateLogic,
                                           MailMagazineMemberRegistService mailMagazineMemberRegistService,
                                           MailMagazineMemberRemoveService mailMagazineMemberRemoveService,
                                           MailMagazineMemberChangeMailService mailMagazineMemberChangeMailService,
                                           ConfirmMailDeleteLogic confirmMailDeleteLogic) {
        this.memberInfoDataCheckLogic = memberInfoDataCheckLogic;
        this.memberInfoUpdateLogic = memberInfoUpdateLogic;
        this.mailMagazineMemberRegistService = mailMagazineMemberRegistService;
        this.mailMagazineMemberRemoveService = mailMagazineMemberRemoveService;
        this.mailMagazineMemberChangeMailService = mailMagazineMemberChangeMailService;
        this.confirmMailDeleteLogic = confirmMailDeleteLogic;
    }

    /**
     * 会員メールアドレス変更処理<br/>
     *
     * @param shopSeq ショップSEQ
     * @param memberInfoEntity 会員エンティティ
     * @param mailMagazine メルマガ購読フラグ
     * @param preMailMagazine 変更前メルマガ購読フラグ
     * @param confirmMailPassword 確認メールパスワード
     */
    @Override
    public void execute(Integer shopSeq,
                        MemberInfoEntity memberInfoEntity,
                        boolean mailMagazine,
                        boolean preMailMagazine,
                        String confirmMailPassword) {

        // パラメータチェック
        ArgumentCheckUtil.assertNotNull("memberInfoEntity", memberInfoEntity);
        ArgumentCheckUtil.assertNotNull("confirmMailPassword", confirmMailPassword);

        // 会員データチェック
        memberInfoDataCheckLogic.execute(memberInfoEntity.getMemberInfoMail(), HTypeMemberInfoStatus.ADMISSION);

        // 会員情報更新
        int result = memberInfoUpdateLogic.execute(memberInfoEntity);
        if (result == 0) {
            throwMessage(MSGCD_MEMBERINFO_UPDATE_ERROR);
        }

        // メルマガ登録処理
        // 購読フラグが、変更後がON かつ 変更前がOFFの場合
        if (mailMagazine && !preMailMagazine) {
            registUpdateMailMagazine(memberInfoEntity);
        }
        // メルマガ解除処理
        // 購読フラグが、変更後がOFF かつ 変更前がONの場合
        else if (!mailMagazine && preMailMagazine) {
            removeMailMagazine(memberInfoEntity);
        } else {
            // メールアドレスだけ更新する
            changeMailMagazineMail(shopSeq, memberInfoEntity);
        }

        int resultDelete = 0;
        // 確認メール削除ロジック実行
        resultDelete = confirmMailDeleteLogic.execute(confirmMailPassword);
        if (resultDelete == 0) {
            throwMessage(MSGCD_MEMBERINFO_UPDATE_ERROR);
        }
    }

    /**
     * メルマガ登録更新処理<br/>
     *
     * @param memberInfoEntity 会員エンティティ
     */
    protected void registUpdateMailMagazine(MemberInfoEntity memberInfoEntity) {

        // メルマガ登録・更新 ※解除は行わない
        mailMagazineMemberRegistService.execute(
                        memberInfoEntity.getMemberInfoMail(), memberInfoEntity.getMemberInfoSeq());
    }

    /**
     * メルマガ解除処理<br/>
     *
     * @param memberInfoEntity 会員エンティティ
     */
    protected void removeMailMagazine(MemberInfoEntity memberInfoEntity) {

        // メルマガ解除
        mailMagazineMemberRemoveService.execute(
                        memberInfoEntity.getMemberInfoMail(), memberInfoEntity.getMemberInfoSeq());
    }

    /**
     * メルマガ会員メアド変更処理<br/>
     *
     * @param shopSeq ショップSEQ
     * @param memberInfoEntity 会員エンティティ
     */
    protected void changeMailMagazineMail(Integer shopSeq, MemberInfoEntity memberInfoEntity) {

        // メルマガ会員のメアドのみ更新
        mailMagazineMemberChangeMailService.execute(
                        shopSeq, memberInfoEntity.getMemberInfoMail(), memberInfoEntity.getMemberInfoSeq());
    }
}