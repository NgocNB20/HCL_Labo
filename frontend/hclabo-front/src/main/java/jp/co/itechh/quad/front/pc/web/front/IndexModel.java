package jp.co.itechh.quad.front.pc.web.front;

import jp.co.itechh.quad.front.base.util.common.CollectionUtil;
import jp.co.itechh.quad.front.web.AbstractModel;
import lombok.Data;

import java.util.List;

/**
 * トップ画面 Model
 *
 * @author kn23834
 * @version $Revision: 1.0 $
 *
 */
@Data
public class IndexModel extends AbstractModel {

    /**
     * ニュースリスト
     */
    public List<IndexModelItem> newsItems;

    /**
     *
     * ニュース存在チェック<br/>
     *
     * @return true:存在する
     */
    public boolean isNewsExists() {
        return CollectionUtil.isNotEmpty(newsItems);
    }

}