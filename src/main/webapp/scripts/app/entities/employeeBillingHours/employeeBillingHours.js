'use strict';

angular.module('forecastApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('employeeBillingHours', {
                parent: 'entity',
                url: '/employeeBillingHourss',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Employee Billing Hours'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/employeeBillingHours/employeeBillingHourss.html',
                        controller: 'EmployeeBillingHoursController'
                    }
                },
                resolve: {
                }
            })
            .state('employeeBillingHours.detail', {
                parent: 'entity',
                url: '/employeeBillingHours/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'EmployeeBillingHours'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/employeeBillingHours/employeeBillingHours-detail.html',
                        controller: 'EmployeeBillingHoursDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'EmployeeBillingHours', function($stateParams, EmployeeBillingHours) {
                        return EmployeeBillingHours.get({id : $stateParams.id});
                    }]
                }
            })
            .state('employeeBillingHours.new', {
                parent: 'entity',
                url: '/employeeBillingHoursComingMonths/{empId}',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/employeeBillingHours/employeeBillingHours-dialog.html',
                        controller: 'EmployeeBillingHoursDialogController',
                        size: 'lg',
                        resolve: {
                            /*entity: ['$stateParams', 'EmployeeBillingHours', 'EmployeeBillingHoursForComingMonths', function($stateParams, EmployeeBillingHours, EmployeeBillingHoursForComingMonths) {
                                return EmployeeBillingHoursForComingMonths.get({empId : $stateParams.empId});
                            }]*/
                        }
                    }).result.then(function(result) {
                        $state.go('employeeBillingHours', null, { reload: true });
                    }, function() {
                        $state.go('employeeBillingHours');
                    })
                }]
            })
            .state('employeeBillingHours.edit', {
                parent: 'employeeBillingHours',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/employeeBillingHours/employeeBillingHours-dialog.html',
                        controller: 'EmployeeBillingHoursDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['EmployeeBillingHours', function(EmployeeBillingHours) {
                                return EmployeeBillingHours.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('employeeBillingHours', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('employeeBillingHours.delete', {
                parent: 'employeeBillingHours',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/employeeBillingHours/employeeBillingHours-delete-dialog.html',
                        controller: 'EmployeeBillingHoursDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['EmployeeBillingHours', function(EmployeeBillingHours) {
                                return EmployeeBillingHours.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('employeeBillingHours', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
