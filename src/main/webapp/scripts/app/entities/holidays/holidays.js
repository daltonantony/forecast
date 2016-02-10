'use strict';

angular.module('forecastApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('holidays', {
                parent: 'entity',
                url: '/holidayss',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Holidays'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/holidays/holidayss.html',
                        controller: 'HolidaysController'
                    }
                },
                resolve: {
                }
            })
            .state('holidays.detail', {
                parent: 'entity',
                url: '/holidays/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Holidays'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/holidays/holidays-detail.html',
                        controller: 'HolidaysDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Holidays', function($stateParams, Holidays) {
                        return Holidays.get({id : $stateParams.id});
                    }]
                }
            })
            .state('holidays.new', {
                parent: 'holidays',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/holidays/holidays-dialog.html',
                        controller: 'HolidaysDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    startDate: null,
                                    endDate: null,
                                    location: null,
                                    lastChangedBy: null,
                                    lastChangedDt: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('holidays', null, { reload: true });
                    }, function() {
                        $state.go('holidays');
                    })
                }]
            })
            .state('holidays.edit', {
                parent: 'holidays',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/holidays/holidays-dialog.html',
                        controller: 'HolidaysDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Holidays', function(Holidays) {
                                return Holidays.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('holidays', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('holidays.delete', {
                parent: 'holidays',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/holidays/holidays-delete-dialog.html',
                        controller: 'HolidaysDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Holidays', function(Holidays) {
                                return Holidays.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('holidays', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
