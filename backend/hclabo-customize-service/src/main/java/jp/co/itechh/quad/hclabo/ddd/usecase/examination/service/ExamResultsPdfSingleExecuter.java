package jp.co.itechh.quad.hclabo.ddd.usecase.examination.service;

import jp.co.itechh.quad.hclabo.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.hclabo.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.hclabo.core.base.utility.FileOperationUtility;
import jp.co.itechh.quad.hclabo.core.base.utility.UnzipUtility;
import jp.co.itechh.quad.hclabo.ddd.domain.examination.entity.ExamKitEntity;
import jp.co.itechh.quad.hclabo.ddd.domain.examination.repository.IExamKitRepository;
import jp.co.itechh.quad.hclabo.ddd.domain.order.adapter.IOrderReceivedAdapter;
import jp.co.itechh.quad.hclabo.ddd.domain.order.adapter.model.OrderReceived;
import jp.co.itechh.quad.hclabo.ddd.exception.BaseException;
import jp.co.itechh.quad.hclabo.ddd.exception.DomainException;
import jp.co.itechh.quad.hclabo.ddd.exception.ExceptionContent;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 検査結果PDFアップロード処理 .
 */
@Service
public class ExamResultsPdfSingleExecuter {

    /**
     * ロガー
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(ExamResultsPdfSingleExecuter.class);

    /**
     * ファイルのZIP拡張子
     */
    private static final String ZIP_FILE_EXTENSION = "zip";

    /**
     * ファイルのPDF拡張子
     */
    private static final String PDF_FILE_EXTENSION = "pdf";

    /**
     * 区切り文字(スラッシュ)
     */
    private static final String DELIMITER_SLASH = "/";

    /**
     * 検査キット番号の桁数
     */
    private static final int EXAMINATION_CODE_LENGTH = 20;

    /**
     * 検査結果PDFの格納場所（プロパティ）
     */
    private static final String PDF_STORAGE_PATH= "examresults.pdf.storage.path";

    /**
     * 非同期処理対象ファイル出力パス
     */
    private static final String TMP_PATH = "batch.file.path";

    /**
     * 検査結果PDFの格納場所（プロパティ）
     */
    private static String DESTINATION_STORAGE_PREFIX_PATH = "";

    /**
     * 検査キットリポジトリー
     */
    private final IExamKitRepository examKitRepository;

    /**
     * 受注アダプター
     */
    private final IOrderReceivedAdapter orderReceivedAdapter;

    /**
     * ファイル操作Helper
     */
    private final FileOperationUtility fileOperationUtility;

    /**
     * コンストラクタ
     */
    @Autowired
    public ExamResultsPdfSingleExecuter(IExamKitRepository examKitRepository,
                                        IOrderReceivedAdapter orderReceivedAdapter) {
        this.examKitRepository = examKitRepository;
        this.orderReceivedAdapter = orderReceivedAdapter;
        this.fileOperationUtility = ApplicationContextUtility.getBean(FileOperationUtility.class);
        DESTINATION_STORAGE_PREFIX_PATH = PropertiesUtil.getSystemPropertiesValue(PDF_STORAGE_PATH);
    }

    /**
     * 検査結果PDFアップロード処理
     *
     * @param filePath
     * @param messageDto
     */
    public void execute(String filePath, ExamResultsEntryMessageDto messageDto) {

        // ファイル拡張子を抽出します
        String fileExtension = (filePath.substring(filePath.lastIndexOf(".") + 1)).toLowerCase();

        if (StringUtils.isEmpty(DESTINATION_STORAGE_PREFIX_PATH)) {
            LOGGER.error(PDF_STORAGE_PATH + "が取得できません。system.propertiesを確認してください。");
            return;
        }

        if (ZIP_FILE_EXTENSION.equals(fileExtension)) {

            MultipartFile uploadedZipFile = toMultiPartFile(filePath);
            String zipFileName = uploadedZipFile.getOriginalFilename();

            String unzipTmpPath = PropertiesUtil.getSystemPropertiesValue(TMP_PATH);

            try {

                if (StringUtils.isEmpty(unzipTmpPath)) {
                    LOGGER.error(TMP_PATH + "が取得できません。system.propertiesを確認してください。");
                    return;
                }

                // ZIP 解凍Helper取得
                UnzipUtility unzipUtility = ApplicationContextUtility.getBean(UnzipUtility.class);

                List<File> unzipFileList = unzipUtility.unzip(uploadedZipFile, unzipTmpPath + DELIMITER_SLASH, true);

                fileOperationUtility.remove(filePath);

                if (CollectionUtils.isNotEmpty(unzipFileList)) {

                    for (File file : unzipFileList) {

                        try {
                            String unitFileName = file.getName();
                            String unitFileExtension = unitFileName.substring(unitFileName.lastIndexOf(".") + 1);

                            if (!PDF_FILE_EXTENSION.equals(unitFileExtension)) {
                                throw new DomainException("HCLABO-CUSTOMIZE-PDFS003-E", new String[]{unitFileName});
                            }

                            uploadProcess(file, messageDto);
                        } catch (Exception e) {
                            if (e instanceof DomainException) {
                                ((BaseException) e).getMessageMap().forEach((fieldName, exceptionContentList) -> {
                                    for (ExceptionContent exceptionContent : exceptionContentList) {
                                        toExamResultsRegisterMessageDto(exceptionContent.getMessage(), messageDto);
                                    }
                                });
                                messageDto.setErrCount(messageDto.getErrCount() + 1);
                            }
                        }
                    }

                    String unzipFolderPath = unzipTmpPath + DELIMITER_SLASH
                            + zipFileName.substring(0, zipFileName.lastIndexOf("."));

                    fileOperationUtility.removeDir(unzipFolderPath);

                } else {
                    LOGGER.error("zip ファイルが空でした。");
                }

            } catch (Exception e) {

                LOGGER.error("予期せぬ例外が発生しました。 -- " + e);
            }

        } else if (PDF_FILE_EXTENSION.equals(fileExtension)) {

            try {
                uploadProcess(new File(filePath), messageDto);
            } catch (Exception e) {
                if (e instanceof DomainException) {
                    ((BaseException) e).getMessageMap().forEach((fieldName, exceptionContentList) -> {
                        for (ExceptionContent exceptionContent : exceptionContentList) {
                            toExamResultsRegisterMessageDto(exceptionContent.getMessage(), messageDto);
                        }
                    });
                    messageDto.setErrCount(messageDto.getErrCount() + 1);
                }
            }

        } else {
            LOGGER.error("zipまたはpdfファイルをアップロードしてください。");
        }

    }

