package jp.co.itechh.quad.admin.pc.web.admin.shop.inquiry;

import jp.co.itechh.quad.admin.annotation.exception.HEHandler;
import jp.co.itechh.quad.admin.base.exception.AppLevelListException;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeInquiryStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeInquiryType;
import jp.co.itechh.quad.admin.constant.type.HTypeSiteType;
import jp.co.itechh.quad.admin.dto.inquiry.InquirySearchResultDto;
import jp.co.itechh.quad.admin.entity.inquiry.InquiryGroupEntity;
import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import jp.co.itechh.quad.admin.web.AbstractController;
import jp.co.itechh.quad.admin.web.PageInfoModule;
import jp.co.itechh.quad.inquiry.presentation.api.InquiryApi;
import jp.co.itechh.quad.inquiry.presentation.api.param.InquiryListGetRequest;
import jp.co.itechh.quad.inquiry.presentation.api.param.InquiryListResponse;
import jp.co.itechh.quad.inquiry.presentation.api.param.PageInfoRequest;
import jp.co.itechh.quad.inquiry.presentation.api.param.PageInfoResponse;
import jp.co.itechh.quad.inquirygroup.presentation.api.InquiryGroupApi;
import jp.co.itechh.quad.inquirygroup.presentation.api.param.InquiryGroupListRequest;
import jp.co.itechh.quad.inquirygroup.presentation.api.param.InquiryGroupListResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * InquiryController Class
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@RequestMapping("/inquiry")
@Controller
@SessionAttributes(value = "inquiryModel")
@PreAuthorize("hasAnyAuthority('SHOP:4')")
public class InquiryController extends AbstractController {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(InquiryController.class);

    /**
     * デフォルトページ番号
     */
    private static final String DEFAULT_PNUM = "1";

    /**
     * デフォルト：ソート項目
     */
    private static final String DEFAULT_ORDER_FIELD = "firstInquiryTime";

    /**
     * デフォルト：ソート条件(昇順/降順)
     */
    private static final boolean DEFAULT_ORDER_ASC = false;

    /**
     * 表示モード:「list」の場合 再検索
     */
    public static final String MODE_LIST = "list";

    /**
     * 問い合わせ検索画面のHelper
     */
    private final InquiryHelper inquiryHelper;

    /**
     * 問合せAPI
     */
    private final InquiryApi inquiryApi;

    /**
     * 問い合わせ分類設定API
     */
    private final InquiryGroupApi inquiryGroupApi;

    @Autowired
    public InquiryController(InquiryHelper inquiryHelper, InquiryApi inquiryApi, InquiryGroupApi inquiryGroupApi) {
        this.inquiryHelper = inquiryHelper;
        this.inquiryApi = inquiryApi;
        this.inquiryGroupApi = inquiryGroupApi;
    }

    /**
     * 初期表示
     *
     * @return 自画面
     */
    @GetMapping("/")
    @HEHandler(exception = AppLevelListException.class, returnView = "inquiry/index")
    public String doLoadIndex(@RequestParam(required = false) Optional<String> md,
                              @RequestParam(required = false) Optional<String> id,
                              InquiryModel inquiryModel,
                              BindingResult error,
                              Model model) {

        if (id.isPresent()) {
            // ら会員詳細 ⇒ 紐づけた会員IDからお問い合わせ詳細
            // 実施する際に、pageLimitが未設定のケースがあったため、こちらのロジックを追加する
            if (inquiryModel.getLimit() == 0) {
                inquiryModel.setLimit(inquiryModel.getPageDefaultLimitModel());
            }
            inquiryModel.setResultInquiryMemberInfoMail(Objects.requireNonNull(id.get()));

            /* 会員詳細からの遷移 */
            // 検索条件の会員ID(メールアドレス)に「メールアドレス」をセット
            // 会員詳細画面で問い合わせ検索用メールアドレスをもち、リダイレクトスコープで取得
            inquiryModel.setSearchMemberInfoMail(inquiryModel.getResultInquiryMemberInfoMail());
            inquiryModel.setResultInquiryMemberInfoMail(null);

            // 検索
            this.search(inquiryModel, error, model);
            if (error.hasErrors()) {
                return "inquiry/index";
            }

        } else {
            // 再検索の場合
            if (md.isPresent() && MODE_LIST.equals(md.get())) {
                if (inquiryModel.getLimit() == 0) {
                    inquiryModel.setLimit(inquiryModel.getPageDefaultLimitModel());
                }
                // 再検索を実行
                search(inquiryModel, error, model);
                if (error.hasErrors()) {
                    return "inquiry/index";
                }
            } else {
                clearModel(InquiryModel.class, inquiryModel, model);
            }
        }
        initComponentValue(inquiryModel, error);

        return "inquiry/index";

    }

    /**
     * 問い合わせ分類設定画面へ遷移
     *
     * @return 問い合わせ分類設定画面
     */
    @PostMapping(value = "/", params = "doInquiryGroup")
    public String doInquiryGroup() {
        return "redirect:/inquiry/inquirygroup";
    }

    /**
     * 検索
     *
     * @param inquiryModel
     * @param error
     * @param model
     * @return 自画面
     */
    @PostMapping(value = "/", params = "doInquirySearch")
    @HEHandler(exception = AppLevelListException.class, returnView = "inquiry/index")
    public String doInquirySearch(@Validated InquiryModel inquiryModel, BindingResult error, Model model) {
        if (error.hasErrors()) {
            return "inquiry/index";
        }

        // ページング関連項目初期化（limitは画面プルダウンで指定されてくる）
        inquiryModel.setPageNumber(DEFAULT_PNUM);
        inquiryModel.setOrderField(DEFAULT_ORDER_FIELD);
        inquiryModel.setOrderAsc(DEFAULT_ORDER_ASC);

        // 検索
        this.search(inquiryModel, error, model);

        return "inquiry/index";
    }

