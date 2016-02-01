'use strict';

angular.module('forecastApp')
	.controller('EmployeeAllocationDeleteController', function($scope, $uibModalInstance, entity, EmployeeAllocation) {

        $scope.employeeAllocation = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            EmployeeAllocation.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
