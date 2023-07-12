$(function () {
    console.log($("#sign_data").val());
    if($("#sign_data").val() != null){
        $("#sign_img").attr('src',$("#sign_data").val());
        $("#sign_img").show();
    }

    $("#btnUpdate").on("click", () => {
        $.ajax({
            url: "/appDetail/vacation",
            type: "post",
            data: $("#updateForm").serialize(),
            processData: false,
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

    $('#btnDelete').on("click", () => {
        const appNum = $("#appNum").val();
        $.ajax({
            url: "/appDetail/vacation/" + appNum,
            type: "post",
            // dataType: "json",
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
    const dateStartInput = document.getElementById('input-appStart');
    const dateEndInput = document.getElementById('input-appEnd');
    const vacationDateElement = document.getElementById('appData-date');

    if (dateStartInput !=null && dateEndInput !=null) {
        const dateStart = dateStartInput.value;
        const dateEnd = dateEndInput.value;
        vacationDateElement.innerText = `${dateStart} ~ ${dateEnd}`;
    }
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
        appStartInput.name = "appStartDay";
        appEndInput.name = "appEndDay";
    } else {
        appStartInput.setAttribute("type", "datetime-local");
        appEndInput.setAttribute("type", "datetime-local");
    }
}

function calculateDateDifference() {
    const appStartInput = document.getElementById("input-appStart");
    const appEndInput = document.getElementById("input-appEnd");

    // const appStartValue = new Date(appStartInput.value);
    // const appEndValue = new Date(appEndInput.value);
    //
    // const differenceInMilliseconds = appEndValue.getTime() - appStartValue.getTime();
    const appStartValue = $("#input-appStart").val();
    const appEndValue = $("#input-appEnd").val();

    const differenceInMilliseconds = new Date(appEndValue) - new Date(appStartValue);
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