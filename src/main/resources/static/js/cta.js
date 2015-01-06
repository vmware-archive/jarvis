angular.
    module('JarvisCTA', []).
    controller('CTAPanelController', function ($scope, $http, $interval, $sce) {
        var due = "<i class=\"fa fa-bus\"></i>";
        var missed = '<i class="fa fa-arrow-left"></i>';
        moment.locale('en-terse', {
            relativeTime: {
                future: "%s",
                past: missed,
                s: due,
                ss: due,
                m: due,
                mm: "%d",
                h: "an hour",
                hh: "%d hours",
                d: "a day",
                dd: "%d days",
                M: "a month",
                MM: "%d months",
                y: "a year",
                yy: "%d years"
            }
        });

        var customStationNames = {
            'Merchandise Mart': 'Mart',
            'Clark/Lake': 'Clark'
        };

        var loadData = function () {
            $http.get('/api/panels/cta').success(function (data) {
                var stationLineGroupings = {};
                angular.forEach(data.predictions, function (prediction) {
                    var key = prediction.station + prediction.line;
                    if (!stationLineGroupings[key]) {
                        stationLineGroupings[key] = {
                            station: customStationNames[prediction.station] || prediction.station,
                            line: prediction.line,
                            destinations: {}
                        };
                    }
                    var stationLineGrouping = stationLineGroupings[key];
                    if (!stationLineGrouping.destinations[prediction.destination]) {
                        stationLineGrouping.destinations[prediction.destination] = [];
                    }
                    stationLineGrouping.destinations[prediction.destination].push(moment(prediction.upcomingTime));
                });

                $scope.predictions = stationLineGroupings;
            });
        };
        loadData();

        $scope.fake = 0;
        $interval(function () {
            $scope.fake = $scope.fake + 1;
        }, 10 * 1000);

        $interval(function () {
            loadData();
        }, 60 * 1000);

        $scope.trust = function (stuff) {
            return $sce.trustAsHtml(stuff);
        };
    });