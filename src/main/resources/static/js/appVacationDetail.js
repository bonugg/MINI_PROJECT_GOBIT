function printDate(){
    const dateStart = document.getElementById('input-appStart').value;
    const dateEnd = document.getElementById('input-appEnd').value;
    document.getElementById('appData-vacationDate').innerText = `${dateStart} ~ ${dateEnd}`;
}

function printType(){
    const type = document.getElementById('input-appVacType').value;
    document.getElementById('appData-vacationType').innerText = type;
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
            alert("수정되었습니다.");
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

$("#btnDelete").on("click", () => {
    $.ajax({
        url: '/appDetail/approval',
        type: 'delete',
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