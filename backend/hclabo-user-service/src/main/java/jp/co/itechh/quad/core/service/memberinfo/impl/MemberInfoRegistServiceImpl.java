/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.memberinfo.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.base.util.seasar.StringUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.constant.type.HTypeMemberInfoStatus;
import jp.co.itechh.quad.core.dao.memberinfo.MemberInfoDao;
import jp.co.itechh.quad.core.entity.memberinfo.MemberInfoEntity;
import jp.co.itechh.quad.core.entity.memberinfo.customeraddress.CustomerAddressBookEntity;
import jp.co.itechh.quad.core.logic.memberinfo.MemberInfoDataCheckLogic;
import jp.co.itechh.quad.core.logic.memberinfo.MemberInfoGetLogic;
import jp.co.itechh.quad.core.logic.memberinfo.MemberInfoRegistLogic;
import jp.co.itechh.quad.core.logic.memberinfo.MemberInfoUpdateLogic;
import jp.co.itechh.quad.core.logic.memberinfo.confirmmail.ConfirmMailDeleteLogic;
import jp.co.itechh.quad.core.logic.memberinfo.customeraddress.CustomerAddressBookRegistLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.memberinfo.MemberInfoRegistService;
import jp.co.itechh.quad.core.service.shop.mailmagazine.MailMagazineMemberRegistService;
import jp.co.itechh.quad.core.service.shop.mailmagazine.MailMagazineMemberRemoveService;
import jp.co.itechh.quad.core.utility.MemberInfoUtility;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 会員登録サービス実装<br/>
 *
 * @author natume
 * 　* @author kaneko　(itec) チケット対応　#2644　訪問者数にクローラが含まれている
 * @author Kaneko (itec) 2012/08/09 UtilからHelperへ変更　Util使用箇所をgetComponentで取得するように変更
 */
@Service
public class MemberInfoRegistServiceImpl extends AbstractShopService implements MemberInfoRegistService {

    /** 会員情報登録ロジック */
    private final MemberInfoRegistLogic memberInfoRegistLogic;

    /** 会員情報更新ロジック */
    private final MemberInfoUpdateLogic memberInfoUpdateLogic;

    /** 会員情報取得ロジック */
    private final MemberInfoGetLogic memberInfoGetLogic;

    /** 会員情報データチェックロジック */
    private final MemberInfoDataCheckLogic memberInfoDataCheckLogic;

    /** メルマガ購読者情報登録サービス */
    private final MailMagazineMemberRegistService mailMagazineMemberRegistService;

    /** メルマガ解除サービス */
    private final MailMagazineMemberRemoveService mailMagazineMemberRemoveService;

    /** 確認メール削除ロジック */
    private final ConfirmMailDeleteLogic confirmMailDeleteLogic;

    /** 会員SEQ取得Dao */
    private final MemberInfoDao memberInfoDao;

    /** 会員業務Utility */
    private final MemberInfoUtility memberInfoUtility;

    /** 顧客住所登録ロジック */
    private final CustomerAddressBookRegistLogic customerAddressBookRegistLogic;

    /** コンストラクタ */
    @Autowired
    public MemberInfoRegistServiceImpl(MemberInfoRegistLogic memberInfoRegistLogic,
                                       MemberInfoUpdateLogic memberInfoUpdateLogic,
                                       MemberInfoGetLogic memberInfoGetLogic,
                                       MemberInfoDataCheckLogic memberInfoDataCheckLogic,
                                       MailMagazineMemberRegistService mailMagazineMemberRegistService,
                                       MailMagazineMemberRemoveService mailMagazineMemberRemoveService,
                                       ConfirmMailDeleteLogic confirmMailDeleteLogic,
                                       MemberInfoDao memberInfoDao,
                                       MemberInfoUtility memberInfoUtility,
                                       CustomerAddressBookRegistLogic customerAddressBookRegistLogic) {
        this.memberInfoRegistLogic = memberInfoRegistLogic;
        this.memberInfoUpdateLogic = memberInfoUpdateLogic;
        this.memberInfoGetLogic = memberInfoGetLogic;
        this.memberInfoDataCheckLogic = memberInfoDataCheckLogic;
        this.mailMagazineMemberRegistService = mailMagazineMemberRegistService;
        this.mailMagazineMemberRemoveService = mailMagazineMemberRemoveService;
        this.confirmMailDeleteLogic = confirmMailDeleteLogic;
        this.memberInfoDao = memberInfoDao;
        this.memberInfoUtility = memberInfoUtility;
        this.customerAddressBookRegistLogic = customerAddressBookRegistLogic;
    }

