(function() {
    'use strict';

    angular
        .module('amasApp')
        .controller('DutyDeleteController',DutyDeleteController);

    DutyDeleteController.$inject = ['$uibModalInstance', 'entity', 'Duty'];

    function DutyDeleteController($uibModalInstance, entity, Duty) {
        var vm = this;

        vm.duty = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Duty.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
