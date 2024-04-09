/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.shop.novelty.impl;

import jp.co.itechh.quad.category.presentation.api.CategoryApi;
import jp.co.itechh.quad.category.presentation.api.param.CategorySeqListGetRequest;
import jp.co.itechh.quad.category.presentation.api.param.CategorySeqListResponse;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.constant.type.HTypeGoodsPriceTotalFlag;
import jp.co.itechh.quad.core.constant.type.HTypeMagazineSendFlag;
import jp.co.itechh.quad.core.constant.type.HTypeMailMagazineSendStatusName;
import jp.co.itechh.quad.core.dao.shop.novelty.NoveltyPresentMemberDao;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsDetailsDto;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsGroupDisplayDto;
import jp.co.itechh.quad.core.dto.icon.GoodsInformationIconDetailsDto;
import jp.co.itechh.quad.core.dto.memberinfo.MemberInfoDetailsDto;
import jp.co.itechh.quad.core.dto.shop.novelty.NoveltyPresentJudgmentDto;
import jp.co.itechh.quad.core.entity.shop.novelty.NoveltyPresentConditionEntity;
import jp.co.itechh.quad.core.service.shop.novelty.NoveltyPresentJudgmentCheckService;
import jp.co.itechh.quad.ddd.domain.product.adapter.IProductAdapter;
import jp.co.itechh.quad.ddd.domain.shipping.entity.ShippingSlipEntity;
import jp.co.itechh.quad.ddd.domain.shipping.repository.IShippingSlipRepository;
import jp.co.itechh.quad.ddd.domain.shipping.valueobject.SecuredShippingItem;
import jp.co.itechh.quad.ddd.domain.user.adapter.ICustomerAdapter;
import jp.co.itechh.quad.ddd.domain.user.adapter.IMailMagazineAdapter;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 *  ノベルティプレゼント判定用チェックサービス<br/>
 *
 * @author aoyama
 *
 */
@Service
public class NoveltyPresentJudgmentCheckServiceImpl implements NoveltyPresentJudgmentCheckService {

    /** ノベルティプレゼント対象会員Dao */
    private final NoveltyPresentMemberDao noveltyPresentMemberDao;

    /** 顧客アダプター */
    private final ICustomerAdapter customerAdapter;

    /** メールマガアダプター */
    private final IMailMagazineAdapter mailMagazineAdapter;

    /** 配送伝票リポジトリ */
    private final IShippingSlipRepository shippingSlipRepository;

    private final IProductAdapter productAdapter;

    private final CategoryApi categoryApi;

    private Map<String, GoodsDetailsDto> goodsDetailsDtoMap;

    /** コンストラクタ */
    @Autowired
    public NoveltyPresentJudgmentCheckServiceImpl(NoveltyPresentMemberDao noveltyPresentMemberDao,
                                                  ICustomerAdapter customerAdapter,
                                                  IMailMagazineAdapter mailMagazineAdapter,
                                                  IShippingSlipRepository shippingSlipRepository,
                                                  IProductAdapter productAdapter,
                                                  CategoryApi categoryApi) {
        this.noveltyPresentMemberDao = noveltyPresentMemberDao;
        this.customerAdapter = customerAdapter;
        this.mailMagazineAdapter = mailMagazineAdapter;
        this.shippingSlipRepository = shippingSlipRepository;
        this.productAdapter = productAdapter;
        this.categoryApi = categoryApi;
    }

    /**
     * ノベルティプレゼント判定処理<br/>
     * @param judgmentDto ノベルティプレゼント判定用パラメータDto
     *
     * @return ノベルティプレゼント条件
     */
    @Override
    public List<NoveltyPresentConditionEntity> execute(NoveltyPresentJudgmentDto judgmentDto) {

        // 追加条件リスト(返却値)
        List<NoveltyPresentConditionEntity> tsuikaJokenList = null;

        if (judgmentDto.getNoveltyPresentConditionEntityList() != null) {
            // ノベルティプレゼント条件判定処理(該当リストの作成)
            List<NoveltyPresentConditionEntity> gaitoList = createGaitoJokenList(judgmentDto);

            if (gaitoList != null && !gaitoList.isEmpty()) {
                // 該当条件除外処理
                tsuikaJokenList = createTsuikaJokenList(gaitoList);
            }
        }

        return tsuikaJokenList;
    }

