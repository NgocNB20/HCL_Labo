package jp.co.itechh.quad.coupon.presentation.api;

import io.swagger.annotations.ApiParam;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.dto.shop.coupon.CouponSearchForDaoConditionDto;
import jp.co.itechh.quad.core.entity.shop.coupon.CouponEntity;
import jp.co.itechh.quad.core.logic.shop.coupon.CouponCheckLogic;
import jp.co.itechh.quad.core.logic.shop.coupon.CouponCodeCheckLogic;
import jp.co.itechh.quad.core.logic.shop.coupon.CouponDeleteLogic;
import jp.co.itechh.quad.core.logic.shop.coupon.CouponGetLogic;
import jp.co.itechh.quad.core.logic.shop.coupon.CouponRegistLogic;
import jp.co.itechh.quad.core.logic.shop.coupon.CouponSearchResultListgetLogic;
import jp.co.itechh.quad.core.logic.shop.coupon.CouponUpdateLogic;
import jp.co.itechh.quad.core.utility.CouponUtility;
import jp.co.itechh.quad.core.web.AbstractController;
import jp.co.itechh.quad.core.web.PageInfoModule;
import jp.co.itechh.quad.coupon.presentation.api.param.CouponCheckRequest;
import jp.co.itechh.quad.coupon.presentation.api.param.CouponCodeResponse;
import jp.co.itechh.quad.coupon.presentation.api.param.CouponListGetRequest;
import jp.co.itechh.quad.coupon.presentation.api.param.CouponListResponse;
import jp.co.itechh.quad.coupon.presentation.api.param.CouponRegistRequest;
import jp.co.itechh.quad.coupon.presentation.api.param.CouponResponse;
import jp.co.itechh.quad.coupon.presentation.api.param.CouponUpdateRequest;
import jp.co.itechh.quad.coupon.presentation.api.param.CouponVersionNoRequest;
import jp.co.itechh.quad.coupon.presentation.api.param.PageInfoRequest;
import jp.co.itechh.quad.coupon.presentation.api.param.PageInfoResponse;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * クーポンエンドポイント Controller
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@RestController
public class CouponController extends AbstractController implements PromotionsApi {

    /**
     * クーポン情報取得ロジック
     */
    private final CouponGetLogic couponGetLogic;

    /**
     * クーポン検索用Helper
     */
    private final CouponHelper couponHelper;

    /**
     * クーポン情報取得ロジック
     */
    private final CouponSearchResultListgetLogic couponSearchResultListgetLogic;

    /**
     * クーポン登録ロジック
     */
    private final CouponRegistLogic couponRegistLogic;

    /**
     * クーポン更新ロジック
     */
    private final CouponUpdateLogic couponUpdateLogic;

    /**
     * クーポン削除ロジック
     */
    private final CouponDeleteLogic couponDeleteLogic;

    /**
     * クーポンチェックロジック
     */
    private final CouponCheckLogic couponCheckLogic;

    /**
     *  クーポン関連ユーティリティクラス
     */
    private final CouponUtility couponUtility;

    /**
     * クーポンコードチェックロジック
     */
    private final CouponCodeCheckLogic couponCodeCheckLogic;

    /**
     * ロガー
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CouponController.class);

    /**
     * 検索画面からの遷移時に情報が取得できなかった場合エラー
     */
    public static final String MSGCD_NOT_GET_COUPONDATA = "ACP000401";

    /**
     * 検索画面からの遷移時に情報が取得できなかった場合エラー
     */
    static final String MSGCD_DONOT_GET_COUPONDATA = "ACP000201";

    /**
     * コンストラクタ
     *
     * @param couponGetLogic                 クーポン情報取得ロジック
     * @param couponHelper                   クーポン検索用Helper
     * @param couponSearchResultListgetLogic クーポン情報取得ロジック
     * @param couponRegistLogic              クーポン登録ロジック
     * @param couponUpdateLogic              クーポン更新ロジック
     * @param couponDeleteLogic              クーポン削除ロジック
     * @param couponCheckLogic               クーポンチェックロジック
     * @param couponUtility                  クーポン関連ユーティリティクラス
     * @param couponCodeCheckLogic           クーポンコードチェックロジック
     */
    public CouponController(CouponGetLogic couponGetLogic,
                            CouponHelper couponHelper,
                            CouponSearchResultListgetLogic couponSearchResultListgetLogic,
                            CouponRegistLogic couponRegistLogic,
                            CouponUpdateLogic couponUpdateLogic,
                            CouponDeleteLogic couponDeleteLogic,
                            CouponCheckLogic couponCheckLogic,
                            CouponUtility couponUtility,
                            CouponCodeCheckLogic couponCodeCheckLogic) {
        this.couponGetLogic = couponGetLogic;
        this.couponHelper = couponHelper;
        this.couponSearchResultListgetLogic = couponSearchResultListgetLogic;
        this.couponRegistLogic = couponRegistLogic;
        this.couponUpdateLogic = couponUpdateLogic;
        this.couponDeleteLogic = couponDeleteLogic;
        this.couponCheckLogic = couponCheckLogic;
        this.couponUtility = couponUtility;
        this.couponCodeCheckLogic = couponCodeCheckLogic;
    }

