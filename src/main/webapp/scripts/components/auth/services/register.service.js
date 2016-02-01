'use strict';

angular.module('forecastApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


