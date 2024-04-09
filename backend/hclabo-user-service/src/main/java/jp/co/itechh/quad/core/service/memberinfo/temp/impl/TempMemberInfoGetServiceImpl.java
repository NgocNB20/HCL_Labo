/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.memberinfo.temp.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.constant.type.HTypeConfirmMailType;
import jp.co.itechh.quad.core.dto.memberinfo.TempMemberInfoDto;
import jp.co.itechh.quad.core.entity.memberinfo.MemberInfoEntity;
import jp.co.itechh.quad.core.entity.memberinfo.confirmmail.ConfirmMailEntity;
import jp.co.itechh.quad.core.logic.memberinfo.MemberInfoDataCheckLogic;
import jp.co.itechh.quad.core.logic.memberinfo.confirmmail.ConfirmMailGetLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.memberinfo.temp.TempMemberInfoGetService;
import jp.co.itechh.quad.core.utility.MemberInfoUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 仮会員情報取得サービスの実装<br/>
 *
 * @author natume
 * @author Kaneko (itec) 2012/08/09 UtilからHelperへ変更　Util使用箇所をgetComponentで取得するように変更
 *
 */
@Service
public class TempMemberInfoGetServiceImpl extends AbstractShopService implements TempMemberInfoGetService {

    /** 確認メール情報取得 */
    private final ConfirmMailGetLogic confirmMailGetLogic;

    /** 会員情報データチェックロジック */
    private final MemberInfoDataCheckLogic memberInfoDataCheckLogic;

    /** MemberInfoUtility */
    private final MemberInfoUtility memberInfoUtility;

    @Autowired
    public TempMemberInfoGetServiceImpl(ConfirmMailGetLogic confirmMailGetLogic,
                                        MemberInfoDataCheckLogic memberInfoDataCheckLogic,
                                        MemberInfoUtility memberInfoUtility) {
        this.confirmMailGetLogic = confirmMailGetLogic;
        this.memberInfoDataCheckLogic = memberInfoDataCheckLogic;
        this.memberInfoUtility = memberInfoUtility;
    }

    /**
     * 仮会員情報取得処理<br/>
     *
     * @param password パスワード
     * @return 会員エンティティ
     */
    @Override
    public TempMemberInfoDto execute(String password) {

        // 入力チェック
        checkParameter(password);

        // 確認メール情報取得
        ConfirmMailEntity confirmMailEntity = getConfirmMail(password);

        // 会員情報を作成する
        MemberInfoEntity memberInfoEntity = createMemberInfoEntity(confirmMailEntity);

        // 会員ID重複チェック
        memberInfoDataCheckLogic.execute(memberInfoEntity);

        // 仮会員登録情報作成
        TempMemberInfoDto tempMemberInfoDto = ApplicationContextUtility.getBean(TempMemberInfoDto.class);
        tempMemberInfoDto.setMemberInfoEntity(memberInfoEntity);

        // 会員エンティティを返す
        return tempMemberInfoDto;
    }

    /**
     * パラメータチェック<br/>
     *
     * @param password メールパスワード
     */
    protected void checkParameter(String password) {
        ArgumentCheckUtil.assertNotEmpty("password", password);
    }

    /**
     * パスワードから有効な確認メール情報を取得する<br/>
     *
     * @param password パスワード
     * @return 確認メールエンティティ
     */
    protected ConfirmMailEntity getConfirmMail(String password) {

        // 確認メール情報取得
        ConfirmMailEntity confirmMailEntity =
                        confirmMailGetLogic.execute(password, HTypeConfirmMailType.TEMP_MEMBERINFO_REGIST);
        if (confirmMailEntity == null) {
            throwMessage(MSGCD_CONFIRMMAILENTITYDTO_NULL);
        }
        return confirmMailEntity;
    }

    /**
     * 確認メール情報から会員情報の作成<br/>
     *
     * @param confirmMailEntity 確認メールエンティティ
     * @return 会員エンティティ
     */
    protected MemberInfoEntity createMemberInfoEntity(ConfirmMailEntity confirmMailEntity) {
        MemberInfoEntity memberInfoEntity = ApplicationContextUtility.getBean(MemberInfoEntity.class);
        memberInfoEntity.setMemberInfoSeq(confirmMailEntity.getMemberInfoSeq());
        memberInfoEntity.setMemberInfoId(confirmMailEntity.getConfirmMail());
        memberInfoEntity.setMemberInfoMail(confirmMailEntity.getConfirmMail());
        Integer shopSeq = confirmMailEntity.getShopSeq();
        memberInfoEntity.setShopSeq(shopSeq);

        // 会員ユニークIDの作成・セット
        memberInfoEntity.setMemberInfoUniqueId(
                        memberInfoUtility.createShopUniqueId(shopSeq, memberInfoEntity.getMemberInfoMail()));
        return memberInfoEntity;
    }

}