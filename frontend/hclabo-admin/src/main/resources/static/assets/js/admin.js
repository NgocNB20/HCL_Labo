/*
 * 簡易削除確認ダイアログ
 */
function simpleDeleteConfirm(){
    return confirm("削除してもよろしいですか？");
}

/*
 * 簡易削除確認ダイアログ
 */
function simpleDeleteCategoryConfirm(){
	var value = $('#dtCategory').val();
	if(typeof value !== "undefined" && value != '')
	{
	    var fixedMessage1 = "並び替えた表示順が登録されていません。";
	    var fixedMessage2 = "\n破棄してもよろしいですか？";
	    var status =  confirm(fixedMessage1 + fixedMessage2);
	    if (status == true) {
	    	return simpleDeleteConfirm();
	    }
	    return false;
	}
	return simpleDeleteConfirm();
}

/*
 * 簡易削除確認ダイアログ
 */
function simpleDeleteCategoryConfirm2(obj){
    var deleteButton = $(obj);
    var value = $('#dtCategory').val();
    var targetSeq = deleteButton.next("input:hidden[id^='deleteTargetIndex']").val();
    $("#resultIndex").val(targetSeq);
    if(typeof value !== "undefined" && value != '')
    {
        var fixedMessage1 = "並び替えた表示順が登録されていません。";
        var fixedMessage2 = "\n破棄してもよろしいですか？";
        var status =  confirm(fixedMessage1 + fixedMessage2);
        if (status == true) {
            return simpleDeleteConfirm();
        }
        return false;
    }
    return simpleDeleteConfirm();
}

/*
 * 簡易確認ダイアログ
 */
function simpleCategoryConfirm(){

    var value = $('#dtCategory').val();
	if(typeof value !== "undefined" && value != '')
	{
	    var fixedMessage1 = "並び替えた表示順が登録されていません。";
	    var fixedMessage2 = "\n破棄してもよろしいですか？";
	    return confirm(fixedMessage1 + fixedMessage2);
	}
}

/*
 * 状態参照機能付き
 * 簡易削除確認ダイアログ
 *
 * id : タグ要素ID属性
 * status : 削除ステータス
 */
function simpleDeleteConfirmByStatus(id, status){
    var target = $id(id);
    if (target.value == status){
        return confirm("削除してもよろしいですか？");
    }else{
        return true;
    }
}

/*
 * 削除確認ダイアログ
 *
 * ポジションを指定した箇所にメッセージを挿入します。
 *
 * message : 追加するメッセージ
 * position : before 固定文言の前に挿入
 *                 between 固定文言の中間に挿入
 *                 behind 固定文言の後ろに挿入
 */
function deleteConfirm(message, position){
    var fixedMessage1 = "削除すると、元に戻すことはできません。";
    var fixedMessage2 = "\r\n\r\n削除してもよろしいですか？";

    // メッセージ指定がない場合は固定文言のみ
    // ポジション指定がない、または誤っている場合　メッセージ+固定文言
    // ポジション前を指定した場合　メッセージ+固定文言
    // ポジション中間を指定した場合　固定文言1+メッセージ+固定文言2
    // ポジション後ろを指定した場合　固定文言+メッセージ
    if (message == null || message == "") {
        return confirm(fixedMessage1 + fixedMessage2);
    } else {
        if (position == null || position == ""|| position == 'before') {
            return confirm(message + '\r\n' + fixedMessage1 + fixedMessage2);
        } else if (position == 'between') {
            return confirm(fixedMessage1 + '\r\n' +message + fixedMessage2);
        } else if (position == 'behind') {
            return confirm(fixedMessage1  + fixedMessage2 + '\r\n' +message);
        } else {
            return confirm(message + '\r\n' + fixedMessage1 + fixedMessage2);
        }
    }
}

/*
 * 状態参照機能付き
 * 削除確認ダイアログ
 *
 * ポジションを指定した箇所にメッセージを挿入します。
 *
 * id : タグ要素ID属性
 * status : 削除ステータス
 * message : 追加するメッセージ
 * position : before 固定文言の前に挿入
 *                 between 固定文言の中間に挿入
 *                 behind 固定文言の後ろに挿入
 */
function deleteConfirmByStatus(id, status, message, position){
    var target = $id(id);

    // ターゲット文言がstatusと違う場合は確認ダイアログを表示しない
    if (target.value != status){
        return true;
    }

    return deleteConfirm(message, position);
}

