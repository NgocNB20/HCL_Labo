/**
 * 商品詳細URL
 */
const goodsDetailUrl = location.protocol + '//' + location.host + '/goods/detail?ggcd=';

/**
 * Facebookシェア<br/>
　* 商品詳細のURLがlocalhostだと、Facebook画面で「Parameter 'href' should represent a valid URL」となる
 */
function facebookShare(ggcd) {

	link = "https://www.facebook.com/share.php?u=" + encodeURIComponent(goodsDetailUrl + ggcd);

	window.open(
		link,
		"facebook",
		"_blank",
		"noopener=yes,noreferrer=yes,menubar=no,toolbar=no,width=550,height=450"
	);
}

/**
 * Twitterシェア<br/>
 * メンションはencodeURIComponentの場合、認識しないのでencodeURIで対応
 */
function tweetShare(ggcd, via) {
	if (via) {
		link = "https://twitter.com/intent/tweet?url=" + encodeURIComponent(goodsDetailUrl + ggcd) + "&via=" + via;
	} else {
		link = "https://twitter.com/intent/tweet?url=" + encodeURIComponent(goodsDetailUrl + ggcd);
	}

	window.open(
		link,
		"Twitter",
		"_blank",
		"noopener=yes,noreferrer=yes,menubar=no,toolbar=no,width=550,height=450"
	);
}

/**
 * Lineシェア
 */
function lineShare(ggcd) {

	link = "https://social-plugins.line.me/lineit/share?url=" + encodeURIComponent(goodsDetailUrl + ggcd);

	window.open(
		link,
		"Line",
		"_blank",
		"noopener=yes,noreferrer=yes,menubar=no,toolbar=no,width=550,height=450"
	);
}

