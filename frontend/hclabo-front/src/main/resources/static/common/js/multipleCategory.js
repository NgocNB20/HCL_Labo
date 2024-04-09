$(function() {

	// レスポンシブ時のswiper設定
	$(window).on('resize',function(){
		setSwiper()
	});

	// Ajaxを順番に実行
	$.when(callAjax('#newItem','#newItemArea'))
			.then(function() {
					return callAjax('#salablenessRanking','#salablenessRankingArea');
			 })
			.then(function() {
					return callAjax('#categoryRanking','#categoryRankingArea');
			 })
			.then(function() {
					return callAjax('#specialCategory','#specialCategoryArea');
			 })
			 .then(function(){
					 setSwiper();
			 });
});

function callAjax(settingId,appendId){

	var cc = $(settingId).attr('cc');
	var seq = $(settingId).attr('seq');
	var limit = $(settingId).attr('limit');
	var priceFrom = $(settingId).attr('pricefrom');
	var priceTo = $(settingId).attr('priceto');
	var stock = $(settingId).attr('stock');
	var viewType = $(settingId).attr('viewtype');

	if(cc == undefined || seq == undefined || limit == undefined || priceFrom == undefined || priceTo == undefined || stock == undefined || viewType == undefined){
		$(appendId).css({display: 'none'});
		return;
	}

	//非同期処理順序制御
	var def = $.Deferred();

	// Ajax通信を行う
	$.ajax({
		 type		 : "GET"
		,url			: pkg_common.getAppComplementUrl() + "/getMultipleCategoryData"
		,dataType : "json"
		,data		 :	{"categoryId":cc, "seq":seq, "limit":limit, "priceFrom":priceFrom, "priceTo":priceTo, "stock":stock, "viewType":viewType}
		,timeout	: 30000
	})
	.done(function(data){setMultipleCategoryData(data,settingId,appendId,def);})
	.fail(function(data){setMultipleCategoryData(data,settingId,appendId,def);});

	return def.promise();
}

function setMultipleCategoryData(response,settingId,appendId,def) {

	//response is null
	if (response == null || response == undefined || response == '') {
		def.resolve();
		return;
	}

	var hideDispId = appendId;
	//カテゴリ別ランキング以外場合
	if(settingId !== "#categoryRanking"){
		hideDispId = hideDispId+'Hide';
	}

	var list = response.categoryItems;
	if(list == null || list.length <= 0){
		$(hideDispId).css({display: 'none'});
		def.resolve();
		return;
	}

	var multipleCategoryMap = response.multipleCategoryMap;

	if(multipleCategoryMap == null || multipleCategoryMap.length <= 0){
		$(hideDispId).css({display: 'none'});
		def.resolve();
		return;
	}

	//カテゴリ別ランキングの場合、カテゴリ枠をレンダリング
	if(settingId==="#categoryRanking"){
		var tpl = Hogan.compile($('#category_display_tmpl').text());
		var html = tpl.render({
			category_items : list
		});
		$(appendId).append(html);
	}

	//スワイプスタイル適用フラ
	var swiperSlide = $(settingId).attr('swiperSlide');
	if(!swiperSlide) swiperSlide = false;

	//ランキング表示フラグ
	var ranking = $(settingId).attr('ranking');
	if(!swiperSlide) swiperSlide = false;

	//商品エリアのレンダリング
	for(var i = 0, size = list.length; i < size ; i++){
		var key = list[i].cid;

		var j = 0;
		for($key in multipleCategoryMap[key]) {

			j++;
			multipleCategoryMap[key][$key].cid=key;

			// 価格のフォーマットをカンマ区切りに変更
			multipleCategoryMap[key][$key].goodsPrice = BigDecimalConVerter(multipleCategoryMap[key][$key].goodsPrice);
			multipleCategoryMap[key][$key].goodsPriceInTax = BigDecimalConVerter(multipleCategoryMap[key][$key].goodsPriceInTax);

			// スワイプスタイル適用フラグ
			multipleCategoryMap[key][$key]['swiperSlide'] = swiperSlide;

			// ランキング表示
			multipleCategoryMap[key][$key]['ranking'] = ranking;
			if(ranking){
				multipleCategoryMap[key][$key]['rankingIndex'] = j;
			}

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

		var tpl1 = Hogan.compile($('#goods_display_tmpl').text());
		var html1 = tpl1.render({
			category_goods_items : multipleCategoryMap[key]
		});

		if(settingId !=="#categoryRanking"){
			$(appendId).append(html1);
		} else {
			//カテゴリ別ランキングの場合
			$('#cate_goods_tmpl_'+key).prepend(html1);
		}

		// TOP画面でCSSの表示制御処理
		switch(settingId) {
			case "#newItem":
				$('<hr class="l-sm">').insertAfter('#newItemAreaHide');
				break;
			case "#salablenessRanking":
				$('<hr class="l-sm">').insertAfter('#salablenessRankingAreaHide');
				break;
			case "#categoryRanking":
				$('<hr class="l-sm">').insertAfter('#categoryRankingArea');
				break;
			case "#specialCategory":
				$('<hr class="l-sm">').insertAfter('#specialCategoryAreaHide');
				break;
			default:
				break;
		}

		// 一覧ページ アイコンが無い場合
		$('.c-product__item-icon').each(function () {
			if ( !($(this).find('span').length)) {
				$(this).css('margin', '0');
				} 
		})
	}

	def.resolve();
}

function setSwiper(){
	$('.c-product-list--ranking').each(function(){
		var slideclass = $(this).attr('class');
		var elem = '[class="' + slideclass + '"]';
		if (window.innerWidth < 900) {
			var rankslide = new Swiper(elem, {
				slidesPerView: 'auto',
				effect: 'slide',
				speed: 500,
				observer: true,
				on: {
					init: function () {
						var totalSlides = this.slides.length;
						if (totalSlides == 0) {
							$('.c-product-list--ranking .swiper-pagination').hide();
						}
					}
				},
				pagination: {
					el: '.swiper-pagination',
					clickable: true
				}
			})
		} else {
			if(this.swiper !== undefined){
				this.swiper.destroy(true,true);
				this.swiper = undefined;
			}
		}
	});
}