    /**
     * ノベルティプレゼント条件判定処理
     *
     * @param judgmentDto ノベルティプレゼント判定用パラメータDto
     * @return 該当条件リスト
     */
    private List<NoveltyPresentConditionEntity> createGaitoJokenList(NoveltyPresentJudgmentDto judgmentDto) {

        List<NoveltyPresentConditionEntity> retList = new ArrayList<>();

        this.goodsDetailsDtoMap = new HashMap<>();

        for (NoveltyPresentConditionEntity conditionDto : judgmentDto.getNoveltyPresentConditionEntityList()) {

            Timestamp orderTime = new Timestamp(judgmentDto.getConditionJudgmentDto().getRegistTime().getTime());
            // 基本情報のチェック
            if (!checkBasicInfo(conditionDto, orderTime)) {
                continue;
            }

            // 会員情報のチェック
            if (!checkMemberInfo(conditionDto, judgmentDto)) {
                continue;
            }

            // 商品情報のチェック
            if (!checkGoodsInfo(conditionDto, judgmentDto)) {
                continue;
            }

            // 受注 - 商品金額合計判定購入商品が指定金額以上であるか
            if (conditionDto.getGoodsPriceTotal() != null) {
                if (HTypeGoodsPriceTotalFlag.OFF.equals(conditionDto.getGoodsPriceTotalFlag())) {
                    // 受注金額で判定する場合
                    BigDecimal orderPrice =
                                    new BigDecimal(judgmentDto.getConditionJudgmentDto().getItemSalesPriceTotal());

                    if (orderPrice.compareTo(conditionDto.getGoodsPriceTotal()) < 0) {
                        continue;
                    }

                } else if (HTypeGoodsPriceTotalFlag.ON.equals(conditionDto.getGoodsPriceTotalFlag())) {
                    // 対象商品のみの場合
                    BigDecimal total = new BigDecimal(0);

                    // 条件に該当した受注商品の金額を累計する
                    List<SecuredShippingItem> matchOrderGoodsList = judgmentDto.getConditionMatchOrderGoodsMap()
                                                                               .get(conditionDto.getNoveltyPresentConditionSeq());
                    for (SecuredShippingItem matchOrderGoods : matchOrderGoodsList) {
                        BigDecimal price = goodsDetailsDtoMap.get(matchOrderGoods.getItemId()).getGoodsPriceInTax();
                        BigDecimal count = BigDecimal.valueOf(matchOrderGoods.getShippingCount().getValue());
                        total = total.add(price.multiply(count));
                    }

                    if (total.compareTo(conditionDto.getGoodsPriceTotal()) < 0) {
                        continue;
                    }
                }
            }

            retList.add(conditionDto);
        }

        return retList;
    }

    /**
     * 該当条件除外処理
     * @param gaitoList 該当条件リスト
     * @return 追加条件リスト
     */
    private List<NoveltyPresentConditionEntity> createTsuikaJokenList(List<NoveltyPresentConditionEntity> gaitoList) {
        List<NoveltyPresentConditionEntity> retList = new ArrayList<NoveltyPresentConditionEntity>();

        Map<String, NoveltyPresentConditionEntity> conditionDtoMap =
                        new HashMap<String, NoveltyPresentConditionEntity>();
        List<String> exclusionSeqList = new ArrayList<String>();

        for (NoveltyPresentConditionEntity conditionDto : gaitoList) {
            conditionDtoMap.put(String.valueOf(conditionDto.getNoveltyPresentConditionSeq()), conditionDto);

            // 除外条件のSEQをまとめる
            String exclusionSeq = conditionDto.getExclusionNoveltyPresentSeq();
            if (exclusionSeq != null && !"".equals(exclusionSeq)) {
                exclusionSeqList.addAll(toList(exclusionSeq, ","));
            }
        }

        // 除外条件が無ければそのまま返却
        if (exclusionSeqList.isEmpty()) {
            return gaitoList;
        }

        // 除外条件のレコードを取り除く
        for (String exclusionSeq : exclusionSeqList) {
            if (conditionDtoMap.containsKey(exclusionSeq)) {
                conditionDtoMap.remove(exclusionSeq);
            }
        }

        // 再度List化
        Set<String> keySet = conditionDtoMap.keySet();
        for (String key : keySet) {
            retList.add(conditionDtoMap.get(key));
        }

        return retList;
    }

