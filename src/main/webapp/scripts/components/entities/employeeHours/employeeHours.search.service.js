'use strict';

angular.module('forecastApp')
    .factory('EmployeeHoursSearch', function ($resource) {
        return $resource('api/_search/employeeHourss/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
