function userLogin() {
    var user_id = $("#user_name");
    var password = $("#user_psw");
    if (user_id.val() == '') {
        alert('请输入登录用户名!');
        user_id.focus();
        return;
    }
    if (password.val() == '') {
        alert('请输入登录密码!');
        password.focus();
        return;
    }
    var user_login_id = user_id.val();
    var user_login_psw = password.val();
    set_cookie('LoginId', user_login_id, 365, '/', null, false);
    set_cookie('LoginPsw', user_login_psw, 30, '/', null, false);

    ajaxRequest("../controller/login.php", {"user_id": user_id.val(), "user_psw": password.val(), "passcode": "mobile"}, false, function(response) {
        if (response.result) {
            if (response.data > 0) {
                alert(response.msg);
            }
            window.location.href = 'index.php';
        } else {
            alert(response.msg);
            if (response.data == 0) {
            } else if (response.data == 1) {
                password.val('');
                password.focus();
            } else {
                window.location.href = 'view/other/personal_psw.php';
            }
        }
    });
}

$(function() {
    var userloginid = get_cookie('LoginId');
    if (userloginid) {
        $("#user_name").val(userloginid);
        $("#user_psw").focus();
    } else {
        $("#user_name").focus();
    }

    $('#user_psw').keydown(function(e) {
        if (e.keyCode == 13) {
            userLogin();
        }
    });
});