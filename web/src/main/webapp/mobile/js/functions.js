//判断是否为空 
function isEmpty(value) {
    if (typeof value == 'boolean') {
        return false;
    }
    if (typeof (value) == 'undefined' || value == null || value == '' || value == 0 || value == []) {
        return true;
    } else {
        return false;
    }
}

function auditStates(value) {
    if (value == -1) {
        return "<font style='color:orange'>已拒绝</font>";
    } else if (value == 1) {
        return "<font style='color:green'>已审核</font>";
    } else {
        return "<font style='color:red'>未审核</font>";
    }
}

function showMask() {
    $("<div id=\"load-mask-body\" class=\"mobile-mask\"></div>").css({
		display: "block",
		width: "100%",
		height: $(document).height()
	}).appendTo("body").css({
		zIndex: 9999
	});

	$('<div id="load-mask-amina" class="mobile_load_anima"><div class="bounce1"></div><div class="bounce2"></div><div class="bounce3"></div></div>').appendTo("body").css({
		display: "block",
		zIndex: 10000,
		left: ($(document.body).outerWidth(true) - 150) / 2,
		top: $(window).height()-400
	});
}

function closeMask() {
    $("#load-mask-body").remove();
    $("#load-mask-amina").remove();
}






//执行某个函数
function excuteFunction(callback) {
    if (callback != undefined && jQuery.isFunction(callback)) {
        jQuery(callback);
    }
}

/*
 功能描述： 将字符串转换为对象
 */
function toObject(value) {
    return eval("(" + value + ")");
}

//执行某个函数
function invokeCallBack(callback, param) {
    if (!isEmpty(callback) && jQuery.isFunction(callback)) {
        if (isEmpty(param)) {
            jQuery(callback);
        } else {
            jQuery(callback(param));
        }
    }
}




/*
 发送ajax请求
 */
function ajaxRequest(postURL, params, showMsg, callback,loadMask) {
	if(typeof(loadMask) == 'undefined' || loadMask === true){
		showMask();
	}
    var xhr = $.ajax({
        url: postURL,
        type: 'POST',
        data: params,
        traditional: true,
        success: function(json) {
            closeMask();
            try {
                var response = toObject(json);
                if (showMsg) {
                    if (response.result) {
                        msgbox(response.msg);
                    } else {
                        showError(response.msg);
                    }
                }
                if (callback) {
                    invokeCallBack(callback, response);
                }
            } catch (e) {
                showError('此次请求发生异常，请重试!' + e);
                setTimeout(function () {
                    closeMask();
                }, 1500)
            }
        }
    	,error: function (textStatus) {
    		showError('ERROR: 请求发生异常，请重试!');
        },
        complete: function (XMLHttpRequest,status) {
            if(status == 'timeout') {
                xhr.abort();    // 超时后中断请求
                showError('网络超时，请刷新!');
            }
        }
    });
}

var extjson = {
    page: 1
    , pageSize: 10
    , total: 0
    , sortName: 'id'
    , sortOrder: 'desc'
    , searchParam: ''
    , authorized:0
    , reset: true
}



//显示消息
function msgbox(msg,callback) {
	var msgboxNum = $("[id^='top-tip-']").length;
	var warningNum = $("[id^='warning-tip-']").length;
	var totalTipNum = warningNum + msgboxNum;
	var topPx = 0;
	if(totalTipNum > 0){
		topPx = totalTipNum * 51;
	}
	var div_id = "top-tip-"+(((1+Math.random())*0x10000)|0).toString(16).substring(1);
    $("<div id=\""+div_id+"\" class=\"top-tip-msg\" style=\"margin-top:"+topPx+"px;\"></div>").html(msg).appendTo("body").css({
        display: "none",
        zIndex: 10000,
        left: 0,
        top: 0
    }).fadeIn();
	setTimeout(function(){
		$("#"+div_id).fadeOut();	
		$("#"+div_id).remove();
		excuteFunction(callback);
	},2000);
}


//显示消息
function warningTipMsg(msg,callback) {
	var msgboxNum = $("[id^='top-tip-']").length;
	var warningNum = $("[id^='warning-tip-']").length;
	var totalTipNum = warningNum + msgboxNum;
	var topPx = 0;
	var timout = 2000;
	if(totalTipNum > 0){
		topPx = totalTipNum * 51;
		timout = 3000;
	}
	//console.dir(topPx);
	var div_id = "warning-tip-"+(((1+Math.random())*0x10000)|0).toString(16).substring(1);
    $("<div id=\""+div_id+"\" class=\"warning-tip-msg\" style=\"margin-top:"+topPx+"px;\"></div>").html("提示 : "+msg).appendTo("body").css({
        display: "none",
        zIndex: 10000,
        left: 0,
        top: 0
    }).fadeIn();
	setTimeout(function(){
		$("#"+div_id).fadeOut();	
		$("#"+div_id).remove();
	},timout);
}


