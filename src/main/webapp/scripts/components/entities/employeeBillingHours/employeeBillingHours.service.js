'use strict';

var app = angular.module('forecastApp');

app.factory('EmployeeBillingHours', function ($resource, DateUtils) {
        return $resource('api/employeeBillingHourss/:id', {}, {
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

app.factory('EmployeeBillingHoursForComingMonths', function ($resource) {
    return $resource('api/employeeBillingHoursComingMonths/:empId', {}, {
        'get': { method: 'GET', isArray: true}
    });
});