    /**
     * 基本情報のチェック
     *
     * @param conditionDto ノベルティプレゼント条件Dto
     * @param orderTime 受注日時
     * @return TRUE:条件に一致、FALSE:条件に不一致
     */
    private boolean checkBasicInfo(NoveltyPresentConditionEntity conditionDto, Timestamp orderTime) {
        Timestamp startTime = conditionDto.getNoveltyPresentStartTime();
        Timestamp endTime = conditionDto.getNoveltyPresentEndTime();

        if (startTime != null && startTime.compareTo(orderTime) > 0) {
            return false;
        }

        if (endTime != null) {
            return endTime.compareTo(orderTime) >= 0;
        }

        return true;
    }

    /**
     * 会員情報のチェック
     *
     * @param conditionDto ノベルティプレゼント条件Dto
     * @param judgmentDto ノベルティプレゼント判定用パラメータDto
     * @return TRUE:条件に一致、FALSE:条件に不一致
     */
    private boolean checkMemberInfo(NoveltyPresentConditionEntity conditionDto, NoveltyPresentJudgmentDto judgmentDto) {

        // 日付Utility取得
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

        Integer memberInfoSeq = judgmentDto.getConditionJudgmentDto().getMemberInfoSeq();
        MemberInfoDetailsDto memberInfo = customerAdapter.getMemberInfoDetailsDto(memberInfoSeq);

        // 会員注文かどうか
        if (judgmentDto.getConditionJudgmentDto().getMemberInfoSeq() == 0) {
            return false;
        }

        // 会員情報を取得
        if (memberInfo == null) {
            return false;
        }

        // 会員 - プレゼント数制限判定同キャンペーンでの個数（お一人様○個）は制限内であるか
        Integer prizeGoodsLimit = conditionDto.getPrizeGoodsLimit();

        if (prizeGoodsLimit != null) {
            // ノベルティプレゼント対象会員を検索
            Integer noveltyPresentConditionSeq = conditionDto.getNoveltyPresentConditionSeq();
            List<Integer> goodsSeqList =
                            noveltyPresentMemberDao.getEntityListByMemberInfoSeq(noveltyPresentConditionSeq,
                                                                                 memberInfoSeq
                                                                                );

            int count = 0;
            if (goodsSeqList != null) {
                count = goodsSeqList.size();
            }

            if (prizeGoodsLimit <= count) {
                return false;
            }
        }

        // 会員 - 入会期間判定ご注文主の会員情報登録日時が「入会期間」の範囲内であるか
        Timestamp admissionStartTime = conditionDto.getAdmissionStartTime();
        Timestamp admissionEndTime = conditionDto.getAdmissionEndTime();
        Timestamp admissionTime = dateUtility.toTimestampValue(memberInfo.getMemberInfoEntity().getAdmissionYmd(),
                                                               DateUtility.YMD_SLASH
                                                              );

        if (admissionStartTime != null) {
            if (admissionStartTime.compareTo(admissionTime) > 0) {
                return false;
            }
        }

        if (admissionEndTime != null) {
            if (admissionEndTime.compareTo(admissionTime) < 0) {
                return false;
            }
        }

        // 会員 - メールマガジン判定ご注文主が「メールマガジン配信中」であるか
        if (HTypeMagazineSendFlag.ON.equals(conditionDto.getMagazineSendFlag())) {
            // メルマガ購読者から検索
            String mailAddr = memberInfo.getMemberInfoEntity().getMemberInfoMail();
            List<String> sendStatusList = mailMagazineAdapter.getMailMagazineSendStatus(mailAddr);

            if (sendStatusList == null || sendStatusList.isEmpty()) {
                // 取得できなければエラー
                return false;
            }

            boolean flg = false;
            for (String sendStatus : sendStatusList) {
                if (HTypeMailMagazineSendStatusName.SENDING.getValue().equals(sendStatus)) {
                    flg = true;
                    break;
                }
            }
            if (!flg) {
                // 送信中のステータスがない場合はエラー
                return false;
            }
        }

        return true;
    }

