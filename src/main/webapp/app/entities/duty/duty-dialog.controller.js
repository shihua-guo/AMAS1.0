(function() {
    'use strict';

    angular
        .module('amasApp')
        .controller('DutyDialogController', DutyDialogController);

    DutyDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'DataUtils', 'entity', 'Duty', 'Department', 'Amember'];

    function DutyDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, DataUtils, entity, Duty, Department, Amember) {
        var vm = this;

        vm.duty = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.departments = Department.query({filter: 'duty-is-null'});
        $q.all([vm.duty.$promise, vm.departments.$promise]).then(function() {
            if (!vm.duty.department || !vm.duty.department.id) {
                return $q.reject();
            }
            return Department.get({id : vm.duty.department.id}).$promise;
        }).then(function(department) {
            vm.departments.push(department);
        });
        vm.amembers = Amember.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.duty.id !== null) {
                Duty.update(vm.duty, onSaveSuccess, onSaveError);
            } else {
                Duty.save(vm.duty, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('amasApp:dutyUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
