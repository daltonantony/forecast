'use strict';

angular.module('forecastApp')
	.controller('EmployeeHoursDeleteController', function($scope, $uibModalInstance, entity, EmployeeHours) {

        $scope.employeeHours = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            EmployeeHours.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
