'use strict';

describe('Controller Tests', function() {

    describe('EmployeeHours Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockEmployeeHours, MockEmployee;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockEmployeeHours = jasmine.createSpy('MockEmployeeHours');
            MockEmployee = jasmine.createSpy('MockEmployee');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'EmployeeHours': MockEmployeeHours,
                'Employee': MockEmployee
            };
            createController = function() {
                $injector.get('$controller')("EmployeeHoursDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'forecastApp:employeeHoursUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
