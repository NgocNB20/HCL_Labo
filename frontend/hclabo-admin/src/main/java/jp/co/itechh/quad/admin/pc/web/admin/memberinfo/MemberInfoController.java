package jp.co.itechh.quad.admin.pc.web.admin.memberinfo;

import jp.co.itechh.quad.admin.annotation.exception.HEHandler;
import jp.co.itechh.quad.admin.base.exception.AppLevelListException;
import jp.co.itechh.quad.admin.base.exception.FileDownloadException;
import jp.co.itechh.quad.admin.base.util.seasar.StringUtil;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeMemberInfoOutData;
import jp.co.itechh.quad.admin.constant.type.HTypeMemberInfoStatus;
import jp.co.itechh.quad.admin.constant.type.HTypePrefectureType;
import jp.co.itechh.quad.admin.constant.type.HTypeSendStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeSexUnnecessaryAnswer;
import jp.co.itechh.quad.admin.dto.memberinfo.MemberInfoDetailsDto;
import jp.co.itechh.quad.admin.dto.memberinfo.MemberInfoSearchForDaoConditionDto;
import jp.co.itechh.quad.admin.entity.mailmagazine.MailMagazineMemberEntity;
import jp.co.itechh.quad.admin.pc.web.admin.common.download.DownloadApiClient;
import jp.co.itechh.quad.admin.pc.web.admin.common.dto.CsvDownloadOptionDto;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.AllDownloadGroup;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.DisplayChangeGroup;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.DownloadBottomGroup;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.DownloadTopGroup;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.OptionDownloadGroup;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.SearchGroup;
import jp.co.itechh.quad.admin.pc.web.admin.memberinfo.validation.PeriodValidator;
import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import jp.co.itechh.quad.admin.utility.CsvUtility;
import jp.co.itechh.quad.admin.web.AbstractController;
import jp.co.itechh.quad.admin.web.PageInfoModule;
import jp.co.itechh.quad.customer.presentation.api.CustomerApi;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerCsvGetOptionRequest;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerCsvOptionListResponse;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerCsvOptionResponse;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerCsvOptionUpdateRequest;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerListCsvGetRequest;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerListGetRequest;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerListResponse;
import jp.co.itechh.quad.customer.presentation.api.param.PageInfoRequest;
import jp.co.itechh.quad.customer.presentation.api.param.PageInfoResponse;
import jp.co.itechh.quad.mailmagazine.presentation.api.MailMagazineApi;
import jp.co.itechh.quad.mailmagazine.presentation.api.param.MailmagazineMemberListResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 会員検索コントロール
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@RequestMapping("/memberinfo")
@Controller
@SessionAttributes(value = "memberInfoModel")
@PreAuthorize("hasAnyAuthority('MEMBER:4')")
public class MemberInfoController extends AbstractController {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(MemberInfoController.class);

    /**
     * 会員検索：デフォルトページ番号
     */
    private static final String DEFAULT_MEMBERSEARCH_PNUM = "1";

    /**
     * 会員検索：デフォルト：ソート項目
     */
    private static final String DEFAULT_MEMBERSEARCH_ORDER_FIELD = "memberInfoId";

    /**
     * 会員検索：デフォルト：ソート条件(昇順/降順)
     */
    private static final boolean DEFAULT_MEMBERSEARCH_ORDER_ASC = true;

    /**
     * 会員検索Helper
     */
    private final MemberInfoHelper memberInfoHelper;

    /**
     * 会員API
     */
    private final CustomerApi customerApi;

    /**
     * メルマガAPI
     */
    private final MailMagazineApi mailMagazineApi;

    /**
     * 変換ユーティリティクラス
     */
    private final ConversionUtility conversionUtility;

    /**
     * ダウンロードAPIクライアント
     */
    private final DownloadApiClient downloadApiClient;

    /**
     * CSVファイル名
     */
    private String fileNameMember = "member";

