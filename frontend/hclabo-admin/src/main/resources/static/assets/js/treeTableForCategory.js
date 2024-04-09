$(function() {
	//remove class initial_f
	$('#targetCategory').removeClass('initial_f');
	//initial class
	SetIC();
	//call event
	$('#targetCategory tr td i.btn-img').unbind().on('click', ImageClick);
});

//set id class when init
function SetIC()
{
	//tree not top
	var display = ' ';
	var id = '10000000';
	var $lst = $('#targetCategory tr').find("input[value^='"+id+"']:first");
	$lst.each(function(x) {
		var value = $(this).val();
		var len = $(this).val().length - 8;
		var inputParent = $(this).val().substr(0, len);
		$(this).closest('tr').addClass('pa-'+inputParent);
		$(this).closest('tr').attr('id', value);
	});
	
	$lst.each(function(x) {
		
		var idChild = $(this).closest('tr').attr('id');
		if(id === idChild)
		{
			//tree have top
			display = 'top';
			var plus = '<i class="fa-minus-square-o btn-img" ></i>&nbsp;';
			$(this).closest('tr').find('.icon_pm').append(plus);
			var len = $('tr.pa-'+ id).length;
			$(this).closest('tr').find('.sumChild').append(" ("+len+")");
			return true;
		}
		var $listChild = $('tr.pa-'+ idChild);
		if($listChild.length > 0)
		{
			var plus = '&nbsp;&nbsp;&nbsp;&nbsp;<i class="fa-minus-square-o btn-img" ></i>&nbsp;';
			if(display == ' ')
			{
				//tree not top
				plus = '<i class="fa-minus-square-o btn-img" ></i>&nbsp;';
				$(this).closest('tr').find('.icon_pm').append(plus);
				return true;
			}
			$(this).closest('tr').find('.icon_pm').append(plus);
			$(this).closest('tr').find('.sumChild').append(" ("+$listChild.length+")");
		}
		else
		{
			var lenId = $(this).closest('tr').attr('id').length;
			if(lenId > 8)
			{
				var space = '&nbsp;<i class="fa-my-child btn-img" ></i>';
				if(display == ' ')
				{
					space = '<i class="fa-my-child btn-img" ></i>';
				}
				$(this).closest('tr').find('.icon_pm').append(space);
			}
			//remove event onClick for image
			if(lenId == 16)
			{
				 $(this).closest('tr').find('.icon_pm').removeAttr('onClick');
			}
			//form input category
			if(display == 'top')
			{
				var $listChild = $('tr.pa-'+$(this).closest('tr').attr('id'));
				if($listChild.length > 0)
				{
					$(this).closest('tr').find('.sumChild').append(" ("+$listChild.length+" )");
				}
			}
		}
	});
}

//expand collapse
function ImageClick()
{
	var idOfParent = $(this).closest('tr').attr('id');;
	var img =  $(this);
	
	if($(this).hasClass('fa-plus-square-o'))
	{
		$(this).removeClass('fa-plus-square-o').addClass('fa-minus-square-o');
	}
	else
	{
		if($(this).hasClass('fa-minus-square-o'))
		{
			$(this).removeClass('fa-minus-square-o').addClass('fa-plus-square-o');
		}	
	}
	
	CloseOpenChild(idOfParent);	
}

//Close or Open child of child
function CloseOpenChild(parentId)
{
	//Get list child of parent
	var $listChild = $('tr.pa-'+parentId);
	//loop list
	$listChild.each(function(){
		$(this).toggle();
		reset();
		if($(this).find('i.btn-img').hasClass('fa-plus-square-o'))
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

//reset row
function reset()
{
	var y = 0;
	$('#targetCategory tr').each(function(x) {
		$(this).removeClass("stripe");
		$(this).removeClass("over");
		var  display= $(this).css('display');
		if(display === 'none')
		{
			y++;
		}
		else
		{
			if((x+y)%2!=1)
			{
				$(this).addClass("stripe");
			}
		}
		
	});
}