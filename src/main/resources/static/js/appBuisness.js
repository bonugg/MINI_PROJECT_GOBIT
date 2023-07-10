$(function () {
    $('#btnSave').on("click", () => {
        $.ajax({
            url: "/appRequest/buisness",
            type: "post",
            data: $("#insertForm").serialize(),
            processData: false,
            // contentType: false,
            dataType: "json",
            success: (obj) => {
                console.log(obj);
                alert(obj.item.msg);
                if(obj.item.result == "success"){
                    window.location.href = obj.item.redirectUrl;
                }
            },
            error: (error) => {
                console.log(error);
            }
        });
    });
});

function printDate() {
    const dateStart = document.getElementById('inputDate-start').value;
    const dateEnd = document.getElementById('inputDate-end').value;
    document.getElementById('appData-date').innerText = `${dateStart} ~ ${dateEnd}`;
}

function printPlace() {
    const place = document.getElementById('input-appLocation').value;
    document.getElementById('appData-place').innerText = place;
}

function printContent() {
    const content = document.getElementById('input-appContent').value;
    document.getElementById('appData-content').innerText = content;
}