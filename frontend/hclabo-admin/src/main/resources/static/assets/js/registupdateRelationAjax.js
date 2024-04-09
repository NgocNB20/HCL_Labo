var token = $('#_csrf').attr('content');
var header = $('#_csrf_header').attr('content');
// ダイアログ表示
$(function () {
    $("#relation_item_dialog_btn").click(function () {
        $("#relation_item_dialog").dialog({
            modal: true,
            width: 1200,
            title: "関連商品の追加",
            buttons: [],
            close: function (event, ui) {
                resetForm();
            }
        });
        $("#doSearch").click(function () {
            doRelationSearch();
        })
        doAddGoodRelationAjax();
    });
});

// 商品変更画面で関連商品追加イベント
function doAddGoodRelationAjax() {
    var form = $('form')[0]
    var formAddgoods = new FormData(form)
    $.ajax({
        type: "POST",
        url: pkg_common.getAppComplementUrl() + "/goods/registupdate/doAddGoodRelationAjax",
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        processData: false,
        contentType: false,
        dataType: "json",
        data: formAddgoods,
        timeout: 30000

    })
        .done(function (data) {
            getGoodsRegistUpdateRelationSearchModel(data)
        })
        .fail(function (data) {
            if (data && data.status && data.status === 403) {
                location.href = pkg_common.getAppComplementUrl() + '/login/'
            }
        })
}

// 関連商品追加ダイアログ表示
function getGoodsRegistUpdateRelationSearchModel(data) {
    $.ajax({
        type: "POST",
        url: pkg_common.getAppComplementUrl() + "/goods/registupdate/relationsearch/ajax",
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        contentType: "application/json",
        dataType: "json",
        data: JSON.stringify(data),
        timeout: 30000

    }).done(function (data) {
        setListCategory(data);
    }).fail(function (data) {
        if (data && data.status && data.status === 403) {
            location.href = pkg_common.getAppComplementUrl() + '/login/'
        }
    })
}

// 関連商品ダイアログにて検索する
function doRelationSearch() {
    resetForm();
    $.ajax({
        type: "POST",
        url: pkg_common.getAppComplementUrl() + "/goods/registupdate/relationsearch/post/ajax",
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        processData: false,
        contentType: false,
        dataType: "json",
        data: getConditionSearch(),
        timeout: 30000
    }).done(function (data) {
        setResultDataRelationSearch(data);
    })
        .fail(function (data) {
            if (data.responseJSON && data.responseJSON.length > 0) {
                bindingErrorMessage(data.responseJSON)
            }
        })
}

// 関連商品追加し、商品変更画面リロード
function doBindingReloadToIndex(data) {
    let resForm = {};
    resForm.redirectGoodsRelationEntityList = data.redirectGoodsRelationEntityList
    resForm.tmpGoodsRelationEntityList = data.tmpGoodsRelationEntityList
    resForm.goodsGroupDto = data.goodsGroupDto
    resForm.goodsRelationEntityList = data.goodsRelationEntityList
    $.ajax({
        type: "POST",
        url: pkg_common.getAppComplementUrl() + "/goods/registupdate/loadAjax",
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        contentType: "application/json",
        dataType: "text",
        data: JSON.stringify(resForm),
        timeout: 30000
    })
        .done(function (data) {
            location.href = pkg_common.getAppComplementUrl() + "/goods/registupdate?scroll=relation"
        })
        .fail(function (data) {
            if (data && data.status && data.status === 403) {
                location.href = pkg_common.getAppComplementUrl() + '/login/'
            }
            if (data.responseJSON && data.responseJSON.length > 0) {
                bindingErrorMessage(data.responseJSON)
            }
        })
}

