let swiper = new Swiper('.swiper-container', {
    speed: 500,
    direction: 'vertical',
    mousewheel: true,
    pagination: {
        el: '.swiper-pagination',
        clickable: true,
    },
    watchOverflow : true,
    on: {
        slideChange: function() {
            setTimeout(function () {
                swiper.params.touchReleaseOnEdges = false;
                swiper.params.mousewheel.releaseOnEdges = false;
            });
        },
        reachEnd: function() {
            setTimeout(function () {
                swiper.params.touchReleaseOnEdges = true;
                swiper.params.mousewheel.releaseOnEdges = true;
            }, 500);
        },
        reachBeginning: function() {
            setTimeout(function () {
                swiper.params.touchReleaseOnEdges = true;
                swiper.params.mousewheel.releaseOnEdges = true;
            }, 500);
        }
    }
});