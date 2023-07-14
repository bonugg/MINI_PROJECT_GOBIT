$(function () {
    const saveDataDebounced = debounce(saveData, 1000); // debounce 함수를 호출하여 반환된 함수를 저장

    $('#btnSave').on("click", () => {
        saveDataDebounced(); // 반환된 함수를 호출하여 saveData 함수 실행
    });
});

function debounce(func, delay = 0) {
    let timer = null;

    return function () {
        const context = this;
        const args = arguments;

        if (timer) {
            clearTimeout(timer);
        }

        timer = setTimeout(() => func.apply(context, args), delay);
    };
}

function saveData() {
    $.ajax({
        url: "/appRequest/vacation",
        type: "post",
        data: $("#insertForm").serialize(),
        processData: false,
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
}

function printDate() {
    const appStartInput = document.getElementById("input-appStart");
    const appEndInput = document.getElementById("input-appEnd");
    const vacationDateElement = document.getElementById('appData-vacationDate');
    if ($('#input-appStart').val() != null && $('#input-appEnd').val() != null) {
        const dateStart = appStartInput.value;
        const dateEnd = appEndInput.value;
        vacationDateElement.innerText = `${dateStart} ~ ${dateEnd}`;
    }
}

function handleVacType() {
    printType();
    changeDateType();
}

function handleStartDate() {
    const appStartInput = document.getElementById("input-appStart");
    const appEndInput = document.getElementById("input-appEnd");
    const appStartValue = new Date(appStartInput.value);
    const appEndValue = new Date(appEndInput.value);
    // 날짜 유효성 검사
    if (appEndValue < appStartValue) {
        alert("휴가 시작일은 휴가 종료일보다 이전이어야 합니다");
        return;
    }
    calculateDateDifference();
    printDate();
}

function handleEndDate() {
    const appStartInput = document.getElementById("input-appStart");
    const appEndInput = document.getElementById("input-appEnd");

    const appStartValue = new Date(appStartInput.value);
    const appEndValue = new Date(appEndInput.value);

    // 날짜 유효성 검사
    if (appEndValue < appStartValue) {
        alert("휴가 종료일은 휴가 시작일보다 이후여야 합니다");
        return;
    }
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
    const appStartValue = new Date(appStartInput.value);
    const appEndValue = new Date(appEndInput.value);
    console.log("시작시간" +appStartValue);
    console.log("종료시간" +appEndValue);
    const differenceInMilliseconds = appEndValue.getTime() - appStartValue.getTime();
    const differenceInSeconds = differenceInMilliseconds / 1000;

    const appVacReq = document.getElementById('appVacReq')

    if(appStartInput.type == 'date'){
        console.log("날짜 단위");
        document.getElementById("appVacReq").value = differenceInSeconds + 86400;
    }
    if(appStartInput.type=='datetime-local'){
        console.log("시간 단위");
        const tmp = differenceInSeconds;
        document.getElementById("appVacReq").value = tmp;
        console.log("신청한 휴가(초): " + appVacReq.value);
    }

    const appVacReqSec = appVacReq.value; // 초 단위의 값
    const days = Math.floor(appVacReqSec / (60 * 60 * 24)); // 일(day)로 변환된 값
    const hours = Math.floor((appVacReqSec % (60 * 60 * 24)) / (60 * 60)); // 시간으로 변환된 값


    let result = "";
    if (days > 0) {
        result += days + "일";
    }
    if (hours > 0 && days === 0) {
        result += hours + "시간";
        console.log(hours);
    }

    document.getElementById('appData-vacationDateCnt').innerText = result;
}

function printContent() {
    const content = document.getElementById('input-appContent').value;
    document.getElementById('appData-content').innerText = content;
}