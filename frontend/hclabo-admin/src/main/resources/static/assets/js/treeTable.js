var newList, item, iChildLast,idParentManyChild,nextDisplay, beforeMove;
var ROWS_HIDE_DISPLAY = 50;
$(function() {
	//initial class
	SetIC();
	//reset background color row
	reset();
	//call event
	$("#targetCategory table tr td div.c-open-btn").unbind().on('click', ImageClick);
//	$("#targetCategory table tr td div.c-open-btn").unbind().on('click', EAllClick);
//	$('#targetCategory table tr td').unbind().on('click', CAllClick);

	EAllClick();
	// 「表示順を登録」ボタンが表示される場合のみ、行の並び替え可能とする
	if($('#doOrder')[0]){
		//Set event drag drop for table
		DragDropTable();
	}
});

$('#doOrder').on('click',function(){
	var val = $('#dtCategory').val();
	if(val=="draged")
	{
	    $('#dtCategory').val("");
	    var lst = $('#targetCategory table tbody .isdraged');
	    //check data is draged
	    if(lst.length > 0)
	    {
			//create data post
			GetDataDragDrop();
	    }
	}

});

//overide sortable to process for dragdrop slowly
$.ui.sortable.prototype.refreshPositions = function(fast) {
    //This has to be redone because due to the item being moved out/into the offsetParent, the offsetParent's position will change
    if(this.offsetParent && this.helper) {
        this.offset.parent = this._getParentOffset();
    }

    for (var i = this.items.length - 1; i >= 0; i--){
        var item = this.items[i];

        //We ignore calculating positions of all connected containers when we're not over them
        if(item.instance != this.currentContainer && this.currentContainer && item.item[0] != this.currentItem[0])
            continue;

        var t = this.options.toleranceElement ? $(this.options.toleranceElement, item.item) : item.item;

        if (!fast) {
            /********** MODIFICATION ***********/

            if(item.item.css('display') === 'none') {
                item.width = 0;
                item.height = 0;
            } else {
                item.width = t.outerWidth();
                item.height = t.outerHeight();
            }

            /********** END MODIFICATION ***********/
        }

        var p = t.offset();
        item.left = p.left;
        item.top = p.top;
    };

    if(this.options.custom && this.options.custom.refreshContainers) {
        this.options.custom.refreshContainers.call(this);
    } else {
        for (var i = this.containers.length - 1; i >= 0; i--){

            /********** MODIFICATION ***********/

            if (this.containers[i].element.css('display') == 'none') {
                this.containers[i].containerCache.left = 0;
                this.containers[i].containerCache.top = 0;
                this.containers[i].containerCache.width = 0;
                this.containers[i].containerCache.height = 0;
            } else {
                var p = this.containers[i].element.offset();
                this.containers[i].containerCache.left = p.left;
                this.containers[i].containerCache.top = p.top;
                this.containers[i].containerCache.width = this.containers[i].element.outerWidth();
                this.containers[i].containerCache.height = this.containers[i].element.outerHeight();
            }

            /********** END MODIFICATION ***********/
        };
    }

    return this;
};

