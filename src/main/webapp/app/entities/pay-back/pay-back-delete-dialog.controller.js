(function() {
    'use strict';

    angular
        .module('partyPaybackApp')
        .controller('PayBackDeleteController',PayBackDeleteController);

    PayBackDeleteController.$inject = ['$uibModalInstance', 'entity', 'PayBack'];

    function PayBackDeleteController($uibModalInstance, entity, PayBack) {
        var vm = this;

        vm.payBack = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            PayBack.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
