package jp.co.itechh.quad.core.service.mail;

import jp.co.itechh.quad.core.dto.mail.MailDto;

import java.util.List;

/**
 * 機能概要：新メール送信サービス（同期送信）
 * 作成日：2021/03/30
 *
 * @author Phan Tien VU (VTI Japan Co., Ltd.)
 */
public interface MailSendService {

    /**
     * 単一MailDtoを依頼する場合
     *
     * @param mailDto
     * @return
     */
    boolean execute(MailDto mailDto);

    /**
     * 複数MailDtoを依頼する場合
     *
     * @param mailDto
     * @return
     */
    boolean execute(List<MailDto> mailDtoList);

}
