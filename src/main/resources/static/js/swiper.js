$(function() {
    let container = document.querySelector(".section_1");
    let swiper = new Swiper('.swiper-container', {
        speed: 500,
        direction: 'vertical',
        mousewheel: true,
        pagination: {
            el: '.swiper-pagination',
            clickable: true,
        },
        watchOverflow: true,
        on: {
        }
    });

    let swiper2 = new Swiper('#swiper2', {
        speed: 500,
        direction: 'horizontal',
        mousewheel: true,
        pagination: {
            el: '.swiper-pagination',
            clickable: true,
        },
        watchOverflow: true,
        effect: 'cube', // 변경된 부분
        cubeEffect: { // 추가된 부분
            slideShadows: false,
            shadow: false
        },
        on: {

        }
    });
    swiper2.autoplay.start();
});