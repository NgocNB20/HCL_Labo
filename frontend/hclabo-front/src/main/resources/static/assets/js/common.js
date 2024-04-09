/*-----------------------------------------------------------
index:
	Rollover Image
	Header Search Parts
	Horizon Scroll View
	Window Onload
-----------------------------------------------------------*/

/* Rollover Image
-----------------------------------------------------------*/
function initRollovers() {
	if (!document.getElementById) return

	var aPreLoad = new Array();
	var sTempSrc;
	var aImages = document.getElementsByTagName('img');
	var iImages = document.getElementsByTagName('input');

	for (var i = 0; i < aImages.length; i++) {
		if (aImages[i].className == 'over') {
			var src = aImages[i].getAttribute('src');
			var index = src.lastIndexOf('?');
			if (index >= 0) {
				src = src.substring(0, index);
			}
			var ftype = src.substring(src.lastIndexOf('.'), src.length);
			var hsrc = src.replace(ftype, '_o'+ftype);

			aImages[i].setAttribute('hsrc', hsrc);

			aPreLoad[i] = new Image();
			aPreLoad[i].src = hsrc;

			aImages[i].onmouseover = function() {
				sTempSrc = this.getAttribute('src');
				this.setAttribute('src', this.getAttribute('hsrc'));
			}

			aImages[i].onmouseout = function() {
				if (!sTempSrc) sTempSrc = this.getAttribute('src').replace('_o'+ftype, ftype);
				this.setAttribute('src', sTempSrc);
			}
		}
	}

	for (var i = 0; i < iImages.length; i++) {
		if (iImages[i].className == 'over') {
			var src = iImages[i].getAttribute('src');
			var index = src.lastIndexOf('?');
			if (index >= 0) {
				src = src.substring(0, index);
			}
			var ftype = src.substring(src.lastIndexOf('.'), src.length);
			var hsrc = src.replace(ftype, '_o'+ftype);

			iImages[i].setAttribute('hsrc', hsrc);

			aPreLoad[i] = new Image();
			aPreLoad[i].src = hsrc;

			iImages[i].onmouseover = function() {
				sTempSrc = this.getAttribute('src');
				this.setAttribute('src', this.getAttribute('hsrc'));
			}

			iImages[i].onmouseout = function() {
				if (!sTempSrc) sTempSrc = this.getAttribute('src').replace('_o'+ftype, ftype);
				this.setAttribute('src', sTempSrc);
			}
		}
	}

}

/* Horizon Scroll View
-----------------------------------------------------------*/
function sv_initHScrollView( range ) {

	var sv_tx = [];
	var sv_max = [];
	var sv_range = [];

	var sv_view = [];
	var sv_element = [];

	var n = 0;
	var n2 = 0;

	while( 1 ) {

		//window.alert( n );

		if( $("#sv_scrollView"+sv_zero( n )).html() == null ) {
			if ( n == 0 ) {
				n = 1;
				if( $("#sv_scrollView"+sv_zero( n )).html() == null ) break;
			} else {
				break;
			}
		}

		sv_view[n] = $("#sv_scrollView"+sv_zero( n ));
		sv_element[n] = $("#sv_scrollElement"+sv_zero( n ));

		sv_view[n].css("position","relative");
		sv_element[n].css("position", "relative");

		sv_tx[n] = 0;
		sv_max[n] = sv_element[n].width() - sv_view[n].width();
		sv_range[n] = range[n2];

		if ( sv_max[n] > 0 ) {
			$("#sv_nextButton"+sv_zero( n )).click( function(e) {
				var i = e.target.id.split("sv_nextButton")[1];
				if ( sv_tx[i] > -sv_max[i] ) {
					sv_tx[i] -= sv_range[i];
					sv_element[i].stop();
					sv_element[i].animate({ left:sv_tx[i] }, 600, "easeOutQuart" );
				}
			});

			$("#sv_prevButton"+sv_zero( n )).click( function(e) {
				var i = e.target.id.split("sv_prevButton")[1];
				if ( sv_tx[i] < 0 ) {
					sv_tx[i] += sv_range[i];
					sv_element[i].stop();
					sv_element[i].animate({ left:sv_tx[i] }, 600, "easeOutQuart" );
				}
			});
		}
		n++;
		n2++;
	}
}

function sv_zero( n ) {
	return ( n == 0 ) ? "" : n;
}

function hcHeight(){
		var hc_a = $(".horizonScroll").innerHeight();
		$(".horizonScroll .back").height(hc_a);
		$(".horizonScroll .next").height(hc_a);
		if (hc_a < 50) {
			$(".horizonScroll .back").css("display","none");
			$(".horizonScroll .next").css("display","none");
		}
};

/*
 * カード情報削除確認ダイアログ
 */
function cardDeleteConfirm(){
    return confirm("削除してもよろしいですか？");
}

/*
 * BIGDECIMAL型をカンマ区切りに変換
 */
function BigDecimalConVerter(targetPrice){
	var converterPrice;
	if (targetPrice != null && targetPrice != undefined) {
		converterPrice = targetPrice.toString().replace(/(\d)(?=(\d{3})+$)/g , '$1,');

	}
	return converterPrice;
}

/* Window Onload
-----------------------------------------------------------*/
$(function(){
	initRollovers();

	// 検索フォーム
	//$("#d_lookup input#search").focus(function() {
	//	if($(this).val() == $(this).attr('defaultValue'))
	//	$(this).val('');
	//}).blur(function() {
	//	if(jQuery.trim($(this).val()) == "") {
	//		$(this).val($(this).attr('defaultValue'));
	//	}
	//});

	// 商品詳細マイリスト
	$("#d_goods_detail #d_mylist").each(function(){
		sv_initHScrollView([136]);
		hcHeight();
	});

	// カートマイリスト
	$("#d_cart #d_mylist").each(function(){
		sv_initHScrollView([146]);
		hcHeight();
	});

	/* 二重サブミットを防止する処理
-----------------------------------------------------------*/
	$('form').on('submit', function(e){
		var $form = $(this);
		if ($form.data('submitted') === true) {
			e.preventDefault();
		} else {
			$form.data('submitted', true);
		}
		setTimeout(function () {
			$form.data('submitted', false);
		}, 3000);
	});

});

// 前方・ブラウザバックでの処理
window.addEventListener( "pageshow", function ( event ) {
	var historyTraversal = event.persisted ||
		( typeof window.performance != "undefined" &&
			window.performance.navigation.type === 2 );

	if (historyTraversal) {
		$('form').data('submitted', false)
	}
});
