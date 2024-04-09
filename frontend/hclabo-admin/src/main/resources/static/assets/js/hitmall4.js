$(function(){
    // 入力チェックでエラー発生した後、各input項目に値が入力されたら、イベントを発火
    var container = document.getElementById('mainContainer');
    if (container != null) {
        displayArticleError('mainContainer');
        removeParentArticleErrorOnChange();
    }
    //検索結果
    $('#selectAll').on('change', function () {
        if ($(this).prop('checked')) {
            $('input:checkbox[name^="resultItems"]').prop('checked', true);
            var cnt = $('#search_result tbody input:checkbox:checked').length;
            $('.count_menu').css({'display': 'flex'});
            $('.resultcount').text( cnt  + '件 選択済' );
        } else {
            $('input:checkbox[name^="resultItems"]').prop('checked', false);
            $('.count_menu').hide();
        }
    });
    $('input:checkbox[name^="resultItems"]').change(function() {
        $('.count_menu').css({'display': 'flex'});
        var cnt = $('#search_result tbody input:checkbox:checked').length;
        $('.resultcount').text( cnt + '件 選択済' );
        if (cnt < 1) {
            $('.count_menu').hide();
        }
    }).trigger('change');
    $( window ).on('load',function() {
        $('#selectAll').prop('checked', true);
        const resultItems = $('input:checkbox[name^="resultItems"]');
        for (let i = 0; i < resultItems.length; i++) {
            if (resultItems[i].checked === false) {
                $('#selectAll').prop('checked', false);
                break;
            }
        }
        if ($('#detailSearch').find('.c-error-txt').length > 0) {
            $('.c-detail-warp .c-btn_add').click();
        }

        // ------------------------------------------------------------------------------------------------------------
        // 検索画面で入力されたキーワードの出力（汎用的な実装）
        // 入力チェックでエラーが発生しない場合のみに表示
        // ------------------------------------------------------------------------------------------------------------
        if ($('#allItemsSearch').find('.c-error-txt').length === 0) {
            let allItemsSearchKeywords = '';
            const allItemsSearchChildDl = document.querySelectorAll('#allItemsSearch dl');
            Array.from(allItemsSearchChildDl).forEach(dl => {
                const childDd = dl.getElementsByTagName('dd')[0];
                // inputタグの項目：text, checkbox, radio という３つパターンがある
                const childInput = childDd.getElementsByTagName('input')[0];
                const childInput2 = childDd.getElementsByTagName('input')[1];
                const childCategory = childDd.getElementsByTagName('span');
                if (childCategory != null && childCategory.item(0) != null && childCategory.item(0).className === 'categorySearchList') {
                    allItemsSearchKeywords = allItemsSearchKeywords + '<span class="keywordLabel">' + dl.getElementsByTagName('dt')[0].innerText;
                    allItemsSearchKeywords = allItemsSearchKeywords + ':</span> ';
                }
                for (let i = 0; i < childCategory.length; i++) {
                    const childCategoryDetail = childDd.getElementsByTagName('span').item(i);
                    if (childCategoryDetail != null && childCategoryDetail.className === 'categorySearchList') {
                        if (i != 0) {
                            allItemsSearchKeywords = allItemsSearchKeywords + ' , ';
                        }
                        allItemsSearchKeywords = allItemsSearchKeywords + '<span>' + childCategoryDetail.innerText;
                        allItemsSearchKeywords = allItemsSearchKeywords + '</span>　';
                    }
                }

                if (childInput != null) {
                    // input type=text 項目
                    if (childInput.type === 'text') {
                        if (childInput.value !== '') {
                            // 項目のラベル
                            allItemsSearchKeywords = allItemsSearchKeywords + '<span class="keywordLabel">' + dl.getElementsByTagName('dt')[0].innerText;
                            allItemsSearchKeywords = allItemsSearchKeywords + ':</span> ';

                            // 項目の値
                            allItemsSearchKeywords = allItemsSearchKeywords + '<span>' + childInput.value;
                            // control spacing FROM-TO yyyy/MM/dd HH:mm ~ yyyy/MM/dd HH:mm
                            let nextElementChecking = childInput.parentNode.nextSibling.nextElementSibling;
                            if (childInput.id === 'searchMinSalesPossibleStockCount' || childInput.id === 'searchNoveltyPresentGoodsCountFrom' || childInput.id === 'orderPriceFrom') {
                                allItemsSearchKeywords = allItemsSearchKeywords + '</span>';
                            } else if (nextElementChecking !== null && nextElementChecking.id === 'timeTo' || nextElementChecking !== null && nextElementChecking.id === 'timeFrom') {
                                allItemsSearchKeywords = allItemsSearchKeywords + '</span>';
                            } else if(childInput2 != null && childInput.classList.contains('hasDatepicker') && childInput2.classList.contains('hasDatepicker')) {
                                allItemsSearchKeywords = allItemsSearchKeywords + '</span>';
                            }else {
                                allItemsSearchKeywords = allItemsSearchKeywords + '</span>　';
                            }
                        }
                        // 日付項目の対応
                        // 日付FROM-TO・時間なしの場合、TO項目の値を出力する：yyyy/MM/dd ~ yyyy/MM/dd
                        if (childInput2 != null && childInput2.type === 'text' && childInput2.classList.contains('hasDatepicker') ) {
                            if (childInput.value === '' && childInput2.value !== '') {
                                allItemsSearchKeywords = allItemsSearchKeywords + '<span class="keywordLabel">' + dl.getElementsByTagName('dt')[0].innerText;
                                allItemsSearchKeywords = allItemsSearchKeywords + ':</span>';

                                allItemsSearchKeywords = allItemsSearchKeywords + '<span>' + ' 〜 ' + childInput2.value;
                                allItemsSearchKeywords = allItemsSearchKeywords + '</span>　';
                            } else if (childInput2.value === '' && childInput.value !== '') {
                                allItemsSearchKeywords = allItemsSearchKeywords + '<span>' + ' 〜 ';
                                allItemsSearchKeywords = allItemsSearchKeywords + '</span>　';
                            } else if (childInput.value !== '' && childInput2.value !== '') {
                                allItemsSearchKeywords = allItemsSearchKeywords + '<span>'+ ' 〜 ' + childInput2.value;
                                allItemsSearchKeywords = allItemsSearchKeywords + '</span>　';
                            }
                        }
                        // 日付FROM-TO・時間ありの場合、TO項目＆時間項目の値を出力する：yyyy/MM/dd HH:mm ~ yyyy/MM/dd HH:mm
                        if (childInput2 != null && childInput2.type === 'text') {
                            if (childInput2.value !== '' && childInput2.id == "timeFrom" || childInput2.value !== '' && childInput2.id == "timeTo") {
                                allItemsSearchKeywords = allItemsSearchKeywords + '<span>' + ', ' + childInput2.value;
                                allItemsSearchKeywords = allItemsSearchKeywords + '</span>　';
                            } else if (childInput2.id === "searchMaxSalesPossibleStockCount" || childInput2.id === "searchNoveltyPresentGoodsCountTo" || childInput2.id === "orderPriceTo") {
                                if (childInput.value !== '' && childInput2.value === '') {
                                    allItemsSearchKeywords = allItemsSearchKeywords + '<span>' + ' 〜 ';
                                    allItemsSearchKeywords = allItemsSearchKeywords + '</span>';
                                } else if (childInput.value === '' && childInput2.value !== '') {
                                    allItemsSearchKeywords = allItemsSearchKeywords + '<span class="keywordLabel">' + dl.getElementsByTagName('dt')[0].innerText;
                                    allItemsSearchKeywords = allItemsSearchKeywords + ':</span> ';

                                    allItemsSearchKeywords = allItemsSearchKeywords + '<span>' + ' 〜 ' + childInput2.value;
                                    allItemsSearchKeywords = allItemsSearchKeywords + '</span>  ';
                                } else if (childInput.value !== '' && childInput2.value !== '') {
                                    allItemsSearchKeywords = allItemsSearchKeywords + '<span>' + ' 〜 ' + childInput2.value;
                                    allItemsSearchKeywords = allItemsSearchKeywords + '</span>  ';
                                }
                            }

                        }
                    }
                    // input type=checkbox 項目 && type=radio 項目
                    if (childInput.type === 'hidden' || childInput.type === 'radio') {
                        const childInputCheckboxes = childDd.getElementsByTagName('input');
                        let checkboxItemResult = '';
                        let isResultSearchItem = document.querySelectorAll('#pageNumber');
                        if (isResultSearchItem.length != 0) {
                            Array.from(childInputCheckboxes).forEach(inputCheckbox => {
                                if (inputCheckbox.checked === true) {
                                    if (checkboxItemResult === '') {
                                        //特殊なケース: 受注検索(checkbox: キャンセルした商品も含む)
                                        if (inputCheckbox.name == "notIncludeCancelGoodsFlag") {
                                            checkboxItemResult = checkboxItemResult + '<span class="keywordLabel">' + inputCheckbox.parentElement.innerText;
                                            checkboxItemResult = checkboxItemResult + '</span> ';
                                        } else {
                                            // 項目のラベル
                                            checkboxItemResult = checkboxItemResult + '<span class="keywordLabel">' + dl.getElementsByTagName('dt')[0].innerText;
                                            checkboxItemResult = checkboxItemResult + ':</span>';
                                            // 項目の値
                                            checkboxItemResult = checkboxItemResult + ' <span class="field-search-element">' + inputCheckbox.parentElement.innerText;
                                            checkboxItemResult = checkboxItemResult + '</span>';
                                        }
                                    } else {
                                        // 項目の値
                                        checkboxItemResult = checkboxItemResult + '<span class="field-search-element">'+ ',' + inputCheckbox.parentElement.innerText;
                                        checkboxItemResult = checkboxItemResult + '</span>';
                                    }
                                  //特殊なケース: フリーエリア検索(公開状態/指定日 yyyy/MM/dd HH:mm)
                                } else if (inputCheckbox.type === 'text') {
                                    if (inputCheckbox.value !== '' && inputCheckbox.disabled === false) {
                                        checkboxItemResult = checkboxItemResult  + '<span class="field-search-element">'+ ', ' + inputCheckbox.value;
                                        checkboxItemResult = checkboxItemResult + '</span>';
                                    }
                                }
                            });
                        }
                        if (checkboxItemResult !== '') {
                            // 項目の値
                            allItemsSearchKeywords = allItemsSearchKeywords + checkboxItemResult + '　';
                        }
                    }
                }
                // textareaタグの項目
                const childTextarea = childDd.getElementsByTagName('textarea')[0];
                if (childTextarea != null) {
                    if (childTextarea.value !== '') {
                        // 項目のラベル
                        allItemsSearchKeywords = allItemsSearchKeywords + '<span class="keywordLabel">' + dl.getElementsByTagName('dt')[0].innerText;
                        allItemsSearchKeywords = allItemsSearchKeywords + ':</span> ';
                        // 項目の値
                        allItemsSearchKeywords = allItemsSearchKeywords + '<span>' + childTextarea.value;
                        allItemsSearchKeywords = allItemsSearchKeywords + '</span>　';
                    }
                }
                // selectタグの項目
                const childSelect = childDd.getElementsByTagName('select')[0];
                if (childSelect != null) {
                    if (childSelect.id.includes('conditionColumn')) {
                        let childInputCondition = document.getElementById(childSelect.id.replace('Column', 'Value'));
                        if (childInputCondition.value !== '') {
                            // 項目のラベル
                            allItemsSearchKeywords = allItemsSearchKeywords + '<span class="keywordLabel">' + dl.getElementsByTagName('dt')[0].innerText;
                            allItemsSearchKeywords = allItemsSearchKeywords + ':</span> ';
                            // 項目の値
                            allItemsSearchKeywords = allItemsSearchKeywords + '<span>' + childSelect.options[childSelect.selectedIndex].text;
                            allItemsSearchKeywords = allItemsSearchKeywords + '</span>　';
                        }
                    //    条件項目:すべての項目 条件値:2 条件項目:すべての項目 条件値:1
                    } else if (childSelect.value !== '') {
                        // 項目のラベル
                        allItemsSearchKeywords = allItemsSearchKeywords + '<span class="keywordLabel">' + dl.getElementsByTagName('dt')[0].innerText;
                        allItemsSearchKeywords = allItemsSearchKeywords + ':</span> ';
                        // 項目の値
                        allItemsSearchKeywords = allItemsSearchKeywords + '<span>' + childSelect.options[childSelect.selectedIndex].text;
                        allItemsSearchKeywords = allItemsSearchKeywords + '</span>　';
                    }
                }
            });
            if (allItemsSearchKeywords !== '') {
                // 末尾のコンマ文字を切って出力する
                document.getElementById('allItemsSearchKeywords').innerHTML = allItemsSearchKeywords;
                document.getElementById('allItemsSearchKeywordsDiv').setAttribute("style", "display: inherit");
            }
        }
    });

    // -------------------------------------------------------------------
    // 商品詳細・商品登録更新：アイコン背景色によるテキスト色変更
    // 今後は、style.cssで定義された「.c-item-status」クラスの
    // 「color: #FFFFFF !important;」属性を削除すれば、下記のロジックは不要です
    // -------------------------------------------------------------------
    if ($('.c-item-status.js-icon-textcolor').length) {
        $('.c-item-status.js-icon-textcolor').each(function () {
            var bgcolor = $(this).css('background-color');
            bgcolor = rgbTo16(bgcolor);
            var textcolor = blackOrWhite(bgcolor);
            if (textcolor === 'black') {
                $(this).addClass('txt-black');
            }
        });
    }

    // -------------------------------------------------------------------
    // 確認ボタン押下したかのフラグ
    // -------------------------------------------------------------------
    $("#doConfirm").click(function () {
        $('#isConfirmBtnClicked').val('true');
    });
    $("#doOnceZipCodeAdd").click(function () {
        $('#isOnceZipCodeAddBtnClicked').val('true');
    });
    $("#doRegistUpdate").click(function () {
        $('#isRegistUpdateBtnClicked').val('true');
    });
    $("#doOnceUpload").click(function () {
        $('#isOnceUploadBtnClicked').val('true');
    });
    $("#doOncePartialUpload").click(function () {
        $('#isOnceUploadBtnClicked').val('true');
    });
    $("#doOnceZipUpload").click(function () {
        $('#isOnceUploadBtnClicked').val('true');
    });
    $("#doOnceFileUpload").click(function () {
        $('#isOnceUploadBtnClicked').val('true');
    });
    $("#doNext").click(function () {
        $('#isNextBtnClicked').val('true');
    });
    $("#doConfirmSendPage").click(function () {
        $('#isConfirmSendPageBtnClicked').val('true');
    });
    $("#doSendTestMail").click(function () {
        $('#isSendTestMail').val('true');
    });
    $("#doOnceUpdate").click(function () {
        $('#isOnceUpdateBtnClicked').val('true');
    });
    $("#doSearch").click(function () {
        $('#isSearchBtnClicked').val('true');
    });
    $("#doCompletion").click(function (e) {
        $('#isCompletionBtnClicked').val('true');
    });
    $("#doOnceRegistStatus").click(function () {
        $('#isRegistStatusBtnClicked').val('true');
    });
    $("#doOnceRelationMember").click(function () {
        $('#isOnceRelationMemberBtnClicked').val('true');
    });
    $("#doOnceRelationMemberRelease").click(function () {
        $('#isOnceRelationMemberBtnClicked').val('true');
    });
    $("#doOnceRegistMemo").click(function () {
        $('#isOnceRegistMemoBtnClicked').val('true');
    });
});

