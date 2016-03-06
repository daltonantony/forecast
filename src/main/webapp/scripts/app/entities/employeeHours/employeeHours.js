'use strict';

angular.module('forecastApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('employeeHours', {
                parent: 'entity',
                url: '/employeeHourss',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'EmployeeHourss'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/employeeHours/employeeHourss.html',
                        controller: 'EmployeeHoursController'
                    }
                },
                resolve: {
                }
            })
            .state('employeeHoursAdmin', {
                parent: 'entity',
                url: '/employeeHoursAdmin',
                data: {
                    authorities: ['ROLE_ADMIN'],
                    pageTitle: 'Employee Hours Administration'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/employeeHours/employeeHoursAdmin.html',
                        controller: 'EmployeeHoursController'
                    }
                },
                resolve: {
                }
            })
            .state('employeeHours.detail', {
                parent: 'entity',
                url: '/employeeHours/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'EmployeeHours'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/employeeHours/employeeHours-detail.html',
                        controller: 'EmployeeHoursDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'EmployeeHours', function($stateParams, EmployeeHours) {
                        return EmployeeHours.get({id : $stateParams.id});
                    }]
                }
            })
            .state('employeeHours.new', {
                parent: 'employeeHours',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/employeeHours/employeeHours-dialog.html',
                        controller: 'EmployeeHoursDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    holidays: null,
                                    createdDate: null,
                                    forecastDate: null,
                                    lastChangedDate: null,
                                    lastChangedBy: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('employeeHours', null, { reload: true });
                    }, function() {
                        $state.go('employeeHours');
                    })
                }]
            })
            .state('employeeHours.edit', {
                parent: 'employeeHours',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/employeeHours/employeeHours-dialog.html',
                        controller: 'EmployeeHoursDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['EmployeeHours', function(EmployeeHours) {
                                return EmployeeHours.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('employeeHours', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('employeeHours.delete', {
                parent: 'employeeHours',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/employeeHours/employeeHours-delete-dialog.html',
                        controller: 'EmployeeHoursDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['EmployeeHours', function(EmployeeHours) {
                                return EmployeeHours.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('employeeHours', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
