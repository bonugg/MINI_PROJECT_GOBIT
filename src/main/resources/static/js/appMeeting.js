function printDate(){
    const dateStart = document.getElementById('inputDate-start').value;
    const dateEnd = document.getElementById('inputDate-end').value;
    document.getElementById('appData-Place').innerText = `${dateStart} ~ ${dateEnd}`;
}

function printPlace(){
    const place = document.getElementById('inputPlace').value;
    document.getElementById('appData-Place').innerText = place;
}

function printPeople(){
    const people = document.getElementById('inputPeople').value;
    document.getElementById('appData-people').innerText = people;
}

function printContent(){
    const content = document.getElementById('inputContent').value;
    document.getElementById('appData-content').innerText = content;
}

//ajax로 db조회
$.ajax({
    url: "/app/meet",
    type: "post",
    data: $("#insertForm").serialize(),
    success: (obj) => {
        console.log(obj);
        // alert(obj.item.msg);
        // location.href = `/appMeeting`;
        // location.href = `/app/meet/{metNum}`;
    },
    error: (error) => {
        console.log(error);
    }
});