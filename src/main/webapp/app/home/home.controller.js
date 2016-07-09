(function() {
    'use strict';

    angular
        .module('partyPaybackApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state', '$log','Event', 'ExtandedUser', 'InitialPayment'];

    function HomeController ($scope, Principal, LoginService, $state, $log, Event, ExtandedUser, InitialPayment) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.event = null;

        vm.participants = [];
        vm.participantsSaved = [];
        vm.ownersOfEvent = [];
        vm.savedUserNmb = 0;

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
        }



        function save () {
            vm.isSaving = true;
            vm.savedUserNmb = 0;
            // TODO modify here
            angular.forEach(vm.participants, function(value) {
                ExtandedUser.save(value, onSaveSuccessUser(result, original), onSaveError);

            });
            while (vm.savedUserNmb != vm.participants.length){}
            Event.save(vm.event, onSaveSuccess, onSaveError);

        }

        function onSaveSuccessUser(result, original) {
            result.paiment = original.paiment;
            vm.participantsSaved.push(result);
            vm.savedUserNmb++;
        }

        function onSaveSuccess (result) {
            angular.forEach(vm.participantsSaved, function (value) {

            });
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
