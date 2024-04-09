/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.memberinfo.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.constant.type.HTypeConfirmMailType;
import jp.co.itechh.quad.core.constant.type.HTypeMemberInfoStatus;
import jp.co.itechh.quad.core.entity.memberinfo.MemberInfoEntity;
import jp.co.itechh.quad.core.entity.memberinfo.confirmmail.ConfirmMailEntity;
import jp.co.itechh.quad.core.logic.memberinfo.MemberInfoDataCheckLogic;
import jp.co.itechh.quad.core.logic.memberinfo.MemberInfoGetLogic;
import jp.co.itechh.quad.core.logic.memberinfo.confirmmail.ConfirmMailGetLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.memberinfo.MemberInfoUpdateMailGetService;
import jp.co.itechh.quad.core.utility.MemberInfoUtility;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 会員変更メールアドレス取得サービス<br/>
 *
 * @author natume
 * @author Kaneko (itec) 2012/08/09 UtilからHelperへ変更　Util使用箇所をgetComponentで取得するように変更
 *
 */
@Service
public class MemberInfoUpdateMailGetServiceImpl extends AbstractShopService implements MemberInfoUpdateMailGetService {

    /**
     * 確認メール情報取得<br/>
     */
    private final ConfirmMailGetLogic confirmMailGetLogic;

    /**
     * 会員データチェックロジック<br/>
     */
    private final MemberInfoDataCheckLogic memberInfoDataCheckLogic;

    /**
     * 会員情報取得ロジック<br/>
     */
    private final MemberInfoGetLogic memberInfoGetLogic;

    @Autowired
    public MemberInfoUpdateMailGetServiceImpl(ConfirmMailGetLogic confirmMailGetLogic,
                                              MemberInfoDataCheckLogic memberInfoDataCheckLogic,
                                              MemberInfoGetLogic memberInfoGetLogic) {
        this.confirmMailGetLogic = confirmMailGetLogic;
        this.memberInfoDataCheckLogic = memberInfoDataCheckLogic;
        this.memberInfoGetLogic = memberInfoGetLogic;
    }

    /**
     * メールアドレス本登録処理<br/>
     *
     * @param password メールパスワード
     * @return 会員情報
     */
    @Override
    public MemberInfoEntity execute(String password) {

        // パラメータチェック
        ArgumentCheckUtil.assertNotEmpty("password", password);

        // 確認メール情報取得
        ConfirmMailEntity confirmMailEntity = getConfirmMail(password);

        // 会員情報取得
        MemberInfoEntity memberInfoEntity = getMemberInfo(confirmMailEntity.getMemberInfoSeq());

        // メールアドレスが既に変更されているかをチェック
        if (StringUtils.isNotEmpty(memberInfoEntity.getMemberInfoMail()) && memberInfoEntity.getMemberInfoMail()
                                                                                            .equals(confirmMailEntity.getConfirmMail())) {
            throwMessage(MSGCD_ALREADY_MAIL_UPDATE_FAIL);
        }

        // id・メール・ユニークidセット
        memberInfoEntity.setMemberInfoId(confirmMailEntity.getConfirmMail());
        memberInfoEntity.setMemberInfoMail(confirmMailEntity.getConfirmMail());

        // 会員業務Helper取得
        MemberInfoUtility memberInfoUtility = ApplicationContextUtility.getBean(MemberInfoUtility.class);
        memberInfoEntity.setMemberInfoUniqueId(memberInfoUtility.createShopUniqueId(confirmMailEntity.getShopSeq(),
                                                                                    confirmMailEntity.getConfirmMail()
                                                                                   ));

        // 会員データチェック
        memberInfoDataCheckLogic.execute(memberInfoEntity.getMemberInfoMail(), HTypeMemberInfoStatus.ADMISSION);

        // 会員情報返す
        return memberInfoEntity;
    }

    /**
     * 確認メール情報取得<br/>
     *
     * @param password パスワード
     * @return 確認メールエンティティ
     */
    protected ConfirmMailEntity getConfirmMail(String password) {
        ConfirmMailEntity confirmMailEntity =
                        confirmMailGetLogic.execute(password, HTypeConfirmMailType.MEMBERINFO_MAIL_UPDATE);
        if (confirmMailEntity == null) {
            throwMessage(MSGCD_CONFIRMMAILENTITYDTO_NULL);
        }
        return confirmMailEntity;
    }

    /**
     * 会員情報取得<br/>
     *
     * @param memberInfoSeq 会員SEQ
     * @return 会員エンティティ
     */
    protected MemberInfoEntity getMemberInfo(Integer memberInfoSeq) {

        // 「入会」会員に限定
        MemberInfoEntity memberInfoEntity = memberInfoGetLogic.execute(memberInfoSeq, HTypeMemberInfoStatus.ADMISSION);
        if (memberInfoEntity == null) {
            throwMessage(MSGCD_MEMBERINFOENTITYDTO_NULL);
        }
        return memberInfoEntity;
    }
}