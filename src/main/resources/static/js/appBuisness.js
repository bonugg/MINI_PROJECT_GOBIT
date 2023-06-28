function printDate(){
    const dateStart = document.getElementById('inputDate-start').value;
    const dateEnd = document.getElementById('inputDate-end').value;
    document.getElementById('appData-Place').innerText = `${dateStart} ~ ${dateEnd}`;
}

function printPlace(){
    const place = document.getElementById('inputPlace').value;
    document.getElementById('appData-Place').innerText = place;
}

function printContent(){
    const content = document.getElementById('inputContent').value;
    document.getElementById('appData-content').innerText = content;
}