$(function() {
    $.ajax({ //출퇴근 버튼 상태 가져오기
        type: 'POST',
        url: '/chart',
        success: function (result) {
            // Initialize the echarts instance based on the prepared dom
            let myChart = echarts.init(document.getElementById('main'));
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
            window.addEventListener('resize', function () {
                myChart.resize();
            });
        },
        error: function(error) {
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
        }
    });
});