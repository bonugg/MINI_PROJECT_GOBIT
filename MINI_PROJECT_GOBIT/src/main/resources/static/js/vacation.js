let popupLayer = document.getElementById("popup_layer");

$('#cancle_btn').on('click', function () {
    popupLayer.style.display = "block";
    if (popupLayer.style.display === "block") {
        document.addEventListener("click", function (event) {
            if (event.target.id == "popup_layer"
                && popupLayer.contains(event.target)) {
                popupLayer.style.display = "none";
            }
        });
    }
});

if ($('#hidden_p').text() === "승인") {
    $('#access_btn').prop('disabled', true);
}
if ($('#hidden_p').text() === "반려") {
    $('#cancle_btn').prop('disabled', true);
}


const showvacationview = (VNO) => {
    location.href = "/vacationAcessView?VNO=" + VNO;
}
const showmeetingview = (VNO) => {
    location.href = "/meetingAcessView?VNO=" + VNO;
}
const showbusinessiew = (VNO) => {
    location.href = "/businessAcessView?VNO=" + VNO;
}
const vacationview = (VNO) => {
    location.href = "/vacationView?VNO=" + VNO;
}
const meetingview = (VNO) => {
    location.href = "/meetingView?VNO=" + VNO;
}
const businessiew = (VNO) => {
    location.href = "/businessView?VNO=" + VNO;
}
const Accessva = (VNO, DEPT, CLASSIFY) => {
    location.href = "/Access?VNO=" + VNO + "&DEPT=" + DEPT + "&CLASSIFY=" + CLASSIFY;
}
const Cancle = (VNO, CLASSIFY, CANCLEREASON) => {
    location.href = "/Cancle?VNO=" + VNO + "&CLASSIFY=" + CLASSIFY + "&CANCLEREASON=" + CANCLEREASON;
}


const leave_type = document.getElementById('leave_type');
const start_date = document.getElementById('start');
const end_date = document.getElementById('end');


leave_type.addEventListener('change', function () {
    if (leave_type.value === '반차') {
        start_date.type = 'datetime-local';  // 반차인 경우, input 타입을 datetime-local로 변경
        end_date.type = 'datetime-local';  // 반차인 경우, input 타입을 datetime-local로 변경
    } else {
        start_date.type = 'date';  // 그 외의 경우, input 타입을 date로 변경
        end_date.type = 'date';  // 그 외의 경우, input 타입을 date로 변경
    }
});

