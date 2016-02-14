'use strict';

angular.module('forecastApp')
    .controller('EmployeeBillingHoursController', function ($scope, $state, EmployeeBillingHours, EmployeeBillingHoursSearch) {

        $scope.employeeBillingHourss = [];
        $scope.loadAll = function() {
            EmployeeBillingHours.query(function(result) {
               $scope.employeeBillingHourss = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            EmployeeBillingHoursSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.employeeBillingHourss = result;
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
            $scope.employeeBillingHours = {
                week1: null,
                week2: null,
                week3: null,
                week4: null,
                week5: null,
                createdDate: null,
                forecastDate: null,
                lastChangedDate: null,
                lastChangedBy: null,
                id: null
            };
        };
    });