/*
 * cancel confirm
 */
function cancelConfirm(){
    return confirm("キャンセルしてもよろしいですか？");
}
// 受注キャンセル
function orderCancelConfirm(button) {

    var orderCancel = confirm("受注キャンセルしてもよろしいですか？\r\n (管理用メモのみ変更内容が反映されます。)");
    if(orderCancel == false) {
        return false;
    }

    var displayCouponDiscountFlg = document.getElementById("useCouponFlg").value;
    // クーポン利用していない場合は、そのままreturn
    if(displayCouponDiscountFlg == "false") {
        return true;
    }
    var buttonId = $("#doOnceOrderCancel-1").attr("id");
    $(couponCancel(buttonId));
    return false;
}

// クーポンキャンセル処理
function couponCancel(buttonId) {
    $("#couponCancel").click(couponCancelDialog(buttonId));
}

// クーポン利用確認ダイアログ
function couponCancelDialog(buttonId) {
    COUPON_TITLE = "クーポン利用確認";

    COUPON_MESSAGE1 = "ご利用中のクーポンを無効にしますか？";

    var couponDiscountPriceDisp = $("#couponDiscountPriceDisp").val();
    if(couponDiscountPriceDisp != ""){
    	COUPON_MESSAGE1 = COUPON_MESSAGE1 + "<br/>(割引金額： " + couponDiscountPriceDisp + " 円)";
    }

    COUPON_MESSAGE2 = "";

    var orderCancelFlg = $("#orderCancelFlg").val();
    var couponTargetGoodsCancelMessgeFlg = $("#couponTargetGoodsCancelMessgeFlg").val();
    var couponDiscountLowerOrderPriceMessgeFlg = $("#couponDiscountLowerOrderPriceMessgeFlg").val();

    if (couponTargetGoodsCancelMessgeFlg == "true") {
	    var couponTargetGoodsIsAllFlg = $("#couponTargetGoodsIsAllFlg").val();
	    let couponTargetType = $("#couponTargetType").val() == '1' ? "（絞込指定）" : "（除外指定）";
	    // クーポンに対象商品がない場合（全商品対象）
	    if (couponTargetGoodsIsAllFlg == "true") {
	    	COUPON_MESSAGE2 = "商品がすべてキャンセルされました。<br/><br/>";

	    } else {
	        var couponTargetGoodsCode = $("#couponTargetGoodsCode").val().split(",");
	        var couponTargetGoodsName = $("#couponTargetGoodsName").val().split(",");
	        var couponTargetGoodsList = "";

	        for (var i = 0; i < couponTargetGoodsCode.length; i++) {
	            couponTargetGoodsList = couponTargetGoodsList + "&nbsp;&nbsp;対象商品番号：" + couponTargetGoodsCode[i]  + "&nbsp;&nbsp;対象商品名："+couponTargetGoodsName[i]+ "<br/>";
	        }

	        COUPON_MESSAGE2 = "クーポン対象商品がすべてキャンセルされました。<br/>[クーポン対象商品]" + couponTargetType + "<br/>" + couponTargetGoodsList + "<br/>";
		}
	} else if (couponDiscountLowerOrderPriceMessgeFlg == "true") {
        var goodsPriceTotalDisp = $("#goodsPriceTotalDisp").val();
        var couponDiscountLowerOrderPriceDisp = $("#couponDiscountLowerOrderPriceDisp").val();

        COUPON_DETAILS = "<br/>&nbsp;&nbsp;商品合計金額： " + goodsPriceTotalDisp + " 円<br/>&nbsp;&nbsp;適用金額： " + couponDiscountLowerOrderPriceDisp + " 円<br/><br/>";
        COUPON_MESSAGE2 = "商品金額合計（税抜）がクーポン適用金額を満たしていません。" + COUPON_DETAILS;

    }

    OK_BUTTON = "はい";
	CANCEL_BUTTON = "いいえ";

	// HTypeCouponLimitTargetType の value値
	var COUPON_LIMIT_TARGET_TYPE_OFF = "0";
	var COUPON_LIMIT_TARGET_TYPE_ON_CHECKED = "2";

    var msgContent = COUPON_MESSAGE2 + COUPON_MESSAGE1;

    // -------------------------------------------------------------------
    // クーポン確認のダイアログ表示
    // -------------------------------------------------------------------
    $.confirm({
        title: COUPON_TITLE,
        content: msgContent,
        boxWidth: '600px',
        useBootstrap: false,
        draggable: false,
        buttons: {
            confirm: {
                text: OK_BUTTON,
                btnClass: 'ui-button ui-corner-all ui-widget',
                keys: ['enter'],
                action: function(){
                    $("#couponLimitTargetTypeValue").val(COUPON_LIMIT_TARGET_TYPE_OFF);
                    $("#" + buttonId).click();
                }
            },
            cancel: {
                text: CANCEL_BUTTON,
                keys: ['esc'],
                action: function(){
                    $("#couponLimitTargetTypeValue").val(COUPON_LIMIT_TARGET_TYPE_ON_CHECKED);
                    $("#" + buttonId).click();
                }
            }
        }
    });
}

