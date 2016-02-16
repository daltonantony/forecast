'use strict';

angular.module('forecastApp').controller('EmployeeBillingHoursDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'EmployeeBillingHours', 'Employee', 'EmployeeBillingHoursForComingMonths',
        function($scope, $stateParams, $uibModalInstance, EmployeeBillingHours, Employee, EmployeeBillingHoursForComingMonths) {

      //  $scope.employeeBillingHours = entity;
        $scope.employees = Employee.query();
        $scope.load = function(id) {
            EmployeeBillingHours.get({id : id}, function(result) {
                $scope.employeeBillingHours = result;
            });
        };

            $scope.loadAll = function (id) {
                EmployeeBillingHoursForComingMonths.get({empId: $stateParams.empId}, function (result) {
                    $scope.forcastEmployeeBillingHours = result;
                });
            };

            $scope.loadAll();

        var onSaveSuccess = function (result) {
            $scope.$emit('forecastApp:employeeBillingHoursUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.employeeBillingHours.id != null) {
                EmployeeBillingHours.update($scope.employeeBillingHours, onSaveSuccess, onSaveError);
            } else {
                EmployeeBillingHours.save($scope.employeeBillingHours, onSaveSuccess, onSaveError);
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
