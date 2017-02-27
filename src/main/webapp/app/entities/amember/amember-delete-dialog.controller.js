(function() {
    'use strict';

    angular
        .module('amasApp')
        .controller('AmemberDeleteController',AmemberDeleteController);

    AmemberDeleteController.$inject = ['$uibModalInstance', 'entity', 'Amember'];

    function AmemberDeleteController($uibModalInstance, entity, Amember) {
        var vm = this;

        vm.amember = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Amember.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
