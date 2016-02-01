'use strict';

angular.module('forecastApp')
	.controller('HolidaysDeleteController', function($scope, $uibModalInstance, entity, Holidays) {

        $scope.holidays = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Holidays.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
