'use strict';

angular.module('forecastApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('employeeHoursAdmin', {
                parent: 'entity',
                url: '/employeeHoursAdmin',
                data: {
                    authorities: ['ROLE_ADMIN'],
                    pageTitle: 'Employee Hours Administration'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/employeeHours/employeeHoursAdmin.html',
                        controller: 'EmployeeHoursAdminController'
                    }
                },
                resolve: {
                }
            });
    });
