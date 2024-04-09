/** カート追加メソッド */
var ACTION_METHOD_ADD_CART;
/** タイムアウト（秒） */
var TIME_OUT;
/** エラー発生時リダイレクト先URL */
var ERROR_PAGE_URL;

/**
 * カートへ追加
 */
function ajaxCartAdd(){
	// 初期処理
	initialProcessing();

	// JavaScript内でチェックした際のエラーメッセージを格納する変数
	var list = [];

	// 入力チェック
	// 規格1が未選択の場合は入力エラーとする
	if($("#unitSelect1").length > 0) {
		if(!$("#unitSelect1").val()) {
			var errObj = {};
			errObj["message"] = $("#errorUnitTitle1").val() + " を選択してください。";
			list[list.length] = errObj;
		}
	}
	// 規格2が未選択の場合は入力エラーとする
	if($("#unitSelect2").length > 0) {
		if(!$("#unitSelect2").val()) {
			var errObj = {};
			errObj["message"] = $("#errorUnitTitle2").val() + " を選択してください。";
			list[list.length] = errObj;
		}
	}

	// 数量が未入力の場合は入力エラーとする
	if($("#gcnt").length > 0) {
		if(!$("#gcnt").val()) {
			var errObj = {};
			errObj["message"] = $("#gcnt").attr("title") + " を入力してください。";
			list[list.length] = errObj;
		} else if($("#gcnt").val().match(/^[^0-9]+$/g)) {
			var errObj = {};
			errObj["message"] = $("#gcnt").attr("title") + " の値は、数値で入力してください。";
			list[list.length] = errObj;
		} else if($("#gcnt").val().match(/^[-][0-9]+$/g)) {
			var errObj = {};
			errObj["message"] = $("#gcnt").attr("title") + " は、0以上の値を入力してください。";
			list[list.length] = errObj;
		} else if($("#gcnt").val().match(/^[-]?[0-9]+\.[0-9]+$/g)) {
			var errObj = {};
			errObj["message"] = $("#gcnt").attr("title") + " は整数で入力してください。";
			list[list.length] = errObj;
		} else if($("#gcnt").val() < 1 || $("#gcnt").val() > 9999) {
			var errObj = {};
			errObj["message"] = $("#gcnt").attr("title") + " は 1 以上、9999 以下で入力してください。";
			list[list.length] = errObj;
		}
	}

	// 商品番号が未設定時の対応
	// ※ブラウザーバックでgcdがクリアされる場合があるため
	if(!$("#gcd").val()) {
		if($("#unitSelect2").length > 0) {
			// 規格2が存在する場合は規格2の値を設定
			$("#gcd").val($("#unitSelect2").val());
		}
		else {
			// 規格1の値を設定
			$("#gcd").val($("#unitSelect1").val());
		}
	}

	// 在庫数チェック
	if(!list.length){
		// ここまででエラーがない場合 -> 規格も数量もきちんと入力されている場合
		// 在庫数チェックを行う
		var gcd = $("#gcd").val();
		var stock = parseInt($('#salesPossibleStock-' + $("#gcd").val()).val().replace(",", ""));
		if (stock === 0) {
			// 在庫数が0の場合エラー
			var errObj = {};
			errObj["message"] = "申し訳ございませんが、現在ご指定の商品は在庫切れです。";
			list[list.length] = errObj;
		}else if(parseInt($("#gcnt").val()) > stock) {
			// 在庫不足の場合エラー
			var errObj = {};
			errObj["message"] = "申し訳ございませんが、現在ご指定の商品は在庫が不足しています。" + stock + "個以内で注文してください。";
			list[list.length] = errObj;
		}
	}

	if(list.length){
		// エラーメッセージをhtml出力
		printErrorMessages(list);
		// クリックを許可
		allowClick();
		$(".c-global-error c-global-error--margin-m").attr("style","display: inherit");
		return;
	}

	if(!list.length) {
		$(".c-global-error c-global-error--margin-m").attr("style","display: none");
	}

	var gcd = $("#gcd").val();
	var gcnt = $("#gcnt").val();
	var ggname = $("#goodsGroupName").text();
	var list_span = $(".goods_lst");
	var gseq = null;

	for (i = 0; i < list_span.length; i++) {
		let item = list_span[i].innerText.split(',');

		if (gcd === item[0]) {
			gseq = item[1];
		}
	}

	//ajax実行
	callAjax(gcd, gcnt, ggname, gseq);
}

/**
 * カートへ追加（マイリスト用）
 */
