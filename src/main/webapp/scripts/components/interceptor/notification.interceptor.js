 'use strict';

angular.module('forecastApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-forecastApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-forecastApp-params')});
                }
                return response;
            }
        };
    });
