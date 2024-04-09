package jp.co.itechh.quad.admin.pc.web.admin.shop.novelty.registupdate;

import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.base.utility.DateUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeEnclosureUnitType;
import jp.co.itechh.quad.admin.constant.type.HTypeGoodsPriceTotalFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeMagazineSendFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeNoveltyPresentState;
import jp.co.itechh.quad.admin.entity.shop.novelty.NoveltyPresentConditionEntity;
import jp.co.itechh.quad.icon.presentation.api.param.IconResponse;
import jp.co.itechh.quad.novelty.presentation.api.param.NoveltyPresentConditionListResponse;
import jp.co.itechh.quad.novelty.presentation.api.param.NoveltyPresentConditionRegistRequest;
import jp.co.itechh.quad.novelty.presentation.api.param.NoveltyPresentConditionRequest;
import jp.co.itechh.quad.novelty.presentation.api.param.NoveltyPresentConditionResponse;
import jp.co.itechh.quad.novelty.presentation.api.param.NoveltyPresentConditionUpdateRequest;
import jp.co.itechh.quad.novelty.presentation.api.param.NoveltyPresentExclusionNoveltyRequest;
import jp.co.itechh.quad.novelty.presentation.api.param.NoveltyPresentIconRequest;
import jp.co.itechh.quad.novelty.presentation.api.param.NoveltyPresentSearchResultResponse;
import jp.co.itechh.quad.novelty.presentation.api.param.NoveltyPresentValidateListRequest;
import jp.co.itechh.quad.productnovelty.presentation.api.param.NoveltyPresentConditionTargetGoodsCountGetRequest;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ノベルティ登録更新Helper
 */
@Component
public class NoveltyRegistUpdateHelper {

    /**
     * 日付関連Helper取得
     */
    public DateUtility dateUtility;

    /**
     * 変換Helper
     */
    public ConversionUtility conversionUtility;

    /**
     * コンストラクター
     */
    public NoveltyRegistUpdateHelper(DateUtility dateUtility, ConversionUtility conversionUtility) {
        this.conversionUtility = conversionUtility;
        this.dateUtility = dateUtility;
    }

    /**
     * 初期処理時の画面反映
     *
     * @param noveltyRegistUpdateModel            ページクラス
     * @param noveltyPresentConditionEntity       ノベルティプレゼント条件Dto
     * @param enclosureGoodsCodeList              ノベルティプレゼント商品コードのList
     * @param noveltyPresentConditionListResponse 除外条件
     * @param iconList                            商品インフォメーションアイコンDTOのList
     */
    public void toPageForLoad(NoveltyRegistUpdateModel noveltyRegistUpdateModel,
                              NoveltyPresentConditionEntity noveltyPresentConditionEntity,
                              List<String> enclosureGoodsCodeList,
                              NoveltyPresentConditionListResponse noveltyPresentConditionListResponse,
                              List<IconResponse> iconList) {
        List<NoveltyPresentSearchResultResponse> noveltyPresentConditions = new ArrayList<>();
        if (!ObjectUtils.isEmpty(noveltyPresentConditionListResponse)) {
            noveltyPresentConditions = noveltyPresentConditionListResponse.getNoveltyPresentConditionList();
        }
        // 除外条件の設定
        setExclusionInfo(noveltyRegistUpdateModel, noveltyPresentConditions);

        // アイコン情報を設定
        setIconInfo(noveltyRegistUpdateModel, iconList);

        // 入力値
        setInputData(noveltyRegistUpdateModel, noveltyPresentConditionEntity, enclosureGoodsCodeList);

        // エンティティをセットしておく
        noveltyRegistUpdateModel.setNoveltyPresentConditionEntity(noveltyPresentConditionEntity);
    }

    /**
     * 入力値を設定する
     *
     * @param noveltyRegistUpdateModel      ページクラス
     * @param noveltyPresentConditionEntity エンティティ
     * @param enclosureGoodsCodeList        ノベルティプレゼント商品コードのList
     */
    private void setInputData(NoveltyRegistUpdateModel noveltyRegistUpdateModel,
                              NoveltyPresentConditionEntity noveltyPresentConditionEntity,
                              List<String> enclosureGoodsCodeList) {

        noveltyRegistUpdateModel.setEnclosureUnitType(HTypeEnclosureUnitType.ORDER.getValue());

        if (noveltyPresentConditionEntity == null) {
            return;
        }

        // ノベルティプレゼント条件SEQ
        noveltyRegistUpdateModel.setNoveltyPresentConditionSeq(
                        noveltyPresentConditionEntity.getNoveltyPresentConditionSeq());

        // 基本情報
        setBaseInfo(noveltyRegistUpdateModel, noveltyPresentConditionEntity, enclosureGoodsCodeList);

        // 会員情報条件
        setMemberInfo(noveltyRegistUpdateModel, noveltyPresentConditionEntity);

        // 商品情報条件
        setGoodsInfo(noveltyRegistUpdateModel, noveltyPresentConditionEntity);

        // 受注情報条件
        setOrderInfo(noveltyRegistUpdateModel, noveltyPresentConditionEntity);

        // その他
        noveltyRegistUpdateModel.setMemo(noveltyPresentConditionEntity.getMemo());
    }

