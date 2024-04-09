package jp.co.itechh.quad.core.service.shop.novelty.impl;

import jp.co.itechh.quad.core.base.exception.LogicException;
import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.constant.type.HTypeEnclosureUnitType;
import jp.co.itechh.quad.core.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.core.dao.shop.novelty.NoveltyPresentConditionDao;
import jp.co.itechh.quad.core.dao.shop.novelty.NoveltyPresentEnclosureGoodsDao;
import jp.co.itechh.quad.core.dao.shop.novelty.NoveltyPresentMemberDao;
import jp.co.itechh.quad.core.dto.shop.novelty.NoveltyPresentExclusionNoveltyDto;
import jp.co.itechh.quad.core.dto.shop.novelty.NoveltyPresentIconDto;
import jp.co.itechh.quad.core.dto.shop.novelty.NoveltyPresentValidateDto;
import jp.co.itechh.quad.core.entity.shop.novelty.NoveltyPresentConditionEntity;
import jp.co.itechh.quad.core.entity.shop.novelty.NoveltyPresentEnclosureGoodsEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.shop.novelty.NoveltyPresentConditionGetLogic;
import jp.co.itechh.quad.core.service.shop.novelty.NoveltyPresentRegistService;
import jp.co.itechh.quad.product.presentation.api.ProductApi;
import jp.co.itechh.quad.product.presentation.api.param.GoodsDetailsResponse;
import jp.co.itechh.quad.product.presentation.api.param.ProductDetailByGoodCodeGetRequest;
import jp.co.itechh.quad.productnovelty.presentation.api.ProductNoveltyApi;
import jp.co.itechh.quad.productnovelty.presentation.api.param.CheckGoodsRequest;
import jp.co.itechh.quad.productnovelty.presentation.api.param.CheckGoodsResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.ListUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class NoveltyPresentRegistServiceImpl extends AbstractShopLogic implements NoveltyPresentRegistService {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(NoveltyPresentRegistServiceImpl.class);

    /** ノベルティプレゼント条件Dao */
    public NoveltyPresentConditionDao noveltyPresentConditionDao;

    /** ノベルティプレゼント同梱商品Dao */
    public NoveltyPresentEnclosureGoodsDao noveltyPresentEnclosureGoodsDao;

    /** ノベルティプレゼント対象会員 */
    public NoveltyPresentMemberDao noveltyPresentMemberDao;

    /** ノベルティプレゼント条件Logic */
    private final NoveltyPresentConditionGetLogic noveltyPresentConditionGetLogic;

    /** 商品ノベルティAPI*/
    private final ProductNoveltyApi productNoveltyApi;

    /** 日付Utility */
    public DateUtility dateUtility;

    /** 変換Helper取得 */
    public ConversionUtility conversionUtility;

    private final ProductApi productApi;

    /**
     * 変換ユーティリティクラス
     */
    @Autowired
    public NoveltyPresentRegistServiceImpl(NoveltyPresentConditionDao noveltyPresentConditionDao,
                                           NoveltyPresentEnclosureGoodsDao noveltyPresentEnclosureGoodsDao,
                                           NoveltyPresentMemberDao noveltyPresentMemberDao,
                                           NoveltyPresentConditionGetLogic noveltyPresentConditionGetLogic,
                                           ProductNoveltyApi productNoveltyApi,
                                           DateUtility dateUtility,
                                           ConversionUtility conversionUtility,
                                           ProductApi productApi) {
        this.noveltyPresentConditionDao = noveltyPresentConditionDao;
        this.noveltyPresentEnclosureGoodsDao = noveltyPresentEnclosureGoodsDao;
        this.noveltyPresentMemberDao = noveltyPresentMemberDao;
        this.noveltyPresentConditionGetLogic = noveltyPresentConditionGetLogic;
        this.productNoveltyApi = productNoveltyApi;
        this.dateUtility = dateUtility;
        this.conversionUtility = conversionUtility;
        this.productApi = productApi;
    }

    /**
     * ノベルティプレゼント条件を登録する
     *
     * @param conditionEntity          ノベルティプレゼント条件エンティティ
     * @return 登録件数
     */
    @Override
    public int regist(NoveltyPresentConditionEntity conditionEntity,
                      NoveltyPresentValidateDto noveltyPresentValidateDto) {

        // パラメータチェック
        this.checkParam(conditionEntity, noveltyPresentValidateDto);

        // 商品条件のチェックを行う
        this.checkInput(conditionEntity, noveltyPresentValidateDto);

        // 日付Utility取得
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);
        Timestamp currentTime = dateUtility.getCurrentTime();

        conditionEntity.setRegistTime(currentTime);
        conditionEntity.setUpdateTime(currentTime);

        int noveltyPresentConditionSeq = noveltyPresentConditionDao.getNoveltyPresentConditionSeq();
        conditionEntity.setNoveltyPresentConditionSeq(noveltyPresentConditionSeq);

        int ret = 0;
        try {
            ret = noveltyPresentConditionDao.insert(conditionEntity);
        } catch (Exception e) {
            LOGGER.error("例外処理が発生しました", e);
            throwMessage("NOVELTY-GOODS-001-E");
        }
        Integer seq = conditionEntity.getNoveltyPresentConditionSeq();

        List<NoveltyPresentEnclosureGoodsEntity> enclosureGoodsEntityList =
                        toNoveltyPresentEnclosureGoodsEntityList(noveltyPresentValidateDto.getNoveltyGoodsCodeList());

        for (NoveltyPresentEnclosureGoodsEntity entity : enclosureGoodsEntityList) {
            entity.noveltyPresentConditionSeq = seq;
            entity.registTime = currentTime;
            entity.updateTime = currentTime;
            try {
                ret += noveltyPresentEnclosureGoodsDao.insert(entity);
            } catch (Exception e) {
                LOGGER.error("例外処理が発生しました", e);
                throwMessage("NOVELTY-GOODS-003-E");
            }
        }

        return ret;
    }

    /**
     * ノベルティプレゼント条件を更新する
     *
     * @param conditionEntity          ノベルティプレゼント条件エンティティ
     * @return 更新件数
     */
    @Override
    public int update(NoveltyPresentConditionEntity conditionEntity,
                      NoveltyPresentValidateDto noveltyPresentValidateDto) {

        // パラメータチェック
        this.checkParam(conditionEntity, noveltyPresentValidateDto);

        // 商品条件のチェックを行う
        this.checkInput(conditionEntity, noveltyPresentValidateDto);

        // 日付Utility取得
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);
        int ret = 0;
        Timestamp currentTime = dateUtility.getCurrentTime();

        Integer noveltyPresentConditionSeq = conditionEntity.getNoveltyPresentConditionSeq();

        conditionEntity.setUpdateTime(currentTime);
        try {
            ret = noveltyPresentConditionDao.update(conditionEntity);
        } catch (Exception e) {
            LOGGER.error("例外処理が発生しました", e);
            throwMessage("NOVELTY-GOODS-002-E");
        }

        noveltyPresentEnclosureGoodsDao.deleteBySeq(noveltyPresentConditionSeq);

        List<NoveltyPresentEnclosureGoodsEntity> enclosureGoodsEntityList =
                        toNoveltyPresentEnclosureGoodsEntityList(noveltyPresentValidateDto.getNoveltyGoodsCodeList());

        for (NoveltyPresentEnclosureGoodsEntity entity : enclosureGoodsEntityList) {
            entity.noveltyPresentConditionSeq = noveltyPresentConditionSeq;
            entity.registTime = currentTime;
            entity.updateTime = currentTime;
            try {
                ret += noveltyPresentEnclosureGoodsDao.insert(entity);
            } catch (Exception e) {
                LOGGER.error("例外処理が発生しました", e);
                throwMessage("NOVELTY-GOODS-003-E");
            }
        }

        return ret;
    }

    /**
     * パラメータチェック
     * (登録時の必須項目がnullでないかチェック)
     *
     * @param conditionEntity          ノベルティプレゼント条件エンティティ
     * @param noveltyPresentValidateDto ノベルティプレゼント同梱商品エンティティのリスト
     */
    protected void checkParam(NoveltyPresentConditionEntity conditionEntity,
                              NoveltyPresentValidateDto noveltyPresentValidateDto) {

        ArgumentCheckUtil.assertNotNull("conditionEntity", conditionEntity);
        ArgumentCheckUtil.assertNotNull("enclosureUnitType", conditionEntity.getEnclosureUnitType());
        ArgumentCheckUtil.assertNotNull("noveltyPresentName", conditionEntity.getNoveltyPresentName());
        ArgumentCheckUtil.assertNotNull("noveltyPresentState", conditionEntity.getNoveltyPresentState());
        ArgumentCheckUtil.assertNotNull("noveltyPresentStartTime", conditionEntity.getNoveltyPresentStartTime());
        ArgumentCheckUtil.assertNotNull("noveltyPresentValidateDto", noveltyPresentValidateDto);

    }

    /**
     * 商品条件のチェックを行う
     *
     * @param conditionEntity          ノベルティプレゼント条件エンティティ
     * @param noveltyPresentValidateDto ノベルティプレゼント商品数確認用 検索条件クラス
     */
    private void checkInput(NoveltyPresentConditionEntity conditionEntity,
                            NoveltyPresentValidateDto noveltyPresentValidateDto) {

        // 日付項目のチェックを行う
        this.checkDateTime(conditionEntity);

        List<String> goodsGroupCodeList = noveltyPresentValidateDto.getGoodsGroupCodeList();
        List<String> goodsCodeList = noveltyPresentValidateDto.getGoodsCodeList();
        List<String> categoryIdList = noveltyPresentValidateDto.getCategoryIdList();
        List<String> goodsNameList = noveltyPresentValidateDto.getGoodsNameList();
        List<String> keywordList = noveltyPresentValidateDto.getSearchKeywordList();

        int iconCheckCount = 0;
        for (NoveltyPresentIconDto iconDto : noveltyPresentValidateDto.getIconList()) {
            if (Boolean.TRUE.equals(iconDto.isIconCheck())) {
                iconCheckCount++;
            }
        }

        HTypeEnclosureUnitType enclosureUnitType = conditionEntity.getEnclosureUnitType();
        if (HTypeEnclosureUnitType.ORDER_GOODS.equals(enclosureUnitType)) {
            if (ObjectUtils.isEmpty(goodsGroupCodeList) && ObjectUtils.isEmpty(goodsCodeList) && ObjectUtils.isEmpty(
                            categoryIdList) && iconCheckCount == 0 && ObjectUtils.isEmpty(goodsNameList)
                && ObjectUtils.isEmpty(keywordList)) {
                throwMessage("ITC-0252-123-A-E");
            }
        }

        // 除外条件:ノベルティプレゼント条件テーブル存在チェック
        List<Integer> errCodeIntList = new ArrayList<>();
        List<Integer> noveltyPresentConditionSeqList = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(noveltyPresentValidateDto.getExclusionNoveltyList())) {
            for (NoveltyPresentExclusionNoveltyDto item : noveltyPresentValidateDto.getExclusionNoveltyList()) {
                noveltyPresentConditionSeqList.add(item.getExclusionNoveltySeq());
            }
        }
        List<NoveltyPresentConditionEntity> entityDtoList =
                        noveltyPresentConditionGetLogic.execute(noveltyPresentConditionSeqList);
        for (Integer param : noveltyPresentConditionSeqList) {
            boolean judge = false;
            for (NoveltyPresentConditionEntity dto : entityDtoList) {
                Integer seq = dto.getNoveltyPresentConditionSeq();

                if (seq.equals(param)) {
                    judge = true;
                    break;
                }
            }

            if (!judge) {
                errCodeIntList.add(param);
            }
        }
        if (!errCodeIntList.isEmpty()) {
            String messageCode = "ITC-0252-112-A-E";
            for (Integer param : errCodeIntList) {
                Object[] args = {param};
                addErrorMessage(new LogicException(messageCode, args, new Throwable()));

            }
        }

        CheckGoodsResponse checkGoodsResponse =
                        productNoveltyApi.checkProduct(toCheckGoodsRequest(noveltyPresentValidateDto));
        checkError(checkGoodsResponse.getNoveltyGoodsErrorCodes(), "ITC-0252-111-A-E");
        checkError(checkGoodsResponse.getGoodsGroupErrorCodes(), "ITC-0252-113-A-E");
        checkError(checkGoodsResponse.getGoodsErrorCodes(), "ITC-0252-114-A-E");
        checkError(checkGoodsResponse.getCategoryErrorId(), "ITC-0252-115-A-E");
        // アイコン名に変換しておく
        if (!ListUtils.isEmpty(checkGoodsResponse.getIconErrorSeqs())) {
            List<String> iconErrorNames = new ArrayList<>();
            for (Integer seq : checkGoodsResponse.getIconErrorSeqs()) {
                for (NoveltyPresentIconDto item : noveltyPresentValidateDto.getIconList()) {
                    if (item.getIconSeq() != null) {
                        Integer iconSeq = item.getIconSeq();
                        if (iconSeq.equals(seq)) {
                            iconErrorNames.add(item.getIconName());
                        }
                    }
                }
            }
            checkError(iconErrorNames, "ITC-0252-116-A-E");
        }
        checkError(checkGoodsResponse.getGoodsErrorNames(), "ITC-0252-117-A-E");
        checkError(checkGoodsResponse.getSearchKewordErrors(), "ITC-0252-118-A-E");

        if (hasErrorList()) {
            throwMessage();
        }
    }

    /**
     * 日付項目のチェックを行う
     *
     * @param conditionEntity          ノベルティプレゼント条件エンティティ
     */
    private void checkDateTime(NoveltyPresentConditionEntity conditionEntity) {
        // 現在日時
        Timestamp currentTime = dateUtility.getCurrentTime();
        Timestamp start = conditionEntity.getNoveltyPresentStartTime();
        Timestamp end = conditionEntity.getNoveltyPresentEndTime();

        Integer seq = conditionEntity.getNoveltyPresentConditionSeq();

        if (seq != null) {
            // 更新時

            NoveltyPresentConditionEntity entity = noveltyPresentConditionGetLogic.execute(seq);
            Timestamp startTimeDb = entity.getNoveltyPresentStartTime();
            HTypeEnclosureUnitType dbEncUnitType = entity.getEnclosureUnitType();
            HTypeEnclosureUnitType inEncUnitType = conditionEntity.getEnclosureUnitType();
            if (startTimeDb.before(currentTime)) {
                // ノベルティプレゼント期間中にノベルティプレゼント条件開始日時を変更した場合はエラー
                if (!startTimeDb.equals(start)) {
                    throwMessage("ITC-0252-121-A-E");
                }
                // ノベルティプレゼント期間中にノベルティ商品同梱単位を変更した場合ははエラー
                if (!dbEncUnitType.equals(inEncUnitType)) {
                    throwMessage("ITC-0252-124-A-E");
                }
            }
        }
    }

    /**
     * Set time
     *
     * @param hourOfDay
     * @param minute
     * @param second
     * @param milisecond
     */
    private Timestamp setTimestamp(int hourOfDay, int minute, int second, int milisecond) {

        Timestamp timestamp = new Timestamp(0);
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timestamp.getTime());
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, second);
        c.set(Calendar.MILLISECOND, milisecond);
        timestamp.setTime(c.getTimeInMillis());
        return timestamp;
    }

    /**
     * ノベルティプレゼント条件を削除する
     *
     * @param noveltyPresentConditionSeq ノベルティプレゼント条件SEQ
     * @return 削除件数
     */
    @Override
    public int delete(Integer noveltyPresentConditionSeq) {
        int ret = 0;
        NoveltyPresentConditionEntity entity = noveltyPresentConditionDao.getEntity(noveltyPresentConditionSeq);
        if (entity != null) {
            ret = noveltyPresentConditionDao.delete(entity);
        }

        ret += noveltyPresentEnclosureGoodsDao.deleteBySeq(noveltyPresentConditionSeq);

        ret += noveltyPresentMemberDao.deleteBySeq(noveltyPresentConditionSeq);
        return ret;
    }

    /**
     * 商品コードから商品SEQに変換する
     *
     * @return 商品SEQのリスト
     */
    private List<NoveltyPresentEnclosureGoodsEntity> toNoveltyPresentEnclosureGoodsEntityList(List<String> goodsCodeList) {

        List<NoveltyPresentEnclosureGoodsEntity> noveltyPresentEnclosureGoodsEntities = new ArrayList<>();
        int priorityOrder = 0;
        for (String goodsCode : goodsCodeList) {
            ProductDetailByGoodCodeGetRequest productDetailByGoodCodeGetRequest =
                            new ProductDetailByGoodCodeGetRequest();
            productDetailByGoodCodeGetRequest.setGoodsCode(goodsCode);
            productDetailByGoodCodeGetRequest.setOpenStatus(HTypeOpenDeleteStatus.NO_OPEN.getValue());
            GoodsDetailsResponse goodsDetailsResponse =
                            productApi.getDetailsByGoodsCode(goodsCode, productDetailByGoodCodeGetRequest);
            if (goodsDetailsResponse != null) {
                NoveltyPresentEnclosureGoodsEntity noveltyPresentEnclosureGoodsEntity =
                                new NoveltyPresentEnclosureGoodsEntity();

                noveltyPresentEnclosureGoodsEntity.setGoodsSeq(goodsDetailsResponse.getGoodsSeq());
                noveltyPresentEnclosureGoodsEntity.setPriorityOrder(priorityOrder);
                priorityOrder++;

                noveltyPresentEnclosureGoodsEntities.add(noveltyPresentEnclosureGoodsEntity);
            }
        }

        return noveltyPresentEnclosureGoodsEntities;
    }

    /**
     * ノベルティプレゼント条件の商品数確認リクエスト
     *
     * @param noveltyPresentValidateDto ノベルティプレゼント商品数確認用 検索条件クラス
     * @return ノベルティプレゼント条件の商品数確認リクエスト
     */
    private CheckGoodsRequest toCheckGoodsRequest(NoveltyPresentValidateDto noveltyPresentValidateDto) {

        CheckGoodsRequest checkGoodsRequest = new CheckGoodsRequest();
        checkGoodsRequest.setNoveltyGoodsCodeList(noveltyPresentValidateDto.getNoveltyGoodsCodeList());
        checkGoodsRequest.setGoodsGroupCodeList(noveltyPresentValidateDto.getGoodsGroupCodeList());
        checkGoodsRequest.setGoodsCodeList(noveltyPresentValidateDto.getGoodsCodeList());
        checkGoodsRequest.setCategoryIdList(noveltyPresentValidateDto.getCategoryIdList());
        List<Integer> iconSeqList = new ArrayList<>();
        for (NoveltyPresentIconDto item : noveltyPresentValidateDto.getIconList()) {
            iconSeqList.add(item.getIconSeq());
        }
        checkGoodsRequest.setIconSeqList(iconSeqList);
        checkGoodsRequest.setGoodsNameList(noveltyPresentValidateDto.getGoodsNameList());
        checkGoodsRequest.setSearchKeywordList(noveltyPresentValidateDto.getSearchKeywordList());

        return checkGoodsRequest;
    }

    public void checkError(List<String> errorList, String messageCode) {
        if (!ListUtils.isEmpty(errorList)) {
            for (String param : errorList) {
                Object[] args = {param};
                addErrorMessage(new LogicException(messageCode, args, new Throwable()));
            }
        }

        errorList = new ArrayList<>();
    }
}