    /**
     * 商品情報のチェック
     *
     * @param conditionDto ノベルティプレゼント条件Dto
     * @param judgmentDto ノベルティプレゼント判定用パラメータDto
     * @return TRUE:条件に一致、FALSE:条件に不一致
     */
    private boolean checkGoodsInfo(NoveltyPresentConditionEntity conditionDto, NoveltyPresentJudgmentDto judgmentDto) {

        ShippingSlipEntity shippingSlipEntity = shippingSlipRepository.getByTransactionId(
                        judgmentDto.getConditionJudgmentDto().getTransactionId());

        judgmentDto.getHmTargetOrderGoodsList().clear();
        judgmentDto.getHmTargetOrderGoodsList().addAll(shippingSlipEntity.getSecuredShippingItemList());

        List<SecuredShippingItem> orderGoodsList = shippingSlipEntity.getSecuredShippingItemList();
        List<String> goodsSeqList = new ArrayList<>();
        for (SecuredShippingItem orderGoods : orderGoodsList) {
            goodsSeqList.add(orderGoods.getItemId());
        }

        List<GoodsDetailsDto> goodsDetailsDtoList = productAdapter.getDetails(goodsSeqList);

        for (GoodsDetailsDto goodsDetailsDto : goodsDetailsDtoList) {
            goodsDetailsDtoMap.put(goodsDetailsDto.getGoodsSeq().toString(), goodsDetailsDto);
        }

        // 商品管理番号判定
        List<String> condGoodsGroupCodeList =
                        toList(conditionDto.getGoodsGroupCode(), System.getProperty("line.separator"));
        if (condGoodsGroupCodeList != null && condGoodsGroupCodeList.size() != 0) {
            if (!checkGoodsGroupCode(judgmentDto, condGoodsGroupCodeList, goodsDetailsDtoMap)) {
                return false;
            }
        }

        // 商品番号判定
        List<String> goodsCodeList = toList(conditionDto.getGoodsCode(), System.getProperty("line.separator"));
        if (goodsCodeList != null && goodsCodeList.size() != 0) {
            if (!checkGoodsCode(judgmentDto, goodsCodeList, goodsDetailsDtoMap)) {
                return false;
            }
        }

        // カテゴリーID判定
        List<String> condCategoryIdList = toList(conditionDto.getCategoryId(), System.getProperty("line.separator"));
        if (condCategoryIdList != null && condCategoryIdList.size() != 0) {
            if (!checkCategoryId(judgmentDto, condCategoryIdList, goodsDetailsDtoMap)) {
                return false;
            }
        }

        // アイコンID判定
        List<String> iconSeqList = toList(conditionDto.getIconId(), ",");
        if (iconSeqList != null && iconSeqList.size() != 0) {
            if (!checkIcon(judgmentDto, iconSeqList, goodsDetailsDtoMap)) {
                return false;
            }
        }

        // 商品名判定
        List<String> condGoodsNameList = toList(conditionDto.getGoodsName(), System.getProperty("line.separator"));
        if (condGoodsNameList != null && condGoodsNameList.size() != 0) {
            if (!checkGoodsName(judgmentDto, condGoodsNameList)) {
                return false;
            }
        }

        // 商品 - 検索キーワード判定「検索キーワード（複数可）」に該当する商品が購入商品に含まれるか
        List<String> condKeywordList = toList(conditionDto.getSearchKeyword(), System.getProperty("line.separator"));
        if (condKeywordList != null && condKeywordList.size() != 0) {
            if (!checkSearchKeyword(judgmentDto, condKeywordList, goodsDetailsDtoMap)) {
                return false;
            }
        }

        List<SecuredShippingItem> addList = new ArrayList<>(judgmentDto.getHmTargetOrderGoodsList());
        Integer seq = conditionDto.getNoveltyPresentConditionSeq();
        judgmentDto.getConditionMatchOrderGoodsMap().put(seq, addList);

        return true;
    }

