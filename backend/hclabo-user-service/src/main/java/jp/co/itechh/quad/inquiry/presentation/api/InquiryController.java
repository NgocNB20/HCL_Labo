package jp.co.itechh.quad.inquiry.presentation.api;

import io.swagger.annotations.ApiParam;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.constant.type.HTypeInquiryStatus;
import jp.co.itechh.quad.core.constant.type.HTypeMailTemplateType;
import jp.co.itechh.quad.core.constant.type.HTypeSiteType;
import jp.co.itechh.quad.core.dto.inquiry.InquiryDetailsDto;
import jp.co.itechh.quad.core.dto.inquiry.InquirySearchDaoConditionDto;
import jp.co.itechh.quad.core.dto.inquiry.InquirySearchResultDto;
import jp.co.itechh.quad.core.entity.inquiry.InquiryEntity;
import jp.co.itechh.quad.core.entity.memberinfo.MemberInfoEntity;
import jp.co.itechh.quad.core.logic.shop.inquiry.InquiryByMemberCheckLogic;
import jp.co.itechh.quad.core.logic.shop.inquiry.InquiryGetLogic;
import jp.co.itechh.quad.core.logic.shop.inquiry.InquiryRegistLogic;
import jp.co.itechh.quad.core.logic.shop.inquiry.InquirySearchResultListGetLogic;
import jp.co.itechh.quad.core.service.memberinfo.MemberInfoGetService;
import jp.co.itechh.quad.core.service.process.AsyncService;
import jp.co.itechh.quad.core.service.shop.inquiry.InquirySearchResultListGetService;
import jp.co.itechh.quad.core.service.shop.inquiry.InquiryUpdateService;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.core.utility.AsyncTaskUtility;
import jp.co.itechh.quad.core.web.AbstractController;
import jp.co.itechh.quad.core.web.PageInfoModule;
import jp.co.itechh.quad.inquiry.presentation.api.param.InquiryCheckResponse;
import jp.co.itechh.quad.inquiry.presentation.api.param.InquiryDetailsResponse;
import jp.co.itechh.quad.inquiry.presentation.api.param.InquiryGetRequest;
import jp.co.itechh.quad.inquiry.presentation.api.param.InquiryListGetRequest;
import jp.co.itechh.quad.inquiry.presentation.api.param.InquiryListResponse;
import jp.co.itechh.quad.inquiry.presentation.api.param.InquiryMemberUpdateRequest;
import jp.co.itechh.quad.inquiry.presentation.api.param.InquiryMemoUpdateRequest;
import jp.co.itechh.quad.inquiry.presentation.api.param.InquiryRegistRequest;
import jp.co.itechh.quad.inquiry.presentation.api.param.InquiryResponse;
import jp.co.itechh.quad.inquiry.presentation.api.param.InquiryStatusUpdateRequest;
import jp.co.itechh.quad.inquiry.presentation.api.param.InquiryUpdateRequest;
import jp.co.itechh.quad.inquiry.presentation.api.param.PageInfoRequest;
import jp.co.itechh.quad.inquiry.presentation.api.param.PageInfoResponse;
import jp.co.itechh.quad.notificationsub.presentation.api.NotificationSubApi;
import jp.co.itechh.quad.notificationsub.presentation.api.param.InquiryRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Objects;

/**
 * 問い合わせ Controller
 *
 * @author PHAM QUANG DIEU (VJP)
 */
@RestController
public class InquiryController extends AbstractController implements UsersApi {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(InquiryController.class);

    /** 問い合わせ取得ロジック */
    private final InquiryGetLogic inquiryGetLogic;

    /** 問い合わせ更新サービス */
    private final InquiryUpdateService inquiryUpdateService;

    /** 問い合わせ検索サービス */
    private final InquirySearchResultListGetService inquirySearchResultListGetService;

    /** 問い合わせ検索結果リスト取得Logic */
    private final InquirySearchResultListGetLogic inquirySearchResultListGetLogic;

    /** お問い合わせ登録ロジック */
    private final InquiryRegistLogic inquiryRegistLogic;

    /** 非同期処理サービス */
    private final AsyncService asyncService;