    /**
     * CSVファイル名
     */
    private String fileNameMail = "mail";

    /**
     * 期間の開始日付、終了日付の動的バリデータ
     */
    private final PeriodValidator periodValidator;

    private static final String DEFAULT_FILE_NAME = "download_file";

    private static final String MEMBERINFO_DOWNLOAD = "/users/customers/csv";

    /**
     * コンストラクタ
     *
     * @param memberInfoHelper  会員検索Helper
     * @param customerApi       会員API
     * @param mailMagazineApi   メルマガAPI
     * @param conversionUtility 変換ユーティリティクラス
     * @param periodValidator   the period validator
     * @param downloadApiClient ダウンロードAPIクライアント
     */
    @Autowired
    public MemberInfoController(MemberInfoHelper memberInfoHelper,
                                CustomerApi customerApi,
                                MailMagazineApi mailMagazineApi,
                                ConversionUtility conversionUtility,
                                PeriodValidator periodValidator,
                                DownloadApiClient downloadApiClient) {
        this.memberInfoHelper = memberInfoHelper;
        this.customerApi = customerApi;
        this.mailMagazineApi = mailMagazineApi;
        this.conversionUtility = conversionUtility;
        this.periodValidator = periodValidator;
        this.downloadApiClient = downloadApiClient;
    }

    @InitBinder(value = "memberInfoModel")
    public void initBinder(WebDataBinder error) {
        // メール件名の動的バリデータをセット
        error.addValidators(periodValidator);
    }

    /**
     * 初期処理
     *
     * @param md              RequestParam
     * @param memberInfoModel 会員検索モデル
     * @param model           Model
     */
    @GetMapping("/")
    @HEHandler(exception = AppLevelListException.class, returnView = "memberinfo/index")
    public String doLoadIndex(@RequestParam(required = false) Optional<String> md,
                              MemberInfoModel memberInfoModel,
                              BindingResult error,
                              Model model) {

        // 再検索の場合
        if (md.isPresent()) {
            // お問い合わせ詳細 ⇒ 紐づけた会員IDから会員詳細 ⇒ 戻る の流れで
            // 実施する際に、pageLimitが未設定のケースがあったため、こちらのロジックを追加する
            if (memberInfoModel.getLimit() == 0) {
                memberInfoModel.setLimit(memberInfoModel.getPageDefaultLimitModel());
            }
            // 再検索を実行
            search(memberInfoModel, error, model);
            if (error.hasErrors()) {
                return "memberinfo/index";
            }
        } else {
            clearModel(MemberInfoModel.class, memberInfoModel, model);
        }

        // プルダウンアイテム情報を取得
        initDropDownValue(memberInfoModel);

        return "memberinfo/index";
    }

    /**
     * 検索
     *
     * @param memberInfoModel    会員検索モデル
     * @param error              BindingResult
     * @param redirectAttributes RedirectAttributes
     * @param model              Model
     */
    @PostMapping(value = "/", params = "doSearch")
    @HEHandler(exception = AppLevelListException.class, returnView = "memberinfo/index")
    public String doSearch(@Validated(SearchGroup.class) MemberInfoModel memberInfoModel,
                           BindingResult error,
                           RedirectAttributes redirectAttributes,
                           Model model) {

        if (error.hasErrors()) {
            return "memberinfo/index";
        }

        // ページング関連項目初期化（limitは画面プルダウンで指定されてくる）
        memberInfoModel.setPageNumber(DEFAULT_MEMBERSEARCH_PNUM);
        memberInfoModel.setOrderField(DEFAULT_MEMBERSEARCH_ORDER_FIELD);
        memberInfoModel.setOrderAsc(DEFAULT_MEMBERSEARCH_ORDER_ASC);

        // 検索
        search(memberInfoModel, error, model);
        return "memberinfo/index";
    }

