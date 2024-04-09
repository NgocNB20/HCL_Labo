function execAjaxSymptomsCategory(appendId){

    // カテゴリーID
    var cc = $(appendId).attr('cc');
    // ソート条件（normal：標準、new：新着順、price：価格順、salableness：売れ筋順）
    var seq = "normal";
    // 取得件数（未設定：無制限）
    var limit = "";
    // 価格from
    var priceFrom = "";
    // 価格to
    var priceTo = "";
    // 在庫指定（有りのみ：true、指定なし：false）
    var stock = "false";
    // 取得画像（リスト：list、サムネイル：thumbnail）
    var viewType = "thumbnail";

    if(cc == undefined || seq == undefined || limit == undefined || priceFrom == undefined || priceTo == undefined || stock == undefined || viewType == undefined){
        $(appendId).css({display: 'none'});
        return;
    }

    // 非同期処理順序制御
    var def = $.Deferred();

    // Ajax通信を行う
    $.ajax({
         type     : "GET"
        ,url      : pkg_common.getAppComplementUrl() + "/getMultipleCategoryData"
        ,dataType : "json"
        ,data     : {"categoryId":cc, "seq":seq, "limit":limit, "priceFrom":priceFrom, "priceTo":priceTo, "stock":stock, "viewType":viewType}
        ,timeout  : 30000
    })
    .done(function(data){setSymptomsCategoryData(data,appendId,def);})
    .fail(function(data){setSymptomsCategoryData(data,appendId,def);});

    return def.promise();
}

function setSymptomsCategoryData(response,appendId,def) {

    var appendHtml = '';

    // Ajax通信レスポンスが不正
    if (response == null || response == undefined || response == '') {
        appendHtml = '<span class="symptom20240417__notsupport">再読み込みをしてください。</span>';
        $(appendId).append(appendHtml);
        def.resolve();
        return;
    }

    // カテゴリー情報が存在しない場合
    var categoryItems = response.categoryItems;
    if(categoryItems == null || categoryItems.length <= 0){
        appendHtml = '<span class="symptom20240417__notsupport">症状カテゴリーなし</span>';
        $(appendId).append(appendHtml);
        def.resolve();
        return;
    }

    // 商品情報が存在しない場合
    var multipleCategoryMap = response.multipleCategoryMap;
    if(multipleCategoryMap == null || multipleCategoryMap.length <= 0){
        appendHtml = '<span class="symptom20240417__notsupport">検査取扱いなし</span>';
        $(appendId).append(appendHtml);
        def.resolve();
        return;
    }

    // 症状別商品一覧のレンダリング
    for(var i = 0, size = categoryItems.length; i < size ; i++){
        var key = categoryItems[i].cid;

        for($key in multipleCategoryMap[key]) {

            multipleCategoryMap[key][$key].cid=key;

            // 価格のフォーマットをカンマ区切りに変更
            multipleCategoryMap[key][$key].goodsPrice = BigDecimalConVerter(multipleCategoryMap[key][$key].goodsPrice);
            multipleCategoryMap[key][$key].goodsPriceInTax = BigDecimalConVerter(multipleCategoryMap[key][$key].goodsPriceInTax);

            if(multipleCategoryMap[key][$key]['goodsIconItems']){
                for(let i in multipleCategoryMap[key][$key]['goodsIconItems']){
                    //インフォメーションアイコン文字色
                    multipleCategoryMap[key][$key]['goodsIconItems'][i]['iconCharColor'] = blackOrWhite(multipleCategoryMap[key][$key]['goodsIconItems'][i]['iconColorCode'] );
                }
            }

            // 商品画像のリサイズパターン＆srcsetの組み込み：src & srcset パスを設定
            multipleCategoryMap[key][$key]['goodsGroupImageThumbnailSrcset'] = $("#goodsImageResizePrefixSrcset").val() + multipleCategoryMap[key][$key].goodsGroupImageThumbnail + ' 2x';
            multipleCategoryMap[key][$key]['goodsGroupImageThumbnail'] = $("#goodsImageResizePrefixSrc").val() + multipleCategoryMap[key][$key].goodsGroupImageThumbnail;
        }

        var tpl1 = Hogan.compile($('#symptom_goods_display_tmpl').text());
        var html1 = tpl1.render({
            category_goods_items : multipleCategoryMap[key]
        });
        $(appendId).append(html1);

    }

    def.resolve();
}
