'use strict';

angular.module('forecastApp')
    .controller('EmployeeDetailController', function ($scope, $rootScope, $stateParams, entity, Employee, EmployeeAllocationSearch) {
        $scope.employee = entity;
        $scope.load = function (id) {
            Employee.get({id: id}, function(result) {
                $scope.employee = result;
                $scope.employeeAllocations = EmployeeAllocationSearch.query({query: $scope.employee.name});
            });
        };
        var unsubscribe = $rootScope.$on('forecastApp:employeeUpdate', function(event, result) {
            $scope.employee = result;
        });
        $scope.$on('$destroy', unsubscribe);
        $scope.load($stateParams.id);

    });
