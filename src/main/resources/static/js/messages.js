angular.
    module('JarvisMessages', []).
    controller('MessagesPanelController', function ($scope, $http, $interval) {
        var loadData = function () {
            $http.get('/api/sms-messages').success(function (data) {
                $scope.messages = data.messages;
            });
        };
        loadData();

        $scope.numberToSMS = "(312) 548-9277";

        $interval(function () {
            loadData();
        }, 60 * 1000);
    }).
    directive('scrolling', ['$interval', '$compile', function ($interval, $compile) {
        return {
            scope: true,
            compile: function (el) {
                for (var i = 0; i < 4; i++) {
                    var theClone = angular.element(el).clone().removeAttr('scrolling');
                    angular.element(el).after(theClone);
                }
                return function (scope, $element) {
                    scope.$watch(function () {
                            return scope.messages;
                        },
                        function (newValue, oldValue) {
                            if (newValue != oldValue) {
                                var remeasuredWidth = $element[0].clientWidth;
                                var parent = angular.element($element[0]).parent();
                                parent[0].style.webkitAnimation = 'slide ' + String(remeasuredWidth / 70) + 's infinite linear';
                            }
                        });
                }
            }
        }
    }]);