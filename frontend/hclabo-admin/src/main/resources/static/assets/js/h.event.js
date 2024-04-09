(function($) {

if (typeof(H) == 'undefined') {
  H = {};

  /** extends **/
  H.extend = function (self, obj) {
    if (self == null) {
      self = {};
    }
    for (var i = 1; i < arguments.length; i++) {
      var o = arguments[i];
      if (typeof(o) != 'undefined' && o != null) {
        for (var k in o) {
          self[k] = o[k];
        }
      }
    }
    return self;
  };
}

if (typeof(H.Event) == 'undefined') {
  H.Event = {};

  var targets = new Array();
  var eventType = new Array();

  H.extend (H.Event, {

	LOAD      : 'load',
	UNLOAD    : 'unload',
	CHANGE    : 'change',
	CLICK     : 'click',
	DBCLICK   : 'dbclick',
	MOUSEOVER : 'mouseover',
	MOUSEOUT  : 'mouseout',
	BLUR      : 'blur',
	KEYDOWN   : 'keydown',
	KEYUP     : 'keyup',
	KEYPRESS  : 'keypress',
	MOUSEDOWN : 'mousedown',
	MOUSEUP   : 'mouseup',

    eventElement : function(evt) {
	  if (document.all) {  // IE
	    return evt.srcElement;
	  } else {
	    return evt.target;
	  }
	},

	actionCancel : function(evt) {
	  if (evt.preventDefault) {
	    evt.preventDefault();
	  } else {
	    evt.returnValue = false; // IE
	  }
	},

	addEvent : function(targetId, actionId, eventId) {
	  if (typeof(targetId) != 'undefined') {
		targetId = $.trim(targetId);
	  }
	  if (typeof(actionId) != 'undefined') {
		actionId = $.trim(actionId);
      }
	  if (typeof(eventId) != 'undefined') {
		eventId = $.trim(eventId);
	  }
	  targets[targetId] = actionId;
	  eventType[targetId] = eventId;
	},

	load : function() {
	  var actionExe = function(evt) {
		var eventElement = H.Event.eventElement(evt);
	    var actionId;
		for (targetId in targets) {
		  if (targetId.indexOf(eventElement.id + '-') > -1) {
	        if (eventType[targetId] == evt.type) {
	          actionId = targets[targetId];
	          break;
	        }
		  }
		}

	    if (typeof(actionId) == 'undefined') {
	      var actionId = targets[eventElement.id];
		}
	    if (typeof(actionId) != 'undefined') {
	      var actionObj = $('#' + actionId);
	      if (actionObj != null) {
	        actionObj.click();
	      }
	    }
	  };

	  var formElements = $('form');
	  var otherTargets = new Array();
	  for (targetId in targets) {
		var isForm = false;
　　　  for (var i = 0; i < formElements.length; i++) {
	      if (targetId == formElements[i].id) {
	        isForm = true;
	        break;
	      }
        }
        if (!isForm) {
          otherTargets[targetId] = targets[targetId];
        }
	  }

	  for (otherTargetId in otherTargets) {
		var existHyphenId = '';
		if (otherTargetId.indexOf('-') > -1) {
			existHyphenId = otherTargetId;
			otherTargetId = otherTargetId.substring(0, otherTargetId.lastIndexOf('-'));
		}

		var otherTargetObj = $('#' + otherTargetId).get(0);
		if (typeof(otherTargetObj) != 'undefined') {
		  if (existHyphenId == '') {
		    if (typeof(targets[otherTargetObj.id]) != 'undefined' && typeof(eventType[otherTargetObj.id]) != 'undefined') {
			  Kumu.Event.addEvent(otherTargetObj, eventType[otherTargetObj.id], actionExe);
		    }
		  } else {
			if (typeof(targets[existHyphenId]) != 'undefined' && typeof(eventType[existHyphenId]) != 'undefined') {
			  Kumu.Event.addEvent(otherTargetObj, eventType[existHyphenId], actionExe);
			}
		  }
		}
	  }
    }
  });
}

if (typeof(H.FormEvent) == 'undefined') {
  H.FormEvent = {};

  H.extend (H.FormEvent, {

    action : function(targetId, actionId) {
	  if (typeof(targetId) != 'undefined') {
	    targetId = $.trim(targetId);
	  }
	  if (typeof(actionId) != 'undefined') {
	    actionId = $.trim(actionId);
	  }
	  targets[targetId] = actionId;
    },

    load : function() {
  	  var keyPressEvent = function(evt) {
  	    if (evt.keyCode == Kumu.Event.KEY_RETURN) {
  	      var eventElement = H.Event.eventElement(evt);
          if (eventElement.tagName=='SELECT' ||
   	   		  (
   	   		   eventElement.tagName == 'INPUT' &&
   	   		   eventElement.type != 'submit' &&
   	   		   eventElement.type != 'button' &&
   	   	       eventElement.type != 'reset' &&
   	           eventElement.type != 'image'
   		   	  )
  	   	  ) {

            H.Event.actionCancel(evt);
            formActionExe(evt);

  	      } else if (typeof(H.Event.eventElement(evt).type) == 'undefined') {
            H.Event.actionCancel(evt); // IE
  	      }
  	    }
  	  }

  	  var formActionExe = function(evt) {
  	    var eventElement = H.Event.eventElement(evt);
  	    var actionId;
  	    if (eventElement.tagName == 'FORM') {
  		  actionId = eventElement.id;
  	    } else if(eventElement.tagName == 'INPUT' || eventElement.tagName == 'SELECT') {
          actionId = targets[eventElement.form.id];
  	    } else {
  	      return;
  	    }

        if (typeof(actionId) != 'undefined') {
      	var actionObj = $('#' + actionId);
          if (actionObj != null) {
         	  actionObj.click();
          }
        }
  	  };

	  var formElements = $('form');
	  for (var i = 0; i < formElements.length; i++) {
	    var formObj = formElements[i];
        var isTargetForm = false;
	    for (targetId in targets) {
	      if (targetId == formObj.id) {
		    isTargetForm = true;
		    break;
	      }
	    }

	    var submiterCount = 0;
	    var inputElements = $('input', formObj);
	    for (var j = 0; j < inputElements.length; j++) {
	      var inputObj = inputElements[j];
	      if (inputObj.type == 'submit' || inputObj.type == 'image') {
		    submiterCount++;
	      }
        }

	    if (submiterCount <= 1) {
	      if (isTargetForm && typeof(targets[formObj.id]) == 'undefined') {
		    Kumu.Event.addEvent(formObj, H.Event.KEYPRESS, keyPressEvent);
	      } else if (isTargetForm && typeof(targets[formObj.id]) != 'undefined' && typeof(eventType[formObj.id]) == 'undefined') {
		    Kumu.Event.addEvent(formObj, H.Event.KEYPRESS, keyPressEvent);
	      } else if (isTargetForm && typeof(targets[formObj.id]) != 'undefined' && typeof(eventType[formObj.id]) != 'undefined') {
		    Kumu.Event.addEvent(formObj, eventType[formObj.id], formActionExe);
	      }
	    } else {
	      if (isTargetForm && typeof(targets[formObj.id]) == 'undefined') {
		    Kumu.Event.addEvent(formObj, H.Event.KEYPRESS, keyPressEvent);
	      } else if (typeof(eventType[formObj.id]) == 'undefined' || eventType[formObj.id] == H.Event.KEYPRESS) {
		    Kumu.Event.addEvent(formObj, H.Event.KEYPRESS, keyPressEvent);
	      } else {
		    Kumu.Event.addEvent(formObj, eventType[formObj.id], formActionExe);
	      }
	    }
      }
    }
  });
}

})(jQuery);

Kumu.Event.addOnLoadEvent(H.Event.load);
Kumu.Event.addOnLoadEvent(H.FormEvent.load);