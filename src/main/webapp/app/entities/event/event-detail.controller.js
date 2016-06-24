(function() {
    'use strict';

    angular
        .module('partyPaybackApp')
        .controller('EventDetailController', EventDetailController);

    EventDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Event', 'ExtandedUser', 'InitialPayment', 'PayBack'];

    function EventDetailController($scope, $rootScope, $stateParams, entity, Event, ExtandedUser, InitialPayment, PayBack) {
        var vm = this;

        vm.event = entity;

        var unsubscribe = $rootScope.$on('partyPaybackApp:eventUpdate', function(event, result) {
            vm.event = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
