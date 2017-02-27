(function() {
    'use strict';

    angular
        .module('amasApp')
        .controller('DepartmentDialogController', DepartmentDialogController);

    DepartmentDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Department', 'Duty', 'Association', 'Amember'];

    function DepartmentDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Department, Duty, Association, Amember) {
        var vm = this;

        vm.department = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.duties = Duty.query();
        vm.associations = Association.query();
        vm.amembers = Amember.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.department.id !== null) {
                Department.update(vm.department, onSaveSuccess, onSaveError);
            } else {
                Department.save(vm.department, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('amasApp:departmentUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setDeptImage = function ($file, department) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        department.deptImage = base64Data;
                        department.deptImageContentType = $file.type;
                    });
                });
            }
        };

    }
})();
