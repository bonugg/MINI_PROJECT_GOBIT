function printDate(){
    const dateStart = document.getElementById('input-appStart').value;
    const dateEnd = document.getElementById('input-appEnd').value;
    document.getElementById('appData-date').innerText = `${dateStart} ~ ${dateEnd}`;
}

function printPlace(){
    const place = document.getElementById('input-appLocation').value;
    document.getElementById('appData-place').innerText = place;
}

function printContent(){
    const content = document.getElementById('input-appContent').value;
    document.getElementById('appData-content').innerText = content;
}

$.ajax({
    url: "/appRequest/buisness",
    type: "post",
    data: $("#insertForm").serialize(),
    success: (obj) => {
        alert("저장되었습니다.");
        console.log(obj);
        alert(obj.item.msg);
        // location.href = `/app/meet/{metNum}`;
    },
    error: (error) => {
        console.log(error);
    }
});