//Create drag drop for table
function DragDropTable()
{
	$('#targetCategory table tbody').sortable({
        connectWith: "tr"
    }).disableSelection();

	$('#targetCategory table tbody').on('mousedown', 'tr', function(e){
	    $(this).addClass('selectedRow').siblings().removeClass('selectedRow');
	    //get data
	    var idOfParent = $(this).attr('id');
	    var classOfParent = $(this).attr('class');
	    $('#targetCategory table').find('tr.pa-'+idOfParent).addClass('selectedRow');
		//addClassSelect(idOfParent);
		if(idOfParent.length==16)
		{
			$(this).nextUntil('tr.pa-10000000').addClass('selectedRow');
		}

		if(idOfParent.length>16)
		{
			//addClassSelect(idOfParent);
			var $lst = $('#targetCategory table tbody').find("tr[id^='"+idOfParent+"']");
			$lst.each(function(){
				$(this).addClass("selectedRow")
			});
		}

	}).sortable({
	axis:   'y',
	items: 'tr:not(tr:first-child)',
	connectWith: 'table',
	cursor: 'move',
	delay: 150,
	revert: 0,
	cursorAt: { cursor: "move", top: 0, left: 0 },
	helper: function(e, item){
		if(!item.hasClass('selectedRow')){
		   item.addClass('selectedRow').siblings().removeClass('selectedRow');
		}
		//Get data drag
		var elements = item.parent().children('.selectedRow').clone();
		item.data('multidrag', elements).siblings('.selectedRow').remove();

		var helper = $('<tr/>');
		return helper.append(elements);
	},
	// 並び替えが開始時、呼び出し
	start: function(event, ui) {
	    item = ui.item;
	    ui.helper.css('padding-top', 0 );
		//init iChildLast
		iChildLast = ' ';
		//init next record is display
		nextDisplay = 0;
		//backup data
	    newList =  ui.item.data('multidrag');
	    idParentManyChild = item.attr('class').split('-')[1].split(' ')[0];
	    //get previous id before move
	    beforeMove = $('#'+newList.attr('id')).prev().attr('id');
	    //initial data
	    $('#dtCategory').val("");
	},
	// 並び替えが終了時、呼び出し
		stop: function(e, ui){

		//get id parent present
		var idPa = " ";
		$('#'+newList.attr('id')).attr('class').split(' ').map(function(clssName){
		  if (clssName.startsWith("pa-"))
		  {
			  idPa = clssName;
		  }
		});

		//Get tr move present
		var $trMovePresent = $('#'+newList.attr('id'));
		//Get Row Display Not Node
		NextRowDisplayNotNone($('tr.selectedRow:last').attr('id'));

		//get id parent pre
		var idPaPre = " "
		$('#'+newList.attr('id')).prev().attr('class').split(' ').map(function(clssName){
			if (clssName.startsWith("pa-"))
			{
				  idPaPre = clssName;
			}
		});

		// 制限された階層に移動した場合、元の行に戻す
		CheckLevelRow(newList,idPa,idPaPre,this);

		var elements = ui.item.data('multidrag');
		ui.item.after(elements).remove();


		//Get tr on tr move
		var $trPre = $('#'+newList.attr('id')).prev();

		//Get tr below tr move
		var $trNext = $('#'+newList.attr('id')).next();

		//get length space of pre id
		var space = $trPre.find('span.ispace').text().length;
		var $listChild = $('tr.pa-'+ $trMovePresent.attr('id'));

		//check object move, child?
		var $lstExChild = $('#targetCategory table tbody').find('tr.pa-'+ $trMovePresent.attr('id'));

		//Get tr move present
		var $trMovePresent = $('#'+newList.attr('id'));

		//Get tr on tr move
		var $trPre = $('#'+newList.attr('id')).prev();

		//Get tr below tr move
		var $trNext = $('#'+newList.attr('id')).next();

		//get length space of pre id
		var space = $trPre.find('span.ispace').text().length;

		//check object move, have child?
		var $lstExChild = $('#targetCategory table tbody').find('tr.pa-'+ $trMovePresent.attr('id'));

		//Get row id is folding when child is closing
		var idRowOpen = CheckRowOpenWhenChildClose($trPre.attr('id'));
		//remove class isdraged
		$('#targetCategory table tbody tr').removeClass('isdraged');

		//case object moving not have child
		if($lstExChild.length == 0 && beforeMove != $trPre.attr('id'))
		{
			//get rowid next
			var idPaNext = GetIdPaNex(newList);

			//check present row diff next row
			//case present row like next row is not change level and change id
			if((idPa != idPaNext))
			{
				//check present row  diff previous row
				if (idPa != idPaPre) {
					//Compare present row and previous row like parent
					//case like is not change level and not change id
					//case unlike is change level and change id
					var like = CheckLikeParent(idPaPre,idPa,idPaNext);
					if(like == 'unlike' && idPa.length != 8)
					{
						//check row is folding
						if(idRowOpen != " ")
						{
							var $trPreOpen = $('#'+idRowOpen);
							//open all child is folding
							CloseOpenChild($trPreOpen.attr('id'));
							//change image
							$trPreOpen.find("#idx").addClass('close');
							//Create space from row is folding
							space = $trPreOpen.find('span.ispace').text().length;
							//remove class
							$trMovePresent.removeClass();
							//create new class
							$trMovePresent.addClass('pa-'+$trPreOpen.attr('id'));
							$trMovePresent.addClass('isdraged');
							//set new level
							$trMovePresent.find('.ispace').text(createSpace(space));
							//create new id
							var idNew = CreateIdNew($trPreOpen.attr('id'),$trMovePresent.attr('id'));
							$trMovePresent.attr('id', idNew);
						}
						//row is not folding
						else
						{
							//get id and compare previous row and next row
							var idLike = CheckLikeParentPrevNext(idPaPre,idPa,idPaNext);
							if(idLike != " ")
							{
								//create space from parent
								var spaceParent = $('#'+idLike).find('span.ispace').text().length;
								//create class
								$trMovePresent.removeClass();
								//create new class
								$trMovePresent.addClass('pa-'+ $('#'+idLike).attr('id'));
								$trMovePresent.addClass('isdraged');
								//set new level
								$trMovePresent.find('.ispace').text(createSpace(spaceParent));
								//create new id
								var idNew = CreateIdNew($('#'+idLike).attr('id'),$trMovePresent.attr('id'));
								$trMovePresent.attr('id', idNew);
							}
							else
							{
								//create class
								$trMovePresent.removeClass();
								//create new class
								$trMovePresent.addClass('pa-'+$trPre.attr('id'));
								$trMovePresent.addClass('isdraged');
								//set new level
								$trMovePresent.find('.ispace').text(createSpace(space));
								//create new id
								var idNew = CreateIdNew($trPre.attr('id'),$trMovePresent.attr('id'));
								$trMovePresent.attr('id', idNew);
								//change image
								$trPre.find("#idx").addClass('close');
//								$trPre.find('i.btn-img').addClass("size-pm");
							}

						}
					}
				}
				else //present row like previous row
				{
					//check previous row have child
					var $listChild = $('tr.pa-'+ $trPre.attr('id'));
					if($listChild.length > 0)
					{
						$trMovePresent.removeClass();
						//create new class
						$trMovePresent.addClass('pa-'+$trPre.attr('id'));
						$trMovePresent.addClass('isdraged');
						//set new level
						$trMovePresent.find('.ispace').text(createSpace(space));
						//create id new
						var idNew = CreateIdNew($trPre.attr('id'),$trMovePresent.attr('id'));
						$trMovePresent.attr('id', idNew);
						//change image
						$trPre.find("#idx").addClass('close');
//						$trPre.find('i.btn-img').addClass("size-pm");
					}
				}
			}
		}

		//case object moving have child
		if ($lstExChild.length > 0 && beforeMove != $trPre.attr('id')) {
			//Get last row of drag row
			GetChildLast($trMovePresent.attr('id'));
			//get next row from last row of drag
			var $idNext = $('#'+ iChildLast).next();
			//get class of next row
			var clsNext = ' ';
			if ($idNext.length != 0) {
				$idNext.attr('class').split(' ').map(function(clssName){

					if (clssName.startsWith("pa-"))
					{
						  clsNext = clssName;
					}
				});
			}

			if (iChildLast != ' ' ) {
				//check present row diff next row
				//case present row like next row is not change level and change id
				if(clsNext !=  idPa)
				{
					//check present row  diff previous row
					if (idPa != idPaPre) {
						//Compare present row and previous row like parent
						//case like is not change level and not change id
						//case unlike is change level and change id
						var like = CheckLikeParent(idPaPre,idPa,clsNext);
						if(like == 'unlike')
						{
							//check row is folding
							if(idRowOpen != " ")
							{
								var $trPreOpen = $('#'+idRowOpen);
								//open all child is folding
								CloseOpenChild($trPreOpen.attr('id'));
								//change image
								$trPreOpen.find("#idx").addClass('close');
								//Get space from parent
								space = $trPreOpen.find('span.ispace').text().length;
								//create level new
								$trMovePresent.find('.ispace').text(createSpace(space));
								$trMovePresent.removeClass();
								//create new class
								$trMovePresent.addClass('pa-'+$trPreOpen.attr('id'));
								$trMovePresent.addClass('isdraged');
								//Get present id
								var idParentOld = $trMovePresent.attr('id');
								//create new id
								var idNew = CreateIdNew($trPreOpen.attr('id'),$trMovePresent.attr('id'));
								$trMovePresent.attr('id', idNew);
								//move child and create id, level for all child
								MoveManyChild(idParentOld,idNew,space);
							}
							else
							{
								//get id and compare previous row and next row
								var idLike = CheckLikeParentPrevNext(idPaPre,idPa,clsNext);
								if(idLike != " ")
								{
									//create level new
									var spaceParent = $('#'+idLike).find('span.ispace').text().length;
									$trMovePresent.find('.ispace').text(createSpace(spaceParent));
									$trMovePresent.removeClass();
									//create new class
									$trMovePresent.addClass('pa-'+$('#'+idLike).attr('id'));
									$trMovePresent.addClass('isdraged');
									//get present id
									var idParentOld = $trMovePresent.attr('id');
									//create new id
									var idNew = CreateIdNew($('#'+idLike).attr('id'),$trMovePresent.attr('id'));
									//set again id
									$trMovePresent.attr('id', idNew);
									//move child and create id, level for all child
									MoveManyChild(idParentOld,idNew,spaceParent);
								}
								else
								{
									$trMovePresent.find('.ispace').text(createSpace(space));
									$trMovePresent.removeClass();
									//$trMovePresent.attr('id',$trPre.attr('id')+idGo);
									$trMovePresent.addClass('pa-'+$trPre.attr('id'));
									$trMovePresent.addClass('isdraged');
									//create level new
									var spacePresent = $trMovePresent.find('.ispace').text().length;
									if(space < spacePresent)
									{
										$trPre.find("#idx").addClass('close');
//										$trPre.find('i.btn-img').addClass("size-pm");
									}
									//get present id
									var idParentOld = $trMovePresent.attr('id');
									//create new id
									var idNew = CreateIdNew($trPre.attr('id'),$trMovePresent.attr('id'));
									//set again id
									$trMovePresent.attr('id', idNew);
									//move child and create id, level for all child
									MoveManyChild(idParentOld,idNew,space);
								}
							}
						}
					}
					else //present row like previous row
					{
						var $listChild = $('tr.pa-'+ $trPre.attr('id'));
						if($listChild.length > 0)
						{
							$trMovePresent.find('.ispace').text(createSpace(space));
							$trMovePresent.removeClass();
							$trMovePresent.addClass('pa-'+$trPre.attr('id'));
							//
							$trMovePresent.addClass('isdraged');
							//get present level
							var spacePresent = $trMovePresent.find('.ispace').text().length;
							//check present level and level change
							//case present level < level change is change button
							if(space < spacePresent)
							{
								$trPre.find("#idx").addClass('close');
//								$trPre.find('i.btn-img').addClass("size-pm");
							}
							var idParentOld = $trMovePresent.attr('id');
							//create new id
							var idNew = CreateIdNew($trPre.attr('id'),$trMovePresent.attr('id'));
							$trMovePresent.attr('id', idNew);
							//move child and create id, level for all child
							MoveManyChild(idParentOld,idNew,space);
						}
					}
				}
			}

		}

		//reset row
		reset();

		//reset image when not child
		var $lstChildTemp = $('#targetCategory table tbody').find('tr.pa-'+ idParentManyChild);
		if($lstChildTemp.length == 0)
		{
			$('#'+idParentManyChild).find("#idx").removeClass('close');
//			$('#'+idParentManyChild).find('i.btn-img').addClass("size-pm");
		}
		//reset button collapse and expanse
//		ResetImgEC();

		$('tr td div.c-open-btn').unbind().on('click', ImageClick);
//		$('#targetCategory table tr td i.btn-goAllClose').unbind().on('click', EAllClick);
//		$('#targetCategory table tr td i.btn-goAllOpen').unbind().on('click', CAllClick);
		//add class isdraged of present row
		$trMovePresent.addClass('isdraged').css('background-color', 'red');
		//Set data temp when draged
		$('#dtCategory').val("draged");
	}
	});
}

