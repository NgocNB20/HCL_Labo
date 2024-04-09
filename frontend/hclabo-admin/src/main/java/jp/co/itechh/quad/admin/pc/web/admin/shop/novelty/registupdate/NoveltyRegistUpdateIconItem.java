package jp.co.itechh.quad.admin.pc.web.admin.shop.novelty.registupdate;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@Component
@Scope("prototype")
public class NoveltyRegistUpdateIconItem implements Serializable {

    /**
     * シリアルバージョンID
     */
    private static final long serialVersionUID = 1L;

    /**
     * アイコンチェック
     */
    private boolean iconCheck;

    /**
     * アイコンSEQ
     */
    private String iconSeq;

    /**
     * アイコン名
     */
    private String iconName;

}
