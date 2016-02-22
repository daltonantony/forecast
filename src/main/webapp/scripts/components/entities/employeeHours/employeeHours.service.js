'use strict';

var app = angular.module('forecastApp');

    app.factory('EmployeeHours', function ($resource, DateUtils) {
        return $resource('api/employeeHourss/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.createdDate = DateUtils.convertLocaleDateFromServer(data.createdDate);
                    data.forecastDate = DateUtils.convertLocaleDateFromServer(data.forecastDate);
                    data.lastChangedDate = DateUtils.convertLocaleDateFromServer(data.lastChangedDate);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.createdDate = DateUtils.convertLocaleDateToServer(data.createdDate);
                    data.forecastDate = DateUtils.convertLocaleDateToServer(data.forecastDate);
                    data.lastChangedDate = DateUtils.convertLocaleDateToServer(data.lastChangedDate);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.createdDate = DateUtils.convertLocaleDateToServer(data.createdDate);
                    data.forecastDate = DateUtils.convertLocaleDateToServer(data.forecastDate);
                    data.lastChangedDate = DateUtils.convertLocaleDateToServer(data.lastChangedDate);
                    return angular.toJson(data);
                }
            }
        });
    });

app.factory('EmployeeHoursForComingMonths', function ($resource) {
    return $resource('api/employeeHoursForComingMonths', {}, {
        'get': { method: 'GET', isArray: false},
        'save': {
            method: 'POST',
            transformRequest: function (data) {
                return angular.toJson(data);
            }
        }
    });
});

