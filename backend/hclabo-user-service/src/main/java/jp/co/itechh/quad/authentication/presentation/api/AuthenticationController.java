package jp.co.itechh.quad.authentication.presentation.api;

import io.swagger.annotations.ApiParam;
import jp.co.itechh.quad.authentication.presentation.api.param.AdminLoginRequest;
import jp.co.itechh.quad.authentication.presentation.api.param.AdminLoginResponse;
import jp.co.itechh.quad.authentication.presentation.api.param.CertificationCodeIssueRequest;
import jp.co.itechh.quad.authentication.presentation.api.param.CertificationCodeIssueResponse;
import jp.co.itechh.quad.authentication.presentation.api.param.MemberLoginRequest;
import jp.co.itechh.quad.authentication.presentation.api.param.MemberLoginResponse;
import jp.co.itechh.quad.authentication.presentation.api.param.MemberLoginUpdateRequest;
import jp.co.itechh.quad.authentication.presentation.api.param.MemberLoginUpdateResponse;
import jp.co.itechh.quad.authentication.presentation.api.param.RememberMeDeleteRequest;
import jp.co.itechh.quad.authentication.presentation.api.param.RememberMeGetRequest;
import jp.co.itechh.quad.authentication.presentation.api.param.RememberMeGetResponse;
import jp.co.itechh.quad.authentication.presentation.api.param.RememberMeRegistRequest;
import jp.co.itechh.quad.authentication.presentation.api.param.RememberMeRegistResponse;
import jp.co.itechh.quad.authentication.presentation.api.param.RememberMeUpdateRequest;
import jp.co.itechh.quad.authentication.presentation.api.param.RememberMeUpdateResponse;
import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.dao.authentication.PersistentRememberMeTokenDao;
import jp.co.itechh.quad.core.entity.administrator.AdministratorEntity;
import jp.co.itechh.quad.core.entity.authentication.PersistentRememberMeTokenEntity;
import jp.co.itechh.quad.core.entity.memberinfo.MemberInfoEntity;
import jp.co.itechh.quad.core.logic.administrator.AdminAuthGroupDetailLogic;
import jp.co.itechh.quad.core.logic.administrator.AdminLogic;
import jp.co.itechh.quad.core.logic.memberinfo.MemberInfoGetLogic;
import jp.co.itechh.quad.core.logic.memberinfo.MemberInfoUpdateLogic;
import jp.co.itechh.quad.core.utility.MemberInfoUtility;
import jp.co.itechh.quad.core.utility.OneTimePasswordUtility;
import jp.co.itechh.quad.core.web.AbstractController;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;

/**
 * 認証 Controller
 * @author Doan Thang (VJP)
 */
@RestController
public class AuthenticationController extends AbstractController implements LoginApi {

    /** 会員情報取得ロジック */
    private final MemberInfoGetLogic memberInfoGetLogic;

    /** 運営者情報操作ロジック */
    private final AdminLogic adminLogic;

    /** 権限グループ詳細ロジック */
    private final AdminAuthGroupDetailLogic adminAuthGroupDetailLogic;

    /** 会員業務ヘルパー */
    private final MemberInfoUtility memberInfoUtility;

    /** 認証 Helper */
    private final AuthenticationHelper authenticationHelper;

    /** PersistentRememberMeTokenDao */
    private final PersistentRememberMeTokenDao persistentRememberMeTokenDao;

    /** OneTimePasswordUtility */
    private final OneTimePasswordUtility oneTimePasswordUtility;

    /** 会員情報更新ロジック */
    private final MemberInfoUpdateLogic memberInfoUpdateLogic;

    /** 情報取得失敗エラー */
    public static final String MSGCD_ADMIN_LOGIN_ERROR = "USERID.NOT_FOUND.E";

    /** 会員情報取得失敗エラー */
    public static final String MSGCD_MEMBER_LOGIN_ERROR = "EMAIL.NOT_FOUND.E";

