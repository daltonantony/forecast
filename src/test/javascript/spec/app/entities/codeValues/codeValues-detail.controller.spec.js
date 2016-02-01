'use strict';

describe('Controller Tests', function() {

    describe('CodeValues Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockCodeValues;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockCodeValues = jasmine.createSpy('MockCodeValues');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'CodeValues': MockCodeValues
            };
            createController = function() {
                $injector.get('$controller')("CodeValuesDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'forecastApp:codeValuesUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