    /**
     * 検索結果の表示切替
     *
     * @param memberInfoModel 会員検索モデル
     * @param model           Model
     */
    @PostMapping(value = "/", params = "doDisplayChange")
    @HEHandler(exception = AppLevelListException.class, returnView = "memberinfo/index")
    public String doDisplayChange(@Validated(DisplayChangeGroup.class) MemberInfoModel memberInfoModel,
                                  BindingResult error,
                                  Model model) {

        if (error.hasErrors()) {
            return "memberinfo/index";
        }

        // 検索結果チェック
        resultListCheck(memberInfoModel);
        search(memberInfoModel, error, model);
        return "memberinfo/index";
    }

    /**
     * 全件CSV出力<br/>
     */
    @PreAuthorize("hasAnyAuthority('MEMBER:8')")
    @PostMapping(value = "/", params = "doAllDownload")
    @HEHandler(exception = AppLevelListException.class, returnView = "memberinfo/index")
    @HEHandler(exception = FileDownloadException.class, returnView = "memberinfo/index")
    public void doAllDownload(@Validated(AllDownloadGroup.class) MemberInfoModel memberInfoModel,
                              BindingResult error,
                              Model model,
                              HttpServletResponse response) {

        if (error.hasErrors()) {
            throw new FileDownloadException(model);
        }
        try {
            // 検索条件作成
            MemberInfoSearchForDaoConditionDto memberInfoConditionDto =
                            memberInfoHelper.toConditionDtoForSearch(memberInfoModel);

            CustomerListCsvGetRequest customerListCsvGetRequest =
                            memberInfoHelper.toCustomerListCsvGetRequest(memberInfoConditionDto);

            CustomerCsvGetOptionRequest customerCsvGetOptionRequest = new CustomerCsvGetOptionRequest();

            if (memberInfoModel.getCsvDownloadOptionDto() != null) {
                customerCsvGetOptionRequest.setOptionId(memberInfoModel.getCsvDownloadOptionDto().getOptionId());
            }

            downloadApiClient.invokeAPI(response, customerApi.getApiClient().getBasePath(), MEMBERINFO_DOWNLOAD,
                                        getFileNameMember(),
                                        conversionUtility.convertObjectListToMap(customerCsvGetOptionRequest,
                                                                                 customerListCsvGetRequest
                                                                                )
                                       );

        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }
        if (error.hasErrors()) {
            throw new FileDownloadException(model);
        }
    }

    /**
     * CSVダウンロード（一覧上部ボタン）<br/>
     */
    @PreAuthorize("hasAnyAuthority('MEMBER:8')")
    @PostMapping(value = "/", params = "doDownload1")
    @HEHandler(exception = AppLevelListException.class, returnView = "memberinfo/index")
    @HEHandler(exception = FileDownloadException.class, returnView = "memberinfo/index")
    public void doDownload1(@Validated(DownloadTopGroup.class) MemberInfoModel memberInfoModel,
                            BindingResult error,
                            Model model,
                            HttpServletResponse response) {

        if (error.hasErrors()) {
            throw new FileDownloadException(model);
        }
        // CSV選択ダウンロード
        downloadCheckedCsv(memberInfoModel, error, memberInfoModel.getCheckedMemberOutData1(), response);
        if (error.hasErrors()) {
            throw new FileDownloadException(model);
        }
    }

    /**
     * CSVダウンロード（一覧下部ボタン）<br/>
     */
    @PreAuthorize("hasAnyAuthority('MEMBER:8')")
    @PostMapping(value = "/", params = "doDownload2")
    @HEHandler(exception = AppLevelListException.class, returnView = "memberinfo/index")
    @HEHandler(exception = FileDownloadException.class, returnView = "memberinfo/index")
    public void doDownload2(@Validated(DownloadBottomGroup.class) MemberInfoModel memberInfoModel,
                            BindingResult error,
                            Model model,
                            HttpServletResponse response) {

        if (error.hasErrors()) {
            throw new FileDownloadException(model);
        }
        // CSV選択ダウンロード
        downloadCheckedCsv(memberInfoModel, error, memberInfoModel.getCheckedMemberOutData2(), response);
        if (error.hasErrors()) {
            throw new FileDownloadException(model);
        }
    }

