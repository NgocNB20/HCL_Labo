// ID発番フラグ
var idFlg = false;
var TAB_ID = "quadTabID";
function forbidMultiTab() {
	var sesTabID = sessionStorage.getItem(TAB_ID);
	var lclTabID = localStorage.getItem(TAB_ID);

	var newAction = function() {
		var tabID = new Date().getTime();
		sessionStorage.setItem(TAB_ID, tabID);
		localStorage.setItem(TAB_ID, tabID);
		idFlg = true;
	};
	var currentAction = function() {
	};
	var oldAction = function() {
		location.href = location.protocol + '//' + location.host + pkg_common.getAppComplementUrl() + '/syncerror/';
	};

	// sessionStorageにタブIDが保存されていないとき
	// もしくはIE8対応として、「新しいタブで開く」から開くと、sessionStorageの値がコピーされているので、発番フラグが立っていないとき
	if (sesTabID == null || (sesTabID == lclTabID && !idFlg)) {
		newAction();
		// sessionStorageの値とlocalStorageの値が等しいとき
	} else if (sesTabID == lclTabID) {
		currentAction();
	} else if (idFlg && lclTabID == null) {
		// 別タブが開かれ、不正操作判定となり、発行フラグは立っているが、localStorageがクリアされた場合
		newAction();
	} else {
		oldAction();
	}
}

window.onunload = function() {
	//ページを離れた時にStorage初期化
	sessionStorage.removeItem(TAB_ID);
	localStorage.removeItem(TAB_ID);
}

jQuery(function() {
	forbidMultiTab();
	setInterval(forbidMultiTab, 250);
});