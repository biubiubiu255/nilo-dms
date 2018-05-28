function set_cookie(name, value, expires, path, domain, secure) {
    // set time, it's in milliseconds
    var today = new Date();
    today.setTime(today.getTime());
    /*
     if the expires variable is set, make the correct 
     expires time, the current script below will set 
     it for x number of days, to make it for hours, 
     delete * 24, for minutes, delete * 60 * 24
     */
    if (expires) {
        expires = expires * 1000 * 60 * 60 * 24;
    }
    var expires_date = new Date(today.getTime() + (expires));

    document.cookie = name + "=" + escape(value) +
            ((expires) ? ";expires=" + expires_date.toGMTString() : "") +
            ((path) ? ";path=" + path : "") +
            ((domain) ? ";domain=" + domain : "") +
            ((secure) ? ";secure" : "");
}

/*
 * This will retrieve the cookie by name, if the cookie does not exist, it will return false, so you can do things like 
 * if ( Get_Cookie( 'your_cookie' ) ) do something.
 */

function get_cookie(name) {
    var start = document.cookie.indexOf(name + "=");
    var len = start + name.length + 1;
    if ((!start) && (name != document.cookie.substring(0, name.length)))
    {
        return null;
    }
    if (start == -1)
        return null;
    var end = document.cookie.indexOf(";", len);
    if (end == -1)
        end = document.cookie.length;
    return unescape(document.cookie.substring(len, end));
}

/*
 * Here all you need to do is put in: Delete_Cookie('cookie name', '/', '') and the cookie will be deleted. Remember to match 
 * the cookie name, path, and domain to what you have it in Set_Cookie exactly, or you may get some very hard to diagnose errors.
 */

// this deletes the cookie when called
function Delete_Cookie(name, path, domain) {
    if (Get_Cookie(name))
        document.cookie = name + "=" + ((path) ? ";path=" + path : "") + ((domain) ? ";domain=" + domain : "") + ";expires=Thu, 01-Jan-1970 00:00:01 GMT";
}

/*
 * SAMPLE CODE
 *
 * <script type="text/javascript">
 * // remember, these are the possible parameters for Set_Cookie:
 * // name, value, expires, path, domain, secure
 * Set_Cookie( 'test', 'it works', '', '/', '', '' );
 * if ( Get_Cookie( 'test' ) ) alert( Get_Cookie('test'));
 * // and these are the parameters for Delete_Cookie:
 * // name, path, domain
 * // make sure you use the same parameters in Set and Delete Cookie.
 * Delete_Cookie('test', '/', '');
 * ( Get_Cookie( 'test' ) ) ? alert( Get_Cookie('test')) : 
 * alert( 'it is gone');
 * </script>
 */
