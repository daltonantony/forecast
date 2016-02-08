'use strict';

angular.module('forecastApp')
    .controller('EmployeeController', function ($scope, $state, Employee, EmployeeSearch) {

        $scope.employees = [];
        $scope.loadAll = function() {
            Employee.query(function(result) {
               $scope.employees = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            EmployeeSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.employees = result;
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
            $scope.employee = {
                name: null,
                associateId: null,
                clientId: null,
                domain: null,
                lastChangedDate: null,
                lastChangedBy: null,
                id: null
            };
        };
    });