    /**
     * アップロードプロセス。
     *
     * @param pdfFile
     * @param messageDto
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void uploadProcess(File pdfFile, ExamResultsEntryMessageDto messageDto) {

        String pdfFileName = pdfFile.getName();
        int extensionIndex = pdfFileName.lastIndexOf(".");

        String examKitCode = "";
        try {
            examKitCode = pdfFileName.substring(extensionIndex - EXAMINATION_CODE_LENGTH, extensionIndex);
        } catch (Exception e) {
            throw new DomainException("HCLABO-CUSTOMIZE-PDFS001-E", new String[]{pdfFileName});
        }

        ExamKitEntity examKitEntity = examKitRepository.getByExamKitCode(examKitCode);

        if (examKitEntity == null) {
            throw new DomainException("HCLABO-CUSTOMIZE-ERS0001-E", new String[]{examKitCode});
        }

        OrderReceived orderReceived = orderReceivedAdapter.getByOrderCode(examKitEntity.getOrderCode());

        if (orderReceived == null) {
            throw new DomainException("HCLABO-CUSTOMIZE-ERS0003-E",
                    new String[]{examKitEntity.getExamKitCode().getValue(), examKitEntity.getOrderCode()});
        }

        String destinationStorage = DESTINATION_STORAGE_PREFIX_PATH + orderReceived.getCustomerId();

        if (!new File(destinationStorage).exists()) {
            new File(destinationStorage).mkdir();
        }

        try {
            fileOperationUtility.put(pdfFile.getAbsolutePath(), destinationStorage, true);
            LOGGER.info("PDFファイルは正常に移動されました：" + pdfFileName);
        } catch (IOException e) {
            throw new DomainException("HCLABO-CUSTOMIZE-PDFS002-E", new String[]{pdfFileName});
        }
        // 検査結果PDF名の更新
        examKitEntity.updatePdfsFileName(pdfFileName);

        try {
            examKitRepository.update(examKitEntity);
        } catch (Exception e) {

            throw new DomainException("HCLABO-CUSTOMIZE-ERS0005-E",
                    new String[]{examKitEntity.getExamKitCode().getValue(), e.getMessage()});
        }

        if (messageDto.getOrderReceivedList() == null) {
            messageDto.setOrderReceivedList(new ArrayList<>());
        }

        // 重複をチェックする
        Boolean isDuplicateOrderReceived = messageDto.getOrderReceivedList()
                .stream().anyMatch(item -> item.getOrderCode().equals(orderReceived.getOrderCode()));

        if (Boolean.FALSE.equals(isDuplicateOrderReceived)) {
            messageDto.getOrderReceivedList().add(orderReceived);
        }

        messageDto.setResultCount(messageDto.getResultCount() + 1);
    }

    /**
     * ファイルに変換
     *
     * @param filePath 画像アップロードリクエスト
     * @return MultipartFile アップロードされたファイル
     */
    private MultipartFile toMultiPartFile(String filePath) {
        FileOperationUtility fileOperationUtility = ApplicationContextUtility.getBean(FileOperationUtility.class);
        return fileOperationUtility.toMultiPartFile(filePath);
    }

    /**
     * 検査結果登録実行メッセージDtoに変換
     *
     * @param message メッセージコード
     * @param messageDto
     */
    private void toExamResultsRegisterMessageDto(String message, ExamResultsEntryMessageDto messageDto) {
        if (messageDto.getErrMessage() == null) {
            messageDto.setErrMessage(new StringBuilder());
        }

        if (messageDto.getErrMessage().length() > 0) {
            messageDto.getErrMessage().append("\r\n");
        }

        messageDto.getErrMessage().append(message);
    }
}
