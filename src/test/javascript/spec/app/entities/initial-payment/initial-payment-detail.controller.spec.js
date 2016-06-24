'use strict';

describe('Controller Tests', function() {

    describe('InitialPayment Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockInitialPayment, MockExtandedUser, MockEvent;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockInitialPayment = jasmine.createSpy('MockInitialPayment');
            MockExtandedUser = jasmine.createSpy('MockExtandedUser');
            MockEvent = jasmine.createSpy('MockEvent');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'InitialPayment': MockInitialPayment,
                'ExtandedUser': MockExtandedUser,
                'Event': MockEvent
            };
            createController = function() {
                $injector.get('$controller')("InitialPaymentDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'partyPaybackApp:initialPaymentUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
