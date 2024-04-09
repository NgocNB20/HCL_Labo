/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.mailmagazine.presentation.api;

import io.swagger.annotations.ApiParam;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.constant.type.HTypeMailTemplateType;
import jp.co.itechh.quad.core.constant.type.HTypeMemberInfoStatus;
import jp.co.itechh.quad.core.entity.mailmagazine.MailMagazineMemberEntity;
import jp.co.itechh.quad.core.entity.memberinfo.MemberInfoEntity;
import jp.co.itechh.quad.core.logic.memberinfo.MemberInfoGetLogic;
import jp.co.itechh.quad.core.logic.memberinfo.MemberInfoProvisionalRegistLogic;
import jp.co.itechh.quad.core.logic.shop.mailmagazine.MailMagazineMemberGetLogic;
import jp.co.itechh.quad.core.logic.shop.mailmagazine.MailMagazineMemberListGetLogic;
import jp.co.itechh.quad.core.service.process.AsyncService;
import jp.co.itechh.quad.core.service.shop.mailmagazine.MailMagazineMemberRegistService;
import jp.co.itechh.quad.core.service.shop.mailmagazine.MailMagazineMemberRemoveService;
import jp.co.itechh.quad.core.utility.AsyncTaskUtility;
import jp.co.itechh.quad.core.web.AbstractController;
import jp.co.itechh.quad.mailmagazine.presentation.api.param.MailmagazineDeleteRequest;
import jp.co.itechh.quad.mailmagazine.presentation.api.param.MailmagazineMemberListResponse;
import jp.co.itechh.quad.mailmagazine.presentation.api.param.MailmagazineMemberResponse;
import jp.co.itechh.quad.mailmagazine.presentation.api.param.MailmagazineRegistRequest;
import jp.co.itechh.quad.mailmagazine.presentation.api.param.PageInfoRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.NotificationSubApi;
import jp.co.itechh.quad.notificationsub.presentation.api.param.MailMagazineProcessCompleteRequest;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 * メルマガ Controller
 *
 * @author PHAM QUANG DIEU (VJP)
 */
@RestController
public class MailMagazineController extends AbstractController implements UsersApi {

    /** メルマガ会員 Helper */
    private final MailMagazineHelper mailmagazineHelper;

    /** 会員メルマガ取得logic */
    private final MailMagazineMemberGetLogic mailMagazineMemberGetLogic;

    /** メルマガ会員リスト取得logic<br/> */
    private final MailMagazineMemberListGetLogic mailMagazineMemberListGetLogic;

    /** 会員情報取得logic */
    private final MemberInfoGetLogic memberInfoGetLogic;

    /** 暫定会員登録logic */
    private final MemberInfoProvisionalRegistLogic memberInfoProvisionalRegistLogic;

    /** メルマガ購読者登録サービス */
    private final MailMagazineMemberRegistService mailMagazineMemberRegistService;

    /** メルマガ購読者解除サービス */
    private final MailMagazineMemberRemoveService mailMagazineMemberRemoveService;

    /** 通知サブドメインAPI */
    private final NotificationSubApi notificationSubApi;

    /** 非同期処理サービス */
    private final AsyncService asyncService;

    /** 解除処理を行わなかった場合 */
    private static final String MSGCD_REMOVE_PROCESS_NONE = "AZX000403";

    /** エラーメッセージコード：不正操作 */
    private static final String MSGCD_ILLEGAL_OPERATION = "MAILMAGAZINE-REGIST-001-";

    /** ショップSEQ */
    private static final int SHOP_SEQ = 1001;

    /**
     * コンストラクタ
     *
     * @param mailmagazineHelper                    メルマガ Helper
     * @param mailMagazineMemberGetLogic            メルマガ購読者情報取得ロジック
     * @param mailMagazineMemberListGetLogic        メルマガ購読者情報リスト取得ロジック
     * @param memberInfoGetLogic                    会員情報取得ロジック
     * @param memberInfoProvisionalRegistLogic      暫定会員情報登録
     * @param mailMagazineMemberRegistService       メルマガ購読者登録サービス
     * @param mailMagazineMemberRemoveService       メルマガ購読者解除サービス
     * @param notificationSubApi                    通知サブドメインAPI
     * @param asyncService                          非同期用共通サービス
     */
    @Autowired
    public MailMagazineController(MailMagazineHelper mailmagazineHelper,
                                  MailMagazineMemberGetLogic mailMagazineMemberGetLogic,
                                  MailMagazineMemberListGetLogic mailMagazineMemberListGetLogic,
                                  MemberInfoGetLogic memberInfoGetLogic,
                                  MemberInfoProvisionalRegistLogic memberInfoProvisionalRegistLogic,
                                  MailMagazineMemberRegistService mailMagazineMemberRegistService,
                                  MailMagazineMemberRemoveService mailMagazineMemberRemoveService,
                                  NotificationSubApi notificationSubApi,
                                  AsyncService asyncService) {
        this.mailmagazineHelper = mailmagazineHelper;
        this.mailMagazineMemberGetLogic = mailMagazineMemberGetLogic;
        this.mailMagazineMemberListGetLogic = mailMagazineMemberListGetLogic;
        this.memberInfoGetLogic = memberInfoGetLogic;
        this.memberInfoProvisionalRegistLogic = memberInfoProvisionalRegistLogic;
        this.mailMagazineMemberRegistService = mailMagazineMemberRegistService;
        this.mailMagazineMemberRemoveService = mailMagazineMemberRemoveService;
        this.notificationSubApi = notificationSubApi;
        this.asyncService = asyncService;
    }

