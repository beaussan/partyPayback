(function() {
    'use strict';

    angular
        .module('partyPaybackApp')
        .controller('EventDetailController', EventDetailController);

    EventDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Event', 'ExtandedUser', 'InitialPayment', 'PayBack'];

    function EventDetailController($scope, $rootScope, $stateParams, entity, Event, ExtandedUser, InitialPayment, PayBack) {
        var vm = this;

        vm.event = entity;
        vm.initials = undefined;
        vm.paybacks = undefined;

        if (entity.id){
            vm.initials = InitialPayment.byEvent({ eventId:entity.id });
            vm.paybacks = PayBack.byEvent({ eventId:entity.id });
        }

        var unsubscribe = $rootScope.$on('partyPaybackApp:eventUpdate', function(event, result) {
            vm.event = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
