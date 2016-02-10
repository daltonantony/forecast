'use strict';

angular.module('forecastApp')
    .controller('CodeValuesController', function ($scope, $state, CodeValues, CodeValuesSearch, ActiveCodeValuesSearch) {

        $scope.codeValuess = [];
        $scope.loadAll = function() {
            CodeValues.query(function(result) {
               $scope.codeValuess = result;
            });
        };
        $scope.loadAll();

        $scope.search = function () {
            CodeValuesSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.codeValuess = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };
        
        $scope.searchActiveCodeValues = function () {
        	ActiveCodeValuesSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.codeValuess = result;
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
            $scope.codeValues = {
                codeType: null,
                codeValue: null,
                effectiveDate: null,
                expiryDate: null,
                lastChangedBy: null,
                lastChangedDate: null,
                id: null
            };
        };
    });
