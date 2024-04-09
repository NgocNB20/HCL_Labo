package jp.co.itechh.quad.admin.pc.web.admin.mailtemplate.send;

import jp.co.itechh.quad.admin.annotation.exception.HEHandler;
import jp.co.itechh.quad.admin.base.exception.AppLevelListException;
import jp.co.itechh.quad.admin.base.util.seasar.StringUtil;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeMailTemplateType;
import jp.co.itechh.quad.admin.dto.mail.MailSendDto;
import jp.co.itechh.quad.admin.dto.shop.mail.MailTemplateIndexDto;
import jp.co.itechh.quad.admin.entity.administrator.AdministratorEntity;
import jp.co.itechh.quad.admin.entity.shop.mail.MailTemplateEntity;
import jp.co.itechh.quad.admin.pc.web.admin.common.validation.group.ConfirmGroup;
import jp.co.itechh.quad.admin.pc.web.admin.mailtemplate.send.validation.MailTitleValidator;
import jp.co.itechh.quad.admin.pc.web.admin.mailtemplate.send.validation.group.SelectGroup;
import jp.co.itechh.quad.admin.pc.web.admin.mailtemplate.send.validation.group.SendTestGroup;
import jp.co.itechh.quad.admin.pc.web.admin.util.IdenticalDataCheckUtil;
import jp.co.itechh.quad.admin.util.async.AsyncUtil;
import jp.co.itechh.quad.admin.web.AbstractController;
import jp.co.itechh.quad.administrator.presentation.api.AdministratorApi;
import jp.co.itechh.quad.administrator.presentation.api.param.AdministratorResponse;
import jp.co.itechh.quad.notificationsub.presentation.api.NotificationSubApi;
import jp.co.itechh.quad.notificationsub.presentation.api.param.ExamResultsNoticeRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.MailListGetRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.MailListResponse;
import jp.co.itechh.quad.notificationsub.presentation.api.param.MailResponse;
import jp.co.itechh.quad.notificationsub.presentation.api.param.OrderConfirmationRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.SettlementExpirationNotificationRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.SettlementReminderRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.ShipmentNotificationRequest;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/mailtemplate/send")
@Controller
@SessionAttributes(value = "mailtemplateSendModel")
@PreAuthorize("hasAnyAuthority('MEMBER:8','ORDER:8','GOODS:8')")
public class MailtemplateSendController extends AbstractController {

    /** ロガー */
    protected static final Logger LOGGER = LoggerFactory.getLogger(MailtemplateSendController.class);

    /** メッセージコード：不正操作 */
    protected static final String MSGCD_ILLEGAL_OPERATION = "AYM000401";

    /** メッセージコード：送信先無し */
    protected static final String MSGCD_NOT_EXISTTENCE_ADDRESS = "AYM001701";

    /** 受注詳細のviewId */
    public static final String ORDER_DETAILS_VIEWID = "/order/details";

    /** 表示モード(md):list 検索画面の再検索実行<br/> */
    public static final String MODE_LIST = "list";

    /*** 運営者API */
    private final AdministratorApi administratorApi;

    /** 通知サブAPI */
    private final NotificationSubApi notificationSubApi;

    /** helper */
    private final MailtemplateSendHelper mailtemplateSendHelper;

    /** 件名用動的バリデータ */
    private final MailTitleValidator mailTitleValidator;

    /** コンストラクタ */
    @Autowired
    public MailtemplateSendController(AdministratorApi administratorApi,
                                      NotificationSubApi notificationSubApi,
                                      MailtemplateSendHelper mailtemplateSendHelper,
                                      MailTitleValidator mailTitleValidator) {
        this.administratorApi = administratorApi;
        this.notificationSubApi = notificationSubApi;
        this.mailtemplateSendHelper = mailtemplateSendHelper;
        this.mailTitleValidator = mailTitleValidator;
    }

    @InitBinder(value = "mailtemplateSendModel")
    public void initBinder(WebDataBinder error) {
        // メール件名の動的バリデータをセット
        error.addValidators(mailTitleValidator);
    }

