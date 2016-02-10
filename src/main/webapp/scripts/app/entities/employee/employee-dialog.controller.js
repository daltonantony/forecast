'use strict';

angular.module('forecastApp').controller('EmployeeDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Employee', 'EmployeeAllocation', 'ActiveCodeValuesSearch',
        function($scope, $stateParams, $uibModalInstance, entity, Employee, EmployeeAllocation, ActiveCodeValuesSearch) {

        $scope.employee = entity;
        $scope.employeeallocations = EmployeeAllocation.query();
        $scope.domains = ActiveCodeValuesSearch.query({query: 'Domain'});
        $scope.load = function(id) {
            Employee.get({id : id}, function(result) {
                $scope.employee = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('forecastApp:employeeUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.employee.id != null) {
                Employee.update($scope.employee, onSaveSuccess, onSaveError);
            } else {
                Employee.save($scope.employee, onSaveSuccess, onSaveError);
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
