function printDate(){
    const dateStart = document.getElementById('inputDate-start').value;
    const dateEnd = document.getElementById('inputDate-end').value;
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
        alert("정상적으로 저장되었습니다.");
        console.log(obj);
        alert(obj.item.msg);
        // location.href = `/app/meet/{metNum}`;
        location.href = '/appDetail';
    },
    error: (error) => {
        console.log(error);
    }
});