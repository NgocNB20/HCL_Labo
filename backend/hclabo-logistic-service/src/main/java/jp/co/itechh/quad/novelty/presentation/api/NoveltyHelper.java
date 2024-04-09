/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.novelty.presentation.api;

import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.constant.type.HTypeEnclosureUnitType;
import jp.co.itechh.quad.core.constant.type.HTypeGoodsPriceTotalFlag;
import jp.co.itechh.quad.core.constant.type.HTypeMagazineSendFlag;
import jp.co.itechh.quad.core.constant.type.HTypeNoveltyPresentState;
import jp.co.itechh.quad.core.dto.shop.novelty.ConditionJudgmentDto;
import jp.co.itechh.quad.core.dto.shop.novelty.NoveltyPresentExclusionNoveltyDto;
import jp.co.itechh.quad.core.dto.shop.novelty.NoveltyPresentIconDto;
import jp.co.itechh.quad.core.dto.shop.novelty.NoveltyPresentSearchForDaoConditionDto;
import jp.co.itechh.quad.core.dto.shop.novelty.NoveltyPresentSearchResultDto;
import jp.co.itechh.quad.core.dto.shop.novelty.NoveltyPresentSearchResultGoodsDto;
import jp.co.itechh.quad.core.dto.shop.novelty.NoveltyPresentValidateDto;
import jp.co.itechh.quad.core.entity.shop.novelty.NoveltyPresentConditionEntity;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.core.web.PageInfoModule;
import jp.co.itechh.quad.novelty.presentation.api.param.NoveltyPresentConditionJudgmentRequest;
import jp.co.itechh.quad.novelty.presentation.api.param.NoveltyPresentConditionListGetRequest;
import jp.co.itechh.quad.novelty.presentation.api.param.NoveltyPresentConditionListResponse;
import jp.co.itechh.quad.novelty.presentation.api.param.NoveltyPresentConditionRegistRequest;
import jp.co.itechh.quad.novelty.presentation.api.param.NoveltyPresentConditionRequest;
import jp.co.itechh.quad.novelty.presentation.api.param.NoveltyPresentConditionResponse;
import jp.co.itechh.quad.novelty.presentation.api.param.NoveltyPresentConditionUpdateRequest;
import jp.co.itechh.quad.novelty.presentation.api.param.NoveltyPresentExclusionNoveltyRequest;
import jp.co.itechh.quad.novelty.presentation.api.param.NoveltyPresentIconRequest;
import jp.co.itechh.quad.novelty.presentation.api.param.NoveltyPresentSearchResultResponse;
import jp.co.itechh.quad.novelty.presentation.api.param.NoveltyPresentValidateListRequest;
import jp.co.itechh.quad.novelty.presentation.api.param.NoveltySearchResultGoodsResponse;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.thymeleaf.util.ListUtils;
import org.thymeleaf.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * ノベルティプレゼント条件Helper
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 *
 */
@Component
public class NoveltyHelper {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(NoveltyHelper.class);

    /** 変換ユーティリティクラス */
    private final ConversionUtility conversionUtility;

    /**
     * コンストラクター
     *  @param conversionUtility 変換ユーティリティクラス
     */
    @Autowired
    public NoveltyHelper(ConversionUtility conversionUtility) {
        this.conversionUtility = conversionUtility;
    }

