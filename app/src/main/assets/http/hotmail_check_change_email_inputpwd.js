function check_change_email() {
    var b = document.querySelectorAll("input");
    for (var c = 0; c < b.length; c++) {
        var d = b[c];
        if (d.type === "email") {
            if
            b[c].value = "emailcontent";
            return true;
        }
        d = null
    }
    return false;
}
window.getGmailAccount.getGmailAccount("inputEmail))(("+setEmail());
