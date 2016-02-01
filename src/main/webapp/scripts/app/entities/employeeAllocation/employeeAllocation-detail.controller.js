'use strict';

angular.module('forecastApp')
    .controller('EmployeeAllocationDetailController', function ($scope, $rootScope, $stateParams, entity, EmployeeAllocation, Employee) {
        $scope.employeeAllocation = entity;
        $scope.load = function (id) {
            EmployeeAllocation.get({id: id}, function(result) {
                $scope.employeeAllocation = result;
            });
        };
        var unsubscribe = $rootScope.$on('forecastApp:employeeAllocationUpdate', function(event, result) {
            $scope.employeeAllocation = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
