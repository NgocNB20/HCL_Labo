// プレビュー画面のリンク遷移、ボタン選択、フォーム操作を制御
jQuery(function($){
    $('.l-page input, .l-page button, .l-footer input, .l-footer button').each(function() {
        $(this).wrap("<span></span>");
        $(this).css({
            pointerEvents: "none",
        });
        $(this).parent().css({
            cursor: "not-allowed"
        })
    });
    $('.l-page a, .l-header__inner a, .l-header__inner input, .l-header__inner button, .l-footer a:not(#pagetop_icon), #nav-slide-sp a, .l-header__global a').each(function() {
        $(this).css({
            pointerEvents: "none",
        });
        $(this).parent().css({
            cursor: "not-allowed"
        })
    });
});