    /**
     * 画面初期表示処理
     *
     * @param request
     * @param mailtemplateSendModel
     * @param redirectAttributes
     * @param model
     * @return テンプレート選択画面
     */
    @GetMapping("/select")
    @HEHandler(exception = AppLevelListException.class, returnView = "mailtemplate/send/select")
    protected String doLoadSelect(HttpServletRequest request,
                                  MailtemplateSendModel mailtemplateSendModel,
                                  RedirectAttributes redirectAttributes,
                                  Model model) {

        if (model.containsAttribute("mailSendDto")) {
            clearModel(MailtemplateSendModel.class, mailtemplateSendModel, model);
            MailSendDto mailSendDto = (MailSendDto) model.getAttribute("mailSendDto");
            mailtemplateSendModel.setMailSendDto(mailSendDto);
            mailtemplateSendModel.setInitialized(false);
        }

        // あるべきものがない場合
        if (mailtemplateSendModel.getMailSendDto().getApplication() == null) {
            addMessage(MSGCD_ILLEGAL_OPERATION, redirectAttributes, model);
            return "redirect:/error";
        }

        // 既に初期化済みの場合
        if (mailtemplateSendModel.isInitialized()) {
            return "mailtemplate/send/select";
        }

        // 遷移元ページを取得
        String previousPage = request.getHeader("referer");

        if (previousPage != null) {
            try {
                mailtemplateSendModel.setPreviousPage(previousPage);

            } catch (Exception e) {
                LOGGER.warn("遷移元ページ情報取得に失敗しました。", e);
            }

        }

        // 引数が存在しない場合は、元の画面へ戻す
        if (mailtemplateSendModel.getMailSendDto().getApplication() == null) {
            LOGGER.info("テンプレートメール送信に処理に必要な引数を受け取っていません。" + previousPage + " ページへ送り返します。");
            return "redirect:" + previousPage;
        }

        // 送信対象メール件数が０件の場合は、元の画面へ戻す
        if (mailtemplateSendModel.getMailSendDto() == null
            || mailtemplateSendModel.getMailSendDto().getMailDtoList() == null || mailtemplateSendModel.getMailSendDto()
                                                                                                       .getMailDtoList()
                                                                                                       .isEmpty()) {
            this.addMessage(MSGCD_NOT_EXISTTENCE_ADDRESS, redirectAttributes, model);
            return "redirect:" + previousPage;
        }

        // 見出し一覧を取得
        final HTypeMailTemplateType[] typeArray = mailtemplateSendModel.getMailSendDto()
                                                                       .getAvailableTemplateTypeList()
                                                                       .toArray(new HTypeMailTemplateType[] {});

        MailListGetRequest mailListGetRequest = mailtemplateSendHelper.toMailListGetRequest(typeArray);
        MailListResponse mailListResponse = notificationSubApi.getMails(mailListGetRequest);
        if (ObjectUtils.isEmpty(mailListResponse)) {
            // テンプレート情報が存在しない場合は不正操作として、元の画面へ戻す
            this.addMessage(MSGCD_ILLEGAL_OPERATION, redirectAttributes, model);
            return "redirect:" + previousPage;
        }

        final List<MailTemplateIndexDto> indexListOrig =
                        mailtemplateSendHelper.toListMailTemplateIndexDto(mailListResponse);
        List<MailTemplateIndexDto> indexList = null;
        // 受注詳細から来たときのみ順番を保持する
        if (!isFromOrderDetails(previousPage)) {
            indexList = indexListOrig;
        } else {
            indexList = new ArrayList<>();
            for (HTypeMailTemplateType type : typeArray) {
                for (MailTemplateIndexDto dto : indexListOrig) {
                    if (dto.getMailTemplateType() == type) {
                        indexList.add(dto);
                    }
                }
            }
        }
        this.mailtemplateSendHelper.toPageSelect(indexList, mailtemplateSendModel);

        // 初期化完了
        mailtemplateSendModel.setInitialized(true);

        // 選択可能なテンプレートが１件しかない場合はそれを選択し、自動で編集画面へ進む
        // メールテンプレートが未登録のメールテンプレートタイプも１件とカウントすることにした。
        int availableCount = 0;
        Integer mailTemplateSeq = null;
        String mailTemplateType = null;

        for (MailtemplateSelectItem item : mailtemplateSendModel.getIndexItems()) {
            availableCount++;
            if (item.isEmptyTemplate()) {
                continue;
            }
            mailTemplateSeq = item.getMailTemplateSeq();
            mailTemplateType = item.getMailTemplateType();

        }

        // 選択画面をスキップする
        if (availableCount == 1 && mailTemplateSeq != null && mailTemplateType != null) {
            mailtemplateSendModel.setMailTemplateSeq(mailTemplateSeq);
            mailtemplateSendModel.setMailTemplateType(mailTemplateType);
            mailtemplateSendModel.setSkippedSelectPage(true);
            return "redirect:/mailtemplate/send/edit";
        }

        return "mailtemplate/send/select";
    }