<!-- Summernote (Thymeleaf Rich Text Editor - WYSIWYG) の属性の初期化 -->
function initSummernote(textAreaId) {
    $("#" + textAreaId).summernote({
        height : 200,
        placeholder : '入力してください',
        toolbar: [
            ['style', ['bold', 'italic', 'underline', 'clear']],
            ['font', ['strikethrough']],
            ['fontsize', ['fontsize']],
            ['color', ['color']],
            ['para', ['ul', 'ol', 'paragraph']],
            ['height', ['height']],
            ['table', ['table']],
            ['insert', ['link', 'picture', 'video']],
            ['view', ['codeview']]
        ]
    });
}

<!-- 画像プレビュー表示の初期化 -->
function initImagePreviewer() {
    $('.imagePreviewer img').each(function () {
        var item = this;
        var itemWidth = item.naturalWidth;
        var itemHeight = item.naturalHeight;
        var itemSrc = item.src;
        var linkSource = '<div class="photobox"><a href="' + itemSrc + '" class="thickbox" rel="imagePreviewer" title="' + itemWidth + 'x' + itemHeight + '"></a></div>';
        $(item).wrap(linkSource);
    });
}

<!-- jQuery.browserのマイグレーション（thickbox.jsで使っている）-->
// jquery1.9以降、$.browserが削除されたため、画像プレビュー時にエラーが出てしまう
// 以下のロジックを追加すれば解消できる
jQuery.browser = {};
(function () {
    jQuery.browser.msie = false;
    jQuery.browser.version = 0;
    if (navigator.userAgent.match(/MSIE ([0-9]+)\./)) {
        jQuery.browser.msie = true;
        jQuery.browser.version = RegExp.$1;
    }
})();

