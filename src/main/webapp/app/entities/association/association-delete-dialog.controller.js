(function() {
    'use strict';

    angular
        .module('amasApp')
        .controller('AssociationDeleteController',AssociationDeleteController);

    AssociationDeleteController.$inject = ['$uibModalInstance', 'entity', 'Association'];

    function AssociationDeleteController($uibModalInstance, entity, Association) {
        var vm = this;

        vm.association = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Association.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