// Move child of child
function MoveManyChild(parentOldId,parentId,space)
{
	var $listChild = $('tr.pa-'+parentOldId);
	$listChild.each(function(x){

		$(this).find('.ispace').text(createSpace(space+3));
		//create class relationship
		$(this).removeClass();
		//set relation with parent
		$(this).addClass('pa-'+parentId);

		//save id present
		var idPresent = $(this).attr('id');
		//Count child of row present
		var len = $('tr.pa-'+idPresent).length;
		//create id new when droped
		var idNew = CreateIdNew(parentId,idPresent);
		//Set id for new row
		$(this).attr('id',idNew);
		if(len>0)
		{
			//call recursive when row have many child
			MoveManyChild(idPresent,idNew,space + 3);
		}

	});
}

//Get last row when rowdrag have many child
function GetChildLast(parentId)
{
	var $listChild = $('tr.pa-'+parentId);
	$listChild.each(function(){
		iChildLast = $(this).attr('id');
		var len = $('tr.pa-'+$(this).attr('id')).length;
		if(len>0)
		{
			GetChildLast($(this).attr('id'));
		}
	});
}

//add class selectedRow for row and child row
function addClassSelect(parentId)
{
	var $listChild = $('tr.pa-'+parentId);
	$listChild.each(function(){
		$(this).addClass('selectedRow');
		var len = $('tr.child-'+$(this).attr('id')).length;
		if(len>0)
		{
			addClassSelect($(this).attr('id'));
		}
	});
}