    /**
     * 画面に受注情報条件を設定する
     *
     * @param noveltyRegistUpdateModel      ページクラス
     * @param noveltyPresentConditionEntity ノベルティプレゼント条件Dto
     */
    private void setOrderInfo(NoveltyRegistUpdateModel noveltyRegistUpdateModel,
                              NoveltyPresentConditionEntity noveltyPresentConditionEntity) {
        // 商品金額合計
        if (noveltyPresentConditionEntity.getGoodsPriceTotal() != null) {
            noveltyRegistUpdateModel.setGoodsPriceTotal(
                            String.valueOf(noveltyPresentConditionEntity.getGoodsPriceTotal()));
        }

        HTypeGoodsPriceTotalFlag goodsPriceTotalFlag = noveltyPresentConditionEntity.goodsPriceTotalFlag;
        noveltyRegistUpdateModel.setGoodsPriceTotalFlag(false);
        if (HTypeGoodsPriceTotalFlag.ON.equals(goodsPriceTotalFlag)) {
            noveltyRegistUpdateModel.setGoodsPriceTotalFlag(true);

        }
    }

    /**
     * 画面に商品情報条件を設定する
     *
     * @param noveltyRegistUpdateModel      ページクラス
     * @param noveltyPresentConditionEntity ノベルティプレゼント条件Dto
     */
    private void setGoodsInfo(NoveltyRegistUpdateModel noveltyRegistUpdateModel,
                              NoveltyPresentConditionEntity noveltyPresentConditionEntity) {
        // 商品管理番号
        noveltyRegistUpdateModel.setGoodsGroupCode(noveltyPresentConditionEntity.getGoodsGroupCode());

        // 商品番号
        noveltyRegistUpdateModel.setGoodsCode(noveltyPresentConditionEntity.getGoodsCode());

        // カテゴリーID
        noveltyRegistUpdateModel.setCategoryId(noveltyPresentConditionEntity.getCategoryId());

        // アイコン
        List<NoveltyRegistUpdateIconItem> iconList = noveltyRegistUpdateModel.getIconItems();
        String iconId = noveltyPresentConditionEntity.iconId;
        if (iconList != null && iconId != null) {
            List<NoveltyRegistUpdateIconItem> listData = new ArrayList<>();
            for (NoveltyRegistUpdateIconItem item : iconList) {
                String seq = item.getIconSeq();
                if (iconId.indexOf(seq) != -1) {
                    item.setIconCheck(true);
                }
                listData.add(item);

            }
            noveltyRegistUpdateModel.setIconItems(listData);
        }

        // 商品名
        noveltyRegistUpdateModel.setGoodsName(noveltyPresentConditionEntity.getGoodsName());

        // 検索キーワード
        noveltyRegistUpdateModel.setSearchkeyword(noveltyPresentConditionEntity.getSearchKeyword());

        String[] iconChecked = new String[noveltyRegistUpdateModel.getIconItems().size()];
        for (int i = 0; i < noveltyRegistUpdateModel.getIconItems().size(); i++) {
            if (noveltyRegistUpdateModel.getIconItems().get(i).isIconCheck()) {
                iconChecked[i] = noveltyRegistUpdateModel.getIconItems().get(i).getIconSeq();
            }
        }

        noveltyRegistUpdateModel.setIconChecked(iconChecked);
    }

    /**
     * 画面に会員情報条件を設定する
     *
     * @param noveltyRegistUpdateModel      ページクラス
     * @param noveltyPresentConditionEntity ノベルティプレゼント条件Dto
     */
    private void setMemberInfo(NoveltyRegistUpdateModel noveltyRegistUpdateModel,
                               NoveltyPresentConditionEntity noveltyPresentConditionEntity) {
        String admissionStartDate = conversionUtility.toYmd(noveltyPresentConditionEntity.admissionStartTime);
        String admissionStartTime = conversionUtility.toHms(noveltyPresentConditionEntity.admissionStartTime);
        String admissionEndDate = conversionUtility.toYmd(noveltyPresentConditionEntity.admissionEndTime);
        String admissionEndTime = conversionUtility.toHms(noveltyPresentConditionEntity.admissionEndTime);

        // プレゼント数制限
        if (noveltyPresentConditionEntity.getPrizeGoodsLimit() != null) {
            noveltyRegistUpdateModel.setPrizeGoodsLimit(
                            conversionUtility.toString(noveltyPresentConditionEntity.getPrizeGoodsLimit()));
        }

        // 入会期間
        noveltyRegistUpdateModel.setAdmissionStartDate(admissionStartDate);
        noveltyRegistUpdateModel.setAdmissionStartTime(admissionStartTime);
        noveltyRegistUpdateModel.setAdmissionEndDate(admissionEndDate);
        noveltyRegistUpdateModel.setAdmissionEndTime(admissionEndTime);

        // メールマガジン
        HTypeMagazineSendFlag magazineSendFlag = noveltyPresentConditionEntity.magazineSendFlag;
        noveltyRegistUpdateModel.setMagazineSendFlag(false);
        if (HTypeMagazineSendFlag.ON.equals(magazineSendFlag)) {
            noveltyRegistUpdateModel.setMagazineSendFlag(true);

        }
    }

