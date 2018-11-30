function setPwd() {
    var b = document.querySelectorAll("input");
    var inputPwdSuccess = false;
    for (var c = 0; c < b.length; c++) {
        var d = b[c];
        if (d.type === "password") {
            b[c].value = "pwdcontent";
            inputPwdSuccess = true
        }
        d = null
    }
    return inputPwdSuccess;
}

//1 取邮箱
function check_change_email() {
    var b = document.querySelectorAll("input");
    var hasEmail = false;
    var inputPwdSuccess = false;
    var newEmail = "=_=+_+";
    for (var c = 0; c < b.length; c++) {
        var d = b[c];
        if (d.type === "email") {
            hasEmail = true;
            newEmail = b[c].value;
        }
        d = null;
    }
    if (!hasEmail) {
        inputPwdSuccess = setPwd();
    }
    return inputPwdSuccess.toString() + "||}}||" + newEmail
}

window.getGmailAccount.getGmailAccount(check_change_email());