//create space 2 byte
function createSpace(sp)
{
	var text = "";
	for (i = 0; i < sp + 3; i++) {
	  text += "　";
	}
	return text;
}

//reset row
function reset()
{
		var y = 0;
		$('#targetCategory tbody tr').each(function(x) {
			$(this).removeClass("stripe");
			$(this).removeClass("over");
			var display= $(this).css('display');
			if(display === 'none')
			{
				y++;
			}
			else
			{
				if((x+y)%2!=1)
				{
					//check row draged to reset
					var cls = ' ';
					$(this).attr('class').split(' ').map(function(clssName){
					  if (clssName.startsWith("isdraged"))
					  {
						  cls = clssName;
					  }
					});
					if(cls == ' ')
					{
						$(this).addClass("stripe");
					}
				}
			}

		});
}

//set id class when init
function SetIC()
{
	var id = $('#targetCategory table tr td').find(".mypc:first").val();
	var $lst = $('#targetCategory table').find("input[value^='"+id+"']");
	$lst.each(function(x) {
		var value = $(this).val();
		var len = $(this).val().length - 8;
		var inputParent = $(this).val().substr(0, len);
		 $(this).closest('tr').addClass('pa-'+inputParent);
		 $(this).closest('tr').attr('id', value);
	});
}

//Create id for new row
function CreateIdNew(idParent,idPresent)
{
	var val=$('#'+idPresent).find('.mypc').val();
	var idOrigin = val.substr(val.length - 8);
	var idNew = idParent + '' + idOrigin;
	return idNew;
}