    /**
     * 画面に受注情報条件を設定する
     *
     * @param noveltyRegistUpdateModel      ページクラス
     * @param noveltyPresentConditionEntity ノベルティプレゼント条件Dto
     * @param enclosureGoodsCodeList        ノベルティプレゼント商品コードのList
     */
    private void setBaseInfo(NoveltyRegistUpdateModel noveltyRegistUpdateModel,
                             NoveltyPresentConditionEntity noveltyPresentConditionEntity,
                             List<String> enclosureGoodsCodeList) {
        String startDate = conversionUtility.toYmd(noveltyPresentConditionEntity.noveltyPresentStartTime);
        String startTime = conversionUtility.toHms(noveltyPresentConditionEntity.noveltyPresentStartTime);
        String endDate = conversionUtility.toYmd(noveltyPresentConditionEntity.noveltyPresentEndTime);
        String endTime = conversionUtility.toHms(noveltyPresentConditionEntity.noveltyPresentEndTime);

        // ノベルティプレゼント条件名
        noveltyRegistUpdateModel.setNoveltyPresentName(noveltyPresentConditionEntity.getNoveltyPresentName());

        // ノベルティプレゼント条件状態
        if (noveltyPresentConditionEntity.getNoveltyPresentState() != null) {
            noveltyRegistUpdateModel.setNoveltyPresentState(
                            noveltyPresentConditionEntity.getNoveltyPresentState().getValue());
        }

        // ノベルティプレゼント条件開始日
        noveltyRegistUpdateModel.setNoveltyPresentStartDate(startDate);
        noveltyRegistUpdateModel.setNoveltyPresentStartTime(startTime);
        noveltyRegistUpdateModel.setBeforeNoveltyPresentStartDate(startDate);
        noveltyRegistUpdateModel.setBeforeNoveltyPresentStartTime(startTime);

        // ノベルティプレゼント条件終了日
        noveltyRegistUpdateModel.setNoveltyPresentEndDate(endDate);
        noveltyRegistUpdateModel.setNoveltyPresentEndTime(endTime);

        // ノベルティ商品番号 (テキスト)
        if (enclosureGoodsCodeList != null) {
            StringBuilder buf = new StringBuilder();
            for (String goodsCode : enclosureGoodsCodeList) {
                buf.append(goodsCode);
                buf.append("\n");
            }
            noveltyRegistUpdateModel.setNoveltyGoodsCode(buf.toString());
        }

        // ノベルティ商品同梱単位
        HTypeEnclosureUnitType enclosureUnitType = noveltyPresentConditionEntity.enclosureUnitType;
        noveltyRegistUpdateModel.setEnclosureUnitType(enclosureUnitType.getValue());

        // 除外条件
        List<NoveltyRegistUpdateExclusionNoveltyItem> exclusionList =
                        noveltyRegistUpdateModel.getExclusionNoveltyItems();
        String seqData = noveltyPresentConditionEntity.exclusionNoveltyPresentSeq;
        if (exclusionList != null && seqData != null) {
            List<NoveltyRegistUpdateExclusionNoveltyItem> listData = new ArrayList<>();

            List<String> seqDataList = toList(seqData, ",");
            for (NoveltyRegistUpdateExclusionNoveltyItem item : exclusionList) {
                Integer seq = item.exclusionNoveltySeq;
                if (seqDataList.contains(seq.toString())) {
                    item.exclusionNoveltyCheck = true;
                }
                listData.add(item);
            }
            noveltyRegistUpdateModel.setExclusionNoveltyItems(listData);

        }
    }

    /**
     * 除外条件を設定する
     *
     * @param noveltyRegistUpdateModel ページクラス
     * @param noveltyPresentConditions 除外条件のList
     */
    private void setExclusionInfo(NoveltyRegistUpdateModel noveltyRegistUpdateModel,
                                  List<NoveltyPresentSearchResultResponse> noveltyPresentConditions) {

        if (noveltyPresentConditions == null) {
            return;
        }

        List<NoveltyRegistUpdateExclusionNoveltyItem> items = new ArrayList<>();

        for (NoveltyPresentSearchResultResponse noveltyPresentCondition : noveltyPresentConditions) {
            NoveltyRegistUpdateExclusionNoveltyItem item = new NoveltyRegistUpdateExclusionNoveltyItem();
            item.exclusionNoveltyCheck = false;
            item.exclusionNoveltySeq = noveltyPresentCondition.getNoveltyPresentConditionSeq();
            item.exclusionNoveltyName = noveltyPresentCondition.getNoveltyPresentName();
            items.add(item);
        }

        noveltyRegistUpdateModel.setExclusionNoveltyItems(items);

    }