    /**
     * ベルティプレゼントDao用検索条件Dto(管理機能用）クラスに変換
     *
     * @param noveltyPresentConditionListGetRequest ノベルティプレゼント条件リクエスト
     * @return NoveltyPresentSearchForDaoConditionDto ベルティプレゼントDao用検索条件Dto(管理機能用）クラス
     */
    public NoveltyPresentSearchForDaoConditionDto toNoveltyPresentSearchForDaoConditionDto(
                    NoveltyPresentConditionListGetRequest noveltyPresentConditionListGetRequest) {
        NoveltyPresentSearchForDaoConditionDto conditionDto = new NoveltyPresentSearchForDaoConditionDto();

        conditionDto.setShopSeq(1001);
        conditionDto.setNoveltyPresentName(noveltyPresentConditionListGetRequest.getNoveltyPresentName());
        if (!ListUtils.isEmpty(noveltyPresentConditionListGetRequest.getNoveltyPresentState())) {
            conditionDto.setNoveltyPresentState(noveltyPresentConditionListGetRequest.getNoveltyPresentState());
        }
        conditionDto.setNoveltyPresentStartTimeFrom(conversionUtility.toTimestamp(
                        noveltyPresentConditionListGetRequest.getNoveltyPresentStartTimeFrom()));
        conditionDto.setNoveltyPresentStartTimeTo(conversionUtility.toTimestamp(
                        noveltyPresentConditionListGetRequest.getNoveltyPresentStartTimeTo()));
        conditionDto.setNoveltyPresentEndTimeFrom(conversionUtility.toTimestamp(
                        noveltyPresentConditionListGetRequest.getNoveltyPresentEndTimeFrom()));
        conditionDto.setNoveltyPresentEndTimeTo(conversionUtility.toTimestamp(
                        noveltyPresentConditionListGetRequest.getNoveltyPresentEndTimeTo()));
        conditionDto.setNoveltyPresentGoodsCode(noveltyPresentConditionListGetRequest.getNoveltyPresentGoodsCode());
        conditionDto.setNoveltyPresentGoodsCountFrom(
                        noveltyPresentConditionListGetRequest.getNoveltyPresentGoodsCountFrom());
        conditionDto.setNoveltyPresentGoodsCountTo(
                        noveltyPresentConditionListGetRequest.getNoveltyPresentGoodsCountTo());

        return conditionDto;
    }

    /**
     * ノベルティプレゼント条件リストレスポンスに変換
     *
     * @param noveltyPresentConditionDtoList 検索結果
     * @return NoveltyPresentConditionListResponse ノベルティプレゼント条件リストレスポンス
     */
    public NoveltyPresentConditionListResponse toNoveltyPresentConditionListResponse(List<NoveltyPresentSearchResultDto> noveltyPresentConditionDtoList) {
        NoveltyPresentConditionListResponse noveltyPresentConditionListResponse =
                        new NoveltyPresentConditionListResponse();
        List<NoveltyPresentSearchResultResponse> noveltyPresentConditionList = new ArrayList<>();

        if (!ListUtils.isEmpty(noveltyPresentConditionDtoList)) {
            for (NoveltyPresentSearchResultDto noveltyPresentSearchResultDto : noveltyPresentConditionDtoList) {

                NoveltyPresentSearchResultResponse noveltyPresentSearchResultResponse =
                                new NoveltyPresentSearchResultResponse();
                NoveltyPresentConditionEntity noveltyPresentConditionEntity =
                                noveltyPresentSearchResultDto.getNoveltyPresentConditionEntity();

                noveltyPresentSearchResultResponse.setNoveltyPresentStartTime(
                                noveltyPresentConditionEntity.getNoveltyPresentStartTime());
                noveltyPresentSearchResultResponse.setNoveltyPresentEndTime(
                                noveltyPresentConditionEntity.getNoveltyPresentEndTime());
                noveltyPresentSearchResultResponse.setNoveltyPresentName(
                                noveltyPresentConditionEntity.getNoveltyPresentName());
                noveltyPresentSearchResultResponse.setNoveltyPresentState(
                                noveltyPresentConditionEntity.getNoveltyPresentState().getValue());
                noveltyPresentSearchResultResponse.setNoveltyPresentConditionSeq(
                                noveltyPresentConditionEntity.getNoveltyPresentConditionSeq());
                noveltyPresentSearchResultResponse.setNoveltyGoodsList(
                                toNoveltyGoodsList(noveltyPresentSearchResultDto.getNoveltyGoodsList()));

                noveltyPresentConditionList.add(noveltyPresentSearchResultResponse);
            }
            noveltyPresentConditionListResponse.setNoveltyPresentConditionList(noveltyPresentConditionList);
        } else {
            noveltyPresentConditionListResponse.setNoveltyPresentConditionList(new ArrayList<>());
        }

        return noveltyPresentConditionListResponse;
    }

