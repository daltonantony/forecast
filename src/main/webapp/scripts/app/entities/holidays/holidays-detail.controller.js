'use strict';

angular.module('forecastApp')
    .controller('HolidaysDetailController', function ($scope, $rootScope, $stateParams, entity, Holidays) {
        $scope.holidays = entity;
        $scope.load = function (id) {
            Holidays.get({id: id}, function(result) {
                $scope.holidays = result;
            });
        };
        var unsubscribe = $rootScope.$on('forecastApp:holidaysUpdate', function(event, result) {
            $scope.holidays = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