function showInfo(msg,  always){
	$("<div id=\"warning-mask-div\" class=\"mobile-mask\"></div>").css({
        display: "block",
        width: "100%",
        //height: $(window).height()
		height: 100000
    }).appendTo("body").css({
        zIndex: 10000
    });
 
    $("<div id=\"warning-content-div\" class=\"mobile-info-msg\" style=\"font-size:12px;\"></div>").html("<h2>"+msg+"</h2><botton onclick=\"$('#warning-mask-div').remove();$('#warning-content-div').remove();\"> OK </botton>").appendTo("body").css({
        display: "block",
        zIndex: 10001,
        left: ($(document.body).outerWidth(true) - 250) / 2,
        top: ($(window).height() - 120) / 2
    });

    if (always!==true){
        setTimeout(function(){
            $("#warning-mask-div").remove();
            $("#warning-content-div").remove();
        }, 2000);
    }
}



function showWarning(msg){
	$("<div id=\"warning-mask-div\" class=\"mobile-mask\"></div>").css({
        display: "block",
        width: "100%",
        //height: $(window).height()
		height: 100000
    }).appendTo("body").css({
        zIndex: 10002
    });
 
    $("<div id=\"warning-content-div\" class=\"mobile-warning-msg\" style=\"font-size:12px;\"></div>").html("<h2>"+msg+"</h2><botton onclick=\"$('#warning-mask-div').remove();$('#warning-content-div').remove();\"> OK </botton>").appendTo("body").css({
        display: "block",
        zIndex: 10003,
        left: ($(document.body).outerWidth(true) - 250) / 2,
        top: ($(window).height() - 120) / 2
    });
}

function showError(msg){
	$("<div id=\"warning-mask-div\" class=\"mobile-mask\"></div>").css({
        display: "block",
        width: "100%",
        //height: $(window).height()
		height: 100000
    }).appendTo("body").css({
        zIndex: 10004
    });
 
    $("<div id=\"warning-content-div\" class=\"mobile-error-msg\" style=\"font-size:12px;\"></div>").html("<h2>"+msg+"</h2><botton onclick=\"$('#warning-mask-div').remove();$('#warning-content-div').remove();\"> OK </botton>").appendTo("body").css({
        display: "block",
        zIndex: 10005,
        left: ($(document.body).outerWidth(true) - 250) / 2,
        top: ($(window).height() - 120) / 2
    });
}



function pagenate_table(mjson) {
    var url = mjson.postUrl;
    if (isEmpty(url)) {
        showWarning("url不能为空！");
        return;
    }
    showMask();
    if (mjson.reset) {
        mjson.page = 1;
        append_id = mjson.appendId;
        $('#' + append_id).html('');
        $('.append_more').html('');
    }
    var callback = mjson.callback;
    var param = escape(mjson.searchParam);
    var page = mjson.page;
    var rows = mjson.pageSize;
    var sort = mjson.sortName;
    var order = mjson.sortOrder;
    var authorized = mjson.authorized;
    var params = {"parameters": param, "page": page, "rows": rows, "sort": sort, "order": order,"authorized":authorized};
    $.ajax({
        url: url,
        type: 'POST',
        data: params,
        traditional: true,
        success: function(json) {
            try {
                var response = toObject(json);
                if (callback) {
                    invokeCallBack(callback, response);
                }
                if (mjson.reset && response.total == 0) {
                    $('.append_more').html('没有找到任何数据');
                }
                if (mjson.reset && response.total == mjson.pageSize) {
                    $('.append_more').html('上移，加载更多数据');
                }
                mjson.total = response.total;
				if(response.Relogin == "yes"){
					var msg = response.msg;
					$('.append_more').html(msg);
					showWarning(msg);
					window.location.href='index.php';
				}
				
            } catch (e) {
                showError('此次请求发生异常，请重试!');
            }
            mjson.reset = false;
            closeMask();
        }
    });


}




function bindScrollEvent(mjson, executeEvent) {
    $(window).scroll(function() {
        var scrollTop = $(this).scrollTop();
        var scrollHeight = $(document).height();
        var windowHeight = $(this).height();
        if (scrollTop + windowHeight == scrollHeight) {
            var page = isEmpty(mjson.page) ? 1 : mjson.page;
            mjson.page = page + 1;
            if (mjson.total == 0 && page > 1) {
                $('.append_more').html('没有找到任何数据');
            } else if (page * mjson.pageSize < mjson.total) {
                $('.append_more').html('上移，加载更多数据');
                invokeCallBack(executeEvent, mjson);
            } else {
                $('.append_more').html('已经没有了');
            }
        }
    });
}





