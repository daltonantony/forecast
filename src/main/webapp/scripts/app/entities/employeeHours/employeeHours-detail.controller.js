'use strict';

angular.module('forecastApp')
    .controller('EmployeeHoursDetailController', function ($scope, $rootScope, $stateParams, entity, EmployeeHours, Employee) {
        $scope.employeeHours = entity;
        $scope.load = function (id) {
            EmployeeHours.get({id: id}, function(result) {
                $scope.employeeHours = result;
            });
        };
        var unsubscribe = $rootScope.$on('forecastApp:employeeHoursUpdate', function(event, result) {
            $scope.employeeHours = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
