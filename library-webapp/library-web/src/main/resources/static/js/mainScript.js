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

function showLoans() {
    var x = document.getElementById('myLoans');
    x.style.display = "block";
}


function alertOverdue() {
    Swal.fire({
        title: 'You can\'t book while having overdue items!',
        type: 'error',
        confirmButtonText: 'Ok, I ll take care of it!'
    });
    showLoans();
}

function alertMaxRented() {
    Swal.fire({
        title: 'You have reach the max reserving limit!',
        type: 'error',
        confirmButtonText: 'Oh, Ok!'
    });
    showLoans();
}


function alertAlreadyRented() {
    Swal.fire({
        title: 'You are already renting that book!',
        type: 'error',
        confirmButtonText: 'Ups, my bad!'
    });
    showLoans();
}

function overdueInitPopup(overdue) {
    $(document).ready(function () {
        if (overdue) {
            if (sessionStorage.getItem('popState') != 'shown') {
                Swal.fire({
                    title: 'You have some overdue items!',
                    type: 'error',
                    confirmButtonText: 'Ok, I ll take care of it!'
                }, document.getElementById("myLoans").style.display = "block");
                sessionStorage.setItem('popState', 'shown')
            }
        }
    });
}


