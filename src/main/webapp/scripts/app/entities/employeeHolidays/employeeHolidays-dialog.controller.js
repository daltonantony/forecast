'use strict';

angular.module('forecastApp').controller('EmployeeHolidaysDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'EmployeeHolidays', 'EmployeeBillingHours',
        function($scope, $stateParams, $uibModalInstance, entity, EmployeeHolidays, EmployeeBillingHours) {

        $scope.employeeHolidays = entity;
        $scope.employeebillinghourss = EmployeeBillingHours.query();
        $scope.load = function(id) {
            EmployeeHolidays.get({id : id}, function(result) {
                $scope.employeeHolidays = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('forecastApp:employeeHolidaysUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.employeeHolidays.id != null) {
                EmployeeHolidays.update($scope.employeeHolidays, onSaveSuccess, onSaveError);
            } else {
                EmployeeHolidays.save($scope.employeeHolidays, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForLastChangedDate = {};

        $scope.datePickerForLastChangedDate.status = {
            opened: false
        };

        $scope.datePickerForLastChangedDateOpen = function($event) {
            $scope.datePickerForLastChangedDate.status.opened = true;
        };
}]);
