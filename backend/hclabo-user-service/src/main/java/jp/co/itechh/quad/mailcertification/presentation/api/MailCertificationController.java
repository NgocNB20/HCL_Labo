package jp.co.itechh.quad.mailcertification.presentation.api;

import io.swagger.annotations.ApiParam;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.constant.type.HTypeConfirmMailType;
import jp.co.itechh.quad.core.entity.memberinfo.MemberInfoEntity;
import jp.co.itechh.quad.core.entity.memberinfo.confirmmail.ConfirmMailEntity;
import jp.co.itechh.quad.core.logic.memberinfo.confirmmail.ConfirmMailGetLogic;
import jp.co.itechh.quad.core.service.memberinfo.MemberInfoUpdateMailGetService;
import jp.co.itechh.quad.core.service.memberinfo.password.PasswordResetMemberInfoGetService;
import jp.co.itechh.quad.core.service.memberinfo.password.PasswordResetSendMailService;
import jp.co.itechh.quad.core.web.AbstractController;
import jp.co.itechh.quad.mail_certification.presentation.api.UsersApi;
import jp.co.itechh.quad.mail_certification.presentation.api.param.ConfirmMailResponse;
import jp.co.itechh.quad.mail_certification.presentation.api.param.CustomerResponse;
import jp.co.itechh.quad.mail_certification.presentation.api.param.PasswordResetMailSendRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.sql.Timestamp;

/**
 * パスワードリセット controller.
 * @author PHAM QUANG DIEU (VJP)
 */
@RestController
public class MailCertificationController extends AbstractController implements UsersApi {

    /** 日付関連Helper取得 */
    private final DateUtility dateUtility;

    /** 会員メールアドレス変更サービス */
    private final MemberInfoUpdateMailGetService memberInfoUpdateMailGetService;

    /** パスワード再設定会員情報取得サービス */
    private final PasswordResetMemberInfoGetService passwordResetMemberInfoGetService;

    /** 確認メール情報取得ロジック */
    private final ConfirmMailGetLogic confirmMailGetLogic;

    /** パスワードリセットメール送信サービス */
    private final PasswordResetSendMailService passwordResetSendMailService;

    /** メール認証ヘルパー */
    private final MailCertificationHelper mailCertificationHelper;

    /**
     * Instantiates a new Mail certification controller.
     *
     * @param memberInfoUpdateMailGetService    会員メールアドレス変更サービス
     * @param passwordResetMemberInfoGetService パスワード再設定会員情報取得サービス
     * @param confirmMailGetLogic               確認メール情報取得ロジック
     * @param passwordResetSendMailService      パスワードリセットメール送信サービス
     */
    public MailCertificationController(MemberInfoUpdateMailGetService memberInfoUpdateMailGetService,
                                       PasswordResetMemberInfoGetService passwordResetMemberInfoGetService,
                                       ConfirmMailGetLogic confirmMailGetLogic,
                                       MailCertificationHelper mailCertificationHelper,
                                       PasswordResetSendMailService passwordResetSendMailService,
                                       DateUtility dateUtility) {
        this.memberInfoUpdateMailGetService = memberInfoUpdateMailGetService;
        this.passwordResetMemberInfoGetService = passwordResetMemberInfoGetService;
        this.confirmMailGetLogic = confirmMailGetLogic;
        this.mailCertificationHelper = mailCertificationHelper;
        this.passwordResetSendMailService = passwordResetSendMailService;
        this.dateUtility = dateUtility;
    }

    /**
     * GET /users/customers/mail-certifications/mailaddresses/change/{password} : メールアドレス変更対象会員情報取得
     * メールアドレス変更対象会員情報取得
     *
     * @param password パスワード (required)
     * @return 会員レスポンス (status code 200)
     * or その他エラー (status code 500)
     */
    @Override
    public ResponseEntity<CustomerResponse> getCustomerInfoUpdateMail(
                    @ApiParam(value = "パスワード", required = true) @PathVariable("password") String password) {
        MemberInfoEntity memberInfoEntity = memberInfoUpdateMailGetService.execute(password);
        CustomerResponse customerResponse = mailCertificationHelper.toCustomerResponse(memberInfoEntity);
        return new ResponseEntity<>(customerResponse, HttpStatus.OK);
    }

    /**
     * GET /users/customers/mail-certifications/password-reset/{password} : パスワード再設定対象会員情報取得
     * パスワード再設定対象会員情報取得
     *
     * @param password パスワード (required)
     * @return 会員レスポンス (status code 200)
     * or その他エラー (status code 500)
     */
    @Override
    public ResponseEntity<CustomerResponse> getCustomerInfoResetPassword(
                    @ApiParam(value = "パスワード", required = true) @PathVariable("password") String password) {

        MemberInfoEntity memberInfoEntity = passwordResetMemberInfoGetService.execute(password);
        CustomerResponse customerResponse = mailCertificationHelper.toCustomerResponse(memberInfoEntity);
        return new ResponseEntity<>(customerResponse, HttpStatus.OK);
    }

    /**
     * GET /users/customers/mail-certifications/confirms/{password} : 確認メール情報取得
     * 確認メール情報取得
     *
     * @param password パスワード (required)
     * @return 確認メールレスポンス (status code 200)
     * or その他エラー (status code 500)
     */
    @Override
    public ResponseEntity<ConfirmMailResponse> getConfirmMail(
                    @ApiParam(value = "パスワード", required = true) @PathVariable("password") String password) {

        ConfirmMailEntity confirmMailEntity =
                        confirmMailGetLogic.execute(password, HTypeConfirmMailType.PASSWORD_REISSUE);
        ConfirmMailResponse confirmMailResponse = mailCertificationHelper.toConfirmMailResponse(confirmMailEntity);
        return new ResponseEntity<>(confirmMailResponse, HttpStatus.OK);
    }

    /**
     * POST /users/customers/mail-certifications/password-reset-mails/send : パスワードリセットメール送信
     * パスワードリセットメール送信
     *
     * @param passwordResetMailSendRequest パスワードリセットリクエスト (required)
     * @return 成功 (status code 200)
     * or その他エラー (status code 500)
     */
    @Override
    public ResponseEntity<Void> sendMailPasswordReset(
                    @ApiParam(value = "パスワードリセットリクエスト", required = true) @Valid @RequestBody
                                    PasswordResetMailSendRequest passwordResetMailSendRequest) {

        Timestamp birthDay = dateUtility.convertDateToTimestamp(passwordResetMailSendRequest.getDateOfBirth());
        passwordResetSendMailService.execute(passwordResetMailSendRequest.getMemberInfoMail(), birthDay);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}