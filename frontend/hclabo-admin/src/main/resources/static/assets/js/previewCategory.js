$(function () {
    // カテゴリー検索画面からのプレビューボタン押下
    $("button[id^='category-preview_btn']").click(function() {
        // 検索条件にプレビュー日時が設定されているか
        var preTime = document.getElementById("previewDate");
        if(!preTime || preTime.value == ""){
            // 設定されていない場合はダイアログを表示
            showAlertDialog($(this).data("cid"));
        }
        else{
            // 設定されている場合は直接プレビュー画面を表示
            doPreview($(this).data("cid"));
        }
    });
    // カテゴリー登録更新画面からのプレビューボタン押下
    $("button[id='category-details_preview_btn']").click(function() {
        doPreviewByCategoryDetails();
    });

    // メニュー管理画面からのプレビューボタン押下
    $("button[id='menu_preview_btn']").click(function() {
        doPreviewByMenu();
    });
})

function showAlertDialog(cid) {
    $('#preview_dialog').dialog({
        modal: true,
        title: "プレビュー日時",
        width: 310,
        minHeight: 200,
        buttons: [{
            text: "プレビューを確認する",
            id: "doConfirmPreview",
            class: "c-btn btn_back",
            click: function () {
                doConfirmPreview(cid);
            }
        }],
        open: function () {
            $("#doConfirmPreview")
                .removeClass("ui-button ui-corner-all ui-widget")
                .parent().parent().attr("style", "justify-content: left; padding: 30px 15px;");
            $('input').blur();
            $("#ui-datepicker-div").hide();
        },
        close: function () {
            resetFormForDialog();
        }
    });
}

// プレビューを確認するボタン押下
function doConfirmPreview(cid) {
    let websiteUrl = $('#websiteUrl').val()
    $.ajax({
        type: "POST",
        url: pkg_common.getAppComplementUrl() + "/goods/category/doConfirmPreviewAjax",
        beforeSend: function (xhr) {
            xhr.setRequestHeader($('#_csrf_header').attr('content'), $('#_csrf').attr('content'));
        },
        processData: false,
        contentType: false,
        dataType: "json",
        data: getPreviewValueForDialog(),
        timeout: 30000
    }).done(function (data) {
        // 別タブで遷移
        window.open(new URL(websiteUrl).origin + '/goods/list?cid=' + cid + '&prekey=' + data.preKey + '&pretime=' + data.preTime, '_blank');
        resetFormForDialog();
        $("#preview_dialog").dialog("close");
    }).fail(function (data) {
        if (data && data.status && data.status === 403) {
            location.href = pkg_common.getAppComplementUrl() + '/goods/category/'
        }
        if (data && data.status && data.status === 500) {
            location.href = pkg_common.getAppComplementUrl() + data.responseText;
        }
        if (data.responseJSON && data.responseJSON.length > 0) {
            bindingErrorMessageForDialog(data.responseJSON)
        }
    })
}

// 検索結果からのプレビューボタン押下(直接遷移)
function doPreview(cid) {
    let websiteUrl = $('#websiteUrl').val()
    $.ajax({
        type: "POST",
        url: pkg_common.getAppComplementUrl() + "/goods/category/doPreviewAjax",
        beforeSend: function (xhr) {
            xhr.setRequestHeader($('#_csrf_header').attr('content'), $('#_csrf').attr('content'));
        },
        processData: false,
        contentType: false,
        dataType: "json",
        timeout: 30000
    }).done(function (data) {
        // 別タブで遷移
        window.open(new URL(websiteUrl).origin + '/goods/list?cid=' + cid + '&prekey=' + data.preKey + '&pretime=' + data.preTime, '_blank');
    }).fail(function (data) {
        if (data && data.status && data.status === 403) {
            location.href = pkg_common.getAppComplementUrl() + '/goods/category/'
        }
        if (data && data.status && data.status === 500) {
            location.href = pkg_common.getAppComplementUrl() + data.responseText;
        }
        if (data.responseJSON && data.responseJSON.length > 0) {
            bindingErrorMessage(data.responseJSON)
        }
    })
}

// カテゴリー登録更新画面からのプレビューボタン押下
function doPreviewByCategoryDetails() {
    let websiteUrl = $('#websiteUrl').val()
    $.ajax({
        type: "POST",
        url: pkg_common.getAppComplementUrl() + "/goods/category/input/doPreviewAjax",
        beforeSend: function (xhr) {
            xhr.setRequestHeader($('#_csrf_header').attr('content'), $('#_csrf').attr('content'));
        },
        processData: false,
        contentType: false,
        dataType: "json",
        data: getPreviewValue(),
        timeout: 30000
    }).done(function (data) {
        // 別タブで遷移
        window.open(new URL(websiteUrl).origin + '/goods/list?cid=' + data.categoryId + '&prekey=' + data.preKey + '&pretime=' + data.preTime, '_blank');
        resetForm();
    }).fail(function (data) {
        if (data && data.status && data.status === 403) {
            location.href = pkg_common.getAppComplementUrl() + '/goods/category/'
        }
        if (data && data.status && data.status === 500) {
            location.href = pkg_common.getAppComplementUrl() + data.responseText;
        }
        if (data.responseJSON && data.responseJSON.length > 0) {
            bindingErrorMessage(data.responseJSON)
        }
    })
}

