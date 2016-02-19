'use strict';

angular.module('forecastApp')
    .controller('EmployeeDetailController', function ($scope, $rootScope, $stateParams, entity, Employee, EmployeeAllocationForAnEmployee) {
        $scope.employee = entity;
        $scope.load = function (id) {
            Employee.get({id: id}, function(result) {
                $scope.employee = result;
                EmployeeAllocationForAnEmployee.get({empId: $scope.employee.id}, function(result) {
                    $scope.employeeAllocations = result;
                });
            });
        };
        var unsubscribe = $rootScope.$on('forecastApp:employeeUpdate', function(event, result) {
            $scope.employee = result;
        });
        $scope.$on('$destroy', unsubscribe);
        $scope.load($stateParams.id);

    });
