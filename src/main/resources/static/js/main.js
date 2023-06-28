$(function () {
    let popupLayer = document.getElementById("popup_layer");
    const onButton = document.getElementById('on');
    const offButton = document.getElementById('off');
    let today = new Date();
    let year = today.getFullYear();
    let month = (today.getMonth() + 1).toString().padStart(2, '0'); // 월은 1월이 0으로 시작하므로 1을 더합니다.
    let day = today.getDate().toString().padStart(2, '0'); // 일자가 두 자리가 되도록 왼쪽에 0을 추가합니다.
    const pattern = "218.153.162.95";

    let formattedDate = `${year}-${month}-${day}`; // yyyy-MM-dd 형식으로 날짜를 표현합니다.

    document.querySelectorAll('.off_img').forEach(function(div) {
        div.addEventListener('mouseover', function(event) {
            let tooltip = event.target.closest('.cno_list').querySelector('.tooltip');
            let offTimeValue = event.target.closest('.off_img').querySelector('input[type="hidden"]').value;
            let offtime = formatDatesec(offTimeValue);
            if(offtime == "NaN시 aN분 aN초"){
                offtime = "퇴근 정보 없음";
            }
            tooltip.innerHTML = offtime;
            tooltip.style.display = 'block';
        });
        div.addEventListener('mousemove', function(event) {
            let tooltip = event.target.closest('.cno_list').querySelector('.tooltip');
            tooltip.style.left = (event.pageX + 10) + 'px';
            tooltip.style.top = (event.pageY + 10) + 'px';
        });


        div.addEventListener('mouseout', function(event) {
            let tooltip = event.currentTarget.querySelector('.tooltip');
            tooltip.style.display = 'none';
        });
    });

    document.querySelectorAll('.on_img').forEach(function(div) {
        div.addEventListener('mouseover', function(event) {
            let tooltip = event.target.closest('.cno_list').querySelector('.tooltip');
            let onTimeValue = event.target.closest('.on_img').querySelector('input[type="hidden"]').value;
            let ontime = formatDatesec(onTimeValue);
            if(ontime == "NaN시 aN분 aN초"){
                ontime = "출근 정보 없음";
            }
            tooltip.innerHTML = ontime;
            tooltip.style.display = 'block';
        });
        div.addEventListener('mousemove', function(event) {
            let tooltip = event.target.closest('.cno_list').querySelector('.tooltip');
            tooltip.style.left = (event.pageX + 10) + 'px';
            tooltip.style.top = (event.pageY + 10) + 'px';
        });

        div.addEventListener('mouseout', function(event) {
            let tooltip = event.currentTarget.querySelector('.tooltip');
            tooltip.style.display = 'none';
        });
    });

    //출근 퇴근 색상별 표시
    $(document).ready(function () {
        $('.on_div').each(function () {
            let text = $(this).data('check').toString();

            if (text === '0') {
                $(this).find('.on_img').css('backgroundColor', 'white');
            } else if (text === '1') {
                $(this).find('.on_img').css('backgroundColor', '#181F42');
            } else if (text === '2') {
                $(this).find('.on_img').css('backgroundColor', 'rgba(24, 31, 66, 0.5)');
            }
        });
        $('.off_div').each(function () {
            let text = $(this).data('check').toString();

            if (text === '0') {
                $(this).find('.off_img').css('background-color', 'white');
            } else if (text === '1') {
                $(this).find('.off_img').css('background-color', '#181F42');
            } else if (text === '2') {
                $(this).find('.off_img').css('background-color', 'rgba(24, 31, 66, 0.5)');
            }
        });
    });

    function ipcheck(onoff) {
        $.getJSON('https://jsonip.com?format=json', function (data) {
            let ipAddress = data.ip;
            console.log(ipAddress);
            if (pattern === ipAddress && onoff == on) {
                $("#on").removeAttr("disabled");
            } else if (pattern === ipAddress && onoff == off) {
                $("#off").removeAttr("disabled");
            }
        })
    }

    $('#on').attr('disabled', true);
    $('#off').attr('disabled', true);

    onButton.onclick = function () { //출근 버튼 클릭 이벤트
        $.ajax({
            type: 'POST',
            url: '/onadd',
            success: function (outputDateString) {
                let formattedStartDate = formatDateString(outputDateString);
                $('#on').attr('disabled', true);
                $('#on_text').text('출근 시각 : ' + formattedStartDate);
                $('#off').removeAttr("disabled");
            },
            error: function (xhr) {
                if (xhr.status === 401) {
                    // 상태 코드가 401 Unauthorized인 경우 로그인 페이지로 이동
                    window.location.href = "/login";
                }
            }
        });
    }
    $.ajax({ //출퇴근 버튼 상태 가져오기
        type: 'POST',
        url: '/onaddcheck',
        data: {
            start: formattedDate
        },
        success: function (startdate) {
            console.log(startdate);
            if (startdate != "") {
                let formattedStartDate = formatDateString(startdate);
                $('#on').attr('disabled', true);
                $('#on_text').text('출근 시각 : ' + formattedStartDate);
                $.ajax({
                    type: 'POST',
                    url: '/offaddcheck',
                    data: {
                        end: formattedDate
                    },
                    success: function (enddate) {
                        if (enddate != "") {
                            let formattedEndDate = formatDateString(enddate);
                            $('#off').attr('disabled', true);
                            $('#off_text').text('퇴근 시각 : ' + formattedEndDate);
                        } else {
                            ipcheck(off);
                        }
                    }
                });
            } else {
                ipcheck(on);
            }
        }
    });
    offButton.onclick = function () { //퇴근 버튼 클릭 이벤트
        $.ajax({
            type: 'POST',
            url: '/offadd',
            data: {
                start: formattedDate
            },
            success: function (outputDateString) {
                let formattedEndDate = formatDateString(outputDateString);
                $('#off').attr('disabled', true);
                $('#off_text').text('퇴근 시각 : ' + formattedEndDate);
                onContentLoaded();
                $('#all').click();
            },
            error: function (xhr) {
                if (xhr.status === 401) {
                    // 상태 코드가 401 Unauthorized인 경우 로그인 페이지로 이동
                    window.location.href = "/login";
                }
            }
        });
    }

    onContentLoaded();

    function onContentLoaded() {
        // 원래 'DOMContentLoaded' 이벤트 리스너에서 실행하려고 했던 코드를 작성
        $(function () {
            const request = $.ajax({
                url: "/main/calendar",
                method: "GET",
                dataType: "json"
            });
            request.done(function (data) {
                console.log(data);
                const calendarEl = document.getElementById('calendar');
                $('#all').css("backgroundColor", "white");

                let filteredAttendanceData = data.filter(function (event) {
                    return event.title === '출근' && event.no == userNum;
                }).map(function (event) {
                    return {
                        ...event,
                        backgroundColor: '#008000',
                        borderColor: '#008000'
                    };
                });

                let filteredEarlyLeaveData = data.filter(function (event) {//휴가가 아닌 데이터 필터링
                    return event.title === '조퇴' && event.no == userNum;
                }).map(function (event) {
                    return {
                        ...event,
                        backgroundColor: 'orange',
                        borderColor: 'orange'
                    };
                });

                let filteredLatenessData = data.filter(function (event) {
                    return event.title === '지각' && event.no == userNum;
                }).map(function (event) {
                    return {
                        ...event,
                        backgroundColor: 'orange',
                        borderColor: 'orange'
                    };
                });

                let filteredAbsentData = data.filter(function (event) {
                    return event.title === '결근' && event.no == userNum;
                }).map(function (event) {
                    return {
                        ...event,
                        backgroundColor: 'indianred',
                        borderColor: 'indianred'
                    };
                });

                let newEventData = filteredAttendanceData.concat(filteredEarlyLeaveData).concat(filteredLatenessData).concat(filteredAbsentData)
                    .map(function (event) {

                        const newEvent = Object.assign({}, event); // 객체 복제
                        const endDate = new Date(newEvent.end); // 'end' 날짜 문자열을 Date 객체로 변환
                        return newEvent;
                    });


                let calendar = new FullCalendar.Calendar(calendarEl, {
                    events: newEventData,
                    dayMaxEventRows: true, // for all non-TimeGrid views
                    views: {
                        timeGrid: {
                            dayMaxEventRows: 6 // adjust to 6 only for timeGridWeek/timeGridDay
                        }
                    },
                    initialView: 'dayGridMonth',
                    headerToolbar: {
                        left: 'today prev',
                        center: 'title',
                        right: 'next'
                    },
                    firstDay: 1, //월요일부터 시작
                    locale: "ko",
                    dayMaxEvents: true, // 이벤트가 오버되면 높이 제한 (+ 몇 개식으로 표현)
                    eventClick: function (info) {
                        popupLayer.style.display = "block";

                        if (info.event.extendedProps.classify == '휴가') {
                            $('#popup_vacationtype_div').text(info.event.extendedProps.title).show();
                            $('#popup_desc_div').css("height", "64%");
                        } else {
                            $('#popup_vacationtype_div').hide();
                            $('#popup_desc_div').css("height", "73%");
                        }
                        $('#popup_title').text(info.event.title);
                        $('#popup_desc').text(info.event.extendedProps.description);


                        let datedata = [];
                        datedata[0] = info.event.start;
                        datedata[1] = info.event.end;

                        for (let i = 0; i < datedata.length; i++) {
                            const year = datedata[i].getFullYear();
                            const month = String(datedata[i].getMonth() + 1).padStart(2, "0");
                            const day = String(datedata[i].getDate()).padStart(2, "0");
                            const hours = String(datedata[i].getHours()).padStart(2, "0");
                            const minutes = String(datedata[i].getMinutes()).padStart(2, "0");
                            const seconds = String(datedata[i].getSeconds()).padStart(2, "0");

                            datedata[i] = `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
                        }

                        $('#popup_date').text(datedata[0] + " - " + datedata[1]);

                        if (popupLayer.style.display === "block") {
                            document.addEventListener("click", function (event) {
                                if (event.target.id == "popup_layer"
                                    && popupLayer.contains(event.target)) {
                                    popupLayer.style.display = "none";
                                }
                            });
                        }
                    }
                });
                calendar.render();

                // #vation 버튼 클릭 이벤트 핸들러 등록
                $('#vacation').on('click', function () {
                    let filteredEvents = data.filter(function (event) {
                        return event.classify === '휴가';
                    }).map(function (event) {
                        return {
                            ...event,
                            backgroundColor: '#008000',
                            borderColor: '#008000'
                        };
                    }).map(function (event) {
                        const newEvent = Object.assign({}, event); // 객체 복제
                        const endDate = new Date(newEvent.end); // 'end' 날짜 문자열을 Date 객체로 변환
                        endDate.setDate(endDate.getDate() + 1); // 날짜를 1일 추가
                        newEvent.end = endDate.toISOString().substring(0, 10); // ISO 8601 문자열로 변환 후, 문자열 일부를 추출
                        return newEvent;
                    });

                    $('#vacation').css("backgroundColor", "white");
                    $('#meeting').css("backgroundColor", "");
                    $('#business').css("backgroundColor", "");
                    $('#all').css("backgroundColor", "");
                    calendar.removeAllEvents();
                    calendar.addEventSource(filteredEvents);
                });
                $('#meeting').on('click', function () {
                    let filteredEvents = data.filter(function (event) {
                        return event.classify === '회의';
                    }).map(function (event) {
                        return {
                            ...event,
                            backgroundColor: 'orange',
                            borderColor: 'orange'
                        };
                    }).map(function (event) {
                        const newEvent = Object.assign({}, event); // 객체 복제
                        const endDate = new Date(newEvent.end); // 'end' 날짜 문자열을 Date 객체로 변환
                        endDate.setDate(endDate.getDate() + 1); // 날짜를 1일 추가
                        newEvent.end = endDate.toISOString().substring(0, 10); // ISO 8601 문자열로 변환 후, 문자열 일부를 추출
                        return newEvent;
                    });
                    $('#meeting').css("backgroundColor", "white");
                    $('#vacation').css("backgroundColor", "");
                    $('#business').css("backgroundColor", "");
                    $('#all').css("backgroundColor", "");
                    calendar.removeAllEvents();
                    calendar.addEventSource(filteredEvents);
                });
                $('#business').on('click', function () {
                    let filteredEvents = data.filter(function (event) {
                        return event.classify === '출장';
                    }).map(function (event) {
                        return {
                            ...event,
                            backgroundColor: 'indianred',
                            borderColor: 'indianred'
                        };
                    }).map(function (event) {
                        const newEvent = Object.assign({}, event); // 객체 복제
                        const endDate = new Date(newEvent.end); // 'end' 날짜 문자열을 Date 객체로 변환
                        endDate.setDate(endDate.getDate() + 1); // 날짜를 1일 추가
                        newEvent.end = endDate.toISOString().substring(0, 10); // ISO 8601 문자열로 변환 후, 문자열 일부를 추출
                        return newEvent;
                    });
                    $('#business').css("backgroundColor", "white");
                    $('#all').css("backgroundColor", "");
                    $('#vacation').css("backgroundColor", "");
                    $('#meeting').css("backgroundColor", "");
                    calendar.removeAllEvents();
                    calendar.addEventSource(filteredEvents);
                });
                $('#all').on('click', function () {
                    $('#all').css("backgroundColor", "white");
                    $('#business').css("backgroundColor", "");
                    $('#vacation').css("backgroundColor", "");
                    $('#meeting').css("backgroundColor", "");
                    calendar.removeAllEvents();
                    calendar.addEventSource(newEventData);
                });
            });
        });
    }

    document.addEventListener('DOMContentLoaded', onContentLoaded);
});

//날짜 형식 포맷
function formatDateString(dateString) {
    let date = new Date(dateString);
    let hours = date.getHours();
    let minutes = ('0' + date.getMinutes()).slice(-2);

    return `${hours}시 ${minutes}분`;
}

function formatDatesec(dateString) {
    let date = new Date(dateString);
    let hours = date.getHours();
    let minutes = ('0' + date.getMinutes()).slice(-2);
    let sec = ('0' + date.getSeconds()).slice(-2);

    return `${hours}시 ${minutes}분 ${sec}초`;
}