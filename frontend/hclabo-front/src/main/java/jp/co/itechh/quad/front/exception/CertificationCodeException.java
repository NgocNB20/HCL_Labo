/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.exception;

import jp.co.itechh.quad.front.base.exception.AppLevelException;
import lombok.Data;
import org.springframework.ui.Model;


/**
 * 二要素認証処理中に例外が発生した際にスローされる。
 * この例外が発生しているということは、画面遷移等はもう不可能な状態になっているので、
 *  例外ハンドラは適切な処理を行う必要がある。
 *
 * @author Hoang Khanh Nam (VTI Japan Co., Ltd.)
 */
@Data
public class CertificationCodeException extends AppLevelException {

    /** シリアル */
    private static final long serialVersionUID = 1L;

    /** Controllerから引き渡されるModel */
    private Model model;

    /**
     * コンストラクタ。
     */
    public CertificationCodeException() {
        super();
    }

    /**
     * コンストラクタ：Controllerから引き渡されるModelを保持するため。
     * 二要素認証処理時にFormバリデーションでエラーが発生した場合に利用する。
     */
    public CertificationCodeException(Model model) {
        this.model = model;
    }

    /**
     * コンストラクタ。
     *
     * @param message メッセージ
     */
    public CertificationCodeException(final String message) {
        super(message);
    }

    /**
     * コンストラクタ。
     *
     * @param cause システムがスローした例外
     */
    public CertificationCodeException(final Throwable cause) {
        super(cause);
    }

}