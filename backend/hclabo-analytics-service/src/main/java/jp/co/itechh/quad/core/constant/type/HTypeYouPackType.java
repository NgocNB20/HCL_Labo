package jp.co.itechh.quad.core.constant.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ゆうプリR用郵送種別
 *
 * @author Hoang Khanh Nam (VTI Japan Co., Ltd.)
 */
@Getter
@AllArgsConstructor
public enum HTypeYouPackType implements EnumType {

    /**
     * ゆうパケット
     */
    YOU_PACKET("ゆうパケット", "0"),

    /**
     * ゆうパック
     */
    YOU_PACK("ゆうパック", "1");

    /**
     * ラベル<br/>
     */
    private String label;

    /**
     * 区分値<br/>
     */
    private String value;
}
