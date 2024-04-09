/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.dto.mail;

import jp.co.itechh.quad.admin.base.util.common.CollectionUtil;
import jp.co.itechh.quad.admin.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.admin.constant.type.HTypeMailTemplateType;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 機能概要：新メール送信サービス
 * 作成日：2021/03/30
 *
 * @author Phan Tien VU (VTI Japan Co., Ltd.)
 */
@Data
@Component
@Scope("prototype")
public class MailDto implements Serializable {

    /** シリアル */
    private static final long serialVersionUID = 1L;
    /** メールテンプレートタイプ */
    private HTypeMailTemplateType mailTemplateType;
    /** 件名 */
    private String subject;
    /** FROM 送信元 */
    private String from;
    /** TO 宛先 */
    private List<String> toList;
    /** CC 宛先 */
    private List<String> ccList;
    /** BCC 宛先 メールテンプレートのデフォルト BCC を使用する場合は設定しない */
    private List<String> bccList;
    /** プレースホルダ情報に使用される変数マップ */
    private Map<String, Object> mailContentsMap;
    /** 添付ファイル有無フラグ */
    private Boolean attachFileFlag = false;
    /** 添付ファイル一覧 */
    private List<AttachFileDto> attachFileList;

    /**
     * 値設定なしのコンストラクタ
     */
    public MailDto() {
        super();
    }

    /**
     * 推奨コンストラクタ
     *
     * @param mailTemplateType
     * @param from
     * @param toList
     * @param ccList
     * @param bccList
     * @param mailContentsMap
     * @param attachFileList
     */
    public MailDto(HTypeMailTemplateType mailTemplateType,
                   String from,
                   List<String> toList,
                   List<String> ccList,
                   List<String> bccList,
                   Map<String, Object> mailContentsMap,
                   List<AttachFileDto> attachFileList) {
        this.mailTemplateType = mailTemplateType;
        this.subject = mailTemplateType.getLabel();
        this.from = from;
        this.toList = toList;
        this.ccList = ccList;
        this.bccList = bccList;
        this.mailContentsMap = mailContentsMap;
        if (!CollectionUtil.isEmpty(attachFileList)) {
            this.attachFileList = new ArrayList<>();
            this.attachFileList.addAll(attachFileList);
            this.attachFileFlag = true;
        }
    }

    /**
     * インスタンス生成後に必要な情報で初期化
     *
     * @param mailTemplateType
     * @param subject
     * @param from
     * @param toList
     * @param ccList
     * @param bccList
     * @param mailContentsMap
     * @param attachFileList
     */
    public void initializeMailDto(HTypeMailTemplateType mailTemplateType,
                                  String subject,
                                  String from,
                                  List<String> toList,
                                  List<String> ccList,
                                  List<String> bccList,
                                  Map<String, Object> mailContentsMap,
                                  List<AttachFileDto> attachFileList) {
        this.mailTemplateType = mailTemplateType;
        this.subject = subject;
        this.from = from;
        this.toList = toList;
        this.ccList = ccList;
        this.bccList = bccList;
        this.mailContentsMap = mailContentsMap;
        if (!CollectionUtil.isEmpty(attachFileList)) {
            this.attachFileList = new ArrayList<>();
            this.attachFileList.addAll(attachFileList);
            this.attachFileFlag = true;
        }
    }

}