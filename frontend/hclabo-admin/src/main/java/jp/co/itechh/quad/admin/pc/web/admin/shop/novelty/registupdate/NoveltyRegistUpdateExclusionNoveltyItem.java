package jp.co.itechh.quad.admin.pc.web.admin.shop.novelty.registupdate;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 *
 */
@Data
@Component
@Scope("prototype")
public class NoveltyRegistUpdateExclusionNoveltyItem implements Serializable {

    /**
     * シリアルバージョンID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 除外条件チェック
     */
    public boolean exclusionNoveltyCheck;

    /**
     * 除外条件SEQ
     */
    public Integer exclusionNoveltySeq;

    /**
     * 除外条件名
     */
    public String exclusionNoveltyName;

}
