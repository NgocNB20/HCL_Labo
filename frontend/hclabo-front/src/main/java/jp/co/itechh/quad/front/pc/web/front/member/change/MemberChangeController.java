package jp.co.itechh.quad.front.pc.web.front.member.change;

import jp.co.itechh.quad.addressbook.presentation.api.AddressBookApi;
import jp.co.itechh.quad.addressbook.presentation.api.param.AddressBookAddressRegistResponse;
import jp.co.itechh.quad.addressbook.presentation.api.param.AddressBookAddressResponse;
import jp.co.itechh.quad.addressbook.presentation.api.param.AddressBookCloseRequest;
import jp.co.itechh.quad.addressbook.presentation.api.param.AddressBookDeleteRequest;
import jp.co.itechh.quad.customer.presentation.api.CustomerApi;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerResponse;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerUpdateRequest;
import jp.co.itechh.quad.front.annotation.exception.HEHandler;
import jp.co.itechh.quad.front.base.exception.AppLevelListException;
import jp.co.itechh.quad.front.constant.type.HTypePrefectureType;
import jp.co.itechh.quad.front.constant.type.HTypeSendStatus;
import jp.co.itechh.quad.front.constant.type.HTypeSexUnnecessaryAnswer;
import jp.co.itechh.quad.front.entity.mailmagazine.MailMagazineMemberEntity;
import jp.co.itechh.quad.front.entity.memberinfo.MemberInfoEntity;
import jp.co.itechh.quad.front.service.common.impl.HmFrontUserDetailsServiceImpl;
import jp.co.itechh.quad.front.util.common.EnumTypeUtil;
import jp.co.itechh.quad.front.web.AbstractController;
import jp.co.itechh.quad.mailmagazine.presentation.api.MailMagazineApi;
import jp.co.itechh.quad.mailmagazine.presentation.api.param.MailmagazineMemberResponse;
import jp.co.itechh.quad.zipcode.presentation.api.ZipcodeApi;
import jp.co.itechh.quad.zipcode.presentation.api.param.ZipCodeCheckRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * 会員情報変更 Controller
 *
 * @author PHAM QUANG DIEU (VJP)
 */
@RequestMapping("/member/change")
@Controller
@SessionAttributes(value = "memberChangeModel")
public class MemberChangeController extends AbstractController {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(MemberChangeController.class);

    /** 顧客Api */
    private final CustomerApi customerApi;

    /** 住所録Api */
    private final AddressBookApi addressBookApi;

    /** メールAPI */
    private final MailMagazineApi mailMagazineApi;

    /** 郵便番号Api */
    private final ZipcodeApi zipcodeApi;

    /** 認証サービス */
    private final HmFrontUserDetailsServiceImpl userDetailsService;

    /** 不正遷移 */
    protected static final String MSGCD_REFERER_FAIL = "AMX000301";

    /** 不正遷移 */
    protected static final String MSGCD_REFERER_FAIL_CONFIRM = "AMX000401";

    /** 都道府県のプルダウンいじられた */
    protected static final String MSGCD_ILLEGAL_PREFECTURE = "AMX000302";

    /** 性別のラジオボタンいじられた */
    protected static final String MSGCD_ILLEGAL_SEX = "AMX000303";

    /** 都道府県と郵便番号の整合性エラー ※W付与 */
    protected static final String MSGCD_PREFECTURE_CONSISTENCY = "AMX000304W";

    /** 都道府県フィールド名 **/
    protected static final String FILED_NAME_PREFECTURE = "memberInfoPrefecture";

    /** 前画面が確認画面の「修正する」からであるかを判断するフラグ */
    protected static final String FLAG_FROMCONFIRM = "fromConfirm";

    private static final String MSGCD_ADDRESS_CLOSE = "LOGISTIC-ADDRESS-CLOSE-001";

    /** 会員情報変更Helper */
    private final MemberChangeHelper memberChangeHelper;

    /** コンストラクタ */
    @Autowired
    public MemberChangeController(CustomerApi customerApi,
                                  AddressBookApi addressBookApi,
                                  MailMagazineApi mailMagazineApi,
                                  ZipcodeApi zipcodeApi,
                                  HmFrontUserDetailsServiceImpl userDetailsService,
                                  MemberChangeHelper memberChangeHelper) {
        this.customerApi = customerApi;
        this.addressBookApi = addressBookApi;
        this.mailMagazineApi = mailMagazineApi;
        this.zipcodeApi = zipcodeApi;
        this.userDetailsService = userDetailsService;
        this.memberChangeHelper = memberChangeHelper;
    }

