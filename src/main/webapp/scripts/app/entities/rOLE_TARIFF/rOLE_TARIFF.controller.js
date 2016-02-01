'use strict';

angular.module('forecastApp')
    .controller('ROLE_TARIFFController', function ($scope, $state, ROLE_TARIFF, ROLE_TARIFFSearch) {

        $scope.rOLE_TARIFFs = [];
        $scope.loadAll = function() {
            ROLE_TARIFF.query(function(result) {
               $scope.rOLE_TARIFFs = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            ROLE_TARIFFSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.rOLE_TARIFFs = result;
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
            $scope.rOLE_TARIFF = {
                role: null,
                tariff: null,
                location: null,
                effectiveDate: null,
                expiryDate: null,
                lastChangedDate: null,
                lastChangedBy: null,
                id: null
            };
        };
    });
