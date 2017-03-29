(function() {
    'use strict';

    angular
        .module('amasApp')
        .controller('AmemberDetailController', AmemberDetailController);

    AmemberDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Amember', 'Association', 'Department', 'Role', 'Duty'];

    function AmemberDetailController($scope, $rootScope, $stateParams, previousState, entity, Amember, Association, Department, Role, Duty) {
        var vm = this;

        vm.amember = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('amasApp:amemberUpdate', function(event, result) {
            vm.amember = result;
        });
        $scope.$on('$destroy', unsubscribe);
        
    }
})();