function paymentAll(){
    return confirm("全件入金済みにしてもよろしいですか？");
}

function shipmentAll(){
    return confirm("全件出荷済みにしてもよろしいですか？");
}


function preview(url, width, height) {
    if (width == null) {
        width = window.screen.availWidth * 0.8;
    } else {
        width = window.screen.availWidth * width;
    }

    if (height == null) {
        height = window.screen.availHeight * 0.8;
    } else {
        height = window.screen.availHeight * height;
    }

    if (url.indexOf("?") != -1) {
        url = url + "&newwindow=true";
    } else {
        url = url + "?newwindow=true";
    }

    // ディスプレイの中央に表示
    var subleft = (window.screen.availWidth - width) / 2;
    var subtop = (window.screen.availHeight - height) / 2;

    window.open(url, "_blank", "width=" + width + ", height=" + height + ", resizable=yes, scrollbars=yes, top=" + subtop + ", left=" + subleft);

    return false;
}

function editPreview(url, width, height) {
    if (width == null) {
        width = window.screen.availWidth * 0.8;
    } else {
        width = window.screen.availWidth * width;
    }

    if (height == null) {
        height = window.screen.availHeight * 0.8;
    } else {
        height = window.screen.availHeight * height;
    }

    // ディスプレイの中央に表示
    var subleft = (window.screen.availWidth - width) / 2;
    var subtop = (window.screen.availHeight - height) / 2;

    window.open(pkg_common.getComplementedAppPath(url), "_blank", "width=" + width + ", height=" + height + ", resizable=yes, scrollbars=yes, top=" + subtop + ", left=" + subleft);

    return false;
}

function unLockConfirm(){
    return confirm("ロックを解除してもよろしいですか？\r\n (管理用メモのみ変更内容が反映されます。)");
}

function registConfirm(){
    return confirm("登録してもよろしいですか？");
}

function approveConfirm(){
    return confirm("レビューを承認状態にしてもよろしいですか？");
}

function denyConfirm(){
    return confirm("レビューを否認状態にしてもよろしいですか？");
}

/*
 * 長い文字を指定文字以降を切り捨てた文字列を返す
 *　ex．アイテック阪急阪神　⇒　アイテック・・・
 *　
 *　プルダウンやラジオボタンでの利用が前提となっている為
 *　繰り返し項目でも利用可能
 *　設定例．"#addressClientSelect > option"
 *　
 *　【注意】
 *　指定された文字は表示する最大の文字数を指定する
 *　全ての文字を1文字としてカウントする為、半角文字も1文字とカウントされる
 *　また 、指定された接尾文字は最大表示数-1文字目からセットする　　　
 *　
 *　@param obj 対象文字オブジェクトのID
 *　@param maxLength 表示最大文字数
 *　　　　　　　　　　　　　　　　未指定の場合は10文字（0文字は未設定扱い）
 *　@param suffix 接尾文字
 *　　　　　　　　　　　　　　未指定の場合は"･･･"
 */
function convertShortendString(obj,maxLength,suffix){

	// パラメータがセットされていなければ何も処理をしない
	if ( !obj ){
		return;
	}

	//　最大文字数が指定されていない　または　数字以外　の場合は10文字をセット
	if ( !maxLength || typeof maxLength != "number"){
		maxLength = 10;
	}

	//　接尾文字が指定されていない場合は"･･･"をセット(空文字""を許可させる)
	if ( suffix == undefined ){
		suffix = "･･･";
	}

	$(obj).each(function(i){
		var targetStr = $(this).text();
		if(targetStr.length > maxLength ){
			$(this).text( targetStr.slice(0, maxLength-1 ) + suffix );
		}
	});
}
