/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.customer.presentation.api;

import io.swagger.annotations.ApiParam;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.constant.type.HTypeMailTemplateType;
import jp.co.itechh.quad.core.dao.memberinfo.MemberInfoDao;
import jp.co.itechh.quad.core.dto.csv.CsvDownloadOptionDto;
import jp.co.itechh.quad.core.dto.csv.CsvOptionDto;
import jp.co.itechh.quad.core.dto.csv.OptionContentDto;
import jp.co.itechh.quad.core.dto.memberinfo.MemberCsvDto;
import jp.co.itechh.quad.core.dto.memberinfo.MemberInfoDetailsDto;
import jp.co.itechh.quad.core.dto.memberinfo.MemberInfoForBackDto;
import jp.co.itechh.quad.core.dto.memberinfo.MemberInfoSearchForDaoConditionDto;
import jp.co.itechh.quad.core.entity.memberinfo.MemberInfoEntity;
import jp.co.itechh.quad.core.entity.memberinfo.csvoption.CsvOptionEntity;
import jp.co.itechh.quad.core.entity.memberinfo.customeraddress.CustomerAddressBookEntity;
import jp.co.itechh.quad.core.handler.csv.CsvDownloadHandler;
import jp.co.itechh.quad.core.logic.memberinfo.MemberInfoAllCsvDownloadLogic;
import jp.co.itechh.quad.core.logic.memberinfo.MemberInfoCsvDownloadLogic;
import jp.co.itechh.quad.core.logic.memberinfo.customeraddress.CustomerAddressBookRegistLogic;
import jp.co.itechh.quad.core.service.memberinfo.MemberInfoDetailsGetService;
import jp.co.itechh.quad.core.service.memberinfo.MemberInfoGetService;
import jp.co.itechh.quad.core.service.memberinfo.MemberInfoMailUpdateSendMailService;
import jp.co.itechh.quad.core.service.memberinfo.MemberInfoMailUpdateService;
import jp.co.itechh.quad.core.service.memberinfo.MemberInfoRegistService;
import jp.co.itechh.quad.core.service.memberinfo.MemberInfoRemoveService;
import jp.co.itechh.quad.core.service.memberinfo.MemberInfoResultSearchService;
import jp.co.itechh.quad.core.service.memberinfo.MemberInfoUpdateMailGetService;
import jp.co.itechh.quad.core.service.memberinfo.MemberInfoUpdateService;
import jp.co.itechh.quad.core.service.memberinfo.csvoption.CsvOptionGetService;
import jp.co.itechh.quad.core.service.memberinfo.csvoption.CsvOptionUpdateService;
import jp.co.itechh.quad.core.service.memberinfo.password.MemberInfoPasswordUpdateService;
import jp.co.itechh.quad.core.service.process.AsyncService;
import jp.co.itechh.quad.core.service.shop.mailmagazine.MailMagazineMemberRegistService;
import jp.co.itechh.quad.core.service.shop.mailmagazine.MailMagazineMemberRemoveService;
import jp.co.itechh.quad.core.util.common.CsvOptionUtil;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.core.utility.AsyncTaskUtility;
import jp.co.itechh.quad.core.web.AbstractController;
import jp.co.itechh.quad.core.web.PageInfoModule;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerAddressRegistRequest;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerCsvGetOptionRequest;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerCsvOptionListResponse;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerCsvOptionResponse;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerCsvOptionUpdateRequest;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerDeleteRequest;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerListCsvGetRequest;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerListGetRequest;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerListResponse;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerMailAddressUpdateRequest;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerMailAddressUpdateSendMailRequest;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerPasswordResetUpdateRequest;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerPasswordUpdateRequest;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerRegistRequest;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerResponse;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerUpdateRequest;
import jp.co.itechh.quad.customer.presentation.api.param.PageInfoRequest;
import jp.co.itechh.quad.customer.presentation.api.param.PageInfoResponse;
import jp.co.itechh.quad.customer.presentation.api.param.TargetMembersListRequest;
import jp.co.itechh.quad.customer.presentation.api.param.TargetMembersListResponse;
import jp.co.itechh.quad.notificationsub.presentation.api.NotificationSubApi;
import jp.co.itechh.quad.notificationsub.presentation.api.param.MailMagazineProcessCompleteRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.MemberinfoProcessCompleteRequest;
import lombok.SneakyThrows;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.thymeleaf.util.ListUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * 会員スエンドポイント　Controller
 *
 * @author PHAM QUANG DIEU (VJP)
 */
@RestController
public class CustomerController extends AbstractController implements UsersApi {

    /** 会員スエンドポイント Helper */
    private final CustomerHelper customerHelper;

    /** 会員詳細取得サービス */
    private final MemberInfoDetailsGetService memberInfoDetailsGetService;

    /** 会員検索サービス */
    private final MemberInfoResultSearchService memberInfoResultSearchService;

    /** 会員登録サービス */
    private final MemberInfoRegistService memberInfoRegistService;