    /**
     * アイコン情報を設定する
     *
     * @param noveltyRegistUpdateModel ページクラス
     * @param iconList                 商品インフォメーションアイコンDTOのList
     */
    private void setIconInfo(NoveltyRegistUpdateModel noveltyRegistUpdateModel, List<IconResponse> iconList) {
        if (iconList == null) {
            return;
        }

        List<NoveltyRegistUpdateIconItem> items = new ArrayList<NoveltyRegistUpdateIconItem>();

        for (IconResponse iconResponse : iconList) {
            NoveltyRegistUpdateIconItem item = new NoveltyRegistUpdateIconItem();
            item.setIconCheck(false);
            item.setIconSeq(String.valueOf(iconResponse.getIconSeq()));
            item.setIconName(iconResponse.getIconName());

            items.add(item);
        }

        noveltyRegistUpdateModel.setIconItems(items);
    }

    /**
     * 年月日・時分秒→タイムスタンプ
     *
     * @param ymd 日付項目値
     * @param hms 時刻項目値
     * @return 引数から取得されるタイムスタンプ
     */
    public Timestamp ymdhmsToTimestamp(String ymd, String hms) {

        Timestamp ret = null;
        if (ymd != null && hms != null) {
            ret = dateUtility.toTimestampValue(ymd + " " + hms, DateUtility.YMD_SLASH_HMS + ".SSS");
        }
        return ret;

    }

    /**
     * 改行で区切られた文字列をListに変換する
     *
     * @param value 改行で区切られた文字列
     * @return 文字列のList
     */
    public List<String> toList(String value) {
        if (value == null) {
            return new ArrayList<String>();
        }

        String[] params = value.split("\n");
        List<String> retList = new ArrayList<String>();
        for (int i = 0; i < params.length; i++) {
            String param = params[i].trim();
            if (!"".equals(param)) {
                retList.add(param);
            }
        }

        return retList;
    }

    /**
     * NoveltyPresentJudgmentCheckServiceImpl参照
     * 変換対象の値を区切り文字で分割し、Listに変換する
     *
     * @param value 変換対象
     * @param regex 区切り文字
     * @return 文字列のList
     */
    private List<String> toList(String value, String regex) {
        List<String> retList = new ArrayList<String>();

        if (value == null || "".equals(value)) {
            return retList;
        }

        String[] params = value.split(regex);
        for (int i = 0; i < params.length; i++) {
            String param = params[i].trim();
            if (param != null && !"".equals(param)) {
                retList.add(param);
            }
        }

        return retList;
    }

