/*-----------------------------------------------------------
index:
	Navigation Hover
	Calender Start End Setting
	Window Onload
-----------------------------------------------------------*/

/* Navigation Hover
-----------------------------------------------------------*/
(function($) {
	jQuery.fn.gnHover = function(){
		var bodyID = document.body.id;
		var bodyID_on  = "g_" + bodyID;
		$("#"+bodyID_on).addClass("active");
	};
})(jQuery);

(function($) {
	jQuery.fn.lnHover = function(){
		var bodyID = document.body.id;
		if(bodyID == "top") return;

		var menuID = $("body div:first-of-type").attr("id");
		var menuID_on  = "l_" + menuID;
		$("#"+menuID_on).addClass("active");

	};
})(jQuery);


/* Calender Start End Setting
-----------------------------------------------------------*/
/*
function customRange(input) {
	return {
		minDate: (input.id == "endDate" ? $("#startDate").datepicker("getDate") : null),
		maxDate: (input.id == "startDate" ? $("#endDate").datepicker("getDate") : null)
	};
}
*/

/* Window Onload
-----------------------------------------------------------*/
jQuery(function($){

	$("#globalNav").gnHover();
	$("#localNav").lnHover();

	/*ストライプテーブル*/
	$("table.storeStatusTable tr:even").addClass("stripe");
	$("table.dataListTable1 tr:even").addClass("stripe");
	$("table.dataListTable3 tr:even").addClass("stripe");

	$("table.dataListTable1 tr").mouseover(function(){
		$(this).addClass("over");
	}).mouseout(function(){
		$(this).removeClass("over");
	});

	$("table.dataListTable3 tr").mouseover(function(){
		$(this).addClass("over");
	}).mouseout(function(){
		$(this).removeClass("over");
	});

	$("table.dataListTable4 tr").mouseover(function(){
		$(this).addClass("over");
	}).mouseout(function(){
		$(this).removeClass("over");
	});

	/*ファイルツリー*/
	$("#browser").treeview({
		animated: "fast",
		persist: "location",
		collapsed: true,
		unique: true
	});

	/*カレンダー*/
	$("input.calender").datepicker();

	/*フォームにフォーカス*/
	$("input[type=text], textarea, input[type=password]").focus(function(){
		$(this).css("background-color", "#FFFFD9");
	}).blur(function(){
		$(this).css("background-color", "#FFFFFF");
	})

/*
	カレンダー　FromToに対する範囲指定をする （動きません）
	$("input.calenderStart,input.calenderEnd").datepicker({
		beforeShow: customRange,
	});
*/

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
		}, 1500);
	});

	/* ENTER プレスサブミットを防止する処理
-----------------------------------------------------------*/
	$(document).on("keydown", ":input:not(textarea, input[id=administratorPassWord], input[name=username])", function(event) {
		return event.key != "Enter";
	});

});

// 前方・ブラウザバックでの処理
window.addEventListener("pageshow", function (event) {
	var historyTraversal = event.persisted ||
		(typeof window.performance != "undefined" &&
			window.performance.navigation.type === 2);

	if (historyTraversal) {
		$('form').data('submitted', false)
	}
});

/* UploadFile ViewPath
-----------------------------------------------------------*/
function uploadfileViewPath(_name, _value){
	var filepath = _name + '_path';
	document.getElementsByName(filepath)[0].value = _value;

	if (fileSizeCheck(_name) === false) {
		const form = document.querySelector('form');
		form.reset();
	}

}

function fileSizeCheck(_name) {
	const files = document.getElementById(_name).files;
	var fileSize = 0;
	for (let i = 0; i <= files.length - 1; i++) {
		fileSize = fileSize + files[i].size;
	}
	fileSize = (fileSize / 1024 / 1024).toFixed(2);
	if (fileSize > 200) {
		openWarningDialogSize();
		return false;
	} else {
		return true;
	}
}

function openWarningDialogSize() {
	$.confirm({
		title: 'ファイルアップロード',
		content: 'アップロードしたファイルが最大サイズ（200MB）を超えていますので、<br/> アップロード処理を中止します。',
		boxWidth: '450px',
		useBootstrap: false,
		draggable: false,
		buttons: {
			cancel: {
				text: '閉じる',
				keys: ['esc'],
				action: function(){
					// 特にしない
				}
			}
		}
	});
}

function uploadfileViewPathForDetailsImage(_name, _value){

    // ループの「-」でidとindexを分割
    var array = _name.split("-");
    var filepath = array[0] + 'Path' + "-"  + array[1];

    // ループ込みのIDを手がかりに対象項目valueにセット
    document.getElementById(filepath).value = _value;
}

function uploadfileViewPathForUnitImage(_name, _value){

    // ループの「-」でidとindexを分割
    var array = _name.split("-");
    var filepath = array[0] + 'Path' + "-"  + array[1];

    // ループ込みのIDを手がかりに対象項目valueにセット
    document.getElementById(filepath).value = _value;
}

/* UploadImage Resize
-----------------------------------------------------------*/
function resizeImage(){

	$('.uploadImages img').each(function(){
		var item = this;
		var itemWidth = item.width;
		var itemHeight = item.height;
		var itemSrc = item.src;

		var linkSource = '<div class="photobox"><a href="' + itemSrc + '" class="thickbox" rel="uploadImages" title="' + itemWidth + 'x' + itemHeight + '"></a></div>';

		/*large size*/
		if (itemWidth > 200) {
			$(item).attr("width", 200);
			$(item).wrap(linkSource);
			return;
		}

		item.title = itemWidth + "x" + itemHeight;
	});

}