    /**
     * ・会員ID重複チェック<br/>
     * ・パスワードの暗号化<br/>
     * ・会員情報の登録を行う <br/>
     * ・顧客の住所IDに紐づく住所情報の登録・更新を行う（管理サイトで住所情報を取得するための暫定対応）<br/>
     * ・メルマガ購読者の登録・更新を行う<br/>
     * ・確認メールの削除を行う<br/>
     *
     * @param accessUid                 端末識別情報
     * @param customerId                顧客ID
     * @param memberInfoEntity          会員エンティティ
     * @param mailmagazine              メルマガ希望フラグ
     * @param preMailmagazine           登録前メルマガ希望フラグ
     * @param confirmMailPassword       確認メールパスワード
     * @param customerAddressBookEntity 顧客住所エンティティ
     */
    @Override
    public void execute(String accessUid,
                        String customerId,
                        MemberInfoEntity memberInfoEntity,
                        boolean mailmagazine,
                        boolean preMailmagazine,
                        String confirmMailPassword,
                        CustomerAddressBookEntity customerAddressBookEntity) {
        clearErrorList();
        execute(accessUid, customerId, memberInfoEntity, mailmagazine, preMailmagazine, true,
                customerAddressBookEntity
               );

        // 確認メール削除ロジック実行（排他チェックは会員情報登録の中で行っているため、ここでは行わない）
        confirmMailDeleteLogic.execute(confirmMailPassword);
    }

    /**
     * ・会員ID重複チェック<br/>
     * ・パスワードの暗号化<br/>
     * ・会員情報の登録を行う <br/>
     * ・顧客の住所IDに紐づく住所情報の登録・更新を行う（管理サイトで住所情報を取得するための暫定対応）<br/>
     * ・メルマガ購読者の登録・更新を行う<br/>
     * ・カート商品のマージを行う（cartMergeがtrueの場合）<br/>
     *
     * @param accessUid                 端末識別情報
     * @param customerId                顧客ID
     * @param memberInfoEntity          会員エンティティ
     * @param mailmagazine              メルマガ希望フラグ
     * @param preMailmagazine           登録前メルマガ希望フラグ
     * @param cartMerge                 カートマージをするかどうか。true..する / false..
     * @param customerAddressBookEntity 顧客住所エンティティ
     */
    @Override
    public void execute(String accessUid,
                        String customerId,
                        MemberInfoEntity memberInfoEntity,
                        boolean mailmagazine,
                        boolean preMailmagazine,
                        boolean cartMerge,
                        CustomerAddressBookEntity customerAddressBookEntity) {

        Integer shopSeq = 1001;
        // 入力チェック
        checkParamater(accessUid, shopSeq, memberInfoEntity);

        // 会員情報の設定
        setMemberInfoEntity(customerId, memberInfoEntity);

        int result = 0;

        // 暫定会員を取得する
        MemberInfoEntity proMemberInfoEntity =
                        memberInfoGetLogic.executeForProvisional(memberInfoEntity.getMemberInfoUniqueId(),
                                                                 memberInfoEntity.getMemberInfoMail()
                                                                );

        if (proMemberInfoEntity != null) {

            // 暫定会員が存在するため情報を設定
            setMemberInfoEntity(memberInfoEntity, proMemberInfoEntity);

            // 本会員へ更新する
            result = memberInfoUpdateLogic.execute(memberInfoEntity);
            if (result == 0) {
                throwMessage(MSGCD_MEMBERINFO_REGIST_FAIL);
            }
        } else {

            // 会員情報チェック
            checkMemberInfo(memberInfoEntity);

            // 会員情報登録
            result = memberInfoRegistLogic.execute(memberInfoEntity);
            if (result == 0) {
                throwMessage(MSGCD_MEMBERINFO_REGIST_FAIL);
            }
        }

        // TODO 顧客の住所IDに紐づく住所情報の登録・更新を行う（管理サイトで住所情報を取得するための暫定対応）
        //  会員SEQを設定（それ以外の項目はAPIのヘルパークラスで設定されている）　※本来ならCustomerController#registで設定される想定（ゲストとアクセスUID（山内さん案）が採用された場合）
        customerAddressBookEntity.setCustomerId(memberInfoEntity.getMemberInfoSeq().toString());
        int resultAddressRegist = this.customerAddressBookRegistLogic.execute(customerAddressBookEntity);
        if (resultAddressRegist == 0) {
            throwMessage(MSGCD_MEMBERINFO_REGIST_FAIL);
        }

        // 購読フラグが、変更後がON かつ 変更前がOFFの場合のみ、メルマガ登録と更新を実施する
        if (mailmagazine && !preMailmagazine) {
            mailMagazineMemberRegistService.execute(
                            memberInfoEntity.getMemberInfoMail(), memberInfoEntity.getMemberInfoSeq());
        }

        // 購読フラグが、変更後がOFF かつ 変更前がONの場合のみ、メルマガ解除を実施する
        if (!mailmagazine && preMailmagazine) {
            mailMagazineMemberRemoveService.execute(
                            memberInfoEntity.getMemberInfoMail(), memberInfoEntity.getMemberInfoSeq());
        }
    }

