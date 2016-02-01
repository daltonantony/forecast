'use strict';

angular.module('forecastApp')
	.controller('ROLE_TARIFFDeleteController', function($scope, $uibModalInstance, entity, ROLE_TARIFF) {

        $scope.rOLE_TARIFF = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            ROLE_TARIFF.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
