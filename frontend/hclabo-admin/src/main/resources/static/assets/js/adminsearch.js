/*
 * return getElementById()
 */
function $id(id) {
	return document.getElementById(id);
}

/*
 * 対象プルダウンの選択値をセット
 * @param select 元セレクトオブジェクト
 * @param id 先セレクトのid
 */
function valueSet(select, id) {
	var targetSelect = $id(id);
	if (targetSelect != null) {
		targetSelect.options[select.selectedIndex].selected = "selected";	
	}
}

/*
 * 指定ボタンをクリック
 *
 * @param buttonId ボタンのid
 */
function actionExe(buttonId){
	var targetButton = $id(buttonId);
	if (targetButton != null) {
		targetButton.click();
	}
}

/*
 * 表示件数切り替えアクション
 *
 * @param select limitプルダウン
 * @param buttonId ボタンId
 */
function limitActionExe(select, buttonId) {
	actionExe(buttonId);	
}

/*
 * ソート項目をhiddenにセットし、指定ボタンをクリックする
 *
 * @param link ソート項目のリンクオブジェクト
 * @param buttonId ボタンのid
 */
function sortActionExe(link, buttonId){

	try{
	
		// 現ソートフィールド保持
		var sort = $id('orderField');
		 
		// 現ソート順フラグ保持
		var sortAsc = $id('orderAsc');

		// 初ソート			
		if (sort.value == null || sort.value == '') {
			var id = link.id;
			var sortId = id.replace('header', '');
			sort.value = sortId.substring(0,1).toLowerCase() + sortId.substring(1);
			sortAsc.value = 'true';
			
		// ソート変更
		} else {

			// 現ソートフィールド保持
			var pageNumberField = $id('pageNumber');
			if (pageNumberField != null) {
				pageNumberField.value = 1;// 1ページをセット
			}

			var sortValue = 'header' + sort.value.substring(0,1).toUpperCase() + sort.value.substring(1);

			// 昇順・降順の切り替え
			if (sortValue == link.id) {
			
				if (sortAsc.value == 'true') {
					sortAsc.value = 'false';
				} else {
					sortAsc.value = 'true';
				}
			
			// ソートフィールドの切り替え
			} else {
				var id = link.id;
				var sortId = id.replace('header', '');
				sort.value = sortId.substring(0,1).toLowerCase() + sortId.substring(1);
				sortAsc.value = 'true';
			}
		}

		// 検索の実行
		actionExe(buttonId);
		
	} catch (e) {
		alert('ソートに失敗 = ' + e.message);
	}
	
	// リンク先に飛ばないようにする
	return false;
}

/*
 * ページ番号をhiddenにセットし、指定ボタンをクリックする
 *
 * @param pageNumber ページ番号
 * @param buttonId ボタンのid
 */
function pageLinkActionExe(pageNumber, buttonId){

	try{
		// 現ソートフィールド保持
		var pageNumberField = $id('pageNumber');
		if (pageNumberField != null) {
			pageNumberField.value = pageNumber;
		}

		// 検索の実行
		actionExe(buttonId);
		
	} catch (e) {
		alert('エラーです = ' + e.message);
	}
	
	// リンク先に飛ばないようにする
	return false;
}
 
/*
 * チェックボックス全てチェック
 *
 * @param formName フォーム名
 * @param itemsName Items名
 * @param checkboxName チェックボックス名
 */
function allCheck(formName, itemsName, checkboxName){
	var startIndex = 0
	var endIndex = 100; // デフォルト値 プルダウンMax値
	while (true) {
//		var id = formName + ':' + itemsName + ':' + startIndex + ':' + checkboxName;
		var id = checkboxName + '-' + startIndex;
		var checkbox = $id(id);
		if (checkbox == null) {
			return false;
		}
		checkbox.checked = 'checked';
		startIndex = startIndex + 1;
		if (startIndex >= endIndex) {
			return false;
		}
	}
}

