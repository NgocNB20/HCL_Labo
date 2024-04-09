/*
 * 郵便番号検索クラス
 */
var Zipsearch = function() {
	// 値はzipCodeSearchメソッドの中でセット
	this.prefectureObj;
	this.address1Obj;
	this.address2Obj;
	this.address3Obj;
	this.zipErrorMsgObj;
}

/*
 * 郵便番号検索クラスのメソッド
 */
Zipsearch.prototype = {

	/* 郵便番号検索処理(都道府県情報はコードで取得)
	 * flagの説明：
	 * 　2入力欄の場合trueを、1入力欄の場合falseを指定
	 * arg1とarg2の説明：
	 * 　2入力欄の場合、arg1に前入力欄を、arg2に後入力欄を指定
	 * 　1入力欄の場合、arg1に入力欄を、
	 * 　arg2には同画面に複数の郵便番号検索がある場合のみエラーメッセージObjを指定
	 */
	zipCodeSearchPrefectureCodeReturn : function(flag,pre,ad1,ad2,ad3,arg1,arg2) {
	 	zipCodeFromAddressSearch(flag,pre,ad1,ad2,ad3,arg1,arg2,true);
	},

	/* 郵便番号検索処理(都道府県情報は名称で取得)
	 * flagの説明：
	 * 　2入力欄の場合trueを、1入力欄の場合falseを指定
	 * arg1とarg2の説明：
	 * 　2入力欄の場合、arg1に前入力欄を、arg2に後入力欄を指定
	 * 　1入力欄の場合、arg1に入力欄を、
	 * 　arg2には同画面に複数の郵便番号検索がある場合のみエラーメッセージObjを指定
	 */
	 zipCodeSearchPrefectureNameReturn : function(flag,pre,ad1,ad2,ad3,arg1,arg2) {
	 	zipCodeFromAddressSearch(flag,pre,ad1,ad2,ad3,arg1,arg2,false);
	},

	/* 郵便番号検索ボタン表示切り替え処理
	 * pageNameの説明：
	 * 　管理者＞受注修正画面から呼び出す場合は「orderChange」を指定
	 * 　その他の画面から呼び出す場合は引数なし
	 */
	switchZipCodeButton : function(pageName) {
		if (pageName == 'orderChange') {
			var buttonOObj = document.getElementById('doZipCodeSearchToOrder');
			var ajaxButtonOObj = document.getElementById('doZipCodeSearchToOrderAjax');
			if (buttonOObj != null && ajaxButtonOObj != null) {
				buttonOObj.style.display = 'none';
				ajaxButtonOObj.style.display = 'inline';
			}
			var orderReceiverSize;
			var buttonRObj;
			var ajaxButtonRObj;
			if (document.getElementById('orderReceiverSize')) {
				orderReceiverSize = document.getElementById('orderReceiverSize').value;
				for (var i = 0; i < orderReceiverSize; i++) {
					buttonRObj = document.getElementById('doZipCodeSearchToReceiver-' + i);
					ajaxButtonRObj = document.getElementById('doZipCodeSearchToReceiverAjax-' + i);
					if (buttonRObj != null && ajaxButtonRObj != null) {
						buttonRObj.style.display = 'none';
						ajaxButtonRObj.style.display = 'inline';
					}
				}
			} else {
				buttonRObj = document.getElementById('doZipCodeSearchToReceiver');
				ajaxButtonRObj = document.getElementById('doZipCodeSearchToReceiverAjax');
				if (buttonRObj != null && ajaxButtonRObj != null) {
					buttonRObj.style.display = 'none';
					ajaxButtonRObj.style.display = 'inline';
				}
			}
		} else {
			var buttonObj = document.getElementById('doZipCodeSearch');
			var ajaxButtonObj = document.getElementById('doZipCodeSearchAjax');
			if (buttonObj != null && ajaxButtonObj != null) {
			    buttonObj.style.display = 'none';
			    ajaxButtonObj.style.display = 'inline';
			}
		}
	}
};

