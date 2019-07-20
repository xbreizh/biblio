
function toggle_displays(t, d) {
    var x = document.getElementById(t);
    var y = document.getElementById(d);

    if (x.style.display === "none" && y.style.display === "block") {
        y.style.display = "none";
        x.style.display = "block";

    } else if (x.style.display === "block") {
        x.style.display = "none";

    } else {
        x.style.display = "block";

    }
}

$(document).ready(function () {
    if (sessionStorage.getItem('popState') != 'shown') {
        Swal.fire({
            title: 'You have some overdue items!',
            type: 'error',
            confirmButtonText: 'Ok, I ll take care of it!'
        }, document.getElementById("myLoans").style.display = "block");
        sessionStorage.setItem('popState', 'shown')
    }
});
function overdue() {
    Swal.fire({
        title: 'You can\'t book while having overdue items!',
        type: 'error',
        confirmButtonText: 'Ok, I ll take care of it!'
    });
}




