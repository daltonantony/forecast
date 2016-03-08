'use strict';

angular.module('forecastApp')
    .controller('EmployeeHoursAdminController', function ($scope, $state, EmployeeHours) {

        $scope.employeeHourss = [];
        $scope.loadAll = function() {
            EmployeeHours.query(function(result) {
               $scope.employeeHourss = result;
            });
        };
        $scope.loadAll();

        $scope.showViewEmployeeHours = function () {
            $scope.showViewEmployeeHoursDiv = true;
            $scope.showDownloadEmployeeHoursDiv = false;
            $scope.showSetForecastFreezeDateDiv = false;
        };

        $scope.showDownloadEmployeeHours = function () {
            $scope.showViewEmployeeHoursDiv = false;
            $scope.showDownloadEmployeeHoursDiv = true;
            $scope.showSetForecastFreezeDateDiv = false;
        };

        $scope.showSetForecastFreezeDate = function () {
            $scope.showViewEmployeeHoursDiv = false;
            $scope.showDownloadEmployeeHoursDiv = false;
            $scope.showSetForecastFreezeDateDiv = true;
        };

    });