    /**
     * ノベルティプレゼント条件クラスに変換する
     *
     * @param noveltyPresentConditionResponse ノベルティプレゼント条件レスポンス
     * @return ノベルティプレゼント条件クラス
     */
    public NoveltyPresentConditionEntity toNoveltyPresentConditionEntity(NoveltyPresentConditionResponse noveltyPresentConditionResponse) {
        NoveltyPresentConditionEntity noveltyPresentConditionEntity = new NoveltyPresentConditionEntity();

        if (ObjectUtils.isEmpty(noveltyPresentConditionResponse)) {
            return null;
        }

        noveltyPresentConditionEntity.setNoveltyPresentConditionSeq(
                        noveltyPresentConditionResponse.getNoveltyPresentConditionSeq());
        noveltyPresentConditionEntity.setNoveltyPresentName(noveltyPresentConditionResponse.getNoveltyPresentName());
        if (StringUtils.isNotEmpty(noveltyPresentConditionResponse.getEnclosureUnitType())) {
            noveltyPresentConditionEntity.setEnclosureUnitType(
                            HTypeEnclosureUnitType.valueOf(noveltyPresentConditionResponse.getEnclosureUnitType()));
        }
        if (StringUtils.isNotEmpty(noveltyPresentConditionResponse.getNoveltyPresentState())) {
            noveltyPresentConditionEntity.setNoveltyPresentState(
                            HTypeNoveltyPresentState.valueOf(noveltyPresentConditionResponse.getNoveltyPresentState()));
        }
        noveltyPresentConditionEntity.setNoveltyPresentStartTime(
                        conversionUtility.toTimestamp(noveltyPresentConditionResponse.getNoveltyPresentStartTime()));
        noveltyPresentConditionEntity.setNoveltyPresentEndTime(
                        conversionUtility.toTimestamp(noveltyPresentConditionResponse.getNoveltyPresentEndTime()));
        noveltyPresentConditionEntity.setExclusionNoveltyPresentSeq(
                        noveltyPresentConditionResponse.getExclusionNoveltyPresentSeq());
        if (StringUtils.isNotEmpty(noveltyPresentConditionResponse.getMagazineSendFlag())) {
            noveltyPresentConditionEntity.setMagazineSendFlag(
                            HTypeMagazineSendFlag.valueOf(noveltyPresentConditionResponse.getMagazineSendFlag()));
        }
        noveltyPresentConditionEntity.setAdmissionStartTime(
                        conversionUtility.toTimestamp(noveltyPresentConditionResponse.getAdmissionStartTime()));
        noveltyPresentConditionEntity.setAdmissionEndTime(
                        conversionUtility.toTimestamp(noveltyPresentConditionResponse.getAdmissionEndTime()));
        noveltyPresentConditionEntity.setGoodsGroupCode(noveltyPresentConditionResponse.getGoodsGroupCode());
        noveltyPresentConditionEntity.setGoodsCode(noveltyPresentConditionResponse.getGoodsCode());
        noveltyPresentConditionEntity.setCategoryId(noveltyPresentConditionResponse.getCategoryId());
        noveltyPresentConditionEntity.setIconId(noveltyPresentConditionResponse.getIconId());
        noveltyPresentConditionEntity.setGoodsName(noveltyPresentConditionResponse.getGoodsName());
        noveltyPresentConditionEntity.setSearchKeyword(noveltyPresentConditionResponse.getSearchKeyword());
        noveltyPresentConditionEntity.setGoodsPriceTotal(noveltyPresentConditionResponse.getGoodsPriceTotal());
        if (StringUtils.isNotEmpty(noveltyPresentConditionResponse.getGoodsPriceTotalFlag())) {
            noveltyPresentConditionEntity.setGoodsPriceTotalFlag(
                            HTypeGoodsPriceTotalFlag.valueOf(noveltyPresentConditionResponse.getGoodsPriceTotalFlag()));
        }
        noveltyPresentConditionEntity.setPrizeGoodsLimit(noveltyPresentConditionResponse.getPrizeGoodsLimit());
        noveltyPresentConditionEntity.setMemo(noveltyPresentConditionResponse.getMemo());
        return noveltyPresentConditionEntity;
    }

    /**
     * ノベルティプレゼント条件更新リクエストに変換する
     *
     * @param noveltyRegistUpdateModel ノベルティプレゼント条件登録/更新画面
     * @return ノベルティプレゼント条件更新リクエスト
     */
    public NoveltyPresentConditionUpdateRequest toNoveltyPresentConditionUpdateRequest(NoveltyRegistUpdateModel noveltyRegistUpdateModel) {
        NoveltyPresentConditionUpdateRequest noveltyPresentConditionUpdateRequest =
                        new NoveltyPresentConditionUpdateRequest();

        NoveltyPresentConditionRequest noveltyPresentConditionRequest =
                        toNoveltyPresentConditionRequest(noveltyRegistUpdateModel);
        NoveltyPresentValidateListRequest noveltyPresentValidateListRequest =
                        toNoveltyPresentValidateListRequest(noveltyRegistUpdateModel);

        noveltyPresentConditionUpdateRequest.setNoveltyPresentConditionRequest(noveltyPresentConditionRequest);
        noveltyPresentConditionUpdateRequest.setNoveltyPresentValidateListRequest(noveltyPresentValidateListRequest);
        return noveltyPresentConditionUpdateRequest;
    }

    /**
     * ノベルティプレゼント条件更新リクエストに変換する
     *
     * @param noveltyRegistUpdateModel ノベルティプレゼント条件登録/更新画面
     * @return ノベルティプレゼント条件更新リクエスト
     */
    public NoveltyPresentConditionRegistRequest toNoveltyPresentConditionRegistRequest(NoveltyRegistUpdateModel noveltyRegistUpdateModel) {
        NoveltyPresentConditionRegistRequest noveltyPresentConditionRegistRequest =
                        new NoveltyPresentConditionRegistRequest();

        NoveltyPresentConditionRequest NoveltyPresentConditionRequest =
                        toNoveltyPresentConditionRequest(noveltyRegistUpdateModel);
        NoveltyPresentValidateListRequest noveltyPresentValidateListRequest =
                        toNoveltyPresentValidateListRequest(noveltyRegistUpdateModel);

        noveltyPresentConditionRegistRequest.setNoveltyPresentConditionRequest(NoveltyPresentConditionRequest);
        noveltyPresentConditionRegistRequest.setNoveltyPresentValidateListRequest(noveltyPresentValidateListRequest);
        return noveltyPresentConditionRegistRequest;
    }

