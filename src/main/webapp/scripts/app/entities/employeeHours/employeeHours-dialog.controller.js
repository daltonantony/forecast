'use strict';

angular.module('forecastApp').controller('EmployeeHoursDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'EmployeeHours', 'Employee',
        function($scope, $stateParams, $uibModalInstance, entity, EmployeeHours, Employee) {

        $scope.employeeHours = entity;
        $scope.employees = Employee.query();
        $scope.load = function(id) {
            EmployeeHours.get({id : id}, function(result) {
                $scope.employeeHours = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('forecastApp:employeeHoursUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.employeeHours.id != null) {
                EmployeeHours.update($scope.employeeHours, onSaveSuccess, onSaveError);
            } else {
                EmployeeHours.save($scope.employeeHours, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForCreatedDate = {};

        $scope.datePickerForCreatedDate.status = {
            opened: false
        };

        $scope.datePickerForCreatedDateOpen = function($event) {
            $scope.datePickerForCreatedDate.status.opened = true;
        };
        $scope.datePickerForForecastDate = {};

        $scope.datePickerForForecastDate.status = {
            opened: false
        };

        $scope.datePickerForForecastDateOpen = function($event) {
            $scope.datePickerForForecastDate.status.opened = true;
        };
        $scope.datePickerForLastChangedDate = {};

        $scope.datePickerForLastChangedDate.status = {
            opened: false
        };

        $scope.datePickerForLastChangedDateOpen = function($event) {
            $scope.datePickerForLastChangedDate.status.opened = true;
        };
}]);