<!-- アイコン背景色によるテキスト色変更 -->
// 「function.js」にもあるため今後は削除予定
function blackOrWhite ( hexcolor ) {
    var r = parseInt( hexcolor.substr( 1, 2 ), 16 ) ;
    var g = parseInt( hexcolor.substr( 3, 2 ), 16 ) ;
    var b = parseInt( hexcolor.substr( 5, 2 ), 16 ) ;

    return ( ( ( (r * 299) + (g * 587) + (b * 114) ) / 1000 ) < 128 ) ? "white" : "black" ;
}
function rgbTo16(col){
    return "#" + col.match(/\d+/g).map(function(a){return ("0" + parseInt(a).toString(16)).slice(-2)}).join("");
}

<!-- 入力チェックでエラー発生した場合の汎用的な取り扱い -->
// ・各要素のパネル（articleエレメント）に赤いborderを設定
// ・エラーのある1つ目のarticleエレメントに自動スクロール
function displayArticleError(containerId) {
    // エラーがあった場合、notificationを表示
    if ($(document).find('.error').length > 0) {
        // エラーメッセージを準備
        var title = 'エラー';
        var message = '入力エラーが 【' + $(document).find('.error').length + '件】 発生しています <br> 入力内容を確認してください';
        displayNotification('error', title, message);
    }

    // 対象コンテナを取得
    var container;
    if (containerId === null || containerId === '') {
        // コンテナIDが指定されなかった場合、document全体を設定
        container = document;
    } else {
        // コンテナIDが指定された場合、対象コンテナのHTMLエレメントを取得
        container = document.getElementById(containerId);
    }
    // 全てのarticleパネルを取得
    var articles = container.getElementsByTagName('article');
    // 自動スクロールのために、初めてエラーがあったarticleを保存
    var firstArticleError = null;
    for (let index = 0; index < articles.length; ++index) {
        if (articles[index].getElementsByClassName('error').length > 0) {
            articles[index].classList.add('error');
            if (firstArticleError === null) {
                firstArticleError = articles[index];
            }
        }
    }
    if (firstArticleError !== null) {
        firstArticleError.scrollIntoView({block: 'center',  behavior: 'smooth'});
    }
}

