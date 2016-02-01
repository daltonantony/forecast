'use strict';

angular.module('forecastApp')
    .controller('HolidaysController', function ($scope, $state, Holidays, HolidaysSearch) {

        $scope.holidayss = [];
        $scope.loadAll = function() {
            Holidays.query(function(result) {
               $scope.holidayss = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            HolidaysSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.holidayss = result;
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
            $scope.holidays = {
                name: null,
                startDate: null,
                endDate: null,
                location: null,
                lastChangedBy: null,
                lastChangedDt: null,
                id: null
            };
        };
    });