    /**
     * 会員情報データチェック<br/>
     *
     * @param memberInfoEntity 会員エンティティ
     */
    protected void checkMemberInfo(MemberInfoEntity memberInfoEntity) {
        memberInfoDataCheckLogic.execute(memberInfoEntity);
    }

    /**
     * 入力チェック<br/>
     *
     * @param accessUid        端末識別情報
     * @param shopSeq          ショップSEQ
     * @param memberInfoEntity 会員情報エンティティ
     */
    protected void checkParamater(String accessUid, Integer shopSeq, MemberInfoEntity memberInfoEntity) {
        ArgumentCheckUtil.assertGreaterThanZero("commonInfo.shopSeq", shopSeq);
        ArgumentCheckUtil.assertNotEmpty("commonInfo.accessUid", accessUid);
        ArgumentCheckUtil.assertNotNull("memberInfoEntity", memberInfoEntity);
    }

    /**
     * 本会員情報のセット<br/>
     *
     * @param customerId       顧客ID
     * @param memberInfoEntity 会員エンティティ
     */
    protected void setMemberInfoEntity(String customerId, MemberInfoEntity memberInfoEntity) {

        // 会員状態：「入会」に設定
        if (memberInfoEntity.getMemberInfoStatus() == null) {
            memberInfoEntity.setMemberInfoStatus(HTypeMemberInfoStatus.ADMISSION);
        }

        // 会員ユニークIDの作成・セット
        Integer shopSeq = 1001;

        // ショップSEQ
        memberInfoEntity.setShopSeq(shopSeq);

        // 会員SEQ設定
        Integer memberInfoSeq = Integer.parseInt(customerId);
        if (StringUtils.isEmpty(customerId)) {
            // 念のための救済措置
            // 2022年9月末までの業務仕様としては、customerIdは呼び出し元で必ず採番されている前提
            // ※フロントエンド側で事前採番されている想定
            memberInfoSeq = memberInfoDao.getMemberInfoSeqNextVal();
        }
        memberInfoEntity.setMemberInfoSeq(memberInfoSeq);

        // ユニークID
        if (HTypeMemberInfoStatus.ADMISSION.equals(memberInfoEntity.getMemberInfoStatus())) {
            memberInfoEntity.setMemberInfoUniqueId(
                            memberInfoUtility.createShopUniqueId(shopSeq, memberInfoEntity.getMemberInfoMail()));
        }

        // SpringSecurity準拠の方式で暗号化
        PasswordEncoder encoder = ApplicationContextUtility.getBean(PasswordEncoder.class);
        // パスワード暗号化
        String encryptedPassword = encoder.encode(memberInfoEntity.getMemberInfoPassword());
        memberInfoEntity.setMemberInfoPassword(encryptedPassword);

        // 住所ID
        memberInfoEntity.setMemberInfoAddressId(memberInfoEntity.getMemberInfoAddressId());

        // 日付関連Helper取得
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

        // 入会日
        if (StringUtil.isEmpty(memberInfoEntity.getAdmissionYmd())) {
            memberInfoEntity.setAdmissionYmd(dateUtility.getCurrentYmd());
        }
    }

    /**
     * 本会員情報のセット<br/>
     * 暫定会員の情報をセットする
     *
     * @param targetMemberInfoEntity 会員エンティティ
     * @param proMemberInfoEntity    会員エンティティ
     */
    protected void setMemberInfoEntity(MemberInfoEntity targetMemberInfoEntity, MemberInfoEntity proMemberInfoEntity) {

        // 会員状態に差分がある場合、変更する
        if (targetMemberInfoEntity.getMemberInfoStatus() != proMemberInfoEntity.getMemberInfoStatus()) {
            targetMemberInfoEntity.setMemberInfoStatus(HTypeMemberInfoStatus.ADMISSION);
        }

        // 会員SEQを暫定会員登録時の設定値に、変更する
        targetMemberInfoEntity.setMemberInfoSeq(proMemberInfoEntity.getMemberInfoSeq());

        // 必須項目をそのまま再設定
        targetMemberInfoEntity.setRegistTime(proMemberInfoEntity.getRegistTime());
    }
}