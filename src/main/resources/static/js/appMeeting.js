function printDate(){
    const dateStart = document.getElementById('inputDate-start').value;
    const dateEnd = document.getElementById('inputDate-end').value;
    document.getElementById('appData-date').innerText = `${dateStart} ~ ${dateEnd}`;
}

function printPlace(){
    const place = document.getElementById('input-appLocation').value;
    document.getElementById('appData-Place').innerText = place;
}

function printPeople(){
    const people = document.getElementById('input-Participant').value;
    document.getElementById('appData-people').innerText = people;
}

function printContent(){
    const content = document.getElementById('input-appContent').value;
    document.getElementById('appData-content').innerText = content;
}

//ajax로 db조회
$.ajax({
    url: "/appRequest/meeting",
    type: "post",
    data: $("#insertForm").serialize(),
    success: (obj) => {
        alert("저장되었습니다.");
        console.log(obj);
        alert(obj.item.msg);
        // window.location.href= 'appDetailPage.html';
        location.href = '/appDetail';
    },
    error: (error) => {
        console.log(error);
    }
});