    /**
     * 入力画面：初期処理
     *
     * @param memberChangeModel
     * @param model
     * @return 入力画面
     */
    @GetMapping(value = "/")
    @HEHandler(exception = AppLevelListException.class, returnView = "member/change/index")
    protected String doLoadIndex(MemberChangeModel memberChangeModel, BindingResult error, Model model) {

        // 確認画面からの遷移の場合は、セッション情報を表示
        if (model.containsAttribute(FLAG_FROMCONFIRM)) {

            // 必須項目がない場合 エラー
            if (checkInput(memberChangeModel)) {
                throwMessage(MSGCD_REFERER_FAIL);
            } else {
                return "member/change/index";
            }
        }

        // モデル初期化
        clearModel(MemberChangeModel.class, memberChangeModel, model);

        // 動的コンポーネント作成
        initComponents(memberChangeModel);

        // 会員SEQから会員情報取得
        Integer memberInfoSeq = memberChangeModel.getCommonInfo().getCommonInfoUser().getMemberInfoSeq();

        CustomerResponse customerResponse = null;
        AddressBookAddressResponse addressResponse = null;
        MemberInfoEntity memberInfoEntity = null;
        try {
            customerResponse = this.customerApi.getByMemberinfoSeq(memberInfoSeq);
            addressResponse = this.addressBookApi.getAddressById(customerResponse.getMemberInfoAddressId());
            memberInfoEntity = this.memberChangeHelper.toMemberInfoEntity(customerResponse);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
        }

        if (error.hasErrors()) {
            return "member/change/index";
        }

        // 取得情報をページへセット
        try {
            this.memberChangeHelper.toPageForLoad(memberInfoEntity, memberChangeModel, addressResponse);
        } catch (IllegalAccessException | InvocationTargetException e) {
            LOGGER.error("例外処理が発生しました", e);
            throwMessage(MSGCD_REFERER_FAIL);
        }

        // ページにメルマガ情報をセット
        setMailMagazineMember(memberChangeModel, error, memberInfoEntity);

        if (error.hasErrors()) {
            return "member/change/index";
        }

        return "member/change/index";
    }

    /**
     * 会員更新処理<br/>
     *
     * @param memberChangeModel
     * @param sessionStatus
     * @param redirectAttributes
     * @param model
     * @return 完了画面
     */
    @PostMapping(value = "/", params = "doOnceMemberInfoUpdate")
    @HEHandler(exception = AppLevelListException.class, returnView = "member/change/index")
    public String doConfirm(@Validated MemberChangeModel memberChangeModel,
                            BindingResult error,
                            SessionStatus sessionStatus,
                            RedirectAttributes redirectAttributes,
                            Model model,
                            HttpServletRequest request) {

        // 都道府県と郵便番号が不一致の場合
        checkPrefectureAndZipCode(memberChangeModel, error);

        if (error.hasErrors()) {
            return "member/change/index";
        }

        // 入力チェック。POSTされた値が不正でないか。
        checkIllegalParameter(memberChangeModel);

        // Sessionの値がなくなっているかのNullチェックを行う
        if (StringUtils.isEmpty(memberChangeModel.getMemberInfoEntity().getMemberInfoId())) {
            // モデル初期化後、エラーページへリダイレクト（現行でもここに侵入する可能性なし）
            clearModel(MemberChangeModel.class, memberChangeModel, model);
            addMessage(MSGCD_REFERER_FAIL, redirectAttributes, model);
            return "redirect:/error";
        }

        // 会員情報の作成
        CustomerUpdateRequest customerUpdateRequest = null;
        AddressBookAddressRegistResponse addressResponse = null;
        try {
            // 住所の登録（API側で変更前住所との差分をがある場合のみ、変更後住所を登録）
            addressResponse = this.addressBookApi.registRelateCustomerInfo(
                            this.memberChangeHelper.toRegistAddressRequestForMemberInfoUpdate(memberChangeModel));

            // 会員情報の更新
            customerUpdateRequest = this.memberChangeHelper.toMemberInfoRequestForMemberInfoUpdate(memberChangeModel,
                                                                                                   addressResponse
                                                                                                  );

            this.customerApi.update(memberChangeModel.getMemberInfoEntity().getMemberInfoSeq(), customerUpdateRequest);

        } catch (HttpServerErrorException | HttpClientErrorException e) {
            LOGGER.error("例外処理が発生しました", e);

            // 【暫定対応】住所録APIからのレスポンスモデルが存在しつつ、ユーザーサービスでエラーが起きた場合、該当住所を削除して元のデフォルト指定フラグを復活させる
            if (addressResponse != null && StringUtils.isNotBlank(addressResponse.getAddressId())) {

                AddressBookDeleteRequest addressBookDeleteRequest = new AddressBookDeleteRequest();
                addressBookDeleteRequest.setAddressId(addressResponse.getAddressId());
                addressBookDeleteRequest.setPreAddressId(memberChangeModel.getPreAddressId());

                // TODO エラー対応 失敗したら、デフォルト住所録がいなくなったり新住所の住所録が残ったりする。管理者に別途メール送信であったり、非同期処理に切り替えるなどが必要では？
                this.addressBookApi.delete(addressBookDeleteRequest);
            }

            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);

            if (error.hasErrors()) {
                return "member/change/index";
            }
        }

