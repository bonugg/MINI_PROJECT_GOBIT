$(function() {
    $.ajax({ //출퇴근 버튼 상태 가져오기
        type: 'POST',
        url: '/chart',
        success: function (result) {
            console.log(result.yearMonthChart);
            console.log(result.yearChart);
            // Initialize the echarts instance based on the prepared dom
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
                                value: result.yearChart,
                                name: result.yearChart + '시간',
                                itemStyle: {
                                    color: '#253170'
                                }
                            },
                            {
                                value: 8 - result.yearChart,
                                name: 8 - result.yearChart + '시간',
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
                                value: result.yearMonthChart,
                                name: result.yearMonthChart + '시간',
                                itemStyle: {
                                    color: '#181F42'
                                }
                            },
                            {
                                value: 8 - result.yearMonthChart,
                                name: 8 - result.yearMonthChart + '시간',
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
        }
    });
});