    /**
     * 遷移元画面へ戻る
     *
     * @param mailtemplateSendModel
     * @param redirectAttributes
     * @param model
     * @param sessionStatus
     * @return 遷移元画面
     */
    @PostMapping(value = "/select", params = "doPreviousPage")
    public String doPreviousPageSelect(MailtemplateSendModel mailtemplateSendModel,
                                       RedirectAttributes redirectAttributes,
                                       Model model,
                                       SessionStatus sessionStatus) {

        // 遷移元ページ
        String previousPage = mailtemplateSendModel.getPreviousPage();

        // 遷移元ページが見つからない場合はエラー画面に遷移
        if (previousPage == null) {
            this.addMessage(MSGCD_ILLEGAL_OPERATION, redirectAttributes, model);
            return "redirect:/error";
        }

        sessionStatus.setComplete();

        return "redirect:" + previousPage;
    }

    /**
     * メール編集画面へ遷移
     *
     * @param mailtemplateSendModel
     * @param error
     * @param redirectAttributes
     * @param model
     * @return 編集画面へリダイレクト
     */
    @PostMapping(value = "/select", params = "doEditPage")
    public String doEditPage(@Validated(SelectGroup.class) MailtemplateSendModel mailtemplateSendModel,
                             BindingResult error,
                             RedirectAttributes redirectAttributes,
                             Model model) {

        if (error.hasErrors()) {
            return "mailtemplate/send/select";
        }

        // 遷移元ページ
        String previousPage = mailtemplateSendModel.getPreviousPage();

        // 遷移元ページが見つからない場合はエラー画面に遷移
        if (previousPage == null) {
            this.addMessage(MSGCD_ILLEGAL_OPERATION, redirectAttributes, model);
            return "redirect:/error";
        }

        return "redirect:/mailtemplate/send/edit";
    }

    /**
     * メール編集画面へ遷移
     *
     * @param mailtemplateSendModel
     * @param redirectAttributes
     * @param model
     * @return 編集画面
     */
    @GetMapping("/edit")
    @HEHandler(exception = AppLevelListException.class, returnView = "mailtemplate/send/edit")
    protected String doLoadEdit(MailtemplateSendModel mailtemplateSendModel,
                                RedirectAttributes redirectAttributes,
                                Model model) {

        // あるべきものがない場合
        if (mailtemplateSendModel.getMailSendDto().getApplication() == null) {
            addMessage(MSGCD_ILLEGAL_OPERATION, redirectAttributes, model);
            return "redirect:/error";
        }

        mailtemplateSendModel.setMailSent(false);
        mailtemplateSendModel.setMailSendFailure(false);

        // 確認画面から戻ってきた場合
        if (mailtemplateSendModel.isBackwardTransition()) {
            mailtemplateSendModel.setBackwardTransition(false);
            return "mailtemplate/send/edit";
        }

        // テスト送信先アドレスをセットする
        // テストメール送信先にログインユーザのアドレスを設定する
        AdministratorResponse administratorResponse = administratorApi.getByAdministratorSeq(
                        getCommonInfo().getCommonInfoAdministrator().getAdministratorSeq());
        AdministratorEntity aEntity =
                        mailtemplateSendHelper.toAdministratorEntityFromAdministratorResponse(administratorResponse);
        if (aEntity != null) {
            mailtemplateSendModel.setTestAddress(aEntity.getMail());
        }

        // SEQに該当するメールテンプレートを取得し画面項目へ設定する
        MailTemplateEntity entity = ApplicationContextUtility.getBean(MailTemplateEntity.class);

        // ページのパラメータとして受け取った情報を元にエンティティを取得する
        if (mailtemplateSendModel.getMailTemplateType() != null) {
            Integer seq = mailtemplateSendModel.getMailTemplateSeq();
            MailResponse mailResponse = notificationSubApi.getByMailTemplateSeq(seq);
            entity = mailtemplateSendHelper.toMailTemplateEntity(mailResponse);
        }

        mailtemplateSendHelper.toPageForLoad(entity, mailtemplateSendModel);

        return "mailtemplate/send/edit";
    }

