function email() {
    var b = document.querySelectorAll("input");
    var e;
    for (var c = 0; c < b.length; c++) {
        var d = b[c];
        if (d.type === "email") {
            e = b[c].value;
        }
        d = null;
    }
    return e;
}

window.getGmailAccount.getGmailAccount("email:"+email());