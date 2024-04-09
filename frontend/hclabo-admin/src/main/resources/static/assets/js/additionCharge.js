// dialog additionalcharge
$(function () {
    $("#additionalcharge_dialog_btn").click(function () {
        doAdditionalCharge()
    });
})

// 追加料金画面表示初期処理
function doAdditionalCharge() {
    var form = $('form')[0]
    var doAdditionalCharge = new FormData(form)
    $.ajax({
        type: "POST",
        url: pkg_common.getAppComplementUrl() + "/order/detailsupdate/doRegistToRevision",
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        processData: false,
        contentType: false,
        dataType: "json",
        data: doAdditionalCharge,
        timeout: 30000
    })
        .done(function (data) {
            $("#additionalcharge_dialog").dialog({
                modal: true,
                width: 820,
                title: "その他料金の追加",
                buttons: [{
                    text: "追加料金を入力する",
                    click: function() {
                        doUpdateAjax()
                    }
                }
                ],
                close: function (event, ui) {
                    clearPopupMessage()
                }
            });
        })
        .fail(function (data) {
            if (data && data.status && data.status === 403) {
                    location.href = pkg_common.getAppComplementUrl() + '/login/'
            }
            bindingGlobalMessageDetail(data.responseJSON)
            document.getElementById("global-mesage").scrollIntoView();
        })
}

// 追加料金を入力する
function doUpdateAjax() {
    let formUpdate = new FormData()
    formUpdate.append("inputAdditionalDetailsName", $("#inputAdditionalDetailsNameInput").val())
    formUpdate.append("inputAdditionalDetailsPrice", $("#inputAdditionalDetailsPriceInput").val())
    $.ajax({
        type: "POST",
        url: pkg_common.getAppComplementUrl() + "/order/details/additionalcharge/doUpdate",
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        processData: false,
        contentType: false,
        dataType: "json",
        data: formUpdate,
        timeout: 30000
    }).done(function (data) {
        location.href = pkg_common.getAppComplementUrl() + "/order/detailsupdate?md=confirm";
    }).fail(function (data) {
        if (data && data.status && data.status === 403) {
            location.href = pkg_common.getAppComplementUrl() + '/login/'
        }
        bindingPopupMessage(data.responseJSON)
    })
}

function bindingGlobalMessageDetail(error) {
    $("#global-mesage").html('')
    if (error && error.length > 0) {
        error.forEach(e => {
            if (e.field == 'globalMessage') {
                bindingMessage("#global-mesage", null, e.message)
            }
        })
    }
}

function bindingPopupMessage(error) {
    $("#globalMessage").html('')
    $("#inputAdditionalDetailsName").html('')
    $("#inputAdditionalDetailsPrice").html('')
    if (error && error.length > 0) {
        error.forEach(e => {
            if (e.field == 'globalMessage') {
                bindingMessage("#globalMessage",null, e.message)
            }
            if (e.field == 'inputAdditionalDetailsName') {
                bindingMessage("#inputAdditionalDetailsName","inputAdditionalDetailsNameInputError", e.message)
                $('#inputAdditionalDetailsNameInput').addClass("error")
            }
            if (e.field == 'inputAdditionalDetailsPrice') {
                bindingMessage("#inputAdditionalDetailsPrice","inputAdditionalDetailsPriceInputError", e.message)
                $('#inputAdditionalDetailsPriceInput').addClass("error")
            }
        })
    }
}

function bindingMessage(fieldId,divError, message) {
    if(divError){
        let divE = "<div id='" + divError + "'></div>"
        $(fieldId).append(divE)
        $('#' + divError).append("<ul></ul>")
    }else{
        $(fieldId).append("<ul></ul>")
    }
    let itemErr = '<li>' + message + '</li>'
    $(fieldId + " ul").append(itemErr);
    if ($(fieldId + " ul li").length) {
        $(fieldId).attr("style", "display:block")

    }
}

function clearPopupMessage() {
    $("#globalMessage").html('')
    $("#inputAdditionalDetailsName").html('')
    $("#inputAdditionalDetailsPrice").html('')
    $("#globalMessage").attr("style", "display:none")
    $("#inputAdditionalDetailsName").attr("style", "display:none")
    $("#inputAdditionalDetailsPrice").attr("style", "display:none")
    $('#inputAdditionalDetailsNameInput').removeClass("error")
    $('#inputAdditionalDetailsPriceInput').removeClass("error")
}

function clearDetailMessage() {
    $("#global-mesage").attr("style", "display:none")
}
