package jp.co.itechh.quad.notification.presentation.api.processor;

import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.constant.type.HTypeBatchName;
import jp.co.itechh.quad.core.constant.type.HTypeMailTemplateType;
import jp.co.itechh.quad.core.dto.mail.AttachFileDto;
import jp.co.itechh.quad.core.dto.mail.MailDto;
import jp.co.itechh.quad.core.service.mail.MailSendService;
import jp.co.itechh.quad.notificationsub.presentation.api.param.GoodsImageUpdateRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.GoodsImageUpdateResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品グループ規格画像更新 Consumer
 *
 * @author Doan Thang (VJP)
 */
@Component
@Scope("prototype")
public class GoodsImageUpdateProcessor {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(GoodsImageUpdateProcessor.class);

    /** カンマ区切り */
    private static final String COMMA_DELIMITER = ",";

    /**
     * メール送信
     *
     * @param goodsImageUpdateRequest 商品グループ規格画像更新通知リクエスト
     */
    public void processor(GoodsImageUpdateRequest goodsImageUpdateRequest) {
        // ショップSEQ
        Integer shopSeq = 1001;

        GoodsImageUpdateResultData resultDataRequest = goodsImageUpdateRequest.getResultData();

        try {
            MailDto mailDto = ApplicationContextUtility.getBean(MailDto.class);

            Map<String, Object> mailContentsMap = new HashMap<>();

            // システム名を取得
            String systemName = PropertiesUtil.getSystemPropertiesValue("system.name");

            final Map<String, String> valueMap = putMapValue(resultDataRequest, systemName, Integer.toString(shopSeq));

            // 本文
            mailContentsMap.put("admin", valueMap);

            String getWarningflagStr = valueMap.get("WARNING_FLG") != null ? valueMap.get("WARNING_FLG") : "";
            // 件名
            mailDto.setSubject("【" + systemName + "】" + HTypeBatchName.BATCH_GOODSIMAGE_UPDATE.getLabel() + "報告"
                               + getWarningflagStr);

            mailDto.setMailTemplateType(HTypeMailTemplateType.GOODSIMAGE_UPDATE_MAIL);
            mailDto.setFrom(PropertiesUtil.getSystemPropertiesValue("mail.setting.goodsimage.upload.mail.from"));
            mailDto.setToList(Arrays.asList(PropertiesUtil.getSystemPropertiesValue(
                            "mail.setting.goodsimage.upload.mail.receivers").split(COMMA_DELIMITER)));

            mailDto.setMailContentsMap(mailContentsMap);

            // 添付ファイル
            LOGGER.info("結果ファイルを添付");
            mailDto.setAttachFileFlag(true);
            List<AttachFileDto> attachFileList = new ArrayList<>();

            goodsImageUpdateRequest.getFileList().forEach(file -> {
                AttachFileDto attachFileDto = new AttachFileDto();
                attachFileDto.setAttachFileName(file.getFileName());
                attachFileDto.setAttachFilePath(file.getFilePath());
                attachFileList.add(attachFileDto);
            });

            // 添付ファイル一覧を設定
            mailDto.setAttachFileList(attachFileList);

            LOGGER.info("CSVファイルが添付されたメールを送信します。");
            LOGGER.info("送信先：" + Collections.singletonList(
                            PropertiesUtil.getSystemPropertiesValue("mail.setting.goodsimage.upload.mail.receivers")));

            MailSendService mailSendService = ApplicationContextUtility.getBean(MailSendService.class);

            mailSendService.execute(mailDto);
            LOGGER.info("管理者へ通知メールを送信しました。");

        } catch (Exception e) {
            LOGGER.warn("管理者への通知メール送信に失敗しました。", e);
            throw e;
        }
    }

