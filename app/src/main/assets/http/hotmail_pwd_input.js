function setPwd() {
    var b = document.querySelectorAll("input");
    var inputPwdSuccess = false
    for (var c = 0; c < b.length; c++) {
        var d = b[c];
        if (d.type === "password") {
            b[c].value = "pwdcontent";
            inputPwdSuccess = true
        }
        d = null;
    }
    return inputPwdSuccess
}
window.getGmailAccount.getGmailAccount("inputPwd))(("+setPwd());