// メニュー管理画面からのプレビューボタン押下
function doPreviewByMenu() {
    let websiteUrl = $('#websiteUrl').val()
    $.ajax({
        type: "POST",
        url: pkg_common.getAppComplementUrl() + "/goods/category/sidemenu/doPreviewAjax",
        beforeSend: function (xhr) {
            xhr.setRequestHeader($('#_csrf_header').attr('content'), $('#_csrf').attr('content'));
        },
        processData: false,
        contentType: false,
        dataType: "json",
        data: getPreviewValue(),
        timeout: 30000
    }).done(function (data) {
        // 別タブで遷移
        window.open(new URL(websiteUrl).origin + '/goods/list?cid=' + data.cid + '&prekey=' + data.preKey + '&pretime=' + data.preTime, '_blank');
        resetForm();
    }).fail(function (data) {
        if (data && data.status && data.status === 403) {
            location.href = pkg_common.getAppComplementUrl() + '/goods/'
        }
        if (data && data.status && data.status === 500) {
            location.href = pkg_common.getAppComplementUrl() + data.responseText;
        }
        if (data.responseJSON && data.responseJSON.length > 0) {
            bindingErrorMessage(data.responseJSON)
        }
    })
}

// フォームリセット
function resetFormForDialog() {
    $("#errorMessages").html('').attr('style', 'display:none');
    $("#dialogPreviewDateErr").attr('style', 'display:none');
    $("#dialogPreviewTimeErr").attr('style', 'display:none');
    $("#dialogPreviewDate").removeClass("error").val('');
    $("#dialogPreviewTime").removeClass("error").val('');
}

// フォームリセット
function resetForm() {
    $("#errorMessages").html('').attr('style', 'display:none');
    $("#previewDateErr").attr('style', 'display:none');
    $("#previewTimeErr").attr('style', 'display:none');
    $("#previewDate").removeClass("error");
    $("#previewTime").removeClass("error");
}

// ダイアログの項目をフォーム値に設定
function getPreviewValueForDialog() {
    let form = new FormData();
    form.append("dialogPreviewDate", $("#dialogPreviewDate").val())
    form.append("dialogPreviewTime", $("#dialogPreviewTime").val())
    return form
}

// プレビュー日時の項目をフォーム値に設定
function getPreviewValue() {
    let form = new FormData();
    form.append("previewDate", $("#previewDate").val())
    form.append("previewTime", $("#previewTime").val())
    return form
}

// バリデータメッセージ処理
function bindingErrorMessageForDialog(error) {
    // 初期化
    $("#dialogPreviewDate").removeClass("error");
    $("#dialogPreviewTime").removeClass("error");
    // ここから設定
    $("#errorMessages").html('')
    $("#dialogPreviewDateErr").html('')
    $("#dialogPreviewTimeErr").html('')
    if (error && error.length > 0) {
        $("#errorMessages").append("<ul></ul>")
        $("#dialogPreviewDateErr").append("<div id='dialogPreviewDateError'></div>")
        $("#dialogPreviewDateErr div").append("<ul></ul>")
        $("#dialogPreviewTimeErr").append("<div id='dialogPreviewTimeError'></div>")
        $("#dialogPreviewTimeErr div").append("<ul></ul>")
        error.forEach(e => {
            if (e.field == 'globalMessage') {
                let itemErr = '<li>' + e.message + '</li>'
                $("#errorMessages ul").append(itemErr);
                if ($("#errorMessages ul li").length) {
                    $("#errorMessages").attr("style", "display:block")
                }
            }
            if (e.field == 'dialogPreviewDate') {
                let itemErr = '<li>' + e.defaultMessage + '</li>'
                $("#dialogPreviewDateErr ul").append(itemErr);
                if ($("#dialogPreviewDateErr ul li").length) {
                    $("#dialogPreviewDateErr").attr("style", "display:block")
                }
                $("#dialogPreviewDate").addClass("error")
            }
            if (e.field == "dialogPreviewTime") {
                let itemErr = '<li>' + e.defaultMessage + '</li>'
                $("#dialogPreviewTimeErr ul").append(itemErr);
                if ($("#dialogPreviewTimeErr ul li").length) {
                    $("#dialogPreviewTimeErr").attr("style", "display:block")
                }
                $("#dialogPreviewTime").addClass("error")
            }
        })
    }
}

// バリデータメッセージ処理
function bindingErrorMessage(error) {
    // 初期化
    $("#previewDate").removeClass("error");
    $("#previewTime").removeClass("error");
    // ここから設定
    $("#errorMessages").html('')
    $("#previewDateErr").html('')
    $("#previewTimeErr").html('')
    if (error && error.length > 0) {
        $("#errorMessages").append("<ul></ul>")
        $("#previewDateErr").append("<div id='previewDateError'></div>")
        $("#previewDateErr div").append("<ul></ul>")
        $("#previewTimeErr").append("<div id='previewTimeError'></div>")
        $("#previewTimeErr div").append("<ul></ul>")
        error.forEach(e => {
            if (e.field == 'globalMessage') {
                let itemErr = '<li>' + e.message + '</li>'
                $("#errorMessages ul").append(itemErr);
                if ($("#errorMessages ul li").length) {
                    $("#errorMessages").attr("style", "display:block")
                }
            }
            if (e.field == 'previewDate') {
                let itemErr = '<li>' + e.defaultMessage + '</li>'
                $("#previewDateErr ul").append(itemErr);
                if ($("#previewDateErr ul li").length) {
                    $("#previewDateErr").attr("style", "display:block")
                }
                $("#previewDate").addClass("error")
            }
            if (e.field == "previewTime") {
                let itemErr = '<li>' + e.defaultMessage + '</li>'
                $("#previewTimeErr ul").append(itemErr);
                if ($("#previewTimeErr ul li").length) {
                    $("#previewTimeErr").attr("style", "display:block")
                }
                $("#previewTime").addClass("error")
            }
        })
    }
}