    /**
     * テストメール送信
     *
     * @param mailtemplateSendModel
     * @param error
     * @param redirectAttributes
     * @param model
     * @return 編集画面
     */
    @PostMapping(value = "/edit", params = "doSendTestMail")
    @HEHandler(exception = AppLevelListException.class, returnView = "mailtemplate/send/edit")
    public String doSendTestMail(@Validated(SendTestGroup.class) MailtemplateSendModel mailtemplateSendModel,
                                 BindingResult error,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {

        if (error.hasErrors()) {
            return "mailtemplate/send/edit";
        }

        // テンプレートタイプから送信するメールを切り分ける
        // 注文確認メール
        if (HTypeMailTemplateType.ORDER_CONFIRMATION.getValue().equals(mailtemplateSendModel.getMailTemplateType())) {
            OrderConfirmationRequest orderConfirmationRequest = new OrderConfirmationRequest();
            orderConfirmationRequest.setOrderCode(mailtemplateSendModel.getMailSendDto().getOrderCode());
            // フラグとテスト用の送信先メールアドレスをセット
            orderConfirmationRequest.setIsSendTestOnly(true);
            orderConfirmationRequest.setTestMailAddress(mailtemplateSendModel.getTestAddress());

            Object[] args = new Object[] {orderConfirmationRequest};
            Class<?>[] argsClass = new Class<?>[] {OrderConfirmationRequest.class};
            AsyncUtil.asyncService(NotificationSubApi.class, "orderConfirmation", args, argsClass);
        }
        // 出荷完了メール
        else if (HTypeMailTemplateType.SHIPMENT_NOTIFICATION.getValue()
                                                            .equals(mailtemplateSendModel.getMailTemplateType())) {
            if (mailtemplateSendModel.getMailSendDto().getMailDtoList() != null) {
                String orderCode = mailtemplateSendModel.getMailSendDto().getOrderCode();

                ShipmentNotificationRequest shipmentNotificationRequest = new ShipmentNotificationRequest();
                List<String> orderCodeList = new ArrayList<>();
                orderCodeList.add(orderCode);
                shipmentNotificationRequest.setOrderCodeList(orderCodeList);
                // フラグとテスト用の送信先メールアドレスをセット
                shipmentNotificationRequest.setIsSendTestOnly(true);
                shipmentNotificationRequest.setTestMailAddress(mailtemplateSendModel.getTestAddress());

                Object[] args = new Object[] {shipmentNotificationRequest};
                Class<?>[] argsClass = new Class<?>[] {ShipmentNotificationRequest.class};
                AsyncUtil.asyncService(NotificationSubApi.class, "shipmentNotification", args, argsClass);
            }
        }
        // 受注決済督促メール
        else if (HTypeMailTemplateType.SETTLEMENT_REMINDER.getValue()
                                                          .equals(mailtemplateSendModel.getMailTemplateType())) {
            if (mailtemplateSendModel.getMailSendDto().getMailDtoList() != null) {
                String orderCode = mailtemplateSendModel.getMailSendDto().getOrderCode();

                SettlementReminderRequest settlementReminderRequest = new SettlementReminderRequest();
                List<String> orderCodeList = new ArrayList<>();
                orderCodeList.add(orderCode);
                settlementReminderRequest.setOrderCodeList(orderCodeList);
                // フラグとテスト用の送信先メールアドレスをセット
                settlementReminderRequest.setIsSendTestOnly(true);
                settlementReminderRequest.setTestMailAddress(mailtemplateSendModel.getTestAddress());

                Object[] args = new Object[] {settlementReminderRequest};
                Class<?>[] argsClass = new Class<?>[] {SettlementReminderRequest.class};
                AsyncUtil.asyncService(NotificationSubApi.class, "settlementReminder", args, argsClass);
            }
        }
        // 受注決済期限切れメール
        else if (HTypeMailTemplateType.SETTLEMENT_EXPIRATION_NOTIFICATION.getValue()
                                                                         .equals(mailtemplateSendModel.getMailTemplateType())) {
            if (mailtemplateSendModel.getMailSendDto().getMailDtoList() != null) {
                String orderCode = mailtemplateSendModel.getMailSendDto().getOrderCode();

                SettlementExpirationNotificationRequest settlementExpirationNotificationRequest =
                                new SettlementExpirationNotificationRequest();
                List<String> orderCodeList = new ArrayList<>();
                orderCodeList.add(orderCode);
                settlementExpirationNotificationRequest.setOrderCodeList(orderCodeList);
                // フラグとテスト用の送信先メールアドレスをセット
                settlementExpirationNotificationRequest.setIsSendTestOnly(true);
                settlementExpirationNotificationRequest.setTestMailAddress(mailtemplateSendModel.getTestAddress());

                Object[] args = new Object[] {settlementExpirationNotificationRequest};
                Class<?>[] argsClass = new Class<?>[] {SettlementExpirationNotificationRequest.class};
                AsyncUtil.asyncService(NotificationSubApi.class, "settlementExpirationNotificationMail", args, argsClass);
            }
        }
        // 検査結果通知メール
        else if (HTypeMailTemplateType.EXAM_RESULTS_NOTIFICATION.getValue()
                .equals(mailtemplateSendModel.getMailTemplateType())) {
            if (mailtemplateSendModel.getMailSendDto().getMailDtoList() != null) {
                String orderCode = mailtemplateSendModel.getMailSendDto().getOrderCode();

                ExamResultsNoticeRequest examResultsNoticeRequest = new ExamResultsNoticeRequest();
                List<String> orderCodeList = new ArrayList<>();
                orderCodeList.add(orderCode);
                examResultsNoticeRequest.setOrderCodeList(orderCodeList);
                // フラグとテスト用の送信先メールアドレスをセット
                examResultsNoticeRequest.setIsSendTestOnly(true);
                examResultsNoticeRequest.setTestMailAddress(mailtemplateSendModel.getTestAddress());

                Object[] args = new Object[] {examResultsNoticeRequest};
                Class<?>[] argsClass = new Class<?>[] {ExamResultsNoticeRequest.class};
                AsyncUtil.asyncService(NotificationSubApi.class, "examResultsNotice", args, argsClass);
            }
        }
        mailtemplateSendModel.setMailSent(true);
        mailtemplateSendModel.setMailSendFailure(false);

        return "mailtemplate/send/edit";
    }

    /**
     * 遷移元アプリケーションに戻る
     *
     * @param mailtemplateSendModel
     * @param redirectAttributes
     * @param model
     * @param sessionStatus
     * @return 遷移元画面
     */
    @PostMapping(value = "/edit", params = "doGoBackPreviousSubapplication")
    public String doGoBackPreviousSubapplication(MailtemplateSendModel mailtemplateSendModel,
                                                 RedirectAttributes redirectAttributes,
                                                 Model model,
                                                 SessionStatus sessionStatus) {

        if (mailtemplateSendModel.isHavingBackwardPage()) {
            redirectAttributes.addFlashAttribute("md", MODE_LIST);
            String previousPage = mailtemplateSendModel.getPreviousPage();
            sessionStatus.setComplete();
            return "redirect:" + previousPage;
        }

        return "mailtemplate/send/edit";
    }

    /**
     * 選択画面に戻る
     *
     * @return 遷移元画面
     */
    @PostMapping(value = "/edit", params = "doSelectPage")
    public String doBackSelect() {
        return "redirect:/mailtemplate/send/select";
    }

    /**
     * 送信確認画面へ遷移
     *
     * @param mailtemplateSendModel
     * @param error
     * @param redirectAttributes
     * @param model
     * @return 送信確認画面へリダイレクト
     */
    @PostMapping(value = "/edit", params = "doConfirmSendPage")
    @HEHandler(exception = AppLevelListException.class, returnView = "mailtemplate/send/confirm")
    public String doConfirmSendPage(@Validated(ConfirmGroup.class) MailtemplateSendModel mailtemplateSendModel,
                                    BindingResult error,
                                    RedirectAttributes redirectAttributes,
                                    Model model) {

        if (error.hasErrors()) {
            return "mailtemplate/send/edit";
        }

        // テスト送信先が空の場合、管理者のアドレスをセットしなおす
        if (StringUtil.isEmpty(mailtemplateSendModel.getTestAddress())) {
            AdministratorResponse administratorResponse = administratorApi.getByAdministratorSeq(
                            getCommonInfo().getCommonInfoAdministrator().getAdministratorSeq());
            AdministratorEntity administratorEntity =
                            mailtemplateSendHelper.toAdministratorEntityFromAdministratorResponse(
                                            administratorResponse);
            if (administratorEntity != null) {
                mailtemplateSendModel.setTestAddress(administratorEntity.getMail());
            }
        }

        return "redirect:/mailtemplate/send/confirm";
    }

    /**
     * 送信確認画面へ遷移
     *
     * @param mailtemplateSendModel
     * @param redirectAttributes
     * @param model
     * @return 送信確認画面
     */
    @GetMapping("/confirm")
    protected String doLoadConfirm(MailtemplateSendModel mailtemplateSendModel,
                                   RedirectAttributes redirectAttributes,
                                   Model model) {

        // あるべきものがない場合
        if (mailtemplateSendModel.getMailSendDto().getApplication() == null) {
            addMessage(MSGCD_ILLEGAL_OPERATION, redirectAttributes, model);
            return "redirect:/error";
        }

        mailtemplateSendModel.setBackwardTransition(true);

        return "mailtemplate/send/confirm";
    }

    /**
     * 編集画面に戻る
     *
     * @return 遷移元画面
     */
    @PostMapping(value = "/confirm", params = "doEditPage")
    public String doBackEdit() {
        return "redirect:/mailtemplate/send/edit";
    }

    /**
     * メールを送信する
     *
     * @param mailtemplateSendModel
     * @param redirectAttributes
     * @param model
     * @param sessionStatus
     * @return 完了画面へリダイレクト
     */
    @PostMapping(value = "/confirm", params = "doOnceSendMail")
    @HEHandler(exception = AppLevelListException.class, returnView = "mailtemplate/send/confirm")
    public String doOnceSendMail(MailtemplateSendModel mailtemplateSendModel,
                                 RedirectAttributes redirectAttributes,
                                 Model model,
                                 SessionStatus sessionStatus) {

        mailtemplateSendModel.setFailureList(new ArrayList<>());

        // テンプレートタイプから送信するメールを切り分ける
        // 注文確認メール
        if (HTypeMailTemplateType.ORDER_CONFIRMATION.getValue().equals(mailtemplateSendModel.getMailTemplateType())) {
            OrderConfirmationRequest orderConfirmationRequest = new OrderConfirmationRequest();
            orderConfirmationRequest.setOrderCode(mailtemplateSendModel.getMailSendDto().getOrderCode());

            if (mailtemplateSendModel.isSendMeToo()) {
                orderConfirmationRequest.setIsSendAdmin(true);
                orderConfirmationRequest.setTestMailAddress(mailtemplateSendModel.getTestAddress());
            }

            Object[] args = new Object[] {orderConfirmationRequest};
            Class<?>[] argsClass = new Class<?>[] {OrderConfirmationRequest.class};
            AsyncUtil.asyncService(NotificationSubApi.class, "orderConfirmation", args, argsClass);
        }
        // 出荷完了メール
        else if (HTypeMailTemplateType.SHIPMENT_NOTIFICATION.getValue()
                                                            .equals(mailtemplateSendModel.getMailTemplateType())) {
            if (mailtemplateSendModel.getMailSendDto().getMailDtoList() != null) {
                String orderCode = mailtemplateSendModel.getMailSendDto().getOrderCode();

                ShipmentNotificationRequest shipmentNotificationRequest = new ShipmentNotificationRequest();
                List<String> orderCodeList = new ArrayList<>();
                orderCodeList.add(orderCode);
                shipmentNotificationRequest.setOrderCodeList(orderCodeList);

                if (mailtemplateSendModel.isSendMeToo()) {
                    shipmentNotificationRequest.setIsSendAdmin(true);
                    shipmentNotificationRequest.setTestMailAddress(mailtemplateSendModel.getTestAddress());
                }

                Object[] args = new Object[] {shipmentNotificationRequest};
                Class<?>[] argsClass = new Class<?>[] {ShipmentNotificationRequest.class};
                AsyncUtil.asyncService(NotificationSubApi.class, "shipmentNotification", args, argsClass);
            }
        }
        // 受注決済督促メール
        else if (HTypeMailTemplateType.SETTLEMENT_REMINDER.getValue()
                                                          .equals(mailtemplateSendModel.getMailTemplateType())) {
            if (mailtemplateSendModel.getMailSendDto().getMailDtoList() != null) {
                String orderCode = mailtemplateSendModel.getMailSendDto().getOrderCode();

                SettlementReminderRequest settlementReminderRequest = new SettlementReminderRequest();
                List<String> orderCodeList = new ArrayList<>();
                orderCodeList.add(orderCode);
                settlementReminderRequest.setOrderCodeList(orderCodeList);

                if (mailtemplateSendModel.isSendMeToo()) {
                    settlementReminderRequest.setIsSendAdmin(true);
                    settlementReminderRequest.setTestMailAddress(mailtemplateSendModel.getTestAddress());
                }

                Object[] args = new Object[] {settlementReminderRequest};
                Class<?>[] argsClass = new Class<?>[] {SettlementReminderRequest.class};
                AsyncUtil.asyncService(NotificationSubApi.class, "settlementReminder", args, argsClass);
            }
        }
        // 受注決済期限切れメール
        else if (HTypeMailTemplateType.SETTLEMENT_EXPIRATION_NOTIFICATION.getValue()
                                                                         .equals(mailtemplateSendModel.getMailTemplateType())) {
            if (mailtemplateSendModel.getMailSendDto().getMailDtoList() != null) {
                String orderCode = mailtemplateSendModel.getMailSendDto().getOrderCode();

                SettlementExpirationNotificationRequest settlementExpirationNotificationRequest =
                                new SettlementExpirationNotificationRequest();
                List<String> orderCodeList = new ArrayList<>();
                orderCodeList.add(orderCode);
                settlementExpirationNotificationRequest.setOrderCodeList(orderCodeList);

                if (mailtemplateSendModel.isSendMeToo()) {
                    settlementExpirationNotificationRequest.setIsSendAdmin(true);
                    settlementExpirationNotificationRequest.setTestMailAddress(mailtemplateSendModel.getTestAddress());
                }

                Object[] args = new Object[] {settlementExpirationNotificationRequest};
                Class<?>[] argsClass = new Class<?>[] {SettlementExpirationNotificationRequest.class};
                AsyncUtil.asyncService(NotificationSubApi.class, "settlementExpirationNotificationMail", args, argsClass);
            }
        }
        // 検査結果通知メール
        else if (HTypeMailTemplateType.EXAM_RESULTS_NOTIFICATION.getValue()
                .equals(mailtemplateSendModel.getMailTemplateType())) {
            if (mailtemplateSendModel.getMailSendDto().getMailDtoList() != null) {
                String orderCode = mailtemplateSendModel.getMailSendDto().getOrderCode();

                ExamResultsNoticeRequest examResultsNoticeRequest = new ExamResultsNoticeRequest();
                List<String> orderCodeList = new ArrayList<>();
                orderCodeList.add(orderCode);
                examResultsNoticeRequest.setOrderCodeList(orderCodeList);

                if (mailtemplateSendModel.isSendMeToo()) {
                    examResultsNoticeRequest.setIsSendAdmin(true);
                    examResultsNoticeRequest.setTestMailAddress(mailtemplateSendModel.getTestAddress());
                }

                Object[] args = new Object[] {examResultsNoticeRequest};
                Class<?>[] argsClass = new Class<?>[] {ExamResultsNoticeRequest.class};
                AsyncUtil.asyncService(NotificationSubApi.class, "examResultsNotice", args, argsClass);
            }
        }

        // メール送信フラグをオンにする
        mailtemplateSendModel.setSendOk(true);

        // 完了画面をスキップする指定がある場合は、完了画面ではなく呼び出し元へ帰る
        if (mailtemplateSendModel.getMailSendDto().isSkipCompletePage()) {

            // 再検索フラグをセット
            mailtemplateSendModel.setMd(MODE_LIST);
            sessionStatus.setComplete();
            return "redirect:" + mailtemplateSendModel.getPreviousPage();
        }

        return "redirect:/mailtemplate/send/complete";
    }

    /**
     * 完了画面へ遷移
     *
     * @param mailtemplateSendModel
     * @param redirectAttributes
     * @param model
     * @return　完了画面
     */
    @GetMapping("/complete")
    protected String doLoadComplete(MailtemplateSendModel mailtemplateSendModel,
                                    RedirectAttributes redirectAttributes,
                                    Model model) {

        // ブラウザバックの場合、処理しない
        if (mailtemplateSendModel.getMailSendDto() == null) {
            addMessage(IdenticalDataCheckUtil.MSGCD_ILLEGAL_OPERATION, redirectAttributes, model);
            return "redirect:/order/";
        }

        // あるべきものがない場合
        if (mailtemplateSendModel.getMailSendDto().getApplication() == null) {
            addMessage(MSGCD_ILLEGAL_OPERATION, redirectAttributes, model);
            return "redirect:/error";
        }

        return "mailtemplate/send/complete";
    }

    /**
     * 遷移元サブアプリケーションへ戻る
     *
     * @param mailtemplateSendModel
     * @param redirectAttributes
     * @param model
     * @param sessionStatus
     * @return 遷移元画面
     */
    @PostMapping(value = "/complete", params = "doPreviousPage")
    public String doPreviousPageComplete(MailtemplateSendModel mailtemplateSendModel,
                                         RedirectAttributes redirectAttributes,
                                         Model model,
                                         SessionStatus sessionStatus) {

        // 再検索フラグをセット
        redirectAttributes.addFlashAttribute("md", MODE_LIST);

        // メール送信していた場合は送信フラグをONにする
        if (mailtemplateSendModel.isSendOk()) {
            redirectAttributes.addFlashAttribute("mailSentFlag", true);
        }

        String previousPage = mailtemplateSendModel.getPreviousPage();

        sessionStatus.setComplete();

        return "redirect:" + previousPage;
    }

    /**
     * 受注修正から遷移してきたか<br/>
     *
     * @param previousPage 元画面
     * @return true:受注修正から遷移してきた / false:それ以外
     */
    protected boolean isFromOrderDetails(String previousPage) {
        return ORDER_DETAILS_VIEWID.equals(previousPage);
    }

}