
$(function() {
    if(document.getElementById("headermenuPageItems-2") != null){
        // Ajaxを実行
        execAjaxHeaderMenu('#headermenuPageItems-2');
    }
    if(document.getElementById("headermenuPageItems-3") != null){
        // Ajaxを実行
    	execAjaxHeaderMenu('#headermenuPageItems-3');
    }
});

function execAjaxHeaderMenu(appendId){

	var viewLevel = "2";
	var cid = $(appendId).attr('cid');

    // プレビュー画面の場合、プレビュー日時を取得
    var preTime = null;
    var target = document.getElementById("preTime");
    if(target){
        preTime = target.value;
    }

	var hsn = (new URL(document.location)).searchParams.get('hsn');
	if(!hsn){
		hsn = "";
	}

	// Ajax通信を行う
	$.ajax({
		 type			:	"GET"
		,url			:	pkg_common.getAppComplementUrl() + "/getCategoryJsonData"
		,data		 	:	{"viewLevel":viewLevel, "cid":cid, "preTime":preTime}
		,dataType 	:	"json"
		,timeout		:	30000
	})
	.done(function(data){ setHeaderMenuHtml(data, appendId, cid); })
	.fail(function(data){ setHeaderMenuHtml(data, appendId, cid); })
	;
}

function setHeaderMenuHtml(resData, appendId, curCid) {

	if (!resData) {
		return;
	}

	var list = resData;
	if(list == null || list.length <= 0){
		$(appendId).css({display: 'none'});
		$(appendId+'_hideTarget').css({display: 'none'});
		return;
	}

	//カテゴリーツリー生成
	for(var i = 0, size = list.length; i < size ; i++){

		var appendHtml = '';

		//　第一階層
		if(list[i].categoryLevel == "1" && list[i].cid == curCid){

            appendHtml = '<a href="' + pkg_common.getAppComplementUrl() + '/goods/list?cid=' + list[i].cid + '&hsn=' + list[i].hierarchicalSerialNumber + '&isSide=true">'
                + '<svg viewBox="0 0 23 23" width="19">'
                + '<use xlink:href="/assets/images/icon/search.svg#search"></use>'
                + '</svg>'
                + '<p>' + list[i].displayName + '</p>'
                + '</a>';
			$(appendId).append(appendHtml);
		}

		//　第二階層
		if(list[i].categoryLevel == "2" && list[i].cidParent == curCid){
			if( list[i-1].categoryLevel == "1"){
                // ひとつ前のループが第一階層の場合（受領データはソート済みのため、以下の条件で、ループのひとつ前は必ず親になる）
                appendHtml =	'<ul class="l-header__global-menu" id="' + list[i].cidParent + '_parent"></ul>';
                $(appendId).append(appendHtml);
            }
            appendHtml = '<li id="' + list[i].cid + list[i].hierarchicalSerialNumber + '"><a href="' + pkg_common.getAppComplementUrl() + '/goods/list?cid=' + list[i].cid + '&hsn=' + list[i].hierarchicalSerialNumber + '&isSide=true">'
                + '<p>' + list[i].displayName + '</p>'
                + '</a></li>';

			$('#' + list[i].cidParent + '_parent').append(appendHtml);
		}
	}

	// プレビュー画面の場合はリクエストパラメータにプレビューキーとプレビュー日時を付与
    var preKey =  document.getElementById("preKey");
    var preTime = document.getElementById("preTime");
    if( preKey && preTime ){
    	$(appendId).find('a').each(function() {
            $(this).css({
                pointerEvents: "none",
            });
            $(this).parent().css({
                cursor: "not-allowed"
            })
    	});
    }
}
