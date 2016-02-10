'use strict';

angular.module('forecastApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('rOLE_TARIFF', {
                parent: 'entity',
                url: '/rOLE_TARIFFs',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Role Tariffs'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/rOLE_TARIFF/rOLE_TARIFFs.html',
                        controller: 'ROLE_TARIFFController'
                    }
                },
                resolve: {
                }
            })
            .state('rOLE_TARIFF.detail', {
                parent: 'entity',
                url: '/rOLE_TARIFF/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'ROLE_TARIFF'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/rOLE_TARIFF/rOLE_TARIFF-detail.html',
                        controller: 'ROLE_TARIFFDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'ROLE_TARIFF', function($stateParams, ROLE_TARIFF) {
                        return ROLE_TARIFF.get({id : $stateParams.id});
                    }]
                }
            })
            .state('rOLE_TARIFF.new', {
                parent: 'rOLE_TARIFF',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/rOLE_TARIFF/rOLE_TARIFF-dialog.html',
                        controller: 'ROLE_TARIFFDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    role: null,
                                    tariff: null,
                                    location: null,
                                    effectiveDate: null,
                                    expiryDate: null,
                                    lastChangedDate: null,
                                    lastChangedBy: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('rOLE_TARIFF', null, { reload: true });
                    }, function() {
                        $state.go('rOLE_TARIFF');
                    })
                }]
            })
            .state('rOLE_TARIFF.edit', {
                parent: 'rOLE_TARIFF',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/rOLE_TARIFF/rOLE_TARIFF-dialog.html',
                        controller: 'ROLE_TARIFFDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['ROLE_TARIFF', function(ROLE_TARIFF) {
                                return ROLE_TARIFF.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('rOLE_TARIFF', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('rOLE_TARIFF.delete', {
                parent: 'rOLE_TARIFF',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/rOLE_TARIFF/rOLE_TARIFF-delete-dialog.html',
                        controller: 'ROLE_TARIFFDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['ROLE_TARIFF', function(ROLE_TARIFF) {
                                return ROLE_TARIFF.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('rOLE_TARIFF', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
