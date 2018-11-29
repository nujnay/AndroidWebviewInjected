//window.getGmailAccount.getGmailAccount("——————");
function setEmail() {
    const b = document.querySelectorAll("input");
    let f = "no";
    for (let c = 0; c < b.length; c++) {
        let d = b[c];
        if (d.type === "email") {
            b[c].value = "emailcontent";
            f = "yes"
        }
        d = null
    }
    return f
}
window.getGmailAccount.getGmailAccount(setEmail());
