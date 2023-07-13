$(function () {
    let popupLayer = document.getElementById("popup_layer");
    const onButton = document.getElementById('on');
    const offButton = document.getElementById('off');
    let today = new Date();
    let year = today.getFullYear();
    let month = (today.getMonth() + 1).toString().padStart(2, '0'); // 월은 1월이 0으로 시작하므로 1을 더합니다.
    let day = today.getDate().toString().padStart(2, '0'); // 일자가 두 자리가 되도록 왼쪽에 0을 추가합니다.
    const pattern = "58.29.191.61";
    // const pattern = "218.153.162.95";
    let myChart = echarts.init(document.getElementById('main'));
    let formattedDate = `${year}-${month}-${day}`; // yyyy-MM-dd 형식으로 날짜를 표현합니다.
    let changeChartHandlerAdded = true; //이벤트 리스너가 한번만 등록되도록 함

    //----------차트------------------
    $(document).ready(function () {
        charton();
    });
    function charton() {
        $.ajax({
            type: 'POST',
            url: '/chart',
            success: function (result) {
                myChart.resize();
                let chartSize = myChart.getWidth();
                if(chartSize <= 100){
                    charton();
                    return;
                }
                // Initialize the echarts instance based on the prepared dom

                // Specify the configuration items and data for the chart
                let option1 = {
                    title: {
                        text: ((result.yearChart / 8) * 100).toFixed(1) + '%',
                        left: 'center',
                        top: 'center',
                        a: '1'
                    },
                    series: [
                        {
                            type: 'pie',
                            data: [
                                {
                                    value: result.yearChart,
                                    name: Number(result.yearChart.toFixed(1)) + '시간',
                                    itemStyle: {
                                        color: '#253170'
                                    }
                                },
                                {
                                    value: 8 - result.yearChart,
                                    name: Number(8 - result.yearChart.toFixed(1)).toFixed(1) + '시간',
                                    itemStyle: {
                                        color: '#E3E3E3'
                                    }
                                }
                            ],
                            radius: ['50%', '70%']
                        }
                    ]
                };
                // Display the chart using the configuration items and data just specified.
                myChart.setOption(option1);
                myChart.on('mouseover', function (params) {
                    let hoverInfo = document.getElementById('hoverInfo');
                    hoverInfo.innerHTML = params.name;
                    hoverInfo.style.display = 'block';
                });
                let main = document.getElementById('main');
                main.addEventListener('mousemove', function (event) {
                    let hoverInfo = document.getElementById('hoverInfo');
                    hoverInfo.style.left = event.clientX + 20 + 'px';
                    hoverInfo.style.top = event.clientY - 30 + 'px';
                });
                myChart.on('mouseout', function () {
                    let hoverInfo = document.getElementById('hoverInfo');
                    hoverInfo.style.display = 'none';
                });

                // Specify the configuration items and data for the chart
                let option2 = {
                    title: {
                        text: ((result.yearMonthChart / 8) * 100).toFixed(1) + '%',
                        left: 'center',
                        top: 'center',
                        a: '2'
                    },
                    series: [
                        {
                            type: 'pie',
                            data: [
                                {
                                    value: result.yearMonthChart,
                                    name: Number(result.yearMonthChart.toFixed(1)) + '시간',
                                    itemStyle: {
                                        color: '#181F42'
                                    }
                                },
                                {
                                    value: 8 - result.yearMonthChart,
                                    name: Number(8 - result.yearMonthChart.toFixed(1)).toFixed(1) + '시간',
                                    itemStyle: {
                                        color: '#E3E3E3'
                                    }
                                }
                            ],
                            radius: ['50%', '70%']
                        }
                    ]
                };
                // Display the chart using the configuration items and data just specified.
                myChart.setOption(option2);

                let currentOption = myChart.getOption().title[0].a;
                if (changeChartHandlerAdded) {
                    document.getElementById('changeChart').addEventListener('click', () => {
                        if (currentOption == myChart.getOption(option1).title[0].a) {
                            $('#changeChart').css("backgroundColor", "#253170");
                            $('#chart_title').text("년간 근무 시간");
                            myChart.setOption(option1);
                        } else {
                            $('#changeChart').css("backgroundColor", "#181F42");
                            $('#chart_title').text("월간 근무 시간");
                            myChart.setOption(option2);

                        }
                    });
                    changeChartHandlerAdded = false;
                }

                window.addEventListener('resize', function () {
                    myChart.resize();
                });
            },
            error: function (error) {
                let myChart = echarts.init(document.getElementById('main'));

                // Specify the configuration items and data for the chart
                let option1 = {
                    title: {
                        text: '년간 평균 근무시간',
                        left: 'center',
                        top: 'center',
                        a: '1'
                    },
                    series: [
                        {
                            type: 'pie',
                            data: [
                                {
                                    value: 0,
                                    name: 0 + '시간',
                                    itemStyle: {
                                        color: '#E3E3E3'
                                    }
                                }
                            ],
                            radius: ['50%', '70%']
                        }
                    ]
                };
                // Display the chart using the configuration items and data just specified.
                myChart.setOption(option1);

                // Specify the configuration items and data for the chart
                let option2 = {
                    title: {
                        text: '월간 평균 근무시간',
                        left: 'center',
                        top: 'center',
                        a: '2'
                    },
                    series: [
                        {
                            type: 'pie',
                            data: [
                                {
                                    value: 0,
                                    name: 0 + '시간',
                                    itemStyle: {
                                        color: '#E3E3E3'
                                    }
                                }
                            ],
                            radius: ['50%', '70%']
                        }
                    ]
                };
                // Display the chart using the configuration items and data just specified.
                myChart.setOption(option2);
                let currentOption = myChart.getOption().title[0].a;
                document.getElementById('changeChart').addEventListener('click', () => {
                    if (currentOption == myChart.getOption(option1).title[0].a) {
                        $('#changeChart').css("backgroundColor", "#253170");
                        myChart.setOption(option1);
                    } else {
                        $('#changeChart').css("backgroundColor", "#181F42");
                        myChart.setOption(option2);

                    }
                });
                window.addEventListener('resize', function () {
                    myChart.resize();
                });
            }
        });
    }

    //-------------------------------

    $.ajax({
        type: 'get',
        url: '/dboardList',
        success: function (obj) {
            if (obj.length == 0) {
                $('#dboardList').html('전사게시판 글이 없습니다.');
            } else {
                let userListHTML = '';
                for (let i = 0; i < obj.length; i++) {
                    let item = obj[i];
                    const regex = /(<([^>]+)>)/ig;
                    item.dboardContent = item.dboardContent.replace(regex, "");
                    console.log(item.dboardContent);
                    userListHTML += `
                                         <table class="notice_table" onclick="location.href='/boardDept/updateCnt/${item.dboardNum}'">
                                                    <tr>
                                                        <td class="notice_td">
                                                            <div class="notice_td_div">
                                                                <div class="notice_type">${item.dboardDept}</div>
                                                            </div>
                                                        </td>
                                                        <td class="notice_title_td">${item.dboardTitle}</td>
                                                        <td class="notice_writer_cnt_td">작성자</td>
                                                        <td class="notice_writer_cnt_td2">${item.dboardWriter}</td>
                                                        <td class="notice_writer_cnt_td">조회수</td>
                                                        <td class="notice_writer_cnt_td2" id="bcnt">${item.dboardCnt}</td>
                                                    </tr>
                                                    <tr class="tr1410">
                                                        <td rowspan="2" class="board_img_td">
                                                   
                                                                <div class="notice_img_div2">
                                                                    <div class="img">
                                                                        전사 게시판
                                                                    </div>
                                                            </div>
                                                        </td>
                                                        <td colspan="4" rowspan="2" class="notice_content_td">
                                                            <div class="notice_content_div">
                                                                <div class="notice_content_div2">
                                                                    <span>${item.dboardContent}
                                                                    </span>
                                                                </div>
                                                            </div>
                                                        </td>
                                                        <td></td>
                                                    </tr>
                                                    <tr class="tr1410">
                                                        <td></td>
                                                    </tr>
                                                </table>
                                    `;
                }
                $('#dboardList').html(userListHTML);
            }
        },
        error: function (err) {
            console.log(err);
        }
    });
    $.ajax({
        type: 'get',
        url: '/nboardList',
        success: function (obj) {
            if (obj.length == 0) {
                $('#nboardList').html('공지게시판 글이 없습니다.');
            } else {
                let userListHTML = '';
                for (let i = 0; i < obj.length; i++) {
                    let item = obj[i];
                    userListHTML += `
                                         <table class="notice_table" onclick="location.href='/noticeDept/updateCnt/${item.nboardNum}'">
                                                    <tr>
                                                        <td class="notice_td">
                                                            <div class="notice_td_div">
                                                                <div class="notice_type">전체</div>
                                                            </div>
                                                        </td>
                                                        <td class="notice_title_td">${item.nboardTitle}</td>
                                                        <td class="notice_writer_cnt_td">작성자</td>
                                                        <td class="notice_writer_cnt_td2">${item.nboardWriter}</td>
                                                        <td class="notice_writer_cnt_td">조회수</td>
                                                        <td class="notice_writer_cnt_td2" id="bcnt">${item.nboardCnt}</td>
                                                    </tr>
                                                    <tr class="tr1410">
                                                             <td rowspan="2" class="board_img_td">
                                                   
                                                                <div class="notice_img_div2">
                                                                    <div class="img">
                                                                        공지 사항
                                                                    </div>
                                                            </div>
                                                        </td>
                                                        <td colspan="4" rowspan="2" class="notice_content_td">
                                                            <div class="notice_content_div">
                                                                <div class="notice_content_div2">
                                                                    <span>${item.nboardContent}
                                                                    </span>
                                                                </div>
                                                            </div>
                                                        </td>
                                                        <td></td>
                                                    </tr>
                                                    <tr class="tr1410">
                                                        <td></td>
                                                    </tr>
                                                </table>
                                    `;
                }
                $('#nboardList').html(userListHTML);
            }
        },
        error: function (err) {
            console.log(err);
        }
    });

    document.querySelectorAll('.off_img').forEach(function (div) {
        div.addEventListener('mouseover', function (event) {
            let tooltip = event.target.closest('.cno_list').querySelector('.tooltip');
            let offTimeValue = event.target.closest('.off_img').querySelector('input[type="hidden"]').value;
            let offtime = formatDatesec(offTimeValue);
            if (offtime == "NaN시 aN분 aN초") {
                offtime = "퇴근 정보 없음";
            }
            tooltip.innerHTML = offtime;
            tooltip.style.display = 'block';
        });
        div.addEventListener('mousemove', function (event) {
            let tooltip = event.target.closest('.cno_list').querySelector('.tooltip');
            tooltip.style.left = (event.pageX + 10) + 'px';
            tooltip.style.top = (event.pageY + 10) + 'px';
        });


        div.addEventListener('mouseout', function (event) {
            let tooltip = event.currentTarget.querySelector('.tooltip');
            tooltip.style.display = 'none';
        });
    });

    document.querySelectorAll('.on_img').forEach(function (div) {
        div.addEventListener('mouseover', function (event) {
            let tooltip = event.target.closest('.cno_list').querySelector('.tooltip');
            let onTimeValue = event.target.closest('.on_img').querySelector('input[type="hidden"]').value;
            let ontime = formatDatesec(onTimeValue);
            if (ontime == "NaN시 aN분 aN초") {
                ontime = "출근 정보 없음";
            }
            tooltip.innerHTML = ontime;
            tooltip.style.display = 'block';
        });
        div.addEventListener('mousemove', function (event) {
            let tooltip = event.target.closest('.cno_list').querySelector('.tooltip');
            tooltip.style.left = (event.pageX + 10) + 'px';
            tooltip.style.top = (event.pageY + 10) + 'px';
        });

        div.addEventListener('mouseout', function (event) {
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
                charton();
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
                        $('.popup_box').css("height", "");
                        if (info.event.extendedProps.classify == '출퇴근') {
                            $('.popup_cont_0').css("margin-bottom", "10px");
                            $('#title').text("출퇴근 캘린더 상세보기");
                            $('.popup_cont_1').css("height", "60px");
                            $('.popup_cont_2').css("height", "60px");
                            $('#popup_date').css("font-size", "15px");
                            $('.popup_cont_3').css("display", "none");
                            $('.popup_cont_4').css("display", "none");
                            $('.popup_cont_5').css("display", "none");
                            $('#vacation_type').css("display", "none");
                            $('#approve_name').css("display", "none");
                        } else if (info.event.extendedProps.classify == 'V') {
                            $('.popup_cont_0').css("margin-bottom", "20px");
                            $('#title').text("휴가 캘린더 상세보기");
                            $('.popup_cont_1').css("height", "60px");
                            $('.popup_cont_2').css("height", "60px");
                            $('.popup_cont_3').css("height", "60px");
                            $('.popup_cont_3').css("display", "");
                            $('#vacation_type').css("display", "");
                            $('#approve_name').css("display", "");
                            $('.popup_cont_4').css("display", "none");
                            $('.popup_cont_5').css("display", "none");
                        } else if (info.event.extendedProps.classify == 'B') {
                            $('#title').text("출장 캘린더 상세보기");
                            $('.popup_cont_0').css("margin-bottom", "20px");
                            $('.popup_cont_1').css("height", "60px");
                            $('.popup_cont_2').css("height", "60px");
                            $('.popup_cont_4').css("height", "60px");
                            $('.popup_cont_3').css("height", "60px");
                            $('.popup_cont_4').css("display", "");
                            $('.popup_cont_3').css("display", "");
                            $('#vacation_type').css("display", "");
                            $('#approve_name').css("display", "");
                            $('.popup_cont_5').css("display", "none");
                        } else if (info.event.extendedProps.classify == 'M') {
                            $('.popup_cont_0').css("margin-bottom", "20px");
                            $('#title').text("회의 캘린더 상세보기");
                            $('.popup_cont_1').css("height", "60px");
                            $('.popup_cont_2').css("height", "60px");
                            $('.popup_cont_4').css("height", "60px");
                            $('.popup_cont_5').css("height", "60px");
                            $('.popup_cont_3').css("height", "60px");
                            $('.popup_cont_4').css("display", "");
                            $('.popup_cont_5').css("display", "");
                            $('.popup_cont_3').css("display", "");
                            $('#vacation_type').css("display", "");
                            $('#approve_name').css("display", "");
                        }
                        $('#applocate').html("<b>장소 : </b>" + info.event.extendedProps.applocate);
                        $('#meetingPT').html("<b>회의 참여자 : </b>" + info.event.extendedProps.meetingPT);
                        $('#vacation_type').text(info.event.extendedProps.vacationtype);
                        $('#approve_name').html("<b>승인자 </b>" + info.event.extendedProps.approvedName);
                        $('#popup_title').text(info.event.title);
                        $('#conetent').html("<b>내용 : </b>" + info.event.extendedProps.description);


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
                        return event.classify === 'V';
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
                        return event.classify === 'M';
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
                        return event.classify === 'B';
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