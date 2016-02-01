'use strict';

describe('Controller Tests', function() {

    describe('ROLE_TARIFF Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockROLE_TARIFF;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockROLE_TARIFF = jasmine.createSpy('MockROLE_TARIFF');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'ROLE_TARIFF': MockROLE_TARIFF
            };
            createController = function() {
                $injector.get('$controller')("ROLE_TARIFFDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'forecastApp:rOLE_TARIFFUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