    /**
     * メール本文作成用マップ
     *
     * @param resultDataRequest result of data request
     * @param systemName        システム名
     * @param shopSeq           ショップSEQ
     */
    private Map<String, String> putMapValue(GoodsImageUpdateResultData resultDataRequest,
                                            String systemName,
                                            String shopSeq) {

        Map<String, String> valueMap = new HashMap<>();

        Integer cntGoodsGroupSumMapSize = resultDataRequest.getCntGoodsGroupSumMapSize();
        Integer cntGoodsGroupNotExistMapSize = resultDataRequest.getCntGoodsGroupNotExistMapSize();
        Integer cntGoodsGroupFileNameNotMatchMapSize = resultDataRequest.getCntGoodsGroupFileNameNotMatchMapSize();
        Integer cntGoodsGroupOpenStateDelMapSize = resultDataRequest.getCntGoodsGroupOpenStateDelMapSize();
        Integer cntGoodsGroupRegistMapSize = resultDataRequest.getCntGoodsGroupRegistMapSize();
        Integer cntGoodsGroupDeleteMapSize = resultDataRequest.getCntGoodsGroupDeleteMapSize();
        Integer cntGoodsGroupUpdateMapSize = resultDataRequest.getCntGoodsGroupUpdateMapSize();

        Integer cntGoodsGroupImageSum = resultDataRequest.getCntGoodsGroupImageSum();
        Integer cntGoodsGroupImageNotExist = resultDataRequest.getCntGoodsGroupImageNotExist();
        Integer cntGoodsGroupImageFileNameNotMatch = resultDataRequest.getCntGoodsGroupImageFileNameNotMatch();
        Integer cntGoodsGroupImageOpenStateDel = resultDataRequest.getCntGoodsGroupImageOpenStateDel();
        Integer cntGoodsGroupImageRegist = resultDataRequest.getCntGoodsGroupImageRegist();
        Integer cntGoodsGroupImageDelete = resultDataRequest.getCntGoodsGroupImageDelete();
        Integer cntGoodsGroupImageUpdate = resultDataRequest.getCntGoodsGroupImageUpdate();

        int goodsGroupImageSuccessCnt = cntGoodsGroupImageRegist + cntGoodsGroupImageDelete + cntGoodsGroupImageUpdate;
        int goodsGroupImageErrorCnt = cntGoodsGroupImageNotExist + cntGoodsGroupImageFileNameNotMatch
                                      + cntGoodsGroupImageOpenStateDel;

        String warningFlag = resultDataRequest.getWarningFlag();
        String warningInfo = resultDataRequest.getWarningInfo();
        String list = resultDataRequest.getList();

        valueMap.put("SYSTEM", systemName);
        valueMap.put("BATCH_NAME", HTypeBatchName.BATCH_GOODSIMAGE_UPDATE.getLabel());
        valueMap.put("SHOP_SEQ", shopSeq);
        valueMap.put("GOODS_IMAGE_PATH", resultDataRequest.getImageDir());

        // 処理結果件数 商品管理番号毎
        valueMap.put("CNT_GOODSGROUP_SUM", Integer.toString(cntGoodsGroupSumMapSize));
        valueMap.put("CNT_GOODSGROUP_NOTEXIST", Integer.toString(cntGoodsGroupNotExistMapSize));
        valueMap.put("CNT_GOODSGROUP_FILENAME_NOTMATCH", Integer.toString(cntGoodsGroupFileNameNotMatchMapSize));
        valueMap.put("CNT_GOODSGROUP_OPENSTATEDEL", Integer.toString(cntGoodsGroupOpenStateDelMapSize));
        valueMap.put("CNT_GOODSGROUP_REGIST", Integer.toString(cntGoodsGroupRegistMapSize));
        valueMap.put("CNT_GOODSGROUP_DELETE", Integer.toString(cntGoodsGroupDeleteMapSize));
        valueMap.put("CNT_GOODSGROUP_UPDATE", Integer.toString(cntGoodsGroupUpdateMapSize));

        // 処理結果件数 画像毎
        // 商品グループ画像
        valueMap.put("CNT_GOODSGROUPIMAGE_SUM", Integer.toString(cntGoodsGroupImageSum));
        valueMap.put("CNT_GOODSGROUPIMAGE_SUCCESS_SUM", Integer.toString(goodsGroupImageSuccessCnt));
        valueMap.put("CNT_GOODSGROUPIMAGE_ERROR_SUM", Integer.toString(goodsGroupImageErrorCnt));
        valueMap.put("CNT_GOODSGROUPIMAGE_NOTEXIST", Integer.toString(cntGoodsGroupImageNotExist));
        valueMap.put("CNT_GOODSGROUPIMAGE_FILENAME_NOTMATCH", Integer.toString(cntGoodsGroupImageFileNameNotMatch));
        valueMap.put("CNT_GOODSGROUPIMAGE_OPENSTATEDEL", Integer.toString(cntGoodsGroupImageOpenStateDel));
        valueMap.put("CNT_GOODSGROUPIMAGE_REGIST", Integer.toString(cntGoodsGroupImageRegist));
        valueMap.put("CNT_GOODSGROUPIMAGE_DELETE", Integer.toString(cntGoodsGroupImageDelete));
        valueMap.put("CNT_GOODSGROUPIMAGE_UPDATE", Integer.toString(cntGoodsGroupImageUpdate));

        // 画像移動に失敗がある場合
        valueMap.put("WARNING_FLG", warningFlag);
        valueMap.put("WARNING_INFO", warningInfo);

        // 画像コピー結果
        valueMap.put("LIST", list);

        return valueMap;
    }

}