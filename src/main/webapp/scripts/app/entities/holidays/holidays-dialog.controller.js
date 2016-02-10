'use strict';

angular.module('forecastApp').controller('HolidaysDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Holidays', 'ActiveCodeValuesSearch',
        function($scope, $stateParams, $uibModalInstance, entity, Holidays, ActiveCodeValuesSearch) {

    	$scope.locations = ActiveCodeValuesSearch.query({query: 'Location'});
        $scope.holidays = entity;
        $scope.load = function(id) {
            Holidays.get({id : id}, function(result) {
                $scope.holidays = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('forecastApp:holidaysUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.holidays.id != null) {
                Holidays.update($scope.holidays, onSaveSuccess, onSaveError);
            } else {
                Holidays.save($scope.holidays, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForStartDate = {};

        $scope.datePickerForStartDate.status = {
            opened: false
        };

        $scope.datePickerForStartDateOpen = function($event) {
            $scope.datePickerForStartDate.status.opened = true;
        };
        $scope.datePickerForEndDate = {};

        $scope.datePickerForEndDate.status = {
            opened: false
        };

        $scope.datePickerForEndDateOpen = function($event) {
            $scope.datePickerForEndDate.status.opened = true;
        };
        $scope.datePickerForLastChangedDt = {};

        $scope.datePickerForLastChangedDt.status = {
            opened: false
        };

        $scope.datePickerForLastChangedDtOpen = function($event) {
            $scope.datePickerForLastChangedDt.status.opened = true;
        };
}]);
