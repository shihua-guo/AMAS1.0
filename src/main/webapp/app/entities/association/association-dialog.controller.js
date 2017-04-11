(function() {
    'use strict';

    angular
        .module('amasApp')
        .controller('AssociationDialogController', AssociationDialogController)
        .directive('assonameAvailable',assonameAvailable);

    AssociationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Association', 'Department', 'Activity', 'Property', 'Amember'];

    function AssociationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Association, Department, Activity, Property, Amember) {
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

    assonameAvailable.$inject = ['$timeout','$q','$http'];
    function assonameAvailable($timeout, $q, $http) {
      return {
        restrict: 'AE',
        require: 'ngModel',
        link: function(scope, elm, attr, model) { 
          model.$asyncValidators.usernameExists = function(modelValue, viewValue) { /*
            return $http.get('api/getAssoIdAndName').then(function(res){+
                console.log(res.data);
                console.log(model.$$rawModelValue);
                var ifExist = false;
                for (var i = res.data.length - 1; i >= 0; i--) {
                    console.log(res.data[i].assoName);
                    if (model.$$rawModelValue == res.data[i].assoName) {
                        ifExist = true;
                        break;
                    }
                }
                console.log(ifExist);
              $timeout(function(){
                model.$setValidity('usernameExists', ifExist); 
              }, 1000);
            }); 
            */
            var exist = false;
            var def = $q.defer();
                    $timeout(function() {
                    $http.get('api/getAssoIdAndName').then(function(res){
                        for (var i = res.data.length - 1; i >= 0; i--) {
                            console.log(res.data[i].assoName);
                            console.log(model.$$rawModelValue);

                            if (model.$$rawModelValue == res.data[i].assoName) {
                                exist = true;
                                console.log("dsdsddsadsdds")
                                break;
                            }
                        }
                    })
                    console.log(exist);
                      // Mock a delayed response
                      if (exist) {
                        // The username is available
                        def.reject();
                      } else {
                        def.resolve();
                      }

                    });

                    return def.promise;
            
          };
        }
      } 
    }
})();
