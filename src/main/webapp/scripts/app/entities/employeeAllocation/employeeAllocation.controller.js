'use strict';

angular.module('forecastApp')
    .controller('EmployeeAllocationController', function ($scope, $state, EmployeeAllocation, EmployeeAllocationSearch) {

        $scope.employeeAllocations = [];
        $scope.loadAll = function() {
            EmployeeAllocation.query(function(result) {
               $scope.employeeAllocations = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            EmployeeAllocationSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.employeeAllocations = result;
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
            $scope.employeeAllocation = {
                project: null,
                startDate: null,
                endDate: null,
                location: null,
                allocation: null,
                lastChangedDate: null,
                lastChangedby: null,
                role: null,
                id: null
            };
        };
    });
