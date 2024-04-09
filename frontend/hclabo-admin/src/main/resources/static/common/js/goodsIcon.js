/* name=iconDispのタグに対して、背景色より自動で文字の白黒を判定
-----------------------------------------------------------*/
$(document).ready(function(){
	$('[name=iconDisp]').each(function(i) {
		  var bColor = rgbtohex($(this).css('background-color'));
		  $(this).css('color',blackOrWhite(bColor));
	});
});

/* 引数で指定した背景色から適した文字色を返却(白か黒)
-----------------------------------------------------------*/
function blackOrWhite ( hexcolor ) {
	var r = parseInt( hexcolor.substr( 1, 2 ), 16 ) ;
	var g = parseInt( hexcolor.substr( 3, 2 ), 16 ) ;
	var b = parseInt( hexcolor.substr( 5, 2 ), 16 ) ;

	return ( ( ( (r * 299) + (g * 587) + (b * 114) ) / 1000 ) < 128 ) ? "white" : "black" ;
}

/* RGBをHEXに変換
-----------------------------------------------------------*/
function rgbtohex(orig){
	var rgb = orig.replace(/\s/g,'').match(/^rgba?\((\d+),(\d+),(\d+)/i);
	return (rgb && rgb.length === 4) ? "#" +
				("0" + parseInt(rgb[1],10).toString(16)).slice(-2) +
				("0" + parseInt(rgb[2],10).toString(16)).slice(-2) +
				("0" + parseInt(rgb[3],10).toString(16)).slice(-2) : orig;
}