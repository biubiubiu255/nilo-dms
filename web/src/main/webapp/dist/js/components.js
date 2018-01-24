/**
 * A component of echarts panel
 * with loading from remote
 * with event handling
 */

function reloadCurrentPage() {
    $(".layui-laypage-btn")[0].click();
}
/*日期格式*/
function formatSex(sex) {
    if (!sex) return '';
    return sex == '1' ? 'Male' : 'Female';
}

/*日期格式*/
function formatDate(time, format) {
    if (!time) return '';
    if (!format) {
        format = "YYYY-MM-DD HH:mm:ss"
    }
    return (new Date(time * 1000)).format(format);
}

function getDiffTime(time) {
    if (!time) return '';

    var nowTime = new Date().getTime() / 1000;

    var total = time - nowTime;

    if (total < 0) {
        return "<font color='red'>" + (new Date(time * 1000)).format("YYYY-MM-DD HH:mm:ss") + "</font>";
    }

    var day = parseInt(total / (24 * 60 * 60));//计算整数天数
    var afterDay = total - day * 24 * 60 * 60;//取得算出天数后剩余的秒数
    var hour = parseInt(afterDay / (60 * 60));//计算整数小时数

    if (day == 0 & hour > 0) {
        return hour + " Hours later"
    }
    if (day == 1) {
        return "Tomorrow";
    }
    if (day > 1) {
        return day + " Days later"
    }
}

function formatCurrency(num) {
    num = num.toString().replace(/\$|\,/g, '');
    if (isNaN(num)) {
        num = "0";
    }
    var sign = (num == (num = Math.abs(num)));
    num = Math.floor(num * 100 + 0.50000000001);
    var cents = num % 100;
    num = Math.floor(num / 100).toString();
    if (cents < 10) {
        cents = "0" + cents;
    }
    for (var i = 0; i < Math.floor((num.length - (1 + i)) / 3); i++) {
        num = num.substring(0, num.length - (4 * i + 3)) + ',' +
            num.substring(num.length - (4 * i + 3));
    }
    return (((sign) ? '' : '-') + num + '.' + cents);
}

/***************************gateway common js begin************************************/
function showBusinessType(value) {
    var html;
    switch (value) {
        case "01":
            html = '<font color="green">Pay</font>';
            break;
        case "02":
            html = '<font color="#ffd700">Pay Charge Off</font>';
            break;
        case "03":
            html = '<font color="#a52a2a">Pay Query</font>';
            break;
        default:
            html = '<font color="red">undefined</font>';
            break;
    }
    return html;
}

function showGatewayType(value) {
    var html;
    switch (value) {
        case "01":
            html = '<font color="green">front gateway</font>';
            break;
        case "02":
            html = '<font color="blue">post gateway</font>';
            break;
        case "03":
            html = '<font color="yellow">other gateway</font>';
            break;
        default:
            html = '<font color="red">undefined</font>';
            break;
    }
}

function showImageView(orderNo, imageType) {

    if (imageType) {
        imageType = "";
    }

    return '<a href="#" onclick="openImage(\'' + orderNo + '\',' + imageType + ')">View</a>';
}

function openImage(orderNo, imageType) {
    $.ajax({
        url: "/image/show.html",
        type: 'POST',
        data: {
            "orderNo": orderNo,
            "imageType": imageType
        },
        dataType: 'text',
        success: function (data) {
            layer.open({
                type: 1,
                title: "Image",
                area: ['680px', '680px'],
                content: data,
                end: function () {
                }
            });
        }
    });
}

/****************************gateway common js end***********************************/