        // 住所登録が正常終了した場合
        if (addressResponse != null && StringUtils.isNotBlank(addressResponse.getAddressId())) {
            // 変更前住所を非公開状態にする
            // TODO エラー対応 このタイミングで例外制御はNG。管理者に別途メール送信であったり、非同期処理に切り替えるなどが必要では？
            AddressBookCloseRequest addressBookCloseRequest = new AddressBookCloseRequest();
            addressBookCloseRequest.setAddressId(memberChangeModel.getPreAddressId());
            try {
                this.addressBookApi.close(addressBookCloseRequest, null);
            } catch (Exception e) {
                LOGGER.error("例外処理が発生しました", e);
                throwMessage(MSGCD_ADDRESS_CLOSE);
            }
        }

        // セッションのユーザー情報を更新
        request.setAttribute("isCheckInfo", true);
        this.userDetailsService.updateUserInfo(this.getCommonInfo().getCommonInfoUser().getMemberInfoId());

        // Modelをセッションより破棄
        sessionStatus.setComplete();

        // 完了画面へ遷移
        return "redirect:/member/change/complete";

    }

    /**
     * 完了画面：画面表示処理
     *
     * @return 完了画面
     */
    @GetMapping(value = "/complete")
    public String doLoadComplete() {

        return "member/change/complete";
    }

    /**
     * 動的コンポーネント作成
     *
     * @param memberChangeModel
     */
    protected void initComponents(MemberChangeModel memberChangeModel) {

        // 都道府県区分値を取得（北海道：北海道）
        Map<String, String> prefectureTypeItems = EnumTypeUtil.getEnumMap(HTypePrefectureType.class, true);
        memberChangeModel.setMemberInfoPrefectureItems(prefectureTypeItems);

        // 性別(未回答)区分値を取得
        Map<String, String> memberInfoSexItems = EnumTypeUtil.getEnumMap(HTypeSexUnnecessaryAnswer.class);
        memberChangeModel.setMemberInfoSexItems(memberInfoSexItems);
    }

    /**
     * メルマガ会員情報を設定<br/>
     * 将来的には、会員とメルマガ会員のテーブルは統合されるので、このあたりのメソッドは不要となる
     *
     * @param memberChangeModel
     * @param memberInfoEntity
     */
    protected void setMailMagazineMember(MemberChangeModel memberChangeModel,
                                         BindingResult error,
                                         MemberInfoEntity memberInfoEntity) {

        // メルマガ会員情報を取得
        MailmagazineMemberResponse mailmagazineMemberResponse = null;
        try {
            // メルマガ会員情報を取得
            mailmagazineMemberResponse = mailMagazineApi.getByMemberinfoSeq(memberInfoEntity.getMemberInfoSeq());
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            // アイテム名調整マップ
            Map<String, String> itemNameAdjust = new HashMap<>();
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, itemNameAdjust);
            return;
        }

        MailMagazineMemberEntity mailMagazineMemberEntity =
                        memberChangeHelper.toMailMagazineMemberEntity(mailmagazineMemberResponse);

        if (mailMagazineMemberEntity != null && mailMagazineMemberEntity.getSendStatus() == HTypeSendStatus.SENDING) {
            memberChangeModel.setMailMagazine(true);
            memberChangeModel.setPreMailMagazine(true);
        } else {
            memberChangeModel.setMailMagazine(false);
            memberChangeModel.setPreMailMagazine(false);
        }
    }

    /**
     * 必須項目を全てチェックし、不正遷移かどうかをチェック<br/>
     *
     * @return true=不正、false=正常
     */
    protected boolean checkInput(MemberChangeModel memberChangeModel) {

        // 氏名(姓)
        if (StringUtils.isEmpty(memberChangeModel.getMemberInfoLastName())) {
            return true;
        }

        // 氏名(名)
        if (StringUtils.isEmpty(memberChangeModel.getMemberInfoFirstName())) {
            return true;
        }

        // フリガナ(セイ)
        if (StringUtils.isEmpty(memberChangeModel.getMemberInfoLastKana())) {
            return true;
        }

        // フリガナ(メイ)
        if (StringUtils.isEmpty(memberChangeModel.getMemberInfoFirstKana())) {
            return true;
        }

        // 性別
        if (StringUtils.isEmpty(memberChangeModel.getMemberInfoSex())) {
            return true;
        }

        // 電話番号
        if (StringUtils.isEmpty(memberChangeModel.getMemberInfoTel())) {
            return true;
        }

        // 郵便番号1
        if (StringUtils.isEmpty(memberChangeModel.getMemberInfoZipCode1())) {
            return true;
        }

        // 郵便番号2
        if (StringUtils.isEmpty(memberChangeModel.getMemberInfoZipCode2())) {
            return true;
        }

        // 都道府県
        if (StringUtils.isEmpty(memberChangeModel.getMemberInfoPrefecture())) {
            return true;
        }

        // 住所1
        if (StringUtils.isEmpty(memberChangeModel.getMemberInfoAddress1())) {
            return true;
        }

        // 住所2
        if (StringUtils.isEmpty(memberChangeModel.getMemberInfoAddress2())) {
            return true;
        }

        return false;
    }

    /**
     * 都道府県と郵便番号の不整合チェック<br/>
     *
     * @param memberChangeModel
     */
    protected void checkPrefectureAndZipCode(MemberChangeModel memberChangeModel, BindingResult error) {

        // nullの場合service未実行(必須チェックは別タスク)
        if (StringUtils.isEmpty(memberChangeModel.getMemberInfoZipCode1()) || StringUtils.isEmpty(
                        memberChangeModel.getMemberInfoZipCode2()) || StringUtils.isEmpty(
                        memberChangeModel.getMemberInfoPrefecture())) {
            return;
        }

        try {
            ZipCodeCheckRequest zipCodeCheckRequest = new ZipCodeCheckRequest();
            zipCodeCheckRequest.setZipCode(
                            memberChangeModel.getMemberInfoZipCode1() + memberChangeModel.getMemberInfoZipCode2());
            zipCodeCheckRequest.setPrefecture(memberChangeModel.getMemberInfoPrefecture());
            zipcodeApi.check(zipCodeCheckRequest);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("例外処理が発生しました", e);
            handleError(e.getStatusCode(), e.getResponseBodyAsString(), error, new HashMap<>());
        }
    }

    /**
     * 入力チェック
     *
     * @param memberChangeModel
     */
    protected void checkIllegalParameter(MemberChangeModel memberChangeModel) {

        // 都道府県入力値チェック
        checkPrefecture(memberChangeModel);
        // 性別入力値チェック
        checkSex(memberChangeModel);

    }

    /**
     * 都道府県の入力チェック<br/>
     * POSTされるプルダウンの値の書き換え対策
     *
     * @param memberChangeModel
     */
    protected void checkPrefecture(MemberChangeModel memberChangeModel) {

        Map<String, String> prefectureMap = EnumTypeUtil.getEnumMap(HTypePrefectureType.class, true);
        boolean existFlag = prefectureMap.containsKey(memberChangeModel.getMemberInfoPrefecture());
        if (!existFlag) {
            throwMessage(MSGCD_ILLEGAL_PREFECTURE);
        }
    }

    /**
     * 性別(未回答)の入力チェック<br/>
     * POSTされる値の書き換え対策
     *
     * @param memberChangeModel
     */
    protected void checkSex(MemberChangeModel memberChangeModel) {

        Map<String, String> memberInfoSexMap = EnumTypeUtil.getEnumMap(HTypeSexUnnecessaryAnswer.class);
        boolean existFlag = memberInfoSexMap.containsKey(memberChangeModel.getMemberInfoSex());
        if (!existFlag) {
            throwMessage(MSGCD_ILLEGAL_SEX);
        }
    }

}