    /**
     * ノベルティプレゼント商品リストレスポンスに変換
     *
     * @param noveltyGoodsDtoList ノベルティ商品検索結果
     * @return NoveltyPresentConditionListResponse ノベルティプレゼント商品レスポンス
     */
    private List<NoveltySearchResultGoodsResponse> toNoveltyGoodsList(List<NoveltyPresentSearchResultGoodsDto> noveltyGoodsDtoList) {
        List<NoveltySearchResultGoodsResponse> responseList = new ArrayList<>();

        if (!CollectionUtils.isEmpty(noveltyGoodsDtoList)) {
            for (NoveltyPresentSearchResultGoodsDto noveltyGoodsDto : noveltyGoodsDtoList) {
                NoveltySearchResultGoodsResponse response = new NoveltySearchResultGoodsResponse();
                response.setPriorityOrder(noveltyGoodsDto.getPriorityOrder());
                response.setNoveltyGoodsName(noveltyGoodsDto.getNoveltyGoodsName());
                response.setUnitValue1(noveltyGoodsDto.getUnitValue1());
                response.setUnitValue2(noveltyGoodsDto.getUnitValue2());
                response.setSalesPossibleStock(noveltyGoodsDto.getSalesPossibleStock());
                responseList.add(response);
            }
        }
        return responseList;
    }

    /**
     * ノノベルティプレゼント条件クラスに変換
     *
     * @param noveltyPresentConditionUpdateRequest ノベルティプレゼント条件更新リクエスト
     * @return NoveltyPresentConditionEntity ノベルティプレゼント条件クラス
     */
    public NoveltyPresentConditionEntity toNoveltyPresentConditionEntity(NoveltyPresentConditionUpdateRequest noveltyPresentConditionUpdateRequest) {
        NoveltyPresentConditionEntity noveltyPresentConditionEntity = new NoveltyPresentConditionEntity();

        if (ObjectUtils.isEmpty(noveltyPresentConditionUpdateRequest.getNoveltyPresentConditionRequest())) {
            return null;
        }
        NoveltyPresentConditionRequest noveltyPresentConditionRequest =
                        noveltyPresentConditionUpdateRequest.getNoveltyPresentConditionRequest();
        noveltyPresentConditionEntity.setNoveltyPresentConditionSeq(
                        noveltyPresentConditionRequest.getNoveltyPresentConditionSeq());
        noveltyPresentConditionEntity.setNoveltyPresentName(noveltyPresentConditionRequest.getNoveltyPresentName());
        if (!StringUtils.isEmpty(noveltyPresentConditionRequest.getEnclosureUnitType())) {
            HTypeEnclosureUnitType enclosureUnitType = EnumTypeUtil.getEnumFromValue(HTypeEnclosureUnitType.class,
                                                                                     noveltyPresentConditionRequest.getEnclosureUnitType()
                                                                                    );
            noveltyPresentConditionEntity.setEnclosureUnitType(enclosureUnitType);
        }
        if (!StringUtils.isEmpty(noveltyPresentConditionRequest.getNoveltyPresentState())) {
            HTypeNoveltyPresentState noveltyPresentState = EnumTypeUtil.getEnumFromValue(HTypeNoveltyPresentState.class,
                                                                                         noveltyPresentConditionRequest.getNoveltyPresentState()
                                                                                        );
            noveltyPresentConditionEntity.setNoveltyPresentState(noveltyPresentState);
        }
        noveltyPresentConditionEntity.setNoveltyPresentStartTime(
                        conversionUtility.toTimestamp(noveltyPresentConditionRequest.getNoveltyPresentStartTime()));
        noveltyPresentConditionEntity.setNoveltyPresentEndTime(
                        conversionUtility.toTimestamp(noveltyPresentConditionRequest.getNoveltyPresentEndTime()));
        noveltyPresentConditionEntity.setExclusionNoveltyPresentSeq(
                        noveltyPresentConditionRequest.getExclusionNoveltyPresentSeq());
        if (!StringUtils.isEmpty(noveltyPresentConditionRequest.getMagazineSendFlag())) {
            HTypeMagazineSendFlag magazineSendFlag = EnumTypeUtil.getEnumFromValue(HTypeMagazineSendFlag.class,
                                                                                   noveltyPresentConditionRequest.getMagazineSendFlag()
                                                                                  );
            noveltyPresentConditionEntity.setMagazineSendFlag(magazineSendFlag);
        }
        noveltyPresentConditionEntity.setAdmissionStartTime(
                        conversionUtility.toTimestamp(noveltyPresentConditionRequest.getAdmissionStartTime()));
        noveltyPresentConditionEntity.setAdmissionEndTime(
                        conversionUtility.toTimestamp(noveltyPresentConditionRequest.getAdmissionEndTime()));
        noveltyPresentConditionEntity.setGoodsGroupCode(noveltyPresentConditionRequest.getGoodsGroupCode());
        noveltyPresentConditionEntity.setGoodsCode(noveltyPresentConditionRequest.getGoodsCode());
        noveltyPresentConditionEntity.setCategoryId(noveltyPresentConditionRequest.getCategoryId());
        noveltyPresentConditionEntity.setIconId(noveltyPresentConditionRequest.getIconId());
        noveltyPresentConditionEntity.setGoodsName(noveltyPresentConditionRequest.getGoodsName());
        noveltyPresentConditionEntity.setSearchKeyword(noveltyPresentConditionRequest.getSearchKeyword());
        noveltyPresentConditionEntity.setGoodsPriceTotal(noveltyPresentConditionRequest.getGoodsPriceTotal());
        if (!StringUtils.isEmpty(noveltyPresentConditionRequest.getGoodsPriceTotalFlag())) {
            HTypeGoodsPriceTotalFlag goodsPriceTotalFlag = EnumTypeUtil.getEnumFromValue(HTypeGoodsPriceTotalFlag.class,
                                                                                         noveltyPresentConditionRequest.getGoodsPriceTotalFlag()
                                                                                        );
            noveltyPresentConditionEntity.setGoodsPriceTotalFlag(goodsPriceTotalFlag);
        }
        noveltyPresentConditionEntity.setPrizeGoodsLimit(noveltyPresentConditionRequest.getPrizeGoodsLimit());
        noveltyPresentConditionEntity.setMemo(noveltyPresentConditionRequest.getMemo());
        noveltyPresentConditionEntity.setRegistTime(
                        conversionUtility.toTimestamp(noveltyPresentConditionRequest.getRegistTime()));
        noveltyPresentConditionEntity.setUpdateTime(
                        conversionUtility.toTimestamp(noveltyPresentConditionRequest.getUpdateTime()));

        return noveltyPresentConditionEntity;
    }