    /**
     * CSVテンプレートの一覧を取得 AJAX<br/>
     *
     * @return CustomerCsvOptionListResponse 会員CSVDLオプションリストレスポンス
     */
    @GetMapping(value = "/getCsvTemplateListAjax")
    @ResponseBody
    public ResponseEntity<CustomerCsvOptionListResponse> getCsvTemplateListAjax() {
        return ResponseEntity.ok(customerApi.getOptionCsv());
    }

    /**
     * CSV オプションの更新テンプレート AJAX <br/>
     *
     * @param csvDownloadOptionDto
     * @param error
     * @param memberInfoModel
     * @return
     */
    @PostMapping(value = "/doUpdateTemplateAjax")
    @ResponseBody
    public ResponseEntity<?> doUpdateTemplateAjax(
                    @RequestBody @Validated(OptionDownloadGroup.class) CsvDownloadOptionDto csvDownloadOptionDto,
                    BindingResult error,
                    MemberInfoModel memberInfoModel) {

        // ユーザーが注文画面でテンプレートを選択しなかった場合
        if (csvDownloadOptionDto.getResetFlg()) {
            memberInfoModel.setCsvDownloadOptionDto(null);
        }

        if (error.hasErrors()) {
            return ResponseEntity.badRequest().body(error.getAllErrors());
        }

        // ユーザーが注文画面でテンプレートのみを選択した場合
        if (csvDownloadOptionDto.getUpdateFlg()) {
            CustomerCsvOptionUpdateRequest customerCsvOptionUpdateRequest =
                            memberInfoHelper.toCustomerCsvOptionUpdateRequest(csvDownloadOptionDto);
            customerApi.updateOption(customerCsvOptionUpdateRequest);
        }

        memberInfoModel.setCsvDownloadOptionDto(csvDownloadOptionDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * デフォルトの CSV ダウンロード オプションを取得 AJAX<br/>
     *
     * @return CustomerCsvOptionResponse 受注検索CSVDLオプションリストレスポンス
     */
    @GetMapping(value = "/getDefaultOptionDownloadAjax")
    @ResponseBody
    public ResponseEntity<CustomerCsvOptionResponse> getDefaultOptionDownloadAjax() {
        return ResponseEntity.ok(customerApi.getDefault());
    }

    /**
     * 検索処理
     *
     * @param memberInfoModel 会員検索モデル
     */
    protected void search(MemberInfoModel memberInfoModel, BindingResult error, Model model) {
        try {
            // 検索条件作成
            MemberInfoSearchForDaoConditionDto memberInfoConditionDto =
                            memberInfoHelper.toConditionDtoForSearch(memberInfoModel);

            // ページング検索セットアップ
            PageInfoModule pageInfoHelper = ApplicationContextUtility.getBean(PageInfoModule.class);

            // リクエスト用のページャーを生成
            PageInfoRequest pageInfoRequest = new PageInfoRequest();

            // リクエスト用のページャー項目をセット
            pageInfoHelper.setupPageRequest(pageInfoRequest,
                                            this.conversionUtility.toInteger(memberInfoModel.getPageNumber()),
                                            memberInfoModel.getLimit(), memberInfoModel.getOrderField(),
                                            memberInfoModel.isOrderAsc()
                                           );

            CustomerListGetRequest customerListGetRequest =
                            memberInfoHelper.toCustomerListGetRequest(memberInfoConditionDto);

            CustomerListResponse customerListResponse = customerApi.get(customerListGetRequest, pageInfoRequest);

            // ページャーにレスポンス情報をセット
            PageInfoResponse tmp = customerListResponse.getPageInfo();
            pageInfoHelper.setupPageInfo(memberInfoModel, tmp.getPage(), tmp.getLimit(), tmp.getNextPage(),
                                         tmp.getPrevPage(), tmp.getTotal(), tmp.getTotalPages()
                                        );

            // 取得
            List<MemberInfoDetailsDto> memberInfoEntityList =
                            memberInfoHelper.toMemberInfoEntityList(customerListResponse);

            // ページにセット
            memberInfoHelper.toPageForResultList(memberInfoEntityList, memberInfoModel, memberInfoConditionDto);

            // ページにメルマガ会員情報をセット
            setMailMagazineMemberForSearch(memberInfoModel.getResultItems(), error);

            // ページャーセットアップ
            pageInfoHelper.setupViewPager(memberInfoModel.getPageInfo(), memberInfoModel);
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            Map<String, String> itemNameAdjust = new HashMap<>();
            itemNameAdjust.put("page", "pageNumber");
            itemNameAdjust.put("sort", "orderAsc");
            itemNameAdjust.put("orderBy", "orderField");
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }
    }

    /**
     * 会員検索結果リストが空でないことをチェックする<br/>
     * (ブラウザバック後の選択出力などでの不具合防止のため)<br/>
     *
     * @param memberInfoModel 会員検索モデル
     */
    protected void resultListCheck(MemberInfoModel memberInfoModel) {
        if (!memberInfoModel.isResult()) {
            return;
        }

        if (StringUtil.isEmpty(memberInfoModel.getResultItems().get(0).getResultMemberInfoId())) {
            memberInfoModel.setResultItems(null);
            this.throwMessage(MemberInfoModel.MSGCD_ILLEGAL_OPERATION);
        }
    }

    /**
     * プルダウンアイテム情報を取得
     *
     * @param memberInfoModel 会員検索モデル
     */
    protected void initDropDownValue(MemberInfoModel memberInfoModel) {

        // プルダウンアイテム情報を取得
        memberInfoModel.setMemberInfoPrefectureItems(EnumTypeUtil.getEnumMap(HTypePrefectureType.class));
        memberInfoModel.setPeriodTypeItems(EnumTypeUtil.getEnumMap(HTypeMemberInfoStatus.class));
        memberInfoModel.setMemberInfoStatusItems(EnumTypeUtil.getEnumMap(HTypeMemberInfoStatus.class));
        memberInfoModel.setMemberInfoSexItems(EnumTypeUtil.getEnumMap(HTypeSexUnnecessaryAnswer.class));
        memberInfoModel.setMemberOutDataItems(EnumTypeUtil.getEnumMap(HTypeMemberInfoOutData.class));
        memberInfoModel.setCheckedMemberOutData1Items(EnumTypeUtil.getEnumMap(HTypeMemberInfoOutData.class));
        memberInfoModel.setCheckedMemberOutData2Items(EnumTypeUtil.getEnumMap(HTypeMemberInfoOutData.class));

        CustomerCsvOptionListResponse customerCsvOptionListResponse = customerApi.getOptionCsv();
        List<CsvDownloadOptionDto> csvDownloadOptionDtoList =
                        memberInfoHelper.toCsvDownloadOptionDtoList(customerCsvOptionListResponse);

        memberInfoModel.setCsvDownloadOptionDtoList(csvDownloadOptionDtoList);
    }

    /**
     * メルマガ会員情報を設定<br/>
     * 将来的には、会員とメルマガ会員のテーブルは統合されるので、このあたりのメソッドは不要となる
     *
     * @param resultItem 会員検索結果画面情報リスト
     */
    protected void setMailMagazineMemberForSearch(List<MemberInfoResultItem> resultItem, BindingResult error) {
        try {
            // メルマガ会員情報を取得
            MailmagazineMemberListResponse mailmagazineMemberListResponse = mailMagazineApi.get(
                            new jp.co.itechh.quad.mailmagazine.presentation.api.param.PageInfoRequest());

            List<MailMagazineMemberEntity> mailMagazineMemberEntityList =
                            memberInfoHelper.toMailMagazineMemberEntityList(mailmagazineMemberListResponse);

            // 会員結果一覧とメルマガ会員情報を紐づける
            for (MemberInfoResultItem result : resultItem) {

                // 初期設定はfalse
                result.setResultMailMagazine(false);

                for (MailMagazineMemberEntity mailMagazineMemberEntity : mailMagazineMemberEntityList) {
                    if (result.getMemberInfoSeq().equals(mailMagazineMemberEntity.getMemberinfoSeq())
                        && mailMagazineMemberEntity.getSendStatus() == HTypeSendStatus.SENDING) {
                        result.setResultMailMagazine(true);
                    }
                }
            }
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }
    }

    /**
     * 選択された会員情報の対象CSVダウンロード<br/>
     */
    protected void downloadCheckedCsv(MemberInfoModel memberInfoModel,
                                      BindingResult error,
                                      String outDataType,
                                      HttpServletResponse response) {

        // 検索結果チェック
        resultListCheck(memberInfoModel);

        /*
         * ブラウザバックの対応としてmembmerInfoSeqをhiddenで持ち
         * resultItemsのセッションのmemberInfoSeqのみをサブミット時に
         * 更新するようにしている為、memberInfoSeqのみは、画面と同期とる それを利用してCSVの出力を行う
         */

        // 会員SEQリスト作成
        List<Integer> memberInfoSeqList = memberInfoHelper.toMemberInfoSeqList(memberInfoModel);

        CustomerListCsvGetRequest customerListCsvGetRequest =
                        memberInfoHelper.toCustomerListCsvGetRequest(memberInfoSeqList);

        // チェック選択なし
        if (memberInfoSeqList.isEmpty()) {
            throwMessage(MemberInfoModel.MSGCD_NOT_SELECTED_DATE);
        }
        try {
            if (HTypeMemberInfoOutData.MEMBER_CSV.getValue().equals(outDataType)) {

                CustomerCsvGetOptionRequest customerCsvGetOptionRequest = new CustomerCsvGetOptionRequest();
                if (memberInfoModel.getOptionTemplateIndexResult() != null) {
                    boolean errorFlag = true;
                    for (CsvDownloadOptionDto csvDownloadOptionDto : memberInfoModel.getCsvDownloadOptionDtoList()) {
                        if (csvDownloadOptionDto.getOptionId().equals(memberInfoModel.getOptionTemplateIndexResult())) {
                            errorFlag = false;
                            break;
                        }
                    }
                    if (errorFlag) {
                        memberInfoModel.setOptionTemplateIndexResult(null);
                    }
                }
                customerCsvGetOptionRequest.setOptionId(memberInfoModel.getOptionTemplateIndexResult());

                // 会員CSVのダウンロード開始
                downloadApiClient.invokeAPI(response, customerApi.getApiClient().getBasePath(), MEMBERINFO_DOWNLOAD,
                                            getFileNameMember(),
                                            conversionUtility.convertObjectListToMap(customerCsvGetOptionRequest,
                                                                                     customerListCsvGetRequest
                                                                                    )
                                           );
            }
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }
    }

    /**
     * 出力CSVファイル名を設定する
     */
    public String getFileNameMember() {
        if (StringUtils.isEmpty(this.fileNameMember)) {
            this.fileNameMember = DEFAULT_FILE_NAME;
        }
        CsvUtility csvUtility = ApplicationContextUtility.getBean(CsvUtility.class);
        return csvUtility.getDownLoadCsvFileName(this.fileNameMember);
    }

    /**
     * 出力CSVファイル名を設定する
     */
    public String getFileNameMail() {
        if (StringUtils.isEmpty(this.fileNameMail)) {
            this.fileNameMail = DEFAULT_FILE_NAME;
        }
        CsvUtility csvUtility = ApplicationContextUtility.getBean(CsvUtility.class);
        return csvUtility.getDownLoadCsvFileName(this.fileNameMail);
    }
}