    /**
     * GET /promotions/mailmagazines/{memberinfoSeq} : メルマガ会員取得
     * メルマガ会員取得
     *
     * @param memberinfoSeq 会員SEQ (required)
     * @return メルマガ会員レスポンス (status code 200)
     * or システムエラー (status code 500)
     */
    @Override
    public ResponseEntity<MailmagazineMemberResponse> getByMemberinfoSeq(
                    @ApiParam(value = "会員SEQ", required = true) @PathVariable("memberinfoSeq") Integer memberinfoSeq) {

        // メルマガ会員情報を取得
        MailMagazineMemberEntity mailMagazineMemberEntity = mailMagazineMemberGetLogic.execute(memberinfoSeq);
        MailmagazineMemberResponse mailmagazineMemberResponse =
                        mailmagazineHelper.toMailmagazineMemberResponse(mailMagazineMemberEntity);

        return new ResponseEntity<>(mailmagazineMemberResponse, HttpStatus.OK);
    }

    /**
     * GET /users/mailmagazines/mail/{memberInfoMail} : 会員メールによるメルマガ会員取得
     * 会員メールによるメルマガ会員取得
     *
     * @param memberInfoMail メールアドレス (required)
     * @return メルマガ会員一覧レスポンス (status code 200)
     * or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     * or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     * or システムエラー (status code 500)
     */
    @Override
    public ResponseEntity<MailmagazineMemberListResponse> getByMemberInfoMail(String memberInfoMail) {
        // 一意制約用メールアドレス
        String uniqueMail = String.valueOf(SHOP_SEQ).concat(memberInfoMail);

        // メルマガ会員情報の一覧取得
        List<MailMagazineMemberEntity> mailMagazineMemberEntityList =
                        mailMagazineMemberListGetLogic.execute(uniqueMail);
        MailmagazineMemberListResponse mailmagazineMemberListResponse =
                        mailmagazineHelper.toMailmagazineMemberListResponse(mailMagazineMemberEntityList);

        return new ResponseEntity<>(mailmagazineMemberListResponse, HttpStatus.OK);
    }

    /**
     * GET /promotions/mailmagazines : メルマガ会員一覧取得
     * メルマガ会員一覧取得
     *
     * @param pageInfoRequest ページ情報リクエスト (optional)
     * @return メルマガ会員一覧レスポンス (status code 200)
     * or システムエラー (status code 500)
     */
    @Override
    public ResponseEntity<MailmagazineMemberListResponse> get(
                    @ApiParam("ページ情報リクエスト") @Valid PageInfoRequest pageInfoRequest) {

        // メルマガ会員情報を取得
        List<MailMagazineMemberEntity> mailMagazineMemberEntityList = mailMagazineMemberListGetLogic.execute();
        MailmagazineMemberListResponse mailmagazineMemberResponse =
                        mailmagazineHelper.toMailmagazineMemberListResponse(mailMagazineMemberEntityList);

        return new ResponseEntity<>(mailmagazineMemberResponse, HttpStatus.OK);
    }

