(function() {
    'use strict';

    angular
        .module('amasApp')
        .controller('AmemberDetailController', AmemberDetailController);

    AmemberDetailController.$inject = ['$scope', '$rootScope', '$stateParams', /*'previousState',*/ 'entity', 'Amember', 'Association', 'Department', 'Role', 'Duty','$uibModalInstance'];

    function AmemberDetailController($scope, $rootScope, $stateParams, /*previousState,*/ entity, Amember, Association, Department, Role, Duty,$uibModalInstance) {
        var vm = this;

        vm.amember = entity;
        // vm.previousState = previousState.name;
        vm.clear = clear;
        var unsubscribe = $rootScope.$on('amasApp:amemberUpdate', function(event, result) {
            vm.amember = result;
        });
        $scope.$on('$destroy', unsubscribe);
        
        function clear () { 
            $uibModalInstance.dismiss('cancel');
        }
    }
})();
