(function() {
    'use strict';

    angular
        .module('partyPaybackApp')
        .controller('PayBackDialogController', PayBackDialogController);

    PayBackDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'PayBack', 'ExtandedUser', 'Event'];

    function PayBackDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, PayBack, ExtandedUser, Event) {
        var vm = this;

        vm.payBack = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
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
            if (vm.payBack.id !== null) {
                PayBack.update(vm.payBack, onSaveSuccess, onSaveError);
            } else {
                PayBack.save(vm.payBack, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('partyPaybackApp:payBackUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.timestamp = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
