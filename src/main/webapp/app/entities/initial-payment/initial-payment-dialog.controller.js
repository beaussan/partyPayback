(function() {
    'use strict';

    angular
        .module('partyPaybackApp')
        .controller('InitialPaymentDialogController', InitialPaymentDialogController);

    InitialPaymentDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'InitialPayment', 'ExtandedUser', 'Event'];

    function InitialPaymentDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, InitialPayment, ExtandedUser, Event) {
        var vm = this;

        vm.initialPayment = entity;
        vm.clear = clear;
        vm.save = save;
        vm.extandedusers = ExtandedUser.query();
        vm.events = Event.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.initialPayment.id !== null) {
                InitialPayment.update(vm.initialPayment, onSaveSuccess, onSaveError);
            } else {
                InitialPayment.save(vm.initialPayment, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('partyPaybackApp:initialPaymentUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