    /**
     * GET /promotions/coupons/{couponSeq} : クーポン情報取得
     * クーポン情報取得
     *
     * @param couponSeq クーポンSEQ (required)
     * @return クーポンレスポンス (status code 200)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<CouponResponse> getByCouponSeq(
                    @ApiParam(value = "クーポンSEQ", required = true) @PathVariable("couponSeq") Integer couponSeq) {
        CouponEntity couponEntity = couponGetLogic.execute(couponSeq);

        if (ObjectUtils.isEmpty(couponEntity)) {
            throwMessage(MSGCD_DONOT_GET_COUPONDATA);
        }

        CouponResponse couponResponse = couponHelper.toCouponResponse(couponEntity);
        return new ResponseEntity<>(couponResponse, HttpStatus.OK);
    }

    /**
     * GET /promotions/coupons/version-no : クーポン情報取得
     * クーポン情報取得
     *
     * @param couponVersionNoRequest クーポン情報を取得する (required)
     * @return クーポンレスポンス (status code 200)
     *         or Bad Request（バリデーションエラー、業務ルールエラー、妥当性チェックエラーの場合） (status code 400)
     *         or Internal Server Error（アサートエラー、予想外エラーの場合） (status code 500)
     *         or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<CouponResponse> getByCouponVersionNo(@NotNull
                                                               @ApiParam(value = "クーポン情報を取得する", required = true)
                                                               @Valid CouponVersionNoRequest couponVersionNoRequest) {
        CouponEntity couponEntity = couponGetLogic.execute(couponVersionNoRequest.getCouponSeq(),
                                                           couponVersionNoRequest.getCouponVersionNo()
                                                          );

        if (ObjectUtils.isEmpty(couponEntity)) {
            throwMessage(MSGCD_DONOT_GET_COUPONDATA);
        }

        CouponResponse couponResponse = couponHelper.toCouponResponse(couponEntity);
        return new ResponseEntity<>(couponResponse, HttpStatus.OK);
    }

    /**
     * GET /promotions/coupons : クーポン一覧取得
     * クーポン一覧取得
     *
     * @param couponListGetRequest クーポン一覧リクエスト (optional)
     * @param pageInfoRequest      ページ情報リクエスト (optional)
     * @return クーポン一覧レスポンス (status code 200)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<CouponListResponse> get(
                    @ApiParam("クーポン一覧リクエスト") @Valid CouponListGetRequest couponListGetRequest,
                    @ApiParam("ページ情報リクエスト") @Valid PageInfoRequest pageInfoRequest) {
        CouponSearchForDaoConditionDto conditionDto = couponHelper.toCouponConditionDtoForSearch(couponListGetRequest);
        // ページング検索セットアップ
        PageInfoModule pageInfoModule = ApplicationContextUtility.getBean(PageInfoModule.class);
        pageInfoModule.setupPageInfo(conditionDto, pageInfoRequest.getPage(), pageInfoRequest.getLimit(),
                                     pageInfoRequest.getOrderBy(), pageInfoRequest.getSort()
                                    );

        List<CouponEntity> couponList = couponSearchResultListgetLogic.execute(conditionDto);
        CouponListResponse couponListResponse = couponHelper.toCouponListResponse(couponList);

        // ページ情報レスポンスを設定
        PageInfoResponse pageInfoResponse = new PageInfoResponse();
        try {
            pageInfoModule.setupResponsePager(conditionDto, pageInfoResponse);
        } catch (InvocationTargetException | IllegalAccessException e) {
            LOGGER.info("ページ情報レスポンスの設定異常： " + e.getMessage());
        }
        couponListResponse.setPageInfo(pageInfoResponse);

        return new ResponseEntity<>(couponListResponse, HttpStatus.OK);
    }

    /**
     * GET /promotions/coupons/code : クーポンコード採番
     * クーポンコード採番
     *
     * @return クーポンコードレスポンス (status code 200)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<CouponCodeResponse> code() {
        String couponCode = this.makeCouponCode();
        CouponCodeResponse couponCodeResponse = couponHelper.toCouponCodeResponse(couponCode);
        return new ResponseEntity<>(couponCodeResponse, HttpStatus.OK);
    }

    /**
     * POST /promotions/coupons : クーポン登録
     * クーポン登録
     *
     * @param couponRegistRequest クーポン登録リクエスト (required)
     * @return クーポンレスポンス (status code 200)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<CouponResponse> regist(@ApiParam(value = "クーポン登録リクエスト", required = true) @Valid @RequestBody
                                                                 CouponRegistRequest couponRegistRequest) {
        CouponEntity couponEntity = couponHelper.toCouponEntityRegist(couponRegistRequest);
        // 登録可能チェックを行う
        couponCheckLogic.checkForRegist(couponEntity);

        couponRegistLogic.execute(couponEntity);

        couponEntity = couponGetLogic.execute(couponEntity.getCouponSeq());

        CouponResponse couponResponse = couponHelper.toCouponResponse(couponEntity);

        return new ResponseEntity<>(couponResponse, HttpStatus.OK);
    }

    /**
     * PUT /promotions/coupons/{couponSeq} : クーポン更新
     * クーポン更新
     *
     * @param couponSeq           クーポンSEQ (required)
     * @param couponUpdateRequest クーポン更新リクエスト (required)
     * @return クーポンレスポンス (status code 200)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<CouponResponse> update(
                    @ApiParam(value = "クーポンSEQ", required = true) @PathVariable("couponSeq") Integer couponSeq,
                    @ApiParam(value = "クーポン更新リクエスト", required = true) @Valid @RequestBody
                                    CouponUpdateRequest couponUpdateRequest) {
        // 登録可能チェックを行う
        CouponEntity preUpdateCoupon = couponHelper.toCouponEntity(couponUpdateRequest.getPreUpdateCoupon());
        CouponEntity postUpdateCoupon = couponHelper.toCouponEntity(couponUpdateRequest.getPostUpdateCoupon());

        couponCheckLogic.checkForUpdate(preUpdateCoupon, postUpdateCoupon);

        couponUpdateLogic.execute(postUpdateCoupon);

        CouponEntity couponEntity = couponGetLogic.execute(couponSeq);
        CouponResponse couponResponse = couponHelper.toCouponResponse(couponEntity);
        return new ResponseEntity<>(couponResponse, HttpStatus.OK);
    }

    /**
     * DELETE /promotions/coupons/{couponSeq} : クーポン削除
     * クーポン削除
     *
     * @param couponSeq クーポンSEQ (required)
     * @return 成功 (status code 200)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<Void> delete(
                    @ApiParam(value = "クーポンSEQ", required = true) @PathVariable("couponSeq") Integer couponSeq) {
        // 削除結果
        int result = couponDeleteLogic.execute(couponSeq);

        if (result == 0) {
            throwMessage(MSGCD_NOT_GET_COUPONDATA);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * POST /promotions/coupons/check : クーポン登録更新チェック
     * クーポン登録更新チェック
     *
     * @param couponCheckRequest クーポン登録更新チェックリクエスト (required)
     * @return 成功 (status code 200)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<Void> check(@ApiParam(value = "クーポン登録更新チェックリクエスト", required = true) @Valid @RequestBody
                                                      CouponCheckRequest couponCheckRequest) {

        // 登録可能チェックを行う
        CouponEntity postUpdateCoupon = couponHelper.toCouponEntity(couponCheckRequest.getPostUpdateCoupon());

        if (ObjectUtils.isEmpty(couponCheckRequest.getPreUpdateCoupon())) {
            couponCheckLogic.checkForRegist(postUpdateCoupon);
        } else {
            CouponEntity preUpdateCoupon = couponHelper.toCouponEntity(couponCheckRequest.getPreUpdateCoupon());
            couponCheckLogic.checkForUpdate(preUpdateCoupon, postUpdateCoupon);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * クーポンコードを自動生成する。<br />
     *
     * <pre>
     * プロパティファイルの設定よりランダムに生成する。
     * </pre>
     *
     * @return 自動生成したクーポンコード
     */
    private String makeCouponCode() {

        String couponCode;

        // 桁数をadminSystem.propertiesから取得する
        int couponCodeLength = couponUtility.getAutoGenerationCouponCodeLength();
        // 利用文字をadminSystem.propertiesから取得する
        String couponCodeCharacter = couponUtility.getCouponCodeUsableCharacter();

        // 既存のクーポンコードと重複しなくなるまで繰り返す
        while (true) {
            // クーポンコード生成
            couponCode = RandomStringUtils.random(couponCodeLength, couponCodeCharacter);

            // 既に利用されているクーポンコードでなければループを抜ける
            if (couponCodeCheckLogic.execute(couponCode)) {
                return couponCode;
            }
        }
    }
}