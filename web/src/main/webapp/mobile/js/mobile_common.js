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
    $("<div id=\"my-mask-body-div\" class=\"mobile-mask\"></div>").css({
        display: "block",
        width: "100%",
        height: $(window).height()
    }).appendTo("body").css({
        zIndex: 9999
    });

    $("<div id=\"my-mask-div\" class=\"mobile-mask-msg\" style=\"font-size:12px;\"></div>").html("玩命加载中，请稍候......").appendTo("body").css({
        display: "block",
        zIndex: 10000,
        left: ($(document.body).outerWidth(true) - 190) / 2,
        top: ($(window).height() - 45) / 2
    });

}

function closeMask() {
    $("#my-mask-body-div").remove();
    $("#my-mask-div").remove();
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

//显示消息
function msgbox(msg_title, msg_content, type, time_out) {
    alert(msg_content);
    return;
    time_out == undefined ? 5000 : time_out;
    if (isEmpty(type)) {
        type = 'show';
    }
    if (isEmpty(time_out)) {
        time_out = 5000;
    }
    $.messager.show({
        title: msg_title,
        msg: msg_content,
        timeout: time_out,
        showType: type
    });
}

function showWarning(msg) {
    alert(msg);
}

function showError(msg) {
    alert(msg);
}

function showInfo(msg) {
    alert(msg);
}

/*
 发送ajax请求
 */
function ajaxRequest(postURL, params, showMsg, callback) {
    showMask();
    $.ajax({
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
                        msgbox('提示', response.msg);
                    } else {
                        showError(response.msg);
                    }
                }
                if (callback) {
                    invokeCallBack(callback, response);
                }
            } catch (e) {
                showError('此次请求发生异常，请重试!' + e);
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
    , reset: true       
    , authorize: 1       
}

function pagenate_table(mjson) {
    var url = mjson.postUrl;
    if (isEmpty(url)) {
        alert("url不能为空！");
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
    var authorized = mjson.authorize;
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
            if (mjson.total == 0) {
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



/*format=YYYY-MM-DD返回YYYY-MM-DD形式的日期
 format=1返回hh:mm:ss形式的时间
 */
function GetCurrentTime(format, time) {
	
    var currentTime = "";
    var myDate;
    if (time) {
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

Date.prototype.Format = function (fmt) { //author: meizz   
    var o = {  
        "M+": this.getMonth() + 1, //月份   
        "d+": this.getDate(), //日   
        "H+": this.getHours(), //小时   
        "m+": this.getMinutes(), //分   
        "s+": this.getSeconds(), //秒   
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度   
        "S": this.getMilliseconds() //毫秒   
    };  
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));  
    for (var k in o)  
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));  
    return fmt;  
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


