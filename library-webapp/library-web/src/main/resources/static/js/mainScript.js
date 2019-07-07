function myFunction(slop) {
    var x = document.getElementsByClassName(slop);
    if (x.style.display === "none") {
        x.style.display = "block";
    } else {
        x.style.display = "none";
    }
}


function toggle_display(id) {
    var e = document.getElementById(id);
    if (e.style.display == 'none')
        e.style.display = 'block';

    else
        e.style.display = 'none';
}