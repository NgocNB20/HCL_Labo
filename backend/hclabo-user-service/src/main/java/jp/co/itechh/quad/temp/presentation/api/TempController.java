package jp.co.itechh.quad.temp.presentation.api;

import io.swagger.annotations.ApiParam;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.ApplicationLogUtility;
import jp.co.itechh.quad.core.dto.memberinfo.TempMemberInfoDto;
import jp.co.itechh.quad.core.entity.memberinfo.confirmmail.ConfirmMailEntity;
import jp.co.itechh.quad.core.service.memberinfo.temp.TempMemberInfoGetService;
import jp.co.itechh.quad.core.service.memberinfo.temp.TempMemberInfoRegistService;
import jp.co.itechh.quad.core.web.AbstractController;
import jp.co.itechh.quad.temp.presentation.api.param.TempCustomerRegistRequest;
import jp.co.itechh.quad.temp.presentation.api.param.TempCustomerResponse;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 仮会員　Controller
 *
 * @author PHAM QUANG DIEU (VJP)
 *
 */
@RestController
public class TempController extends AbstractController implements UsersApi {

    /** 仮会員情報取得サービス */
    private final TempMemberInfoGetService tempMemberInfoGetService;

    /** 仮会員登録サービス */
    private final TempMemberInfoRegistService tempMemberInfoRegistService;

    /** 仮会員 Helper */
    private final TempHelper tempHelper;

    /** DB一意制約例外 */
    protected static final String MSGCD_DB_UNIQUE_CONFIRMMAIL_PASSWORD_FAIL = "AMR000101";

    /**
     * 会員コンストラクター
     * @param tempMemberInfoGetService 仮会員情報取得サービス
     * @param tempMemberInfoRegistService 仮会員登録サービス
     * @param tempHelper 仮会員 Helper
     */
    public TempController(TempMemberInfoGetService tempMemberInfoGetService,
                          TempMemberInfoRegistService tempMemberInfoRegistService,
                          TempHelper tempHelper) {
        this.tempMemberInfoGetService = tempMemberInfoGetService;
        this.tempMemberInfoRegistService = tempMemberInfoRegistService;
        this.tempHelper = tempHelper;
    }

    /**
     * GET /users/customers/temps/{password} : 仮会員情報取得
     * 仮会員情報取得
     *
     * @param password パスワード (required)
     * @return 仮会員レスポンス (status code 200)
     */
    @Override
    public ResponseEntity<TempCustomerResponse> getMemberTemp(
                    @ApiParam(value = "パスワード", required = true) @PathVariable("password") String password) {
        // 仮会員情報取得
        TempMemberInfoDto tempMemberInfoDto = tempMemberInfoGetService.execute(password);

        TempCustomerResponse tempCustomerResponse = tempHelper.toTempCustomerResponse(tempMemberInfoDto);

        return new ResponseEntity<>(tempCustomerResponse, HttpStatus.OK);
    }

    /**
     * POST /users/customers/temps : 仮会員登録
     * 仮会員登録
     *
     * @param tempCustomerRegistRequest 会員登録リクエスト (required)
     * @return 会員レスポンス (status code 200)
     *         or その他エラー (status code 500)
     */
    @Override
    public ResponseEntity<TempCustomerResponse> registTemps(
                    @ApiParam(value = "会員登録リクエスト", required = true) @Valid @RequestBody
                                    TempCustomerRegistRequest tempCustomerRegistRequest) {
        TempCustomerResponse tempCustomerResponse = new TempCustomerResponse();
        try {
            // 仮会員登録サービス実行
            ConfirmMailEntity confirmMailEntity = tempMemberInfoRegistService.executeMemberRegist(
                            tempCustomerRegistRequest.getMemberInfoMail(), null);

            TempMemberInfoDto tempMemberInfoDto =
                            tempMemberInfoGetService.execute(confirmMailEntity.getConfirmMailPassword());

            tempCustomerResponse = tempHelper.toTempCustomerResponse(tempMemberInfoDto);

        } catch (DuplicateKeyException dke) {
            // Exceptionログを出力しておく
            ApplicationLogUtility appLogUtility = ApplicationContextUtility.getBean(ApplicationLogUtility.class);
            appLogUtility.writeExceptionLog(dke);

            throwMessage(MSGCD_DB_UNIQUE_CONFIRMMAIL_PASSWORD_FAIL);
        }

        return new ResponseEntity<>(tempCustomerResponse, HttpStatus.OK);
    }
}