'use strict';

angular.module('forecastApp')
	.controller('EmployeeHolidaysDeleteController', function($scope, $uibModalInstance, entity, EmployeeHolidays) {

        $scope.employeeHolidays = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            EmployeeHolidays.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
