(function() {
    'use strict';

    angular
        .module('amasApp')
        .controller('AmemberDialogController', AmemberDialogController);

    AmemberDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Amember', 'Association', 'Department', 'Role', 'Duty'];

    function AmemberDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Amember, Association, Department, Role, Duty) {
        var vm = this;

        vm.amember = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.associations = Association.query();
        vm.departments = Department.query();
        vm.roles = Role.query();
        vm.duties = Duty.query();
        
        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.amember.id !== null) {
                Amember.update(vm.amember, onSaveSuccess, onSaveError);
            } else {
                Amember.save(vm.amember, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('amasApp:amemberUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.membJoinDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