    /**
     * ノベルティプレゼント条件レスポンスに変換<br/>
     *
     * @param noveltyPresentConditionEntity ノベルティプレゼント条件クラス
     * @return ノベルティプレゼント条件レスポンス
     */
    public NoveltyPresentConditionResponse toNoveltyPresentConditionResponse(NoveltyPresentConditionEntity noveltyPresentConditionEntity)
                    throws InvocationTargetException, IllegalAccessException {
        NoveltyPresentConditionResponse noveltyPresentConditionResponse = new NoveltyPresentConditionResponse();

        if (noveltyPresentConditionEntity != null) {
            BeanUtils.copyProperties(noveltyPresentConditionResponse, noveltyPresentConditionEntity);
        }

        return noveltyPresentConditionResponse;
    }

    /**
     * 検索結果に変換
     *
     * @param noveltyPresentSearchResultDtoList ノベルティプレゼント条件更新リクエスト
     * @return NoveltyPresentConditionListResponse 検索結果
     */
    public NoveltyPresentConditionListResponse toNoveltyPresentSearchResultDtoList(List<NoveltyPresentConditionEntity> noveltyPresentSearchResultDtoList)
                    throws InvocationTargetException, IllegalAccessException {

        if (CollectionUtils.isEmpty(noveltyPresentSearchResultDtoList)) {
            return null;
        }

        NoveltyPresentConditionListResponse noveltyPresentConditionExclusionsListResponse =
                        new NoveltyPresentConditionListResponse();
        List<NoveltyPresentSearchResultResponse> noveltyPresentSearchResultResponses = new ArrayList<>();

        for (NoveltyPresentConditionEntity noveltyPresentSearchResultDto : noveltyPresentSearchResultDtoList) {

            NoveltyPresentSearchResultResponse presentSearchResultResponse = new NoveltyPresentSearchResultResponse();
            BeanUtils.copyProperties(presentSearchResultResponse, noveltyPresentSearchResultDto);

            noveltyPresentSearchResultResponses.add(presentSearchResultResponse);
        }

        noveltyPresentConditionExclusionsListResponse.setNoveltyPresentConditionList(
                        noveltyPresentSearchResultResponses);

        return noveltyPresentConditionExclusionsListResponse;
    }