function ajaxCartAddWishlist(req){
	// 初期処理
	initialProcessing();
	// パラメータを取得
	var gcd = $(req).find("#gcd").val();
	var gcnt = $(req).find("#gcnt").val();
	var ggname = $(req).find("#ggname").val();
	var list_span = $(".goods_lst");
	var gseq = null;

	for (i = 0; i < list_span.length; i++) {
		let item = list_span[i].innerText.split(',');

		if (gcd === item[0]) {
			gseq = item[1];
		}
	}

	// ajax実行
	callAjax(gcd, gcnt, ggname, gseq);
}

/**
 * カートへ追加するボタンが押された際の初期処理
 */
function initialProcessing(){
	// 複数回カートに追加されるのを防止するため、クリックを禁止
	prohibitClick();

	$('.add-bag-wrap').unbind();

	$("#errorMessages").attr("style","display: none");

	// エラーメッセージを一旦削除
	if ($("#errorMessages").children().length > 0) {
		$("#errorMessages").children().remove();
	}

	// サーバサイドでバリデータエラーが発生している場合はメッセージを非表示にする
	if($(".inputError").length > 0) {
		$(".inputError").hide();
	}
}

/** クリックを禁止するメソッド */
function prohibitClick(){
	if($("#ajaxCartAdd").length > 0) {
		$("#ajaxCartAdd").attr('onclick', '(event.preventDefault) ? event.preventDefault():event.returnValue=false;');
	}
	if($("[id^='goAjaxCart']").length > 0) {
		$("[id^='goAjaxCart']").attr('onclick', '(event.preventDefault) ? event.preventDefault():event.returnValue=false;');
	}
}

/**
 * ajax実行
 */
function callAjax(gcd, gcnt, ggname, gseq){
	// Ajax通信を行う
	$.ajax({
		 type			:	"GET"
		,url			:	pkg_common.getAppComplementUrl() + ACTION_METHOD_ADD_CART
		,dataType	:	"json"
		,data			:	{ "gcd":gcd , "gcnt":gcnt, "gseq": gseq}
		,timeout		:	TIME_OUT
	})
	.done(function(data){updateCartView(data, ggname);})
	.fail(function(data){updateCartView(data, ggname);})
	;
}

/**
 * カート追加通信のコールバックメソッド
 */
function updateCartView(response, ggname) {
	showResult(response, ggname);
	// クリック許可
	allowClick();
}

/**
 * カート追加結果を表示
 */
function showResult(response, ggname){
	// セッション切れの場合は再読込する
	if(!response["cart"]["okSession"]){
		location.href = location.href;
	}
	// カート追加結果の表示
	if (response["cart"]["result"]) {
		// 追加成功
		$("#cartMessageGoodsName").html(ggname);
		//ダイアログオープン
		$("#modalOpen").trigger('click');
	} else {
		// 追加失敗
		if (response["cart"]["validatorErrorList"].length) {
			// バリデータエラー
			printErrorMessages(response["cart"]["validatorErrorList"]);
		} else if (response["cart"]["errorList"].length){
			// カート追加エラー errorList
			printErrorMessages(response["cart"]["errorList"]);
		}
	}

	//ヘッダーの商品点数を変更
	if(null != response["cart"]["cartInfo"] && response["cart"]["cartInfo"]["goodsTotalCount"] != "0"){
		if($("#sumCount").length < 1 && $("#sumCountSP").length < 1) {
			$("#countSum").append("<em id=\"sumCount\" class=\"c-baloon c-baloon--number\" ></em>");
			$("#countSumSP").append("<em id=\"sumCountSP\"></em>");
		}
		var sumCount;
		var goodsTotalCount = parseInt(response["cart"]["cartInfo"]["goodsTotalCount"]);
		if (goodsTotalCount > 999){
			sumCount = "999+";
		}else{
			sumCount = goodsTotalCount.toString();
		}
		$("#sumCount").html(sumCount);
		$("#sumCountSP").html(sumCount);
	}
}

/**
 * エラーメッセージをhtmlに出力するメソッド
 */
function printErrorMessages(errorMessage){
	if(errorMessage.length){
		var html = "<div class=\"inputError\"><ul id=\"allMessages\">";
		for(var i = 0; i < errorMessage.length; i++){
			html += "<li>" + errorMessage[i].message + "</li>\n";
		}
		html += "</ul>\n</div>";
		$("#errorMessages").html(html);
		$("#errorMessages").attr("style","display: inherit");
		$("html,body").animate({scrollTop:0});
	}
}

/** クリック許可メソッド */
function allowClick(){
	if($("#ajaxCartAdd").length > 0) {
		$("#ajaxCartAdd").attr('onclick', 'ajaxCartAdd();');
	}
	if($("[id^='goAjaxCart']").length > 0) {
		$("[id^='goAjaxCart']").attr('onclick', 'ajaxCartAddWishlist(this); return false;');
	}
}

/** クリックイベントを無効にするメソッド */
function preventOnClick(e) {
	(e.preventDefault) ? e.preventDefault():e.returnValue=false;
}
