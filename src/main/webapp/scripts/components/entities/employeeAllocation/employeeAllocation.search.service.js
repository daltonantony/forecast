'use strict';

angular.module('forecastApp')
    .factory('EmployeeAllocationSearch', function ($resource) {
        return $resource('api/_search/employeeAllocations/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