    /** 問い合わせ詳細画面Dxo */
    private final InquiryHelper inquiryHelper;

    /** 会員に紐付く問い合わせの存在チェックロジック */
    private final InquiryByMemberCheckLogic inquiryByMemberCheckLogic;

    /** 会員情報取得サービス */
    private final MemberInfoGetService memberInfoGetService;

    /** 変換ユーティリティ */
    private final ConversionUtility conversionUtility;

    /** 通知サブドメインAPI */
    private final NotificationSubApi notificationSubApi;

    /**
     * コンストラクタ
     *
     * @param inquiryGetLogic                   問い合わせ取得
     * @param inquiryHelper                     問い合わせHelper
     * @param inquiryByMemberCheckLogic         会員に紐付く問い合わせの存在チェック
     * @param inquirySearchResultListGetService 問い合わせ検索結果リスト取得
     * @param inquiryRegistLogic                問い合わせ登録ロジック
     * @param inquiryUpdateService              問い合わせ更新サービス
     * @param asyncService                      非同期用共通サービス 非同期処理はこの共通サービスを経由してcall
     * @param inquirySearchResultListGetLogic   問い合わせ検索結果リスト取得Logic
     * @param memberInfoGetService              会員情報取得
     * @param conversionUtility                 変換ユーティリティ
     * @param notificationSubApi                通知サブドメインAPI
     */
    public InquiryController(InquiryGetLogic inquiryGetLogic,
                             InquiryHelper inquiryHelper,
                             InquiryByMemberCheckLogic inquiryByMemberCheckLogic,
                             InquirySearchResultListGetService inquirySearchResultListGetService,
                             InquiryRegistLogic inquiryRegistLogic,
                             InquiryUpdateService inquiryUpdateService,
                             AsyncService asyncService,
                             InquirySearchResultListGetLogic inquirySearchResultListGetLogic,
                             MemberInfoGetService memberInfoGetService,
                             ConversionUtility conversionUtility,
                             NotificationSubApi notificationSubApi) {
        this.inquiryGetLogic = inquiryGetLogic;
        this.inquiryHelper = inquiryHelper;
        this.inquirySearchResultListGetService = inquirySearchResultListGetService;
        this.inquiryRegistLogic = inquiryRegistLogic;
        this.inquiryByMemberCheckLogic = inquiryByMemberCheckLogic;
        this.inquiryUpdateService = inquiryUpdateService;
        this.asyncService = asyncService;
        this.inquirySearchResultListGetLogic = inquirySearchResultListGetLogic;
        this.memberInfoGetService = memberInfoGetService;
        this.conversionUtility = conversionUtility;
        this.notificationSubApi = notificationSubApi;
    }

    /**
     * GET /users/inquiries/{inquiryCode} : 問い合わせ情報取得
     * 問い合わせ情報取得
     *
     * @param inquiryCode       問い合わせコード (required)
     * @param inquiryGetRequest 問い合わせ情報取得リクエスト (optional)
     * @return 問い合わせ情報レスポンス (status code 200)
     * or その他エラー (status code 500)
     */
    @Override
    public ResponseEntity<InquiryResponse> getByInquiryCode(
                    @ApiParam(value = "問い合わせコード", required = true) String inquiryCode,
                    @ApiParam("問い合わせ情報取得リクエスト") @Valid InquiryGetRequest inquiryGetRequest) {
        InquiryDetailsDto inquiryDetailsDto = new InquiryDetailsDto();
        InquiryResponse inquiryResponse = null;
        if (inquiryGetRequest.getInquiryTel() == null) {
            // 問い合わせ詳細DTOの取得
            inquiryDetailsDto = inquiryGetLogic.execute(inquiryCode);
        } else {
            // 問い合わせ詳細DTOの取得
            inquiryDetailsDto = inquiryGetLogic.execute(inquiryCode);
            InquiryEntity inquiryEntity = inquiryGetLogic.execute(inquiryCode, inquiryGetRequest.getInquiryTel());
            if (inquiryDetailsDto != null) {
                inquiryDetailsDto.setInquiryEntity(inquiryEntity);
            }
        }
        if (inquiryDetailsDto != null) {
            inquiryResponse = inquiryHelper.toInquiryResponse(inquiryDetailsDto);
        }
        return new ResponseEntity<>(inquiryResponse, HttpStatus.OK);
    }