function zipCodeFromAddressSearch(flag,pre,ad1,ad2,ad3,arg1,arg2,prefectureFlg) {
	// 郵便番号の値を取得
	var zipCode = '';
	if (flag) {
		var zip1Obj = arg1;
		var zip2Obj = arg2;
		zipCode = zip1Obj.value + zip2Obj.value;
	} else {
		var zipCodeObj = arg1;
		zipCode = zipCodeObj.value;
	}
	// 引数で渡されたobjectをインスタンス変数にセット
	setAddresInfoObjects(pre,ad1,ad2,ad3,flag,arg2);
	// 入力チェックでエラーがない場合
	if (!hasInputError(zipCode)){
		// 全角数字を半角数字に変換しセット
		if (flag) {
			zip1Obj.value = convertNumberHankaku(zip1Obj.value);
			zip2Obj.value = convertNumberHankaku(zip2Obj.value);
			zipCode = zip1Obj.value + zip2Obj.value;
		} else {
			zipCodeObj.value = convertNumberHankaku(zipCode);
			zipCode = zipCodeObj.value
		}
		// Ajax用の郵便番号検索クラスを呼ぶ
		$.ajax({
			 type     : "GET"
			,url      : pkg_common.getAppComplementUrl() + "/zipcodeSearch"
			,dataType : "json"
			,data     : {"zipCode":zipCode, "prefectureFlg":prefectureFlg}
			,timeout  : 30000
		})
			.done(function(data){setValue(data);})
			.fail(function(data){
				if (data && data.status && data.status === 403) {
					location.href = pkg_common.getAppComplementUrl() + '/login/'
				}
				setValue(data);})
		;
	}
}

/* Ajax用の郵便番号検索クラスの戻り値をhtmlにセットします */
function setValue(response){
	// エラーメッセージがない場合、各住所入力欄に値をセット、エラーをクリア
	if (response.zipErrorMsg == null) {
		zipsearch.prefectureObj.value = response.prefecture;
		zipsearch.address1Obj.value = response.address1;
		zipsearch.address2Obj.value = response.address2;
		zipsearch.address3Obj.value = response.address3;
		zipsearch.zipErrorMsgObj.innerHTML = '';
	// エラーメッセージがある場合、エラーメッセージをセット
	} else {
		zipsearch.zipErrorMsgObj.innerHTML  = '<div class="c-error-txt mt5">' + response.zipErrorMsg + '</div>';
	}
}

/* 入力チェックをします　true...エラーあり、false...エラーなし */
function hasInputError(zipCode){
	// 必須チェック
	if (zipCode == '') {
		zipsearch.zipErrorMsgObj.innerHTML = '<div class="c-error-txt mt5">郵便番号を入力してください。<br /></div>';
		return true;
	}
	// 7桁数字チェック
	if (zipCode.search(/[0-9０-９]{7}/) == -1) {
		zipsearch.zipErrorMsgObj.innerHTML = '<div class="c-error-txt mt5">郵便番号が不正です。<br /></div>';
		return true;
	}
	return false;
}

/* 全角数字を半角数字に変換して返します */
function convertNumberHankaku(str){
	str = str.replace(/０/g,"0");
	str = str.replace(/１/g,"1");
	str = str.replace(/２/g,"2");
	str = str.replace(/３/g,"3");
	str = str.replace(/４/g,"4");
	str = str.replace(/５/g,"5");
	str = str.replace(/６/g,"6");
	str = str.replace(/７/g,"7");
	str = str.replace(/８/g,"8");
	str = str.replace(/９/g,"9");
	return str;
}

/* 住所入力欄とエラーメッセージのobjectをインスタンス変数セット */
function setAddresInfoObjects(pre,ad1,ad2,ad3,flag,arg2){
	zipsearch.prefectureObj=pre;
	zipsearch.address1Obj=ad1;
	zipsearch.address2Obj=ad2;
	zipsearch.address3Obj=ad3;
	if (flag || arg2 == undefined) {
		zipsearch.zipErrorMsgObj=document.getElementById('zipErrorMsg');
	} else {
		zipsearch.zipErrorMsgObj=arg2;
	}
}
