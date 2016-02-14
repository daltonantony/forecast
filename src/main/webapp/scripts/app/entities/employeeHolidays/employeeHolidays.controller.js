'use strict';

angular.module('forecastApp')
    .controller('EmployeeHolidaysController', function ($scope, $state, EmployeeHolidays, EmployeeHolidaysSearch) {

        $scope.employeeHolidayss = [];
        $scope.loadAll = function() {
            EmployeeHolidays.query(function(result) {
               $scope.employeeHolidayss = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            EmployeeHolidaysSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.employeeHolidayss = result;
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
            $scope.employeeHolidays = {
                week1: null,
                week2: null,
                week3: null,
                week4: null,
                week5: null,
                lastChangedDate: null,
                lastChangedBy: null,
                id: null
            };
        };
    });