<!-- articleへの自動スクロールが実施されたかのチェック -->
// ・1つだけのarticleにerrorクラスがある場合、Trueを返す
// ・全てのarticleにerrorクラスがない場合、Falseを返す
function isAutoScrollToArticleError() {
    return (document.querySelectorAll('article.error').length > 0);
}

<!-- 入力チェックでエラー発生した後、各input項目に値が入力されたら発動 -->
// 対象input項目の種類は以下の通り
// ・input[type="text"]
// ・input[type="radio"]
// ・input[type="checkbox"]
// ・select
// ・textarea
function removeParentArticleErrorOnChange() {
    // テキストボックスに変更を加えたら発動
    $('input[type="text"]').change(function() {
        if ($(this).hasClass('error')) {
            // 自分のerrorクラスを削除
            $(this).removeClass('error');

            // 該当項目のエラーメッセージを削除
            // 以下の汎用的な実装だと全パターンが対応されていない（エラーメッセージ出力のdiv構成によって異なるため）
            // 一旦、サンプルとして、一番多いパターンを対象し実装してみる
            // 今後は、エラーメッセージ出力のdivに対象項目に紐付ける「id」を付与されば、全パターンが対応可能
            var errorMsgDiv = $(this).parent().find('div.c-error-txt')[0];
            if (typeof errorMsgDiv !== "undefined" && errorMsgDiv !== null) {
                errorMsgDiv.remove();
            } else {
                errorMsgDiv = document.getElementById(this.id + 'Error');
                if (typeof errorMsgDiv !== "undefined" && errorMsgDiv !== null) {
                    errorMsgDiv.remove();
                }
            }

            // 親articleのerrorクラスを確認・削除
            removeParentArticleError(this);
        }
    });

    // インプットのファイルに変更を加えたら発動
    $('input[type="file"]').change(function() {
        if ($(this).parent().find('input').eq(1).hasClass('error')) {
            // 自分のerrorクラスを削除
            $(this).parent().find('input').eq(1).removeClass('error');

            var errorMsgDiv = document.getElementById(this.id + 'Error');
            if (typeof errorMsgDiv !== "undefined" && errorMsgDiv !== null) {
                errorMsgDiv.remove();
            }
            // 親articleのerrorクラスを確認・削除
            removeParentArticleError(this);
        }
    });

    // インプットのパスワードに変更を加えたら発動
    $('input[type="password"]').change(function() {
        if ($(this).parent().find('input').eq(0).hasClass('error')) {
            // 自分のerrorクラスを削除
            $(this).parent().find('input').eq(0).removeClass('error');

            var errorMsgDiv = document.getElementById(this.id + 'Error');
            if (typeof errorMsgDiv !== "undefined" && errorMsgDiv !== null) {
                errorMsgDiv.remove();
            }
            // 親articleのerrorクラスを確認・削除
            removeParentArticleError(this);
        }
    });

    // テキストアリアに変更を加えたら発動
    $('textarea').change(function() {
        if ($(this).parent().find('textarea').eq(0).hasClass('error')) {
            // 自分のerrorクラスを削除
            $(this).parent().find('textarea').eq(0).removeClass('error');

            var errorMsgDiv = document.getElementById(this.id + 'Error');
            if (typeof errorMsgDiv !== "undefined" && errorMsgDiv !== null) {
                errorMsgDiv.remove();
            }
            // 親articleのerrorクラスを確認・削除
            removeParentArticleError(this);
        }
    });

    // ラジオボタンをチェックしたら発動
    $('input[type="radio"]').change(function() {
        if ($(this).parent().find('i').eq(0).hasClass('error')) {
            // 自分のerrorクラスを削除
            $(this).parent().find('i').eq(0).removeClass('error');

            // 親articleのerrorクラスを確認・削除
            removeParentArticleError(this);
        }
    });

    // チェックボックスをチェック＆チェック解除したら発動
    $('input[type="checkbox"]').change(function() {
        if ($(this).parent().find('i').eq(0).hasClass('error')) {
            // 自分のerrorクラスを削除
            $(this).parent().find('i').eq(0).removeClass('error');

            // 親articleのerrorクラスを確認・削除
            removeParentArticleError(this);
        }
    });

    // セレクトボックスが切り替わったら発動
    $('select').change(function() {
        if ($(this).hasClass('error')) {
            // 自分のerrorクラスを削除
            $(this).removeClass('error');
            var errorMsgDiv = document.getElementById(this.id + 'Error');
            if (typeof errorMsgDiv !== "undefined" && errorMsgDiv !== null) {
                errorMsgDiv.remove();
            }
            // 親articleのerrorクラスを確認・削除
            removeParentArticleError(this);
        }
    });

    // テキストエリアに変更を加えたら発動
    $('textarea').change(function() {
        if ($(this).hasClass('error')) {
            // 自分のerrorクラスを削除
            $(this).removeClass('error');

            // 親articleのerrorクラスを確認・削除
            removeParentArticleError(this);
        }
    });
}

