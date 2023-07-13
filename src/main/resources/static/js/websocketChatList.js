$(function () {
// 현재 페이지 URL이 /main 인 경우에만 웹소켓 연결을 시작합니다.
    window.onload = () => {
            const chaturl = `ws://${location.host}/database-change?userNum=` + userNum;
            setupWebSocketAsync(chaturl);
    };

    function scrollToBottom() {
        let messageDiv = document.getElementById("message_div");
        messageDiv.scrollTop = messageDiv.scrollHeight;
    }


    async function setupWebSocketAsync(url) {
        const socket = new WebSocket(url);

        socket.onopen = () => {
            console.log(url);
            console.log("Connected to WebSocket server");
        };

        socket.onmessage = (event) => {
            console.log("---------------");
            let jsonObj = JSON.parse(event.data);
            console.log(jsonObj);
            console.log("---------------");
            if(jsonObj.receiveUser == userNum){
                $('#num'+jsonObj.sendid).css("display", "").text(jsonObj.sendUserCnt);
            }
            if($('#num'+jsonObj.sendid).text() == 0){
                $('#num'+jsonObj.sendid).css("display", "none");
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
});