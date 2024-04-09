/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.memberinfo;

import jp.co.itechh.quad.admin.constant.type.HTypeMemberInfoStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeSexUnnecessaryAnswer;
import jp.co.itechh.quad.admin.entity.memberinfo.MemberInfoEntity;
import jp.co.itechh.quad.admin.web.AbstractModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Timestamp;
import java.util.List;

/**
 * 会員詳細モデル
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class MemberInfoDetailsModel extends AbstractModel {

    /**
     * 検索条件保持判定用<br />
     * 遷移元パッケージを格納<br />
     * チケット対応#2743対応
     */
    private String from;

    /**
     * 検索条件保持判定用<br />
     * 「戻る」ボタンに使用<br />
     * チケット対応#2743対応
     */
    private String md = "list";

    /**
     * ユーザレビューSEQ
     */
    private Integer userReviewSeq;

    /**
     * 受注番号
     * 受注詳細からの遷移時に「戻る」ボタンに使用
     */
    private String orderCode;

    /************************************
     ** 会員状態情報
     ************************************/
    /**
     * 問い合わせ検索用会員ID(メールアドレス)
     */
    private String resultInquiryMemberInfoMail;

    /**
     * 会員SEQ（アカウントロック解除用）<br />
     */
    private Integer unlockTargetSeq;

    /**
     * 会員情報エンティティ<br />
     */
    private MemberInfoEntity memberInfoEntity;

    /**
     * 会員状態<br/>
     */
    private HTypeMemberInfoStatus memberInfoStatus;

    /**
     * 入会日<br/>
     */
    private String admissionYmd;

    /**
     * 退会日<br/>
     */
    private String secessionYmd;

    /**
     * 登録日時<br/>
     */
    private Timestamp registTime;

    /**
     * 更新日時<br/>
     */
    private Timestamp updateTime;

    /**
     * アカウントロック状態<br/>
     * true..ロックされている
     */
    private boolean accountLock;

    /**
     * アカウントロック日時
     */
    private Timestamp accountLockTime;

    /**
     * ログイン失敗回数
     */
    private Integer loginFailureCount;

    /************************************
     ** 最終ログイン情報
     ************************************/
    /**
     * 最終ログイン日時
     */
    private String lastLoginTime;

    /**
     * 最終ログインユーザーエージェント
     */
    private String lastLoginUserAgent;

    /************************************
     ** お客様情報
     ************************************/
    /**
     * 会員SEQ<br />
     */
    private Integer memberInfoSeq;

    /**
     * 会員ID<br/>
     */
    private String memberInfoId;

    /**
     * 氏名<br/>
     */
    private String memberInfoName;

    /**
     * フリガナ<br/>
     */
    private String memberInfoKana;

    /**
     * 性別<br/>
     */
    private HTypeSexUnnecessaryAnswer memberInfoSex;

    /**
     * 生年月日<br/>
     */
    private Timestamp memberInfoBirthday;

    /**
     * Tel<br/>
     */
    private String memberInfoTel;

    /**
     * 郵便番号<br/>
     */
    private String memberInfoZipCode;

    /**
     * 住所<br/>
     */
    private String memberInfoAddress;

    /**
     * 問い合わせフラグ<br/>
     * 問い合わせ検索画面リンクの表示・非表示に使用
     */
    private boolean inquiryFlag;

    /************************************
     ** メルマガ
     ************************************/

    /**
     * メルマガ購読フラグ
     */
    private boolean mailMagazine;

    /************************************
     ** 注文履歴
     ************************************/
    /**
     * 注文履歴アイテムリスト<br/>
     */
    private List<MemberInfoDetailsOrderItem> orderHistoryItems;

    /** ページング用 */
    /**
     * ページ番号
     */
    private String pageNumber;

    /**
     * 最大表示件数
     */
    private int limit;

    /**
     * ソート項目
     */
    private String orderField;

    /**
     * ソート条件
     */
    private boolean orderAsc;

    /**
     * 検索結果総件数
     */
    private int totalCount;

    /**
     * 注文履歴の有無
     *
     * @return true...有、false...無
     */
    public boolean isOrderHistoryExist() {
        return orderHistoryItems != null && !orderHistoryItems.isEmpty();
    }

    /**
     * 入会かどうか判定<br/>
     * 新規受注ボタンの表示判定に使用<br/>
     *
     * @return true...入会、false...入会以外
     */
    public boolean isAdmission() {
        return HTypeMemberInfoStatus.ADMISSION.equals(memberInfoStatus);
    }

    /**
     * 本会員か暫定会員を判定<br/>
     * ユニークIDが存在しない　かつ　入会済みの場合は暫定会員<br/>
     *
     * @return true...暫定会員、false...本会員
     */
    public boolean isTemporaryMember() {
        return memberInfoEntity.getMemberInfoUniqueId() == null && isAdmission();
    }
}