<!-- 親articleのerrorクラスを削除 -->
function removeParentArticleError(obj) {
    // article内のエラーのある全項目に値が変更されたかをチェック
    if ($(obj).closest('article.error').find('.error').length === 0) {
        // 商品登録更新での規格設定＆在庫設定のエラーがあれば削除（特別な扱い）
        if ($(obj).closest('article.error').find('#unitItemsError').length > 0) {
            $('#unitItemsError').fadeOut(100);
        }
        if ($(obj).closest('article.error').find('#stockItemsError').length > 0) {
            $('#stockItemsError').fadeOut(100);
        }

        // 親articleのerrorクラス（赤いborder）を削除
        $(obj).closest('article.error').addClass('not-error');
        $(obj).closest('article.error').removeClass('error');
    }
}

<!-- Form-Submitの時に、フィールドエラーが残っている場合、notificationを表示 -->
// エラーとなった全項目に値が変更されたかのチェックだけで、
// 入力された値の正当性はバックエンド側で再度検証する
// ・エラーが全て消えた場合、Trueを返す
// ・エラーが残っている場合、Falseを返す
function errorCheckOnclickConfirmButton() {

    // 確認ボタン以外を押下した場合は処理なし
    if ($('#isConfirmBtnClicked').val() === 'false') {
        return true;
    }
    if ($('#isOnceZipCodeAddBtnClicked').val() === 'false') {
        return true;
    }
    if ($('#isRegistUpdateBtnClicked').val() === 'true' && $('#isRegistUpdateBtnClicked').closest('.error').length === 0) {
        $('#isRegistUpdateBtnClicked').val(false)
        return true;
    }
    if ($('#isOnceUploadBtnClicked').val() === 'false') {
        return true;
    }
    if ($('#isOnceZipCodeAddBtnClicked').val() === 'false') {
        return true;
    }
    if ($('#isNextBtnClicked').val() === 'false') {
        return true;
    }
    if ($('#isConfirmSendPageBtnClicked').val() === 'true' && $('#isConfirmSendPageBtnClicked').closest('.error').length === 0) {
        $('#isConfirmSendPageBtnClicked').val(false)
        return true;
    }
    if ($('#isSendTestMail').val() === 'true' && $('#isSendTestMail').closest('.error').length === 0) {
        $('#isSendTestMail').val(false)
        return true;
    }
    if ($('#isOnceUpdateBtnClicked').val() === 'false') {
        return true;
    }
    if ($('#isSearchBtnClicked').val() === 'true' && $('#isSearchBtnClicked').closest('.error').length === 0) {
        $('#isSearchBtnClicked').val(false)
        return true;
    }
    if( $('#isRegistStatusBtnClicked').val() === 'true' && $('#isRegistStatusBtnClicked').closest('.error').length === 0){
        $('#isRegistStatusBtnClicked').val(false)
        return true;
    }
    if( $('#isOnceRelationMemberBtnClicked').val() === 'true' && $('#isOnceRelationMemberBtnClicked').closest('.error').length === 0){
        $('#isOnceRelationMemberBtnClicked').val(false)
        return true;
    }
    if( $('#isCompletionBtnClicked').val() === 'true' && $('#isCompletionBtnClicked').closest('.error').length === 0){
        $('#isCompletionBtnClicked').val(false)
        return true;
    }
    if( $('#isOnceRegistMemoBtnClicked').val() === 'true' && $('#isOnceRegistMemoBtnClicked').closest('.error').length === 0){
        $('#isOnceRegistMemoBtnClicked').val(false)
        return true;
    }

    // エラー件数を算出
    var totalErrorClass = $(document).find('.error').length;
    var articleErrorClass = $(document).find('article.error').length;
    var notiErrorClass = $(document).find('.ncf.error').length;
    var fieldErrorCount = totalErrorClass - articleErrorClass - notiErrorClass;

    // エラーが残っている場合、notificationを表示し、Falseを返すことでForm-Submit処理を中止
    if (fieldErrorCount > 0) {
        $('form').data('submitted', false)
        // エラーメッセージを準備
        var title = 'エラー';
        var message = '入力エラーが 【' + fieldErrorCount + '件】 発生しています <br> 入力内容を確認してください';
        displayNotification('error', title, message);
        document.querySelectorAll('article.error')[0].scrollIntoView({block: 'center',  behavior: 'smooth'});
        // 処理後に確認ボタン押下したかのフラグをリセット
        $('#isConfirmBtnClicked').val('false');
        $('#isOnceZipCodeAddBtnClicked').val('false');
        $('#isRegistUpdateBtnClicked').val('false');
        $('#isOnceUploadBtnClicked').val('false');
        $('#isNextBtnClicked').val('false');
        $('#isConfirmSendPageBtnClicked').val('false');
        $('#isOnceUpdateBtnClicked').val('false');
        $('#isSearchBtnClicked').val('false');
        $('#isCompletionBtnClicked').val('false');
        $('#isRegistStatusBtnClicked').val('false');
        $('#isOnceRelationMemberBtnClicked').val('false');
        $('#isOnceRegistMemoBtnClicked').val('false');

        return false;
    }

    return true;
}

<!-- 入力チェックでエラーがあった場合等、notificationを表示 -->
// ・画面の右上に表示
// ・マウスクリックで閉じることが可能
// ・メッセージの表示は一旦「7秒」を設定（表示してから7秒後に自動的に消える）
function displayNotification(theme, title, message) {
    window.createNotification({
        closeOnClick: true,
        displayCloseButton: true,
        positionClass: 'nfc-top-right',
        showDuration: 7000,
        theme: theme
    })({
        title: title,
        message: message
    });
}

/*
 * 配送方法設定のチェックボックス全てチェック
 *
 */
function allCheckDelivery(){
    let startIndex = 0;
    const endIndex = 100;
    while (true) {
        const id = 'deliveryPrefectureCarriageItems' + startIndex + '.activeFlag1';
        const checkbox = $id(id);
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
 * 配送方法設定のチェックボックス全てチェックをはずす
 *
 */
function allUnCheckDelivery(){
    let startIndex = 0;
    const endIndex = 100;
    while (true) {
        const id = 'deliveryPrefectureCarriageItems' + startIndex + '.activeFlag1';
        const checkbox = $id(id);
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
