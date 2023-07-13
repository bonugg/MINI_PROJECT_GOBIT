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
        url: "/appRequest/buisness",
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
    const dateStartInput = document.getElementById('input-appStart');
    const dateEndInput = document.getElementById('input-appEnd');
    const vacationDateElement = document.getElementById('appData-date');

    if (dateStartInput != null && dateEndInput != null) {
        const dateStart = dateStartInput.value;
        const dateEnd = dateEndInput.value;
        vacationDateElement.innerText = `${dateStart} ~ ${dateEnd}`;
    }
}

function handleStartDate() {
    const appStartInput = document.getElementById("input-appStart");
    const appEndInput = document.getElementById("input-appEnd");
    const appStartValue = new Date(appStartInput.value);
    const appEndValue = new Date(appEndInput.value);
    // 날짜 유효성 검사
    if (appEndValue <= appStartValue) {
        alert("출장 시작일은 출장 종료일보다 이전이어야 합니다");
        return;
    }
    printDate();
}

function handleEndDate() {
    const appStartInput = document.getElementById("input-appStart");
    const appEndInput = document.getElementById("input-appEnd");

    const appStartValue = new Date(appStartInput.value);
    const appEndValue = new Date(appEndInput.value);

    // 날짜 유효성 검사
    if (appEndValue <= appStartValue) {
        alert("출장 종료일은 출장 시작일보다 이후여야 합니다");
        return;
    }

    printDate();
}

function printPlace() {
    const place = document.getElementById('input-appLocation').value;
    document.getElementById('appData-place').innerText = place;
}

function printContent() {
    const content = document.getElementById('input-appContent').value;
    document.getElementById('appData-content').innerText = content;
}