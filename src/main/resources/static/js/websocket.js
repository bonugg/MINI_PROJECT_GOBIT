$(function () {
    if ($('#hid').text() != 0) {
        $('#hid_1').css("display", "block");
        $('#hid_2').css("display", "block");
        $('#hid').css("display", "block");
    }
// 현재 페이지 URL이 /main 인 경우에만 웹소켓 연결을 시작합니다.
    window.onload = () => {
        const url = `ws://${location.host}/database-change?dept=` + userDept;
        setupWebSocketAsync(url);
    };

    async function setupWebSocketAsync(url) {
        const socket = new WebSocket(url);

        socket.onopen = () => {
            console.log(url);
            console.log("Connected to WebSocket server");
        };

        socket.onmessage = (event) => {
            let jsonObj = JSON.parse(event.data);
            $('#hid').text(jsonObj.testcnt);
            if (userDept == jsonObj.userdept) {
                if (jsonObj.end == 0) {
                    if (window.location.pathname === "/") {
                        let startObj = new Date(jsonObj.start);
                        let hour = startObj.getHours();
                        if (hour <= 9) {
                            $('#' + jsonObj.usernum + " .on_img").css("backgroundColor", "#181F42");
                        } else {
                            $('#' + jsonObj.usernum + " .on_img").css("backgroundColor", "rgba(24, 31, 66, 0.5)");
                        }
                        $('#ontime' + jsonObj.usernum).val(jsonObj.start);

                    }
                    message = jsonObj.username + `님이 출근하였습니다.`;

                } else {
                    if (window.location.pathname === "/") {
                        let endObj = new Date(jsonObj.end);
                        let hour = endObj.getHours();
                        if (hour >= 18) {
                            $('#' + jsonObj.usernum + " .off_img").css("backgroundColor", "#181F42");
                        } else {
                            $('#' + jsonObj.usernum + " .off_img").css("backgroundColor", "rgba(24, 31, 66, 0.5)");
                        }
                        $('#offtime' + jsonObj.usernum).val(jsonObj.end);
                    }
                    message = jsonObj.username + `님이 퇴근하였습니다.`;
                }
                showNotification(message);
            }
            if (jsonObj.testusernum == userNum) {
                if ($('#hid').text() != 0) {
                    $('#hid_1').css("display", "block");
                    $('#hid_2').css("display", "block");
                    $('#hid').css("display", "block").text(jsonObj.testcnt);
                }
            }
        };

        socket.onclose = () => {
            console.log("Disconnected from WebSocket server");
        };

        // WebSocket이 종료될 때까지 기다리기
        await new Promise((resolve) => {
            socket.addEventListener("close", resolve);
        });
    }

    function showNotification(message) {
        const notificationWrapper = document.createElement("div");
        notificationWrapper.innerHTML = `<div class="notification">${message}</div>`;
        notificationWrapper.classList.add('notification-wrapper');

        // CSS 스타일을 추가
        const style = `
        width : 220px;
        height : 80px;
        position: fixed;
        bottom: 1rem;
        right: 1rem;
        padding: 8px 16px;
        background-color: #181F42;
        color: white;
        border-radius: 4px;
        font-size: 1rem;
        font-weight : bold;
        display: flex;
        align-items: center;
        justify-content: center;
        z-index: 1000;
        opacity: 0;
        transition: opacity 0.5s;
    `;
        const styleWrapper = `
        position: fixed;
        bottom: 1rem;
        right: 1rem;
        z-index: 999;
    `;
        notificationWrapper.firstChild.setAttribute("style", style);
        notificationWrapper.setAttribute("style", styleWrapper);

        document.body.appendChild(notificationWrapper);

        // 알림 표시
        setTimeout(() => {
            notificationWrapper.firstChild.style.opacity = 1;
        }, 100);

        // 알림 숨기기
        setTimeout(() => {
            notificationWrapper.firstChild.style.opacity = 0;
            setTimeout(() => {
                notificationWrapper.remove();
            }, 500); // 500ms 동안 서서히 사라지게 설정
        }, 3000);
    }
});