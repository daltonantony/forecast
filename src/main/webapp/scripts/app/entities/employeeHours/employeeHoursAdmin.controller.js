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
            $scope.showEmployeeForecast = false;

            EmployeeHoursAdminShowForecast.get(function (result) {
                $scope.dto = result;
            });
        };

        $scope.showEachEmployeeHours = function () {
            $scope.showViewEmployeeHoursDiv = true;
            $scope.showDownloadEmployeeHoursDiv = false;
            $scope.showSetForecastFreezeDateDiv = false;
            $scope.showEmployeeForecast = true;
        };

        $scope.showDownloadEmployeeHours = function () {
            $scope.showViewEmployeeHoursDiv = false;
            $scope.showDownloadEmployeeHoursDiv = true;
            $scope.showSetForecastFreezeDateDiv = false;
			$scope.showEmployeeForecast = false;

            //EmployeeHoursAdminDownloadForecast.get();
            /*EmployeeHoursAdminDownloadForecast.get(function (result) {

                var blob = new Blob([result], {type: "application/vnd.ms-excel"});
                var objectUrl = URL.createObjectURL(blob);
                window.open(objectUrl);

            });*/
            window.open('api/downloadForecastForAll', '_parent');
        };
        
        $scope.setSelectedEmployee = function () {
        	var selectedId = $scope.dto.employeeIdSelected;
        	angular.forEach($scope.dto.employeesForecast, function(employeeForecast){
                if(employeeForecast.associateId === parseInt($scope.dto.employeeIdSelected, 10)){
                	$scope.dto.employeeHours = employeeForecast.employeeHours;
                	$scope.dto.employeeNameSelected = employeeForecast.name;
                	$scope.showEachEmployeeHours();
                }
            })
        };

        $scope.showSetForecastFreezeDate = function () {
            $scope.showViewEmployeeHoursDiv = false;
            $scope.showDownloadEmployeeHoursDiv = false;
            $scope.showSetForecastFreezeDateDiv = true;
			$scope.showEmployeeForecast = false;

            EmployeeHoursAdminSetForecastFreezeDate.get(function (result) {
                $scope.dto = result;
            });
        };

        $scope.setForecastFreezeDate = function () {
            $scope.showViewEmployeeHoursDiv = false;
            $scope.showDownloadEmployeeHoursDiv = false;
            $scope.showSetForecastFreezeDateDiv = true;
			$scope.showEmployeeForecast = false;

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
