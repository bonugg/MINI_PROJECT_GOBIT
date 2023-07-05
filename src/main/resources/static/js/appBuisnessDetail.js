function printDate(){
    const dateStart = document.getElementById('inputDate-start').value;
    const dateEnd = document.getElementById('inputDate-end').value;
    document.getElementById('appData-date').innerText = `${dateStart} ~ ${dateEnd}`;
}

function printPlace(){
    const place = document.getElementById('input-appLocation').value;
    document.getElementById('appData-Place').innerText = place;
}

function printContent(){
    const content = document.getElementById('input-appContent').value;
    document.getElementById('appData-content').innerText = content;
}

$("#btnUpdate").on("click", () => {
    $.ajax({
        url: "/appDetail/approval",
        type: "post",
        data: $("#updateForm").serialize(),
        success: (obj) => {
            // location.href = `/app/meet/{metNum}`;
            location.href = '/appDetail';
            alert("수정되었습니다.");
            console.log(obj);
        },
        error: (error) => {
            console.log(error);
        }
    });
});

$("#btnDelete").on("click", () => {
    $.ajax({
        url: "/appDetail/approval",
        type: "delete",
        data: {
            appNum: $("#appNum").val()
        },
        success: (obj) => {
            alert("삭제되었습니다.");
            console.log(obj);
            alert(obj.item.msg);
            // location.href = `/app/meet/{metNum}`;
            // location.href = '/appDetail';
        },
        error: (error) => {
            console.log(error);
        }
    });
});