    /** 会員情報更新サービス */
    private final MemberInfoUpdateService memberInfoUpdateService;

    /** メルマガ購読者登録サービス */
    private final MailMagazineMemberRegistService mailMagazineMemberRegistService;

    /** メルマガ購読者解除サービス */
    private final MailMagazineMemberRemoveService mailMagazineMemberRemoveService;

    /** 仮会員情報取得サービス */
    private final MemberInfoGetService memberInfoGetService;

    /** 非同期処理サービス */
    private final AsyncService asyncService;

    /** 会員メールアドレス変更サービス */
    private final MemberInfoMailUpdateService memberInfoMailUpdateService;

    /** 会員パスワード変更サービス */
    private final MemberInfoPasswordUpdateService memberInfoPasswordUpdateService;

    /** 会員退会更新サービス */
    private final MemberInfoRemoveService memberInfoRemoveService;

    /** 会員データCSV出力ロジック */
    private final MemberInfoCsvDownloadLogic memberInfoCsvDownloadLogic;

    /** 会員CSVダウンロードロジック */
    private final MemberInfoAllCsvDownloadLogic memberInfoAllCsvDownloadLogic;

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);

    /** メールアドレス変更サービス */
    private final MemberInfoMailUpdateSendMailService memberInfoMailUpdateSendMailService;

    /** 会員メールアドレス変更サービス */
    private final MemberInfoUpdateMailGetService memberInfoUpdateMailGetService;

    /** 顧客住所登録ロジック */
    private final CustomerAddressBookRegistLogic customerAddressBookRegistLogic;

    /** 不正遷移 */
    protected static final String MSGCD_REFERER_FAIL_CONFIRM = "AMX000401";

    /** 会員DAO */
    private final MemberInfoDao memberInfoDao;

    /** 通知サブドメインAPI */
    private final NotificationSubApi notificationSubApi;

    /** csvOptionGetService */
    private final CsvOptionGetService csvOptionGetService;

    /** csvOptionUpdateService */
    private final CsvOptionUpdateService csvOptionUpdateService;

    private static final String MSGCD_MEMBERINFO_CSVOPTION_UPDATE_ERROR = "CSV-OPTION-001-";

    /** コンストラクタ */
    public CustomerController(CustomerHelper customerHelper,
                              MemberInfoDetailsGetService memberInfoDetailsGetService,
                              MemberInfoResultSearchService memberInfoResultSearchService,
                              MemberInfoPasswordUpdateService memberInfoPasswordUpdateService,
                              MemberInfoRemoveService memberInfoRemoveService,
                              MemberInfoRegistService memberInfoRegistService,
                              MemberInfoUpdateService memberInfoUpdateService,
                              MailMagazineMemberRegistService mailMagazineMemberRegistService,
                              MailMagazineMemberRemoveService mailMagazineMemberRemoveService,
                              AsyncService asyncService,
                              MemberInfoGetService memberInfoGetService,
                              MemberInfoMailUpdateService memberInfoMailUpdateService,
                              MemberInfoCsvDownloadLogic memberInfoCsvDownloadLogic,
                              MemberInfoAllCsvDownloadLogic memberInfoAllCsvDownloadLogic,
                              MemberInfoMailUpdateSendMailService memberInfoMailUpdateSendMailService,
                              MemberInfoUpdateMailGetService memberInfoUpdateMailGetService,
                              CustomerAddressBookRegistLogic customerAddressBookRegistLogic,
                              MemberInfoDao memberInfoDao,
                              NotificationSubApi notificationSubApi,
                              CsvOptionGetService csvOptionGetService,
                              CsvOptionUpdateService csvOptionUpdateService) {
        this.customerHelper = customerHelper;
        this.memberInfoDetailsGetService = memberInfoDetailsGetService;
        this.memberInfoResultSearchService = memberInfoResultSearchService;
        this.memberInfoPasswordUpdateService = memberInfoPasswordUpdateService;
        this.memberInfoRemoveService = memberInfoRemoveService;
        this.memberInfoRegistService = memberInfoRegistService;
        this.memberInfoUpdateService = memberInfoUpdateService;
        this.mailMagazineMemberRegistService = mailMagazineMemberRegistService;
        this.mailMagazineMemberRemoveService = mailMagazineMemberRemoveService;
        this.asyncService = asyncService;
        this.memberInfoGetService = memberInfoGetService;
        this.memberInfoMailUpdateService = memberInfoMailUpdateService;
        this.memberInfoCsvDownloadLogic = memberInfoCsvDownloadLogic;
        this.memberInfoAllCsvDownloadLogic = memberInfoAllCsvDownloadLogic;
        this.memberInfoMailUpdateSendMailService = memberInfoMailUpdateSendMailService;
        this.memberInfoUpdateMailGetService = memberInfoUpdateMailGetService;
        this.customerAddressBookRegistLogic = customerAddressBookRegistLogic;
        this.memberInfoDao = memberInfoDao;
        this.notificationSubApi = notificationSubApi;
        this.csvOptionGetService = csvOptionGetService;
        this.csvOptionUpdateService = csvOptionUpdateService;
    }

    /**
     * GET /users/customers/{memberinfoSeq} : 会員情報取得
     * 会員情報取得
     *
     * @param memberinfoSeq 会員情報SEQ (required)
     * @return 会員レスポンス (status code 200) or その他エラー (status code 500)
     */
    @Override
    public ResponseEntity<CustomerResponse> getByMemberinfoSeq(Integer memberinfoSeq) {
        MemberInfoDetailsDto memberInfoDetailsDto = memberInfoDetailsGetService.execute(memberinfoSeq);

        CustomerResponse customerResponse = customerHelper.convertDetailsDtoToCustomerResponse(memberInfoDetailsDto);

        return new ResponseEntity<>(customerResponse, HttpStatus.OK);
    }

    /**
     * GET /users/customers : 会員一覧取得
     * 会員一覧取得
     *
     * @param customerListGetRequest 会員一覧取得リクエスト (required)
     * @param pageInfoRequest        ページ情報リクエスト（ページネーションのため） (optional)
     * @return 会員一覧レスポンス (status code 200) or その他エラー (status code 500)
     */
    @Override
    public ResponseEntity<CustomerListResponse> get(@NotNull @Valid CustomerListGetRequest customerListGetRequest,
                                                    @Valid PageInfoRequest pageInfoRequest) {

        // 検索条件作成
        MemberInfoSearchForDaoConditionDto memberInfoConditionDto =
                        customerHelper.toConditionDtoForSearch(customerListGetRequest);

        // ページング検索セットアップ
        PageInfoModule pageInfoModule = ApplicationContextUtility.getBean(PageInfoModule.class);
        pageInfoModule.setupPageInfo(memberInfoConditionDto, pageInfoRequest.getPage(), pageInfoRequest.getLimit(),
                                     pageInfoRequest.getOrderBy(), pageInfoRequest.getSort()
                                    );

        // 取得
        List<MemberInfoForBackDto> memberInfoForBackDtoList =
                        memberInfoResultSearchService.execute(memberInfoConditionDto);

        // ページ情報レスポンスを設定
        PageInfoResponse pageInfoResponse = new PageInfoResponse();
        try {
            pageInfoModule.setupResponsePager(memberInfoConditionDto, pageInfoResponse);
        } catch (InvocationTargetException | IllegalAccessException e) {
            LOGGER.info("ページ情報レスポンスの設定異常： " + e.getMessage());
        }

        CustomerListResponse customerListResponse = customerHelper.toCustomerListResponse(memberInfoForBackDtoList);
        customerListResponse.setPageInfo(pageInfoResponse);

        return new ResponseEntity<>(customerListResponse, HttpStatus.OK);
    }

    /**
     * POST /users/customers : 本会員登録処理
     * 本会員登録処理
     *
     * @param customerRegistRequest 会員登録リクエスト (required)
     * @return 会員レスポンス (status code 200) or その他エラー (status code 500)
     */
    @Override
    public ResponseEntity<CustomerResponse> regist(@Valid CustomerRegistRequest customerRegistRequest) {

        // 登録
        MemberInfoEntity memberInfoEntity = customerHelper.toCustomerEntity(customerRegistRequest);

        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(
                        RequestContextHolder.getRequestAttributes())).getRequest();
        // 顧客IDをヘッダーから取得
        String customerId = request.getHeader("X-LOGGED-IN-MEMBER-SEQ");

        // 顧客住所エンティティを作成
        CustomerAddressRegistRequest address = customerRegistRequest.getAddressBook();
        CustomerAddressBookEntity customerAddressBookEntity =
                        customerHelper.toCustomerAddressBookEntity(customerRegistRequest.getMemberInfoAddressId(),
                                                                   customerId, address
                                                                  );

        memberInfoRegistService.execute(customerRegistRequest.getAccessUid(), customerId, memberInfoEntity,
                                        customerRegistRequest.getMailMagazine(),
                                        customerRegistRequest.getPreMailMagazine(), customerRegistRequest.getMid(),
                                        customerAddressBookEntity
                                       );

        // 会員登録完了メール送信通知
        // パラメータ設定
        MemberinfoProcessCompleteRequest memberinfoProcessCompleteRequest = new MemberinfoProcessCompleteRequest();
        memberinfoProcessCompleteRequest.setMemberInfoSeq(memberInfoEntity.getMemberInfoSeq());
        memberinfoProcessCompleteRequest.setMailTemplateType(HTypeMailTemplateType.MEMBER_REGISTRATION.getValue());

        Object[] args = new Object[] {memberinfoProcessCompleteRequest};
        Class<?>[] argsClass = new Class<?>[] {MemberinfoProcessCompleteRequest.class};
        AsyncTaskUtility.executeAfterTransactionCommit(() -> {
            asyncService.asyncService(notificationSubApi, "memberinfoProcessComplete", args, argsClass);
        });

        // 購読フラグが、変更後がON かつ 変更前がOFFの場合のみメール送信
        if (Boolean.TRUE.equals(customerRegistRequest.getMailMagazine()) && Boolean.FALSE.equals(
                        customerRegistRequest.getPreMailMagazine())) {
            // メルマガ登録完了メールを送信（非同期処理）
            // パラメータ設定
            MailMagazineProcessCompleteRequest mailMagazineProcessCompleteRequest =
                            createMailMagazineProcessCompleteRequest(memberInfoEntity,
                                                                     HTypeMailTemplateType.MAILMAGAZINE_REGISTRATION
                                                                    );

            Object[] objectRequest = new Object[] {mailMagazineProcessCompleteRequest};
            Class<?>[] typeClass = new Class<?>[] {MailMagazineProcessCompleteRequest.class};
            AsyncTaskUtility.executeAfterTransactionCommit(() -> {
                asyncService.asyncService(notificationSubApi, "mailMagazineProcessComplete", objectRequest, typeClass);
            });
        }

        // 購読フラグが、変更後がOFF かつ 変更前がONの場合のみメール送信
        if (Boolean.FALSE.equals(customerRegistRequest.getMailMagazine()) && Boolean.TRUE.equals(
                        customerRegistRequest.getPreMailMagazine())) {
            // メルマガ解除完了メールを送信する（非同期処理）
            // パラメータ設定
            MailMagazineProcessCompleteRequest mailMagazineProcessCompleteRequest =
                            createMailMagazineProcessCompleteRequest(memberInfoEntity,
                                                                     HTypeMailTemplateType.MAILMAGAZINE_UNREGISTRATION
                                                                    );
            Object[] objectRequest = new Object[] {mailMagazineProcessCompleteRequest};
            Class<?>[] typeClass = new Class<?>[] {MailMagazineProcessCompleteRequest.class};
            asyncService.asyncService(notificationSubApi, "mailMagazineProcessComplete", objectRequest, typeClass);

        }

        // 取得
        MemberInfoEntity memberInfoEntityRegisted = memberInfoGetService.execute(memberInfoEntity.getMemberInfoSeq());
        CustomerResponse customerResponse = customerHelper.toCustomerResponse(memberInfoEntityRegisted);

        return new ResponseEntity<>(customerResponse, HttpStatus.OK);
    }

    /**
     * PUT /users/customers/{memberinfoSeq} : 会員情報更新
     * 会員情報更新
     *
     * @param memberinfoSeq         会員情報SEQ (required)
     * @param customerUpdateRequest 会員更新リクエスト (required)
     * @return 会員レスポンス (status code 200) or その他エラー (status code 500)
     */
    @Override
    public ResponseEntity<CustomerResponse> update(Integer memberinfoSeq,
                                                   @Valid CustomerUpdateRequest customerUpdateRequest) {
        MemberInfoEntity memberInfoEntity;
        // 会員情報の作成
        memberInfoEntity = memberInfoGetService.execute(memberinfoSeq);

        try {
            memberInfoEntity = customerHelper.toCustomerEntity(customerUpdateRequest, memberInfoEntity);
        } catch (IllegalAccessException | InvocationTargetException e) {
            LOGGER.error("例外処理が発生しました", e);
            throwMessage(MSGCD_REFERER_FAIL_CONFIRM);
        }

        // 会員情報更新
        memberInfoUpdateService.execute(memberInfoEntity);

        // 顧客住所エンティティを作成
        CustomerAddressRegistRequest address = customerUpdateRequest.getAddressBook();
        CustomerAddressBookEntity customerAddressBookEntity =
                        customerHelper.toCustomerAddressBookEntity(customerUpdateRequest.getMemberInfoAddressId(),
                                                                   memberinfoSeq.toString(), address
                                                                  );

        // TODO 顧客の住所IDに紐づく住所情報の登録・更新を行う（管理サイトで住所情報を取得するための暫定対応）
        // 会員SEQを設定（それ以外の項目はAPIのヘルパークラスで設定されている）
        customerAddressBookEntity.setCustomerId(memberInfoEntity.getMemberInfoSeq().toString());
        int resultAddressRegist = this.customerAddressBookRegistLogic.execute(customerAddressBookEntity);
        if (resultAddressRegist == 0) {
            throwMessage(MSGCD_REFERER_FAIL_CONFIRM);
        }

        // メルマガ登録更新
        // 購読フラグが、変更後がON かつ 変更前がOFFの場合
        if (Boolean.TRUE.equals(customerUpdateRequest.getMailMagazine()) && Boolean.FALSE.equals(
                        customerUpdateRequest.getPreMailMagazine())) {

            // メルマガ登録・更新 ※解除は行わない
            int result = mailMagazineMemberRegistService.execute(memberInfoEntity.getMemberInfoMail(),
                                                                 memberInfoEntity.getMemberInfoSeq()
                                                                );

            // 件数がある かつ 修正前はフラグがOFFの場合
            if (result > 0) {
                MailMagazineProcessCompleteRequest mailMagazineProcessCompleteRequest =
                                createMailMagazineProcessCompleteRequest(memberInfoEntity,
                                                                         HTypeMailTemplateType.MAILMAGAZINE_REGISTRATION
                                                                        );

                Object[] args = new Object[] {mailMagazineProcessCompleteRequest};
                Class<?>[] argsClass = new Class<?>[] {MailMagazineProcessCompleteRequest.class};
                AsyncTaskUtility.executeAfterTransactionCommit(() -> {
                    asyncService.asyncService(notificationSubApi, "mailMagazineProcessComplete", args, argsClass);
                });
            }
        }

        // メルマガ解除
        // 購読フラグが、変更後がOFF かつ 変更前がONの場合
        if (Boolean.FALSE.equals(customerUpdateRequest.getMailMagazine()) && Boolean.TRUE.equals(
                        customerUpdateRequest.getPreMailMagazine())) {
            boolean removeFlg = mailMagazineMemberRemoveService.execute(memberInfoEntity.getMemberInfoMail(),
                                                                        memberInfoEntity.getMemberInfoSeq()
                                                                       );

            // 解除した場合
            if (removeFlg) {
                MailMagazineProcessCompleteRequest mailMagazineProcessCompleteRequest =
                                createMailMagazineProcessCompleteRequest(memberInfoEntity,
                                                                         HTypeMailTemplateType.MAILMAGAZINE_UNREGISTRATION
                                                                        );
                Object[] args = new Object[] {mailMagazineProcessCompleteRequest};
                Class<?>[] argsClass = new Class<?>[] {MailMagazineProcessCompleteRequest.class};
                AsyncTaskUtility.executeAfterTransactionCommit(() -> {
                    asyncService.asyncService(notificationSubApi, "mailMagazineProcessComplete", args, argsClass);
                });
            }
        }

        MemberInfoEntity memberInfoEntityUpdated = memberInfoGetService.execute(memberinfoSeq);
        CustomerResponse customerResponse = customerHelper.toCustomerResponse(memberInfoEntityUpdated);

        return new ResponseEntity<>(customerResponse, HttpStatus.OK);

    }

    /**
     * PUT /users/customers/{memberinfoSeq}/password-reset : パスワードリセット
     * パスワードリセット
     *
     * @param memberinfoSeq                      会員情報SEQ (required)
     * @param customerPasswordResetUpdateRequest 会員パスワードリセット更新リクエスト (required)
     * @return 会員レスポンス (status code 200) or その他エラー (status code 500)
     */
    @Override
    public ResponseEntity<CustomerResponse> resetPassword(Integer memberinfoSeq,
                                                          @Valid @RequestBody
                                                                          CustomerPasswordResetUpdateRequest customerPasswordResetUpdateRequest) {

        MemberInfoEntity memberInfoEntity = memberInfoGetService.execute(memberinfoSeq);

        // 会員パスワード変更サービス実行
        memberInfoPasswordUpdateService.execute(customerPasswordResetUpdateRequest.getConfirmMailPassword(),
                                                memberInfoEntity,
                                                customerPasswordResetUpdateRequest.getMemberInfoNewPassword()
                                               );

        MemberInfoEntity memberInfoEntityUpdated = memberInfoGetService.execute(memberinfoSeq);
        CustomerResponse customerResponse = customerHelper.toCustomerResponse(memberInfoEntityUpdated);

        return new ResponseEntity<>(customerResponse, HttpStatus.OK);
    }

    /**
     * DELETE /users/customers/{memberinfoSeq} : 会員登録解除
     * 会員登録解除
     *
     * @param memberinfoSeq         会員情報SEQ (required)
     * @param customerDeleteRequest 会員登録解除リクエスト (required)
     * @return 成功 (status code 200) or その他エラー (status code 500)
     */
    @Override
    public ResponseEntity<Void> delete(Integer memberinfoSeq,
                                       @NotNull @Valid CustomerDeleteRequest customerDeleteRequest) {

        // 会員登録解除サービス実行
        memberInfoRemoveService.execute(customerDeleteRequest.getAccessUid(), memberinfoSeq,
                                        customerDeleteRequest.getMemberInfoId(),
                                        customerDeleteRequest.getMemberInfoPassWord()
                                       );

        MemberinfoProcessCompleteRequest memberinfoProcessCompleteRequest = new MemberinfoProcessCompleteRequest();
        memberinfoProcessCompleteRequest.setMemberInfoSeq(memberinfoSeq);
        memberinfoProcessCompleteRequest.setMailTemplateType(HTypeMailTemplateType.MEMBER_UNREGISTRATION.getValue());

        Object[] args = new Object[] {memberinfoProcessCompleteRequest};
        Class<?>[] argsClass = new Class<?>[] {MemberinfoProcessCompleteRequest.class};
        AsyncTaskUtility.executeAfterTransactionCommit(() -> {
            asyncService.asyncService(notificationSubApi, "memberinfoProcessComplete", args, argsClass);
        });

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * PUT /users/customers/{memberinfoSeq}/mailaddresses : 会員メールアドレス変更
     * 会員メールアドレス変更
     * <p>
     * メールアドレスの変更処理<br/>
     *
     * @return 完了画面
     */
    @Override
    public ResponseEntity<CustomerResponse> updateMail(Integer memberinfoSeq,
                                                       @Valid
                                                                       CustomerMailAddressUpdateRequest customerMailAddressUpdateRequest) {

        MemberInfoEntity memberInfo = memberInfoUpdateMailGetService.execute(customerMailAddressUpdateRequest.getMid());

        // 会員メールアドレス変更サービス実行
        Integer shopSeq = 1001;

        memberInfoMailUpdateService.execute(shopSeq, memberInfo, customerMailAddressUpdateRequest.getMailMagazine(),
                                            customerMailAddressUpdateRequest.getPreMailMagazine(),
                                            customerMailAddressUpdateRequest.getMid()
                                           );

        MemberinfoProcessCompleteRequest memberinfoProcessCompleteRequest = new MemberinfoProcessCompleteRequest();
        memberinfoProcessCompleteRequest.setMemberInfoSeq(memberInfo.getMemberInfoSeq());
        memberinfoProcessCompleteRequest.setMailTemplateType(
                        HTypeMailTemplateType.EMAIL_MODIFICATION_COMPLATE.getValue());

        Object[] args = new Object[] {memberinfoProcessCompleteRequest};
        Class<?>[] argsClass = new Class<?>[] {MemberinfoProcessCompleteRequest.class};
        AsyncTaskUtility.executeAfterTransactionCommit(() -> {
            asyncService.asyncService(notificationSubApi, "memberinfoProcessComplete", args, argsClass);
        });

        // 購読フラグが、変更後がON かつ 変更前がOFFの場合のみメール送信
        if (customerMailAddressUpdateRequest.getMailMagazine()
            && !customerMailAddressUpdateRequest.getPreMailMagazine()) {

            MailMagazineProcessCompleteRequest mailMagazineProcessCompleteRequest =
                            createMailMagazineProcessCompleteRequest(memberInfo,
                                                                     HTypeMailTemplateType.MAILMAGAZINE_REGISTRATION
                                                                    );

            Object[] objectRequest = new Object[] {mailMagazineProcessCompleteRequest};
            Class<?>[] typeClass = new Class<?>[] {MailMagazineProcessCompleteRequest.class};
            AsyncTaskUtility.executeAfterTransactionCommit(() -> {
                asyncService.asyncService(notificationSubApi, "mailMagazineProcessComplete", objectRequest, typeClass);
            });
        }

        // 購読フラグが、変更後がOFF かつ 変更前がONの場合のみメール送信
        if (!customerMailAddressUpdateRequest.getMailMagazine()
            && customerMailAddressUpdateRequest.getPreMailMagazine()) {
            MailMagazineProcessCompleteRequest mailMagazineProcessCompleteRequest =
                            createMailMagazineProcessCompleteRequest(memberInfo,
                                                                     HTypeMailTemplateType.MAILMAGAZINE_UNREGISTRATION
                                                                    );

            Object[] objectRequest = new Object[] {mailMagazineProcessCompleteRequest};
            Class<?>[] typeClass = new Class<?>[] {MailMagazineProcessCompleteRequest.class};
            AsyncTaskUtility.executeAfterTransactionCommit(() -> {
                asyncService.asyncService(notificationSubApi, "mailMagazineProcessComplete", objectRequest, typeClass);
            });
        }

        CustomerResponse customerResponse = new CustomerResponse();
        customerHelper.setResponseFromEntity(customerResponse, memberInfo);

        return new ResponseEntity<>(customerResponse, HttpStatus.OK);
    }

    /**
     * createMailMagazineProcessCompleteRequest
     *
     * @param memberInfo
     * @param mailmagazineUnregistration
     * @return メルマガ登録/解除完了メリクエスト
     */
    private MailMagazineProcessCompleteRequest createMailMagazineProcessCompleteRequest(MemberInfoEntity memberInfo,
                                                                                        HTypeMailTemplateType mailmagazineUnregistration) {
        MailMagazineProcessCompleteRequest mailMagazineProcessCompleteRequest =
                        new MailMagazineProcessCompleteRequest();
        mailMagazineProcessCompleteRequest.setMemberInfoSeq(memberInfo.getMemberInfoSeq());
        mailMagazineProcessCompleteRequest.setMail(memberInfo.getMemberInfoMail());
        mailMagazineProcessCompleteRequest.setMailTemplateType(EnumTypeUtil.getValue(mailmagazineUnregistration));
        return mailMagazineProcessCompleteRequest;
    }

    /**
     * PUT /users/customers/{memberinfoSeq}/password : パスワード変更
     * パスワード変更<br/>
     *
     * @param memberinfoSeq
     * @param customerPasswordUpdateRequest
     * @return パスワード変更完了画面
     */
    @Override
    public ResponseEntity<CustomerResponse> updatePassword(Integer memberinfoSeq,
                                                           @Valid
                                                                           CustomerPasswordUpdateRequest customerPasswordUpdateRequest) {

        MemberInfoEntity memberInfo = memberInfoGetService.execute(memberinfoSeq);

        memberInfoPasswordUpdateService.execute(memberInfo, customerPasswordUpdateRequest.getMemberInfoPassword(),
                                                customerPasswordUpdateRequest.getMemberInfoNewPassWord()
                                               );

        CustomerResponse customerResponse = new CustomerResponse();
        customerHelper.setResponseFromEntity(customerResponse, memberInfo);

        return new ResponseEntity<>(customerResponse, HttpStatus.OK);
    }

    /**
     * GET /users/customers/csv : 会員CSVDL
     * 会員CSVDL
     *
     * @param customerListCsvGetRequest 会員一覧取CSVDLリクエスト (optional)
     * @return 成功 (status code 200)
     * or その他エラー (status code 200)
     */
    @SneakyThrows
    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public ResponseEntity<Void> downloadCsv(@NotNull @ApiParam(value = "CSVダウンロードオプションリクエスト", required = true) @Valid
                                                            CustomerCsvGetOptionRequest customerCsvGetOptionRequest,
                                            @ApiParam("会員一覧取CSVDLリクエスト") @Valid
                                                            CustomerListCsvGetRequest customerListCsvGetRequest) {
        // ショップSEQ
        Integer shopSeq = 1001;

        HttpServletResponse response = ((ServletRequestAttributes) Objects.requireNonNull(
                        RequestContextHolder.getRequestAttributes())).getResponse();

        assert response != null;
        response.setCharacterEncoding("MS932");

        // Apache Common CSV を特化したCSVフォーマットを準備する
        // 主にHIT-MALL独自のCsvDownloadOptionDtoからCSVFormatに変換する
        CsvDownloadOptionDto csvDownloadOptionDto = new CsvDownloadOptionDto();
        Map<String, OptionContentDto> csvDownloadOption;

        if (customerCsvGetOptionRequest.getOptionId() != null) {
            CsvOptionEntity csvOptionEntity =
                            csvOptionGetService.execute(Integer.parseInt(customerCsvGetOptionRequest.getOptionId()));
            CsvOptionDto csvOptionDto = customerHelper.toCsvOptionDto(csvOptionEntity);

            csvDownloadOptionDto.setOutputHeader(csvOptionDto.isOutHeader());

            csvDownloadOption = new LinkedHashMap<>();
            for (OptionContentDto optionContentDto : csvOptionDto.getOptionContent()) {
                if (optionContentDto != null) {
                    csvDownloadOption.put(optionContentDto.getItemName(), optionContentDto);
                }
            }
        } else {
            csvDownloadOptionDto.setOutputHeader(true);
            csvDownloadOption = CsvOptionUtil.getDefaultMapOptionContentDto(MemberCsvDto.class);
        }

        CSVFormat outputCsvFormat = CsvDownloadHandler.csvFormatBuilder(csvDownloadOptionDto);

        // Apache Common CSV を特化したCSVプリンター（設定したCSVフォーマットに沿ってデータを出力）を初期化する
        StringWriter stringWriter = new StringWriter();
        CSVPrinter printer = new CSVPrinter(stringWriter, outputCsvFormat);
        PrintWriter writer = response.getWriter();

        if (csvDownloadOptionDto.isOutputHeader()) {
            printer.printRecord(CsvDownloadHandler.outHeader(MemberCsvDto.class, csvDownloadOption));
            writer.write(stringWriter.toString());
            stringWriter.getBuffer().setLength(0);
            writer.flush();
        }

        if (ListUtils.isEmpty(customerListCsvGetRequest.getMemberInfoSeqList())) {
            // 検索条件作成
            MemberInfoSearchForDaoConditionDto conditionDto =
                            customerHelper.toConditionDtoForSearch(customerListCsvGetRequest);
            conditionDto.setShopSeq(shopSeq);

            try (Stream<MemberCsvDto> memberCsvDtoStream = memberInfoAllCsvDownloadLogic.execute(conditionDto)) {
                memberCsvDtoStream.forEach((memberCsvDto -> {
                    try {
                        printer.printRecord(CsvDownloadHandler.outCsvRecord(memberCsvDto, csvDownloadOption));
                        writer.write(stringWriter.toString());
                        stringWriter.getBuffer().setLength(0);
                    } catch (IOException e) {
                        LOGGER.error("例外処理が発生しました", e);
                    }
                }));
                writer.flush();
            }
        } else {
            // 会員SEQリスト作成
            List<Integer> memberInfoSeqList = customerListCsvGetRequest.getMemberInfoSeqList();

            try (Stream<MemberCsvDto> memberCsvDtoStream = memberInfoCsvDownloadLogic.execute(memberInfoSeqList)) {
                memberCsvDtoStream.forEach((memberCsvDto -> {
                    try {
                        printer.printRecord(CsvDownloadHandler.outCsvRecord(memberCsvDto, csvDownloadOption));
                        writer.write(stringWriter.toString());
                        stringWriter.getBuffer().setLength(0);
                    } catch (IOException e) {
                        LOGGER.error("例外処理が発生しました", e);
                    }
                }));
                writer.flush();
            }
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * GET /users/customers/csv/option-download/default : CSV ダウンロード オプション - デフォルトを取得
     * CSV ダウンロード オプション - デフォルトを取得
     *
     * @return 成功 (status code 200)
     *         or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     *         or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     *         or その他エラー (status code 200)
     */
    @Override
    public ResponseEntity<CustomerCsvOptionResponse> getDefault() {

        CsvOptionDto csvOptionDto = CsvOptionUtil.getDefaultCsvOption(MemberCsvDto.class);
        CustomerCsvOptionResponse customerCsvOptionResponse = customerHelper.toCustomerCsvOptionResponse(csvOptionDto);

        return new ResponseEntity<>(customerCsvOptionResponse, HttpStatus.OK);
    }

    /**
     * GET /users/customers/csv/option-download : CSVダウンロードオプション
     * CSVダウンロードオプション
     *
     * @return 成功 (status code 200)
     *         or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     *         or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     *         or その他エラー (status code 200)
     */
    @Override
    public ResponseEntity<CustomerCsvOptionListResponse> getOptionCsv() {

        List<CsvOptionEntity> csvOptionEntityList = csvOptionGetService.execute();

        CustomerCsvOptionListResponse customerCsvOptionListResponse =
                        customerHelper.toCustomerCsvOptionListResponse(csvOptionEntityList);

        return new ResponseEntity<>(customerCsvOptionListResponse, HttpStatus.OK);
    }

    /**
     * PUT /users/customers/csv/option-download/default : CSVダウンロードオプション更新
     * CSVダウンロードオプション更新
     *
     * @param customerCsvOptionUpdateRequest CSV ダウンロードオプションのリクエスト (required)
     * @return 成功 (status code 200)
     *         or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     *         or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     *         or その他エラー (status code 200)
     */
    @Override
    public ResponseEntity<Void> updateOption(@Valid CustomerCsvOptionUpdateRequest customerCsvOptionUpdateRequest) {

        CsvOptionEntity csvOptionEntity = customerHelper.toCsvOptionEntity(customerCsvOptionUpdateRequest);

        int result = csvOptionUpdateService.execute(csvOptionEntity);

        if (result == 0) {
            throwMessage(MSGCD_MEMBERINFO_CSVOPTION_UPDATE_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * POST /users/customers/{memberinfoSeq}/mailaddresses/change-mail/send : 会員メールアドレス変更メール送信
     * 会員メールアドレス変更メール送信
     *
     * @param memberinfoSeq                            会員情報SEQ (required)
     * @param customerMailAddressUpdateSendMailRequest 会員メールアドレス変更（メール送信）リクエスト (required)
     * @return 成功 (status code 200)
     * or その他エラー (status code 500)
     */
    @Override
    public ResponseEntity<Void> send(Integer memberinfoSeq,
                                     @Valid
                                                     CustomerMailAddressUpdateSendMailRequest customerMailAddressUpdateSendMailRequest) {

        Integer shopSeq = 1001;

        // 変更メールアドレス登録
        memberInfoMailUpdateSendMailService.execute(
                        shopSeq, memberinfoSeq, customerMailAddressUpdateSendMailRequest.getMemberInfoNewMail());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * GET /users/customers/getListByMemberInfo : 会員情報クラス
     * 会員情報クラス
     *
     * @param targetMembersListRequest 会員IDリストリクエスト (required)
     * @return 会員レスポンス (status code 200)
     * or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     * or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     * or その他エラー (status code 200)
     */
    @Override
    public ResponseEntity<TargetMembersListResponse> getEntityListByMemberInfoIdList(
                    @NotNull @ApiParam(value = "会員IDリストリクエスト", required = true) @Valid
                                    TargetMembersListRequest targetMembersListRequest) {
        TargetMembersListResponse targetMembersListResponse = new TargetMembersListResponse();

        List<String> checkMembersList =
                        memberInfoDao.getEntityListByMemberInfoIdList(targetMembersListRequest.getTargetMembersList());

        targetMembersListResponse.setCheckMembersList(checkMembersList);

        return new ResponseEntity<>(targetMembersListResponse, HttpStatus.OK);
    }
}