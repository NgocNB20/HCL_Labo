/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.dto.shop.questionnaire;

import jp.co.itechh.quad.admin.constant.type.HTypeReplyRequiredFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeReplyType;
import jp.co.itechh.quad.admin.constant.type.HTypeReplyValidatePattern;
import jp.co.itechh.quad.admin.entity.shop.questionnaire.QuestionEntity;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Map;

/**
 * アンケート回答画面表示用DTO<br />
 * アンケート回答画面にアンケート設問を表示するために必要なプロパティをひとまとめにしたDTO<br />
 *
 * @author DtoGenerator
 */
@Data
@Component
@Scope("prototype")
public class QuestionnaireReplyDisplayDto implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /**
     * アンケート設問エンティティ<br />
     */
    private QuestionEntity questionEntity;

    /**
     * 設問SEQ<br />
     */
    private Integer questionSeq;

    /**
     * 設問No<br />
     * 必須項目未入力エラーメッセージにて表示する番号。<br />
     */
    private Integer displayNumber;

    /**
     * 設問内容
     */
    private String question;

    /**
     * テキストボックス<br />
     */
    private String questionTextBox;

    /**
     * テキストエリア<br />
     */
    private String questionTextArea;

    /**
     * ラジオボタン・プルダウン選択肢<br />
     * 画面で入力されたチェックボックスの値が格納される。<br />
     * 画面で入力された選択肢の値が格納される。<br />
     * HTML上のid,nameはこのプロパティ名に設定しておくこと。<br />
     */
    private String questionRadio;

    /**
     * ラジオボタン・プルダウン選択肢リスト<br />
     * 選択肢にセットする値を格納するMap。<br />
     * key:value値、value:ラベル<br />
     */
    private Map<String, String> questionRadioItems;

    /**
     * （モバイル半角変換表示用）ラジオボタン・プルダウン選択肢<br />
     */
    private String questionRadioDisp;

    /**
     * ラジオボタン・プルダウン選択肢<br />
     * 画面で入力されたチェックボックスの値が格納される。<br />
     * 画面で入力された選択肢の値が格納される。<br />
     * HTML上のid,nameはこのプロパティ名に設定しておくこと。<br />
     */
    private String questionPullDown;

    /**
     * ラジオボタン・プルダウン選択肢リスト<br />
     * 選択肢にセットする値を格納するMap。<br />
     * key:value値、value:ラベル<br />
     */
    private Map<String, String> questionPullDownItems;

    /**
     * （モバイル半角変換表示用）ラジオボタン・プルダウン選択肢<br />
     */
    private String questionPullDownDisp;

    /**
     * チェックボックス選択肢リスト<br />
     * チェックボックスにセットする選択肢を格納するMap。<br />
     * key:チェックボックスvalue値、value:チェックボックスラベル<br />
     */
    private Map<String, String> questionCheckBoxItems;

    /**
     * チェックボックス選択肢<br />
     * 画面で入力されたチェックボックスの値が格納される。<br />
     * チェックボックスは複数選択可能なのでString配列となっている<br />
     * HTML上のチェックボックスのid,nameはこのプロパティ名に設定しておくこと<br />
     */
    private String[] questionCheckBox;

    /**
     * チェックボックス選択肢リスト（確認画面表示用）<br />
     * String配列であるチェックボックス選択肢を確認画面で表示するためのプロパティ<br />
     * HTML上に表示するためにプロパティ名の末尾に"Items"が必要なため作成<br />
     */
    private String[] questionCheckBoxDispItems;

    /**
     * チェックボックス選択肢（確認画面表示用）<br />
     * 表示上必要なItemsに対応したプロパティ<br />
     */
    private String questionCheckBoxDisp;

    /**
     * アンケート回答必須フラグ<br />
     */
    private HTypeReplyRequiredFlag replyRequiredFlag;

    /**
     * アンケート回答形式種別<br />
     */
    private HTypeReplyType replyType;

    /**
     * アンケート回答文字種別<br />
     */
    private HTypeReplyValidatePattern replyValidatePattern;

    /**
     * アンケート回答桁数<br />
     */
    private Integer replyMaxLength;
}