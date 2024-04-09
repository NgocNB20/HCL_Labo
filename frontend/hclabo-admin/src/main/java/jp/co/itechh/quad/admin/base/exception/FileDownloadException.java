/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.base.exception;

import lombok.Data;
import org.springframework.ui.Model;

/**
 * ファイルダウンロード中に例外が発生した際にスローされる。<br />
 * この例外が発生しているということは、画面遷移等はもう不可能な状態になっているので、
 * 例外ハンドラは適切な処理を行う必要がある。
 *
 * @author Phan Tien VU (VTI Japan Co., Ltd.)
 *
 */
@Data
public class FileDownloadException extends AppLevelException {

    /** シリアル */
    private static final long serialVersionUID = 1L;

    /** Controllerから引き渡されるModel */
    private Model model;

    /**
     * コンストラクタ。
     */
    public FileDownloadException() {
        super();
    }

    /**
     * コンストラクタ：Controllerから引き渡されるModelを保持するため。
     * CSV非同期ダウンロード時にFormバリデーションでエラーが発生した場合に利用する。
     */
    public FileDownloadException(Model model) {
        this.model = model;
    }

    /**
     * コンストラクタ。
     *
     * @param message メッセージ
     */
    public FileDownloadException(final String message) {
        super(message);
    }

    /**
     * コンストラクタ。
     *
     * @param cause システムがスローした例外
     */
    public FileDownloadException(final Throwable cause) {
        super(cause);
    }

}