'use strict';

angular.module('forecastApp')
    .controller('EmployeeHoursController', function ($scope, $state, EmployeeHours, EmployeeHoursSearch, EmployeeHoursForComingMonths) {

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
        };

        $scope.save = function () {
            EmployeeHoursForComingMonths.save($scope.forcastEmployeeHours);
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
