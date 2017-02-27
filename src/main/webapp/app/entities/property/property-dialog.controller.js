(function() {
    'use strict';

    angular
        .module('amasApp')
        .controller('PropertyDialogController', PropertyDialogController);

    PropertyDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Property', 'Association'];

    function PropertyDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Property, Association) {
        var vm = this;

        vm.property = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.associations = Association.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.property.id !== null) {
                Property.update(vm.property, onSaveSuccess, onSaveError);
            } else {
                Property.save(vm.property, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('amasApp:propertyUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.propBuyDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