    /**
     * POST /users/{memberinfoSeq}/inquiries/check : 会員に紐づく問い合わせの存在チェック
     * 会員に紐づく問い合わせの存在チェック
     *
     * @param memberinfoSeq 会員情報SEQ (required)
     * @return 問い合わせチェックレスポンス (status code 200)
     * or システムエラー (status code 500)
     */
    @Override
    public ResponseEntity<InquiryCheckResponse> check(
                    @ApiParam(value = "会員情報SEQ", required = true) Integer memberinfoSeq) {
        InquiryCheckResponse inquiryCheckResponse = new InquiryCheckResponse();
        inquiryCheckResponse.setInquiryFlag(inquiryByMemberCheckLogic.execute(memberinfoSeq));
        return new ResponseEntity<>(inquiryCheckResponse, HttpStatus.OK);
    }

    /**
     * GET /users/inquiries : 問い合わせ情報一覧取得
     * 問い合わせ情報一覧取得
     *
     * @param inquiryListGetRequest 問い合わせ情報一覧取得リクエスト (required)
     * @param pageInfoRequest       ページ情報リクエスト（ページネーションのため） (optional)
     * @return 問い合わせ情報一覧レスポンス (status code 200)
     * or システムエラー (status code 500)
     */
    @Override
    public ResponseEntity<InquiryListResponse> get(
                    @ApiParam(value = "問い合わせ情報一覧取得リクエスト", required = true) @NotNull @Valid
                                    InquiryListGetRequest inquiryListGetRequest,
                    @ApiParam("ページ情報リクエスト（ページネーションのため）") @Valid PageInfoRequest pageInfoRequest) {
        InquirySearchDaoConditionDto inquirySearchDaoConditionDto =
                        inquiryHelper.toInquiryListResponse(inquiryListGetRequest);
        // ページング検索セットアップ
        PageInfoModule pageInfoModule = ApplicationContextUtility.getBean(PageInfoModule.class);
        pageInfoModule.setupPageInfo(inquirySearchDaoConditionDto, pageInfoRequest.getPage(),
                                     pageInfoRequest.getLimit(), pageInfoRequest.getOrderBy(), pageInfoRequest.getSort()
                                    );

        List<InquirySearchResultDto> inquirySearchResultDtoList;
        HTypeSiteType siteType = null;
        if (inquiryListGetRequest.getSiteType() != null) {
            siteType = EnumTypeUtil.getEnumFromValue(HTypeSiteType.class, inquiryListGetRequest.getSiteType());
        }
        if (siteType == HTypeSiteType.FRONT_PC) {
            inquirySearchResultDtoList = inquirySearchResultListGetLogic.executeFront(inquirySearchDaoConditionDto);
        } else {
            // 検索
            inquirySearchResultDtoList = inquirySearchResultListGetService.execute(inquirySearchDaoConditionDto);
        }

        List<InquiryDetailsResponse> inquiryResponseList =
                        inquiryHelper.toInquiryResponseList(inquirySearchResultDtoList);

        InquiryListResponse inquiryListResponse = new InquiryListResponse();
        inquiryListResponse.setInquiryList(inquiryResponseList);
        // ページ情報レスポンスを設定
        PageInfoResponse pageInfoResponse = new PageInfoResponse();
        try {
            pageInfoModule.setupResponsePager(inquirySearchDaoConditionDto, pageInfoResponse);
        } catch (InvocationTargetException | IllegalAccessException e) {
            LOGGER.info("ページ情報レスポンスの設定異常： " + e.getMessage());
        }
        inquiryListResponse.setPageInfo(pageInfoResponse);

        return new ResponseEntity<>(inquiryListResponse, HttpStatus.OK);
    }

