'use strict';

describe('Controller Tests', function() {

    describe('Event Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockEvent, MockExtandedUser, MockInitialPayment, MockPayBack;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockEvent = jasmine.createSpy('MockEvent');
            MockExtandedUser = jasmine.createSpy('MockExtandedUser');
            MockInitialPayment = jasmine.createSpy('MockInitialPayment');
            MockPayBack = jasmine.createSpy('MockPayBack');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Event': MockEvent,
                'ExtandedUser': MockExtandedUser,
                'InitialPayment': MockInitialPayment,
                'PayBack': MockPayBack
            };
            createController = function() {
                $injector.get('$controller')("EventDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'partyPaybackApp:eventUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
