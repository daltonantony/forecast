'use strict';

angular.module('forecastApp').controller('CodeValuesDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'CodeValues',
        function($scope, $stateParams, $uibModalInstance, entity, CodeValues) {

        $scope.codeValues = entity;
        $scope.load = function(id) {
            CodeValues.get({id : id}, function(result) {
                $scope.codeValues = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('forecastApp:codeValuesUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.codeValues.id != null) {
                CodeValues.update($scope.codeValues, onSaveSuccess, onSaveError);
            } else {
                CodeValues.save($scope.codeValues, onSaveSuccess, onSaveError);
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
