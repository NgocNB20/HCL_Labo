// Contructor PopUp
$(function() {
    // 商品検索ダイアログ
    $("#goodssearch_dialog_btn").click(function() {
        // 親画面入力項目を保存する
        doOrderGoodsModify();
        // 検索ダイアログ表示
        $("#goodssearch_dialog").dialog({
            modal:true,
            minHeight:400,
            width: 1200,
            title:"受注商品の追加",
            buttons:[],
            close: function () {
                resetForm();
            }
        });
    });

    $("#doGoodsSearchPopup").click(function() {
        doGoodsSearch();
    });
});

// 親画面入力項目を保存する
function doOrderGoodsModify(){

    var form = $('form')[0]
    var dataModifiedForm = new FormData(form)
    $.ajax({
        type: "POST",
        url: pkg_common.getAppComplementUrl() + "/order/detailsupdate/doRegistToRevision",
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        processData: false,
        contentType: false,
        dataType: "json",
        data: dataModifiedForm,
        timeout: 30000
    })
        .fail(function (data){
            if (data && data.status && data.status === 403) {
                location.href = pkg_common.getAppComplementUrl() + '/login/'
            }
        })
}

// 商品検索
function doGoodsSearch() {
    // 検索結果リセット
    resetForm();
    $.ajax({
        type: "POST",
        url: pkg_common.getAppComplementUrl() + "/order/details/goodssearch/doGoodsSearchAjax",
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        processData: false,
        contentType: false,
        dataType: "json",
        data: getConditionSearch(),
        timeout: 30000

    })
        .done(function (data) {
            setResultDataGoodsSearch(data)
        })
        .fail(function (data) {
            if (data.status === 500){
                bindingServerErrorMessage(data.responseText)
            }
            if (data && data.status && data.status === 403){
                location.href = pkg_common.getAppComplementUrl() + '/login/'
            }
            if (data.responseJSON && data.responseJSON.length > 0) {
                bindingErrorMessage(data.responseJSON)
            }
        })
}

//helper function
function getConditionSearch() {
    let form = new FormData();
    form.append("searchGoodsGroupCode", $("#searchGoodsGroupCode").val());
    form.append("searchGoodsCode", $("#searchGoodsCode").val());
    form.append("searchJanCode", $("#searchJanCode").val());
    form.append("searchGoodsGroupName", $("#searchGoodsGroupName").val());
    form.append("limit", -1);
    return form
}

