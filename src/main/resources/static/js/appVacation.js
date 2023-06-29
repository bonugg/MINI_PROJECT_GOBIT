function printDate(){
    const dateStart = document.getElementById('inputDate-start').value;
    const dateEnd = document.getElementById('inputDate-end').value;
    document.getElementById('appData-vacationDate').innerText = `${dateStart} ~ ${dateEnd}`;
}

function printType(){
    const type = document.getElementById('vacationType').value;
    document.getElementById('appData-vacationType').innerText = type;
}


function printContent(){
    const content = document.getElementById('inputContent').value;
    document.getElementById('appData-content').innerText = content;
}