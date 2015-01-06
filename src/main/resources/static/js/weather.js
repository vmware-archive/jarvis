angular.
    module('JarvisWeather', []).
    controller('WeatherPanelController', ['$scope', '$http', '$interval', function (scope, http, interval) {
        var forecastURL = 'https://api.forecast.io/forecast/e2942214586788038186936fdfb4198f/41.8884899,-87.6358495?callback=JSON_CALLBACK';

        var loadData = function () {
            http.jsonp(forecastURL).success(function (data) {
                scope.forecast = data;
            });
        };
        loadData();

        interval(function () {
            loadData();
        }, 5 * 60 * 1000);
    }]).
    directive('jvWeatherIcon', [function () {
        var iconMap = {
            'clear-day': 'wi-day-sunny',
            'clear-night': 'wi-night-clear',
            'rain': 'wi-rain',
            'snow': 'wi-snow',
            'sleet': 'wi-rain-mix',
            'wind': 'wi-strong-wind',
            'fog': 'wi-fog',
            'cloudy': 'wi-cloudy',
            'partly-cloudy-day': 'wi-day-sunny-overcast',
            'partly-cloudy-night': 'wi-night-cloudy'
        };

        return {
            restrict: 'E',
            template: '<i class="wi {{icon}}"></i>',
            link: function (scope, element, attributes) {
                scope.icon = iconMap[attributes.icon] || ('wi-' + attributes.icon);
            }
        }
    }]).
    filter("moment", function () {
        return function (input, format) {
            return moment.unix(input).format(format);
        }
    });
