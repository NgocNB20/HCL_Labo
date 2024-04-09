/** GMOトークン決済 */

/** GMOトークン取得通信正常終了コード */
var GMO_TOKEN_RESULTOK = "000";
/** クレジットカードの表示 */
var CREDIT = "CREDIT";
/** トークンを取得済みかどうか？ */
var isGotToken = false;
/** 親要素にerrorPartをセットするか */
var needParent = true;
/** セキュリティコードの必須チェックを実施するか */
var checkSecurityCode = true;

/**
 * 画面初期化 イベント割当、セレクトの選択肢準備、セレクトの初期値セット
 */
function initiate() {
	$(jQSelectorH.doNext).click(function() {
		return getToken(this);
	});
	$(jQSelectorH.origexpire1 + ":first option").clone().appendTo(jQSelectorH.expire1);
	$(jQSelectorH.origexpire2 + ":first option").clone().appendTo(jQSelectorH.expire2);
	$(jQSelectorH.expire1).val("");
	$(jQSelectorH.expire2).val("");
	$(jQSelectorH.securitycode).val("");
}

/**
 * トークン取得処理 needProcessの実装がページ個別に必要
 * @returns true=遷移可能
 */
function getToken(elem) {
	// 後続処理が必要か
	if (!needProcess(elem)) {
		return true;
	}
	if (checkSecurityCode && "" == getSelected().find(jQSelectorH.securitycode).val()) {
		errorHandle("121");
		return false;
	}
	Multipayment.init($(jQSelectorH.MulPay).val());
	var $selected = getSelected();
	var cardno = $selected.find(jQSelectorH.cardno).val();
	var expire = $selected.find(jQSelectorH.expire1).val()
			+ $selected.find(jQSelectorH.expire2).val();
	var securitycode = $selected.find(jQSelectorH.securitycode).val();
	var hash = {
		cardno : cardno,
		expire : expire,
		securitycode : securitycode,
		holdername : null
	};

	Multipayment.getToken(hash, callBackGetToken);
	return false;
}

/**
 * トークン取得コールバック
 */
function callBackGetToken(response) {

	if (response.resultCode != GMO_TOKEN_RESULTOK) {
		errorHandle(response.resultCode);
		return;
	}

	$(jQSelectorH.token).val(response.tokenObject.token);

	// カード情報移送
	$(jQSelectorH.origcardno)
			.val(maskCardNo(response.tokenObject.maskedCardNo));
	var $selected = getSelected();
	var expire1 = $selected.find(jQSelectorH.expire1).val();
	var expire2 = $selected.find(jQSelectorH.expire2).val();
	$(jQSelectorH.origexpire1).val(expire1);
	$(jQSelectorH.origexpire2).val(expire2);

	// カード情報削除
	$(jQSelectorH.cardno).val("");
	$(jQSelectorH.expire1).val("");
	$(jQSelectorH.expire2).val("");
	$(jQSelectorH.securitycode).val("");

	isGotToken = true;
	$(jQSelectorH.doNext).click();
}

/**
 * カード番号マスク関数
 * @param カード番号
 * @return String マスクされたカード番号文字列
 */
function maskCardNo(c) {
	ca = c.split("");
	for ( var i = 0, l = ca.length - 4; i < l; i++) {
		ca[i] = "*";
	}
	c = ca.join("");
	return c;
}

// GMOエラーコードデータ構造
var errorHash = {
	"100" : {
		message : "カード番号は必須です",
		selectors : [ jQSelectorH.cardno ]
	},
	"101" : {
		message : "正しいカード番号を入力してください",
		selectors : [ jQSelectorH.cardno ]
	},
	"102" : {
		message : "正しいカード番号を入力してください",
		selectors : [ jQSelectorH.cardno ]
	},
	"110" : {
		message : "有効期限は必須です",
		selectors : [ jQSelectorH.expire1, jQSelectorH.expire2 ]
	},
	"111" : {
		message : "有効期限を正しく入力してください",
		selectors : [ jQSelectorH.expire1, jQSelectorH.expire2 ]
	},
	"112" : {
		message : "有効期限を正しく入力してください",
		selectors : [ jQSelectorH.expire1, jQSelectorH.expire2 ]
	},
	"113" : {
		message : "有効期限の月を正しく入力してください",
		selectors : [ jQSelectorH.expire2 ]
	},
	"121" : {
		message : "セキュリティコードを正しく入力してください",
		selectors : [ jQSelectorH.securitycode ]
	},
	"122" : {
		message : "セキュリティコードを正しく入力してください",
		selectors : [ jQSelectorH.securitycode ]
	}
};

/**
 * エラー処理
 * @param エラーコード
 */
function errorHandle(errCode) {
	var html = $(jQSelectorH.templateError).text();
	var errorPart = "errorPart";
	$("." + errorPart).removeClass(errorPart);
	$(".inputError").remove();
	if (document.getElementById('notJsError') !== null) {
		$("#notJsError").children().remove();
		document.getElementById('notJsError').setAttribute("style", "display: none");
	}
	if (errorHash[errCode]) {
		html = html.replace("${error}", errorHash[errCode].message
				+ "&nbsp;(エラーコード：" + errCode + ")");
		for ( var i = 0, l = errorHash[errCode].selectors.length; i < l; i++) {
			$(errorHash[errCode].selectors[i]).addClass(errorPart);
		}
	} else {
		html = html.replace("${error}", "処理中にエラーが発生しました" + "&nbsp;(エラーコード："
				+ errCode + ")");
		if (needParent) {
			$(jQSelectorH.isCredit1).parent().parent(jQSelectorH.isCredit4)
					.addClass(errorPart);
		}
	}
	document.getElementById('jsError').setAttribute("style", "display: inherit");
	window.scrollTo({top: 0, behavior: 'smooth'});
	$(jQSelectorH.jsErrorDiv).html(html);
}
