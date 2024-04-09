package jp.co.itechh.quad.image.presentation.api;

import io.swagger.annotations.ApiParam;
import jp.co.itechh.quad.core.service.goods.goods.GoodsImageZipFileUploadService;
import jp.co.itechh.quad.core.web.AbstractController;
import jp.co.itechh.quad.image.presentation.api.param.OtherImagesZipUploadRequest;
import jp.co.itechh.quad.image.presentation.api.param.OtherImagesZipUploadResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

/**
 * 画像 Controller
 *
 * @author PHAM QUANG DIEU (VJP)
 */
@RestController
public class ImageController extends AbstractController implements ContentManagementApi {

    /**
     * 商品画像zip一括アップロードサービス<br/>
     */
    private final GoodsImageZipFileUploadService goodsImageZipFileUploadService;

    /**
     * 画像 Helper
     */
    private final ImageHelper imageHelper;

    /**
     * コンストラクタ
     *
     * @param goodsImageZipFileUploadService 商品画像zip一括アップロードサービス
     * @param imageHelper 画像 Helper
     */
    public ImageController(GoodsImageZipFileUploadService goodsImageZipFileUploadService, ImageHelper imageHelper) {
        this.goodsImageZipFileUploadService = goodsImageZipFileUploadService;
        this.imageHelper = imageHelper;
    }

    /**
     * POST /content-management/other-images/zip : その他画像zip一括アップロード
     * その他画像zip一括アップロード
     *
     * @param otherImagesZipUploadRequest 画像アップロードリクエスト (required)
     * @return 画像アップロードレスポンス (status code 200)
     *         or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<OtherImagesZipUploadResponse> registZip(
                    @ApiParam(value = "画像アップロードリクエスト", required = true) @Valid @RequestBody
                                    OtherImagesZipUploadRequest otherImagesZipUploadRequest) {

        MultipartFile multipartFile = imageHelper.toMultiPartFile(otherImagesZipUploadRequest);

        int fileListSize = goodsImageZipFileUploadService.execute(multipartFile,
                                                                  otherImagesZipUploadRequest.getZipImageTarget()
                                                                 );

        OtherImagesZipUploadResponse otherImagesZipUploadResponse = new OtherImagesZipUploadResponse();
        otherImagesZipUploadResponse.setFileListSize(fileListSize);
        return new ResponseEntity<>(otherImagesZipUploadResponse, HttpStatus.OK);
    }

}