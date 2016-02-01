'use strict';

angular.module('forecastApp')
	.controller('CodeValuesDeleteController', function($scope, $uibModalInstance, entity, CodeValues) {

        $scope.codeValues = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            CodeValues.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
