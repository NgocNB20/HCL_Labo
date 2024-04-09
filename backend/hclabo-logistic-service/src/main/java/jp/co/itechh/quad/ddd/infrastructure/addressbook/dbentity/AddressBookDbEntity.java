package jp.co.itechh.quad.ddd.infrastructure.addressbook.dbentity;

import lombok.Data;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

/**
 * 住所録DBエンティティ
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Entity
@Table(name = "AddressBook")
@Data
@Component
@Scope("prototype")
public class AddressBookDbEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** 住所ID（必須） */
    @Id
    private String addressId;

    /** 顧客ID */
    private String customerId;

    /** 住所名 */
    private String addressName;

    /** 氏名(姓) */
    private String lastName;

    /** 氏名(名) */
    private String firstName;

    /** フリガナ(姓) */
    private String lastKana;

    /** フリガナ(名) */
    private String firstKana;

    /** 電話番号 */
    private String tel;

    /** 郵便番号 */
    private String zipCode;

    /** 都道府県 */
    private String prefecture;

    /** 住所1 */
    private String address1;

    /** 住所2 */
    private String address2;

    /** 住所3 */
    private String address3;

    /** 配送メモ */
    private String shippingMemo;

    /** 登録日時 */
    private Date registDate;

    /** 非表示フラグ */
    private boolean hideFlag;

    /** デフォルト指定フラグ（必須） */
    private boolean defaultFlag = false;

}
