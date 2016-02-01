'use strict';

angular.module('forecastApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('codeValues', {
                parent: 'entity',
                url: '/codeValuess',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'CodeValuess'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/codeValues/codeValuess.html',
                        controller: 'CodeValuesController'
                    }
                },
                resolve: {
                }
            })
            .state('codeValues.detail', {
                parent: 'entity',
                url: '/codeValues/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'CodeValues'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/codeValues/codeValues-detail.html',
                        controller: 'CodeValuesDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'CodeValues', function($stateParams, CodeValues) {
                        return CodeValues.get({id : $stateParams.id});
                    }]
                }
            })
            .state('codeValues.new', {
                parent: 'codeValues',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/codeValues/codeValues-dialog.html',
                        controller: 'CodeValuesDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    codeType: null,
                                    codeValue: null,
                                    effectiveDate: null,
                                    expiryDate: null,
                                    lastChangedBy: null,
                                    lastChangedDate: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('codeValues', null, { reload: true });
                    }, function() {
                        $state.go('codeValues');
                    })
                }]
            })
            .state('codeValues.edit', {
                parent: 'codeValues',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/codeValues/codeValues-dialog.html',
                        controller: 'CodeValuesDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['CodeValues', function(CodeValues) {
                                return CodeValues.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('codeValues', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('codeValues.delete', {
                parent: 'codeValues',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/codeValues/codeValues-delete-dialog.html',
                        controller: 'CodeValuesDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['CodeValues', function(CodeValues) {
                                return CodeValues.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('codeValues', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
