package jp.co.itechh.quad.administrator.presentation.api;

import io.swagger.annotations.ApiParam;
import jp.co.itechh.quad.administrator.presentation.api.param.AdministratorExistCheckResponse;
import jp.co.itechh.quad.administrator.presentation.api.param.AdministratorListGetRequest;
import jp.co.itechh.quad.administrator.presentation.api.param.AdministratorListResponse;
import jp.co.itechh.quad.administrator.presentation.api.param.AdministratorRegistRequest;
import jp.co.itechh.quad.administrator.presentation.api.param.AdministratorResponse;
import jp.co.itechh.quad.administrator.presentation.api.param.AdministratorSameCheckResponse;
import jp.co.itechh.quad.administrator.presentation.api.param.AdministratorUpdateRequest;
import jp.co.itechh.quad.administrator.presentation.api.param.AdministratorsSameCheckRequest;
import jp.co.itechh.quad.administrator.presentation.api.param.PageInfoRequest;
import jp.co.itechh.quad.core.dto.administrator.AdministratorSearchForDaoConditionDto;
import jp.co.itechh.quad.core.entity.administrator.AdministratorEntity;
import jp.co.itechh.quad.core.logic.administrator.AdminLogic;
import jp.co.itechh.quad.core.web.AbstractController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 運営者エンドポイント Controller
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@RestController
public class AdministratorController extends AbstractController implements UsersApi {

    /** 管理者情報取得ロジック<br/> */
    private final AdminLogic adminLogic;

    /** 運営者検索ページHelper<br/> */
    private final AdministratorHelper administratorHelper;

    /** 運営者情報取得失敗 */
    public static final String MSGCD_ADMINISTRATOR_NO_DATA = "AYO000201";

    /**
     * 運用者情報取得失敗<br/>
     * <code>MSGCD_ADMINISTRATORENTITYDTO_NULL</code><br/>
     */
    String MSGCD_ADMINISTRATORENTITYDTO_NULL = "SKA000101";

    public AdministratorController(AdminLogic adminLogic, AdministratorHelper administratorHelper) {
        this.adminLogic = adminLogic;
        this.administratorHelper = administratorHelper;
    }

