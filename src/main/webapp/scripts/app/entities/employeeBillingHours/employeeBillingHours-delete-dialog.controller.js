'use strict';

angular.module('forecastApp')
	.controller('EmployeeBillingHoursDeleteController', function($scope, $uibModalInstance, entity, EmployeeBillingHours) {

        $scope.employeeBillingHours = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            EmployeeBillingHours.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
