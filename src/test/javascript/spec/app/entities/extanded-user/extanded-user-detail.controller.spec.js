'use strict';

describe('Controller Tests', function() {

    describe('ExtandedUser Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockExtandedUser, MockPayBack, MockEvent, MockInitialPayment;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockExtandedUser = jasmine.createSpy('MockExtandedUser');
            MockPayBack = jasmine.createSpy('MockPayBack');
            MockEvent = jasmine.createSpy('MockEvent');
            MockInitialPayment = jasmine.createSpy('MockInitialPayment');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'ExtandedUser': MockExtandedUser,
                'PayBack': MockPayBack,
                'Event': MockEvent,
                'InitialPayment': MockInitialPayment
            };
            createController = function() {
                $injector.get('$controller')("ExtandedUserDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'partyPaybackApp:extandedUserUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
