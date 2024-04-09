﻿/*********************
//* jQuery Multi Level CSS Menu (horizontal)- By Dynamic Drive DHTML code library: http://www.dynamicdrive.com
//* Menu instructions page: http://www.dynamicdrive.com/dynamicindex1/ddlevelsmenu/
//* Last modified: Sept 6th, 08'. Usage Terms: http://www.dynamicdrive.com/style/csslibrary/tos/
*********************/

var jquerycssmenu={

fadesettings: {overduration: 150, outduration: 30}, //duration of fade in/ out animation, in milliseconds

buildmenu:function(menuid){
	jQuery(document).ready(function($){
		var $mainmenu=$("#"+menuid+">ul")
		var $headers=$mainmenu.find("ul").parent()
		$headers.each(function(i){
			var $curobj=$(this)
			var $subul=$(this).find('ul:eq(0)')
			this._dimensions={w:this.offsetWidth, h:this.offsetHeight, subulw:$subul.outerWidth(), subulh:$subul.outerHeight()}
			this.istopheader=$curobj.parents("ul").length==1? true : false
			$subul.css({top:"34px"})

			$curobj.hover(
				function(e){
					$(this).addClass("over");
					var $targetul=$(this).children("ul:eq(0)")
					this._offsets={left:$(this).offset().left, top:$(this).offset().top}
					var menuleft=this.istopheader? 0 : this._dimensions.w
					menuleft=(this._offsets.left+menuleft+this._dimensions.subulw>$(window).width())? (this.istopheader? -this._dimensions.subulw+this._dimensions.w : -this._dimensions.w) : menuleft
					$targetul.css({left:0}).fadeIn(jquerycssmenu.fadesettings.overduration)
					// IE6 z-index 無視対策
					if (typeof document.body.style.maxHeight != "undefined") {
						// IE 7, mozilla, safari, opera 9
					} else {
						// IE6, older browsers
						$($targetul).bgiframe();
					}
				},
				function(e){
					$(this).removeClass("over");
					$(this).children("ul:eq(0)").fadeOut(jquerycssmenu.fadesettings.outduration)
				}
			) //end hover
		}) //end $headers.each()
		$mainmenu.find("ul").css({display:'none', visibility:'visible'})
	}) //end document.ready
}
}

jquerycssmenu.buildmenu("globalNav")