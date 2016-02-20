'use strict';

angular.module('forecastApp')
    .controller('EmployeeHoursController', function ($scope, $state, EmployeeHours, EmployeeHoursSearch, EmployeeHoursForComingMonths, WorkingDatesPerWeekForMonths) {

        $scope.employeeHourss = [];
        $scope.loadAll = function() {
            EmployeeHours.query(function(result) {
               $scope.employeeHourss = result;
            });
        };
        $scope.loadAll();

        $scope.newForecastForComingMonths = function () {
            $scope.newForecast = true;
            EmployeeHoursForComingMonths.get(function (result) {
                $scope.forcastEmployeeHours = result;
            });
/*            WorkingDatesPerWeekForMonths.get(function (result) {
                $scope.workingDatesPerWeekForMonths = result;
            });*/
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
                week1: null,
                week2: null,
                week3: null,
                week4: null,
                week5: null,
                createdDate: null,
                forecastDate: null,
                type: null,
                lastChangedDate: null,
                lastChangedBy: null,
                id: null
            };
        };
    });
