/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.goods.goods.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.UnzipUtility;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.goods.goods.GoodsImageZipFileUploadService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 商品画像zip一括アップロードService実装クラス
 *
 * @author Iwama (Itec) 2010/08/25 チケット #2184 対応
 * @author Kaneko (itec) 2012/08/17 UtilからHelperへ変更　Util使用箇所をgetComponentで取得するように変更
 *
 */
@Service
public class GoodsImageZipFileUploadServiceImpl extends AbstractShopService implements GoodsImageZipFileUploadService {

    /**
     * DOCUMENT ME!
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(GoodsImageZipFileUploadServiceImpl.class);

    /** DOCUMENT ME! */
    public static final String UPLOAD_PATH_GOODS = "goodsimage.input.directory";

    /** DOCUMENT ME! */
    public static final String UPLOAD_PATH_DESIGN = "goodsimage.d_images.directory";

    /**
     * 商品画像zip一括アップロード処理を実行します。
     *
     * @param uploadedFile UploadedFile
     * @param zipImageTarget アップロード先判定
     *
     * @return int
     */
    @Override
    public int execute(MultipartFile uploadedFile, String zipImageTarget) {
        // パラメータのチェック
        ArgumentCheckUtil.assertNotNull("uploadedFile", "uploadedFile");

        // 展開後のファイルリスト
        List<File> fileList = null;

        try {
            // 展開先のパスを取得
            String uploadPath = UPLOAD_PATH_GOODS;
            if (zipImageTarget == null) {
                LOGGER.warn("画面選択値のアップロード対象が取得できませんでしたので、商品画像としてアップロードします。");
            } else {
                if (zipImageTarget.equals("1")) {
                    uploadPath = UPLOAD_PATH_DESIGN;
                }
            }
            String imageFilePath = PropertiesUtil.getSystemPropertiesValue(uploadPath);

            if (StringUtils.isEmpty(imageFilePath)) {
                LOGGER.warn(uploadPath + "が取得できません。system.propertiesを確認してください。");
                return 0;
            }

            String separator = System.getProperty("file.separator");
            if (imageFilePath.lastIndexOf(separator) != (imageFilePath.length() - 1)) {
                imageFilePath = imageFilePath + separator;
            }

            // ZIP 解凍Helper取得
            UnzipUtility unzipUtility = ApplicationContextUtility.getBean(UnzipUtility.class);

            fileList = unzipUtility.unzip(uploadedFile, imageFilePath, true);
        } catch (RuntimeException e) {
            if (e instanceof IllegalArgumentException) {
                throwMessage("SGG001101");
            }

            LOGGER.warn("RuntimeExceptionが発生しました。 -- " + e);

            return 0;
        } catch (Exception e) {
            if (e.getCause() != null && e.getCause().getCause() instanceof IOException) {
                throwMessage("SGG001101");
            }

            LOGGER.warn("予期せぬ例外が発生しました。 -- " + e);

            return 0;
        }

        if (fileList == null) {
            LOGGER.warn("アップロード結果が取得できません。");

            return 0;
        }

        return fileList.size();
    }
}