//expand collapse
function ImageClick()
{
//	var item = $("#targetCategory table tr td div.c-open-btn");
	var idOfParent = $(this).closest('tr').attr('id');
	var img =  $(this);

	if(!$(this).hasClass('close'))
	{
		$(this).addClass('close');
	}
	else
	{
		if($(this).hasClass('close'))
		{
			$(this).removeClass('close');
		}
	}
	//close or open child
	CloseOpenChild(idOfParent);
	//reset row
	reset();
}

//reset image expand collapse
//並び替えが終了した際に、全カテゴリー開閉ボタンを1階層目の
//カテゴリーで表示するように設定
function ResetImgEC()
{

	// 抽出カテゴリーが表示されている場合、この処理は行わない。
//	if($('#extractCategoryNameItems')[0]){
//		return;
//	}

//	var EC = "<i class='btn-goAllOpen size-pm plus_all' alt='goAllOpen'></i> ";
//	EC += "<i class='btn-goAllClose size-pm minus_all' alt='goAllClose'></i>";
	var id = $('#targetCategory table tbody').find('tr.pa-').attr('id');
	var $listChild = $('tr.pa-'+id);

	/*$('#targetCategory table tr td').find('i.btn-goAllClose').remove();
	$('#targetCategory table tr td').find('i.btn-goAllOpen').remove();*/
	$("#targetCategory table tr td.cat_name div:first").removeClass('c-open-btn');
//	$('#targetCategory table tr td i.btn-goAllOpen').remove();

	$listChild.each(function(){
		var len = $('tr.pa-'+$(this).attr('id')).length;
		if(len==0)
		{
			$(this).find("td.cat_name").find("div:first").removeClass('c-open-btn');
		}
		else
		{
			if($(this).find("div:first").length == 0)
			{
				$(this).find("td.cat_name").find('div:first').addClass('c-open-btn');
			}
		}
	});
}

//expand  all
function EAllClick()
{
	var item =$("#targetCategory table tr td");
	var idOfParent = $(item).closest('tr').attr('id');
	var len = $(item).closest('tr').nextUntil('tr.pa-10000000').length;

	if(len>50)
	{
		if(!$(item).closest('tr').find("div.c-open-btn").hasClass('close'))
		{
			$(item).closest('tr').find("div.c-open-btn").addClass('close');
		}

		timeout(0,len,$(item),"expanse");
	}
	else
	{
		//open all until meet row continute
		$(item).closest('tr').nextUntil('tr.pa-10000000').css({'display':'none'});
		if($(item).closest('tr').find("div.c-open-btn").hasClass('close'))
		{
			$(item).closest('tr').find("div.c-open-btn").removeClass('close');
		}
		ReplaceIconCollapseExpand(idOfParent,'expand');
		reset();
	}
}

//collapse all
function CAllClick()
{
	var item =$("#targetCategory table tr td");
	var idOfParent = $(item).closest('tr').attr('id');
	var len = $(item).closest('tr').nextUntil('tr.pa-10000000').length;

	if(len>50)
	{
		if(!$(item).closest('tr').find("div.c-open-btn").hasClass('close'))
		{
			$(item).closest('tr').find("div.c-open-btn").addClass('close');
		}
		timeout(0,len,$(item),"collapse");
	}
	else
	{
		//open all until meet row continute
		$(item).closest('tr').nextUntil('tr.pa-10000000').css({'display':'table-row'});
		if($(item).closest('tr').nextUntil('tr.pa-10000000').find("div").hasClass('c-open-btn'))
		{
			if($(item).closest('tr').nextUntil('tr.pa-10000000').find("div.c-open-btn").hasClass('close')){
				$(item).removeClass('close');
			}
			$(item).closest('tr').nextUntil('tr.pa-10000000').removeClass('cat01').addClass('cat01_sub');
			$(item).closest('tr').nextUntil('tr.pa-10000000').find("div.c-open-btn").removeClass('c-open-btn').addClass('cat_sub_end');
		}
		ReplaceIconCollapseExpand(idOfParent,'collapse');
		reset();
	}

}

