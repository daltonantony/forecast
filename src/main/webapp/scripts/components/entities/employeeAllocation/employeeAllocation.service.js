'use strict';

angular.module('forecastApp')
    .factory('EmployeeAllocation', function ($resource, DateUtils) {
        return $resource('api/employeeAllocations/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.startDate = DateUtils.convertLocaleDateFromServer(data.startDate);
                    data.endDate = DateUtils.convertLocaleDateFromServer(data.endDate);
                    data.lastChangedDate = DateUtils.convertLocaleDateFromServer(data.lastChangedDate);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.startDate = DateUtils.convertLocaleDateToServer(data.startDate);
                    data.endDate = DateUtils.convertLocaleDateToServer(data.endDate);
                    data.lastChangedDate = DateUtils.convertLocaleDateToServer(data.lastChangedDate);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.startDate = DateUtils.convertLocaleDateToServer(data.startDate);
                    data.endDate = DateUtils.convertLocaleDateToServer(data.endDate);
                    data.lastChangedDate = DateUtils.convertLocaleDateToServer(data.lastChangedDate);
                    return angular.toJson(data);
                }
            }
        });
    });