/*
 * チェックボックス全てチェックをはずす
 *
 * @param formName フォーム名
 * @param itemsName Items名
 * @param checkboxName チェックボックス名
 */
function allUnCheck(formName, itemsName, checkboxName){ 
	var startIndex = 0
	var endIndex = 100; // デフォルト値 プルダウンMax値
	while (true) {
//		var id = formName + ':' + itemsName + ':' + startIndex + ':' + checkboxName;
		var id = checkboxName + '-' + startIndex;
		var checkbox = $id(id);
		if (checkbox == null) {
			return false;
		}
		checkbox.checked = '';
		startIndex = startIndex + 1;
		if (startIndex >= endIndex) {
			return false;
		}
	}
}

/*
 * チェックボックスのチェックされているvalue値の配列を作成する
 *
 * @param checkSeqArrayId SEQ配列のフィールドのId
 * @param resultSeqId 取得するSEQのId
 */
function setCheckSeqArray(checkSeqArrayId, checkboxName, resultSeqId){

	// セットするオブジェクト取得
	var checkSeqArray = $id(checkSeqArrayId);
	if (checkSeqArray == null) {
		return;
	}
	
	var value = '';
	var startIndex = 0
	var endIndex = 100; // デフォルト値 プルダウンMax値
	while (true) {
//		var id = formName + ':' + itemsName + ':' + startIndex + ':' + checkboxName;
		var id = checkboxName + '-' + startIndex;
		var checkbox = $id(id);
		if (checkbox == null) {
			break;
		}

		// チェックされているもの
		if (checkbox.checked == true) {

			// SEQ取得		
			var seq = $id(resultSeqId + '-' + startIndex);
			if (seq == null) {
				continue;
			}

			// valueにセット
			if (value != '') {
				value = value + ',';
			}
			value = value + seq.value;
		}
		
		startIndex = startIndex + 1;
		if (startIndex >= endIndex) {
			break;
		}
	}
	
	// 値をセット
	checkSeqArray.value = value;
}

/*
 * 期間区分を使用した期間選択用スクリプト
 *
 *　key  期間区分　name 
 * From 期間-From　id
 * To   期間-To   id
 */
function changePeriod(key,From,To){
    if (document.getElementById(key).checked) {
    	$("#"+From).prop('disabled', false);
    	$("#"+To).prop('disabled', false);
    	
    }else{
    	$("#"+From).prop('disabled', true);
    	$("#"+To).prop('disabled', true);
    }
}

/* 
 * 日付フィールドにモード毎の日付を設定する。
 * 時刻フィールドにモード毎の時刻を設定する。
 * 検索条件の期間で利用される事を想定する。
 *　
 *　【モード】
 *　・今日（today）
 *　・昨日（yesterday）
 *　・先月（crrentMonth）
 *　・今月（prevMonth）
 *　・クリア（clear）
 *　 
 *　@param mode 日付セットモード
 *　@param dateFromId 日付 From のID
 *　@param dateToId 日付 To のID
 *　@param format フォーマット形式（day - 「yyyy/mm/dd」 / month - 「yyyy/mm」）
 *　@param timeFromId 時刻 From のID
 *　@param timeToId 時刻 To のID
 */