    /**
     * ノベルティプレゼント条件クラスに変換
     *
     * @param noveltyPresentConditionRegistRequest ノベルティプレゼント条件更新リクエスト
     * @return NoveltyPresentConditionEntity ノベルティプレゼント条件クラス
     */
    public NoveltyPresentConditionEntity toNoveltyPresentConditionEntity(NoveltyPresentConditionRegistRequest noveltyPresentConditionRegistRequest) {
        NoveltyPresentConditionEntity noveltyPresentConditionEntity = new NoveltyPresentConditionEntity();

        if (ObjectUtils.isEmpty(noveltyPresentConditionRegistRequest.getNoveltyPresentConditionRequest())) {
            return null;
        }
        NoveltyPresentConditionRequest noveltyPresentConditionRequest =
                        noveltyPresentConditionRegistRequest.getNoveltyPresentConditionRequest();
        noveltyPresentConditionEntity.setNoveltyPresentConditionSeq(
                        noveltyPresentConditionRequest.getNoveltyPresentConditionSeq());
        noveltyPresentConditionEntity.setNoveltyPresentName(noveltyPresentConditionRequest.getNoveltyPresentName());
        if (!StringUtils.isEmpty(noveltyPresentConditionRequest.getEnclosureUnitType())) {
            HTypeEnclosureUnitType enclosureUnitType = EnumTypeUtil.getEnumFromValue(HTypeEnclosureUnitType.class,
                                                                                     noveltyPresentConditionRequest.getEnclosureUnitType()
                                                                                    );
            noveltyPresentConditionEntity.setEnclosureUnitType(enclosureUnitType);
        }
        if (!StringUtils.isEmpty(noveltyPresentConditionRequest.getNoveltyPresentState())) {
            HTypeNoveltyPresentState noveltyPresentState = EnumTypeUtil.getEnumFromValue(HTypeNoveltyPresentState.class,
                                                                                         noveltyPresentConditionRequest.getNoveltyPresentState()
                                                                                        );
            noveltyPresentConditionEntity.setNoveltyPresentState(noveltyPresentState);
        }
        noveltyPresentConditionEntity.setNoveltyPresentStartTime(
                        conversionUtility.toTimestamp(noveltyPresentConditionRequest.getNoveltyPresentStartTime()));
        noveltyPresentConditionEntity.setNoveltyPresentEndTime(
                        conversionUtility.toTimestamp(noveltyPresentConditionRequest.getNoveltyPresentEndTime()));
        noveltyPresentConditionEntity.setExclusionNoveltyPresentSeq(
                        noveltyPresentConditionRequest.getExclusionNoveltyPresentSeq());
        if (!StringUtils.isEmpty(noveltyPresentConditionRequest.getMagazineSendFlag())) {
            HTypeMagazineSendFlag magazineSendFlag = EnumTypeUtil.getEnumFromValue(HTypeMagazineSendFlag.class,
                                                                                   noveltyPresentConditionRequest.getMagazineSendFlag()
                                                                                  );
            noveltyPresentConditionEntity.setMagazineSendFlag(magazineSendFlag);
        }
        noveltyPresentConditionEntity.setAdmissionStartTime(
                        conversionUtility.toTimestamp(noveltyPresentConditionRequest.getAdmissionStartTime()));
        noveltyPresentConditionEntity.setAdmissionEndTime(
                        conversionUtility.toTimestamp(noveltyPresentConditionRequest.getAdmissionEndTime()));
        noveltyPresentConditionEntity.setGoodsGroupCode(noveltyPresentConditionRequest.getGoodsGroupCode());
        noveltyPresentConditionEntity.setGoodsCode(noveltyPresentConditionRequest.getGoodsCode());
        noveltyPresentConditionEntity.setCategoryId(noveltyPresentConditionRequest.getCategoryId());
        noveltyPresentConditionEntity.setIconId(noveltyPresentConditionRequest.getIconId());
        noveltyPresentConditionEntity.setGoodsName(noveltyPresentConditionRequest.getGoodsName());
        noveltyPresentConditionEntity.setSearchKeyword(noveltyPresentConditionRequest.getSearchKeyword());
        noveltyPresentConditionEntity.setGoodsPriceTotal(noveltyPresentConditionRequest.getGoodsPriceTotal());
        if (!StringUtils.isEmpty(noveltyPresentConditionRequest.getGoodsPriceTotalFlag())) {
            HTypeGoodsPriceTotalFlag goodsPriceTotalFlag = EnumTypeUtil.getEnumFromValue(HTypeGoodsPriceTotalFlag.class,
                                                                                         noveltyPresentConditionRequest.getGoodsPriceTotalFlag()
                                                                                        );
            noveltyPresentConditionEntity.setGoodsPriceTotalFlag(goodsPriceTotalFlag);
        }
        noveltyPresentConditionEntity.setPrizeGoodsLimit(noveltyPresentConditionRequest.getPrizeGoodsLimit());
        noveltyPresentConditionEntity.setMemo(noveltyPresentConditionRequest.getMemo());
        noveltyPresentConditionEntity.setRegistTime(
                        conversionUtility.toTimestamp(noveltyPresentConditionRequest.getRegistTime()));
        noveltyPresentConditionEntity.setUpdateTime(
                        conversionUtility.toTimestamp(noveltyPresentConditionRequest.getUpdateTime()));

        return noveltyPresentConditionEntity;
    }

