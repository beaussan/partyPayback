(function() {
    'use strict';

    angular
        .module('partyPaybackApp')
        .controller('InitialPaymentDetailController', InitialPaymentDetailController);

    InitialPaymentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'InitialPayment', 'ExtandedUser', 'Event'];

    function InitialPaymentDetailController($scope, $rootScope, $stateParams, entity, InitialPayment, ExtandedUser, Event) {
        var vm = this;

        vm.initialPayment = entity;

        var unsubscribe = $rootScope.$on('partyPaybackApp:initialPaymentUpdate', function(event, result) {
            vm.initialPayment = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
