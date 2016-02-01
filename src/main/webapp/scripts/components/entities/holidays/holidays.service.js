'use strict';

angular.module('forecastApp')
    .factory('Holidays', function ($resource, DateUtils) {
        return $resource('api/holidayss/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.startDate = DateUtils.convertLocaleDateFromServer(data.startDate);
                    data.endDate = DateUtils.convertLocaleDateFromServer(data.endDate);
                    data.lastChangedDt = DateUtils.convertLocaleDateFromServer(data.lastChangedDt);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.startDate = DateUtils.convertLocaleDateToServer(data.startDate);
                    data.endDate = DateUtils.convertLocaleDateToServer(data.endDate);
                    data.lastChangedDt = DateUtils.convertLocaleDateToServer(data.lastChangedDt);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.startDate = DateUtils.convertLocaleDateToServer(data.startDate);
                    data.endDate = DateUtils.convertLocaleDateToServer(data.endDate);
                    data.lastChangedDt = DateUtils.convertLocaleDateToServer(data.lastChangedDt);
                    return angular.toJson(data);
                }
            }
        });
    });