// 検索結果フォームリセット
function resetForm() {
    $("#goods_search_result").html('');
    $("#globalMesDiv").html('');
    $("#globalMesDiv").attr('style', 'display:none');
    $("#searchGoodsGroupCodeErr").attr('style', 'display:none');
    $("#searchGoodsNameErr").attr('style', 'display:none');
    $("#searchGoodsCodeErr").attr('style', 'display:none');
    $("#searchJanCodeErr").attr('style', 'display:none');
    $("#searchGoodsGroupCodeErr").html('');
    $("#searchGoodsNameErr").html('');
    $("#searchGoodsCodeErr").html('');
    $("#searchJanCodeErr").html('');
    $("#searchGoodsGroupCode").removeClass("error")
    $("#searchGoodsName").removeClass("error")
    $("#searchGoodsCode").removeClass("error")
    $("#searchJanCode").removeClass("error")
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

// 検索結果描画
function setResultDataGoodsSearch(data) {
    if (data) {
        let noResultItem = '<div class="l-inner_wrap" >\n' +
            '                   <div class="col wrap_flex">\n' +
            '                       <h4 class="c-pagetitle_h4 mr10">検索結果</h4>\n' +
            '                       <span class="as_end">全0件が該当しました</span>\n' +
            '                  </div>\n' +
            '               </div> '
        $("#goods_search_result").html('');
        $("#goodssearch_dialog").dialog({
            buttons: [],
        })
        $("#goods_search_result").append(noResultItem);
    }
    if (typeof data !== 'undefined' && data.length > 0) {
        const iconSort = '<span class="ascd" style="display: none">&nbsp;▲</span>' + '<span class="desc" style="display: none">&nbsp;▼</span>' + '</th>\n'
        let tableResultItems = ' <table id="result_table" class="c-tbl large60 tbl_relation_item">\n' +
            '    <thead>\n' +
            '    <tr>\n' +
            '        <th class="check w60px"></th>\n' +
            '        <th class="w160px">商品管理番号' + iconSort +
            '        <th class="w125px">商品番号' + iconSort +
            '        <th class="w125px">JANコード' + iconSort +
            '        <th>商品名' + iconSort +
            '        <th class="w110px">規格1' + iconSort +
            '        <th class="w110px">規格2' + iconSort +
            '        <th class="w110px">価格（税抜）' + iconSort +
            '        <th class="w130px" >販売可能在庫数' + iconSort +
            '    </tr>\n' +
            '    </thead>\n' +
            '    <tbody id="items_row">\n' +
            '    \n' +
            '    </tbody>\n' +
            '</table> '
        $("#goods_search_result").html('');
        $("#goods_search_result").append(tableResultItems);
        data.forEach((val, index) => {
            let groupCode = val.goodsGroupCode ? val.goodsGroupCode : ''
            let goodsCode = val.goodsCode ? val.goodsCode : ''
            let janCode = val.janCode ? val.janCode : ''
            let goodName = val.goodsGroupName ? val.goodsGroupName : ''
            let unitVal1 = val.unitValue1 ? val.unitValue1 : ''
            let unitVal2 = val.unitValue2 ? val.unitValue2 : ''
            let goodsPrice = val.goodsPrice ? val.goodsPrice : ''
            let salesPossibleStock = val.stockManagementFlag == '1' && val.salesPossibleStock != null && val.salesPossibleStock != undefined ? new Intl.NumberFormat('ja-JP').format(val.salesPossibleStock) : '―'
            let trData = '<tr>' +
                '<td class="check">\n' +
                '     <label class="c-form-control"><input onchange="onchangeCheckbox(name,this)" name="resultItems[' + index + '].resultGoodsCheck" value="true" type="checkbox"><i></i></label>\n' +
                '</td> ' +
                '<td class="alphabetic">' + groupCode + '</td>' +
                '<td class="alphabetic">' + goodsCode + '</td>' +
                '<td class="alphabetic">' + janCode + '</td>' +
                '<td>' + goodName + '</td>' +
                '<td class="">' + unitVal1 + '</td>' +
                '<td class="">' + unitVal2 + '</td>' +
                '<td class="price">' + new Intl.NumberFormat('ja-JP').format(goodsPrice)+ '円' +'</td>' +
                '<td class="txt_right">' + salesPossibleStock + '</td>' + '' +
                '</tr>'
            $("#items_row").append(trData)
        })

        $("#goodssearch_dialog").dialog({
            buttons: [
                {
                    text: "追加する",
                    click: function () {
                        $.ajax({
                            type: "POST",
                            url: pkg_common.getAppComplementUrl() + "/order/details/goodssearch/doOrderGoodsAddAjax",
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
                                $("#goodssearch_dialog").dialog("close");
                                location.href = pkg_common.getAppComplementUrl() + "/order/detailsupdate/?md=confirm"
                            })
                            .fail(function (data) {
                                if (data.status === 500){
                                    bindingServerErrorMessage(data.responseText)
                                }
                                if (data && data.status && data.status === 403){
                                    location.href= pkg_common.getAppComplementUrl() + '/login/'
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

        jQuery.extend( jQuery.fn.dataTableExt.oSort, {
            "formatted-num-pre": function ( a ) {
                a = (a === "―" || a === "") ? -1 : a.replace( /[,]/, "" );
                return parseFloat( a );
            },
            "formatted-num-asc": function ( a, b ) {
                return a - b;
            },
            "formatted-num-desc": function ( a, b ) {
                return b - a;
            }
        } );

        $(document).ready(function () {
            $('#result_table').DataTable({
                paging: false,
                searching: false,
                info: false,
                order: [[1, "asc"]],
                columnDefs: [{orderable: false, targets: 0}, {type: "formatted-num", targets: 8}],
            });
        });
    }
}

// バリデータメッセージ処理
function bindingErrorMessage(error) {
    $("#globalMesDiv").html('');
    $("#globalMesDiv").attr('style', 'display:none');
    $("#searchGoodsGroupCodeErr").attr('style', 'display:none');
    $("#searchGoodsNameErr").attr('style', 'display:none');
    $("#searchGoodsCodeErr").attr('style', 'display:none');
    $("#searchJanCodeErr").attr('style', 'display:none');
    $("#searchGoodsGroupCodeErr").html('');
    $("#searchGoodsNameErr").html('');
    $("#searchGoodsCodeErr").html('');
    $("#searchJanCodeErr").html('');
    $("#searchGoodsGroupCode").removeClass("error")
    $("#searchGoodsName").removeClass("error")
    $("#searchGoodsCode").removeClass("error")
    $("#searchJanCode").removeClass("error")
    if (error && error.length > 0) {
        error.forEach(e => {
            if (e.field == 'globalMessage') {
                $("#globalMesDiv").append("<ul></ul>")
                let itemErr = '<li>' + e.message + '</li>'
                $("#globalMesDiv ul").append(itemErr);
                if ($("#globalMesDiv ul li").length) {
                    $("#globalMesDiv").attr("style", "display:block")
                }
            }
            if (e.field == "searchGoodsGroupCode") {
                $("#searchGoodsGroupCodeErr").append("<div id='searchGoodsGroupCodeError'></div>")
                $("#searchGoodsGroupCodeErr div").append("<ul></ul>")
                $("#goodssearch_dialog").dialog({
                    buttons: [],
                })
                let itemErr = '<li>' + e.message + '</li>'
                $("#searchGoodsGroupCodeErr ul").append(itemErr);
                if ($("#searchGoodsGroupCodeErr ul li").length) {
                    $("#searchGoodsGroupCodeErr").attr("style", "display:block")
                }
                $("#searchGoodsGroupCode").addClass("error")
            }
            if (e.field == "searchGoodsCode") {
                $("#searchGoodsCodeErr").append("<div id='searchGoodsCodeError'></div>")
                $("#searchGoodsCodeErr div").append("<ul></ul>")
                $("#goodssearch_dialog").dialog({
                    buttons: [],
                })
                let itemErr = '<li>' + e.message + '</li>'
                $("#searchGoodsCodeErr ul").append(itemErr);
                if ($("#searchGoodsCodeErr ul li").length) {
                    $("#searchGoodsCodeErr").attr("style", "display:block")
                }
                $("#searchGoodsCode").addClass("error")
            }
            if (e.field == "searchGoodsName") {
                $("#searchGoodsNameErr").append("<div id='searchGoodsNameError'></div>")
                $("#searchGoodsNameErr div").append("<ul></ul>")
                $("#goodssearch_dialog").dialog({
                    buttons: [],
                })
                let itemErr = '<li>' + e.message + '</li>'
                $("#searchGoodsNameErr ul").append(itemErr);
                $("#searchGoodsNameErr ul li").length ? $("#searchGoodsNameErr").attr("style", "display:block") : ''
                $("#searchGoodsName").addClass("error")
            }
            if (e.field == "searchJanCode") {
                $("#searchJanCodeErr").append("<div id='searchJanCodeError'></div>")
                $("#searchJanCodeErr div").append("<ul></ul>")
                $("#goodssearch_dialog").dialog({
                    buttons: [],
                })
                let itemErr = '<li>' + e.message + '</li>'
                $("#searchJanCodeErr ul").append(itemErr);
                $("#searchJanCodeErr ul li").length ? $("#searchJanCodeErr").attr("style", "display:block") : ''
                $("#searchJanCode").addClass("error")
            }
        })
    }
}

// サーバーエラーメッセージ処理
function bindingServerErrorMessage(error) {
    $("#globalMesDiv").html('');
    $("#globalMesDiv").attr('style', 'display:none');
    if (error != null) {
        $("#globalMesDiv").append("<ul></ul>")
        let itemErr = '<li>' + error + '</li>'
        $("#globalMesDiv ul").append(itemErr);
        if ($("#globalMesDiv ul li").length) {
            $("#globalMesDiv").attr("style", "display:block")
        }
    }
}