//Create time out to open all
function timeout(counter,len, $this,mode)
{
	var cnt = counter;
	var evenCurent = false;
	//check curent row is even row
	if($this.closest('tr').hasClass('stripe'))
	{
		evenCurent = true;
	}
	var $temp=null;
	//lock screen
//	$('#underLayerLoading').show();
	setTimeout(function(){
		$this.closest('tr').nextUntil('tr.pa-10000000').each(function (x) {
			//set class even row or odd row
			if(evenCurent)
			{
				if(x%2!=0)
				{
					$(this).addClass('stripe');
				}
			}
			else
			{
				if(x%2==0)
				{
					$(this).addClass('stripe');
				}
			}
			//display with mode
			if(mode=="collapse")
			{
//				$(this).css({'display':'table-row'});
				$(this).attr("style", "display:table-row !important")
				//change icon
				if($(this).find("div.c-open-btn").hasClass('close'))
				{
					$(this).find("div.c-open-btn").removeClass('close');
				}
			}
			else
			{
//				$(this).css({'display':'none'});
				$(this).attr("style", "display:none !important");
				if(!$(this).find("div.c-open-btn").hasClass('close'))
				{
					$(this).find("div.c-open-btn").addClass('close');
				}
			}

			//when to last row is reset rows
			if(counter == (len-1))
			{
				//open lock screen
//				$('#underLayerLoading').hide();
				reset();
			}
			//get curent object
			$temp=$(this);
			counter++;
			//only 50 rows
			if(x==ROWS_HIDE_DISPLAY-1)
			{
				return false;
			}

		});
		//check all
		if(counter < len && counter%ROWS_HIDE_DISPLAY==0 && len > ROWS_HIDE_DISPLAY)
		{
			//call recursive
			timeout(counter,len,$temp,mode);
		}
	    }, 1);

}

//replace icon collapse
function ReplaceIconCollapseExpand(parentId,type)
{
	var $listChild = $('tr.pa-'+parentId);
//	if($listChild.length === 0){
//		$($listChild.attr('id')).find("td.cat_name").find("span.cat_top").removeAttr('class').attr('class','cat_sub_end');
//		$($listChild.attr('id')).find("div.c-open-btn").removeAttr('class').attr('class','');
//	}
//	else if($listChild.length === 1){
//		$($listChild.attr('id')).find("td.cat_name").find("span.cat_top").removeAttr('class').attr('class','cat_sub_end');
//		var $sub = $('tr.pa-'+$listChild.attr('id'))
//		if($sub.length === 0){
//			$($listChild.attr('id')).find("div.c-open-btn").removeAttr('class').attr('class','');
//		}
//	}else{
		$listChild.each(function(index){
			//set class for child
			//check this item has child or not
			var len = $('tr.pa-'+$(this).attr('id')).length;
			//this is child of top
			if(parentId === '10000000'){

			}else{
				$(this).removeClass('cat01').addClass('cat_sub');
			}
			//not is last element
			if(index !== $listChild.length - 1){
				$(this).find("td.cat_name").find("span.cat_top").removeAttr('class').attr('class','cat_sub');
				if(len === 0){
					$(this).find("td.cat_name").find("span.cat_top").removeAttr('class').attr('class','cat_sub_end');
					$(this).find("div.c-open-btn").removeAttr('class').attr('class','');
				}
			}else{
				$(this).find("td.cat_name").find("span.cat_top").removeAttr('class').attr('class','cat_sub_end');
				if(len === 0){
					$(this).find("div.c-open-btn").removeAttr('class').attr('class','');
				}
			}
		if(type === 'collapse')
		{
			if($(this).find('div.c-open-btn').hasClass('close'))
			{
				$(this).find('div.c-open-btn').removeClass('close');
			}
		}

		if(type == 'expand')
		{
			if(!$(this).find('div.c-open-btn').hasClass('close'))
			{
				$(this).find('div.c-open-btn').addClass('close');
			}
		}

		var len = $('tr.pa-'+$(this).attr('id')).length;
		if(len>0)
		{
			ReplaceIconCollapseExpand($(this).attr('id'),type);
		}
	});
	//}
}

//Close or Open child of child
function CloseOpenChild(parentId)
{
	//Get list child of parent
	var $listChild = $('tr.pa-'+parentId);
	//loop list
	$listChild.each(function(){
		$(this).toggle();
		if($(this).find("div.c-open-btn").hasClass('close'))
		{
			return true;
		}
		//Count child of child
		var len = $('tr.pa-'+$(this).attr('id')).length;
		//if have child
		if(len>0)
		{
			//call recursive
			CloseOpenChild($(this).attr('id'));
		}
	});
}

