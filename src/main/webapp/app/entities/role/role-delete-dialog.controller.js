(function() {
    'use strict';

    angular
        .module('amasApp')
        .controller('RoleDeleteController',RoleDeleteController);

    RoleDeleteController.$inject = ['$uibModalInstance', 'entity', 'Role'];

    function RoleDeleteController($uibModalInstance, entity, Role) {
        var vm = this;

        vm.role = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Role.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