// フォームリセット
function resetForm() {
    $("#result_search_data").html('');
    $("#globalMesDiv").html('');
    $("#globalMesDiv").attr('style', 'display:none');
    $("#searchGoodsGroupCodeErr").attr('style', 'display:none');
    $("#searchGoodsGroupNameErr").attr('style', 'display:none');
    $("#searchCategoryIdErr").attr('style', 'display:none');
    $("#searchGoodsGroupCodeErr").html('');
    $("#searchGoodsGroupNameErr").html('');
    $("#searchCategoryIdErr").html('');
    $("#searchGoodsGroupCode").removeClass("error")
    $("#searchGoodsGroupName").removeClass("error")
    $("#goodsSearchCategory").removeClass("error")
    formCheckbox = new FormData();
    $(".ui-dialog-buttonpane").remove();
}

// ダイアログの項目にフォーム値から設定
function getConditionSearch() {
    let form = new FormData();
    form.append("searchGoodsGroupCode", $("#searchGoodsGroupCode").val())
    form.append("searchGoodsGroupName", $("#searchGoodsGroupName").val())
    form.append("searchCategoryId", $("#goodsSearchCategory").val())
    return form
}

let formCheckbox = new FormData();

// チェックボックス処理
function onchangeCheckbox(name, e) {
    let value = $(e).is(":checked")
    if (value) {
        formCheckbox.append(name, value)
    } else {
        formCheckbox.set(name, value)
    }
}

// カテゴリーを設定
function setListCategory(data) {
    for (const [key, value] of Object.entries(data.searchCategoryIdItems)) {
        let optionVal = '<option value="' + key + '">' + value + '</option>'
        $("#goodsSearchCategory").append(optionVal)
    }
}

// 検索結果を設定
function setResultDataRelationSearch(data) {
    if (typeof data == 'undefined' || data.length == 0) {
        let noResultItem = '<div class="l-inner_wrap" >\n' +
            '                   <div class="col wrap_flex">\n' +
            '                       <h4 class="c-pagetitle_h4 mr10">検索結果</h4>\n' +
            '                       <span class="as_end">全0件が該当しました</span>\n' +
            '                  </div>\n' +
            '               </div> '
        $("#result_search_data").html('');
        $("#result_search_data").append(noResultItem);
    }
    if (typeof data !== 'undefined' && data.length > 0) {
        const iconSort = '<span class="ascd" style="display: none">&nbsp;▲</span>' + '<span class="desc" style="display: none">&nbsp;▼</span>' + '</th>\n'
        let tableResultItems = ' <table id="relation_result" class="c-tbl large60 tbl_relation_item">\n' +
            '    <thead>\n' +
            '    <tr>\n' +
            '        <th class="check w60px"></th>\n' +
            '        <th class="w155px">商品管理番号' + iconSort +
            '        <th>商品名' + iconSort +
            '        <th class="w240px">公開状態' + iconSort +
            '    </tr>\n' +
            '    </thead>\n' +
            '    <tbody id="items_data_row">\n' +
            '    \n' +
            '    </tbody>\n' +
            '</table> '
        $("#result_search_data").html('');
        $("#result_search_data").append(tableResultItems);
        data.forEach((val, index) => {
            let groupCode = val.goodsGroupCode ? val.goodsGroupCode : ''
            let goodName = val.goodsGroupName ? val.goodsGroupName : ''
            let goodsStatusPC = ''
            if (val.goodsOpenStatusPC) {
                if (val.goodsOpenStatusPC === 'OPEN') {
                    goodsStatusPC = '公開中'
                } else if (val.goodsOpenStatusPC === 'NO_OPEN') {
                    goodsStatusPC = '非公開'
                } else if (val.goodsOpenStatusPC === 'DELETED') {
                    goodsStatusPC = '削除'
                }
            }
            let trData = '<tr>' +
                '<td class="check">\n' +
                '     <label class="c-form-control"><input onchange="onchangeCheckbox(name,this)" name="resultItems[' + index + '].resultCheck" value="true" type="checkbox"><i></i></label>\n' +
                '</td> ' +
                '<td class="w155px alphabetic">' + groupCode + '</td>' +
                '<td>' + goodName + '</td>' +
                '<td class="w240px">' + goodsStatusPC + '</td>' + '' +
                '</tr>'
            $("#items_data_row").append(trData)
        })
        //4
        $("#relation_item_dialog").dialog({
            buttons: [{
                text: "追加する",
                click: function () {
                    $.ajax({
                        type: "POST",
                        url: pkg_common.getAppComplementUrl() + "/goods/registupdate/relationsearch/doSelectRelationAjax",
                        beforeSend: function (xhr) {
                            xhr.setRequestHeader(header, token);
                        },
                        processData: false,
                        contentType: false,
                        dataType: "json",
                        data: formCheckbox,
                        timeout: 30000
                    })
                        .done(function (data) {
                            doBindingReloadToIndex(data)
                            $("#relation_item_dialog").dialog("close");
                        })
                        .fail(function (data) {
                            if (data && data.status && data.status === 403) {
                                location.href = pkg_common.getAppComplementUrl() + '/login/'
                            }
                            if (data.responseJSON && data.responseJSON.length > 0) {
                                bindingErrorMessage(data.responseJSON)
                            }

                        })
                    formCheckbox = new FormData()
                }
            }
            ]
        });
        $(document).ready(function () {
            $('#relation_result').DataTable({
                paging: false,
                searching: false,
                info: false,
                order: [[1, "asc"]],
                columnDefs: [{orderable: false, targets: 0}],
            });
        });
    }
}

