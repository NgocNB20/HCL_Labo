/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.hclabo.core.web;

import org.springframework.transaction.annotation.Transactional;

/**
 * コントローラ基底クラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 * @Transactionalでトランザクション制御
 */
@Transactional(rollbackFor = Exception.class)
public abstract class AbstractController {

}
