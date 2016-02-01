'use strict';

angular.module('forecastApp')
    .factory('ROLE_TARIFFSearch', function ($resource) {
        return $resource('api/_search/rOLE_TARIFFs/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
