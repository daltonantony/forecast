'use strict';

describe('Controller Tests', function() {

    describe('EmployeeBillingHours Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockEmployeeBillingHours, MockEmployee;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockEmployeeBillingHours = jasmine.createSpy('MockEmployeeBillingHours');
            MockEmployee = jasmine.createSpy('MockEmployee');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'EmployeeBillingHours': MockEmployeeBillingHours,
                'Employee': MockEmployee
            };
            createController = function() {
                $injector.get('$controller')("EmployeeBillingHoursDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'forecastApp:employeeBillingHoursUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
