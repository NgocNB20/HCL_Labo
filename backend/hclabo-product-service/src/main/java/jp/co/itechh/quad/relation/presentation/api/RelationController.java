/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.relation.presentation.api;

import io.swagger.annotations.ApiParam;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.core.constant.type.HTypeSiteType;
import jp.co.itechh.quad.core.dto.goods.goodsgroup.GoodsRelationDto;
import jp.co.itechh.quad.core.dto.goods.goodsgroup.GoodsRelationSearchForDaoConditionDto;
import jp.co.itechh.quad.core.service.goods.relation.GoodsRelationListGetForBackService;
import jp.co.itechh.quad.core.service.goods.relation.RelationGoodsListGetService;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.core.web.AbstractController;
import jp.co.itechh.quad.core.web.PageInfoModule;
import jp.co.itechh.quad.relation.presentation.api.param.PageInfoRequest;
import jp.co.itechh.quad.relation.presentation.api.param.RelationGoodsListGetRequest;
import jp.co.itechh.quad.relation.presentation.api.param.RelationGoodsListResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 関連商品 Controller
 *
 * @author PHAM QUANG DIEU (VJP)
 */
@RestController
public class RelationController extends AbstractController implements ProductsApi {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(RelationController.class);

    /** 関連商品 Helper */
    private final RelationHelper relationHelper;

    /** ConversionUtility */
    private final ConversionUtility conversionUtility;

    /** 関連商品情報取得サービス */
    private final RelationGoodsListGetService relationGoodsListGetService;

    private final GoodsRelationListGetForBackService goodsRelationListGetForBackService;

    /** コンストラクタ */
    public RelationController(RelationHelper relationHelper,
                              ConversionUtility conversionUtility,
                              RelationGoodsListGetService relationGoodsListGetService,
                              GoodsRelationListGetForBackService goodsRelationListGetForBackService) {
        this.relationHelper = relationHelper;
        this.conversionUtility = conversionUtility;
        this.relationGoodsListGetService = relationGoodsListGetService;
        this.goodsRelationListGetForBackService = goodsRelationListGetForBackService;
    }

    /**
     * GET /products/{goodsGroupSeq}/relations : 関連商品一覧取得
     * 関連商品一覧取得
     *
     * @param goodsGroupSeq               商品グループSEQ (required)
     * @param relationGoodsListGetRequest 関連商品一覧取得リクエスト (required)
     * @param pageInfoRequest             ページ情報リクエスト (optional)
     * @return 関連商品一覧レスポンス (status code 200) or その他エラー (status code 200)
     */
    @Override
    public ResponseEntity<RelationGoodsListResponse> get(
                    @ApiParam(value = "商品グループSEQ", required = true) @PathVariable("goodsGroupSeq")
                                    Integer goodsGroupSeq,
                    @NotNull @ApiParam(value = "関連商品一覧取得リクエスト", required = true) @Valid
                                    RelationGoodsListGetRequest relationGoodsListGetRequest,
                    @ApiParam(value = "ページ情報リクエスト") @Valid PageInfoRequest pageInfoRequest) {

        try {
            // 検索条件Dto生成
            GoodsRelationSearchForDaoConditionDto conditionDto =
                            ApplicationContextUtility.getBean(GoodsRelationSearchForDaoConditionDto.class);
            conditionDto.setGoodsGroupSeq(goodsGroupSeq);
            if (StringUtils.isNotEmpty(relationGoodsListGetRequest.getOpenStatus())) {
                HTypeOpenDeleteStatus openDeleteStatus = EnumTypeUtil.getEnumFromValue(HTypeOpenDeleteStatus.class,
                                                                                       relationGoodsListGetRequest.getOpenStatus()
                                                                                      );
                conditionDto.setOpenStatus(openDeleteStatus);
            }
            conditionDto.setSiteType(HTypeSiteType.FRONT_PC);
            conditionDto.setFrontDisplayReferenceDate(this.conversionUtility.toTimeStamp(
                            relationGoodsListGetRequest.getFrontDisplayReferenceDate()));

            // ページング検索セットアップ
            PageInfoModule pageInfoModule = ApplicationContextUtility.getBean(PageInfoModule.class);
            pageInfoModule.setupPageInfo(conditionDto, pageInfoRequest.getPage(), pageInfoRequest.getLimit(),
                                         pageInfoRequest.getOrderBy(), pageInfoRequest.getSort()
                                        );

            List<GoodsRelationDto> goodsRelationDtoList = relationGoodsListGetService.execute(conditionDto);

            RelationGoodsListResponse relationGoodsListResponse =
                            relationHelper.toRelationGoodsListResponse(goodsRelationDtoList);

            return new ResponseEntity<>(relationGoodsListResponse, HttpStatus.OK);

        } catch (Exception e) {
            LOGGER.error("例外処理が発生しました", e);
            throwMessage("SGC001104");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}