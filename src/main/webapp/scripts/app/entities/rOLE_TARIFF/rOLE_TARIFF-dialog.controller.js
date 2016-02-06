'use strict';

angular.module('forecastApp').controller('ROLE_TARIFFDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'ROLE_TARIFF', 'CodeValuesSearch',
        function($scope, $stateParams, $uibModalInstance, entity, ROLE_TARIFF, CodeValuesSearch) {

    	$scope.roles = CodeValuesSearch.query({query: 'Role'});
    	$scope.locations = CodeValuesSearch.query({query: 'Location'});
        $scope.rOLE_TARIFF = entity;
        $scope.load = function(id) {
            ROLE_TARIFF.get({id : id}, function(result) {
                $scope.rOLE_TARIFF = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('forecastApp:rOLE_TARIFFUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.rOLE_TARIFF.id != null) {
                ROLE_TARIFF.update($scope.rOLE_TARIFF, onSaveSuccess, onSaveError);
            } else {
                ROLE_TARIFF.save($scope.rOLE_TARIFF, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForEffectiveDate = {};

        $scope.datePickerForEffectiveDate.status = {
            opened: false
        };

        $scope.datePickerForEffectiveDateOpen = function($event) {
            $scope.datePickerForEffectiveDate.status.opened = true;
        };
        $scope.datePickerForExpiryDate = {};

        $scope.datePickerForExpiryDate.status = {
            opened: false
        };

        $scope.datePickerForExpiryDateOpen = function($event) {
            $scope.datePickerForExpiryDate.status.opened = true;
        };
        $scope.datePickerForLastChangedDate = {};

        $scope.datePickerForLastChangedDate.status = {
            opened: false
        };

        $scope.datePickerForLastChangedDateOpen = function($event) {
            $scope.datePickerForLastChangedDate.status.opened = true;
        };
}]);
