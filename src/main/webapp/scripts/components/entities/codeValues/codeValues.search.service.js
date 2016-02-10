'use strict';

var app = angular.module('forecastApp');

    app.factory('CodeValuesSearch', function ($resource) {
        return $resource('api/_search/codeValuess/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
    
    app.factory('ActiveCodeValuesSearch', function ($resource) {
        return $resource('api/_search/activeCodeValues/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
