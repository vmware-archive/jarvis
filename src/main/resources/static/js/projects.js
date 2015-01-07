angular.
    module('JarvisAllocations', []).
    controller('ProjectsPanelController', function ($scope, $http, $interval) {
        var loadData = function () {
            $http.get('/api/panels/projects').success(function (data) {
                $scope.projects = data.projects;
            });
        };
        loadData();

        $interval(function () {
            loadData();
        }, 86400*1000/24);
    });