    /**
     * 商品管理番号判定（HM用）<br/>
     * 「商品管理番号（複数可）」に該当する商品が購入商品に含まれるか
     *
     * @param judgmentDto ノベルティプレゼント判定用パラメータDto
     * @param condGoodsGroupCodeList ノベルティプレゼント条件に登録されている商品管理番号のリスト
     * @return TRUE:条件に一致、FALSE:条件に不一致
     */
    private boolean checkGoodsGroupCode(NoveltyPresentJudgmentDto judgmentDto,
                                        List<String> condGoodsGroupCodeList,
                                        Map<String, GoodsDetailsDto> goodsDetailsDtoMap) {

        ShippingSlipEntity shippingSlipEntity = shippingSlipRepository.getByTransactionId(
                        judgmentDto.getConditionJudgmentDto().getTransactionId());
        List<SecuredShippingItem> checkedList = new ArrayList<>();

        for (SecuredShippingItem orderGoods : shippingSlipEntity.getSecuredShippingItemList()) {

            if (Integer.valueOf(0).equals(orderGoods.getShippingCount().getValue())) {
                continue;
            }
            if (condGoodsGroupCodeList.contains(goodsDetailsDtoMap.get(orderGoods.getItemId()).getGoodsGroupCode())) {
                checkedList.add(orderGoods);
            }
        }

        if (checkedList.isEmpty()) {
            return false;
        }

        judgmentDto.setHmTargetOrderGoodsList(checkedList);

        return true;
    }

    /**
     * 商品番号判定（HM用）<br/>
     * 「商品番号（複数可）」に該当する商品が購入商品に含まれるか
     *
     * @param judgmentDto ノベルティプレゼント判定用パラメータDto
     * @param goodsCodeList ノベルティプレゼント条件に登録されている商品番号のリスト
     * @return TRUE:条件に一致、FALSE:条件に不一致
     */
    private boolean checkGoodsCode(NoveltyPresentJudgmentDto judgmentDto,
                                   List<String> goodsCodeList,
                                   Map<String, GoodsDetailsDto> goodsDetailsDtoMap) {

        List<SecuredShippingItem> hmTargetList = judgmentDto.getHmTargetOrderGoodsList();
        List<SecuredShippingItem> checkList = new ArrayList<>();
        for (SecuredShippingItem shippingItem : hmTargetList) {
            if (Integer.valueOf(0).equals(shippingItem.getShippingCount().getValue())) {
                continue;
            }
            if (goodsCodeList.contains(goodsDetailsDtoMap.get(shippingItem.getItemId()).getGoodsCode())) {
                checkList.add(shippingItem);
            }
        }

        if (checkList.isEmpty()) {
            return false;
        }

        hmTargetList.clear();
        hmTargetList.addAll(checkList);
        judgmentDto.setHmTargetOrderGoodsList(hmTargetList);

        return true;
    }

    /**
     * カテゴリーID判定（HM用）<br/>
     * 「カテゴリーID（複数可）」に該当する商品が購入商品に含まれるか
     *
     * @param judgmentDto ノベルティプレゼント判定用パラメータDto
     * @param condCategoryIdList ノベルティプレゼント条件に登録されているカテゴリIDのリスト
     * @return TRUE:条件に一致、FALSE:条件に不一致
     */
    private boolean checkCategoryId(NoveltyPresentJudgmentDto judgmentDto,
                                    List<String> condCategoryIdList,
                                    Map<String, GoodsDetailsDto> goodsDetailsDtoMap) {
        List<SecuredShippingItem> checkList = new ArrayList<>();
        List<SecuredShippingItem> targetList = judgmentDto.getHmTargetOrderGoodsList();
        for (SecuredShippingItem orderGoods : targetList) {
            if (Integer.valueOf(0).equals(orderGoods.getShippingCount().getValue())) {
                continue;
            }
            String goodsGroupCode = goodsDetailsDtoMap.get(orderGoods.getItemId()).getGoodsGroupCode();

            // ノベルティ条件のカテゴリSEQパスを取得
            List<String> condCategorySeqPathList = getCategorySeqPathList(condCategoryIdList);
            List<String> categorySeqPathList = getCategorySeqPathList(goodsGroupCode);

            boolean checkFlag = false;
            for (String categorySeqPath : categorySeqPathList) {
                for (String condCategorySeqPath : condCategorySeqPathList) {
                    // 部分一致
                    if (categorySeqPath.contains(condCategorySeqPath)) {
                        checkList.add(orderGoods);
                        checkFlag = true;
                        break;
                    }
                }
                if (checkFlag) {
                    break;
                }
            }
        }

        if (checkList.isEmpty()) {
            return false;
        }

        targetList.clear();
        targetList.addAll(checkList);
        judgmentDto.setHmTargetOrderGoodsList(targetList);

        return true;
    }