// 制限された階層に移動した場合、元の行に戻しアラートを表示
// newList:backup data
// idPa   :移動する前の親カテゴリーSEQパス
// idPaPre:移動させた行から、前の行の親カテゴリーSEQパス
// table  : #targetCategory table
function CheckLevelRow(newList,idPa,idPaPre,table)
{
	var extractCategoryNameItems = $('#extractCategoryNameItems');

	// 抽出カテゴリーが表示されてない場合、アラート表示しない。
	 if(!extractCategoryNameItems[0]){
		 return;
	}

	 // 抽出カテゴリー指定フラグ取得「表示指定あり:true」or「表示指定なし:false」
	 var extractCategorySpecifiedFlag = $('#extractCategorySpecified').val();

	 // 移動させた行から、次の行の親カテゴリーSEQパス取得
	 var idPaNext = GetIdPaNex(newList);

	 // 制限されたレベル階層取得
	 var limit = extractCategoryNameItems.attr('limit');

	// [pa-」削除
	 idPa = idPa.replace(/pa-/g,"");
	 idPaNext = idPaNext.replace(/pa-/g,"");
	 idPaPre = idPaPre.replace(/pa-/g,"");

	 // 抽出カテゴリー指定フラグが「指定あり」場合、制限された階層に移動したかチェック
	 if(CheckLevelRowForSpecified(extractCategorySpecifiedFlag,limit,idPa,idPaPre,idPaNext)){
		 return;
	 }

	 // 抽出カテゴリー指定フラグが「指定なし」場合、制限された階層に移動したかチェック
	 if(CheckLevelRowForUnspecified(extractCategorySpecifiedFlag,idPa,idPaPre,idPaNext)){
		 return;
	 }

	// 元の行に戻す
	 $(table).sortable("cancel");

	// アラート表示
	alert('制限された階層に並び替えることはできません');

}

//抽出カテゴリー指定フラグが「指定あり」場合、制限された階層に移動したかチェック
//extractCategorySpecifiedFlag：抽出カテゴリー指定フラグ
//limit     :制限されたレベル階層取得
//idPa      :移動する前の親カテゴリーSEQパス
//idPaPre   :移動させた行から、前の行の親カテゴリーSEQパス
//idPaNext  :移動させた行から、次の行の親カテゴリーSEQパス
function CheckLevelRowForSpecified(extractCategorySpecifiedFlag,limit,idPa,idPaPre,idPaNext)
{

	// 抽出カテゴリー指定フラグが「指定あり」以外の場合、このチェックは行わない。
	if(extractCategorySpecifiedFlag !== "true"){
		return false;
	}

	 // limitの制限を1上げる
	 limit = parseInt(limit) + 1;

	 // 制限された階層までカテゴリーSEQパスを切り取り
	 idPa = idPa.substring(0,(limit*8));
	 idPaNext = idPaNext.substring(0,(limit*8));
	 idPaPre = idPaPre.substring(0,(limit*8));

	 // 移動前の親カテゴリーSEQパスと
	 // 移動した行から前後の親カテゴリーSEQパスを比較し
	 // 一致しないものは元の行に戻しアラートを表示
	 if(idPaNext === idPa || idPaPre === idPa){
		return true;
	}

	return false;
}

//抽出カテゴリー指定フラグが「指定なし」場合、制限された階層に移動したかチェック
//extractCategorySpecifiedFlag：抽出カテゴリー指定フラグ
//idPa      :移動する前の親カテゴリーSEQパス
//idPaPre   :移動させた行から、前の行の親カテゴリーSEQパス
//idPaNext  :移動させた行から、次の行の親カテゴリーSEQパス
function CheckLevelRowForUnspecified(extractCategorySpecifiedFlag,idPa,idPaPre,idPaNext)
{

	// 抽出カテゴリー指定フラグが「指定なし」以外の場合、このチェックは行わない。
	if(extractCategorySpecifiedFlag !== "false"){
		return false;
	}

	// 比較する親カテゴリーSEQパス
	var idPaCompare;

	// 各カテゴリーSEQパスからカテゴリーレベルを取得
	var idPaLevel = GetCategoryLevel(idPa);
	var idPaPreLevel = GetCategoryLevel(idPaPre);
	var idPaNextLevel = GetCategoryLevel(idPaNext);

	// 隣接したカテゴリー内から、同じ階層内の移動
	if(idPa === idPaPre && idPa === idPaNext){
		return true;
	}

	// 隣接したカテゴリー内から、同じ階層内の一番上に移動
	if(idPa === idPaNext && idPaLevel > idPaPreLevel){
		return true;
	}

	// 隣接したカテゴリー内から、同じ階層内の一番下に移動
	if(idPa === idPaPre && idPaLevel > idPaNextLevel){
		return true;
	}

	// 隣接していないカテゴリー内から、同じ階層内の移動
	if(idPa === idPaNext && idPaLevel < idPaPreLevel){
		return true;
	}

	 return false;
}


// 移動させた行から、次の行の親カテゴリーSEQパス取得
function GetIdPaNex(newList)
{
	var idPaNext = " ";
	if ($('tr#'+newList.attr('id')).next('tr').length != 0) {
		$('tr#'+newList.attr('id')).next('tr').attr('class').split(' ').map(function(clssName){
			if (clssName.startsWith("pa-"))
			{
				  idPaNext = clssName;
			}
		});
	}

	return idPaNext;
}

// カテゴリーSEQパスからカテゴリーレベルを取得
function GetCategoryLevel(id)
{
	return (id.length)/8;
}


