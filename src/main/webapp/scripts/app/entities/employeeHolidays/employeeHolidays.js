'use strict';

angular.module('forecastApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('employeeHolidays', {
                parent: 'entity',
                url: '/employeeHolidayss',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'EmployeeHolidayss'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/employeeHolidays/employeeHolidayss.html',
                        controller: 'EmployeeHolidaysController'
                    }
                },
                resolve: {
                }
            })
            .state('employeeHolidays.detail', {
                parent: 'entity',
                url: '/employeeHolidays/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'EmployeeHolidays'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/employeeHolidays/employeeHolidays-detail.html',
                        controller: 'EmployeeHolidaysDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'EmployeeHolidays', function($stateParams, EmployeeHolidays) {
                        return EmployeeHolidays.get({id : $stateParams.id});
                    }]
                }
            })
            .state('employeeHolidays.new', {
                parent: 'employeeHolidays',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/employeeHolidays/employeeHolidays-dialog.html',
                        controller: 'EmployeeHolidaysDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    week1: null,
                                    week2: null,
                                    week3: null,
                                    week4: null,
                                    week5: null,
                                    lastChangedDate: null,
                                    lastChangedBy: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('employeeHolidays', null, { reload: true });
                    }, function() {
                        $state.go('employeeHolidays');
                    })
                }]
            })
            .state('employeeHolidays.edit', {
                parent: 'employeeHolidays',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/employeeHolidays/employeeHolidays-dialog.html',
                        controller: 'EmployeeHolidaysDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['EmployeeHolidays', function(EmployeeHolidays) {
                                return EmployeeHolidays.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('employeeHolidays', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('employeeHolidays.delete', {
                parent: 'employeeHolidays',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/employeeHolidays/employeeHolidays-delete-dialog.html',
                        controller: 'EmployeeHolidaysDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['EmployeeHolidays', function(EmployeeHolidays) {
                                return EmployeeHolidays.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('employeeHolidays', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
