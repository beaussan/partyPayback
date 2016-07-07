(function() {
    'use strict';

    angular
        .module('partyPaybackApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state', '$log'];

    function HomeController ($scope, Principal, LoginService, $state, $log) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.event = null;

        vm.participants = [];
        vm.newParticipant = null;
        vm.addParticipantForm = null;

        vm.login = LoginService.open;
        vm.register = register;

        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.appendParticipant = appendParticipant;

        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        getAccount();

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }
        function register () {
            $state.go('register');
        }


        function appendParticipant() {
            if (vm.newParticipant === null){
                return;
            }
            $log.debug(vm.newParticipant);
            vm.participants.push(vm.newParticipant);
            vm.newParticipant = null;
            vm.newParticipant.name = null;
            vm.newParticipant.email = null;
            vm.newParticipant.paiment = null;
            vm.addParticipantForm.name.$setPristine();
            vm.addParticipantForm.name.$setUntouched();
            vm.addParticipantForm.email.$setPristine();
            vm.addParticipantForm.email.$setUntouched();
            vm.addParticipantForm.paiment.$setPristine();
            vm.addParticipantForm.paiment.$setUntouched();
            vm.addParticipantForm.$setPristine();
            vm.addParticipantForm.$setUntouched();
        }



        function save () {
            vm.isSaving = true;
            // TODO modify here
            if (vm.event.id !== null) {
                Event.update(vm.event, onSaveSuccess, onSaveError);
            } else {
                Event.save(vm.event, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('partyPaybackApp:eventUpdate', result);
            //$uibModalInstance.close(result);
            vm.isSaving = false;
            // TODO $state.go('somehwere');
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.date = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
