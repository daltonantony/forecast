'use strict';

angular.module('forecastApp')
    .factory('HolidaysSearch', function ($resource) {
        return $resource('api/_search/holidayss/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
