'use strict';

var app = angular.module('forecastApp');

app.factory('EmployeeHoursAdminSetForecastFreezeDate', function ($resource, DateUtils) {
    return $resource('api/setForecastFreezeDate', {}, {
        'get': {
            method: 'GET',
            transformRequest: function (data) {
                return angular.toJson(data);
            }
        },
        'save': {
            method: 'POST',
            transformRequest: function (data) {
                return angular.toJson(data);
            }
        }
    });
});

app.factory('EmployeeHoursAdminDownloadForecast', function ($resource) {
    return $resource('api/downloadForecast', {}, {
        'get': {
           method: 'GET', isArray : false
       }
    });
});

