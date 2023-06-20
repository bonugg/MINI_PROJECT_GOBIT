$(function() {

    $('.slider-2 .page-nav > div').click(function () {
        console.log("5")

        let $this = $(this);
        let $pagenav = $this.parent()
        let $current = $pagenav.find('.active');

        $current.removeClass('active');
        $this.addClass('active');

        let index = $this.index();
        let $slider = $this.closest('.slider-2');

        $slider.find('.slides > div.active').removeClass('active');
        $slider.find('.slides > div').eq(index).addClass('active');


    });

    $('.slider-2 > .side-btns > div:first-child').click(function () {
        console.log("6")
        let $this = $(this);
        let $slider = $this.closest('.slider-2');

        let $current = $slider.find('.page-nav > div.active');
        let $post = $current.prev();

        if ($post.length == 0) {
            $post = $slider.find('.page-nav > div:last-child');
        }

        $post.click();
    });

    $('.slider-2 > .side-btns > div:last-child').click(function () {
        console.log("5")
        let $this = $(this);
        let $slider = $this.closest('.slider-2');

        let $current = $slider.find('.page-nav > div.active');
        let $post = $current.next();

        if ($post.length == 0) {
            $post = $slider.find('.page-nav > div:first-child');
        }

        $post.click();
    });

});