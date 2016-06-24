(function() {
    'use strict';

    angular
        .module('partyPaybackApp')
        .controller('ExtandedUserDialogController', ExtandedUserDialogController);

    ExtandedUserDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'ExtandedUser', 'User', 'PayBack', 'Event', 'InitialPayment'];

    function ExtandedUserDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, ExtandedUser, User, PayBack, Event, InitialPayment) {
        var vm = this;

        vm.extandedUser = entity;
        vm.clear = clear;
        vm.save = save;
        vm.users = User.query();
        vm.paybacks = PayBack.query();
        vm.events = Event.query();
        vm.initialpayments = InitialPayment.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.extandedUser.id !== null) {
                ExtandedUser.update(vm.extandedUser, onSaveSuccess, onSaveError);
            } else {
                ExtandedUser.save(vm.extandedUser, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('partyPaybackApp:extandedUserUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
