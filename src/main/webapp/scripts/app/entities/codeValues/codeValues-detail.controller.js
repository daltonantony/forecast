'use strict';

angular.module('forecastApp')
    .controller('CodeValuesDetailController', function ($scope, $rootScope, $stateParams, entity, CodeValues) {
        $scope.codeValues = entity;
        $scope.load = function (id) {
            CodeValues.get({id: id}, function(result) {
                $scope.codeValues = result;
            });
        };
        var unsubscribe = $rootScope.$on('forecastApp:codeValuesUpdate', function(event, result) {
            $scope.codeValues = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
