package jp.co.itechh.quad.notification.presentation.api;

import io.swagger.annotations.ApiParam;
import jp.co.itechh.quad.core.constant.type.HTypeMailTemplateType;
import jp.co.itechh.quad.core.dto.shop.mail.MailTemplateIndexDto;
import jp.co.itechh.quad.core.entity.shop.mail.MailTemplateEntity;
import jp.co.itechh.quad.core.service.mailtemplate.MailTemplateGetIndexListService;
import jp.co.itechh.quad.core.service.mailtemplate.MailTemplateGetService;
import jp.co.itechh.quad.notificationsub.presentation.api.NotificationsApi;
import jp.co.itechh.quad.notificationsub.presentation.api.param.MailListGetRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.MailListResponse;
import jp.co.itechh.quad.notificationsub.presentation.api.param.MailResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 通知サブドメイン Controller
 *
 * @author PHAM QUANG DIEU (VJP)
 */
@RestController
public class NotificationController implements NotificationsApi {

    /** メールテンプレート詳細取得 Helper */
    private final NotificationHelper notificationHelper;

    /** メールテンプレート取得サービス */
    private final MailTemplateGetService mailTemplateGetService;

    /** 見出し取得サービス */
    private final MailTemplateGetIndexListService mailTemplateGetIndexListService;

    /**
     * コンストラクタ
     *
     * @param notificationHelper メールテンプレート詳細取得 Helper
     * @param mailTemplateGetService メールテンプレート取得サービス
     * @param mailTemplateGetIndexListService 見出し取得サービス
     */
    @Autowired
    public NotificationController(NotificationHelper notificationHelper,
                                  MailTemplateGetService mailTemplateGetService,
                                  MailTemplateGetIndexListService mailTemplateGetIndexListService) {
        this.notificationHelper = notificationHelper;
        this.mailTemplateGetService = mailTemplateGetService;
        this.mailTemplateGetIndexListService = mailTemplateGetIndexListService;
    }

    /**
     * GET /notifications/mails/{mailTemplateSeq} : メールテンプレート詳細取得
     * メールテンプレート詳細取得
     *
     * @param mailTemplateSeq メールテンプレートSEQ (required)
     * @return メール情報レスポンス (status code 200)
     *         or その他エラー (status code 200)
     */
    @Override
    public ResponseEntity<MailResponse> getByMailTemplateSeq(
                    @ApiParam(value = "メールテンプレートSEQ", required = true) @PathVariable("mailTemplateSeq")
                                    Integer mailTemplateSeq) {
        MailTemplateEntity entity = mailTemplateGetService.execute(mailTemplateSeq);
        MailResponse mailResponse = notificationHelper.toMailResponse(entity);
        return new ResponseEntity<>(mailResponse, HttpStatus.OK);
    }

    /**
     * GET /notifications/mails : メールテンプレート一覧取得
     * メールテンプレート一覧取得
     *
     * @param mailListGetRequest メール情報一覧取得リクエスト (required)
     * @return メール情報一覧レスポンス (status code 200)
     *         or その他エラー (status code 200)
     */
    @Override
    public ResponseEntity<MailListResponse> getMails(
                    @NotNull @ApiParam(value = "メール情報一覧取得リクエスト", required = true) @Valid
                                    MailListGetRequest mailListGetRequest) {
        HTypeMailTemplateType[] typeArray = notificationHelper.toHTypeMailTemplateType(mailListGetRequest);
        List<MailTemplateIndexDto> indexListOrig = this.mailTemplateGetIndexListService.execute(typeArray);
        MailListResponse mailListResponse = notificationHelper.toMailListResponse(indexListOrig);
        return new ResponseEntity<>(mailListResponse, HttpStatus.OK);
    }
}