    public NoveltyPresentValidateDto toNoveltyPresentValidateDto(NoveltyPresentValidateListRequest noveltyPresentValidateListRequest) {
        NoveltyPresentValidateDto noveltyPresentValidateDto = new NoveltyPresentValidateDto();

        if (ObjectUtils.isEmpty(noveltyPresentValidateListRequest)) {
            return null;
        }

        noveltyPresentValidateDto.setNoveltyGoodsCodeList(noveltyPresentValidateListRequest.getNoveltyGoodsCodeList());
        noveltyPresentValidateDto.setGoodsGroupCodeList(noveltyPresentValidateListRequest.getGoodsGroupCodeList());
        noveltyPresentValidateDto.setGoodsCodeList(noveltyPresentValidateListRequest.getGoodsCodeList());
        noveltyPresentValidateDto.setCategoryIdList(noveltyPresentValidateListRequest.getCategoryIdList());
        noveltyPresentValidateDto.setCategorySeqList(noveltyPresentValidateListRequest.getCategorySeqList());
        noveltyPresentValidateDto.setIconList(
                        toNoveltyPresentIconDtoList(noveltyPresentValidateListRequest.getIconList()));
        noveltyPresentValidateDto.setGoodsNameList(noveltyPresentValidateListRequest.getGoodsNameList());
        noveltyPresentValidateDto.setSearchKeywordList(noveltyPresentValidateListRequest.getSearchKeywordList());
        noveltyPresentValidateDto.setExclusionNoveltyList(toNoveltyPresentExclusionNoveltyDtoList(
                        noveltyPresentValidateListRequest.getExclusionNoveltyList()));

        return noveltyPresentValidateDto;
    }

