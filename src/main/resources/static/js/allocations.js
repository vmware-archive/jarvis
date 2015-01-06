angular.
    module('JarvisAllocations', []).
    controller('AllocationsPanelController', function ($scope, $http, $interval) {
        var loadData = function () {
            $http.get('/api/panels/allocations').success(function (data) {
                $scope.projects = data.projects;
            });
        };
        loadData();

        $interval(function () {
            loadData();
        }, 86400*1000/24);
    });