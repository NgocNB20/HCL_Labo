function CsvOptionDownload(urlGetTemplate = '', urlUpdateTemplate = '', urlGetDefault = '') {
    // event
    // テーブルの水平スクロールを防止する
    $('#optional_download_tbl').scroll(function (e) {
        if ($(this).scrollLeft() > 1) {
            $(this).scrollLeft(0);
        }
    })

    $("#downloadOptionType").change(function () {
        onchangeDownloadType();
    })

    $("#downloadOptionTypeResult").change(function () {
        onchangeDownloadTypeForResult();
    })

    $("#option_template_index").change(async function () {
        await onchangeTemplateIndex(this);
    })

    $('#option_template_element').change(function () {
        onchangeTemplate(this);
    })

    $("#isOutHeader").change(function () {
        onchangeIsOutHeader(this);
    })

    // グローバル変数
    var token = $('#_csrf').attr('content');
    var header = $('#_csrf_header').attr('content');
    var optionDownloadObjList = {};
    var optionDownloadObj = {};
    var tableParent = '<table id="download_header_table" class="c-tbl">\n' +
        '            <thead>\n' +
        '                <tr>\n' +
        '                    <th class="drag w30px"></th>' +
        '                    <th class="check w18px">\n' +
        '                        <label class="c-form-control"><input id="checkAllHeader" checked="true" type="checkbox" name="" value=""><i></i></label>\n' +
        '                    </th>\n' +
        '                    <th class="w20px">#</th>\n' +
        '                    <th class="w120px">項目名</th>\n' +
        '                    <th class="w180px">変更後の項目名</th>\n' +
        '                </tr>\n' +
        '            </thead>\n' +
        '            <tbody id="items_row"></tbody>\n' +
        '        </table>';

    // ダイアログ表示
    $(function () {
        $("#doOptionDownload").click(function () {
            let idTemplate = $('#option_template_index option:selected').val();
            doLoadHeaderData(true, idTemplate);
            $("#optionDownload_dialog").dialog({
                modal: true,
                width: 800,
                position: {
                    my: 'top',
                    at: 'top+20'
                },
                title: "CSVダウンロードテンプレート",
                buttons: [
                    {
                        text: '保存する',
                        click: function () {
                            doSubmitData(optionDownloadObj);
                        }
                    }, {
                        text: 'リセット',
                        class: 'c-btn btn_del',
                        click: function () {
                            doLoadDefaultData();
                        }
                    }],
                close: function (event, ui) {
                    resetForm();
                }
            });
        });
        onchangeDownloadType();
        onchangeDownloadTypeForResult();
    });

    // テーブルの水平スクロールを防止する
    $('#optional_download_tbl').scroll(function (e) {
        if ($(this).scrollLeft() > 1) {
            $(this).scrollLeft(0);
        }
    })

    // イベント処理
    // 注文画面でCSVDLテンプレートオプションを選択
    async function onchangeTemplateIndex(el) {
        // リスト テンプレートの非同期取得
        await doLoadHeaderData(false);

        // 選択したオブジェクト データ
        let optionIdSelected = $(el).val();
        let selectedObj = optionDownloadObjList.csvDownloadOptionList.find(obj => obj.optionId === optionIdSelected);
        if (typeof selectedObj == 'undefined') {
            selectedObj = {};
            selectedObj["resetFlg"] = true;
        } else {
            selectedObj["resetFlg"] = false;
        }

        selectedObj["updateFlg"] = false;
        // AJAX POST
        $.ajax({
            type: "POST",
            url: pkg_common.getAppComplementUrl() + urlUpdateTemplate,
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            },
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(selectedObj),
            timeout: 30000

        })
            .done(function (data) {})
            .fail(function (data) {})
    }
    // ポップアップで CSVDL テンプレート オプションを選択します
    function onchangeTemplate(el) {
        // エラーメッセージをクリア
        $("#optionTemplateNameErr").attr("style", "display:none;");
        $("#optionTemplateNameErr").html('');
        $("#option_template_input").removeClass("error");

        let csvTemplate = $("#option_template_element :selected").val();
        let csvTemplateName = $("#option_template_element :selected").get(0).innerText;

        // オプション名の入力値を設定
        $("#option_template_input").val(csvTemplateName);
        if ($("#option_template_input").val()) {
            $("#option_template_input").prop('disabled', false);
        }
        // 現在のテンプレートを取得してテーブルを表示する
        optionDownloadObj = optionDownloadObjList.csvDownloadOptionList.find(temp => temp.optionId ===  csvTemplate);

        if (typeof optionDownloadObj != 'undefined') {
            appendTable();
        }
        setDataTable(optionDownloadObj);
    }
    // すべてにチェックを入れ、すべてのチェックボックスをオフにします
    function onchangeCheckAllHeader(el) {
        if ($(el).prop('checked')) {
            $('input:checkbox[name^="checkItems"]').prop('checked', true);
        } else {
            $('input:checkbox[name^="checkItems"]').prop('checked', false);
        }
    }
    // ヘッダーのタイトル
    function onchangeIsOutHeader(el) {
        let isHeader = $(el).is(":checked");

        controlDisplayInput(isHeader);
    }
    // CSVDLタイプのコントロールボタン
    function onchangeDownloadType() {
        let csvType = $('#downloadOptionType').find(":selected").val();
        let optionBtn = $('#doOptionDownload');

        // 受注データタイプ == 受注CSV
        if (csvType === "6" || csvType == "0") {
            optionBtn.removeAttr("disabled").removeClass('btn_disabled_gray');
            $("#option_template_index").removeAttr("disabled");
        } else {
            optionBtn.prop('disabled', true).addClass('btn_disabled_gray');
            $("#option_template_index").prop("disabled", true);
        }
    }

    // CSVDLタイプのコントロールボタン
    function onchangeDownloadTypeForResult() {
        let csvType = $('#downloadOptionTypeResult').find(":selected").val();
        // 受注データタイプ == 受注CSV
        if (csvType === "6" || csvType == "0") {
            $("#option_template_index_result").removeAttr("disabled");
        } else {
            $("#option_template_index_result").prop("disabled", true);
        }
    }
    // ドロップダウン要素の場合の初期データ
    function setSelectOptional(data, idSelected) {
        resetForm();
        if (typeof data === 'undefined' || data === null) {

        } else {
            let optionData = '<option value="">選択してください</option>\n';
            data.csvDownloadOptionList.forEach(val => {
                optionData = optionData + '<option value="' + val.optionId + '" >' + val.optionName + '</option>\n'
            })
            $("#option_template_element").html('');
            $("#option_template_element").append(optionData);
            $("#option_template_element").val(idSelected).change();
        }
    }
    // テーブルのデータを設定する
    function setDataTable(downloadObj) {
        if (typeof downloadObj !== 'undefined' && downloadObj !== null) {
            $("#optional_download_tbl").html('');
            $("#optional_download_tbl").append(tableParent);
            $("#optionDownload_dialog hr").attr('style','display: block;');
            $("#checkAllHeader").change(function () {
                onchangeCheckAllHeader(this);
            })
            // setting isOutHeader
            $('#isOutHeader_label').attr('style', 'display: inline-flex;');
            $('#isOutHeader').prop('checked', downloadObj.outHeader);

            downloadObj.optionContent.forEach(val => {
                val.columnLabel = val.columnLabel == null || typeof val.columnLabel === 'undefined' ? "" : val.columnLabel;
                let trData = '<tr class="tbl_cat_list">' +
                    '<td class="drag"><i class="material-icons menu"></i></td>'+
                    '<td name="item-name" class="check">\n' +
                    '     <label class="c-form-control"><input id="check-flag-'+val.itemName+'" name="checkItems['+val.itemName+']"  value="'+val.itemName+'" type="checkbox"><i></i></label>\n' +
                    '</td> ' +
                    '<td name="item-default-order" >' + val.defaultOrder + '</td>' +
                    '<td name="item-label" id="'+val.itemName+'" class="alphabetic">' + val.defaultColumnLabel + '</td>' +
                    '<td class="alphabetic">' + '<input id="label-value-'+val.itemName+'" maxlength="15" type="text" value="" class="c-form-control w80p" placeholder="変更したい名称を入力してください" />'
                    +'<div style="display: none;" id="columnLabelInputErr'+val.itemName+'" class="c-req"></div>'
                    + '</td>' +
                    '</tr>'
                $("#items_row").append(trData);
                // デフォルトでオンのチェックボックス
                document.getElementById('check-flag-'+val.itemName+'').checked = val.outFlag;
                document.getElementById('label-value-'+val.itemName+'').value = val.columnLabel;
            });

            // 表示ボタン
            $("#optionDownload_dialog").dialog("option", "buttons", [
                {
                    text: '保存する',
                    click: function () {
                        doSubmitData(optionDownloadObj);
                    }
                }, {
                    text: 'リセット',
                    class: 'c-btn btn_del',
                    click: function () {
                        doLoadDefaultData();
                    }
                }]);
            // display input or not
            controlDisplayInput(optionDownloadObj.outHeader);
        } else {
            // 掃除
            $("#optionDownload_dialog").dialog("option", "buttons", {});
            $("#option_template_input").prop('disabled', true);
            resetForm();
        }

        // ソートテーブルのドラッグ可能設定
        $('#download_header_table tbody').sortable({
            cursor: 'n-resize',
            items: 'tr',
            stop: function () {
                let trTagLst = $('#items_row tr');
                optionDownloadObj.optionContent = [];
                // Data update
                for (let i = 1; i <= trTagLst.length; i++) {
                    optionDownloadObj.optionContent.push({
                        itemName: trTagLst.get(i - 1).querySelector('td[name="item-name"] input').getAttribute('value'),
                        defaultOrder: trTagLst.get(i - 1).querySelector('td[name="item-default-order"]').textContent,
                        order: i * 10,
                        defaultColumnLabel: trTagLst.get(i - 1).querySelector('td[name="item-label"]').textContent,
                        columnLabel: trTagLst.get(i - 1).querySelector('td:last-of-type input').getAttribute('value'),
                        outFlag: trTagLst.get(i - 1).querySelector('td[name="item-name"] input').getAttribute('checked')
                    })
                }
            }
            // ドラッグ時にホイールマウスをスクロールできます
        }).draggable({
            scroll: true
        });
    }

    // AJAX GET
    // デフォルトデータを取得する
    function doLoadDefaultData() {
        // クリアデータ
        $("#option_template_input").removeClass("error");
        $("#optionTemplateNameErr").html("");
        $("#optionTemplateNameErr").attr('style', 'display: none;');

        $.ajax({
            type: "GET",
            url: pkg_common.getAppComplementUrl() + urlGetDefault,
            dataType: "json",
            data: null,
            timeout: 30000
        })
            .done(function (data) {
                if (data) {
                    data["optionId"] = optionDownloadObj.optionId;
                    data["defaultOptionName"] = optionDownloadObj.defaultOptionName;
                    $('#option_template_input').val(optionDownloadObj.defaultOptionName);
                    optionDownloadObj = data;
                    setDataTable(data);
                }

            })
            .fail(function (data) {

            })
    }

    // AJAX GET
    // Ajax からデータを取得する
    function doLoadHeaderData(indexFlg, idSelected = null) {
        return new Promise(resolve => {
            resetForm();
            $.ajax({
                type: "GET",
                url: pkg_common.getAppComplementUrl() + urlGetTemplate,
                dataType: "json",
                data: null,
                timeout: 30000
            })
                .done(function (data) {
                    optionDownloadObjList = data;
                    if (indexFlg) {
                        setSelectOptional(data, idSelected);
                    }
                    resolve();
                })
                .fail(function (data) {

                })
        })
    }
    // AJAX POST
    // CSVDL テンプレートを更新するためのデータを送信する
    function doSubmitData(data) {
        data.outHeader = $('#isOutHeader').is(":checked");
        data.optionName = $('#option_template_input').val();
        data.optionContent.forEach(val => {
            val.columnLabel = $('#label-value-' + val.itemName).val();
            val.outFlag = $('#check-flag-' + val.itemName).is(":checked");
        })
        data["updateFlg"] = true;
        data["resetFlg"] = false;
        $.ajax({
            type: "POST",
            url: pkg_common.getAppComplementUrl() + urlUpdateTemplate,
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            },
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(data),
            timeout: 30000

        })
            .done(function () {
                // 注文画面で変更したオプションを更新します
                $("#option_template_index").val(data.optionId).change();
                $("#option_template_index option[value=" + data.optionId + "]").text(data.optionName);

                $("#optionDownload_dialog").dialog("close");
            })
            .fail(function (data) {
                if (data && data.status &&
                    (data.status === 403 || (data.status === 200 && data.responseText.includes('login')))
                ) {
                    location.href = pkg_common.getAppComplementUrl() + '/login/'
                } else if (data.responseJSON && data.responseJSON.length > 0) {
                    bindingErrorMessage(data.responseJSON);
                } else {
                    $("#optionDownload_dialog").dialog("close");
                }
            })
    }

    // ヘルパー関数
    // きれいなデータ
    function resetForm() {
        $("#optionDownload_dialog hr").attr('style', 'display: none;');
        $('#download_header_table').html('');
        $("#option_template_input").val('');
        $('#isOutHeader_label').attr('style', 'display: none;');
        $("#option_template_input").removeClass("error");
        $("#optionTemplateNameErr").html("");
        $("#optionTemplateNameErr").attr('style', 'display: none;');
        $('#option_template_input').prop('disabled', true);

    }
    // control display or not
    function controlDisplayInput(displayFlg) {
        let inputList = $('#items_row tr td:last-of-type input');

        if (!displayFlg) {
            Array.from(inputList).forEach(input => {
                $(input).hide();
                $(input).parent().addClass('h58px');
            })
        } else {
            Array.from(inputList).forEach(input => {
                $(input).show();
                $(input).parent().removeClass('h58px');
            })
        }
    }

    // テーブルを表示する
    function appendTable() {
        $("#optional_download_tbl").html('');
        $("#optional_download_tbl").append(tableParent);
        $("#optionDownload_dialog hr").attr('style','display: block;');

    }

    // 入力の位置を取得
    function getExtractPosition(str, start, end) {
        var startindex = str.indexOf(start);
        var endindex = str.indexOf(end, startindex);
        return str.substring(startindex + 1, endindex)
    }

    // バリデータメッセージ処理
    function bindingErrorMessage(error) {
        // 明確なメッセージ
        $("#optionTemplateNameErr").attr("style", "display:none;");
        $("#optionTemplateNameErr").html('');
        $("#option_template_input").removeClass("error");
        let divErrList = document.querySelectorAll('#download_header_table td.alphabetic div');
        let containError = Array.from(divErrList).filter(el => el.textContent !== '');
        containError.forEach(errDiv => {
            $(errDiv).attr('style', 'display:none;');
            $(errDiv).html('');
            $(errDiv).prev().removeClass('error');
        });
        // エラーメッセージを表示
        if (error && error.length > 0) {
            error.forEach(e => {
                if (e.field === 'optionName') {
                    $("#optionTemplateNameErr").append("<ul></ul>");
                    let itemErr = '<li>' + e.defaultMessage + '</li>'
                    $("#optionTemplateNameErr ul").append(itemErr);
                    if ($("#optionTemplateNameErr ul li").length) {
                        $("#optionTemplateNameErr").attr("style", "display:block");
                    }
                    $("#option_template_input").addClass("error");
                }
                if (e.field.substring(0, 13) === 'optionContent') {
                    let fieldPos = getExtractPosition(e.field, '[', ']');
                    let fieldTarget = optionDownloadObj.optionContent[fieldPos].itemName;
                    $("#columnLabelInputErr" + fieldTarget + "").text(e.defaultMessage);
                    if ($("#columnLabelInputErr" + fieldTarget + "").text() !== "") {
                        $("#columnLabelInputErr" + fieldTarget + "").attr('style', 'display:block;');
                        $("#columnLabelInputErr" + fieldTarget + "").prev().addClass('error');
                    }
                }
            })
        }
    }
}