    public List<NoveltyPresentIconDto> toNoveltyPresentIconDtoList(List<NoveltyPresentIconRequest> noveltyPresentIconRequests) {
        List<NoveltyPresentIconDto> noveltyPresentIconDtos = new ArrayList<>();

        if (CollectionUtils.isEmpty(noveltyPresentIconRequests)) {
            return null;
        }

        for (NoveltyPresentIconRequest noveltyPresentIconRequest : noveltyPresentIconRequests) {
            NoveltyPresentIconDto noveltyPresentIconDto = new NoveltyPresentIconDto();

            noveltyPresentIconDto.setIconCheck(noveltyPresentIconRequest.getIconCheck());
            noveltyPresentIconDto.setIconSeq(Integer.parseInt(noveltyPresentIconRequest.getIconSeq()));
            noveltyPresentIconDto.setIconName(noveltyPresentIconRequest.getIconName());

            noveltyPresentIconDtos.add(noveltyPresentIconDto);
        }
        return noveltyPresentIconDtos;
    }

    public List<NoveltyPresentExclusionNoveltyDto> toNoveltyPresentExclusionNoveltyDtoList(List<NoveltyPresentExclusionNoveltyRequest> noveltyPresentExclusionNoveltyRequests) {
        List<NoveltyPresentExclusionNoveltyDto> noveltyPresentExclusionNoveltyDtos = new ArrayList<>();

        if (CollectionUtils.isEmpty(noveltyPresentExclusionNoveltyRequests)) {
            return null;
        }

        for (NoveltyPresentExclusionNoveltyRequest noveltyPresentExclusionNoveltyRequest : noveltyPresentExclusionNoveltyRequests) {
            NoveltyPresentExclusionNoveltyDto noveltyPresentExclusionNoveltyDto =
                            new NoveltyPresentExclusionNoveltyDto();

            noveltyPresentExclusionNoveltyDto.setExclusionNoveltyCheck(
                            noveltyPresentExclusionNoveltyRequest.getExclusionNoveltyCheck());
            noveltyPresentExclusionNoveltyDto.setExclusionNoveltySeq(
                            Integer.parseInt(noveltyPresentExclusionNoveltyRequest.getExclusionNoveltySeq()));
            noveltyPresentExclusionNoveltyDto.setExclusionNoveltyName(
                            noveltyPresentExclusionNoveltyRequest.getExclusionNoveltyName());

            noveltyPresentExclusionNoveltyDtos.add(noveltyPresentExclusionNoveltyDto);
        }

        return noveltyPresentExclusionNoveltyDtos;
    }

    /**
     * ノベルティプレゼント条件エンティティ一覧へ変換
     *
     * @param request ノベルティプレゼント条件判定リクエスト
     * @return ノベルティプレゼント条件エンティティ一覧
     */
    public NoveltyPresentSearchForDaoConditionDto createNoveltyConditionList(NoveltyPresentConditionJudgmentRequest request) {
        // 受注の最小時間と最大時間
        Timestamp minOrderTime = new Timestamp(request.getRegistTime().getTime());
        Timestamp maxOrderTime = new Timestamp(request.getRegistTime().getTime());

        // 検索条件を設定
        NoveltyPresentSearchForDaoConditionDto conditionDto = new NoveltyPresentSearchForDaoConditionDto();
        conditionDto.setNoveltyPresentStartTimeFrom(minOrderTime);
        conditionDto.setNoveltyPresentEndTimeTo(maxOrderTime);
        conditionDto.setNoveltyPresentGoodsCountFrom(1);

        // ページング検索セットアップ
        PageInfoModule pageInfoModule = new PageInfoModule();
        pageInfoModule.setupPageInfo(conditionDto, 1, Integer.MAX_VALUE);

        // 検索
        return conditionDto;
    }

    /**
     * 条件判定へ変換
     *
     * @param request ノベルティプレゼント条件判定リクエスト
     * @return 条件判定Dto
     */
    public ConditionJudgmentDto toConditionJudgmentDto(NoveltyPresentConditionJudgmentRequest request) {
        ConditionJudgmentDto conditionJudgmentDto = new ConditionJudgmentDto();
        try {
            BeanUtils.copyProperties(conditionJudgmentDto, request);
        } catch (IllegalAccessException | InvocationTargetException e) {
            LOGGER.error("例外処理が発生しました", e);
            return null;
        }
        return conditionJudgmentDto;
    }
}