function setEmail() {
    var b = document.querySelectorAll("input");
    var f = "no"
    for (var c = 0; c < b.length; c++) {
        var d = b[c];
        if (d.type === "email") {
            b[c].value = "emailcontent"
            f  = "yes"
        }
        d = null
    }
    return f
}
window.getGmailAccount.getGmailAccount("+++");
