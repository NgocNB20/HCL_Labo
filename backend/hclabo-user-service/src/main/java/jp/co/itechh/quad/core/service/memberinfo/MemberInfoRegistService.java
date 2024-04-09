/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.memberinfo;

import jp.co.itechh.quad.core.entity.memberinfo.MemberInfoEntity;
import jp.co.itechh.quad.core.entity.memberinfo.customeraddress.CustomerAddressBookEntity;

/**
 * 会員登録サービス<br/>
 *
 * @author natume
 * @version $Revision: 1.6 $
 */
public interface MemberInfoRegistService {

    /* メッセージ */

    /**
     * 会員情報登録失敗エラー<br/>
     * <code>MSGCD_MEMBERINFO_REGIST_FAIL</code>
     */
    String MSGCD_MEMBERINFO_REGIST_FAIL = "SMM000101";

    /**
     * ・会員ID重複チェック<br/>
     * ・パスワードの暗号化<br/>
     * ・会員情報の登録を行う <br/>
     * TODO ・顧客の住所IDに紐づく住所情報の登録・更新を行う（管理サイトで住所情報を取得するための暫定対応）<br/>
     * ・メルマガ購読者の登録・更新を行う<br/>
     * ・確認メールの削除を行う<br/>
     *
     * @param accessUid                 端末識別情報
     * @param customerId                顧客ID
     * @param memberInfoEntity          会員エンティティ
     * @param mailmagazine              メルマガ希望フラグ
     * @param preMailmagazine           登録前メルマガ希望フラグ
     * @param confirmMailPassword       確認メールパスワード
     * @param customerAddressBookEntity 顧客住所エンティティ
     */
    void execute(String accessUid,
                 String customerId,
                 MemberInfoEntity memberInfoEntity,
                 boolean mailmagazine,
                 boolean preMailmagazine,
                 String confirmMailPassword,
                 CustomerAddressBookEntity customerAddressBookEntity);

    /**
     * ・会員ID重複チェック<br/>
     * ・パスワードの暗号化<br/>
     * ・会員情報の登録を行う <br/>
     * TODO ・顧客の住所IDに紐づく住所情報の登録・更新を行う（管理サイトで住所情報を取得するための暫定対応）<br/>
     * ・メルマガ購読者の登録・更新を行う<br/>
     * ・カート商品のマージを行う（cartMergeがtrueの場合） <br/>
     *
     * @param accessUid                 端末識別情報
     * @param customerId                顧客ID
     * @param memberInfoEntity          会員エンティティ
     * @param mailmagazine              メルマガ希望フラグ
     * @param preMailmagazine           登録前メルマガ希望フラグ
     * @param cartMerge                 カートマージをするかどうか。true..する / false..しない
     * @param customerAddressBookEntity 顧客住所エンティティ
     */
    void execute(String accessUid,
                 String customerId,
                 MemberInfoEntity memberInfoEntity,
                 boolean mailmagazine,
                 boolean preMailmagazine,
                 boolean cartMerge,
                 CustomerAddressBookEntity customerAddressBookEntity);
}