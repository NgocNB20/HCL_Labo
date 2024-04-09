/**
 * 受注修正画面用JavaScriptメソッド
 */

/**
 * お届け時間帯情報の表示切替(Ajax呼び出し)
 */
function setSelectChangeTimeZone(deliveryMethodSeq) {
	// Ajax
	$.ajax({
		type: "GET"
		, url: pkg_common.getAppComplementUrl() + "/order/detailsupdate/ajax?deliveryMethodSeq=" + deliveryMethodSeq
		, dataType: "html"
		, data: null
		, timeout: 30000
	})
		.done(function (data) {
			if (data.substring(1, 7).toUpperCase() !== "SELECT") {
				location.reload();
			} else {
				orderReceiverTimeZoneCreateAjax_ajaxChangeTimeZone(data);
			}
		})
		.fail(function (data) {
			if (data && data.status && data.status === 403) {
				location.href = pkg_common.getAppComplementUrl() + '/login/'
			}
		})
	;
}

/**
 * お届け時間帯情報の表示切替(Ajax呼び出し)
 */
function orderReceiverTimeZoneCreateAjax_ajaxChangeTimeZone(res){
	if (res == "選択不可") {
		$id('deliverySelect').classList.add("mt10");
	} else {

		$id('deliverySelect').classList.remove("mt10");
	}
	$id('deliverySelect').innerHTML = res;
	// 初期ロード時のみ、選択していたお届け時間帯を選択状態にする
	$('#receiverTimeZone').val("");

	if ($('#deliveryTime').find('.c-error-txt').length > 0) {
		$("#receiverTimeZone").addClass("error");
	}
	return false;
}

/**
 * お届け時間帯情報の表示切替
 */
function changeTimeZone(){
	var delivery = $id('updateDeliveryMethodSeq').value;
	setSelectChangeTimeZone(delivery);
	return false;
}

/**
 * 決済方法変更時の決済情報の表示切替
 */
function changeDisplayCreditConveni(){
	var settlement = $id('updateSettlementMethodSeq').value;
	var originalSettlement = $id('originalSettlementMethodSeq').value;
	var credit = $id('creditSettlementMethodSeqList').value.split(',');
	var result = "";

	var settlementMethodList = [];
	settlementMethodList.push(['credit', credit]);
	$.each(settlementMethodList, function(i, settlementMethod){
		$.each(settlementMethod[1], function(j, settlementMethodSeq){
			if (settlementMethodSeq == settlement) {
				result = settlementMethod[0];
				return false;
			}
		})
	});

	$(".settlement_credit").hide();

	switch(result) {
	case "credit":
		$(".settlement_credit").show();
		break;
	}
}

function changeMailRequired(){
	var settlement = $id('updateSettlementMethodSeq').value;
	var mailRequired = $id('mailRequiredSettlementMethodSeqList').value.split(',');
	var result = "";
	for (i=0;i<mailRequired.length;i++) {
		if (!mailRequired[i])
			continue;
		if (mailRequired[i] == settlement) {
			result = "mailRequired";
		}
	}
	if (result == "mailRequired") {
		$("#preClaim").fadeIn("fast");
	} else {
		$("#preClaim").fadeOut("fast");
	}
}

// enable receiver data.
function disableReceiverData(){
	$("[id^='receiverLastName-']").prop("disabled", false);
	$("[id^='receiverLastKana-']").prop("disabled", false);
	$("[id^='receiverTel-']").prop("disabled", false);
	$("[id^='receiverFirstName-']").prop("disabled", false);
	$("[id^='receiverFirstKana-']").prop("disabled", false);
	$("[id^='receiverZipCode-']").prop("disabled", false);
	$("[id^='receiverPrefecture-']").prop("disabled", false);
	$("[id^='receiverAddress1-']").prop("disabled", false);
	$("[id^='receiverAddress2-']").prop("disabled", false);
	$("[id^='receiverAddress3-']").prop("disabled", false);
	$("[id^='doZipCodeSearchToReceiverAjax-']").css("pointerEvents", "auto");
	$("[id^='doZipCodeSearchToReceiverAjax-']").css("opacity", "1");
	$("[id^='doZipCodeSearchToReceiverAjax-']").css("filter", "alpha(opacity=100)"); // IE fallback
}

/**
 * 決済方法変更のイベントを設定する<br/>
 */
function setSettlementMethodChangeEvent() {
	// 決済方法セレクトボックスが存在すればイベントをセット
	if ($id("updateSettlementMethodSeq") == null) {
		// 決済方法セレクトボックスが存在しないので処理を終了
		return;
	}

	// 決済方法を変更した場合のイベントを設定
	changeMailRequired();
}