    /**
     * POST /users/inquiries : お問い合わせ登録
     * お問い合わせ登録
     *
     * @param inquiryRegistRequest 問い合わせ登録リクエスト (required)
     * @return 問い合わせ情報レスポンス (status code 200)
     * or システムエラー (status code 500)
     */
    @Override
    public ResponseEntity<InquiryResponse> regist(@ApiParam(value = "問い合わせ登録リクエスト", required = true) @Valid @RequestBody
                                                                  InquiryRegistRequest inquiryRegistRequest) {

        // お問い合わせ登録用会員SEQを取得
        String memberSeq = getmemberInfoSeqForInquiryRegist();

        InquiryDetailsDto inquiryDetailsDto =
                        inquiryHelper.toInquiryDetailsDtoFromRequest(memberSeq, inquiryRegistRequest);
        inquiryRegistLogic.executeInquiryRegist(inquiryDetailsDto);

        InquiryRequest inquiryRequest = new InquiryRequest();
        inquiryRequest.setInquiryCode(inquiryDetailsDto.getInquiryEntity().getInquiryCode());
        inquiryRequest.setMailTemplateType(HTypeMailTemplateType.INQUIRY_RECEPTION.getValue());

        Object[] args = new Object[] {inquiryRequest};
        Class<?>[] argsClass = new Class<?>[] {InquiryRequest.class};

        AsyncTaskUtility.executeAfterTransactionCommit(() -> {
            asyncService.asyncService(notificationSubApi, "inquiry", args, argsClass);
        });
        inquiryDetailsDto = inquiryGetLogic.execute(inquiryDetailsDto.getInquiryEntity().getInquiryCode());
        InquiryResponse inquiryResponse = inquiryHelper.toInquiryResponse(inquiryDetailsDto);
        return new ResponseEntity<>(inquiryResponse, HttpStatus.OK);
    }

    /**
     * お問い合わせ登録用会員SEQを取得<br/>
     * ※会員登録済の場合のみ会員SEQを返却（ゲストユーザーの場合はnullを返却）
     * @return お問い合わせ登録用会員SEQ
     */
    private String getmemberInfoSeqForInquiryRegist() {
        // リクエストヘッダーから会員SEQを取得
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(
                        RequestContextHolder.getRequestAttributes())).getRequest();
        String memberSeq = request.getHeader("X-LOGGED-IN-MEMBER-SEQ");

