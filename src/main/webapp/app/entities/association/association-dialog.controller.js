(function() {
    'use strict';

    angular
        .module('amasApp')
        .controller('AssociationDialogController', AssociationDialogController);

    AssociationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Association', 'Department', 'Activity', 'Property', 'Amember', 'User'];

    function AssociationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Association, Department, Activity, Property, Amember, User) {
        var vm = this;

        vm.association = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.departments = Department.query();
        vm.activities = Activity.query();
        vm.properties = Property.query();
        vm.amembers = Amember.query();
        vm.users = User.query();

        
        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.association.id !== null) {
                Association.update(vm.association, onSaveSuccess, onSaveError);
            } else {
                Association.save(vm.association, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('amasApp:associationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.assoFoundDate = false;

        vm.setAssoImage = function ($file, association) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        association.assoImage = base64Data;
                        association.assoImageContentType = $file.type;
                    });
                });
            }
        };

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
