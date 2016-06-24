(function() {
    'use strict';

    angular
        .module('partyPaybackApp')
        .controller('PayBackDetailController', PayBackDetailController);

    PayBackDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'PayBack', 'ExtandedUser', 'Event'];

    function PayBackDetailController($scope, $rootScope, $stateParams, entity, PayBack, ExtandedUser, Event) {
        var vm = this;

        vm.payBack = entity;

        var unsubscribe = $rootScope.$on('partyPaybackApp:payBackUpdate', function(event, result) {
            vm.payBack = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
