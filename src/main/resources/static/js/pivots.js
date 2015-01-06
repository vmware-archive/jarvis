angular.
    module('JarvisPivots', []).
    service('jvPivotsService', ['$http', '$timeout', function (http, timeout) {
        var cachedPivots = { email: {}, phone: {} };

        var isLoading = false;
        var maybeLoad = function () {
            if (isLoading) {
                return;
            }
            isLoading = true;

            http.get('/api/panels/pivots').success(function (data) {
                angular.forEach(data.pivots, function (pivot) {
                    if (cachedPivots.email[pivot.email]) {
                        angular.extend(cachedPivots.email[pivot.email], pivot);
                    } else {
                        cachedPivots.email[pivot.email] = pivot;
                    }

                    if (cachedPivots.phone[pivot.phone]) {
                        angular.extend(cachedPivots.phone[pivot.phone], pivot);
                    } else {
                        cachedPivots.phone[pivot.phone] = pivot;
                    }
                });

                isLoading = false;
            });
        };

        return {
            findByEmail: function (email) {
                if (!cachedPivots.email[email]) {
                    maybeLoad();
                    cachedPivots.email[email] = {name: '?'};
                }
                return cachedPivots.email[email];
            },
            findByPhone: function (phone) {
                if (!cachedPivots.phone[phone]) {
                    maybeLoad();
                    cachedPivots.phone[phone] = {name: '?'};
                }
                return cachedPivots.phone[phone];
            }
        }
    }]).
    directive('jvPivot', ['jvPivotsService', function (pivotsService) {
        return {
            restrict: 'E',
            template: '{{pivot.name | initials}}',
            link: function (scope, element, attributes) {
                if (attributes.email) {
                    scope.pivot = pivotsService.findByEmail(attributes.email);
                } else if (attributes.phone) {
                    scope.pivot = pivotsService.findByPhone(attributes.phone);
                }
            }
        }
    }]).
    filter("initials", function () {
        return function (input) {
            if (!input) {
                return '';
            }
            var initials = input.match(/\b\w/g);
            return initials ? initials.join("") : input;
        }
    });