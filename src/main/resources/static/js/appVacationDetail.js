$(function () {
//작성자와 로그인 유저가 다르면 수정 삭제 버튼 사라지게 하고 폼 데이터 입력 읽기모드로
// const loginUser = /*[[${#authentication.getName()}]]*/;
// const appWriter = /*[[${approval.userNum.USERENO}]]*/;

// if (loginUser != appWriter) {
//     $("#btnUpdate").hide();
//     $("#btnDelete").hide();
//     $("#inputDate-start").attr("readonly", true);
//     $("#inputDate-end").attr("readonly", true);
//     $("#input-appLocation").attr("readonly", true);
//     $("#input-appContent").attr("readonly", true);
// }

    $("#btnUpdate").on("click", () => {
        $.ajax({
            url: "/appDetail/approval",
            type: "post",
            data: $("#updateForm").serialize(),
            success: (obj) => {
                alert("수정되었습니다.");
                // location.href = `/app/meet/{metNum}`;
                location.href = '/appDetail';
                console.log(obj);
            },
            error: (error) => {
                console.log(error);
            }
        });
    });

    $('#btnDelete').on("click", () => {
        // const appNum = parseInt(document.getElementById("appNum").value, 10);  // radix 10으로 지정하여 10진수로 변환
        // const appNum = document.getElementById("appNum").value;
        const appNum = $("#appNum").val();
        $.ajax({
            url: "/appDetail/approval/" + appNum,
            // url: `/appDetail/approval/${appNum}`,
            type: 'post',
            success: (obj) => {
                alert("삭제되었습니다.");
                console.log(obj);
                location.href = '/appDetail';
                // location.href = `/app/meet/{metNum}`;
                // location.href = '/appDetail';
            },
            error: (error) => {
                console.log(error);
            }
        });
    });

});

function printDate() {
    const dateStart = document.getElementById('input-appStart').value;
    const dateEnd= document.getElementById('input-appEnd').value;
    document.getElementById('appData-vacationDate').innerText = `${dateStart} ~ ${dateEnd}`;
}
function handleVacType(){
    printType();
    changeDateType();
}

function handleDate(){
    handleVacReq();
    printDate();
}

function handleVacReq(){
    calculateDateDifference();
    // printDateDifference();
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
        appStartInput.name = 'appStart2';
        appEndInput.name = 'appEnd2';
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