    /**
     * コンストラクター
     *
     * @param memberInfoGetLogic 会員情報取得ロジック
     * @param adminLogic 運営者情報操作ロジック
     * @param adminAuthGroupDetailLogic 権限グループ詳細ロジック
     * @param memberInfoUtility 会員業務ヘルパー
     * @param authenticationHelper 認証ヘルパー
     * @param persistentRememberMeTokenDao PersistentRememberMeTokenDao
     * @param memberInfoUpdateLogic 会員情報更新ロジック
     * @param oneTimePasswordUtility OneTimePasswordUtility
     */
    @Autowired
    public AuthenticationController(MemberInfoGetLogic memberInfoGetLogic,
                                    AdminLogic adminLogic,
                                    AdminAuthGroupDetailLogic adminAuthGroupDetailLogic,
                                    MemberInfoUtility memberInfoUtility,
                                    AuthenticationHelper authenticationHelper,
                                    PersistentRememberMeTokenDao persistentRememberMeTokenDao,
                                    MemberInfoUpdateLogic memberInfoUpdateLogic,
                                    OneTimePasswordUtility oneTimePasswordUtility) {
        this.memberInfoGetLogic = memberInfoGetLogic;
        this.adminLogic = adminLogic;
        this.adminAuthGroupDetailLogic = adminAuthGroupDetailLogic;
        this.memberInfoUtility = memberInfoUtility;
        this.authenticationHelper = authenticationHelper;
        this.persistentRememberMeTokenDao = persistentRememberMeTokenDao;
        this.memberInfoUpdateLogic = memberInfoUpdateLogic;
        this.oneTimePasswordUtility = oneTimePasswordUtility;
    }

