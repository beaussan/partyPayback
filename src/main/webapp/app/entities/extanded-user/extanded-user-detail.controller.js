(function() {
    'use strict';

    angular
        .module('partyPaybackApp')
        .controller('ExtandedUserDetailController', ExtandedUserDetailController);

    ExtandedUserDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'ExtandedUser', 'PayBack', 'Event', 'InitialPayment'];

    function ExtandedUserDetailController($scope, $rootScope, $stateParams, entity, ExtandedUser, PayBack, Event, InitialPayment) {
        var vm = this;

        vm.extandedUser = entity;

        var unsubscribe = $rootScope.$on('partyPaybackApp:extandedUserUpdate', function(event, result) {
            vm.extandedUser = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