    /**
     * 検索結果の表示切替
     *
     * @param inquiryModel
     * @param model
     * @return 自画面
     */
    @PostMapping(value = "/", params = "doDisplayChange")
    @HEHandler(exception = AppLevelListException.class, returnView = "inquiry/index")
    public String doDisplayChange(@Validated InquiryModel inquiryModel, BindingResult error, Model model) {
        if (error.hasErrors()) {
            return "inquiry/index";
        }

        search(inquiryModel, error, model);
        return "inquiry/index";
    }

    /**
     * 検索処理
     *
     * @param inquiryModel
     * @param model
     */
    private void search(InquiryModel inquiryModel, BindingResult error, Model model) {
        try {
            // 検索条件作成
            InquiryListGetRequest inquiryListGetRequest = inquiryHelper.toInquiryListGetRequestForSearch(inquiryModel);

            // ページング検索セットアップ
            PageInfoModule pageInfoHelper = ApplicationContextUtility.getBean(PageInfoModule.class);

            PageInfoRequest pageInfoRequest = new PageInfoRequest();
            int page = 1;
            if (inquiryModel.getPageNumber() != null) {
                page = Integer.parseInt(inquiryModel.getPageNumber());
            }
            pageInfoHelper.setupPageRequest(pageInfoRequest, page, inquiryModel.getLimit(),
                                            inquiryModel.getOrderField(), inquiryModel.isOrderAsc()
                                           );

            InquiryListResponse inquiryListResponse = inquiryApi.get(inquiryListGetRequest, pageInfoRequest);

            // 検索
            List<InquirySearchResultDto> resultList = inquiryHelper.toInquirySearchResultDtoList(
                            Objects.requireNonNull(inquiryListResponse.getInquiryList()));

            // 反映
            inquiryHelper.toPageForSearch(inquiryListResponse, resultList, inquiryModel);

            // ページャーにレスポンス情報をセット
            PageInfoResponse pageInfoResponse = inquiryListResponse.getPageInfo();
            pageInfoHelper.setupPageInfo(inquiryModel, pageInfoResponse.getPage(), pageInfoResponse.getLimit(),
                                         pageInfoResponse.getNextPage(), pageInfoResponse.getPrevPage(),
                                         pageInfoResponse.getTotal(), pageInfoResponse.getTotalPages()
                                        );
            // ページャーセットアップ
            pageInfoHelper.setupViewPager(inquiryModel.getPageInfo(), inquiryModel);

            // 件数セット
            inquiryModel.setTotalCount(pageInfoResponse.getTotal());
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            Map<String, String> itemNameAdjust = new HashMap<>();
            itemNameAdjust.put("inquiryGroupSeq", "searchInquiryGroupSeq");
            itemNameAdjust.put("inquiryStatus", "searchInquiryStatus");
            itemNameAdjust.put("inquiryCode", "searchInquiryCode");
            itemNameAdjust.put("inquiryTimeFrom", "searchInquiryTimeFrom");
            itemNameAdjust.put("inquiryTimeTo", "searchInquiryTimeTo");
            itemNameAdjust.put("inquiryMail", "searchInquiryMail");
            itemNameAdjust.put("inquiryType", "searchInquiryType");
            itemNameAdjust.put("orderCode", "searchOrderCode");
            itemNameAdjust.put("inquiryTel", "searchInquiryTel");
            itemNameAdjust.put("lastRepresentative", "searchLastRepresentative");
            itemNameAdjust.put("cooperationMemo", "searchCooperationMemo");
            itemNameAdjust.put("memberInfoMail", "searchMemberInfoMail");

            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }
    }

    /**
     * プルダウンアイテム情報を取得
     *
     * @param inquiryModel お問い合わせ検索モデル
     */
    private void initComponentValue(InquiryModel inquiryModel, BindingResult error) {
        try {
            // set Search Inquiry Status
            inquiryModel.setSearchInquiryStatusItems(EnumTypeUtil.getEnumMap(HTypeInquiryStatus.class));

            // set Search Inquiry Type
            Map<String, String> inquiryTypeMap = new HashMap<>();
            inquiryTypeMap.put(HTypeInquiryType.GENERAL.getValue(), HTypeInquiryType.GENERAL.getLabel());
            inquiryModel.setSearchInquiryTypeItems(inquiryTypeMap);

            InquiryGroupListRequest inquiryGroupListRequest = new InquiryGroupListRequest();
            inquiryGroupListRequest.setSiteType(HTypeSiteType.BACK.getValue());
            InquiryGroupListResponse inquiryGroupListResponse = inquiryGroupApi.get(null, inquiryGroupListRequest);

            List<InquiryGroupEntity> list = inquiryHelper.toInquiryGroupEntityList(
                            Objects.requireNonNull(inquiryGroupListResponse.getInquiryGroupList()));
            Map<String, String> searchInquiryGroupSeqMap = new HashMap<>();
            for (InquiryGroupEntity entity : list) {
                searchInquiryGroupSeqMap.put(entity.getInquiryGroupSeq().toString(), entity.getInquiryGroupName());
            }
            inquiryModel.setSearchInquiryGroupSeqItems(searchInquiryGroupSeqMap);
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            Map<String, String> itemNameAdjust = new HashMap<>();
            itemNameAdjust.put("orderDisplay", "orderDisplayRadio");
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }
    }
}