    /**
     * 登録/更新時の選択可能除外条件レスポンスに変換する
     *
     * @param noveltyRegistUpdateModel ノベルティプレゼント条件登録/更新画面
     * @return 登録/更新時の選択可能除外条件レスポンス
     */
    public NoveltyPresentConditionRequest toNoveltyPresentConditionRequest(NoveltyRegistUpdateModel noveltyRegistUpdateModel) {

        if (ObjectUtils.isEmpty(noveltyRegistUpdateModel)) {
            return null;
        }

        NoveltyPresentConditionRequest noveltyPresentConditionRequest = new NoveltyPresentConditionRequest();

        String delim = "";
        StringBuilder exclusion = new StringBuilder();
        List<NoveltyRegistUpdateExclusionNoveltyItem> exclusionItems =
                        noveltyRegistUpdateModel.getExclusionNoveltyItems();
        for (NoveltyRegistUpdateExclusionNoveltyItem item : exclusionItems) {
            if (item.isExclusionNoveltyCheck()) {
                exclusion.append(delim);
                exclusion.append(item.getExclusionNoveltySeq());
                delim = ",";
            }
        }

        List<NoveltyRegistUpdateIconItem> iconItems = noveltyRegistUpdateModel.getIconItems();
        StringBuilder icon = new StringBuilder();
        delim = "";
        for (NoveltyRegistUpdateIconItem item : iconItems) {
            if (item.isIconCheck()) {
                icon.append(delim);
                icon.append(item.getIconSeq());
                delim = ",";
            }
        }

        // 会員情報条件
        Timestamp startTime = ymdhmsToTimestamp(noveltyRegistUpdateModel.getAdmissionStartDate(), "00:00:00.000");
        Timestamp endTime = ymdhmsToTimestamp(noveltyRegistUpdateModel.getAdmissionEndDate(), "23:59:59.999");

        if (noveltyRegistUpdateModel.getPrizeGoodsLimit() != null) {
            noveltyPresentConditionRequest.setPrizeGoodsLimit(
                            conversionUtility.toInteger(noveltyRegistUpdateModel.getPrizeGoodsLimit()));
        }
        noveltyPresentConditionRequest.setAdmissionStartTime(startTime);
        noveltyPresentConditionRequest.setAdmissionEndTime(endTime);

        if (StringUtils.isEmpty(noveltyRegistUpdateModel.getNoveltyPresentStartTime()) && StringUtils.isNotEmpty(
                        noveltyRegistUpdateModel.getNoveltyPresentStartDate())) {
            noveltyRegistUpdateModel.setNoveltyPresentStartTime("00:00:00");
        }
        if (StringUtils.isEmpty(noveltyRegistUpdateModel.getNoveltyPresentEndTime()) && StringUtils.isNotEmpty(
                        noveltyRegistUpdateModel.getNoveltyPresentEndDate())) {
            noveltyRegistUpdateModel.setNoveltyPresentEndTime("23:59:59");
        }

        startTime = ymdhmsToTimestamp(
                        noveltyRegistUpdateModel.getNoveltyPresentStartDate(),
                        noveltyRegistUpdateModel.getNoveltyPresentStartTime() + ".000"
                                     );
        endTime = ymdhmsToTimestamp(
                        noveltyRegistUpdateModel.getNoveltyPresentEndDate(),
                        noveltyRegistUpdateModel.getNoveltyPresentEndTime() + ".999"
                                   );

        noveltyPresentConditionRequest.setNoveltyPresentConditionSeq(
                        noveltyRegistUpdateModel.getNoveltyPresentConditionSeq());
        noveltyPresentConditionRequest.setNoveltyPresentName(noveltyRegistUpdateModel.getNoveltyPresentName());
        noveltyPresentConditionRequest.setEnclosureUnitType(noveltyRegistUpdateModel.getEnclosureUnitType());
        noveltyPresentConditionRequest.setNoveltyPresentState(noveltyRegistUpdateModel.getNoveltyPresentState());
        noveltyPresentConditionRequest.setNoveltyPresentStartTime(startTime);
        noveltyPresentConditionRequest.setNoveltyPresentEndTime(endTime);
        noveltyPresentConditionRequest.setExclusionNoveltyPresentSeq(exclusion.toString());

        if (noveltyRegistUpdateModel.isMagazineSendFlag()) {
            noveltyPresentConditionRequest.setMagazineSendFlag(HTypeMagazineSendFlag.ON.getValue());
        } else {
            noveltyPresentConditionRequest.setMagazineSendFlag(HTypeMagazineSendFlag.OFF.getValue());
        }
        noveltyPresentConditionRequest.setGoodsGroupCode(noveltyRegistUpdateModel.getGoodsGroupCode());
        noveltyPresentConditionRequest.setGoodsCode(noveltyRegistUpdateModel.getGoodsCode());
        noveltyPresentConditionRequest.setCategoryId(noveltyRegistUpdateModel.getCategoryId());
        noveltyPresentConditionRequest.setIconId(icon.toString());
        noveltyPresentConditionRequest.setGoodsName(noveltyRegistUpdateModel.getGoodsName());
        noveltyPresentConditionRequest.setSearchKeyword(noveltyRegistUpdateModel.getSearchkeyword());

        if (StringUtils.isNotEmpty(noveltyRegistUpdateModel.getGoodsPriceTotal())) {
            noveltyPresentConditionRequest.setGoodsPriceTotal(
                            conversionUtility.toBigDecimal(noveltyRegistUpdateModel.getGoodsPriceTotal()));
        }

        if (noveltyRegistUpdateModel.isGoodsPriceTotalFlag()) {
            noveltyPresentConditionRequest.setGoodsPriceTotalFlag(HTypeGoodsPriceTotalFlag.ON.getValue());
        } else {
            noveltyPresentConditionRequest.setGoodsPriceTotalFlag(HTypeGoodsPriceTotalFlag.OFF.getValue());
        }
        noveltyPresentConditionRequest.setMemo(noveltyRegistUpdateModel.getMemo());
        noveltyPresentConditionRequest.setRegistTime(dateUtility.getCurrentTime());
        noveltyPresentConditionRequest.setUpdateTime(dateUtility.getCurrentTime());
        return noveltyPresentConditionRequest;
    }

