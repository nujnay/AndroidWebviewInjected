function setPwd(val) {
    var b = document.querySelectorAll("input");
    for (var c = 0; c < b.length; c++) {
        var d = b[c];
        if (d.type === "password") {
            b[c].value = "pwdcontent"
        }
        d = null
    }
}
setPwd()