// バリデータメッセージ処理
function bindingErrorMessage(error) {
    $("#globalMesDiv").html('')
    $("#searchGoodsGroupCodeErr").html('')
    $("#searchGoodsGroupNameErr").html('')
    $("#searchCategoryIdErr").html('')
    if (error && error.length > 0) {
        $("#globalMesDiv").append("<ul></ul>")
        $("#searchGoodsGroupCodeErr").append("<div id='searchGoodsGroupCodeError'></div>")
        $("#searchGoodsGroupCodeErr div").append("<ul></ul>")
        $("#searchGoodsGroupNameErr").append("<div id='searchGoodsGroupNameError'></div>")
        $("#searchGoodsGroupNameErr div").append("<ul></ul>")
        $("#searchCategoryIdErr").append("<div id='searchCategoryIdError'></div>")
        $("#searchCategoryIdErr div").append("<ul></ul>")
        error.forEach(e => {
            if (e.field == 'globalMessage') {
                let itemErr = '<li>' + e.message + '</li>'
                $("#globalMesDiv ul").append(itemErr);
                if ($("#globalMesDiv ul li").length) {
                    $("#globalMesDiv").attr("style", "display:block")
                }
            }
            if (e.field == 'searchGoodsGroupCode') {
                let itemErr = '<li>' + e.message + '</li>'
                $("#searchGoodsGroupCodeErr ul").append(itemErr);
                if ($("#searchGoodsGroupCodeErr ul li").length) {
                    $("#searchGoodsGroupCodeErr").attr("style", "display:block")
                }
                $("#searchGoodsGroupCode").addClass("error")
            }
            if (e.field == "searchGoodsGroupName") {
                let itemErr = '<li>' + e.message + '</li>'
                $("#searchGoodsGroupNameErr ul").append(itemErr);
                $("#searchGoodsGroupNameErr ul li").length ? $("#searchGoodsGroupNameErr").attr("style", "display:block") : ''
                $("#searchGoodsGroupName").addClass("error")
            }
            if (e.field == "searchCategoryId") {
                let itemErr = '<li>' + e.message + '</li>'
                $("#searchCategoryIdErr ul").append(itemErr);
                $("#searchCategoryIdErr ul li").length ? $("#searchCategoryIdErr").attr("style", "display:block") : ''
                $("#goodsSearchCategory").addClass("error")
            }
        })
    }
}
