package jp.co.itechh.quad.inquirygroup.presentation.api;

import io.swagger.annotations.ApiParam;
import jp.co.itechh.quad.core.constant.type.HTypeSiteType;
import jp.co.itechh.quad.core.entity.inquiry.InquiryGroupEntity;
import jp.co.itechh.quad.core.logic.shop.inquiry.InquiryGroupRegistUpdateCheckLogic;
import jp.co.itechh.quad.core.service.shop.inquiry.InquiryGroupGetService;
import jp.co.itechh.quad.core.service.shop.inquiry.InquiryGroupListGetForBackService;
import jp.co.itechh.quad.core.service.shop.inquiry.InquiryGroupListGetService;
import jp.co.itechh.quad.core.service.shop.inquiry.InquiryGroupListUpdateService;
import jp.co.itechh.quad.core.service.shop.inquiry.InquiryGroupRegistService;
import jp.co.itechh.quad.core.service.shop.inquiry.InquiryGroupUpdateService;
import jp.co.itechh.quad.core.web.AbstractController;
import jp.co.itechh.quad.inquirygroup.presentation.api.param.InquiryGroupCheckRequest;
import jp.co.itechh.quad.inquirygroup.presentation.api.param.InquiryGroupListRequest;
import jp.co.itechh.quad.inquirygroup.presentation.api.param.InquiryGroupListResponse;
import jp.co.itechh.quad.inquirygroup.presentation.api.param.InquiryGroupListUpdateRequest;
import jp.co.itechh.quad.inquirygroup.presentation.api.param.InquiryGroupRegistRequest;
import jp.co.itechh.quad.inquirygroup.presentation.api.param.InquiryGroupRequest;
import jp.co.itechh.quad.inquirygroup.presentation.api.param.InquiryGroupResponse;
import jp.co.itechh.quad.inquirygroup.presentation.api.param.InquiryGroupUpdateRequest;
import jp.co.itechh.quad.inquirygroup.presentation.api.param.PageInfoRequest;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * お問い合わせ分類 Controller
 *
 * @author PHAM QUANG DIEU (VJP)
 */
@RestController
public class InquiryGroupController extends AbstractController implements UsersApi {

    /**
     * お問い合わせ分類取得
     */
    private final InquiryGroupGetService inquiryGroupGetService;

    /**
     * お問い合わせ分類リスト取得サービス
     */
    private final InquiryGroupListGetService inquiryGroupListGetService;

    /**
     * 問い合わせ分類登録
     */
    private final InquiryGroupRegistService inquiryGroupRegistService;

    /**
     * お問い合わせ分類リスト取得サービス(管理者用)
     */
    private final InquiryGroupListGetForBackService inquiryGroupListGetForBackService;

    /**
     * お問い合わせ分類登録更新チェックロジック
     */
    private final InquiryGroupRegistUpdateCheckLogic inquiryGroupRegistUpdateCheckLogic;

    /**
     * 問い合わせ分類更新
     */
    private final InquiryGroupUpdateService inquiryGroupUpdateService;

    /**
     * 問い合わせ分類リスト更新
     */
    private final InquiryGroupListUpdateService inquiryGroupListUpdateService;

    /**
     * お問い合わせ分類 Helper
     */
    private final InquiryGroupHelper inquiryGroupHelper;

    /**
     * コンストラクタ
     *
     * @param inquiryGroupGetService             お問い合わせ分類取得
     * @param inquiryGroupListGetService         お問い合わせ分類リスト取得サービス
     * @param inquiryGroupRegistService          問い合わせ分類登録
     * @param inquiryGroupListGetForBackService
     * @param inquiryGroupRegistUpdateCheckLogic お問い合わせ分類登録更新チェックロジック
     * @param inquiryGroupUpdateService          問い合わせ分類更新
     * @param inquiryGroupListUpdateService      問い合わせ分類リスト更新
     * @param inquiryGroupHelper                 お問い合わせ分類
     * @param inquiryGroupListGetForBackService  お問い合わせ分類リスト取得サービス
     */
    public InquiryGroupController(InquiryGroupGetService inquiryGroupGetService,
                                  InquiryGroupListGetService inquiryGroupListGetService,
                                  InquiryGroupRegistService inquiryGroupRegistService,
                                  InquiryGroupListGetForBackService inquiryGroupListGetForBackService,
                                  InquiryGroupRegistUpdateCheckLogic inquiryGroupRegistUpdateCheckLogic,
                                  InquiryGroupUpdateService inquiryGroupUpdateService,
                                  InquiryGroupListUpdateService inquiryGroupListUpdateService,
                                  InquiryGroupHelper inquiryGroupHelper) {
        this.inquiryGroupGetService = inquiryGroupGetService;
        this.inquiryGroupListGetService = inquiryGroupListGetService;
        this.inquiryGroupRegistService = inquiryGroupRegistService;
        this.inquiryGroupListGetForBackService = inquiryGroupListGetForBackService;
        this.inquiryGroupRegistUpdateCheckLogic = inquiryGroupRegistUpdateCheckLogic;
        this.inquiryGroupUpdateService = inquiryGroupUpdateService;
        this.inquiryGroupListUpdateService = inquiryGroupListUpdateService;
        this.inquiryGroupHelper = inquiryGroupHelper;
    }