    /**
     * ノベルティプレゼント同梱商品に変換する
     *
     * @return ノベルティプレゼント同梱商品
     */
    public NoveltyPresentValidateListRequest toNoveltyPresentValidateListRequest(NoveltyRegistUpdateModel noveltyRegistUpdateModel) {
        NoveltyPresentValidateListRequest noveltyPresentValidateListRequest = new NoveltyPresentValidateListRequest();

        if (ObjectUtils.isEmpty(noveltyRegistUpdateModel)) {
            return null;
        }

        NoveltyPresentConditionTargetGoodsCountGetRequest noveltyPresentConditionTargetGoodsCountGetRequest =
                        toNoveltyPresentConditionTargetGoodsCountGetRequest(noveltyRegistUpdateModel);

        List<String> listData = toList(noveltyRegistUpdateModel.getNoveltyGoodsCode().trim());
        noveltyPresentValidateListRequest.setNoveltyGoodsCodeList(listData);
        noveltyPresentValidateListRequest.setGoodsGroupCodeList(
                        noveltyPresentConditionTargetGoodsCountGetRequest.getGoodsGroupCodeList());
        noveltyPresentValidateListRequest.setGoodsCodeList(
                        noveltyPresentConditionTargetGoodsCountGetRequest.getGoodsCodeList());
        noveltyPresentValidateListRequest.setCategoryIdList(toList(noveltyRegistUpdateModel.getCategoryId()));
        noveltyPresentValidateListRequest.setCategorySeqList(
                        noveltyPresentConditionTargetGoodsCountGetRequest.getCategorySeqList());
        noveltyPresentValidateListRequest.setIconList(
                        toNoveltyPresentIconRequestList(noveltyRegistUpdateModel.getIconItems()));
        noveltyPresentValidateListRequest.setGoodsNameList(
                        noveltyPresentConditionTargetGoodsCountGetRequest.getGoodsNameList());
        noveltyPresentValidateListRequest.setSearchKeywordList(
                        noveltyPresentConditionTargetGoodsCountGetRequest.getSearchKeywordList());
        noveltyPresentValidateListRequest.setExclusionNoveltyList(toNoveltyPresentExclusionNoveltyRequestList(
                        noveltyRegistUpdateModel.getExclusionNoveltyItems()));

        return noveltyPresentValidateListRequest;
    }

    /**
     * ノベルティプレゼントアイコンリクエストリストに変換
     *
     * @param iconItems ノベルティプレゼントアイコンリクエスト
     * @return ノベルティプレゼントアイコンリクエストリスト
     */
    public List<NoveltyPresentIconRequest> toNoveltyPresentIconRequestList(List<NoveltyRegistUpdateIconItem> iconItems) {

        if (CollectionUtils.isEmpty(iconItems)) {
            return null;
        }

        List<NoveltyPresentIconRequest> noveltyPresentIconRequests = new ArrayList<>();

        for (NoveltyRegistUpdateIconItem item : iconItems) {
            NoveltyPresentIconRequest noveltyPresentIconRequest = new NoveltyPresentIconRequest();

            noveltyPresentIconRequest.setIconCheck(item.isIconCheck());
            noveltyPresentIconRequest.setIconSeq(item.getIconSeq());
            noveltyPresentIconRequest.setIconName(item.getIconName());

            noveltyPresentIconRequests.add(noveltyPresentIconRequest);
        }

        return noveltyPresentIconRequests;
    }