    /**
     * カテゴリIDの一覧からカテゴリSEQのListを取得する
     *
     * @param categoryIdList カテゴリIDの一覧
     * @return カテゴリSEQのList
     */
    private List<String> getCategorySeqPathList(List<String> categoryIdList) {
        List<String> retList = new ArrayList<String>();

        CategorySeqListGetRequest categorySeqListGetRequest = new CategorySeqListGetRequest();
        categorySeqListGetRequest.setCategoryIdList(categoryIdList);
        CategorySeqListResponse response = categoryApi.getSeqListByIdList(categorySeqListGetRequest);

        if (ObjectUtils.isEmpty(response) || ObjectUtils.isEmpty(response.getCategorySeqList())) {
            return retList;
        }
        for (Integer categorySeqPath : response.getCategorySeqList()) {
            if (!retList.contains(categorySeqPath.toString())) {
                retList.add(categorySeqPath.toString());
            }
        }
        return retList;
    }

    /**
     * 商品管理番号に紐づくカテゴリSEQパスのリストを取得する
     *
     * @param goodsGroupCode 商品管理番号
     * @return カテゴリIDのリスト
     */
    private List<String> getCategorySeqPathList(String goodsGroupCode) {
        List<String> retList = new ArrayList<String>();

        CategorySeqListResponse response = categoryApi.getSeqListByGoodsGroupCode(goodsGroupCode);

        if (ObjectUtils.isEmpty(response) || ObjectUtils.isEmpty(response.getCategorySeqList())) {
            return retList;
        }

        for (Integer categorySeqPath : response.getCategorySeqList()) {
            if (!retList.contains(categorySeqPath.toString())) {
                retList.add(categorySeqPath.toString());
            }
        }
        return retList;
    }

    /**
     * アイコンID判定（HM用）<br/>
     * 「アイコンID（複数可）」に該当する商品が購入商品に含まれるか
     *
     * @param judgmentDto ノベルティプレゼント判定用パラメータDto
     * @param iconSeqList ノベルティプレゼント条件に登録されているアイコンIDのリスト
     * @return TRUE:条件に一致、FALSE:条件に不一致
     */
    private boolean checkIcon(NoveltyPresentJudgmentDto judgmentDto,
                              List<String> iconSeqList,
                              Map<String, GoodsDetailsDto> goodsDetailsDtoMap) {

        if (iconSeqList == null || iconSeqList.isEmpty()) {
            return true;
        }

        List<SecuredShippingItem> checkList = new ArrayList<>();
        List<SecuredShippingItem> targetList = judgmentDto.getHmTargetOrderGoodsList();
        for (SecuredShippingItem orderGoods : targetList) {
            if (Integer.valueOf(0).equals(orderGoods.getShippingCount().getValue())) {
                continue;
            }
            List<String> iconList = new ArrayList<>();
            for (GoodsInformationIconDetailsDto detailsDto : goodsDetailsDtoMap.get(orderGoods.getItemId())
                                                                               .getGoodsIconList()) {
                iconList.add(detailsDto.getIconSeq().toString());
            }

            for (String iconSeq : iconSeqList) {
                if (iconList.contains(iconSeq)) {
                    checkList.add(orderGoods);
                    break;
                }
            }
        }
        if (checkList.isEmpty()) {
            return false;
        }

        targetList.clear();
        targetList.addAll(checkList);
        judgmentDto.setHmTargetOrderGoodsList(targetList);

        return true;
    }

