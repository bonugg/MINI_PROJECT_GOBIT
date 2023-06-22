$(function() {
    let slideTimer = setInterval(slideTimeout, 5000);  // 5초마다 실행

    function slideTimeout() {
        let $current = $('.slider-2 .page-nav > div.active');
        let $post = $current.next();

        if ($post.length == 0) {
            $post = $('.slider-2 .page-nav > div:first-child');
        }

        $post.click();
    }
    $('.slider-2 .page-nav > div').click(function () {
        let $this = $(this);
        let $pagenav = $this.parent()
        let $current = $pagenav.find('.active');

        $current.removeClass('active');
        $this.addClass('active');

        let index = $this.index();
        let $slider = $this.closest('.slider-2');

        $slider.find('.slides > div.active').removeClass('active');
        $slider.find('.slides > div').eq(index).addClass('active');

        clearInterval(slideTimer);
        slideTimer = setInterval(slideTimeout, 5000);  // 5초마다 실행
    });

    $('.slider-2 > .side-btns > div:first-child').click(function () {
        let $this = $(this);
        let $slider = $this.closest('.slider-2');

        let $current = $slider.find('.page-nav > div.active');
        let $post = $current.prev();

        if ($post.length == 0) {
            $post = $slider.find('.page-nav > div:last-child');
        }

        $post.click();
        clearInterval(slideTimer);
        slideTimer = setInterval(slideTimeout, 5000);  // 5초마다 실행
    });

    $('.slider-2 > .side-btns > div:last-child').click(function () {
        let $this = $(this);
        let $slider = $this.closest('.slider-2');

        let $current = $slider.find('.page-nav > div.active');
        let $post = $current.next();

        if ($post.length == 0) {
            $post = $slider.find('.page-nav > div:first-child');
        }

        $post.click();
        clearInterval(slideTimer);
        slideTimer = setInterval(slideTimeout, 5000);  // 5초마다 실행
    });

});