// 商品詳細画面のプレビューボタン用処理
function doPreviewGoods() {

    resetForm();

    // フォームから取得
    var inputPreviewDate = $("#previewDate").val();
    var inputPreviewTime = $("#previewTime").val();

    var errorFlag = false;

    // バリデータチェック
    if(!inputPreviewDate){
        // 日付未入力の場合
        bindingErrorMessageForNoEmptyDate();
        errorFlag = true;
    } else {
        // 日付部分のチェック
        if(Date.parse(new Date(inputPreviewDate + " " + "00:00:00")).toLocaleString()=="NaN") {
          // エラー時
    	    bindingErrorMessageForFraudDate();
    	    errorFlag = true;
         }
    }

    if(!inputPreviewTime){
        // 時刻未入力の場合はデフォルト値を設定
        inputPreviewTime = "00:00:00";
    }  else {
         // 日付部分のチェック
         if(Date.parse(new Date("2000/01/01" + " " + inputPreviewTime)).toLocaleString()=="NaN") {
            // エラー時
          	bindingErrorMessageForFraudTime();
          	errorFlag = true;
         }
    }

    if(errorFlag){
        return;
    }

    // 月/日が1桁入力の場合に0埋めを行ったあと、リクエストパラメータ用の値に変換
    var previewDateAry =  inputPreviewDate.split('/');
    if(previewDateAry.length != 3){
       bindingErrorMessageForFraudDate();
       return;
    }
    var previewDate = dateFormat(previewDateAry[0], previewDateAry[1], previewDateAry[2]).replace("/", "").replace("/", "");

    // 時/分/秒が1桁入力の場合に0埋めを行ったあと、リクエストパラメータ用の値に変換
    var previewTimeAry =  inputPreviewTime.split(':');
    if(previewTimeAry.length != 3){
       bindingErrorMessageForFraudTime();
       return;
    }
    var previewTime = timeFormat(previewTimeAry[0], previewTimeAry[1], previewTimeAry[2]).replace(":", "").replace(":", "");

    var cid = $("#cid").val();
    if(cid){
      location.href = pkg_common.getAppComplementUrl() + '/goods/detail?ggcd=' + $("#ggcd").val() + '&cid=' + cid + '&prekey=' + $("#preKey").val() + '&pretime=' + previewDate + previewTime;
    } else {
      location.href = pkg_common.getAppComplementUrl() + '/goods/detail?ggcd=' + $("#ggcd").val() + '&prekey=' + $("#preKey").val() + '&pretime=' + previewDate + previewTime;
    }
}

// 商品一覧画面のプレビューボタン用処理
function doPreviewGoodsList() {

    resetForm();

    // フォームから取得
    var inputPreviewDate = $("#previewDate").val();
    var inputPreviewTime = $("#previewTime").val();

    var errorFlag = false;

    // バリデータチェック
    if(!inputPreviewDate){
        // 日付未入力の場合
        bindingErrorMessageForNoEmptyDate();
        errorFlag = true;
    } else {
        // 日付部分のチェック
        if(Date.parse(new Date(inputPreviewDate + " " + "00:00:00")).toLocaleString()=="NaN") {
          // エラー時
    	    bindingErrorMessageForFraudDate();
    	    errorFlag = true;
         }
    }

    if(!inputPreviewTime){
        // 時刻未入力の場合はデフォルト値を設定
        inputPreviewTime = "00:00:00";
    }  else {
         // 日付部分のチェック
         if(Date.parse(new Date("2000/01/01" + " " + inputPreviewTime)).toLocaleString()=="NaN") {
            // エラー時
          	bindingErrorMessageForFraudTime();
          	errorFlag = true;
         }
    }

    if(errorFlag){
        return;
    }

    // 月/日が1桁入力の場合に0埋めを行ったあと、リクエストパラメータ用の値に変換
    var previewDateAry =  inputPreviewDate.split('/');
    if(previewDateAry.length != 3){
       bindingErrorMessageForFraudDate();
       return;
    }
    var previewDate = dateFormat(previewDateAry[0], previewDateAry[1], previewDateAry[2]).replace("/", "").replace("/", "");

    // 時/分/秒が1桁入力の場合に0埋めを行ったあと、リクエストパラメータ用の値に変換
    var previewTimeAry =  inputPreviewTime.split(':');
    if(previewTimeAry.length != 3){
       bindingErrorMessageForFraudTime();
       return;
    }
    var previewTime = timeFormat(previewTimeAry[0], previewTimeAry[1], previewTimeAry[2]).replace(":", "").replace(":", "");

    location.href = pkg_common.getAppComplementUrl() + '/goods/list?cid=' + $("#cid").val() + '&prekey=' + $("#preKey").val() + '&pretime=' + previewDate + previewTime;
}

// 日付フォーマット変換（yyyy/mm/dd）
function dateFormat(year, month, day){
	var zero = "0";
	// hh:mm:ss
	return year + "/" + (zero+month).slice(-2) + "/" + (zero+day).slice(-2);;
}

// 時刻フォーマット変換（hh:mm:ss）
function timeFormat(hour, minute, second){
	var zero = "0";
	// hh:mm:ss
	return (zero+hour).slice(-2) + ":" + (zero+minute).slice(-2) + ":" + (zero+second).slice(-2);;
}

// 日付用の必須バリデータメッセージ処理
function bindingErrorMessageForNoEmptyDate() {
    $("#previewDateErr").html('')
    $("#previewDateErr").append("<div id='previewDateError'></div>")
    $("#previewDateErr div").append("<ul></ul>")

    let itemErr = '<li>' + '日付を入力してください' + '</li>'
    $("#previewDateErr ul").append(itemErr);
    if ($("#previewDateErr ul li").length) {
        $("#previewDateErr").attr("style", "display:block")
    }
    $("#previewDate").addClass("error")
}

// 日付のフォーム入力値をDateに変換できなかった場合のバリデータメッセージ処理
function bindingErrorMessageForFraudDate() {
    $("#previewDateErr").html('')
    $("#previewDateErr").append("<div id='previewDateError'></div>")
    $("#previewDateErr div").append("<ul></ul>")

    let itemErr = '<li>' + '日付を正しく入力してください' + '</li>'
    $("#previewDateErr ul").append(itemErr);
    if ($("#previewDateErr ul li").length) {
        $("#previewDateErr").attr("style", "display:block")
    }
    $("#previewDate").addClass("error")
}

// 時間のフォーム入力値をDateに変換できなかった場合のバリデータメッセージ処理
function bindingErrorMessageForFraudTime() {
    $("#previewTimeErr").html('')
    $("#previewTimeErr").append("<div id='previewTimeError'></div>")
    $("#previewTimeErr div").append("<ul></ul>")

    let itemErr = '<li>' + '時間を正しく入力してください' + '</li>'
    $("#previewTimeErr ul").append(itemErr);
    if ($("#previewTimeErr ul li").length) {
        $("#previewTimeErr").attr("style", "display:block")
    }
    $("#previewTime").addClass("error")
}

// フォームリセット
function resetForm() {
    $("#previewDateErr").attr('style', 'display:none');
    $("#previewTimeErr").attr('style', 'display:none');
}