'use strict';

angular.module('forecastApp')
    .controller('EmployeeDetailController', function ($scope, $rootScope, $stateParams, entity, Employee, EmployeeAllocation) {
        $scope.employee = entity;
        $scope.load = function (id) {
            Employee.get({id: id}, function(result) {
                $scope.employee = result;
            });
        };
        var unsubscribe = $rootScope.$on('forecastApp:employeeUpdate', function(event, result) {
            $scope.employee = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
