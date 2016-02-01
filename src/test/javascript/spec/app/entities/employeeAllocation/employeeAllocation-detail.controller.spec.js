'use strict';

describe('Controller Tests', function() {

    describe('EmployeeAllocation Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockEmployeeAllocation, MockEmployee;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockEmployeeAllocation = jasmine.createSpy('MockEmployeeAllocation');
            MockEmployee = jasmine.createSpy('MockEmployee');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'EmployeeAllocation': MockEmployeeAllocation,
                'Employee': MockEmployee
            };
            createController = function() {
                $injector.get('$controller')("EmployeeAllocationDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'forecastApp:employeeAllocationUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