    /**
     * 商品名判定（HM用）<br/>
     * 「商品名（複数可）」に該当する商品が購入商品に含まれるか
     *
     * @param judgmentDto ノベルティプレゼント判定用パラメータDto
     * @param condGoodsNameList ノベルティプレゼント条件に登録されている商品名のリスト
     * @return TRUE:条件に一致、FALSE:条件に不一致
     */
    private boolean checkGoodsName(NoveltyPresentJudgmentDto judgmentDto, List<String> condGoodsNameList) {

        List<SecuredShippingItem> checkList = new ArrayList<>();
        List<SecuredShippingItem> targetList = judgmentDto.getHmTargetOrderGoodsList();
        for (SecuredShippingItem shippingItem : targetList) {
            if (Integer.valueOf(0).equals(shippingItem.getShippingCount().getValue())) {
                continue;
            }
            String goodsName = shippingItem.getItemName();
            for (String condGoodsName : condGoodsNameList) {
                // 部分一致
                if (goodsName.contains(condGoodsName)) {
                    checkList.add(shippingItem);
                    break;
                }
            }
        }

        if (checkList.isEmpty()) {
            return false;
        }
        targetList.clear();
        targetList.addAll(checkList);
        judgmentDto.setHmTargetOrderGoodsList(targetList);

        return true;
    }

    /**
     * 検索キーワード判定（HM用）<br/>
     * 「検索キーワード（複数可）」に該当する商品が購入商品に含まれるか
     *
     * @param judgmentDto ノベルティプレゼント判定用パラメータDto
     * @param condKeywordList ノベルティプレゼント条件に登録されている検索キーワードのリスト
     * @return TRUE:条件に一致、FALSE:条件に不一致
     */
    private boolean checkSearchKeyword(NoveltyPresentJudgmentDto judgmentDto,
                                       List<String> condKeywordList,
                                       Map<String, GoodsDetailsDto> goodsDetailsDtoMap) {

        List<SecuredShippingItem> checkList = new ArrayList<>();
        List<SecuredShippingItem> hmTargetList = judgmentDto.getHmTargetOrderGoodsList();

        for (SecuredShippingItem shippingItem : hmTargetList) {
            if (Integer.valueOf(0).equals(shippingItem.getShippingCount().getValue())) {
                continue;
            }
            List<String> keywordList =
                            getKeywordList(goodsDetailsDtoMap.get(shippingItem.getItemId()).getGoodsGroupCode());

            boolean checkFlag = false;
            for (String condKeyword : condKeywordList) {
                for (String word : keywordList) {
                    if (word.contains(condKeyword)) {
                        checkList.add(shippingItem);
                        checkFlag = true;
                        break;
                    }
                }
                if (checkFlag) {
                    break;
                }
            }
        }

        if (checkList.isEmpty()) {
            return false;
        }
        hmTargetList.clear();
        hmTargetList.addAll(checkList);
        judgmentDto.setHmTargetOrderGoodsList(hmTargetList);

        return true;
    }

    /**
     * 検索キーワードをリストにして返却する
     *
     * @param goodsGroupCode 商品管理番号
     * @return 検索キーワードのリスト
     */
    private List<String> getKeywordList(String goodsGroupCode) {
        GoodsGroupDisplayDto goodsGroupDisplayDto = productAdapter.getGoodsGroupDisplayByGoodsGroupCode(goodsGroupCode);

        List<String> keywordList = new ArrayList<String>();
        String keyword = goodsGroupDisplayDto.getSearchKeyword();
        String keywordEm = goodsGroupDisplayDto.getSearchKeywordEm();

        if (keyword != null) {
            keywordList.add(keyword);
        }

        if (keywordEm != null) {
            keywordList.add(keywordEm);
        }
        return keywordList;
    }

    /**
     * 変換対象の値を区切り文字で分割し、Listに変換する
     * @param value 変換対象
     * @param regex 区切り文字
     * @return 文字列のList
     */
    private List<String> toList(String value, String regex) {
        if (value == null || "".equals(value)) {
            return null;
        }
        List<String> retList = new ArrayList<String>();

        String[] params = value.split(regex);
        for (int i = 0; i < params.length; i++) {
            String param = params[i].trim();
            if (param != null && !"".equals(param)) {
                retList.add(param);
            }
        }

        return retList;
    }

}