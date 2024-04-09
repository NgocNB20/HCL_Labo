package jp.co.itechh.quad.front.pc.web.front;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 *
 * トップ画面 ModelItem
 *
 * @author kn23834
 * @version $Revision: 1.0 $
 *
 */
@Data
@Component
@Scope("prototype")
public class IndexModelItem implements Serializable {

    /**
     * serialVersionUID<br/>
     */
    private static final long serialVersionUID = 1L;

    // ニュース
    /** ニュースSEQ */
    private String nseq;
    /** ニュースタイトルPC用 */
    private String newsTitlePC;
    /** ニュースリンク先URLPC用 */
    private String newsUrlPC;
    /** ニュース本文PC用 */
    private String newsBodyPC;
    /** ニュース詳細PC用 */
    private String newsNotePC;
    /** ニュース日付 */
    private Timestamp newsTime;

    /**
     *
     * ニュースリンクPCチェック(外部リンク)<br/>
     *
     * @return true:リンクあり
     */
    public boolean isNewsLinkPC() {
        return StringUtils.isNotEmpty(newsUrlPC);
    }

    /**
     *
     * ニュースリンクPCチェック(ニュース詳細画面)<br/>
     *
     * @return true:リンクあり
     */
    public boolean isNewsDetailsLinkPC() {
        return StringUtils.isNotEmpty(newsNotePC);
    }

    /**
     *
     * ニュース本文PC存在チェック<br/>
     *
     * @return true:本文あり
     */
    public boolean isNewsBodyPCExists() {
        return StringUtils.isNotEmpty(newsBodyPC);
    }

}