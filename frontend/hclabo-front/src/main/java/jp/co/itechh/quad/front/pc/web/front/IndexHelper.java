package jp.co.itechh.quad.front.pc.web.front;

import jp.co.itechh.quad.front.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.front.base.utility.ConversionUtility;
import jp.co.itechh.quad.front.constant.type.HTypeOpenStatus;
import jp.co.itechh.quad.front.entity.shop.news.NewsEntity;
import jp.co.itechh.quad.front.util.common.EnumTypeUtil;
import jp.co.itechh.quad.news.presentation.api.param.NewsListResponse;
import jp.co.itechh.quad.news.presentation.api.param.NewsResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * トップ画面 Helper
 *
 * @author kn23834
 * @version $Revision: 1.0 $
 *
 */
@Component
public class IndexHelper {

    /** 変換Helper */
    private final ConversionUtility conversionUtility;

    /**
     * コンストラクター
     *
     * @param conversionUtility 変換ユーティリティクラス
     */
    public IndexHelper(ConversionUtility conversionUtility) {
        this.conversionUtility = conversionUtility;
    }

    /**
     * Modelへの変換処理
     *
     * @param newsEntityList ニュースエンティティリスト
     * @param indexModel トップ画面Model
     */
    public void toPageForLoadNews(List<NewsEntity> newsEntityList, IndexModel indexModel) {
        if (newsEntityList != null) {
            List<IndexModelItem> itemsList = new ArrayList<>();

            for (NewsEntity newsEntity : newsEntityList) {

                IndexModelItem indexModelItem = ApplicationContextUtility.getBean(IndexModelItem.class);

                if (newsEntity.getNewsSeq() != null) {
                    indexModelItem.setNseq(newsEntity.getNewsSeq().toString());
                }
                indexModelItem.setNewsTime(newsEntity.getNewsTime());
                indexModelItem.setNewsTitlePC(newsEntity.getTitlePC());
                indexModelItem.setNewsUrlPC(newsEntity.getNewsUrlPC());
                indexModelItem.setNewsBodyPC(newsEntity.getNewsBodyPC());
                indexModelItem.setNewsNotePC(newsEntity.getNewsNotePC());

                itemsList.add(indexModelItem);
            }
            indexModel.newsItems = itemsList;
        }
    }

    /**
     * Listニュースクラス
     *
     * @param newsListResponse ニュース一覧レスポンス
     * @return ニュースエンティティ
     */
    public List<NewsEntity> toListNewsEntity(NewsListResponse newsListResponse) {
        // 処理前は存在しないためnullを返す
        if (ObjectUtils.isEmpty(newsListResponse) || newsListResponse.getNewsList() == null) {
            return null;
        }

        List<NewsEntity> list = new ArrayList<>();
        List<NewsResponse> newsList = newsListResponse.getNewsList();

        newsList.forEach(newsResponse -> {
            NewsEntity newsEntity = new NewsEntity();

            newsEntity.setNewsSeq(newsResponse.getNewsSeq());
            newsEntity.setShopSeq(1001);
            newsEntity.setTitlePC(newsResponse.getTitle());
            newsEntity.setNewsBodyPC(newsResponse.getNewsBody());
            newsEntity.setNewsUrlPC(newsResponse.getNewsUrl());
            if (newsResponse.getNewsOpenStatus() != null) {
                newsEntity.setNewsOpenStatusPC(
                                EnumTypeUtil.getEnumFromValue(HTypeOpenStatus.class, newsResponse.getNewsOpenStatus()));
            }
            newsEntity.setNewsOpenStartTimePC(conversionUtility.toTimestamp(newsResponse.getNewsOpenStartTime()));
            newsEntity.setNewsOpenEndTimePC(conversionUtility.toTimestamp(newsResponse.getNewsOpenEndTime()));
            newsEntity.setNewsTime(conversionUtility.toTimestamp(newsResponse.getNewsTime()));
            newsEntity.setRegistTime(conversionUtility.toTimestamp(newsResponse.getRegistTime()));
            newsEntity.setUpdateTime(conversionUtility.toTimestamp(newsResponse.getUpdateTime()));
            newsEntity.setNewsNotePC(newsResponse.getNewsNote());

            list.add(newsEntity);
        });
        return list;
    }

}