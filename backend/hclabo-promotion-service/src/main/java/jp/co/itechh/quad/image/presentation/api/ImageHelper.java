package jp.co.itechh.quad.image.presentation.api;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.FileOperationUtility;
import jp.co.itechh.quad.image.presentation.api.param.OtherImagesZipUploadRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * 画像 Helper
 *
 * @author PHAM QUANG DIEU (VJP)
 */
@Component
public class ImageHelper {

    /**
     * ファイルに変換
     *
     * @param otherImagesZipUploadRequest 画像アップロードリクエスト
     * @return MultipartFile アップロードされたファイル
     */
    public MultipartFile toMultiPartFile(OtherImagesZipUploadRequest otherImagesZipUploadRequest) {
        FileOperationUtility fileOperationUtility = ApplicationContextUtility.getBean(FileOperationUtility.class);
        return fileOperationUtility.toMultiPartFile(otherImagesZipUploadRequest.getZipImageUrl());
    }
}
