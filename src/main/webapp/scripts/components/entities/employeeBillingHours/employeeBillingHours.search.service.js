'use strict';

angular.module('forecastApp')
    .factory('EmployeeBillingHoursSearch', function ($resource) {
        return $resource('api/_search/employeeBillingHourss/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