    /**
     * POST /login/member/certificationcodeissue : 認証コード発行
     *
     * @param certificationCodeIssueRequest 認証コード発行リクエスト (required)
     * @return 認証コード発行レスポンス
     */
    @Override
    @SneakyThrows
    public ResponseEntity<CertificationCodeIssueResponse> issueCertificationCode(@Valid CertificationCodeIssueRequest certificationCodeIssueRequest) {
        String otp = oneTimePasswordUtility.generateTOTP(certificationCodeIssueRequest.getMemberInfoUniqueId());
        CertificationCodeIssueResponse response = new CertificationCodeIssueResponse();
        response.setCertificationCode(otp);
        response.setMemberInfoUniqueId(certificationCodeIssueRequest.getMemberInfoUniqueId());
        response.setCertificationCodeExpiry(PropertiesUtil.getSystemPropertiesValueToInt("certification-code.expiry"));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 管理者のログイン
     *
     * @param adminLoginRequest 管理者のログインリクエスト
     * @return 管理者のログインレスポンス
     */
    @Override
    public ResponseEntity<AdminLoginResponse> adminLogin(
                    @ApiParam(value = "管理者のログインリクエスト", required = true) @Valid @RequestBody
                                    AdminLoginRequest adminLoginRequest) {

        // 管理者情報取得
        Integer shopSeq = 1001;
        AdministratorEntity administratorEntity = adminLogic.getAdministrator(shopSeq, adminLoginRequest.getAdminId());
        if (ObjectUtils.isEmpty(administratorEntity)) {
            throwMessage(MSGCD_ADMIN_LOGIN_ERROR);
        }

        // 管理者のログインレスポンスへの変換
        AdminLoginResponse adminLoginResponse = authenticationHelper.toAdminLoginResponse(administratorEntity);

        // ユーザIDが存在する場合に、対象ユーザーの権限リストを取得する
        List<String> authorityList =
                        adminAuthGroupDetailLogic.getAuthorityList(administratorEntity.getAdminAuthGroupSeq());
        adminLoginResponse.setAuthorityList(authorityList);

        return new ResponseEntity<>(adminLoginResponse, HttpStatus.OK);
    }

    /**
     * 管理者のログイン
     *
     * @param memberLoginRequest 会員のログインリクエスト
     * @return 管理者のログインレスポンス
     */
    @Override
    public ResponseEntity<MemberLoginResponse> memberLogin(
                    @ApiParam(value = "会員のログインリクエスト", required = true) @Valid @RequestBody
                                    MemberLoginRequest memberLoginRequest) {
        // ユニークIDで取得する 大文字小文字の区別をなくす為
        Integer shopSeq = 1001;
        String shopUniqueId = memberInfoUtility.createShopUniqueId(shopSeq, memberLoginRequest.getMemberId());

        // 会員情報取得
        MemberInfoEntity memberInfoEntity = memberInfoGetLogic.execute(shopUniqueId);
        if (memberInfoEntity == null) {
            throwMessage(MSGCD_MEMBER_LOGIN_ERROR);
        }

        // 会員のログインレスポンスへの変換
        MemberLoginResponse response = authenticationHelper.toMemberLoginResponse(memberInfoEntity);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 会員のログイン日時の更新
     *
     * @param memberLoginUpdateRequest 会員のログイン日時の更新リクエスト
     * @return 会員のログイン日時の更新レスポンス
     */
    @Override
    public ResponseEntity<MemberLoginUpdateResponse> memberLoginUpdate(
                    @ApiParam(value = "会員のログイン日時の更新リクエスト", required = true) @Valid @RequestBody
                                    MemberLoginUpdateRequest memberLoginUpdateRequest) {
        // 取得した会員のログイン情報を更新
        memberInfoUpdateLogic.execute(
                        memberLoginUpdateRequest.getMemberInfoSeq(), memberLoginUpdateRequest.getUserAgent());

        // 会員情報取得
        MemberInfoEntity memberInfoEntity = memberInfoGetLogic.execute(memberLoginUpdateRequest.getMemberInfoSeq());
        if (memberInfoEntity == null) {
            throwMessage(MSGCD_MEMBER_LOGIN_ERROR);
        }

        MemberLoginUpdateResponse response = authenticationHelper.toMemberLoginUpdateResponse(memberInfoEntity);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * RememberMe取得
     *
     * @param rememberMeGetRequest PersistentRememberMeToken取得リクエスト
     * @return RememberMe取得レスポンス
     */
    @Override
    public ResponseEntity<RememberMeGetResponse> get(
                    @NotNull @ApiParam(value = "PersistentRememberMeToken取得リクエスト", required = true) @Valid
                                    RememberMeGetRequest rememberMeGetRequest) {
        // RememberMe情報を取得する
        PersistentRememberMeTokenEntity entity =
                        persistentRememberMeTokenDao.select(rememberMeGetRequest.getSeriesId());

        return new ResponseEntity<>(authenticationHelper.toRememberMeGetResponse(entity), HttpStatus.OK);
    }

    /**
     * RememberMe登録
     *
     * @param rememberMeRegistRequest RememberMe登録リクエスト
     * @return RememberMe登録レスポンス
     */
    @Override
    public ResponseEntity<RememberMeRegistResponse> post(
                    @ApiParam(value = "RememberMe登録リクエスト", required = true) @Valid @RequestBody
                                    RememberMeRegistRequest rememberMeRegistRequest) {

        // RememberMeを登録する
        PersistentRememberMeTokenEntity entity = new PersistentRememberMeTokenEntity();
        entity.setUsername(rememberMeRegistRequest.getUsername());
        entity.setSeries(rememberMeRegistRequest.getSeriesId());
        entity.setToken(rememberMeRegistRequest.getToken());
        entity.setLast_used(new Timestamp(rememberMeRegistRequest.getLastUsed().getTime()));
        persistentRememberMeTokenDao.insert(entity);

        return new ResponseEntity<>(authenticationHelper.toRememberMeRegistResponse(entity), HttpStatus.OK);
    }

    /**
     * RememberMe更新
     *
     * @param rememberMeUpdateRequest RememberMe更新リクエスト
     * @return RememberMe更新レスポンス
     */
    @Override
    public ResponseEntity<RememberMeUpdateResponse> put(
                    @ApiParam(value = "RememberMe更新リクエスト", required = true) @Valid @RequestBody
                                    RememberMeUpdateRequest rememberMeUpdateRequest) {

        // RememberMeを更新する
        PersistentRememberMeTokenEntity entity =
                        persistentRememberMeTokenDao.select(rememberMeUpdateRequest.getSeriesId());
        entity.setToken(rememberMeUpdateRequest.getToken());
        entity.setLast_used(new Timestamp(rememberMeUpdateRequest.getLastUsed().getTime()));
        persistentRememberMeTokenDao.update(entity);

        return new ResponseEntity<>(authenticationHelper.toRememberMeUpdateResponse(entity), HttpStatus.OK);
    }

    /**
     * RememberMe削除
     *
     * @param rememberMeDeleteRequest PersistentRememberMeToken削除リクエスト
     * @return 削除ステータス
     */
    @Override
    public ResponseEntity<Void> delete(
                    @NotNull @ApiParam(value = "PersistentRememberMeToken削除リクエスト", required = true) @Valid
                                    RememberMeDeleteRequest rememberMeDeleteRequest) {

        // RememberMeを削除する
        List<PersistentRememberMeTokenEntity> entityList =
                        persistentRememberMeTokenDao.getUsers(rememberMeDeleteRequest.getUsername());
        for (PersistentRememberMeTokenEntity entity : entityList) {
            persistentRememberMeTokenDao.delete(entity);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

}