    /**
     * GET /users/administrators/{administratorSeq} : 運営者取得
     * 運営者取得
     *
     * @param administratorSeq 運営者SEQ (required)
     * @return 運営者レスポンス (status code 200)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<AdministratorResponse> getByAdministratorSeq(Integer administratorSeq) {
        AdministratorEntity administratorEntity = adminLogic.getAdministrator(administratorSeq);
        AdministratorResponse administratorResponse = administratorHelper.toAdministratorResponse(administratorEntity);
        return new ResponseEntity<>(administratorResponse, HttpStatus.OK);

    }

    /**
     * POST /users/administrators : 運営者登録
     * 運営者登録
     *
     * @param administratorRegistRequest 運営者登録リクエスト (required)
     * @return 運営者レスポンス (status code 200)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<AdministratorResponse> regist(
                    @ApiParam(value = "運営者登録リクエスト", required = true) @Valid @RequestBody
                                    AdministratorRegistRequest administratorRegistRequest) {
        AdministratorEntity administratorEntity = administratorHelper.toEntityForRegist(administratorRegistRequest);
        adminLogic.register(administratorEntity);
        AdministratorEntity administratorEntityRes =
                        adminLogic.getAdministrator(administratorEntity.getAdministratorSeq());
        AdministratorResponse administratorResponse =
                        administratorHelper.toAdministratorResponse(administratorEntityRes);
        return new ResponseEntity<>(administratorResponse, HttpStatus.OK);

    }

    /**
     * POST /users/administrators/{administratorId}/exist-check : 運営者存在チェック
     * 運営者存在チェック
     *
     * @param administratorId 運営者ユーザID (required)
     * @return 運営者存在チェックレスポンス (status code 200)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<AdministratorExistCheckResponse> checkExist(
                    @ApiParam(value = "運営者ユーザID", required = true) @PathVariable("administratorId")
                                    String administratorId) {
        Boolean existedAdmin = adminLogic.isExistedAdmin(administratorId);
        AdministratorExistCheckResponse administratorExistCheckResponse =
                        administratorHelper.toAdministratorExistCheckResponse(existedAdmin);
        return new ResponseEntity<>(administratorExistCheckResponse, HttpStatus.OK);
    }

    /**
     * DELETE /users/administrators/{administratorSeq} : 運営者削除
     * 運営者削除
     *
     * @param administratorSeq 運営者SEQ (required)
     * @return 成功 (status code 200)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<Void> delete(@ApiParam(value = "運営者SEQ", required = true) @PathVariable("administratorSeq")
                                                       Integer administratorSeq) {
        AdministratorEntity administratorEntity = adminLogic.getAdministrator(administratorSeq);
        if (administratorEntity == null) {
            throwMessage(MSGCD_ADMINISTRATOR_NO_DATA);
        }
        adminLogic.delete(administratorEntity);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * GET /users/administrators : 運営者一覧取得
     * 運営者一覧取得
     *
     * @param administratorListGetRequest 運営者一覧取得リクエスト (required)
     * @param pageInfoRequest             ページ情報リクエスト（ページネーションのため） (optional)
     * @return 運営者一覧レスポンス (status code 200)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<AdministratorListResponse> get(
                    @NotNull @ApiParam(value = "運営者一覧取得リクエスト", required = true) @Valid
                                    AdministratorListGetRequest administratorListGetRequest,
                    @ApiParam("ページ情報リクエスト（ページネーションのため）") @Valid PageInfoRequest pageInfoRequest) {
        AdministratorSearchForDaoConditionDto administratorConditionDto =
                        administratorHelper.getAdministratorSearchForDaoConditionDto(administratorListGetRequest);
        // 取得
        List<AdministratorEntity> adminList = adminLogic.getList(administratorConditionDto);
        AdministratorListResponse administratorListResponse =
                        administratorHelper.toAdministratorListResponse(adminList);
        return new ResponseEntity<>(administratorListResponse, HttpStatus.OK);
    }

    /**
     * POST /users/administrators/same-check : 運営者同一チェック
     * 運営者同一チェック
     *
     * @param administratorsSameCheckRequest 運営者同一チェックリクエスト (required)
     * @return 運営者同一チェックレスポンス (status code 200)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<AdministratorSameCheckResponse> checkDuplicate(
                    @ApiParam(value = "運営者同一チェックリクエスト", required = true) @Valid @RequestBody
                                    AdministratorsSameCheckRequest administratorsSameCheckRequest) {
        Integer shopSeq = 1001;
        boolean administratorFlag =
                        adminLogic.isSameAdmin(administratorsSameCheckRequest.getAdministratorSeq(), shopSeq,
                                               administratorsSameCheckRequest.getUserId()
                                              );
        AdministratorSameCheckResponse administratorSameCheckResponse =
                        administratorHelper.toAdministratorSameCheckResponse(administratorFlag);
        return new ResponseEntity<>(administratorSameCheckResponse, HttpStatus.OK);
    }

    /**
     * PUT /users/administrators/{administratorSeq} : 運営者更新
     * 運営者更新
     *
     * @param administratorSeq           運営者SEQ (required)
     * @param administratorUpdateRequest 運営者更新リクエスト (required)
     * @return 運営者レスポンス (status code 200)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<AdministratorResponse> update(
                    @ApiParam(value = "運営者SEQ", required = true) @PathVariable("administratorSeq")
                                    Integer administratorSeq,
                    @ApiParam(value = "運営者更新リクエスト", required = true) @Valid @RequestBody
                                    AdministratorUpdateRequest administratorUpdateRequest) {
        AdministratorEntity administratorEntity = adminLogic.getAdministrator(administratorSeq);
        if (ObjectUtils.isEmpty(administratorEntity)) {
            throwMessage(MSGCD_ADMINISTRATORENTITYDTO_NULL);
        }
        administratorHelper.toAdministratorEntity(administratorEntity, administratorUpdateRequest);
        adminLogic.update(administratorEntity);
        administratorEntity = adminLogic.getAdministrator(administratorSeq);
        AdministratorResponse administratorResponse = administratorHelper.toAdministratorResponse(administratorEntity);
        return new ResponseEntity<>(administratorResponse, HttpStatus.OK);
    }
}