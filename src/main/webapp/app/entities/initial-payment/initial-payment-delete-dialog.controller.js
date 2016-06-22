(function() {
    'use strict';

    angular
        .module('partyPaybackApp')
        .controller('InitialPaymentDeleteController',InitialPaymentDeleteController);

    InitialPaymentDeleteController.$inject = ['$uibModalInstance', 'entity', 'InitialPayment'];

    function InitialPaymentDeleteController($uibModalInstance, entity, InitialPayment) {
        var vm = this;

        vm.initialPayment = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            InitialPayment.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