function setTargetDate(mode, dateFromId, dateToId, format, timeFromId, timeToId) {

	// 現在日時を変数に格納
	var currentDate = new Date();
	var year = currentDate.getFullYear();
	var month = currentDate.getMonth();
	var day = currentDate.getDate();

	var resultDateFrom = null;
	var resultDateTo = null;
	var firstDay = "1";
	var resultTimeFrom = null;
	var resultTimeTo = null;
	var firstTime = "00:00";
	var lastTime = "23:59";
	if ("today" == mode) {
		/* 今日 */
		var today = dateFormat(year, month, day, format);
		resultDateFrom = today;
		resultDateTo = today;
		resultTimeFrom = firstTime;
		resultTimeTo = lastTime;
	} else if　("yesterday" == mode) {
		/* 昨日 */ 
		var pre = new Date(year, month,　day-1);
		// 日付フォーマット
		var yesterday = dateFormat(pre.getFullYear(), pre.getMonth(), pre.getDate(), format);
		resultDateFrom = yesterday;
		resultDateTo = yesterday;
		resultTimeFrom = firstTime;
		resultTimeTo = lastTime;
	} else if　("crrentMonth" == mode) {
		/* 今月 */
		//　今月末日取得
		var lastDay  = new Date(year,　month+1,　0).getDate();
		// 日付フォーマット
		resultDateFrom = dateFormat(year, month, firstDay, format);
		resultDateTo = dateFormat(year, month, lastDay, format);
		resultTimeFrom = firstTime;
		resultTimeTo = lastTime;
	} else if　("prevMonth" == mode) {
		/* 先月 */
		//　先月末日取得
		var prevDate = new Date(year,　month,　0);
		// 日付フォーマット
		resultDateFrom = dateFormat(prevDate.getFullYear(), prevDate.getMonth(), firstDay, format);
		resultDateTo = dateFormat(prevDate.getFullYear(), prevDate.getMonth(), prevDate.getDate(), format);
		resultTimeFrom = firstTime;
		resultTimeTo = lastTime;
	} else if　("clear" == mode) {
		/* クリア */
		// デフォルト値をセットするので、処理なし
	}
	
	// 日付フィールドに設定		
	$("#" + dateFromId).val(resultDateFrom);
	$("#" + dateToId).val(resultDateTo);
	// 時刻フィールドに設定
	if (timeFromId != "") {
		$("#" + timeFromId).val(resultTimeFrom);
	}
	if (timeToId != "") {
		$("#" + timeToId).val(resultTimeTo);
	}
}

/* 日付フォーマットを行う。
 * @param year 年
 *　@param month 月
 *　@param month 日
 *　@param format フォーマット形式（day - 「yyyy/mm/dd」 / month - 「yyyy/mm」）
 * @param parse 区切り文字
 */
function dateFormat(year, month, day, format, parse){
	//　デフォルト区切り文字
	var defaultParse = "/";
	if(parse == undefined || parse == ""){
		parse = defaultParse;
	}
	//　デフォルトフォーマット形式
	var defaultFormat = "day";
	if(format == undefined || format == ""){
		format = defaultFormat;
	}
	// 指定フォーマットに変換
	var zero = "0";
	var createFormatDate = null;
	
	if("day" == format){
		/* yyyy/mm/dd */
		createFormatDate = year + parse + (zero + (month + 1)).slice(-2) + parse + (zero + day).slice(-2);
	} else if("month" == format) {
		/* yyyy/mm */
		createFormatDate = year + parse + (zero + (month + 1)).slice(-2);
	}
	
	return createFormatDate;
}

/*
 * チェックボックス全てチェック(100レコード分必ず処理する)
 *
 * @param formName フォーム名
 * @param itemsName Items名
 * @param checkboxName チェックボックス名
 */
function allCheckDefaultCount(formName, itemsName, checkboxName){
	var startIndex = 0
	var endIndex = 100; // デフォルト値 プルダウンMax値
	while (true) {
		var id = checkboxName + '-' + startIndex;
		var checkbox = $id(id);
		if (checkbox != null) {
			checkbox.checked = 'checked';
		}
		startIndex = startIndex + 1;
		if (startIndex >= endIndex) {
			return false;
		}
	}
}

/*
 * チェックボックス全てチェックをはずす(100レコード分必ず処理する)
 *
 * @param formName フォーム名
 * @param itemsName Items名
 * @param checkboxName チェックボックス名
 */
function allUnCheckDefaultCount(formName, itemsName, checkboxName){ 
	var startIndex = 0
	var endIndex = 100; // デフォルト値 プルダウンMax値
	while (true) {
		var id = checkboxName + '-' + startIndex;
		var checkbox = $id(id);
		if (checkbox != null) {
			checkbox.checked = '';
		}
		startIndex = startIndex + 1;
		if (startIndex >= endIndex) {
			return false;
		}
	}
}