'use strict';

angular.module('forecastApp')
    .controller('NavbarController', function ($scope, $location, $state, Auth, Principal, ENV, UserDetails) {
        $scope.isAuthenticated = Principal.isAuthenticated;
        $scope.$state = $state;
        $scope.inProduction = ENV === 'prod';
        $scope.user = UserDetails.get();

        $scope.logout = function () {
            Auth.logout();
            $state.go('home');
        };
    });
