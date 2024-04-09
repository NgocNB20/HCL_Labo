/**
 * 規格画像の切り替え設定
 */
var goodsImageViewer = {
	/* 規格1選択リストのエレメントID */
	unitSelect1Id : "unitSelect1",
	/* 静的ファイル補完URL */
	sUrl : "",
	/* 静的ファイル補完URL(g_images) */
	gImagePath : "",
	/* 静的ファイル補完URL(images/goods) */
	goodsImagePath : "",
	/* 画像なしの場合に表示する画像 */
	sNoImage : "/noimage_pds.gif",
	mNoImage : "/noimage_pdm.gif",
}

$(function(){

	// 静的ファイルの作成
	goodsImageViewer.sUrl = $("#staticComplementUrl").val();
	goodsImageViewer.gImagePath = $("#goodsImagePath").val();
	goodsImageViewer.goodsImagePath = goodsImageViewer.sUrl += "/images/goods";

	// グループ画像の設定
	var oNode = $('<div class="imgBox heightLine"></div>');
	var goodsname = $('#goodsGroupNamePC').text();
	$("span[id^='goodsGroupImageForJs']").each(function(i){
		// 画像データを取得
		var goodsData = $(this).html();
		if (!goodsData) {
			// データがなければ読み飛ばす
			return true;
		}
		
		// 画像データを取得
		var ary = createImageData(goodsData);
		if (ary[5]) {
			// 拡大画像がある時
			node = $('<div class="thmbContainer"><div style="background-image:url(' + ary[3] + ')" pc2image="' + ary[4] + '">'
					+ '<a href="' + ary[5] + '" title="' + goodsname + '"><img src="' + goodsImageViewer.goodsImagePath + '/zoom_image_s.gif" width="60" height="60" style="display:none;" class="alphafilter" /></a></div></div>');
		} else {
			// 拡大画像がない時
			node = $('<div class="thmbContainer"><div style="background-image:url(' + ary[3] + ')" pc2image="' + ary[4] + '" title="' + goodsname + '"></div></div>');
		}
		//初期画像の追加とクラスの追加
		if (i == 0) {
			setMainImage(node, ary);
		}

		// グループ画像に追加
		oNode.append(node);
	});

	// 商品画像のデータが一枚も存在していない場合
	if (!oNode.is(":has('div')")) {
		$("#d_photo").css({
			"background-image" : "url(" + goodsImageViewer.gImagePath + goodsImageViewer.mNoImage + ")",
			"background-position":"center",
			"background-repeat": "no-repeat"
		});
	}

	// グループ画像のサムネイル追加
	if (oNode.children().length != 0) {
		$("#other-images").append(oNode);
	}

	//---------------------------------------------
	// サムネイルのロールオーバーイベントの設定
	//---------------------------------------------
	$("#other-images").find(".imgBox>div").hover(function(){
		var s = $(this).css("background-image").split('"')[1];
		setSelectGoodsImage($(this));
	})

	//---------------------------------------------
	//lightBoxの初期化
	//---------------------------------------------
	$("#other-images").find("a").lightBox({
		imageBtnPrev : goodsImageViewer.goodsImagePath + "/lightbox-btn-prev.gif",
		imageBtnNext : goodsImageViewer.goodsImagePath + "/lightbox-btn-next.gif",
		imageBtnClose : goodsImageViewer.goodsImagePath + "/lightbox-btn-close.gif",
		imageBlank : goodsImageViewer.goodsImagePath + "/lightbox-blank.gif",
		imageLoading : goodsImageViewer.goodsImagePath + "/lightbox-ico-loading.gif",
		containerBorderSize : 37,
		overlayBgColor : '#000',
		overlayOpacity : 0.6,
		txtImage : '',
		txtOf : '/'
	});

	//---------------------------------------------
	// m画像のクリック処理
	//---------------------------------------------
	$("#d_photo").click(function(){
		if ($("div.select").is(":has('a')")) $("div.select a").click();
	});
});

/**
 * 画像データを配列に変換
 *
 * 商品グループ画像の場合
 *   {,,,小画像,中画像,大画像}
 * 規格画像の場合
 *   {規格画像コード,規格値,規格画像グループNO,小画像,中画像,大画像}
 *
 * @param goodsData カンマ区切りの画像データ
 * @return 配列の画像データ
 */
function createImageData(goodsData) {
	var ary = goodsData.split(",");

	//1個目がs画像だったとき
	if (isSmallImage(ary[3])) {
		//2個目が空の時はnoimage指定
		if (!ary[4]) {
			ary[4] = goodsImageViewer.gImagePath + goodsImageViewer.mNoImage;
		}
		//2個目がl画像だったとき
		else if (isLargeImage(ary[4])) {
			//3個目にl画像を移動して2個目にnoimage指定
			ary[5] = ary[4];
			ary[4] = goodsImageViewer.gImagePath + goodsImageViewer.mNoImage;
		}
	}
	//1個目がm画像だったとき
	else if (isMediumImage(ary[3])) {
		//2個目が存在したときは3個目をl画像にする
		if (ary[4]) ary[5] = ary[4];
		//2個目にm画像を移動して1個目にnoimage指定
		ary[4] = ary[3];
		ary[3] = goodsImageViewer.gImagePath + goodsImageViewer.sNoImage;
	}
	//1個目がl画像だったとき
	else if (isLargeImage(ary[3])) {
		//3個目にl画像を移動して、後はnoimage指定
		ary[5] = ary[3];
		ary[3] = goodsImageViewer.gImagePath + goodsImageViewer.sNoImage;
		ary[4] = goodsImageViewer.gImagePath + goodsImageViewer.mNoImage;
	}
	return ary;
}

/**
 * @return 小画像か？
 */
function isSmallImage(fileName) {
	return fileName.indexOf("_pds.") >= 0
}

/**
 * @return 中画像か？
 */
function isMediumImage(fileName) {
	return fileName.indexOf("_pdm.") >= 0
}

/**
 * @return 大画像か？
 */
function isLargeImage(fileName) {
	return fileName.indexOf("_pdl.") >= 0
}

/**
 * 対象の画像データを選択状態にする
 */
function setMainImage(node, ary) {
	$("#d_photo").css({
		"background-image": "url(" + ary[4] + ")",
		"background-position":"center",
		"background-repeat": "no-repeat"
	});
	node.children("div").addClass("select");
	if (ary[5]) {
		node.find("img").show();
		$("#d_photo").find("img").show();
	}
	if (ary[1] == "") {
		ary[1] = "&nbsp;"
	}
}

function setSelectGoodsImage(selectGoodsImage){
	$("#d_photo").css({
		"background-image": "url(" + selectGoodsImage.children("div").attr("pc2image") + ")",
		"background-position":"center",
		"background-repeat": "no-repeat"
	});

	$("#other-images div.select img").hide();
	$("#other-images div.select").removeClass("select");

	var imgInfo = selectGoodsImage.children("div");
	imgInfo.addClass("select");

	//拡大可否
	if (selectGoodsImage.is(":has('a')") ) {
		$("#d_photo img").show();
		selectGoodsImage.find("img").show();
	} else {
		$("#d_photo img").hide();
	}
}
