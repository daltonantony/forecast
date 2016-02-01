'use strict';

angular.module('forecastApp')
    .factory('ROLE_TARIFF', function ($resource, DateUtils) {
        return $resource('api/rOLE_TARIFFs/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.effectiveDate = DateUtils.convertLocaleDateFromServer(data.effectiveDate);
                    data.expiryDate = DateUtils.convertLocaleDateFromServer(data.expiryDate);
                    data.lastChangedDate = DateUtils.convertLocaleDateFromServer(data.lastChangedDate);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.effectiveDate = DateUtils.convertLocaleDateToServer(data.effectiveDate);
                    data.expiryDate = DateUtils.convertLocaleDateToServer(data.expiryDate);
                    data.lastChangedDate = DateUtils.convertLocaleDateToServer(data.lastChangedDate);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.effectiveDate = DateUtils.convertLocaleDateToServer(data.effectiveDate);
                    data.expiryDate = DateUtils.convertLocaleDateToServer(data.expiryDate);
                    data.lastChangedDate = DateUtils.convertLocaleDateToServer(data.lastChangedDate);
                    return angular.toJson(data);
                }
            }
        });
    });
