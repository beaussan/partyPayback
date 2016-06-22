'use strict';

describe('Controller Tests', function() {

    describe('PayBack Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPayBack, MockExtandedUser, MockEvent;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPayBack = jasmine.createSpy('MockPayBack');
            MockExtandedUser = jasmine.createSpy('MockExtandedUser');
            MockEvent = jasmine.createSpy('MockEvent');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'PayBack': MockPayBack,
                'ExtandedUser': MockExtandedUser,
                'Event': MockEvent
            };
            createController = function() {
                $injector.get('$controller')("PayBackDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'partyPaybackApp:payBackUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
