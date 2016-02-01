'use strict';

angular.module('forecastApp')
    .factory('CodeValuesSearch', function ($resource) {
        return $resource('api/_search/codeValuess/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
