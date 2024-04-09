/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.memberinfo.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.base.util.common.CopyUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.constant.type.HTypeMemberInfoStatus;
import jp.co.itechh.quad.core.dto.memberinfo.MemberInfoDetailsDto;
import jp.co.itechh.quad.core.entity.memberinfo.MemberInfoEntity;
import jp.co.itechh.quad.core.logic.memberinfo.MemberInfoDataCheckLogic;
import jp.co.itechh.quad.core.logic.memberinfo.MemberInfoGetLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.memberinfo.MemberInfoDetailsUpdateService;
import jp.co.itechh.quad.core.service.memberinfo.MemberInfoUpdateService;
import jp.co.itechh.quad.core.utility.MemberInfoUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

import static jp.co.itechh.quad.core.constant.type.HTypeMemberInfoStatus.ADMISSION;
import static jp.co.itechh.quad.core.constant.type.HTypeMemberInfoStatus.REMOVE;

/**
 * 会員詳細情報更新サービス実装クラス<br/>
 * 管理画面で使う事を想定としている。<br/>
 * <pre>
 * ・会員情報
 * ・メルマガ購読者情報
 * </pre>
 *
 * @author negishi
 * @author tomo (itec) 2012/01/23 チケット #2722 対応 入会日・退会日の自動補完処理を削除
 * @author Kaneko (itec) 2012/08/09 UtilからHelperへ変更　Util使用箇所をgetComponentで取得するように変更
 *
 */
@Service
public class MemberInfoDetailsUpdateServiceImpl extends AbstractShopService implements MemberInfoDetailsUpdateService {

    /** 会員情報取得ロジック */
    private final MemberInfoGetLogic memberInfoGetLogic;

    /** 会員情報データチェックロジック */
    private final MemberInfoDataCheckLogic memberInfoDataCheckLogic;

    /** 会員情報更新サービス */
    private final MemberInfoUpdateService memberInfoUpdateService;

    @Autowired
    public MemberInfoDetailsUpdateServiceImpl(MemberInfoGetLogic memberInfoGetLogic,
                                              MemberInfoDataCheckLogic memberInfoDataCheckLogic,
                                              MemberInfoUpdateService memberInfoUpdateService) {
        this.memberInfoGetLogic = memberInfoGetLogic;
        this.memberInfoDataCheckLogic = memberInfoDataCheckLogic;
        this.memberInfoUpdateService = memberInfoUpdateService;
    }

    /**
     * サービス実行<br/>
     * <pre>
     * 会員状態を更新された場合には以下の処理を行います。
     * ①入会⇒退会 に更新された場合
     * 　　・お気に入り、アドレス帳の削除を行う。
     * ②退会⇒入会 に更新された場合
     * 　　・会員重複チェックを行う。
     * </pre>
     *
     * @param memberInfoDetailsDto 会員詳細DTO
     * @return 更新件数
     */
    @Override
    public int execute(MemberInfoDetailsDto memberInfoDetailsDto) {
        // パラメータチェック
        checkParameter(memberInfoDetailsDto);

        // 現在日時取得
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);
        Timestamp currentTime = dateUtility.getCurrentTime();

        // 更新に使用する会員情報エンティティ
        MemberInfoEntity memberInfoEntity = CopyUtil.deepCopy(memberInfoDetailsDto.getMemberInfoEntity());

        // 修正対象の会員の現在の会員情報を取得
        MemberInfoEntity memberInfoEntityBase = memberInfoGetLogic.execute(memberInfoEntity.getMemberInfoSeq());
        // 修正対象の会員の現在の会員状態を取得
        HTypeMemberInfoStatus memberInfoStatusBase = memberInfoEntityBase.getMemberInfoStatus();

        HTypeMemberInfoStatus memberInfoStatus = memberInfoEntity.getMemberInfoStatus();
        // 入会⇒退会の場合
        if (ADMISSION.equals(memberInfoStatusBase) && REMOVE.equals(memberInfoStatus)) {
            // 会員一意制約用IDを設定
            memberInfoEntity.setMemberInfoUniqueId(null);

            // 退会⇒入会の場合
        } else if (REMOVE.equals(memberInfoStatusBase) && ADMISSION.equals(memberInfoStatus)) {
            // 会員一意制約用IDを設定
            memberInfoEntity.setMemberInfoUniqueId(createShopUniqueId(memberInfoEntity));
            // 会員情報データチェック
            checkMemberInfo(memberInfoEntity);
        }

        // 会員状態が「退会」の場合、ユニークIDにnullをセット
        // メールチェック時にセットされてしまってるようなので、ここで初期化する
        if (memberInfoStatus == REMOVE) {
            memberInfoEntity.setMemberInfoUniqueId(null);
        }

        // パスワードの暗号化
        encryptMemberInfoPassword(memberInfoEntity, memberInfoEntityBase);

        // 会員情報更新サービス実行。更新失敗したらここ例外投げられますよ。
        int processeCount = memberInfoUpdateService.execute(memberInfoEntity, currentTime);

        return processeCount;
    }

    /**
     * パラメータチェック
     *
     * @param memberInfoDetailsDto 会員詳細DTO
     */
    protected void checkParameter(MemberInfoDetailsDto memberInfoDetailsDto) {
        ArgumentCheckUtil.assertNotNull("memberInfoDetailsDto", memberInfoDetailsDto);
        ArgumentCheckUtil.assertNotNull("memberInfoEntity", memberInfoDetailsDto.getMemberInfoEntity());
    }

    /**
     * 会員一意制約用IDを作成
     *
     * @param memberInfoEntity 会員情報エンティティ
     * @return 会員一意制約用ID
     */
    protected String createShopUniqueId(MemberInfoEntity memberInfoEntity) {
        // 会員業務Helper取得
        MemberInfoUtility memberInfoUtility = ApplicationContextUtility.getBean(MemberInfoUtility.class);

        return memberInfoUtility.createShopUniqueId(
                        memberInfoEntity.getShopSeq(), memberInfoEntity.getMemberInfoMail());
    }

    /**
     * 会員情報データチェック
     *
     * @param memberInfoEntity 会員エンティティ
     */
    protected void checkMemberInfo(MemberInfoEntity memberInfoEntity) {
        memberInfoDataCheckLogic.execute(memberInfoEntity);
    }

    /**
     * パスワードの暗号化<br/>
     * パスワードが入力されている場合のみ行います。
     *
     * @param memberInfoEntity 会員情報エンティティ
     * @param memberInfoEntityBase 修正対象の会員の現在の会員情報エンティティ
     */
    protected void encryptMemberInfoPassword(MemberInfoEntity memberInfoEntity, MemberInfoEntity memberInfoEntityBase) {
        String password = memberInfoEntity.getMemberInfoPassword();
        // パスワードが入力されていない場合は、元々のパスワードを設定
        if (password == null) {
            memberInfoEntity.setMemberInfoPassword(memberInfoEntityBase.getMemberInfoPassword());
            // パスワードが入力されていれば暗号化
        } else {
            // SHA-256ハッシュ値計算用文字列生成
            //            String sha256HashText = memberInfoUtility.createSHA256HashValue(memberInfoEntityBase.getShopSeq(), memberInfoEntityBase.getMemberInfoSeq(), password);
            //            String encryptedPassword = cryptoHelper.encryptMemberPasswordforSHA256(sha256HashText);
            //            memberInfoEntity.setMemberInfoPassword(encryptedPassword);
        }
    }
}