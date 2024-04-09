/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.transaction;

import jp.co.itechh.quad.addressbook.presentation.api.param.ClientErrorResponse;
import jp.co.itechh.quad.addressbook.presentation.api.param.ErrorContent;
import jp.co.itechh.quad.core.base.util.AppLevelFacesMessageUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.ddd.exception.AssertChecker;
import jp.co.itechh.quad.ddd.exception.BaseException;
import jp.co.itechh.quad.ddd.exception.ExceptionContent;
import jp.co.itechh.quad.ddd.usecase.transaction.service.ShipmentRegistSingleExecuter;
import jp.co.itechh.quad.ddd.usecase.transaction.service.ShipmentRegisterMessageDto;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.ArrayList;
import java.util.List;

/**
 * 出荷実績登録ユースケース
 */
@Service
public class RegistShipmentResultUseCase {

    /** 出荷登録実行クラス */
    private final ShipmentRegistSingleExecuter shipmentRegister;

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(RegistShipmentResultUseCase.class);

    /** コンストラクタ */
    @Autowired
    public RegistShipmentResultUseCase(ShipmentRegistSingleExecuter shipmentRegister) {
        this.shipmentRegister = shipmentRegister;
    }

    /**
     * 取引の出荷実績を登録する
     *
     * @param registShipmentResultUseCaseParamList 出荷実績登録ユースケースパラメータリスト
     * @param administratorSeq                     運営者SEQ
     * @return List<ShipmentRegisterMessageDto>
     */
    public List<ShipmentRegisterMessageDto> registShipmentResult(List<RegistShipmentResultUseCaseParam> registShipmentResultUseCaseParamList,
                                                                 Integer administratorSeq) {

        // パラメータリストが存在しない場合
        AssertChecker.assertNotEmpty(
                        "registShipmentResultUseCaseParamList is null", registShipmentResultUseCaseParamList);

        // 返却用メッセージDtoリスト
        List<ShipmentRegisterMessageDto> messageDtoList = new ArrayList<>();

        // 成功数
        Integer resultCnt = 0;

        // 取引単位のループを行う
        for (RegistShipmentResultUseCaseParam param : registShipmentResultUseCaseParamList) {
            ShipmentRegisterMessageDto messageDto = null;
            try {
                // 実行
                messageDto = shipmentRegister.execute(param.getOrderCode(), param.getShipmentStatusConfirmCode(),
                                                      param.getCompleteShipmentDate(), administratorSeq
                                                     );
                shipmentRegister.asyncAfterProcess(messageDto);

                // エラーの場合
                if (StringUtils.isNotBlank(messageDto.getErrCode())) {
                    // メッセージ内容をセット
                    messageDto.setErrMessage(AppLevelFacesMessageUtil.getAllMessage(messageDto.getErrCode(),
                                                                                    messageDto.getArgs()
                                                                                   ).getMessage());
                    messageDtoList.add(messageDto);
                } else {
                    resultCnt++;
                }
            } catch (HttpClientErrorException | HttpServerErrorException e) {
                LOGGER.error("例外処理が発生しました", e);
                // 出荷処理実行時に、外部APIでエラーが発生した場合はこちらでキャッチ
                ConversionUtility conversionUtility = ApplicationContextUtility.getBean(ConversionUtility.class);
                ClientErrorResponse clientError =
                                conversionUtility.toObject(e.getResponseBodyAsString(), ClientErrorResponse.class);
                if (clientError == null || clientError.getMessages() == null) {
                    LOGGER.error("出荷登録に失敗しました。詳細：", e);
                } else {
                    clientError.getMessages().forEach((fieldName, errorContentList) -> {
                        for (ErrorContent errorContent : errorContentList) {
                            ShipmentRegisterMessageDto messageDtoForError = new ShipmentRegisterMessageDto();
                            messageDtoForError.setErrCode(errorContent.getCode());
                            messageDtoForError.setErrMessage("[受注番号：" + param.getOrderCode() + "]の出荷登録に失敗しました。 詳細："
                                                             + errorContent.getMessage());
                            messageDtoList.add(messageDtoForError);
                        }
                    });
                }

            } catch (BaseException e) {
                LOGGER.error("例外処理が発生しました", e);
                // 出荷処理実行時に、BaseExceptionを継承したException（業務エラーなど）が発生した場合はこちらでキャッチ
                e.getMessageMap().forEach((fieldName, exceptionContentList) -> {
                    for (ExceptionContent exceptionContent : exceptionContentList) {
                        ShipmentRegisterMessageDto messageDtoForError = new ShipmentRegisterMessageDto();
                        messageDtoForError.setErrCode(exceptionContent.getCode());
                        messageDtoForError.setErrMessage("[受注番号：" + param.getOrderCode() + "]の出荷登録に失敗しました。 詳細："
                                                         + exceptionContent.getMessage());
                        messageDtoList.add(messageDtoForError);
                    }
                });
            } catch (Exception e) {
                LOGGER.error("例外処理が発生しました", e);
                // 出荷処理実行時に、想定外エラーが発生した場合はこちらでキャッチ
                ShipmentRegisterMessageDto messageDtoForError = new ShipmentRegisterMessageDto();
                messageDtoForError.setErrCode("ORDER-SHIPPING07-E");
                messageDtoForError.setArgs(new String[] {param.getOrderCode()});
                messageDtoForError.setErrMessage(AppLevelFacesMessageUtil.getAllMessage(messageDtoForError.getErrCode(),
                                                                                        messageDtoForError.getArgs()
                                                                                       ).getMessage());
                messageDtoList.add(messageDtoForError);
            }
        }

        if (resultCnt > 0) {
            // 成功メッセージをセット
            ShipmentRegisterMessageDto messageDtoForResult = new ShipmentRegisterMessageDto();
            messageDtoForResult.setErrCode("ORDER-SHIPPING01-I");
            messageDtoForResult.setArgs(new String[] {resultCnt.toString()});
            messageDtoForResult.setErrMessage(AppLevelFacesMessageUtil.getAllMessage(messageDtoForResult.getErrCode(),
                                                                                     messageDtoForResult.getArgs()
                                                                                    ).getMessage());

            messageDtoList.add(0, messageDtoForResult);
        }

        return messageDtoList;
    }

}