'use strict';

angular.module('forecastApp')
    .controller('EmployeeHoursController', function ($scope, $state, EmployeeHours, EmployeeHoursSearch, EmployeeHoursForComingMonths, EmployeeHoursForPreviousMonths) {

        $scope.employeeHourss = [];
        $scope.loadAll = function() {
            EmployeeHours.query(function(result) {
               $scope.employeeHourss = result;
            });
        };
        $scope.loadAll();

        $scope.newForecastForComingMonths = function () {
            $scope.newForecast = true;
            $scope.oldForecast = false;
            EmployeeHoursForComingMonths.get(function (result) {
                $scope.newForecastEmployeeHours = result;
            });
        };

        $scope.oldForecastForPreviousMonths = function () {
            $scope.oldForecast = true;
            $scope.newForecast = false;
            EmployeeHoursForPreviousMonths.get(function (result) {
                $scope.oldForecastEmployeeHoursMap = result;
            });
        };

        $scope.save = function () {
            EmployeeHoursForComingMonths.save($scope.newForecastEmployeeHours);
        };

        $scope.search = function () {
            EmployeeHoursSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.employeeHourss = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.employeeHours = {
                holidays: null,
                createdDate: null,
                forecastDate: null,
                lastChangedDate: null,
                lastChangedBy: null,
                id: null
            };
        };
    });
