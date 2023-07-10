$(function () {
    $('#btnSave').on("click", () => {
        $('#btnSave').on("click", () => {
            $.ajax({
                url: "/appRequest/vacation",
                type: "post",
                data: $("#insertForm").serialize(),
                processData: false,
                // contentType: false,
                dataType: "json",
                success: (obj) => {
                    console.log(obj);
                    alert(obj.item.msg);
                    if (obj.item.result == "success") {
                        window.location.href = obj.item.redirectUrl;
                    }
                },
                error: (error) => {
                    console.log(error);
                }
            });
        });
    });
});

function printDate() {
    const dateStart = document.getElementById('input-appStart').value;
    const dateEnd = document.getElementById('input-appEnd').value;
    document.getElementById('appData-vacationDate').innerText = `${dateStart} ~ ${dateEnd}`;
}

function handleVacType() {
    printType();
    changeDateType();
}

function handleDate() {
    calculateDateDifference();
    printDate();
}

function printType() {
    const type = document.getElementById('input-appVacType').value;
    document.getElementById('appData-vacationType').innerText = type;
}

function changeDateType() {
    const vacType = document.getElementById('input-appVacType').value;
    const appStartInput = document.getElementById("input-appStart");
    const appEndInput = document.getElementById("input-appEnd");

    if (vacType === 'dayOff' || vacType === 'sickDay') {
        appStartInput.setAttribute("type", "date");
        appEndInput.setAttribute("type", "date");
        appStartInput.name = "appStart2";
        appEndInput.name = "appEnd2";
    } else {
        appStartInput.setAttribute("type", "datetime-local");
        appEndInput.setAttribute("type", "datetime-local");
    }
}

function calculateDateDifference() {
    const appStartInput = document.getElementById("input-appStart");
    const appEndInput = document.getElementById("input-appEnd");

    const appStartValue = new Date(appStartInput.value);
    const appEndValue = new Date(appEndInput.value);

    const differenceInMilliseconds = appEndValue.getTime() - appStartValue.getTime();

    const differenceInSeconds = differenceInMilliseconds / 1000;
    console.log("차이(초):", differenceInSeconds);
    const appVacReq = document.getElementById('appVacReq')
    // appVacReq.setAttribute("value", differenceInSeconds);
    document.getElementById("appVacReq").value = differenceInSeconds;

    console.log(appVacReq);

    const appVacReqSec = document.getElementById('appVacReq').value;
    const days = appVacReqSec / (60 * 60 * 24);
    document.getElementById('appData-vacationDateCnt').innerText = days.toFixed(1);

}

function printContent() {
    const content = document.getElementById('input-appContent').value;
    document.getElementById('appData-content').innerText = content;
}