    /**
     * POST /promotions/mailmagazines : メルマガ登録
     * メルマガ登録
     *
     * @param mailmagazineRegistRequest メルマガ会員登録リクエスト (required)
     * @return メルマガ会員レスポンス (status code 200)
     * or システムエラー (status code 500)
     */
    @Override
    public ResponseEntity<MailmagazineMemberResponse> regist(
                    @ApiParam(value = "メルマガ会員登録リクエスト", required = true) @Valid @RequestBody
                                    MailmagazineRegistRequest mailmagazineRegistRequest) {

        // 登録用メールアドレス
        String mailAddress = mailmagazineRegistRequest.getMailAddress();

        // 会員SEQ
        Integer memberinfoSeq = null;

        // メールアドレスとステータスで会員情報を取得
        MemberInfoEntity entityDb =
                        memberInfoGetLogic.executeByMailStatus(mailAddress, HTypeMemberInfoStatus.ADMISSION);

        // 会員情報が無い場合、暫定会員を登録する
        if (entityDb == null) {
            // 暫定会員の設定
            MemberInfoEntity memberInfoEntity = ApplicationContextUtility.getBean(MemberInfoEntity.class);
            memberInfoEntity.setMemberInfoId(mailAddress);
            memberInfoEntity.setMemberInfoMail(mailAddress);

            // 暫定会員登録
            memberInfoProvisionalRegistLogic.execute(memberInfoEntity);

            // 上記で登録した、会員エンティティの会員SEQをセット
            memberinfoSeq = memberInfoEntity.getMemberInfoSeq();
        } else {
            // 取得した本会員または暫定会員の会員SEQをセット
            memberinfoSeq = entityDb.getMemberInfoSeq();
        }

        if (Objects.isNull(memberinfoSeq)) {
            throwMessage(MSGCD_ILLEGAL_OPERATION);
        }

        // メルマガ登録処理
        mailMagazineMemberRegistService.execute(mailAddress, memberinfoSeq);

        // メルマガ登録完了メールを送信（非同期処理）
        // パラメータ設定
        MailMagazineProcessCompleteRequest mailMagazineProcessCompleteRequest =
                        new MailMagazineProcessCompleteRequest();
        mailMagazineProcessCompleteRequest.setMemberInfoSeq(memberinfoSeq);
        mailMagazineProcessCompleteRequest.setMail(mailAddress);
        mailMagazineProcessCompleteRequest.setMailTemplateType(
                        HTypeMailTemplateType.MAILMAGAZINE_REGISTRATION.getValue());

        Object[] args = new Object[] {mailMagazineProcessCompleteRequest};
        Class<?>[] argsClass = new Class<?>[] {MailMagazineProcessCompleteRequest.class};
        AsyncTaskUtility.executeAfterTransactionCommit(() -> {
            asyncService.asyncService(notificationSubApi, "mailMagazineProcessComplete", args, argsClass);
        });
        // メルマガ会員情報を取得
        MailMagazineMemberEntity mailMagazineMemberEntity = mailMagazineMemberGetLogic.execute(memberinfoSeq);

        MailmagazineMemberResponse mailmagazineMemberResponse =
                        mailmagazineHelper.toMailmagazineMemberResponse(mailMagazineMemberEntity);

        return new ResponseEntity<>(mailmagazineMemberResponse, HttpStatus.OK);
    }

    /**
     * DELETE /promotions/mailmagazines : メルマガ解除
     * メルマガ解除
     *
     * @param mailmagazineDeleteRequest メルマガ会員登録リクエスト (required)
     * @return 成功 (status code 200)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<Void> delete(@Valid MailmagazineDeleteRequest mailmagazineDeleteRequest) {

        // メールアドレスで会員情報を取得
        MemberInfoEntity memberInfoEntity =
                        memberInfoGetLogic.executeByMailStatus(mailmagazineDeleteRequest.getMailAddress(),
                                                               HTypeMemberInfoStatus.ADMISSION
                                                              );

        if (ObjectUtils.isEmpty(memberInfoEntity)) {
            throwMessage(MSGCD_REMOVE_PROCESS_NONE);
        }

        // メルマガ購読者解除サービス実行
        boolean removeFlg = mailMagazineMemberRemoveService.execute(mailmagazineDeleteRequest.getMailAddress(),
                                                                    memberInfoEntity.getMemberInfoSeq()
                                                                   );

        // 解除していない場合 メッセージ表示
        if (!removeFlg) {
            throwMessage(MSGCD_REMOVE_PROCESS_NONE);
        }

        // メルマガ解除完了メールを送信（非同期処理）
        // パラメータ設定

        MailMagazineProcessCompleteRequest mailMagazineProcessCompleteRequest =
                        new MailMagazineProcessCompleteRequest();
        mailMagazineProcessCompleteRequest.setMail(mailmagazineDeleteRequest.getMailAddress());
        mailMagazineProcessCompleteRequest.setMemberInfoSeq(memberInfoEntity.getMemberInfoSeq());
        mailMagazineProcessCompleteRequest.setMailTemplateType(
                        HTypeMailTemplateType.MAILMAGAZINE_UNREGISTRATION.getValue());

        Object[] args = new Object[] {mailMagazineProcessCompleteRequest};
        Class<?>[] argsClass = new Class<?>[] {MailMagazineProcessCompleteRequest.class};
        AsyncTaskUtility.executeAfterTransactionCommit(() -> {
            asyncService.asyncService(notificationSubApi, "mailMagazineProcessComplete", args, argsClass);
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }
}