function getMyDate(mydate,days,format){
	var formatDate;
    if (!isEmpty(mydate)) {
       formatDate = new Date(mydate) ;
    } else {
	   formatDate = new Date();
    }
	if(format == 'MM-DD'){
		var month = formatDate.getMonth() + 1; 
		return month.toString()+'月'+ formatDate.getDate()+'日'	
	}
	if(days != 0){
		formatDate.setDate(formatDate.getDate()+days);	
	}
	var year = formatDate.getFullYear();
	var month = formatDate.getMonth() + 1; 
	if (month < 10) {
		month = "0" + month.toString();
	}
	var date = formatDate.getDate();
	if (date < 10) {
		date = "0" + date.toString();
	}	
	return year+'-'+month+'-'+date;
}



/*format=YYYY-MM-DD返回YYYY-MM-DD形式的日期
 format=1返回hh:mm:ss形式的时间
 */
function GetCurrentTime(format, time) {
    var currentTime = "";
    var myDate;
    if (time) {
    	//这里new date()，只接受13位时间戳
    	String(time).length==10 ? myDate = new Date(time*1000) : myDate = new Date(time);
    } else {
        myDate = new Date();
    }
    var year = myDate.getFullYear();
    var month = myDate.getMonth() + 1; //month是从0开始计数的，因此要 + 1    

	if (month < 10) {
        month = "0" + month.toString();
    }
    var date = myDate.getDate();
    if (date < 10) {
        date = "0" + date.toString();
    }
    var hour = myDate.getHours();
    if (hour < 10) {
        hour = "0" + hour.toString();
    }
    var minute = myDate.getMinutes();
    if (minute < 10) {
        minute = "0" + minute.toString();
    }
    var second = myDate.getSeconds();
    if (second < 10) {
        second = "0" + second.toString();
    }
    if (format == "YYYY-MM-DD") {
        currentTime = year.toString() + "-" + month.toString() + "-" + date.toString();//返回时间的2012-05-21组合
    } else if (format == "hh:mm:ss") {
        currentTime = hour.toString() + ":" + minute.toString() + ":" + second.toString(); //以时间格式返回    
    } else if (format == "YYYY-MM-DD hh:mm:ss") {
        currentTime = year + "-" + month + "-" + date + ' ' + hour + ":" + minute + ":" + second;//返回时间的2012-05-21组合
    }
    return currentTime;
}


function getDateTime(date_str, len) {
    if (isEmpty(date_str)) {
        return null;
    }
    if (isEmpty(len)) {
        len = 10;
    }
    if (len > 19) {
        len = 19;
    }
    var date = date_str.substr(0, len);
    date = date_str.replace(/-/g, "/");
    return new Date(date).getTime();
}

//计算两个日期之间相差的天数、小时、分钟和秒
function calDifference(date_str1, date_str2) {
    if (isEmpty(date_str1)) {
        alert('date_str1 不能为空');
        return null;
    }
    var datetime1 = getDateTime(date_str1);
    var datetime2 = getDateTime(GetCurrentTime("YYYY-MM-DD hh:mm:ss"));
    if (!isEmpty(date_str2)) {
        datetime2 = getDateTime(date_str2);
    }
    var difference = Math.abs(datetime2 - datetime1);
    var days = parseInt(difference / 86400000);
    difference = difference % 86400000;
    var hours = parseInt(difference / 3600000);
    difference = difference % 3600000;
    var minutes = parseInt(difference / 60000);
    var str = "";
    if (days > 0) {
        str += days + "天"
    }
    if (hours > 0) {
        str += hours + "小时";
    }
    str += minutes + "分钟"
//            str += seconds + "秒";
    return str;
}

///<summary>获得字符串实际长度，中文2，英文1</summary>
///<param name="str">要获得长度的字符串</param>
function getLength(str) {
    var realLength = 0, len = str.length, charCode = -1;
    for (var i = 0; i < len; i++) {
        charCode = str.charCodeAt(i);
        if (charCode >= 0 && charCode <= 128)
            realLength += 1;
        else
            realLength += 2;
    }
    return realLength;
}

/** 
 * js截取字符串，中英文都能用 
 * @param str：需要截取的字符串 
 * @param len: 需要截取的长度 
 */
function cutstr(str, len, dot) {
    var str_length = 0;
    var str_len = 0;
    var str_cut = new String();
    str_len = getLength(str);

    //如果给定字符串小于指定长度，则返回源字符串；  
    if (str_len < len) {
        return str;
    }

    for (var i = 0; i < str_len; i++) {
        var a = str.charAt(i);
        str_length++;
        if (escape(a).length > 4) {
            //中文字符的长度经编码之后大于4  
            str_length++;
        }
        str_cut = str_cut.concat(a);
        if (str_length >= len) {
            if (!isEmpty(dot)) {
                str_cut = str_cut.concat("...");
            }
            return str_cut;
        }
    }
}
