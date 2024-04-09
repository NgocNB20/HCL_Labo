package jp.co.itechh.quad.ddd.presentation.accesskeywords.api;

import io.swagger.annotations.ApiParam;
import jp.co.itechh.quad.accesskeywords.presentation.api.ReportsApi;
import jp.co.itechh.quad.accesskeywords.presentation.api.param.AccessKeywordListResponse;
import jp.co.itechh.quad.accesskeywords.presentation.api.param.AccessKeywordsCsvGetRequest;
import jp.co.itechh.quad.accesskeywords.presentation.api.param.AccessKeywordsGetRequest;
import jp.co.itechh.quad.accesskeywords.presentation.api.param.PageInfoRequest;
import jp.co.itechh.quad.core.dto.accesskeywords.AccessKeywordsCSVDto;
import jp.co.itechh.quad.core.dto.csv.OptionContentDto;
import jp.co.itechh.quad.core.handler.csv.CsvDownloadHandler;
import jp.co.itechh.quad.core.util.common.CsvOptionUtil;
import jp.co.itechh.quad.ddd.exception.AssertChecker;
import jp.co.itechh.quad.ddd.exception.DomainException;
import jp.co.itechh.quad.ddd.usecase.accesskeywords.AccessKeywordsUseCase;
import jp.co.itechh.quad.ddd.usecase.query.accesskeywords.AccessKeywordsQueryCondition;
import jp.co.itechh.quad.ddd.usecase.query.accesskeywords.AccessKeywordsQueryModel;
import lombok.SneakyThrows;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * 検索キーワード集計エンドポイント Controller
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@RestController
public class AccessKeywordsController implements ReportsApi {

    /**
     * ロガー
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AccessKeywordsController.class);

    /**
     * 検索キーワード集計ユースケース
     */
    private final AccessKeywordsUseCase useCase;

    /**
     * 検索キーワード集計ヘルパー
     */
    private final AccessKeywordsHelper helper;

    /**
     * コンストラクタ
     *
     * @param useCase 検索キーワード集計ユースケース
     * @param helper  検索キーワード集計ヘルパー
     */
    @Autowired
    public AccessKeywordsController(AccessKeywordsUseCase useCase, AccessKeywordsHelper helper) {
        this.useCase = useCase;
        this.helper = helper;
    }

    /**
     * GET /reports/access-keywords/csv : キーワード集計CSVDL集計
     * キーワード集計CSVDL集計
     *
     * @param accessKeywordsCsvGetRequest キーワード集計CSVDLリクエスト (optional)
     * @return 成功 (status code 200)
     * or その他エラー (status code 500)
     */
    @Override
    public ResponseEntity<Void> downloadCsv(@ApiParam("キーワード集計CSVDLリクエスト") @Valid
                                            AccessKeywordsCsvGetRequest accessKeywordsCsvGetRequest) {
        AccessKeywordsQueryCondition queryCondition = helper.toDownloadQueryCondition(accessKeywordsCsvGetRequest);

        HttpServletResponse response = ((ServletRequestAttributes) Objects.requireNonNull(
                        RequestContextHolder.getRequestAttributes())).getResponse();

        AssertChecker.assertNotNull("response is null", response);

        response.setCharacterEncoding("MS932");

        try {
            // Apache Common CSV を特化したCSVフォーマットを準備する
            // 主にHIT-MALL独自のCsvDownloadOptionDtoからCSVFormatに変換する
            Map<String, OptionContentDto> csvDownloadOption =
                            CsvOptionUtil.getDefaultMapOptionContentDto(AccessKeywordsCSVDto.class);

            // Apache Common CSV を特化したCSVプリンター（設定したCSVフォーマットに沿ってデータを出力）を初期化する
            StringWriter stringWriter = new StringWriter();
            CSVPrinter printer = new CSVPrinter(stringWriter, CSVFormat.DEFAULT);

            PrintWriter writer = response.getWriter();
            printer.printRecord(CsvDownloadHandler.outHeader(AccessKeywordsCSVDto.class, csvDownloadOption));
            writer.write(stringWriter.toString());
            stringWriter.getBuffer().setLength(0);
            writer.flush();

            try (Stream<AccessKeywordsQueryModel> queryModelStream = useCase.download(queryCondition)) {
                queryModelStream.forEach((queryModel -> {
                    try {
                        AccessKeywordsCSVDto csvDto = helper.toAccessKeywordsCSVDto(queryModel);

                        printer.printRecord(CsvDownloadHandler.outCsvRecord(csvDto, csvDownloadOption));
                        writer.write(stringWriter.toString());
                        stringWriter.getBuffer().setLength(0);
                    } catch (IOException e) {
                        LOGGER.error(e.getMessage());
                    }
                }));
                writer.flush();
            }
        } catch (Exception e) {
            LOGGER.error("例外処理が発生しました", e);
            throw new DomainException("ANALYTICS-AKQC0001-E");
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * GET /reports/access-keywords : キーワード集計
     * キーワード集計
     *
     * @param accessKeyword   キーワード集計リクエスト (optional)
     * @param pageInfoRequest ページ情報リクエスト（ページネーションのため） (optional)
     * @return 検索キーワード一覧レスポンス (status code 200)
     * or その他エラー (status code 500)
     */
    @SneakyThrows
    @Override
    public ResponseEntity<AccessKeywordListResponse> get(
                    @ApiParam("キーワード集計リクエスト") @Valid AccessKeywordsGetRequest accessKeyword,
                    @ApiParam("ページ情報リクエスト（ページネーションのため）") @Valid PageInfoRequest pageInfoRequest) {

        AccessKeywordsQueryCondition queryCondition = helper.toGetQueryCondition(accessKeyword, pageInfoRequest);

        List<AccessKeywordsQueryModel> queryModelList = useCase.get(queryCondition);

        try {
            AccessKeywordListResponse listResponse =
                            helper.toAccessKeywordListResponse(queryModelList, pageInfoRequest);
            return new ResponseEntity<>(listResponse, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("例外処理が発生しました", e);
            throw new DomainException("ANALYTICS-AKLR0001-E");
        }

    }
}