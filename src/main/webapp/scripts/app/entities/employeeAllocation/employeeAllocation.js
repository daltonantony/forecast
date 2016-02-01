'use strict';

angular.module('forecastApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('employeeAllocation', {
                parent: 'entity',
                url: '/employeeAllocations',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'EmployeeAllocations'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/employeeAllocation/employeeAllocations.html',
                        controller: 'EmployeeAllocationController'
                    }
                },
                resolve: {
                }
            })
            .state('employeeAllocation.detail', {
                parent: 'entity',
                url: '/employeeAllocation/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'EmployeeAllocation'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/employeeAllocation/employeeAllocation-detail.html',
                        controller: 'EmployeeAllocationDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'EmployeeAllocation', function($stateParams, EmployeeAllocation) {
                        return EmployeeAllocation.get({id : $stateParams.id});
                    }]
                }
            })
            .state('employeeAllocation.new', {
                parent: 'employeeAllocation',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/employeeAllocation/employeeAllocation-dialog.html',
                        controller: 'EmployeeAllocationDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    project: null,
                                    startDate: null,
                                    endDate: null,
                                    location: null,
                                    allocation: null,
                                    lastChangedDate: null,
                                    lastChangedby: null,
                                    role: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('employeeAllocation', null, { reload: true });
                    }, function() {
                        $state.go('employeeAllocation');
                    })
                }]
            })
            .state('employeeAllocation.edit', {
                parent: 'employeeAllocation',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/employeeAllocation/employeeAllocation-dialog.html',
                        controller: 'EmployeeAllocationDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['EmployeeAllocation', function(EmployeeAllocation) {
                                return EmployeeAllocation.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('employeeAllocation', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('employeeAllocation.delete', {
                parent: 'employeeAllocation',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/employeeAllocation/employeeAllocation-delete-dialog.html',
                        controller: 'EmployeeAllocationDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['EmployeeAllocation', function(EmployeeAllocation) {
                                return EmployeeAllocation.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('employeeAllocation', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
