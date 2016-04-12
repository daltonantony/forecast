'use strict';

angular.module('forecastApp')
    .controller('EmployeeHoursAdminController', function ($scope, $state, EmployeeHours, EmployeeHoursAdminSetForecastFreezeDate, EmployeeHoursAdminDownloadForecast, EmployeeHoursAdminShowForecast) {

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
            
            EmployeeHoursAdminShowForecast.get(function (result) {
                $scope.forecastForAllEmployees = result;
            });
        };

        $scope.showDownloadEmployeeHours = function () {
            $scope.showViewEmployeeHoursDiv = false;
            $scope.showDownloadEmployeeHoursDiv = true;
            $scope.showSetForecastFreezeDateDiv = false;

            //EmployeeHoursAdminDownloadForecast.get();
            /*EmployeeHoursAdminDownloadForecast.get(function (result) {

                var blob = new Blob([result], {type: "application/vnd.ms-excel"});
                var objectUrl = URL.createObjectURL(blob);
                window.open(objectUrl);

            });*/
            window.open('api/downloadForecastForAll', '_parent');
        };

        $scope.showSetForecastFreezeDate = function () {
            $scope.showViewEmployeeHoursDiv = false;
            $scope.showDownloadEmployeeHoursDiv = false;
            $scope.showSetForecastFreezeDateDiv = true;

            EmployeeHoursAdminSetForecastFreezeDate.get(function (result) {
                $scope.dto = result;
            });
        };

        $scope.setForecastFreezeDate = function () {
            $scope.showViewEmployeeHoursDiv = false;
            $scope.showDownloadEmployeeHoursDiv = false;
            $scope.showSetForecastFreezeDateDiv = true;

            EmployeeHoursAdminSetForecastFreezeDate.save($scope.dto);
        };

        $scope.datePickerForForecastFreezeDate = {};

        $scope.datePickerForForecastFreezeDate.status = {
            opened: false
        };

        $scope.datePickerForForecastFreezeDateOpen = function($event) {
            $scope.datePickerForForecastFreezeDate.status.opened = true;
        };

    });
