'use strict';

angular.module('forecastApp')
    .controller('EmployeeBillingHoursDetailController', function ($scope, $rootScope, $stateParams, entity, EmployeeBillingHours, Employee) {
        $scope.employeeBillingHours = entity;
        $scope.load = function (id) {
            EmployeeBillingHours.get({id: id}, function(result) {
                $scope.employeeBillingHours = result;
            });
        };
        var unsubscribe = $rootScope.$on('forecastApp:employeeBillingHoursUpdate', function(event, result) {
            $scope.employeeBillingHours = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
