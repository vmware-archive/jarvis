angular.
    module('JarvisDivvy', []).
    controller('DivvyStationsController', ['$scope', '$http', '$interval', function ($scope, $http, $interval) {
        var loadData = function () {
            $http.get('/api/public-transit/divvy-stations').success(function (data) {
                $scope.divvyStations = data.divvyStations;

                $scope.divvyStations.forEach(function (station) {
                    var totalDocks = station.availableBikes + station.availableDocks;
                    station.percentAvailable = (station.availableBikes / totalDocks) * 100;
                });
            });
        };
        loadData();

        $interval(loadData, 1000 * 30);
    }]);