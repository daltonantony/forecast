'use strict';

angular.module('forecastApp')
    .controller('ROLE_TARIFFDetailController', function ($scope, $rootScope, $stateParams, entity, ROLE_TARIFF) {
        $scope.rOLE_TARIFF = entity;
        $scope.load = function (id) {
            ROLE_TARIFF.get({id: id}, function(result) {
                $scope.rOLE_TARIFF = result;
            });
        };
        var unsubscribe = $rootScope.$on('forecastApp:rOLE_TARIFFUpdate', function(event, result) {
            $scope.rOLE_TARIFF = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
