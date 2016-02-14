'use strict';

angular.module('forecastApp')
    .factory('EmployeeHolidaysSearch', function ($resource) {
        return $resource('api/_search/employeeHolidayss/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