    /**
     * GET /users/inquiries/groups/{inquiryGroupSeq} : 問い合わせ分類取得 問い合わせ分類取得
     *
     * @param inquiryGroupSeq 問い合わせ分類SEQ (required)
     * @return 問い合わせ分類レスポンス (status code 200) or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<InquiryGroupResponse> getByInquiryGroupSeq(
                    @ApiParam(value = "問い合わせ分類SEQ", required = true) @PathVariable("inquiryGroupSeq")
                                    Integer inquiryGroupSeq) {

        InquiryGroupEntity inquiryGroupEntity = inquiryGroupGetService.execute(inquiryGroupSeq);

        // レスポンス
        InquiryGroupResponse inquiryGroupResponse = inquiryGroupHelper.toInquiryGroupResponse(inquiryGroupEntity);
        return new ResponseEntity<>(inquiryGroupResponse, HttpStatus.OK);
    }

    /**
     * GET /users/inquiries/groups : お問い合わせ分類一覧取得 お問い合わせ分類一覧取得
     *
     * @param pageInfoRequest ページ情報リクエスト（ページネーションのため） (optional)
     * @return 問い合わせ分類一覧レスポンス (status code 200) or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<InquiryGroupListResponse> get(
                    @ApiParam("ページ情報リクエスト（ページネーションのため）") @Valid PageInfoRequest pageInfoRequest,
                    @Valid InquiryGroupListRequest inquiryGroupListRequest) {
        if (HTypeSiteType.FRONT_PC.getValue().equals(inquiryGroupListRequest.getSiteType())) {
            List<InquiryGroupEntity> inquiryGroupEntities = inquiryGroupListGetService.execute();

            InquiryGroupListResponse inquiryGroupListResponse =
                            inquiryGroupHelper.toInquiryGroupListResponse(inquiryGroupEntities);
            return new ResponseEntity<>(inquiryGroupListResponse, HttpStatus.OK);
        } else {
            List<InquiryGroupEntity> inquiryGroupEntities = inquiryGroupListGetForBackService.execute();

            InquiryGroupListResponse inquiryGroupListResponse =
                            inquiryGroupHelper.toInquiryGroupListResponse(inquiryGroupEntities);
            return new ResponseEntity<>(inquiryGroupListResponse, HttpStatus.OK);
        }
    }

    /**
     * POST /users/inquiries/groups : 問い合わせ分類登録 問い合わせ分類登録
     *
     * @param inquiryGroupRegistRequest 問い合わせ分類登録リクエスト (required)
     * @return 問い合わせ分類レスポンス (status code 200) or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<InquiryGroupResponse> regist(
                    @ApiParam(value = "問い合わせ分類登録リクエスト", required = true) @Valid @RequestBody
                                    InquiryGroupRegistRequest inquiryGroupRegistRequest) {
        InquiryGroupEntity inquiryGroupEntity =
                        inquiryGroupHelper.toInquiryGroupEntityFromRegistRequest(inquiryGroupRegistRequest);

        // 登録内容チェック
        inquiryGroupRegistUpdateCheckLogic.execute(inquiryGroupEntity);

        inquiryGroupRegistService.execute(inquiryGroupEntity);

        InquiryGroupEntity inquiryGroup = inquiryGroupGetService.execute(inquiryGroupEntity.getInquiryGroupSeq());
        InquiryGroupResponse inquiryGroupResponses = inquiryGroupHelper.toInquiryGroupResponse(inquiryGroup);

        return new ResponseEntity<>(inquiryGroupResponses, HttpStatus.OK);
    }

    /**
     * POST /users/inquiries/groups/check : 問い合わせ分類登録更新チェック 問い合わせ分類登録更新チェック
     *
     * @param inquiryGroupCheckRequest 問い合わせ分類チェックリクエスト (required)
     * @return 成功 (status code 200) or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<Void> check(@ApiParam(value = "問い合わせ分類チェックリクエスト", required = true) @Valid @RequestBody
                                                      InquiryGroupCheckRequest inquiryGroupCheckRequest) {
        InquiryGroupEntity inquiryGroupEntity =
                        inquiryGroupHelper.toInquiryGroupEntityFromCheckRequest(inquiryGroupCheckRequest);
        inquiryGroupRegistUpdateCheckLogic.execute(inquiryGroupEntity);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * PUT /users/inquiries/groups/{inquiryGroupSeq} : 問い合わせ分類更新 問い合わせ分類更新
     *
     * @param inquiryGroupSeq           問い合わせ分類SEQ (required)
     * @param inquiryGroupUpdateRequest 問い合わせ分類更新リクエスト (required)
     * @return 問い合わせ分類レスポンス (status code 200) or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<InquiryGroupResponse> update(
                    @ApiParam(value = "問い合わせ分類SEQ", required = true) @PathVariable("inquiryGroupSeq")
                                    Integer inquiryGroupSeq,
                    @ApiParam(value = "問い合わせ分類更新リクエスト", required = true) @Valid @RequestBody
                                    InquiryGroupUpdateRequest inquiryGroupUpdateRequest) {
        InquiryGroupEntity inquiryGroupEntity =
                        inquiryGroupHelper.toInquiryGroupEntityFromUpdateRequest(inquiryGroupSeq,
                                                                                 inquiryGroupUpdateRequest
                                                                                );

        // 登録内容チェック
        inquiryGroupRegistUpdateCheckLogic.execute(inquiryGroupEntity);

        inquiryGroupUpdateService.execute(inquiryGroupEntity);

        InquiryGroupEntity inquiryGroupRes = inquiryGroupGetService.execute(inquiryGroupSeq);
        InquiryGroupResponse inquiryGroupResponses = inquiryGroupHelper.toInquiryGroupResponse(inquiryGroupRes);

        return new ResponseEntity<>(inquiryGroupResponses, HttpStatus.OK);
    }

    /**
     * PUT /users/inquiries/groups : 問い合わせ分類一覧更新 問い合わせ分類一覧更新
     *
     * @param inquiryGroupListUpdateRequest 問い合わせ分類一覧更新リクエスト (required)
     * @return 問い合わせ分類一覧レスポンス (status code 200) or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<InquiryGroupListResponse> updateList(
                    @ApiParam(value = "問い合わせ分類一覧更新リクエスト", required = true) @Valid @RequestBody
                                    InquiryGroupListUpdateRequest inquiryGroupListUpdateRequest) {
        int result = 0;
        List<InquiryGroupEntity> inquiryGroupList =
                        inquiryGroupHelper.toInquiryGroupList(inquiryGroupListUpdateRequest);

        if (!inquiryGroupList.isEmpty()) {
            // 要素があった場合のみ
            result = inquiryGroupListUpdateService.execute(inquiryGroupList);
        }

        List<InquiryGroupResponse> inquiryGroupListRes = new ArrayList<>();
        for (InquiryGroupRequest inquiryGroupRequest : inquiryGroupListUpdateRequest.getInquiryGroupListUpdate()) {
            InquiryGroupEntity inquiryGroupEntity =
                            inquiryGroupGetService.execute(inquiryGroupRequest.getInquiryGroupSeq());
            if (ObjectUtils.isNotEmpty(inquiryGroupEntity)) {
                InquiryGroupResponse inquiryGroupResponse =
                                inquiryGroupHelper.toInquiryGroupResponse(inquiryGroupEntity);
                inquiryGroupListRes.add(inquiryGroupResponse);
            }
        }

        InquiryGroupListResponse inquiryGroupListResponse = new InquiryGroupListResponse();
        inquiryGroupListResponse.setInquiryGroupList(inquiryGroupListRes);

        return new ResponseEntity<>(inquiryGroupListResponse, HttpStatus.OK);
    }
}