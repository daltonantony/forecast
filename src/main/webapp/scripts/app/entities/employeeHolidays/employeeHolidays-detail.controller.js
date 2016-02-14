'use strict';

angular.module('forecastApp')
    .controller('EmployeeHolidaysDetailController', function ($scope, $rootScope, $stateParams, entity, EmployeeHolidays, EmployeeBillingHours) {
        $scope.employeeHolidays = entity;
        $scope.load = function (id) {
            EmployeeHolidays.get({id: id}, function(result) {
                $scope.employeeHolidays = result;
            });
        };
        var unsubscribe = $rootScope.$on('forecastApp:employeeHolidaysUpdate', function(event, result) {
            $scope.employeeHolidays = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