        // 会員SEQが取得できた場合は、会員テーブルにひもづいた会員SEQかどうか確認
        // ⇒会員テーブルのデータ取得できた場合のみ、会員SEQを返却する
        //
        // 【この判定が必要な理由】---------------------------------------
        // お問い合わせ受付メール上のお問い合わせ詳細URL誤り防止のため
        // ＜詳細説明＞
        // お問い合わせ詳細URLは会員用とゲスト用とで値が異なる
        // ⇒メールテンプレート上、会員SEQが０ならゲスト用、０でなければ会員用のURLを設定している
        //   つまり、ゲストユーザーの場合は会員SEQに０を設定させる必要がある
        //   しかし、QUADフェーズ２の「ゲストカートＩＮ」の対応で、ゲストユーザーがカートＩＮすると、
        //   会員登録前であっても会員SEQ（顧客ID）を先行払い出しするような仕様に変更した
        //   ↓
        //   これにより、ゲストカートＩＮしたユーザーがお問い合わせ登録すると、
        //   会員SEQに値が入った状態で本ＡＰＩが呼ばれることとなる
        //   ⇒こうすると、ゲストユーザーなのに、お問い合わせテーブルの会員SEQに値が入る状態となり
        //     結果、会員用の詳細ＵＲＬがメールに記載されてしまう
        //
        //     これを防ぐために、本制御を入れ込んだ
        //     尚、ゲストの場合は本メソッドをnullを返しているが、こうしておけば
        //     後続処理で会員SEQに0が設定されるようになる（Entityのフィールド初期値が0）
        // -------------------------------------------------------------
        if (StringUtils.isNotEmpty(memberSeq)) {
            MemberInfoEntity memberInfoEntity =
                            memberInfoGetService.execute(this.conversionUtility.toInteger(memberSeq));
            if (memberInfoEntity != null) {
                return memberSeq;
            }
        }
        // 上記以外はnullを返却（ゲストユーザー）
        return null;
    }

    /**
     * PUT /users/inquiries/{inquirySeq}/status : 問い合わせ状態更新 問い合わせ状態更新
     *
     * @param inquirySeq                 問い合わせSEQ (required)
     * @param inquiryStatusUpdateRequest 問い合わせ状態更新リクエスト (required)
     * @return 問い合わせ情報レスポンス (status code 200) or その他エラー (status code 500)
     */
    @Override
    public ResponseEntity<InquiryResponse> updateStatus(
                    @ApiParam(value = "問い合わせSEQ", required = true) @PathVariable("inquirySeq") Integer inquirySeq,
                    @ApiParam(value = "問い合わせ状態更新リクエスト", required = true) @Valid @RequestBody
                                    InquiryStatusUpdateRequest inquiryStatusUpdateRequest) {

        InquiryDetailsDto inquiryDetailsDto = inquiryGetLogic.execute(inquirySeq);
        HTypeInquiryStatus inquiryStatus = EnumTypeUtil.getEnumFromValue(HTypeInquiryStatus.class,
                                                                         inquiryStatusUpdateRequest.getInquiryStatus()
                                                                        );
        inquiryDetailsDto.getInquiryEntity().setInquiryStatus(inquiryStatus);
        inquiryUpdateService.executeStatusRegist(inquiryDetailsDto.getInquiryEntity());

        inquiryDetailsDto = inquiryGetLogic.execute(inquirySeq);
        InquiryResponse inquiryResponse = inquiryHelper.toInquiryResponse(inquiryDetailsDto);
        return new ResponseEntity<>(inquiryResponse, HttpStatus.OK);
    }

    /**
     * PUT /users/inquiries/{inquirySeq}/member : 問い合わせ会員紐づけ 問い合わせ会員紐づけ
     *
     * @param inquirySeq                 問い合わせSEQ (required)
     * @param inquiryMemberUpdateRequest 問い合わせ会員紐づけリクエスト (required)
     * @return 問い合わせ情報レスポンス (status code 200) or その他エラー (status code 500)
     */
    @Override
    public ResponseEntity<InquiryResponse> updateRelationMember(
                    @ApiParam(value = "問い合わせSEQ", required = true) @PathVariable("inquirySeq") Integer inquirySeq,
                    @ApiParam(value = "問い合わせ会員紐づけリクエスト", required = true) @Valid @RequestBody
                                    InquiryMemberUpdateRequest inquiryMemberUpdateRequest) {

        InquiryDetailsDto inquiryDetailsDto = inquiryGetLogic.execute(inquirySeq);

        if (inquiryMemberUpdateRequest.getIsRelease()) {
            InquiryResponse inquiryResponse;
            inquiryDetailsDto.getInquiryEntity().setMemberInfoSeq(0);
            inquiryUpdateService.executeMemberRegistRelease(inquiryDetailsDto.getInquiryEntity());
            inquiryDetailsDto = inquiryGetLogic.execute(inquirySeq);
            inquiryResponse = inquiryHelper.toInquiryResponse(inquiryDetailsDto);

            return new ResponseEntity<>(inquiryResponse, HttpStatus.OK);
        } else {
            inquiryDetailsDto.getInquiryEntity().setMemberInfoId(inquiryMemberUpdateRequest.getMemberInfoId());
            InquiryResponse inquiryResponse;
            // 問い合わせ情報更新
            MemberInfoEntity memberInfoEntity =
                            inquiryUpdateService.executeMemberRegist(inquiryDetailsDto.getInquiryEntity());
            inquiryDetailsDto = inquiryGetLogic.execute(inquirySeq);
            inquiryResponse = inquiryHelper.toInquiryResponse(inquiryDetailsDto);
            inquiryResponse.setInquiryCustomerDetailsResponse(
                            inquiryHelper.toInquiryCustomerDetailResponse(memberInfoEntity));

            return new ResponseEntity<>(inquiryResponse, HttpStatus.OK);
        }

    }

    /**
     * PUT /users/inquiries/{inquirySeq} : 問い合わせ更新
     * 問い合わせ更新
     *
     * @param inquirySeq           問い合わせSEQ (required)
     * @param inquiryUpdateRequest 問い合わせ更新リクエスト (required)
     * @return 問い合わせ情報レスポンス (status code 200)
     * or その他エラー (status code 500)
     */
    @Override
    public ResponseEntity<InquiryResponse> update(@ApiParam(value = "問い合わせSEQ", required = true) Integer inquirySeq,
                                                  @ApiParam(value = "問い合わせ更新リクエスト", required = true) @Valid @RequestBody
                                                                  InquiryUpdateRequest inquiryUpdateRequest) {
        InquiryDetailsDto inquiryDetailsDto = inquiryGetLogic.execute(inquirySeq);
        inquiryUpdateRequest.setInquirySeq(inquirySeq);
        InquiryDetailsDto inquiryDetailsDtoResult =
                        inquiryHelper.toInquiryDetailsDto(inquiryUpdateRequest, inquiryDetailsDto);
        inquiryRegistLogic.executeInquiryRegist(inquiryDetailsDtoResult);
        // 完了報告の場合
        if (HTypeInquiryStatus.COMPLETION.getValue().equals(inquiryUpdateRequest.getInquiryStatus())
            && inquiryUpdateRequest.getSendMailFlag()) {
            InquiryRequest inquiryRequest = new InquiryRequest();
            inquiryRequest.setInquiryCode(inquiryDetailsDto.getInquiryEntity().getInquiryCode());
            inquiryRequest.setMailTemplateType(HTypeMailTemplateType.INQUIRY_ANSWER.getValue());

            Object[] args = new Object[] {inquiryRequest};
            Class<?>[] argsClass = new Class<?>[] {InquiryRequest.class};
            // メール送信
            asyncService.asyncService(notificationSubApi, "inquiry", args, argsClass);
            // 問い合わせ返信の場合
        } else if (HTypeInquiryStatus.SENDING.getValue().equals(inquiryUpdateRequest.getInquiryStatus())
                   && inquiryUpdateRequest.getSendMailFlag()) {
            InquiryRequest inquiryRequest = new InquiryRequest();
            inquiryRequest.setInquiryCode(inquiryDetailsDto.getInquiryEntity().getInquiryCode());
            inquiryRequest.setMailTemplateType(HTypeMailTemplateType.INQUIRY_ANSWER.getValue());

            Object[] args = new Object[] {inquiryRequest};
            Class<?>[] argsClass = new Class<?>[] {InquiryRequest.class};
            // メール送信
            asyncService.asyncService(notificationSubApi, "inquiry", args, argsClass);
        }

        inquiryDetailsDto = inquiryGetLogic.execute(inquirySeq);
        InquiryResponse inquiryResponse = inquiryHelper.toInquiryResponse(inquiryDetailsDto);

        return new ResponseEntity<>(inquiryResponse, HttpStatus.OK);
    }

    /**
     * PUT /users/inquiries/{inquirySeq}/memo : 問い合わせ管理メモ更新
     * 問い合わせ管理メモ更新
     *
     * @param inquirySeq               問い合わせSEQ (required)
     * @param inquiryMemoUpdateRequest 問い合わせメモ更新リクエスト (required)
     * @return 問い合わせ情報レスポンス (status code 200)
     * or その他エラー (status code 500)
     */
    @Override
    public ResponseEntity<InquiryResponse> updateMemo(
                    @ApiParam(value = "問い合わせSEQ", required = true) @PathVariable("inquirySeq") Integer inquirySeq,
                    @ApiParam(value = "問い合わせメモ更新リクエスト", required = true) @Valid @RequestBody
                                    InquiryMemoUpdateRequest inquiryMemoUpdateRequest) {

        InquiryDetailsDto inquiryDetailsDto = inquiryGetLogic.execute(inquirySeq);
        InquiryEntity inquiryEntity = inquiryHelper.toInquiryMemoEntity(inquiryDetailsDto.getInquiryEntity(),
                                                                        inquiryMemoUpdateRequest
                                                                       );

        inquiryUpdateService.executeMemoRegist(inquiryEntity);

        inquiryDetailsDto = inquiryGetLogic.execute(inquirySeq);
        InquiryResponse inquiryResponse = inquiryHelper.toInquiryResponse(inquiryDetailsDto);
        return new ResponseEntity<>(inquiryResponse, HttpStatus.OK);
    }

}