'use strict';

angular.module('forecastApp').controller('EmployeeAllocationDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'EmployeeAllocation', 'Employee',
        function($scope, $stateParams, $uibModalInstance, entity, EmployeeAllocation, Employee) {

        $scope.employeeAllocation = entity;
        $scope.employees = Employee.query();
        $scope.load = function(id) {
            EmployeeAllocation.get({id : id}, function(result) {
                $scope.employeeAllocation = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('forecastApp:employeeAllocationUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.employeeAllocation.id != null) {
                EmployeeAllocation.update($scope.employeeAllocation, onSaveSuccess, onSaveError);
            } else {
                EmployeeAllocation.save($scope.employeeAllocation, onSaveSuccess, onSaveError);
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
        $scope.datePickerForLastChangedDate = {};

        $scope.datePickerForLastChangedDate.status = {
            opened: false
        };

        $scope.datePickerForLastChangedDateOpen = function($event) {
            $scope.datePickerForLastChangedDate.status.opened = true;
        };
}]);
