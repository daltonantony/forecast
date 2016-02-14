'use strict';

describe('Controller Tests', function() {

    describe('EmployeeHolidays Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockEmployeeHolidays, MockEmployeeBillingHours;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockEmployeeHolidays = jasmine.createSpy('MockEmployeeHolidays');
            MockEmployeeBillingHours = jasmine.createSpy('MockEmployeeBillingHours');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'EmployeeHolidays': MockEmployeeHolidays,
                'EmployeeBillingHours': MockEmployeeBillingHours
            };
            createController = function() {
                $injector.get('$controller')("EmployeeHolidaysDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'forecastApp:employeeHolidaysUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
