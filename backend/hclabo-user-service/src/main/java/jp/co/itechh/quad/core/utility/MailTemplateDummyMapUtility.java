package jp.co.itechh.quad.core.utility;

import jp.co.itechh.quad.core.base.util.common.PropertiesControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

@Component
public class MailTemplateDummyMapUtility {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(MailTemplateDummyMapUtility.class);

    /** リソース格納ディレクトリ */
    protected static final String RESOURCE_DIR = "config/hitmall/mailTemplateDummies/";

    /**
     * コンストラクタ<br/>
     */
    public MailTemplateDummyMapUtility() {
    }

    /**
     * 設定ファイルよりメールテンプレート用ダミーマップを取得する
     * @param resourceName ダミーマップを取得したい TransformHelper 名
     * @return 設定ファイルより作成したダミーマップ
     */
    public Map<String, String> getDummyValueMap(String resourceName) {

        ResourceBundle bundle;

        try {

            bundle = ResourceBundle.getBundle(RESOURCE_DIR + resourceName, new PropertiesControl(5000L));

        } catch (MissingResourceException e) {
            LOGGER.error("ダミー値マップ用リソースが見つかりません。");
            throw e;
        }

        Map<String, String> tmpMap = new HashMap<>();

        for (Enumeration<String> enu = bundle.getKeys(); enu.hasMoreElements(); ) {
            String key = enu.nextElement();
            String val = bundle.getString(key);
            tmpMap.put(key, val);
        }

        // unmodifiable にする必要が無いため、変更可能な Map をそのまま返す
        return tmpMap;
    }
}