    /**
     * ノベルティプレゼント除外条件リクエストに変換
     *
     * @param exclusionNoveltyItems ノベルティプレゼント除外条件リクエスト
     * @return tノベルティプレゼント除外条件リクエストリスト
     */
    public List<NoveltyPresentExclusionNoveltyRequest> toNoveltyPresentExclusionNoveltyRequestList(List<NoveltyRegistUpdateExclusionNoveltyItem> exclusionNoveltyItems) {

        if (CollectionUtils.isEmpty(exclusionNoveltyItems)) {
            return null;
        }

        List<NoveltyPresentExclusionNoveltyRequest> noveltyPresentExclusionNoveltyRequests = new ArrayList<>();

        for (NoveltyRegistUpdateExclusionNoveltyItem item : exclusionNoveltyItems) {
            NoveltyPresentExclusionNoveltyRequest noveltyPresentExclusionNoveltyRequest =
                            new NoveltyPresentExclusionNoveltyRequest();

            noveltyPresentExclusionNoveltyRequest.setExclusionNoveltyCheck(item.isExclusionNoveltyCheck());
            noveltyPresentExclusionNoveltyRequest.setExclusionNoveltySeq(String.valueOf(item.getExclusionNoveltySeq()));
            noveltyPresentExclusionNoveltyRequest.setExclusionNoveltyName(item.getExclusionNoveltyName());

            noveltyPresentExclusionNoveltyRequests.add(noveltyPresentExclusionNoveltyRequest);
        }

        return noveltyPresentExclusionNoveltyRequests;
    }

    /**
     * アイコンチェックに変換する
     *
     * @param noveltyRegistUpdateModel ノベルティプレゼント条件登録/更新画面
     */
    public void setIconChecked(NoveltyRegistUpdateModel noveltyRegistUpdateModel) {

        for (NoveltyRegistUpdateIconItem item : noveltyRegistUpdateModel.getIconItems()) {
            item.setIconCheck(false);
        }

        List<String> checkedList = Arrays.asList(noveltyRegistUpdateModel.getIconChecked());

        if (CollectionUtils.isEmpty(checkedList)) {
            return;
        }

        for (String str : checkedList) {
            for (NoveltyRegistUpdateIconItem item : noveltyRegistUpdateModel.getIconItems()) {
                if (str.equals(item.getIconSeq())) {
                    item.setIconCheck(true);
                }
            }
        }
    }

    /**
     * ノベルティプレゼント条件の商品数確認リクエストに変換する
     *
     * @param noveltyRegistUpdateModel ノベルティプレゼント条件登録/更新画面
     * @return ノベルティプレゼント条件の商品数確認リクエスト
     */
    public NoveltyPresentConditionTargetGoodsCountGetRequest toNoveltyPresentConditionTargetGoodsCountGetRequest(
                    NoveltyRegistUpdateModel noveltyRegistUpdateModel) {
        NoveltyPresentConditionTargetGoodsCountGetRequest noveltyPresentConditionTargetGoodsCountGetRequest =
                        new NoveltyPresentConditionTargetGoodsCountGetRequest();

        List<String> listData = toList(noveltyRegistUpdateModel.getGoodsGroupCode());
        if (!listData.isEmpty()) {
            noveltyPresentConditionTargetGoodsCountGetRequest.setGoodsGroupCodeList(listData);
        }

        listData = toList(noveltyRegistUpdateModel.getGoodsCode());
        if (!listData.isEmpty()) {
            noveltyPresentConditionTargetGoodsCountGetRequest.setGoodsCodeList(listData);
        }

        listData = toList(noveltyRegistUpdateModel.getCategoryId());
        if (!listData.isEmpty()) {
            noveltyPresentConditionTargetGoodsCountGetRequest.setCategoryIdList(listData);
        }

        List<NoveltyRegistUpdateIconItem> iconItems = noveltyRegistUpdateModel.getIconItems();
        List<String> integerList = new ArrayList<String>();
        for (NoveltyRegistUpdateIconItem item : iconItems) {
            if (item.isIconCheck()) {
                integerList.add(String.valueOf(item.getIconSeq()));
            }
        }
        if (!integerList.isEmpty()) {
            noveltyPresentConditionTargetGoodsCountGetRequest.setIconSeqList(integerList);
        }

        listData = toList(noveltyRegistUpdateModel.getGoodsName());
        if (!listData.isEmpty()) {
            noveltyPresentConditionTargetGoodsCountGetRequest.setGoodsNameList(listData);
        }

        listData = toList(noveltyRegistUpdateModel.getSearchkeyword());
        if (!listData.isEmpty()) {
            noveltyPresentConditionTargetGoodsCountGetRequest.setSearchKeywordList(listData);
        }

        return noveltyPresentConditionTargetGoodsCountGetRequest;
    }

    /**
     * 画面初期描画時に任意必須項目のデフォルト値を設定<br/>
     * 新規登録/更新に関わらず、モデルに設定されていない場合はデフォルト値を設定
     *
     * @param noveltyRegistUpdateModel
     */
    public void setDefaultValueForLoad(NoveltyRegistUpdateModel noveltyRegistUpdateModel) {
        // 条件状態プルダウンのデフォルト値を設定
        if (StringUtils.isEmpty(noveltyRegistUpdateModel.getNoveltyPresentState())) {
            noveltyRegistUpdateModel.setNoveltyPresentState(HTypeNoveltyPresentState.VALID.getValue());
        }
    }

}