//like parent?
function CheckLikeParent(idPaPre, idPaPresent,idPaNext)
{
	var idPre = idPaPre.split('-')[1];
	var idPresent = idPaPresent.split('-')[1];
	var idNext = " ";
	if(idPaNext != ' ')
	{
		idNext = idPaNext.split('-')[1];
	}
	//case id == 8 move to make child (level1)
	if(idPresent.length == 8)
	{
		return 'unlike';
	}
	if(idPre == idPresent)
	{
		return "like";
	}


	var len = idPresent.length / 8;
	for (i = len; i > 0 ; i--) {
		var id = idPre.substr(0, i*8);
		if(id==idPresent && id == idNext)
		{
			return "like";
		}
	}
	return "unlike";
}

//prev and next like parent?
//return idParent of Prev and Next
function CheckLikeParentPrevNext(idPaPrev,idPaPresent, idPaNext)
{
	if(idPaNext == " ")
	{
		return " ";
	}
	var idPrev = idPaPrev.split('-')[1];
	var idNext = idPaNext.split('-')[1];
	var idPresent = idPaPresent.split('-')[1];

	if(idPrev == idNext)
	{
		return idPrev;
	}

	var len = idNext.length / 8;
	for (i = len; i > 0 ; i--) {
		//id parent
		var id = idPrev.substr(0, i*8);
		if(id==idNext && id==idPresent)
		{
			return id;
		}
	}
	return " ";
}

//add class selectedRow for row and child row
function addClassSelect(parentId)
{
	var $listChild = $('tr.pa-'+parentId);
	$listChild.each(function(){
		$(this).addClass('selectedRow');
		var len = $('tr.pa-'+$(this).attr('id')).length;
		if(len>0)
		{
			addClassSelect($(this).attr('id'));
		}
	});
}

//Check open row to get id parent when child at tree closing
function CheckRowOpenWhenChildClose(input)
{
	var len = input.length / 8;

	for (i = len; i > 0 ; i--) {
		var id = input.substr(0, i*8);
		if(id.length == 8)
		{
			return " ";
		}
		var display = $('#targetCategory table').find("#"+id).css('display');
		var img = $('#targetCategory table').find("#"+id).find('i.btn-img');
		if(display != 'none' && img.hasClass('fa-plus-square-o'))
		{
			return input.substr(0, i*8);
		}
	}
	return " ";
}

//Get Row Data
function GetDataDragDrop()
{
	var $lst = $('#targetCategory table tbody tr');
	$lst.each(function(index){
		//ignore 2 row first (header and top)
		if(index > 1)
		{
			//get present id
			var idPresent =$(this).attr('id');
			//get origin id
			var idOrigin = $(this).find("input.mypc").val();
			//get parent class
			var clsParent = $(this).attr('class');
			//
			var orderDiplay = CreateOD(clsParent,idPresent);
			//get origin index
			var indexOrigin =  $(this).find("input.mypc").attr('name').split(":")[2];
			//Get data
			var valPresent = $('#dtCategory').val().length == 0 ? '' : $('#dtCategory').val() + '/';
			//Get again new data
			var valNew = valPresent + '' + idOrigin + ','  + idPresent + ','+ orderDiplay + ',' + indexOrigin;
			//Set new data
			$('#dtCategory').val(valNew);
		}
	});
}

//Create Order Display
function CreateOD(clsParent,idPresent)
{
	var orderDiplay = "1";
	var clsPa = " ";
	clsParent.split(' ').map(function(clssName){
	  if (clssName.startsWith("pa-"))
	  {
		  clsPa = clssName;
	  }
	});
	var $lstChild =  $('#targetCategory table tbody tr.'+clsPa);
	if($lstChild.length > 1)
	{
		var index = $lstChild.index($("#"+idPresent));
		if(idPresent.length==16)
		{
			orderDiplay = index + 2;
		}
		else
		{
			orderDiplay = index + 1;
		}
	}
	return orderDiplay;
}

//Insert row to row when row next hidden
function NextRowDisplayNotNone(idParent){
	var display = $('#'+idParent).next().css('display');
	if(display == 'none')
	{
		var id = $('#'+idParent).next().attr('id');
		nextDisplay ++;
		//call recursive
		NextRowDisplayNotNone(id);
	}
	else
	{
		//Check first row is not display
		if(nextDisplay > 0  )
		{
			if(typeof display !== "undefined")
			{
				$('#targetCategory table tbody tr.selectedRow').insertBefore($('#'+idParent).next());
			}
			else
			{
				var status= CheckAfter(idParent);
				if(status == "OK")
				{
					$('#targetCategory table tbody tr.selectedRow').insertAfter($('#'+idParent));
				}
			}
		}
	}
}


//check after in case idParent is child selectedRow
function CheckAfter(idParent)
{
	 var status = "OK";
	$('#targetCategory table tbody tr.selectedRow').each(function(){
		if($(this).attr('id')==idParent)
		{
			status = "Fail"
			return false;
		}
	});
	return status;
}
